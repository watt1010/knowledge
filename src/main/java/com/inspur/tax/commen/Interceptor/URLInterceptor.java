package com.inspur.tax.commen.Interceptor;

import com.inspur.tax.commen.RSAUtil;
import com.inspur.tax.commen.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * UPL请求拦截器-用于用户验证
 */
public class URLInterceptor implements HandlerInterceptor {


    @Autowired
    UserUtil userUtil;
    private static final String pri_key = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAJ_-5Te8Z5oRgpS5E4cjQ3BJV_KwdkmGsuVTkapMWgDN9NMMezYH210Klb7WZ-rP0T3ivnEsZWuGmIwBeDM1KirFOEIM9dnL6RxBwaWgyTZnSy4McyMX0IgS_8Pd_hZowrNWKuqoeb-4LKOSjyGjoV0X_Sw9_w6NIRyp8rmD193XAgMBAAECgYEAjQs5qo7OjgWNpeoaCvHNS7l8bJefT2YNYxjuusAgP8FQaJUCMTlv6m-gXuHILjoR7Ypr9Hz803OOGy5YdlwR1MhWgrJQ-5oN2Ii-X9gxhuR83QitXUc2Hc776dwvTbG0AiuBGhFm-3FIMuUIcuuQQiUiNy7ddXKLWj2l2lhBXUECQQDdNnZE3HaszPkdIy8xn4gW7Bub1oUZs_ZotverAlfnppORf4U3sf7KOjSEUL-stMbNt3LJ0-bionBHHDE2xSFnAkEAuSf4LVvxKvYEyaU0P9nLACTJum50X6IN-B8fzc7ri8f6lb9xuuX8yHY6_n1f8-oo_w7XBZoZMbNa7g-8yDRqEQJBAMOUflZ9sTMwemPnkrdF_BWAJRzQOpeyA_8rHagFh2DZZwkx_L90UPfNJFeD6SOyJT8GnaeSAUWJJsnIRD7PZasCQQCfk54mYb5pu10G-VYdzbRSrGIcRoQPxBhsB08ezr5dW35Rv4ziesMxdgyEN0QScXbh1EVnc5dRKXYuBOw8VEgxAkEAusOQxoO7OVVmjEZ_4aqbbwhBSVU_ov5Ydwk1rQouNKs3nn768nlqiJ3gsigf4uEukMsiyZb47hwH9WqDO_tViQ";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 请求访问controller之前通过用户传入的App_Key进行验证
     *
     * @param request
     * @param response
     * @param arg2
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        RSAUtil rsaUtil = new RSAUtil();
        String de_app_key = request.getParameter("App_Key");
        String de_user_id = userUtil.queryUserIdByApp_key(de_app_key);
        if (de_user_id != "" && de_user_id != null) {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String data = "您好！请将App_Key进行RSA加密传输！";
            logger.info("用户"+de_app_key+"正在发起请求，拒绝通过："+data);
            OutputStream stream = response.getOutputStream();
            stream.write(data.getBytes("UTF-8"));
            return false;
        }
        if (de_app_key == null || de_app_key.isEmpty()) {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String data = "您好，请传入App_Key！";
            logger.info("用户"+de_app_key+"正在发起请求，拒绝通过："+data);
            OutputStream stream = response.getOutputStream();
            stream.write(data.getBytes("UTF-8"));
            return false;
        }
        String app_key = rsaUtil.privateDecrypt(de_app_key, RSAUtil.getPrivateKey(pri_key));
        if (app_key==null || app_key.equals("")){
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String data = "您好，传入的App_Key为空！";
            logger.info("用户"+de_app_key+"正在发起请求，拒绝通过："+data);
            OutputStream stream = response.getOutputStream();
            stream.write(data.getBytes("UTF-8"));
            return false;
        }
        String user_id = userUtil.queryUserIdByApp_key(app_key);
        if (!user_id.isEmpty()) {
            logger.info("用户"+user_id+"正在发起请求，允许通过！");
            request.setAttribute("user_id",user_id);
            return true;
        }
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        String data = "您好，该用户暂时没有访问权限，请联系管理员！";
        OutputStream stream = response.getOutputStream();
        stream.write(data.getBytes("UTF-8"));
        return false;
    }
}
