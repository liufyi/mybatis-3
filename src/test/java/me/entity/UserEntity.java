package me.entity;

import lombok.Data;
import lombok.experimental.Accessors;

//生成getter 和setter方法
@Data
//setter方法支持链接调用
@Accessors(chain = true)
public class UserEntity
{
  private long id;
  private String name;
  private int age;
  private String email;

}
