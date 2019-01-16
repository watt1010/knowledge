package com.inspur.tax.mvc.service;

import com.inspur.tax.mvc.dao.ExposedDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExposedService {
    @Autowired
    ExposedDao exposedDao;
    public List<Map> queryTop5Qu(Map map) {
        return exposedDao.queryTop5Qu(map);
    }
}
