package site.bleem.boot.hazelcast.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@DS("dp")
@Mapper
public interface PhoenixDao {

    @Select("SELECT * from TDJ.risk")
    List<Object> queryAll();

}