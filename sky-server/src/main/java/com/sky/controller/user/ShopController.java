package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "店铺操作接口")
public class ShopController {
    public static final String key = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 获取店铺的营业状态
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(key);
        log.info("当前店铺的营业状态为:{}", status == 1 ? "营业中" : "打烊");
        return Result.success();
    }

}
