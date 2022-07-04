package com.quickmarket.member.constant;

/**
* @desc: 类的描述:会员服务常量类
* @createDate: 2022/1/22 15:27
* @version: 1.0
*/
public class MDA {

    /**
     * 会员服务第三方客户端(这个客户端在认证服务器配置好的oauth_client_details)
     */
    public static final String CLIENT_ID = "quickmall-member";

    /**
     * 会员服务第三方客户端密码(这个客户端在认证服务器配置好的oauth_client_details)
     */
    public static final String CLIENT_SECRET = "123123";

    /**
     * 认证服务器登陆地址
     */
    public static final String OAUTH_LOGIN_URL = "http://quickmall-authcenter/oauth/token";


    public static final String USER_NAME = "username";

    public static final String PASS = "password";

    public static final String GRANT_TYPE = "grant_type";

    public static final String SCOPE = "scope";

    public static final String SCOPE_AUTH = "read";
}
