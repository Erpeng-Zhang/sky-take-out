package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl implements SetmealService{

    private SetmealMapper setmealMapper;

    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();

        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 设置状态默认关闭
        setmeal.setStatus(StatusConstant.DISABLE);

        // 保存
        setmealMapper.insert(setmeal);
    }

    @Override
    public void startOrStop(Integer status, Long id) {

        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();

        setmealMapper.update(setmeal);
    }
}