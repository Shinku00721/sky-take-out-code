package com.sky.mapper;

import com.sky.annotation.AutoFile;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 查询关联套餐中的菜品id
     * @return
     */
    List<Long> getDishId(List<Long> ids);

    /**
     * 关联套餐和菜品中关联的数据
     * @param setmealDishes
     */
    @AutoFile(OperationType.INSERT)
    void insertBatch(List<SetmealDish> setmealDishes);
}
