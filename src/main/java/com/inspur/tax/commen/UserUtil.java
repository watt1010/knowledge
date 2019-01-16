package com.inspur.tax.commen;

import com.inspur.tax.commen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户查询类
 */
@Controller
public class UserUtil {

    @Autowired
    UserService userService;

    /**
     * 根据app_key查询用户id
     *
     * @param app_key
     * @return
     */
    public String queryUserIdByApp_key(String app_key) {
        if (app_key.isEmpty()) {
            return "";
        }
        Map map = new HashMap();
        map.put("app_key", app_key);
        Map remap = userService.queryUserIdByApp_key(map);
        if (remap == null) {
            return "";
        }
        return remap.get("USER_ID").toString();

    }
}
