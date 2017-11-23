package cn.ltl.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ltl.sso.query.api.UserQueryService;
import cn.ltl.sso.query.bean.User;

@Service
public class UserService {

    @Autowired
    private UserQueryService userQueryService;

    /**
     * 根据token查询用户信息
     * 
     * @param token
     * @return
     */
    public User queryByToken(String token) {
        return this.userQueryService.queryUserByToken(token);
    }

}
