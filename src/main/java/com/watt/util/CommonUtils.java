package com.watt.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CommonUtils {
    /**
     * 给target按照score的大小重新从大到小排序
     * @return 排序后的array
     */
    public static JSONArray arrayCompare(JSONArray target) {
        JSONArray result = new JSONArray();
        for (int i = 0; target.size() > 0; i++) {
            JSONObject iobj = findMaxScore(target);
            removeOne(target, iobj);
            result.add(iobj);
        }
        return result;
    }

    /**
     * 获得JSON列表的top N
     *
     * @param target 目表array
     * @param n      多少个
     */
    public static JSONArray getTop(JSONArray target, int n) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < target.size() && i < n; i++) {
            result.add(target.get(i));
        }
        return result;
    }

    /**
     * 遍历整个list找出一个最大的Object
     */
    private static JSONObject findMaxScore(JSONArray target) {
        JSONObject firstObject = target.getJSONObject(0);
        for (int i = 1; i < target.size(); i++) {
            if (target.getJSONObject(i).getDouble("score") > firstObject.getDouble("score")) {
                firstObject = target.getJSONObject(i);
            }
        }
        return firstObject;
    }

    /**
     * 移除某个key为制定的object
     */
    private static void removeOne(JSONArray target, JSONObject one) {
        for (int i = 0; i < target.size(); i++) {
            if (target.getJSONObject(i).get("questionID").equals(one.get("questionID"))) {
                target.remove(i);
                return;
            }
        }
    }

//    public static void main(String[] args) {
//        JSONArray array = new JSONArray();
//        JSONObject object1 = new JSONObject();
//        JSONObject object2 = new JSONObject();
//        JSONObject object3 = new JSONObject();
//        JSONObject object4 = new JSONObject();
//
//        object1.put("score",2.44);
//        object1.put("key",2.44);
//        object2.put("score",4.454);
//        object2.put("key",4.454);
//        object3.put("score",3.24);
//        object3.put("key",3.24);
//        object4.put("score",16.00);
//        object4.put("key",16.00);
//        array.add(object1);
//        array.add(object2);
//        array.add(object3);
//        array.add(object4);
//        System.out.println(arrayCompare(array).toJSONString());
//    }
}
