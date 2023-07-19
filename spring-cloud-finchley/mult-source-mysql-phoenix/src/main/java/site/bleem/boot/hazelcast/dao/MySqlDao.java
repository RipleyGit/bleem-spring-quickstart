package site.bleem.boot.hazelcast.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface MySqlDao {

    @Select("SELECT * from TDJ.risk")
    List<Object> queryAll();

}