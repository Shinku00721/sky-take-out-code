package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 删除数据
     * @param i
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteById(Long i);

    /**
     * 根据id查询菜品和口味信息
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id =#{dishId}")
    List<DishFlavor> getByDishId(Long id);
}
