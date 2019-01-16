package com.inspur.tax.mvc.controller;

import com.inspur.tax.mvc.beans.ExposedResult;
import com.inspur.tax.mvc.service.ExposedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExposedController {

    @Autowired
    ExposedService exposedService;

    /**
     * 查询热点问题Top5接口
     * @param request
     * @return
     */
    @RequestMapping("/queryTop5q")
    public ExposedResult queryTop5Qu(HttpServletRequest request) {
        String user = request.getParameter("user_id");
        Map map = new HashMap();
        map.put("user_id",user);
        List<Map> relist = exposedService.queryTop5Qu(map);
        List rellist = new ArrayList();
        String kw_id = "";
        for (int i = 0; i < relist.size(); i++) {
            Map remap = relist.get(i);
            String rekw = remap.get("kw_id").toString();
            if (kw_id.equals(rekw)){
                continue;
            }
            kw_id=rekw;
            rellist.add(remap);
        }
        return new ExposedResult(rellist);
    }

    }
