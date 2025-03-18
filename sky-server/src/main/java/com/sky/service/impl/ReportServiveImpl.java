package com.sky.service.impl;

import com.fasterxml.jackson.databind.node.DoubleNode;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportServive;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import io.lettuce.core.output.DoubleListOutput;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiveImpl implements ReportServive {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WorkspaceService workspaceService;

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

    /**
     * 导出营业数据报表
     * @param response
     */
    @Override
    public void expotrBusinessData(HttpServletResponse response) {
        // 查询数据库，获得营业数据
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MIN).plusDays(-1);
        LocalDateTime begin = end.plusDays(-30);
        BusinessDataVO businessData = workspaceService.getBusinessData(begin,end);
        // 通过POI将数据写入excel文件
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try{
            XSSFWorkbook excel = new XSSFWorkbook(in);
            XSSFSheet sheet = excel.getSheet("Sheet1");

            // 填充数据
            // 获得第2行2列
            sheet.getRow(1).getCell(1).setCellValue("时间" + begin + "至" + end);

            // 获得第4行
            XSSFRow row = sheet.getRow(3);
            // 营业额
            row.getCell(2).setCellValue(businessData.getTurnover());
            // 订单完成率
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            // 新增用户数
            row.getCell(6).setCellValue(businessData.getNewUsers());

            //第5行
            row = sheet.getRow(4);
            // 有效订单
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            // 平均客单价
            row.getCell(4).setCellValue(businessData.getUnitPrice());

            // 8行以后

            for(int i = 0; i < 30; i++){
                LocalDateTime reportDate = begin.plusDays(i);
                businessData = workspaceService.getBusinessData(reportDate,reportDate);

                row = sheet.getRow(7+i);
                //时间
                row.getCell(1).setCellValue(reportDate.toString());
                // 营业额
                row.getCell(2).setCellValue(businessData.getTurnover());
                // 订单完成率
                row.getCell(3).setCellValue(businessData.getOrderCompletionRate());
                // 新增用户数
                row.getCell(4).setCellValue(businessData.getNewUsers());
                // 有效订单
                row.getCell(5).setCellValue(businessData.getValidOrderCount());
                // 平均客单价
                row.getCell(6).setCellValue(businessData.getUnitPrice());
            }


            // 通过输出流下载
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            // 关闭资源
            excel.close();
            out.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        // 通过输出流输出excel文件
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
