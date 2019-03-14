import java.io.Serializable;
import java.util.HashMap;

public class MapCount<T> implements Serializable {
    private HashMap<T, int[]> data = null;

    //[0] 表示词频
    //【1】文档数
    //【2】index
    MapCount() {
        this.data = new HashMap<>();
    }

    public MapCount(int initialCapacity) {
        this.data = new HashMap<>(initialCapacity);
    }

    private void add(T key, int index, int n) {
        int[] value;
        if ((value = this.data.get(key)) != null) {
            value[index] = value[index] + n;
            this.data.put(key, value);
        } else {
            value = new int[3];
            value[index] = value[index] + n;
            this.data.put(key, value);
        }

    }

    void add(T key, int index) {
        this.add(key, index, 1);
    }

    public int size() {
        return this.data.size();
    }

    public void remove(T t) {
        this.data.remove(t);
    }

    public HashMap<T, int[]> get() {
        return this.data;
    }
}