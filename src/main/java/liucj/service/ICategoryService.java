package liucj.service;

import liucj.common.ServerResponse;
import liucj.pojo.Category;

import java.util.List;

/**
 * 产品分类接口
 */
public interface ICategoryService {
    //添加分类
    ServerResponse addCategory(String categoryName, Integer parentId);

    //查询分类子节点
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    //递归分类子节点
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

    //更新品类名称
    ServerResponse updateCategoryName(Integer categoryId,String categoryName);
}
