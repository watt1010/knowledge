package com.inspur.tax.commen.service;

import com.inspur.tax.commen.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public Map queryUserIdByApp_key(Map map) {
        return userDao.queryUserIdByApp_key(map);
    }
}
