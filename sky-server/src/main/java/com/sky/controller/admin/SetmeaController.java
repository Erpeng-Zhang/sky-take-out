package com.sky.controller.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 套餐相关接口
 */
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmeaController {

    @Autowired
    private SetmealService setmealService;
    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售、停售")
    public Result startOrStop(@PathVariable("status") Integer status, Long id){
        log.info("套餐起售/停售{},{}", status, id);
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}
