package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 修改分类
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    /**
     * 新增分类
     *   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
     *   `type` int DEFAULT NULL COMMENT '类型   1 菜品分类 2 套餐分类',
     *   `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '分类名称',
     *   `sort` int NOT NULL DEFAULT '0' COMMENT '顺序',
     *   `status` int DEFAULT NULL COMMENT '分类状态 0:禁用，1:启用',
     *   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
     *   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
     *   `create_user` bigint DEFAULT NULL COMMENT '创建人',
     *   `update_user` bigint DEFAULT NULL COMMENT '修改人',
     * @param category
     */
    @Insert("insert into category" +
    "(type, name, sort, status, create_time, update_time, create_user, update_user)" +
    "values" +
    "(#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Category category);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> getCategoryListByType(Integer type);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
