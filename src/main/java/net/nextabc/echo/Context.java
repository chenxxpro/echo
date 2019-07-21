package net.nextabc.echo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class Context extends Query {

    private static final ThreadLocal<Context> TL_CTX =
            ThreadLocal.withInitial(() -> new Context(new HashMap<>()));

    private Context(Map<String, Object> realMap) {
        super(realMap);
    }

    public static Context get() {
        return TL_CTX.get();
    }

}