package liucj.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import liucj.common.Const;
import liucj.common.ServerResponse;
import liucj.dao.UserMapper;
import liucj.pojo.User;
import liucj.service.IUserService;
import liucj.vo.UserVo1;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户实现类
 */
@Service("IUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
//        String md5password = MD5Util.MD5EncodeUtf8(password);
        try {
            User user = userMapper.selectLogin(username, password);
            user.setPassword(StringUtils.EMPTY);
            if (user == null) {
                return ServerResponse.createByErrorMessage("密码错误");
            }
            return ServerResponse.createBySuccess("登陆成功", user);
        } catch (Exception e) {
            e.getStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    /**
     * 注册
     *
     * @param user 用户对象
     * @return
     */
    @Override
    public ServerResponse<String> register(User user) {
        //校验用户名是否存在
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        //校验Email是否存在
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        //设置 普通用户和管理员
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setUpdateTime(new Date());
        user.setCreateTime(new Date());
        //MD5加密 MD5Util.MD5EncodeUtf8(user.getPassword())
        user.setPassword(user.getPassword());
        try {
            int resultCount = userMapper.insert(user);
            if (resultCount > 0) {
                return ServerResponse.createBySuccessMessage("注册成功");
            } else {
                return ServerResponse.createByErrorMessage("注册失败");
            }
        } catch (Exception e) {
            e.getStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    /**
     * 检验是否是管理员
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    /**
     * 校验用户名
     *
     * @param str
     * @param type
     * @return
     */
    private ServerResponse checkValid(String str, String type) {
        if (StringUtils.isNotEmpty(type)) {
            //开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("Email已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse list(int pageNum, int pageSize) {
        //startPage--start
        PageHelper.startPage(pageNum, pageSize);
        //填充自己的sql查询逻辑
        List<User> userList = userMapper.selectList();
        List<UserVo1> userVoList = Lists.newArrayList();
        for (User user : userList) {
            userVoList.add(new UserVo1(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getQuestion(),
                    user.getAnswer(),
                    user.getRole(),
                    user.getCreateTime(),
                    user.getUpdateTime()
            ));
        }
        //pageHelper-收尾
        PageInfo pageResult = new PageInfo(userList);
        pageResult.setList(userVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse listUserCount() {
        List<User> userList = userMapper.selectList();
        return ServerResponse.createBySuccess(userList.size());
    }

}
