package net.nextabc.echo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
class Dispatcher {

    private static final Logger LOG = LoggerFactory.getLogger(Dispatcher.class);

    private final List<Routing> before = new ArrayList<>();
    private final List<Routing> routes = new ArrayList<>();
    private final List<Routing> afters = new ArrayList<>();

    void addRoute(Routing routing) {
        routes.add(routing);
    }

    void addBefore(Routing routing) {
        before.add(routing);
    }

    void addAfter(Routing routing) {
        afters.add(routing);
    }

    void serve(Request request, Response response) {
        try {
            walkRoutes("BEFORE", before, request, response);
            walkRoutes("ROUTE", routes, request, response);
            walkRoutes("AFTER", afters, request, response);
        } catch (HaltException halt) {
            response.json(halt.statusCode, halt.body());
        } catch (Exception err) {
            LOG.error("Request dispatch error", err);
            response.json(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    AMap.from("location", "dispatcher").add("message", err.getMessage()));
        } finally {
            response.flush();
            Context.get().clear();
        }
    }

    private static void walkRoutes(String tag, List<Routing> routes, Request request, Response response) throws Exception {
        final String uri = request.uri();
        final boolean debug = LOG.isDebugEnabled();
        try {
            if (debug) {
                LOG.debug("{} START: {}", tag, uri);
            }
            routes.stream()
                    .filter(it -> it.matcher.match(request))
                    .forEach(pair -> work(pair, request, response, uri, tag));
        } finally {
            if (debug) {
                LOG.debug("{} END: {}", tag, uri);
            }
        }
    }

    private static void work(Routing routing, Request request, Response response, String uri, String tag) {
        final boolean debug = LOG.isDebugEnabled();
        if (debug) {
            LOG.debug("-> (#{}) {} START: {}", Thread.currentThread().getId(), tag, uri);
        }
        final long ts_start = System.currentTimeMillis();
        // 由匹配到的路由来处理接口
        try {
            routing.handler.process(request, response);
        } catch (HaltException halt) {
            throw halt;
        } catch (Exception err) {
            throw new GeneralException(err);
        } finally {
            if (debug) {
                long escaped = System.currentTimeMillis() - ts_start;
                LOG.debug("-> (#{}) {} END: {}, escaped: {}ms, pattern: {}, handler: {}",
                        Thread.currentThread().getId(), tag, uri, escaped,
                        routing.matcher.uriMatcher.pattern(), routing.handler);
            }
        }
    }

    ////

    final static class Routing {

        final Matcher matcher;
        final Handler handler;

        Routing(Matcher matcher, Handler handler) {
            this.matcher = matcher;
            this.handler = handler;
        }
    }
}
