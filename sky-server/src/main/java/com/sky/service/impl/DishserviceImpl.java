package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishserviceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealdishmapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 进行新增菜品和口味
     * @param dishDTO
     */
    @Override
    @Transactional
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

    /**
     * 进行菜品的分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //开始进行分页查询，获取页码和每页的记录数
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        //开始进行分页查询
        Page<DishVO> page =dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断菜品时候能够删除，起售中的不能够删除
        for (Long id : ids) {
            //根据菜品id查询菜品，来获取菜品的状态
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                //菜品不能够删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断菜品是否能够删除，被关联套餐的不能够删除
        List<Long> id = setmealdishmapper.getDishId(ids);
        if(id != null && id.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //进行菜品的批量删除
        for (Long i : ids) {
            dishMapper.deleteById(i);
            //进行菜品关联的口味表的批量删除
            dishFlavorMapper.deleteById(i);
        }
    }

    /**
     * 根据id查询菜品和口味信息
     * @param id
     * @return
     */
    @Override
    public DishVO getByDishWithFlavor(Long id) {
        //在菜品表中查询菜品的信息
        Dish dish = dishMapper.getById(id);
        //在口味表中查询口味信息
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);

        //将查询到的数据封装到对象中
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    /**
     * 根据id修改菜品和口味的信息
     * @param dishDTO
     */
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //修改菜品种中的信息
        dishMapper.update(dish);

        //删除口味表中的数据
        dishFlavorMapper.deleteById(dishDTO.getId());

        //添加口味表中的数据
        List<DishFlavor> flavors = dishDTO.getFlavors();

        //判断口味是否为空
        if(flavors != null && flavors.size() > 0){
            //对集合进行遍历，插入主键id
            flavors.forEach(d -> d.setDishId(dishDTO.getId()));
            //批量插入菜品数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 对菜品的启用和禁用
     * @param status
     * @param id
     */
    @Override
    public void status(Integer status, Long id) {
        //创建dish对象
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        //根据id来修改菜品的启售状态
        dishMapper.update(dish);

        if (status == StatusConstant.DISABLE) {
            // 如果是停售操作，还需要将包含当前菜品的套餐也停售
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            List<Long> setmealIds = setmealdishmapper.getDishId(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }

    /**
     * 根据分类的id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
