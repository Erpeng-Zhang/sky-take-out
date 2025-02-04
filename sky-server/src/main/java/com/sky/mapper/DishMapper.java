package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {
    @Select("select count(1) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);
}
