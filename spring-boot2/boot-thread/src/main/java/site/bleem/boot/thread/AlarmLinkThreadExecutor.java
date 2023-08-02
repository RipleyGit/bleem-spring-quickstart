package site.bleem.boot.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.bestway.broadcast.data.dto.EquipCacheDto;
import com.bestway.broadcast.data.dto.EquipDto;
import com.bestway.broadcast.data.dto.SetBrdInfoDto;
import com.bestway.broadcast.data.enums.EquipEnum;
import com.bestway.broadcast.data.enums.FuctionTypeEnum;
import com.bestway.broadcast.data.remote.BrdDataFeignClient;
import com.bestway.broadcast.data.vo.SetOrderResultVO;
import com.bestway.broadcast.main.config.BroadcastMainBaseConfig;
import com.bestway.broadcast.main.entity.main.LinkExecTask;
import com.bestway.broadcast.main.enums.LinkEnum;
import com.bestway.broadcast.main.service.brdEquipInfo.BrdEquipInfoService;
import com.bestway.broadcast.main.service.link.LinkExecTaskService;
import com.bestway.broadcast.main.utils.SpringContextUtil;
import com.bestway.kernel.base.constant.CacheConstants;
import com.bestway.kernel.base.util.R;
import com.bestway.kernel.base.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author yubangshui
 * @desc todo
 * @date 2023/7/26
 */
//@Component

@Slf4j
public class AlarmLinkThreadExecutor extends Thread {
    private static final int MAX_SIZE = 10;

    private String hid;

    private EquipDto equipDto;

    private static BrdDataFeignClient brdDataFeignClient;
    private static LinkExecTaskService linkExecTaskService;
    private static BrdEquipInfoService brdEquipInfoService;
    private static BroadcastMainBaseConfig broadcastMainBaseConfig;

    private static RedisService redisService;

    public AlarmLinkThreadExecutor(String hid) {
        this.hid = hid;
        setName("a_link_" + hid);
        brdDataFeignClient = SpringContextUtil.getBean(BrdDataFeignClient.class);
        linkExecTaskService = SpringContextUtil.getBean(LinkExecTaskService.class);
        brdEquipInfoService = SpringContextUtil.getBean(BrdEquipInfoService.class);
        redisService = SpringContextUtil.getBean(RedisService.class);
        broadcastMainBaseConfig = SpringContextUtil.getBean(BroadcastMainBaseConfig.class);

        EquipCacheDto equipFromRedis = brdEquipInfoService.getEquipFromRedis(hid);
        EquipDto dto = new EquipDto();
        dto.setIp(equipFromRedis.getIp());
        dto.setHid(hid);
        dto.setEquipType(equipFromRedis.getEquipType());
        if (equipFromRedis.getPort() != null) {
            dto.setPort(Integer.valueOf(equipFromRedis.getPort()));
        }
        this.equipDto = dto;

    }

    private ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue();
    //使用线程安全的跳表，保证元素的有序
    private ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, Integer>> concurrentSkipListMap = new ConcurrentSkipListMap<>();

    public ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, Integer>> getConcurrentSkipListMap() {
        return concurrentSkipListMap;
    }

    public ConcurrentLinkedQueue<String> getMessageQueue() {
        return messageQueue;
    }

    public void enqueue(String message, Long taskId, Integer times) {
        synchronized (this) {
            messageQueue.offer(message);
        }
    }
//    public void enqueue(String message, Long taskId, Integer times) {
//        synchronized (this) {
//            //查看消息是否存在
//            if (concurrentSkipListMap.containsKey(message)) {
//                ConcurrentSkipListMap<Long, Integer> skipListMap = concurrentSkipListMap.get(message);
//                //消息存在
//                if (skipListMap.containsKey(taskId)) {
//                    //taskid 相同则认为是同一个任务
//                    log.debug("重复消息进入，默认忽略");
//                    return;
//                }
//                if (times == 0) {
//                    //告警停止
//                    for (Long key : skipListMap.keySet()) {
//                        skipListMap.put(key, -1);//次数重置为0;
//                    }
//                    skipListMap.put(taskId, -1);
//                } else {
//                    skipListMap.put(taskId, times);
//                }
//                concurrentSkipListMap.put(message, skipListMap);//更新记录
//                return;
//            } else if (concurrentSkipListMap.size() >= MAX_SIZE) {
//                //队列已满，移除队首
//                String frist = messageQueue.poll();
//                concurrentSkipListMap.remove(frist);
//            }
//            ConcurrentSkipListMap<Long, Integer> taskMap = new ConcurrentSkipListMap<>();
//            taskMap.put(taskId, times);
//            concurrentSkipListMap.put(message, taskMap);
//            messageQueue.offer(message);//消息进入队列
//        }
//    }

    @Override
    public void run() {
        //队列不为空则继续运行
        while (!messageQueue.isEmpty()) {
            String message = messageQueue.poll();//出队运行
            String logMsg = "[" + hid + "]中消息[" + message + "]";
            LinkExecTask execTask = linkExecTaskService.queryOneMessageByExecStatus(LinkEnum.TASK_EXEC.WAIT.getCode(), hid, message);
            if (execTask == null) {
                continue;
            }
            if (execTask.getLinkAlarmType() == LinkEnum.EXEC_TYPE.END.getCode()) {
                AlarmLinkThreadManager.getInstance().removeOnSet(execTask.getLinkExecTaskId());
                linkExecTaskService.updateEndTask(execTask, message);
            } else if (execTask.getLinkRunCount() < execTask.getLinkExecCount()) {
                //需要播放的任务
                //构建消息下发命令
                SetBrdInfoDto infoDto = new SetBrdInfoDto();
                infoDto.setDto(equipDto);
                infoDto.setUuid(UuidUtils.generateUuid());
                infoDto.setFunctionType(FuctionTypeEnum.InfoType.THIRD_REL_ALARM.getCode());
                infoDto.setCycleCount(1);
                infoDto.setInfoContent(message);
                //下发
                SetOrderResultVO vo = null;
                try {
                    R<SetOrderResultVO> resultVOR = brdDataFeignClient.syncSettingEquipInfoPlayOrder(infoDto);
                    vo = resultVOR.getData();
                    log.info(logMsg + "播放ing");
                } catch (Exception e) {
                    String errorMsg = hid + "下发接口调用失败：" + e.getMessage();
                    log.error(errorMsg, e);
                    vo = new SetOrderResultVO();
                    vo.setExcuteResult(errorMsg);
                    vo.setIsSendSuccess(0);
                    vo.setIsExcuteSuccess(0);
                }
                Integer sleepTheWord = null;
                try {
                    sleepTheWord = message.length() / broadcastMainBaseConfig.getLinkExecSpeed();
                } catch (Exception e) {
                    log.error("停顿时间计算异常，默认1s", e);
                    sleepTheWord = 1;
                }
                Boolean isSuccess = false;
                if (vo.getIsSendSuccess() != null && vo.getIsSendSuccess() == 1 &&
                        vo.getIsExcuteSuccess() != null && vo.getIsExcuteSuccess() == 1
                ) {
                    isSuccess = true;
                    log.info(logMsg + "停顿:" + sleepTheWord);
                    waitFree(sleepTheWord);
                }else {
                    log.info(logMsg + "播放失败:" + vo.getExcuteResult());
                }
                //更新任务状态
                Integer linkRunCount = execTask.getLinkRunCount();
                linkRunCount++;
                execTask.setLinkRunCount(linkRunCount);//更新运行次数
                if (isSuccess) {
                    execTask.setLinkLastStatus(LinkEnum.TASK_EXEC.SUCCESS.getCode());
                } else if (!isSuccess && execTask.getLinkLastStatus() != LinkEnum.TASK_EXEC.SUCCESS.getCode()) {
                    execTask.setLinkLastStatus(LinkEnum.TASK_EXEC.FAIL.getCode());
                    execTask.setLinkRunResult(vo.getExcuteResult());
                } else {
                    log.info("不是第一次失败，不更新状态");
                }
                linkExecTaskService.updateRunTask(execTask);
            } else {
                //执行完成
                AlarmLinkThreadManager.getInstance().removeOnSet(execTask.getLinkExecTaskId());
                linkExecTaskService.updateEndTask(execTask);
            }
            messageQueue.offer(message);//进入队尾
        }
        //线程退出，同步管理器
        AlarmLinkThreadManager.getInstance().remove(hid);
    }

    //    @Override
    public void runBack() {
        //队列不为空则继续运行
        while (!messageQueue.isEmpty()) {
            String message = messageQueue.poll();//出队运行
            String logMsg = "[" + hid + "]中消息[" + message + "]";
            if (message == null) {
                continue;
            }
            if (!concurrentSkipListMap.containsKey(message)) {
                log.warn(logMsg + "在消息队列中存在，但任务中没有相关数据");
                continue;
            }

            ConcurrentSkipListMap<Long, Integer> skipListMap = concurrentSkipListMap.get(message);
            if (skipListMap == null || skipListMap.isEmpty()) {
                concurrentSkipListMap.remove(message);
                log.info(logMsg + ":队列为空！");
                continue;
            }

            Iterator<Map.Entry<Long, Integer>> iterator = skipListMap.entrySet().iterator();
            Map.Entry<Long, Integer> entry = null;
            while (iterator.hasNext()) {
                entry = iterator.next();
                Long taskId = entry.getKey();
                if (entry.getValue() < 0) {
                    log.info(logMsg + "任务被重置：" + taskId);
                    skipListMap.remove(taskId);//任务移出播放队列
                    AlarmLinkThreadManager.getInstance().removeOnSet(taskId);
                    //通知任务状态更新
                    linkExecTaskService.updateRunTaskStatus(taskId, entry.getValue(), null);
                    entry = null;
                } else {
                    break;
                }
            }

            if (entry == null) {
                //该消息不播放
                log.info(logMsg + "全部被重置，退出播放队列");
                concurrentSkipListMap.remove(message);
                continue;
            }

            waitFree();//等待设备空闲

            //构建消息下发命令
            SetBrdInfoDto infoDto = new SetBrdInfoDto();
            infoDto.setDto(equipDto);
            infoDto.setUuid(UuidUtils.generateUuid());
            infoDto.setFunctionType(FuctionTypeEnum.InfoType.THIRD_REL_ALARM.getCode());
            infoDto.setCycleCount(1);
            infoDto.setInfoContent(message);

            //下发
            SetOrderResultVO vo = null;
            try {
                R<SetOrderResultVO> resultVOR = brdDataFeignClient.syncSettingEquipInfoPlayOrder(infoDto);
                vo = resultVOR.getData();
                log.info(logMsg + "播放ing");
            } catch (Exception e) {
                String errorMsg = hid + "下发接口调用失败：" + e.getMessage();
                log.error(errorMsg, e);
                vo = new SetOrderResultVO();
                vo.setExcuteResult(errorMsg);
                vo.setIsSendSuccess(0);
                vo.setIsExcuteSuccess(0);
            }

            Integer sleepTheWord = null;
            try {
                sleepTheWord = message.length() / broadcastMainBaseConfig.getLinkExecSpeed();
                log.info(logMsg + "停顿:" + sleepTheWord);
            } catch (Exception e) {
                log.error("停顿时间计算异常，默认1s", e);
                sleepTheWord = 1;
            }
            //更新任务状态
            waitFree(sleepTheWord);//等待设备执行完成
            Long taskId = entry.getKey();
            Integer waitTimes = entry.getValue();
            waitTimes--;
            linkExecTaskService.updateRunTaskStatus(taskId, waitTimes, vo);
            if (waitTimes <= 0) {
                //播放完成
                AlarmLinkThreadManager.getInstance().removeOnSet(taskId);
                skipListMap.remove(taskId);
            } else {
                //更新次数
                skipListMap.put(taskId, waitTimes);
            }

            //判断该消息是否播放完成
            if (skipListMap.isEmpty()) {
                //播放完成提出队列
                concurrentSkipListMap.remove(message);
            } else {
                //再次入队
                messageQueue.offer(message);
            }
        }
        //线程退出，同步管理器
        AlarmLinkThreadManager.getInstance().remove(hid);
    }

    /**
     * 默认停顿1s
     */
    private void waitFree() {
        waitFree(1);
    }

    /**
     * 等待设备空闲
     */
    private void waitFree(Integer sleepSecond) {
        //等待设备空闲，再进入消息播放
        while (true) {
            //等待1s
            try {
                Thread.sleep(sleepSecond * 1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
            Map hmedGet = redisService.hmGet(CacheConstants.BRD_EQUIP_STATUS_KEY);
            Object equip = hmedGet.get(hid);
            if (ObjectUtils.isEmpty(equip)) {
                //todo
            }
            JSONObject json = (JSONObject) JSON.toJSON(equip);
            EquipCacheDto equipCacheDto = JSON.toJavaObject(json, EquipCacheDto.class);
            EquipEnum.WorkStatus workStatus = getByCode(equipCacheDto.getDevStatus());
            if (workStatus == null) {
                continue;
            }
            log.info("当前设备【" + hid + "】状态：" + workStatus.getDesc());
            if (workStatus == EquipEnum.WorkStatus.FRR) {
                //设备空闲状态,进入下一轮消息播报
                break;
            }

        }
    }


    /**
     * 根据code找到具体枚举描述
     *
     * @param code
     * @return
     */
    public static EquipEnum.WorkStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EquipEnum.WorkStatus workStatus : EquipEnum.WorkStatus.values()) {
            if (workStatus.getCode() == code) {
                return workStatus;
            }
        }
        return null;
    }


}
