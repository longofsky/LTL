package cn.ltl.sso.query.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cn.ltl.common.service.RedisService;
import cn.ltl.sso.query.api.UserQueryService;
import cn.ltl.sso.query.bean.User;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Integer REDIS_TIME = 60 * 30;

    @Autowired
    private RedisService redisService;

    @Override
    public User queryUserByToken(String token) {
        String key = "TOKEN_" + token;
        String jsonData = this.redisService.get(key);
        if (StringUtils.isEmpty(jsonData)) {
            // 登录超时
            return null;
        }

        // 重新设置Redis中的生存时间
        this.redisService.expire(key, REDIS_TIME);

        try {
            return MAPPER.readValue(jsonData, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
