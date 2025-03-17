package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 根据订单状态和下单时间查询订单
     * @param status
     * @param orderTime
     */
    @Select("SELECT * FROM orders where status = #{status} AND order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);


    /**
     * 根据日期查询当天营业额
     * @param date
     * @return
     */
    @Select("Select SUM(amount) FROM orders \n" +
            "WHERE order_time >= #{date} " +
            "AND order_time < DATE_ADD(#{date}, INTERVAL 1 DAY) \n" +
            "AND status = 5")
    Double getTurnoverByDate(LocalDate date);

    /**
     * 获得每天用户总量
     * @param date
     * @return
     */
    @Select("select count(1) from user where create_time < DATE_ADD(#{date}, INTERVAL 1 DAY)")
    Double getTotalUserByDate(LocalDate date);

    /**
     * 获得每天新增用户量
     * @param date
     * @return
     */
    @Select("select count(1) from user " +
            "where create_time >= #{date}" +
            "AND create_time < DATE_ADD(#{date}, INTERVAL 1 DAY)")
    Double getNewUserByDate(LocalDate date);


    /**
     * 根据条件查询订单数量
     * @param map
     * @return
     */
    Integer getOrderCountByMap(Map map);

    /**
     * 获取指定时间区间内销量排名前10
     * @param begin
     * @param end
     * @return
     */
    @Select("select od.name, sum(od.number) number from order_detail od, orders o\n" +
            "where od.order_id = o.id \n" +
            "and o.order_time >= #{begin} \n" +
            "AND o.order_time < date_add(#{end}, interval 1 day)\n" +
            "and o.status = 5 \n" +
            "group by od.name order by number desc limit 10")
    List<GoodsSalesDTO> getSalesTop10(LocalDate begin, LocalDate end);


    /**
     * 根据动态条件统计营业额数据
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 根据动态条件统计订单数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
