package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("进行新增菜品:{}", dishDTO);
        dishService.saveDishAndFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 进行菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("开始进行菜品的分页查询:{}", dishPageQueryDTO);
        PageResult PageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(PageResult);
    }

    /**
     * 菜品的批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result deleteDish(@RequestParam List<Long> ids) {
        log.info("批量删除菜品:{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品和口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品和口味信息")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品和口味信息:{}", id);
        DishVO DishVO = dishService.getByDishWithFlavor(id);
        return Result.success(DishVO);
    }

    /**
     * 修改菜品的信息
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息:{}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 进行启用禁用菜品
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用菜品")
    public Result status(@PathVariable Integer status, Long id) {
        log.info("启用禁用菜品:{},{}",status,id);
        //进行启用禁用菜品
        dishService.status(status,id);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        log.info("根据分类id查询菜品:{}",categoryId);
        List<Dish> list = dishService.getByCategoryId(categoryId);
        return Result.success(list);
    }
}
