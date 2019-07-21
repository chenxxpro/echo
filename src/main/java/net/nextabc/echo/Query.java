package net.nextabc.echo;

import net.nextabc.echo.util.CastKit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class Query implements Map<String, Object> {

    private final Map<String, Object> mRealMap;

    public Query(Map<String, Object> realMap) {
        mRealMap = realMap;
    }

    // Overrides

    @Override
    public int size() {
        return mRealMap.size();
    }

    @Override
    public boolean isEmpty() {
        return mRealMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return mRealMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return mRealMap.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return mRealMap.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return mRealMap.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return mRealMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        mRealMap.putAll(m);
    }

    @Override
    public void clear() {
        mRealMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return mRealMap.keySet();
    }

    @Override
    public Collection<Object> values() {
        return mRealMap.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return mRealMap.entrySet();
    }

    // Extensions

    public <T> T getCast(String key, T defaultValue) {
        final Object value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return (T) value;
        }
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    public float getFloat(String key) {
        return getFloat(key, 0f);
    }

    public double getDouble(String key) {
        return getDouble(key, 0.0);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public Integer getInt(String key, Integer defaultValue) {
        final Object value = get(key);
        return CastKit.castInt(value, defaultValue);
    }

    public Long getLong(String key, Long defaultValue) {
        final Object value = get(key);
        return CastKit.castLong(value, defaultValue);
    }

    public Double getDouble(String key, Double defaultValue) {
        final Object value = get(key);
        return CastKit.castDouble(value, defaultValue);
    }

    public Float getFloat(String key, Float defaultValue) {
        final Object value = get(key);
        return CastKit.castFloat(value, defaultValue);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        final Object value = get(key);
        return CastKit.castBoolean(value, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        final Object value = get(key);
        return CastKit.castString(value, defaultValue);
    }

    @Override
    public String toString() {
        return mRealMap.toString();
    }
}