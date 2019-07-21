package net.nextabc.echo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class EchoServer implements EmbeddedServer.Application {

    private static final Logger LOG = LoggerFactory.getLogger(EchoServer.class);

    private final List<RoutePair> before = new ArrayList<>();
    private final List<RoutePair> routes = new ArrayList<>();
    private final List<RoutePair> afters = new ArrayList<>();

    /**
     * 添加请求路由处理接口
     *
     * @param routable 路由配置
     * @param handler  请求处理接口
     * @return Echo
     */
    public EchoServer route(Routable routable, Handler handler) {
        routes.add(new RoutePair(routable, handler));
        return this;
    }

    /**
     * 添加Get请求处理接口
     *
     * @param pattern 处理的URI地址Pattern
     * @param handler 请求处理接口
     * @return Echo
     */
    public EchoServer GET(String pattern, Handler handler) {
        return route(Routable.GET(pattern), handler);
    }

    /**
     * 添加Post请求处理接口
     *
     * @param pattern 处理的URI地址Pattern
     * @param handler 请求处理接口
     * @return Echo
     */
    public EchoServer POST(String pattern, Handler handler) {
        return route(Routable.POST(pattern), handler);
    }

    /**
     * 添加Delete请求处理接口
     *
     * @param pattern 处理的URI地址Pattern
     * @param handler 请求处理接口
     * @return Echo
     */
    public EchoServer DELETE(String pattern, Handler handler) {
        return route(Routable.DELETE(pattern), handler);
    }

    /**
     * 添加Put请求处理接口
     *
     * @param pattern 处理的URI地址Pattern
     * @param handler 请求处理接口
     * @return Echo
     */
    public EchoServer PUT(String pattern, Handler handler) {
        return route(Routable.PUT(pattern), handler);
    }

    /**
     * 添加Option请求处理接口
     *
     * @param pattern 处理的URI地址Pattern
     * @param handler 请求处理接口
     * @return Echo
     */
    public EchoServer OPTION(String pattern, Handler handler) {
        return route(Routable.OPTION(pattern), handler);
    }

    /**
     * 按分组注册请求处理接口
     *
     * @param path Group的前缀路径
     * @param work 分组处理接口
     * @return Echo
     */
    public EchoServer GROUP(String path, Consumer<RouteGroup> work) {
        work.accept(new RouteGroup(path, this));
        return this;
    }

    /**
     * 添加请求前拦截器处理接口
     *
     * @param routable 路由配置
     * @param handler  请求处理接口
     * @return Echo
     */
    public EchoServer BEFORE(Routable routable, Handler handler) {
        before.add(new RoutePair(routable, handler));
        return this;
    }

    /**
     * 添加请求后拦截器处理接口
     *
     * @param routable 路由配置
     * @param handler  请求处理接口
     * @return Echo
     */
    public EchoServer AFTER(Routable routable, Handler handler) {
        afters.add(new RoutePair(routable, handler));
        return this;
    }

    /**
     * 启动服务器
     *
     * @param bindAddr 绑定地址
     * @param port     绑定端口
     */
    public void serve(String bindAddr, int port) {
        EmbeddedServer.run(this, bindAddr, port);
    }

    /**
     * 启动默认服务器，5570端口
     */
    public void serve() {
        serve("0.0.0.0", 5570);
    }

    private void serve(Request request, Response response) {
        try {
            walkRoutes("BEFORE", before, request, response);
            walkRoutes("ROUTE", routes, request, response);
            walkRoutes("AFTER", afters, request, response);
        } catch (HaltException he) {
            response.json(he.statusCode, he.body());
        } catch (Exception ge) {
            LOG.error("Request routing error", ge);
            response.json(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    AMap.from("location", "request routing")
                            .add("message", ge.getMessage()));
        } finally {
            response.flush();
            Context.get().clear();
        }
    }

    private static void walkRoutes(String tag, List<RoutePair> routes, Request request, Response response) throws Exception {
        final String uri = request.uri();
        final boolean debug = LOG.isDebugEnabled();
        try {
            if (debug) {
                LOG.debug("{} START: {}", tag, uri);
            }
            routes.stream()
                    .filter(it -> it.routable.match(request))
                    .forEach(pair -> work(pair, request, response, uri, tag));
        } finally {
            if (debug) {
                LOG.debug("{} END: {}", tag, uri);
            }
        }
    }

    private static void work(RoutePair pair, Request request, Response response, String uri, String tag) {
        final boolean debug = LOG.isDebugEnabled();
        if (debug) {
            LOG.debug("-> (#{}) {} START: {}", Thread.currentThread().getId(), tag, uri);
        }
        final long ts_start = System.currentTimeMillis();
        // Context
        // 由匹配到的路由来处理接口
        try {
            pair.handler.process(request, response);
        } catch (HaltException he) {
            throw he;
        } catch (Exception ge) {
            throw new GeneralException(ge);
        } finally {
            if (debug) {
                long escaped = System.currentTimeMillis() - ts_start;
                LOG.debug("-> (#{}) {} END: {}, escaped: {}ms, pattern: {}, handler: {}",
                        Thread.currentThread().getId(), tag, uri, escaped,
                        pair.routable.matcher.pattern(), pair.handler);
            }
        }
    }

    @Override
    public void onStart() {
        LOG.info("Echo: start");
    }

    @Override
    public void onStop() {
        LOG.info("Echo: stop");
    }

    @Override
    public void onHandle(HttpServletRequest request, HttpServletResponse response) {
        final Response resp;
        try {
            resp = new Response(response);
        } catch (IOException e) {
            LOG.error("Echo: 创建Response出错", e);
            return;
        }
        final Request req = new Request(request);
        this.serve(req, resp);
    }

    //////

    private final static class RoutePair {

        final Routable routable;
        final Handler handler;

        private RoutePair(Routable routable, Handler handler) {
            this.routable = routable;
            this.handler = handler;
        }
    }
}
