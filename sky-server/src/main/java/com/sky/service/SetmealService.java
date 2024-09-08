package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;

public interface SetmealService {
    /**
     * 新增套餐
     * @param setmealDTO
     */
    void addsetmeal(SetmealDTO setmealDTO);

    /**
     * 套餐的分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
