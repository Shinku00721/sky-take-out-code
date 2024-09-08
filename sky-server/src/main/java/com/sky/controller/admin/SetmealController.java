package com.sky.controller.admin;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐管理相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result add(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增菜品:{}", setmealDTO);
        setmealService.addsetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐的分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐管理的分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐的分页查询:{}", setmealPageQueryDTO);
        PageResult PageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(PageResult);
    }
}
