package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
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

    /**
     * 根据id修改菜品和口味的信息
     * @param dishDTO
     */
    void update(DishDTO dishDTO);

    /**
     * 对菜品的启用和禁用
     * @param status
     * @param id
     */
    void status(Integer status, Long id);

    /**
     * 根据分类的id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> getByCategoryId(Long categoryId);


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
