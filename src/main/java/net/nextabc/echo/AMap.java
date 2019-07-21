package net.nextabc.echo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class AMap extends HashMap<String, Object> {

    public AMap() {
    }

    public AMap(int initialCapacity) {
        super(initialCapacity);
    }

    public AMap(Map<? extends String, ?> m) {
        super(m);
    }

    public AMap add(String key, Object value) {
        put(key, value);
        return this;
    }

    public static AMap from(String key, Object value) {
        return new AMap().add(key, value);
    }
}
