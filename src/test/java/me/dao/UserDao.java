package me.dao;

import me.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDao
{
  //@Select("select * from user")
  List<UserEntity> selectList(@Param("name") String xm
                              ,@Param("ids") List<Long> ids);
}
