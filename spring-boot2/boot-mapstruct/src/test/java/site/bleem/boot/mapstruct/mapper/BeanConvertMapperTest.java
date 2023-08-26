package site.bleem.boot.mapstruct.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import site.bleem.boot.mapstruct.controller.vo.WareHouseVO;
import site.bleem.boot.mapstruct.entity.GosWareHouse;

import javax.annotation.Resource;
import java.time.LocalDateTime;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class BeanConvertMapperTest {
    @Test
    public void e2vTest(){
        GosWareHouse wareHouse =
                GosWareHouse.builder().id(new Long(20)).houseName("仓库001").houseAddress("地址001").deviceCount(123).cleanFinishedTime(LocalDateTime.now()).build();

        WareHouseVO wareHouseVO = BeanConvertMapper.INSTANCE.convertWareHouse(wareHouse);

        System.out.println(wareHouseVO);
        //WareHouseVO(id=null, houseName=仓库001, houseAddress=地址001, cleanFinishedTime=2023-01-29 15:58:09, predictionNum=123)
    }

    @Resource
    private SpringBeanConvertMapper springBeanConvertMapper;

    @Test
    public void v2eTest(){
        WareHouseVO wareHouse =
                WareHouseVO.builder().id(new Long(20)).houseName("仓库001").houseAddress("地址001").predictionNum(123).build();
        GosWareHouse gosWareHouse = springBeanConvertMapper.convertGosWareHouse(wareHouse);

        System.out.println(gosWareHouse);
    }
}