package liucj.service;

import com.github.pagehelper.PageInfo;
import liucj.common.ServerResponse;
import liucj.pojo.Product;


public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);
    /**
     * 前台通过分类去查询 产品列表
     *
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);

    /**
     * 获取产品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
}
