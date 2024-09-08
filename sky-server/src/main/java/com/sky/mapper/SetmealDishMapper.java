package com.sky.mapper;

import com.sky.annotation.AutoFile;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 查询关联套餐中的菜品id
     *
     * @return
     */
    List<Long> getDishId(List<Long> ids);

    /**
     * 关联套餐和菜品中关联的数据
     *
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 删除关联表中的数据
     *
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteById(Long setmealId);


    /**
     * 单个查询关联表中的数据
     *
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> GetDishId(Long setmealId);


}
