package liucj.service;

import com.github.pagehelper.PageInfo;
import liucj.common.ServerResponse;
import liucj.pojo.Product;
import liucj.vo.ProductDetailVo;


public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    /**
     * 更新状态（上下架）
     * @param productId
     * @param status
     * @return
     */
    ServerResponse<String> setSaleStatus(Integer productId,Integer status);

    //前台商品详情
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

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

    /**
     * 搜索
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
}
