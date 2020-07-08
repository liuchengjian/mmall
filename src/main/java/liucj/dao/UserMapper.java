package liucj.dao;

import liucj.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //检查用户是否存在
    int checkUsername(String username);

    //检查Email是否存在
    int checkEmail(String username);

    //登录
    User selectLogin(@Param("username") String username, @Param("password") String password);
    //用户列表
    List<User> selectList();
}