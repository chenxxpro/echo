package net.nextabc.echo.sample;

import net.nextabc.echo.EchoServer;
import net.nextabc.echo.Routable;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class SampleServer {

    public static void main(String[] args) {

        final EchoServer echo = new EchoServer();

        echo.BEFORE(Routable.GET("/hello"), (req, resp) -> {
            resp.halt(500, "{\"error\": \"gun\"}");
        });

        echo.GROUP("/wx", (group) -> {
            group.GET("/auth", (req, resp) -> {
                resp.json("{\"token\": \"yoojia#nextabc\"}");
            });
        });

        echo.serve();
    }
}
