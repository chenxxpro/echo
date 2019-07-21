package net.nextabc.echo;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class RouteGroup {

    private final String path;
    private final Echo ref;

    RouteGroup(String path, Echo echo) {
        this.path = Texts.requireNonEmpty(path);
        if (path.contains(UriSegment.WILDCARD)) {
            throw new IllegalArgumentException("Group.path must be static/fixed path, was: " + path);
        }
        this.ref = echo;
    }

    public RouteGroup GET(String pattern, Handler handler) {
        ref.GET(groupPattern(pattern), handler);
        return this;
    }

    public RouteGroup POST(String pattern, Handler handler) {
        ref.POST(groupPattern(pattern), handler);
        return this;
    }

    public RouteGroup DELETE(String pattern, Handler handler) {
        ref.DELETE(groupPattern(pattern), handler);
        return this;
    }

    public RouteGroup PUT(String pattern, Handler handler) {
        ref.PUT(groupPattern(pattern), handler);
        return this;
    }

    public RouteGroup OPTION(String pattern, Handler handler) {
        ref.OPTION(groupPattern(pattern), handler);
        return this;
    }

    private String groupPattern(String pattern) {
        return this.path + pattern;
    }

}
