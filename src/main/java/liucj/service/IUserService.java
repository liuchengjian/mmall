package liucj.service;

import liucj.common.ServerResponse;
import liucj.pojo.User;

/**
 * 用户服务接口
 */
public interface IUserService {
    //登录
    ServerResponse<User> login(String username, String password);

    //注册
    ServerResponse<String> register(User user);

    //检验是否是管理员
    ServerResponse checkAdminRole(User user);
}
