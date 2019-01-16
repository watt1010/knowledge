package com.inspur.tax.scene.controller;

import com.inspur.tax.scene.beans.SceneResponse;
import com.inspur.tax.scene.service.SceneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SceneController {

    private SceneService sceneService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SceneController(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    /*
     *根据id判断是否是场景
     */
    private Boolean isScene(String id, String user_id) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("user_id", user_id);
        Map remap = sceneService.isScene(map);
        if (null == remap) {
            return false;
        } else return remap.size() > 0;
    }

    /*
     *根据id查询节点话术
     */
    private String queryNodeSpeech(String id) {
        Map map = new HashMap();
        map.put("id", id);
        Map remap = sceneService.queryNodeSpeech(map);
        if (remap.size() > 0) {
            return remap.get("EN_SAY").toString();
        }
        logger.info("未查询到该id对应的话术");
        return null;
    }

    /*
     *根据id查询节点子节点
     */
    private List<Map> queryEnSon(String id, String user_id) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("user_id", user_id);
        List<Map> relist = sceneService.queryEnSon(map);
        if (relist.size() > 0) {
            return relist;
        }
        return null;
    }

    /**
     * 查询是否有子节点-判断是否是叶子结点
     *
     * @param map
     * @return
     */
    private List queryHaveSon(Map map) {
        return sceneService.queryHaveSon(map);
    }

    /*
     *用户回答匹配子实体（去除口语话）
     */
    private Map matchSon(List<Map> sonlist, String talk) {
        Map remap = new HashMap();
        int j = 0;
        for (int i = 0; i < sonlist.size(); i++) {
            Map map = sonlist.get(i);
            if (talk.contains(map.get("EN_NAME").toString()) || map.get("EN_NAME").toString().contains(talk)) {
                j = 1;
                remap = map;
                return remap;
            } else {
                Map samap = new HashMap();
                samap.put("en_name", map.get("EN_NAME").toString());
                List<String> list1 = sceneService.querySpeechWord(samap);
                for (int q = 0; q < list1.size(); q++) {
                    if (talk.contains(list1.get(q))) {
                        j = 1;
                        remap = map;
                        return remap;
                    }
                }
            }
        }
        return remap;
    }

    /*
     *多轮对话相关
     */

    public SceneResponse scene(@RequestParam(name = "en_id") String en_id, @RequestParam(name = "talk") String talk, @RequestParam(name = "user_id") String user_id) {
        logger.info("接收到多轮对话请求：en_id=" + en_id + "；talk=" + talk);
        if (en_id.isEmpty()) {
            logger.info("传入的en_id为空！");
            return new SceneResponse();
        }
        if (isScene(en_id, user_id) && talk.equals("")) {
            String say = queryNodeSpeech(en_id);
            if (say != null) {
                logger.info("--多轮对话开始--请求成功！返回反问话术：" + say + "");
                return new SceneResponse("请求成功！", en_id, say, 0, 0, 11);
            }
        } else {
            if (talk.equals("")) {
                logger.info("--多轮对话结束--用户未作出回答！");
                return new SceneResponse();
            }
            List<Map> sonlist = queryEnSon(en_id, user_id);
            Map map = matchSon(sonlist, talk);
            if (map.isEmpty()) {
                logger.info("--多轮对话结束--未匹配到该节点下的子节点!");
                return new SceneResponse();
            }
            List list = queryHaveSon(map);
            if (list.size() > 0) {
                logger.info("请求成功！返回反问话术：" + map.get("EN_SAY").toString());
                return new SceneResponse("请求成功！", map.get("EN_ID").toString(), map.get("EN_SAY").toString(), 0, 0, 11);
            } else {
                logger.info("--多轮对话结束--请求成功！返回答案：" + map.get("EN_SAY").toString() + "");
                return new SceneResponse("请求成功！", "", map.get("EN_SAY").toString(), 1, 0, 22);
            }
        }
        return new SceneResponse();
    }
}