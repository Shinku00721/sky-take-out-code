package com.sky.aspect;

import com.sky.annotation.AutoFile;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import io.lettuce.core.cluster.api.sync.Executions;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFileAspect {
    //切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFile)")
    public void autoFilePointCut() {}


    //定义前置通知
    @Before("autoFilePointCut()")
    public void autoFile(JoinPoint joinPoint) {
        log.info("前置通知开始执行");

        //获取到数据库执行的操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获取到签名对象
        AutoFile annotation = signature.getMethod().getAnnotation(AutoFile.class);//获取到方法上的注解
        OperationType operationType = annotation.value();//获取数据库的操作类型

        //获取到当前执行的方法参数、
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];

        //获取到需要操作的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //根据不同的数据库操作类型执行不同的操作
        if(operationType == OperationType.INSERT){
            //需要插入四条数据
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //赋值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }else {
            //需要插入两条数据
            try {

                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
