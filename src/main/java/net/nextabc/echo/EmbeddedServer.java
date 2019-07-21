package net.nextabc.echo;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class EmbeddedServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedServer.class);

    private final InetSocketAddress mAddress;
    private final Server jetty;

    public EmbeddedServer(String host, int port, Application app) {
        this.mAddress = InetSocketAddress.createUnresolved(host, port);
        jetty = new Server(mAddress);
        jetty.setHandler(new AbstractHandler() {
            @Override
            protected void doStart() throws Exception {
                super.doStart();
                app.onStart();
            }

            @Override
            protected void doStop() throws Exception {
                super.doStop();
                app.onStop();
            }

            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                app.onHandle(request, response);
            }
        });
    }

    public void run() throws Exception {
        LOGGER.debug("Server RUN on: {}", mAddress);
        jetty.start();
        jetty.join();
        jetty.stop();
    }

    //

    interface Application {

        void onStart();

        void onStop();

        void onHandle(HttpServletRequest request, HttpServletResponse response);
    }

    public static void run(Application app, String addr, int port) {
        try {
            new EmbeddedServer(addr, port, app).run();
        } catch (Exception e) {
            LOGGER.error("服务器运行异常，程序已退出", e);
        }
    }
}
