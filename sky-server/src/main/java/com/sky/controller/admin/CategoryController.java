package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sky.service.CategoryService;
import com.sky.result.PageResult;
import com.sky.result.Result;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 启用/禁用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用/禁用分类")
    public Result startOrStop(@PathVariable("status") Integer status, Long id){
        log.info("启用/禁用分类{}", id);
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 类型分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分类分页查询, 参数为：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> getCategoryByType(Integer type){

        log.info("根据类型查询分类{}", type);
        List<Category> list = categoryService.getCategoryListByType(type);
        return Result.success(list);
    }

//    /**
//     * 根据类型查询分类
//     * @param type
//     * @return
//     */
//    @GetMapping("/list")
//    @ApiOperation("根据类型查询分类")
//    public Result<PageResult> getCategoryByType(Integer type){
//
//        log.info("根据类型查询分类{}", type);
//
//        // 新建分页查询对象
//        CategoryPageQueryDTO categoryPageQueryDTO = new CategoryPageQueryDTO();
//        categoryPageQueryDTO.setType(type);
//        categoryPageQueryDTO.setPage(1);
//        categoryPageQueryDTO.setPageSize(10);
//
//        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
//        return Result.success(pageResult);
//    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除分类")
    public Result deleteById(Long id){
        log.info("根据id:{}删除分类", id);
        categoryService.deleteById(id);
        return Result.success();
    }
}
