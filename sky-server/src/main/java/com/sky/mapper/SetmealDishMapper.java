package com.sky.mapper;

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


}
