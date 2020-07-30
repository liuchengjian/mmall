package liucj.controller.backend;

import liucj.common.Const;
import liucj.common.ServerResponse;
import liucj.pojo.ListCount;
import liucj.pojo.User;
import liucj.service.IOrderService;
import liucj.service.IProductService;
import liucj.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                //说明登录的是管理员
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            } else {
                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }

    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ServerResponse<User> response = iUserService.list(pageNum, pageSize);
        if (response.isSuccess()) {
            return ServerResponse.createBySuccess(response.getData());
        }
        return ServerResponse.createByErrorMessage("获取用户数据错误");
    }


    @RequestMapping(value = "listCount.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ListCount> listCount() {
        ListCount listCount = new ListCount();
        ServerResponse<Integer> userResponse  = iUserService.listUserCount();
        ServerResponse<Integer> productResponse  = iProductService.getProductListCount();
        ServerResponse<Integer> orderResponse  = iOrderService.manageListCount();
        if (userResponse.isSuccess()) {
            listCount.setUserCount(userResponse.getData());
        }else {
            listCount.setUserCount(0);
        }
        if (productResponse.isSuccess()) {
            listCount.setProductCount(productResponse.getData());
        }else {
            listCount.setProductCount(0);
        }
        if (orderResponse.isSuccess()) {
            listCount.setOrderCount(orderResponse.getData());
        }else {
            listCount.setOrderCount(0);
        }
        return ServerResponse.createBySuccess(listCount);
    }


}
