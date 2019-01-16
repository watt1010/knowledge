package com.inspur.tax.core.nlp.cosinesimlarity.datastructs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 底层采用hashMap实现用于每次向内增加文本时都能取出文本的添加次数，而不是覆盖掉Key
 * 计数专用map
 *
 * @param <T>
 * @author 王辉
 */
public class MapCount<T> {
    private HashMap<T, Integer> hm = null;

    public MapCount() {
        this.hm = new HashMap<>();
    }

    public MapCount(int initialCapacity) {
        this.hm = new HashMap<>(initialCapacity);
    }

    public void add(T t, int n) {
        Integer integer = null;
        if((integer = (Integer)this.hm.get(t)) != null) {
            this.hm.put(t, Integer.valueOf(integer.intValue() + n));
        } else {
            this.hm.put(t, Integer.valueOf(n));
        }

    }

    public void add(T t) {
        this.add(t, 1);
    }

    public int size() {
        return this.hm.size();
    }

    public void remove(T t) {
        this.hm.remove(t);
    }

    public HashMap<T, Integer> get() {
        return this.hm;
    }

    public String getDic() {
        Iterator iterator = this.hm.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        Entry next = null;

        while(iterator.hasNext()) {
            next = (Entry)iterator.next();
            sb.append(next.getKey());
            sb.append("\t");
            sb.append(next.getValue());
            sb.append("\n");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(9223372036854775807L);
    }
}
