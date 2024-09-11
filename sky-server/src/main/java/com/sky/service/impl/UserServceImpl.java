package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServceImpl implements UserService {
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;
    //微信接口的地址
    private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    /**
     *  微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {
        //调用微信的接口，获取到openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code",userLoginDTO.getCode());
        map.put("grant_type", "authorization_code");
        String wechatLogin = HttpClientUtil.doGet(WX_LOGIN, map);

        //将结果转换为json格式，获取openid
        JSONObject jsonObject = JSONObject.parseObject(wechatLogin);
        String openid = jsonObject.getString("openid");

        //判断openid是否为空，为空则抛出异常
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //根据openid查询用户信息
        User user = userMapper.getByOpenid(openid);

        //判断是否为新用户，如果是新用户，自动完成注册
        if(user == null){
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());

            //插入信息
            userMapper.insert(user);
        }

        //返回用户信息
        return user;

    }
}
