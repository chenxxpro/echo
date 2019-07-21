package net.nextabc.echo;

/**
 * 路由匹配器
 *
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
final public class Matcher {

    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_DELETE = "DELETE";
    public static final String HTTP_PUT = "PUT";
    public static final String HTTP_OPTION = "OPTION";
    public static final String HTTP_ALL = "ALL";

    final UriMatcher uriMatcher;
    private final String method;

    private Matcher(String pattern, String method) {
        this.uriMatcher = new UriMatcher(pattern);
        this.method = method.toUpperCase();
    }

    boolean match(Request request) {
        final String m = request.method();
        return (HTTP_ALL.equals(m) || this.method.equals(m))
                && uriMatcher.match(request.uri());
    }

    /* Factories */

    public static Matcher GET(String pattern) {
        return new Matcher(pattern, HTTP_GET);
    }

    public static Matcher POST(String pattern) {
        return new Matcher(pattern, HTTP_POST);
    }

    public static Matcher DELETE(String pattern) {
        return new Matcher(pattern, HTTP_DELETE);
    }

    public static Matcher PUT(String pattern) {
        return new Matcher(pattern, HTTP_PUT);
    }

    public static Matcher OPTION(String pattern) {
        return new Matcher(pattern, HTTP_OPTION);
    }

    public static Matcher ALL(String pattern) {
        return new Matcher(pattern, HTTP_ALL);
    }
}
