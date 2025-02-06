package com.sky.controller.admin;

import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理相关接口
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品管理相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品： {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除
     * 可以一次删除1个或批量删除多个
     * 起售中的菜品不能删除
     * 被套餐关联的菜品不能删除
     * 删除菜品后，关联的口味数据也要删除掉
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品批量删除：{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据Id查询菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据Id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据Id查询菜品{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品信息
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品信息{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 设置菜品起售/停售
     * 起售停售在
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("设置菜品起售/停售")
    public Result updateStatus(@PathVariable Integer status, Long id){
        log.info("设置菜品起售/停售:{}", status);
        dishService.update(id, status);
        return Result.success();
    }
}
