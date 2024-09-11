package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;

import java.util.List;

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

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 启用禁用套餐
     * @param status
     * @param id
     */
    void status(Integer status, Long id);

    /**
     * 对套餐进行修改
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);



    /**
     * 根据id查询套餐数据
     * @param id
     */
    SetmealDTO gteById(Long id);


    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
