package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportServive;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * 数据统计相关接口
 */
@RestController
@RequestMapping("/admin/report")
@Api("数据统计相关接口")
@Slf4j
public class ReportController {

    @Autowired
    private ReportServive reportServive;


    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("营业额统计：{}--{}",begin,end);
        TurnoverReportVO turnoverReportVO = reportServive.getTurnoverStatistics(begin,end);
        return Result.success(turnoverReportVO);
    }

    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("用户统计：{}--{}",begin,end);
        UserReportVO userReportVO = reportServive.getUserStatistics(begin,end);
        return Result.success(userReportVO);
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> orderStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("订单统计：{}--{}",begin,end);
        OrderReportVO orderReportVO = reportServive.getOrderStatistics(begin,end);
        return Result.success(orderReportVO);
    }

    /**
     * 销量排名Top10
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10")
    @ApiOperation("销量排名Top10")
    public Result<SalesTop10ReportVO> top10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("销量排名Top10：{}--{}",begin,end);
        SalesTop10ReportVO salesTop10ReportVO = reportServive.getSalesTop10(begin,end);
        return Result.success(salesTop10ReportVO);
    }

    /**
     * 导出30天运营数据报表
     * @param response
     * @return
     */
    @GetMapping("/export")
    @ApiOperation("导出近30天运营数据")
    public Result export(HttpServletResponse response){
        log.info("导出近30天数据");
        reportServive.expotrBusinessData(response);
        return Result.success();
    }
}
