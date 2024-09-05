package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishserviceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 进行新增菜品和口味
     * @param dishDTO
     */
    @Override
    public void saveDishAndFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //向菜品表中插入一条菜品数据
        dishMapper.insert(dish);

        Long id = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();

        //判断口味是否为空
        if(flavors != null && flavors.size() > 0){
            //对集合进行遍历，插入主键id
            flavors.forEach(d -> d.setDishId(id));
            //批量插入菜品数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }
}
