package net.nextabc.echo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
class UriMatcher {

    private final String pattern;
    private final boolean dynamic;
    private final boolean wilcard;
    private final List<UriSegment> segments;

    UriMatcher(String definePattern) {
        if (null == definePattern || definePattern.isEmpty()) {
            throw new IllegalArgumentException("Define pattern muste be specified");
        }
        this.pattern = definePattern;
        if (UriSegment.WILDCARD.equals(this.pattern)) {
            dynamic = false;
            wilcard = true;
            segments = Collections.emptyList();
        } else {
            // 解析定义的路由路径
            final String[] array = Texts.split(definePattern, "/");
            boolean dynamic = false;
            boolean wildcard = false;
            for (int i = 0; i < array.length; i++) {
                final String item = array[i].trim();
                if (UriSegment.WILDCARD.equals(item)) {
                    wildcard = true;
                    // 通配符必须是最后一个分段
                    if (i != array.length - 1) {
                        throw new IllegalArgumentException("Wildcard( * ) MUST BE the last segment in: " + definePattern);
                    }

                } else {
                    // 存在 {xxx} 这样的字段时，为动态参数模式：
                    final int startIndex = item.indexOf('{');
                    final int endIndex = startIndex >= 0 ? item.indexOf('}', startIndex) : -1;
                    dynamic = startIndex >= 0 && endIndex > startIndex;
                }
                // 通配符号和动态参数不能同时存在
                if (dynamic && wildcard) {
                    throw new IllegalArgumentException("Wildcard( * ) or DynamicParameter( {$NAME} ) MUST be ONE of them in: " + definePattern);
                }
            }

            this.dynamic = dynamic;
            this.wilcard = wildcard;
            // 动态参数模式，或者通配符号模式，需要解析URI各个分段
            if (dynamic || wildcard) {
                this.segments = new ArrayList<>(array.length);
                for (short i = 0; i < array.length; i++) {
                    segments.add(new UriSegment(i, array[i]));
                }
            } else {
                // 否则（静态匹配），不需要解析了，直接对比全路径即可。
                segments = Collections.emptyList();
            }
        }
    }

    public String pattern() {
        return pattern;
    }

    /**
     * 与请求URI对比，检查是否匹配。
     * 匹配规则如下：
     * 1. 定义路由为静态URI的，全字段匹配： /users/profile 只能匹配  /users/profile；
     * <p>
     * 2. 定义路由为动态参数URI的，分段长度相同，动态字段部分忽略：
     * /users/{username} 匹配 /users/yoojia 或 /users/chen，但不匹配  /users/yoojia/profiles；
     * <p>
     * 3. 定义为通配符的，前部分全匹配，通配符后自动忽略：
     * /users/* 匹配 /users/yoojia, /users/yoojia/profile 等；
     *
     * @param requestUri 请求的URI
     * @return 是否匹配
     */
    boolean match(String requestUri) {
        // Any
        if (UriSegment.WILDCARD.equals(this.pattern)) {
            return true;
        }
        // test matches
        if (dynamic || wilcard) {
            final String[] requests = Texts.split(requestUri, "/");
            final int defineLength = segments.size();
            if (dynamic) {
                return requests.length == defineLength
                        && segments.stream()
                        .allMatch((segment ->
                                segment.match(requests[segment.index])));
            } else/* if (wildcard)*/ {
                // 如果定义的路径长度大于请求路径，不匹配： /api/users/* 对比请求： /users
                if (defineLength > requests.length) {
                    return false;
                }
                // 定义路径长度 小于等于 请求路径:
                // 分段中，任何一个分段不匹配，则结果是不匹配。
                for (int i = 0; i < defineLength; i++) {
                    final UriSegment seg = segments.get(i);
                    // 通配符号，后面全部匹配
                    if (seg.wildcard) {
                        break;
                    }
                    if (!seg.match(requests[i])) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return pattern.equals(requestUri);
        }
    }
}
