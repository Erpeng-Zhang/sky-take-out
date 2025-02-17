package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.SetmealDTO;

public interface SetmealService {
    /**
     * 新增套餐
     * @param setmealDTO
     */
    void save(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     * @return
     */
    void startOrStop(Integer status, Long id);
}
