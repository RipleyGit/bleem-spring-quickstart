package site.bleem.boot.socket.api;

import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Service;
import site.bleem.boot.socket.container.ContainerManager;
import site.bleem.boot.socket.data.CmdData;
import site.bleem.boot.socket.data.ConnectionEntity;
import site.bleem.boot.socket.enums.CmdResultCode;
import site.bleem.boot.socket.enums.ConnectProtocolType;
import site.bleem.boot.socket.enums.DispatchTaskType;
import site.bleem.boot.socket.task.DefaultServer2ClientCmdDispatchTCPTask;
import site.bleem.boot.socket.task.DefaultServer2ClientCmdDispatchUDPTask;
import site.bleem.boot.socket.task.DispatchTask;
import site.bleem.boot.socket.task.TaskDispatcher;
import sun.net.util.IPAddressUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnWebApplication
public class CmdControl {
    private static final Logger log = LoggerFactory.getLogger(CmdControl.class);
    @Autowired
    TaskDispatcher taskDispatcher;
    @Autowired
    ContainerManager containerManager;

    public CmdControl() {
    }

    public CmdResultCode send(CmdData cmdData) {
        if (!StringUtil.isNullOrEmpty(cmdData.getEquIdentity()) && !StringUtil.isNullOrEmpty(cmdData.getUuid())) {
            if (null != cmdData.getUdpConnectParam() && (!StringUtil.isNullOrEmpty(cmdData.getUdpConnectParam().getIp()) && !IPAddressUtil.isIPv4LiteralAddress(cmdData.getUdpConnectParam().getIp()) || cmdData.getUdpConnectParam().getPort() != null && (cmdData.getUdpConnectParam().getPort() < 0 || cmdData.getUdpConnectParam().getPort() > 65535))) {
                log.info("命令下发接口 cmdParam:{}, tUdpConnectParam 不合法 return:{}", JSONObject.toJSONString(cmdData), CmdResultCode.Fail_ParamError);
                return CmdResultCode.Fail_ParamError;
            } else {
                List<ConnectionEntity> targetConnects = this.findConnect(cmdData.getEquIdentity(), cmdData.getProtocolType());
                if (targetConnects != null && targetConnects.size() != 0) {
                    targetConnects.forEach((connect) -> {
                        Object task;
                        if (connect.getProtocolType().equals(ConnectProtocolType.Tcp)) {
                            task = new DefaultServer2ClientCmdDispatchTCPTask(cmdData, connect, 0);
                        } else {
                            task = new DefaultServer2ClientCmdDispatchUDPTask(cmdData, this.fixUdpConnect(connect, cmdData.getUdpConnectParam()));
                        }

                        this.taskDispatcher.addNewTask((DispatchTask)task, DispatchTaskType.Down.getCode());
                    });
                    return CmdResultCode.Successed;
                } else {
                    return CmdResultCode.Fail_Socket_Inactive;
                }
            }
        } else {
            log.info("命令下发接口 cmdParam:{}, return:{}", JSONObject.toJSONString(cmdData), CmdResultCode.Fail_ParamError);
            return CmdResultCode.Fail_ParamError;
        }
    }

    private List<ConnectionEntity> findConnect(String equIdentity, int protocolType) {
        if (StringUtil.isNullOrEmpty(equIdentity)) {
            return null;
        } else {
            List<ConnectionEntity> result = new ArrayList();
            ConnectionEntity targetConnect;
            if (protocolType == 0 || protocolType == 1) {
                targetConnect = this.containerManager.getNettyRpcClientContainer().find(equIdentity, ConnectProtocolType.Tcp);
                if (targetConnect != null) {
                    result.add(targetConnect);
                }
            }

            if (protocolType == 0 || protocolType == 2) {
                targetConnect = this.containerManager.getNettyRpcClientContainer().find(equIdentity, ConnectProtocolType.Udp);
                if (targetConnect != null) {
                    result.add(targetConnect);
                }
            }

            return result;
        }
    }

    private ConnectionEntity fixUdpConnect(ConnectionEntity oriConnect, CmdData.UdpConnectParam param) {
        return null == param ? oriConnect : new ConnectionEntity(oriConnect.getChannel(), oriConnect.getEquIdentity(), ConnectProtocolType.Udp, StringUtil.isNullOrEmpty(param.getIp()) ? oriConnect.getIP() : param.getIp(), null == param.getPort() ? oriConnect.getPort() : param.getPort());
    }
}
