package liucj.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import liucj.common.Const;
import liucj.common.ResponseCode;
import liucj.common.ServerResponse;
import liucj.dao.CategoryMapper;
import liucj.dao.ProductMapper;
import liucj.pojo.Category;
import liucj.pojo.Product;
import liucj.service.ICategoryService;
import liucj.service.IProductService;
import liucj.utils.PropertiesUtil;
import liucj.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产品实现类
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        Date date = new Date();
        if (product != null) {
            if (StringUtils.isNoneBlank(product.getSubImages())) {
                //通过“，”判断是否有缩略图
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                product.setUpdateTime(date);
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("更新产品成功");
                }
                return ServerResponse.createBySuccess("更新产品失败");
            } else {
                product.setCreateTime(date);
                product.setUpdateTime(date);
                try {
                    int rowCount = productMapper.insert(product);
                    if (rowCount > 0) {
                        return ServerResponse.createBySuccess("新增产品成功");
                    }
                    return ServerResponse.createBySuccess("新增产品失败");
                } catch (Exception e) {
                    e.getMessage();
                    return ServerResponse.createBySuccess(e.getMessage());
                }

            }
        }
        return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
    }

    /**
     * 通过栏目获取商品列表
     *
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            try {
                ServerResponse<List<Integer>> serverResponse = iCategoryService.selectCategoryAndChildrenById(category.getId());
                categoryIdList = serverResponse.getData();
            } catch (Exception e) {
                e.getStackTrace();
                return ServerResponse.createByErrorMessage(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);
        //排序处理
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        try {
            List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);
            List<ProductListVo> productListVoList = Lists.newArrayList();
            for (Product product : productList) {
                ProductListVo productListVo = assembleProductListVo(product);
                productListVoList.add(productListVo);
            }
            PageInfo pageInfo = new PageInfo(productList);
            pageInfo.setList(productListVoList);
            return ServerResponse.createBySuccess(pageInfo);
        } catch (Exception e) {
            e.getStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        //startPage--start
        PageHelper.startPage(pageNum, pageSize);
        //填充自己的sql查询逻辑
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        //pageHelper-收尾
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    /**
     * @param product
     * @return
     * @Override public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
     * return null;
     * }
     * <p>
     * /**
     * productList -> productListVo
     */
    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://10.114.73.119/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }
}
