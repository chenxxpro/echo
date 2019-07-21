package net.nextabc.echo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class Echo implements EmbeddedServer.Application {

    private static final Logger LOG = LoggerFactory.getLogger(Echo.class);

    private final Dispatcher dispatcher = new Dispatcher();

    /**
     * 添加请求路由处理接口
     *
     * @param matcher 路由匹配配置
     * @param handler 请求处理接口
     * @return Echo
     */
    public Echo route(Matcher matcher, Handler handler) {
        dispatcher.addRoute(new Dispatcher.Routing(matcher, handler));
        return this;
    }

    /**
     * 添加Get请求处理接口
     *
     * @param pattern 处理的URI地址Pattern
     * @param handler 请求处理接口
     * @return Echo
     */
    public Echo GET(String pattern, Handler handler) {
        return route(Matcher.GET(pattern), handler);
    }

    /**
     * 添加Post请求处理接口
     *
     * @param pattern 处理的URI地址Pattern
     * @param handler 请求处理接口
     * @return Echo
     */
    public Echo POST(String pattern, Handler handler) {
        return route(Matcher.POST(pattern), handler);
    }

    /**
     * 添加Delete请求处理接口
     *
     * @param pattern 处理的URI地址Pattern
     * @param handler 请求处理接口
     * @return Echo
     */
    public Echo DELETE(String pattern, Handler handler) {
        return route(Matcher.DELETE(pattern), handler);
    }

    /**
     * 添加Put请求处理接口
     *
     * @param pattern 处理的URI地址Pattern
     * @param handler 请求处理接口
     * @return Echo
     */
    public Echo PUT(String pattern, Handler handler) {
        return route(Matcher.PUT(pattern), handler);
    }

    /**
     * 添加Option请求处理接口
     *
     * @param pattern 处理的URI地址Pattern
     * @param handler 请求处理接口
     * @return Echo
     */
    public Echo OPTION(String pattern, Handler handler) {
        return route(Matcher.OPTION(pattern), handler);
    }

    /**
     * 按分组注册请求处理接口
     *
     * @param path Group的前缀路径
     * @param work 分组处理接口
     * @return Echo
     */
    public Echo GROUP(String path, Consumer<RouteGroup> work) {
        work.accept(new RouteGroup(path, this));
        return this;
    }

    /**
     * 添加请求前拦截器处理接口
     *
     * @param matcher 路由配置
     * @param handler 请求处理接口
     * @return Echo
     */
    public Echo BEFORE(Matcher matcher, Handler handler) {
        dispatcher.addBefore(new Dispatcher.Routing(matcher, handler));
        return this;
    }

    /**
     * 添加请求前拦截器处理接口
     *
     * @param pattern URI配置
     * @param handler 请求处理接口
     * @return Echo
     */
    public Echo BEFORE(String pattern, Handler handler) {
        return BEFORE(Matcher.ALL(pattern), handler);
    }

    /**
     * 添加请求后拦截器处理接口
     *
     * @param matcher 路由配置
     * @param handler 请求处理接口
     * @return Echo
     */
    public Echo AFTER(Matcher matcher, Handler handler) {
        dispatcher.addAfter(new Dispatcher.Routing(matcher, handler));
        return this;
    }

    /**
     * 添加请求后拦截器处理接口
     *
     * @param pattern URI配置
     * @param handler 请求处理接口
     * @return Echo
     */
    public Echo AFTER(String pattern, Handler handler) {
        return AFTER(Matcher.ALL(pattern), handler);
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

    /**
     * Halt立即中断请求处理，返回一个状态码和Body数据到客户端。
     *
     * @param statusCode 状态码
     * @param body       Body数据
     * @throws HaltException Halt异常
     */
    public static void HALT(int statusCode, String body) throws HaltException {
        throw new HaltException(statusCode, body);
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
        this.dispatcher.serve(req, resp);
    }

}
