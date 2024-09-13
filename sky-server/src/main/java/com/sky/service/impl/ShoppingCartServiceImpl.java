package com.sky.service.impl;

import com.fasterxml.jackson.databind.BeanProperty;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断购物车中的商品是否存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        if (list != null && list.size() > 0) {
            //已经存在，数量加1
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            //更新数据
            shoppingCartMapper.update(cart);

        } else {
            //不存在，向购物车中添加商品数据
            //判断提交的是一个菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                //表示添加的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            } else {
                Long setmealId = shoppingCartDTO.getSetmealId();
                //表示添加的是套餐
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
        }
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartMapper.insert(shoppingCart);
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    /**
     * 删除购物车中的单个商品
     *
     * @param shoppingCartDTO
     */
    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //查询商品的信息
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);


        ShoppingCart cart = list.get(0);
        //判断数量是否为0
        if (cart.getNumber() == 1) {
            //直接删除
            shoppingCartMapper.deleteByUserId(userId);
        } else {
            //数量减一
            cart.setNumber(cart.getNumber() - 1);
            //更新数据
            shoppingCartMapper.update(cart);
        }


    }


}
