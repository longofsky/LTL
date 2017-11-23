package cn.ltl.sso.query.api;

import cn.ltl.sso.query.bean.User;

public interface UserQueryService {

    /**
     * 根据token查询User对象
     * 
     * @return
     */
    public User queryUserByToken(String token);

}
