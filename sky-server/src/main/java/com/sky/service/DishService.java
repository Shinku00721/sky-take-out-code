package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;


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

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品和口味信息
     * @param id
     * @return
     */
    DishVO getByDishWithFlavor(Long id);
}
