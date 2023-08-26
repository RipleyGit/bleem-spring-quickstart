package site.bleem.boot.mapstruct.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import site.bleem.boot.mapstruct.controller.vo.WareHouseVO;
import site.bleem.boot.mapstruct.entity.GosWareHouse;

@Mapper
public interface BeanConvertMapper {

    BeanConvertMapper INSTANCE = Mappers.getMapper(BeanConvertMapper.class);
    
//    @Mapping(target = "id", ignore = true) // 忽略id，不进行映射
//    @Mapping(target = "predictionNum", source = "deviceCount")
//    @Mapping(target = "cleanFinishedTime", source = "cleanFinishedTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    WareHouseVO convertWareHouse(GosWareHouse wareHouse);
	
}