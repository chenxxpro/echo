package net.nextabc.echo;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
final public class Routable {

    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_DELETE = "DELETE";
    public static final String HTTP_PUT = "PUT";
    public static final String HTTP_OPTION = "OPTION";
    public static final String HTTP_ALL = "ALL";

    final UriMatcher matcher;
    private final String method;

    public Routable(String pattern, String method) {
        this.matcher = new UriMatcher(pattern);
        this.method = method.toUpperCase();
    }

    boolean match(Request request) {
        final String m = request.method();
        return (HTTP_ALL.equals(m) || this.method.equals(m))
                && matcher.match(request.uri());
    }

    /* Factories */

    public static Routable GET(String pattern) {
        return new Routable(pattern, HTTP_GET);
    }

    public static Routable POST(String pattern) {
        return new Routable(pattern, HTTP_POST);
    }

    public static Routable DELETE(String pattern) {
        return new Routable(pattern, HTTP_DELETE);
    }

    public static Routable PUT(String pattern) {
        return new Routable(pattern, HTTP_PUT);
    }

    public static Routable OPTION(String pattern) {
        return new Routable(pattern, HTTP_OPTION);
    }

    public static Routable ALL(String pattern) {
        return new Routable(pattern, HTTP_ALL);
    }
}
