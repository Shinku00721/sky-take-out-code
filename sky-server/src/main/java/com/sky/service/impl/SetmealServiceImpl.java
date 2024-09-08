package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void addsetmeal(SetmealDTO setmealDTO) {
        //将数据进行复制
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //向套餐表中插入数据，在前端并没有显示套餐id，所以需要获取到自增的id
        setmealMapper.insert(setmeal);
        //获取套餐表中的套餐id
        Long id = setmeal.getId();
        //获取到setmealdish集合
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
        }
        //向套餐菜品关联表中批量插入数据
        setmealDishMapper.insertBatch(setmealDishes);

    }
    /**
     * 套餐的分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        //开始进行分页查询
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());

        Page<Setmeal> setmeal = setmealMapper.pageQuery(setmealPageQueryDTO);

        //返回对象
        Long total = setmeal.getTotal();
        List<Setmeal> result = setmeal.getResult();
        return new PageResult(total, result);
    }





}
