package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;


public interface DishService {
    /**
     * 新增菜品和口味
     * @param dishDTO
     */
    void saveDishAndFlavor(DishDTO dishDTO);

    /**
     * 进行菜品的分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
