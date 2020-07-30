package liucj.service;

import liucj.common.ServerResponse;
import liucj.pojo.User;
import org.springframework.web.bind.annotation.RequestParam;

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

    ServerResponse list( @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize);

    ServerResponse<Integer> listUserCount();


}
