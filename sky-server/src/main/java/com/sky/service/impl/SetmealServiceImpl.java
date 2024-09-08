package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
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

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        //判断套餐是否能够删除，起售状态的不能够删除
        for (Long id : ids) {
            //根据id批量查询数据
            Setmeal setmeal = setmealMapper.getById(id);
            //获取到套餐的状态
            Integer status = setmeal.getStatus();
            if(StatusConstant.ENABLE == status){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        //批量进行套餐的删除
        setmealMapper.deleteBatch(ids);

        //批量删除关联表中的数据
        for (Long id : ids) {
            setmealDishMapper.deleteById(id);
        }
    }

    /**
     * 启用禁用套餐
     * @param status
     * @param id
     */
    @Override
    public void status(Integer status, Long id) {
        //根据id修改状态
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmealMapper.update(setmeal);
    }

    /**
     * 根据id查询套餐中的数据
     * @param id
     */
    @Override
    public SetmealDTO gteById(Long id) {
        //根据id查询套餐表中的数据
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealDTO setmealDTO = new SetmealDTO();
        BeanUtils.copyProperties(setmeal,setmealDTO);
        //根据套餐id查询套餐菜品关联表中的数据
        List<SetmealDish> setmealDish = setmealDishMapper.GetDishId(id);
        setmealDTO.setSetmealDishes(setmealDish);
        return setmealDTO;
    }

    /**
     * 对套餐进行修改
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //对套餐表中的数据进行修改
        setmealMapper.update(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //删除
        setmealDishMapper.deleteById(setmealDTO.getId());
        //批量插入数据
        setmealDishMapper.insertBatch(setmealDishes);
    }



}
