package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFile;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据id修改套餐
     * @param setmeal
     */
    @AutoFile(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 向套餐表中插入数据
     * @param setmeal
     */
    @AutoFile(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 开始进行分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
