package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

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
        List<Double> turnover= new ArrayList<>();
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
            if(sum == null){
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

            map.put("end",endTime);

            //根据结束时间，查询总用户数据
            Integer sum = userMapper.getSumBymap(map);
            sumlist.add(sum);//放入集合中

            //根据结束和开始时间，查询新增的用户数据
            map.put("begin",beginTime);
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
}
