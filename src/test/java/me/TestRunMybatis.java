package me;

import me.dao.UserDao;
import me.entity.UserEntity;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestRunMybatis
{
  /**
   * 使用mybatis配置文件实例化SqlSessionFactory
   * @throws IOException
   */
  @Test
  public void test() throws IOException
  {
    //1.加载mybatis配置文件
    InputStream inStream=Resources.getResourceAsStream("mybatis-config.xml");
    //2.构建一个SqlSessionFactory,mybatis初始化完成
    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inStream);
    //3.打开session
    try(SqlSession session=sessionFactory.openSession())
    {
      //4.获取dao
      UserDao userDao=session.getMapper(UserDao.class);
      //5.调用dao的方法查询数据
      List<UserEntity> data=userDao.selectList("Tom", Arrays.asList(1L,2L,3L));
      List<String> names=data.stream()
        .map(UserEntity::getName)
        .collect(Collectors.toList())
      ;
      //6.打印所有用户名
      System.out.println(names);
    }

  }

  /**
   * 使用mybatis配置文件实例化SqlSessionFactory
   */
  @Test
  public void testNoXmlConfig()
  {
    //1.使用mybatis自带的jdbc连接池,实例化一个数据源
    PooledDataSource dataSource=new PooledDataSource();
    dataSource.setDriver("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/test?serverTimezone=UTC");
    dataSource.setUsername("test");
    dataSource.setPassword("test");
    //2.jdbc事务管理器
    TransactionFactory transactionFactory = new JdbcTransactionFactory();
    Environment environment = new Environment("development", transactionFactory, dataSource);
    Configuration configuration = new Configuration(environment);
    //注:UserDao访问数据库的方法,需要有对应的注解如@Selct指明要执行的sql语句
    //,如果不用注解,需要同级目录下面有对同名的xml sql配置映射配置文件
    configuration.addMapper(UserDao.class);
    //4.构建一人SqlSessionFactory
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    try(SqlSession session=sqlSessionFactory.openSession())
    {
      //4.获取dao
      UserDao userDao=session.getMapper(UserDao.class);
      //5.调用dao的方法查询数据
      List<UserEntity> data=userDao.selectList("Tom", Arrays.asList(1L,2L,3L));
      List<String> names=data.stream()
        .map(UserEntity::getName)
        .collect(Collectors.toList())
        ;
      //6.打印所有用户名
      System.out.println(names);
    }
  }
}
