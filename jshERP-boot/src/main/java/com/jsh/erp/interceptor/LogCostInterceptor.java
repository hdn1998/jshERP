package com.jsh.erp.interceptor;

import com.jsh.erp.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LogCostInterceptor implements HandlerInterceptor {

    private static final String[] ALLOW_URLS = {
            "/jshERP-boot/user/login", "/jshERP-boot/user/weixinLogin",
            "/jshERP-boot/user/weixinBind", "/jshERP-boot/user/registerUser",
            "/jshERP-boot/user/randomImage", "/jshERP-boot/platformConfig/getPlatform",
            "/jshERP-boot/v2/api-docs", "/jshERP-boot/webjars",
            "/jshERP-boot/systemConfig/static", "/jshERP-boot/api/plugin/wechat/weChat/share",
            "/jshERP-boot/api/plugin/general-ledger/pdf/voucher", "/jshERP-boot/api/plugin/tenant-statistics/tenantClean"
    };

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURI();
        //具体，比如：处理若用户未登录，则跳转到登录页
        Object userId = redisService.getObjectFromSessionByKey(request, "userId");
        if (userId != null) { //如果已登录，不阻止
            return true;
        }
        if (requestUrl != null && (requestUrl.contains("/doc.html") ||
                requestUrl.contains("/user/login") || requestUrl.contains("/user/register"))) {
            return true;
        }
        if (null != ALLOW_URLS) {
            for (String url : ALLOW_URLS) {
                if (requestUrl != null && requestUrl.startsWith(url)) {
                    return true;
                }
            }
        }
        response.setStatus(500);
        if (requestUrl != null && !requestUrl.contains("/user/logout") && !requestUrl.contains("/function/findMenuByPNumber")) {
            response.getWriter().write("loginOut");
        }
        return false;
    }
}
