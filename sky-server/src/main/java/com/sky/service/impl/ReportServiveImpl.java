package com.sky.service.impl;

import com.fasterxml.jackson.databind.node.DoubleNode;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportServive;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.lettuce.core.output.DoubleListOutput;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiveImpl implements ReportServive {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {

        //获取日期列表
        List<LocalDate> datelist = getDateList(begin, end);

        TurnoverReportVO turnoverReportVO = TurnoverReportVO.builder()
                        .dateList(StringUtils.join(datelist,","))
                        .build();

        // 查询营业额
        List<Double> trunoverlist = new ArrayList<>();
        for (LocalDate date : datelist) {
            Double trunover = orderMapper.getTurnoverByDate(date);
            trunover = trunover==null ? 0 : trunover;
            trunoverlist.add(trunover);
        }
        turnoverReportVO.setTurnoverList(StringUtils.join(trunoverlist,","));

        return turnoverReportVO;
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {

        //获取日期列表
        List<LocalDate> datelist = getDateList(begin, end);

        UserReportVO userReportVO = UserReportVO.builder()
                .dateList(StringUtils.join(datelist,","))
                .build();

        // 查询用户
        List<Double> totaluserlist = new ArrayList<>();
        List<Double> newuseruist = new ArrayList<>();
        for (LocalDate date : datelist) {
            Double totaluser = orderMapper.getTotalUserByDate(date);
            Double newuser = orderMapper.getNewUserByDate(date);

            totaluser = totaluser==null ? 0 : totaluser;
            newuser = newuser==null ? 0 : newuser;

            totaluserlist.add(totaluser);
            newuseruist.add(newuser);
        }
        userReportVO.setTotalUserList(StringUtils.join(totaluserlist,","));
        userReportVO.setNewUserList(StringUtils.join(newuseruist,","));
        return userReportVO;
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        //获取日期列表
        List<LocalDate> datelist = getDateList(begin, end);

        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", null);

        // 计算订单总数和有效订单以及完成率
        Integer totalordercount = orderMapper.getOrderCountByMap(map);

        map.put("status", Orders.COMPLETED);
        Integer validordercount = orderMapper.getOrderCountByMap(map);

        // 计算每天数据
        List<Integer> totalorderCountList = new ArrayList<>();
        List<Integer> validorderCountList = new ArrayList<>();
        for (LocalDate date : datelist) {

            map.put("begin", date);
            map.put("end", date);
            map.put("status", null);
            Integer totalcount = orderMapper.getOrderCountByMap(map);

            map.put("status", Orders.COMPLETED);
            Integer validcount = orderMapper.getOrderCountByMap(map);

            totalcount = totalcount ==null ? 0 : totalcount;
            validcount = validcount ==null ? 0 : validcount;

            totalorderCountList.add(totalcount);
            validorderCountList.add(validcount);
        }


        OrderReportVO orderReportVO = new OrderReportVO();

        orderReportVO.setDateList(StringUtils.join(datelist,","));
        orderReportVO.setOrderCountList(StringUtils.join(totalorderCountList,","));
        orderReportVO.setValidOrderCountList(StringUtils.join(validorderCountList,","));

        orderReportVO.setTotalOrderCount(totalordercount);
        orderReportVO.setValidOrderCount(validordercount);
        orderReportVO.setOrderCompletionRate(
                (double)Math.round((double)validordercount / (double)totalordercount * 10000) / 100
        );

        return orderReportVO;
    }

    /**
     * 获取销量前10数据
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {

        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(begin, end);

        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());


        // 返回值
        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();

        salesTop10ReportVO.setNameList(StringUtils.join(names,","));
        salesTop10ReportVO.setNumberList(StringUtils.join(numbers,","));


        return salesTop10ReportVO;
    }

    List<LocalDate> getDateList(LocalDate begin, LocalDate end){
        List<LocalDate> datelist = new ArrayList<>();

        datelist.add(begin);
        while (! begin.equals(end)){
            begin = begin.plusDays(1);
            datelist.add(begin);
        }

        return datelist;
    }
}
