package net.nextabc.echo.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 参考alibaba.fastjson的实现
 *
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @since 1.0
 */
final public class CastKit {

    private CastKit() {
    }

    public static List castList(Object value, List defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof List) {
            return (List) value;
        }
        throw new ClassCastException("Object cannot cast to List, object: " + value);
    }

    public static Map castMap(Object value, Map defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Map) {
            return (Map) value;
        }
        throw new ClassCastException("Object cannot cast to Map, object: " + value);
    }

    public static String castString(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return String.valueOf(value);
    }

    public static Boolean castBoolean(final Object value, Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return ((Boolean) value);
        }
        return parseStringTo(value, defaultValue,
                Boolean::valueOf,
                arg -> {
                    if (arg instanceof Integer) {
                        return ((Integer) arg) > 0;
                    }
                    throw new ClassCastException("Object cannot cast to Boolean, object: " + value);
                });
    }

    public static Integer castInt(final Object value, final Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return parseStringTo(value, defaultValue,
                Integer::parseInt,
                arg -> {
                    if (arg instanceof Boolean) {
                        return ((Boolean) arg) ? 1 : 0;
                    }
                    throw new ClassCastException("Object cannot cast to Integer, object: " + value);
                });
    }

    public static Long castLong(final Object value, final Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return parseStringTo(value, defaultValue,
                Long::parseLong,
                arg -> {
                    if (arg instanceof Boolean) {
                        return ((Boolean) arg) ? 1L : 0;
                    }
                    throw new ClassCastException("Object cannot cast to Long, object: " + value);
                });
    }

    public static Float castFloat(final Object value, final Float defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return parseStringTo(value, defaultValue,
                Float::parseFloat,
                arg -> {
                    if (arg instanceof Boolean) {
                        return ((Boolean) arg) ? 1f : 0;
                    }
                    throw new ClassCastException("Object cannot cast to Float, object: " + value);
                });
    }

    public static Double castDouble(final Object value, final Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return parseStringTo(value, defaultValue,
                Double::parseDouble,
                arg -> {
                    if (arg instanceof Boolean) {
                        return ((Boolean) arg) ? 1.0 : 0;
                    }
                    throw new ClassCastException("Object cannot cast to Double, object: " + value);
                });
    }

    private static <T> T parseStringTo(Object value,
                                       T defaultValue,
                                       Function<String, T> parseAction,
                                       Function<Object, T> notStringAction) {
        if (value instanceof String) {
            String str = (String) value;
            if (str.length() == 0 || "null".equalsIgnoreCase(str)) {
                return defaultValue;
            }
            if (str.indexOf(',') != 0) {
                str = str.replaceAll(",", "");
            }
            try {
                return parseAction.apply(str);
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return notStringAction.apply(value);
    }

}