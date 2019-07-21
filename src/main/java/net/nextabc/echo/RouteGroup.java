package net.nextabc.echo;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class RouteGroup {

    private final String path;
    private final EchoServer echo;

    RouteGroup(String path, EchoServer echo) {
        this.path = Texts.requireNonEmpty(path);
        if (path.contains(UriSegment.WILDCARD)) {
            throw new IllegalArgumentException("Group.path must be static/fixed path, was: " + path);
        }
        this.echo = echo;
    }

    public RouteGroup GET(String pattern, Handler handler) {
        echo.GET(groupPattern(pattern), handler);
        return this;
    }

    public RouteGroup POST(String pattern, Handler handler) {
        echo.POST(groupPattern(pattern), handler);
        return this;
    }

    public RouteGroup DELETE(String pattern, Handler handler) {
        echo.DELETE(groupPattern(pattern), handler);
        return this;
    }

    public RouteGroup PUT(String pattern, Handler handler) {
        echo.PUT(groupPattern(pattern), handler);
        return this;
    }

    public RouteGroup OPTION(String pattern, Handler handler) {
        echo.OPTION(groupPattern(pattern), handler);
        return this;
    }

    private String groupPattern(String pattern) {
        return this.path + pattern;
    }

}
