package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {
    /**
     * 根据分类id获得套参数
     * @param categoryId
     * @return
     */
    @Select("select count(1) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入新套餐
     * @param setmeal
     */
    @Insert("insert into setmeal " +
            "(category_id, name, price, status, description, image, create_time, update_time, create_user, update_user) " +
            "values " +
            "(id, #{categoryId}, ${name}, price, status, description, image, createTime, updateTime, createUser, updateUser)"
    )
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 更新套餐
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);
}
