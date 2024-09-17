package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IntegerField;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.HashPrintJobAttributeSet;
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
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 进行营业额数据统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getturnoverStatistics(LocalDate begin, LocalDate end) {

        //计算日期，将日期放入list集合中
        List<LocalDate> datelist = new ArrayList<>();
        datelist.add(begin);

        //设置日期的结束时间
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            datelist.add(begin);
        }
        List<Double> turnover = new ArrayList<>();
        //查询营业额数据
        for (LocalDate date : datelist) {
            //获取一天的开始时间和结束时间
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            //将数据存放map集合中
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double sum = orderMapper.sumByMap(map);
            if (sum == null) {
                sum = 0.0;
            }
            turnover.add(sum);
        }

        //将对象进行封装
        String join = StringUtils.join(datelist, ",");
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        turnoverReportVO.setDateList(join);
        String join2 = StringUtils.join(turnover, ",");
        turnoverReportVO.setTurnoverList(join2);

        return turnoverReportVO;
    }

    /**
     * 开始进行用户数据统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        //计算日期，将日期放入list集合中
        List<LocalDate> datelist = new ArrayList<>();
        datelist.add(begin);

        //设置日期的结束时间
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            datelist.add(begin);
        }
        //存放总用户数量数据
        List<Integer> sumlist = new ArrayList<>();
        //存放新增员工数据
        List<Integer> newUserList = new ArrayList<>();
        for (LocalDate date : datelist) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            //将开始时间和结束时间存入map集合中
            Map map = new HashMap();

            map.put("end", endTime);

            //根据结束时间，查询总用户数据
            Integer sum = userMapper.getSumBymap(map);
            sumlist.add(sum);//放入集合中

            //根据结束和开始时间，查询新增的用户数据
            map.put("begin", beginTime);
            Integer newUser = userMapper.getNewBymap(map);
            newUserList.add(newUser);//放入集合中
        }
        //进行数据封装
        UserReportVO userReportVO = new UserReportVO();
        userReportVO.setDateList(StringUtils.join(datelist, ","));
        userReportVO.setNewUserList(StringUtils.join(newUserList, ","));
        userReportVO.setTotalUserList(StringUtils.join(sumlist, ","));

        return userReportVO;


    }

    /**
     * 统计订单数据
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        //计算日期，将日期放入list集合中
        List<LocalDate> datelist = new ArrayList<>();
        datelist.add(begin);

        //设置日期的结束时间
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            datelist.add(begin);
        }

        List<Integer> orderCountList = new ArrayList<>();//存放每天订单的总数量
        List<Integer> vailidOrderCountList = new ArrayList<>();//存放每天的有效订单数量
        for (LocalDate date : datelist) {
            //设置开始时间和结束时间
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            //将数据放入map集合中
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);

            //查询每天订单的总数量
            Integer count = orderMapper.orderCount(map);
            orderCountList.add(count);//放入集合中

            map.put("status", Orders.COMPLETED);
            //查询每天的有效订单数量
            Integer vaild = orderMapper.vaildorderCount(map);
            vailidOrderCountList.add(vaild);
        }


        //获取订单的总数量
        Integer totalorderCount = orderCountList.stream().reduce(Integer::sum).get();
        //获取有效订单的总数
        Integer totalvaildorderCount = vailidOrderCountList.stream().reduce(Integer::sum).get();

        //获取完成率
        Double completionRate = 0.0;
        if(completionRate != 0.0){
            completionRate = totalvaildorderCount.doubleValue() / totalorderCount;
        }

        //封装数据
        OrderReportVO orderReportVO = new OrderReportVO();
        orderReportVO.setDateList(StringUtils.join(datelist, ","));
        orderReportVO.setOrderCountList(StringUtils.join(orderCountList, ","));
        orderReportVO.setValidOrderCountList(StringUtils.join(vailidOrderCountList, ","));
        orderReportVO.setTotalOrderCount(totalorderCount);
        orderReportVO.setValidOrderCount(totalvaildorderCount);
        return orderReportVO;
    }

    /**
     * 销量数据top10
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        //设置开始时间和结束时间
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        //进行查询
        List<GoodsSalesDTO> list = orderMapper.getSalesTop10(beginTime, endTime);

        //将数据进行封装返回
        List<String> names = list.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = list.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return new SalesTop10ReportVO(StringUtils.join(names, ","), StringUtils.join(numbers, ","));
    }

    @Override
    public void exportReport(HttpServletResponse response) {
        //创建查询的时间
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        //查询数据库，获得营业数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(beginTime, endTime);

        //将营业数据进行读取并写入
        InputStream inputStream = this.getClass().getResourceAsStream("/template/运营数据报表模板.xlsx");
        try {
            //基于模板创建一个新的excel文件
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
            //获取第一个sheet
            XSSFSheet sheet = xssfWorkbook.getSheet("Sheet1");

            //填充第二行的数据
            sheet.getRow(1).getCell(1).setCellValue("时间"+beginTime+"至"+endTime);

            //填充第四行数据
            sheet.getRow(3).getCell(2).setCellValue(businessDataVO.getTurnover());
            sheet.getRow(3).getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            sheet.getRow(3).getCell(6).setCellValue(businessDataVO.getNewUsers());

            //填充第五行数据
            sheet.getRow(4).getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            sheet.getRow(4).getCell(4).setCellValue(businessDataVO.getUnitPrice());


            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                //查询数据库，获得营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN),LocalDateTime.of(date, LocalTime.MAX));
                //插入数据
                sheet.getRow(7+i).getCell(1).setCellValue(date.toString());
                sheet.getRow(7+i).getCell(2).setCellValue(businessData.getTurnover());
                sheet.getRow(7+i).getCell(3).setCellValue(businessData.getValidOrderCount());
                sheet.getRow(7+i).getCell(4).setCellValue(businessData.getOrderCompletionRate());
                sheet.getRow(7+i).getCell(4).setCellValue(businessData.getUnitPrice());
                sheet.getRow(7+i).getCell(5).setCellValue(businessData.getNewUsers());
            }
            ServletOutputStream out = response.getOutputStream();
            xssfWorkbook.write(out);

            //关闭资源
            out.close();
            xssfWorkbook.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
