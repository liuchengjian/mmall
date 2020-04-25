package liucj.service;

import com.github.pagehelper.PageInfo;
import liucj.common.ServerResponse;
import liucj.pojo.Shipping;

public interface IShippingService {
    //添加地址
    ServerResponse add(Integer userId, Shipping shipping);

    //删除地址
    ServerResponse<String> del(Integer userId, Integer shippingId);

    //更新地址
    ServerResponse update(Integer userId, Shipping shipping);

    //查询地址
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    //地址列表
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

}
