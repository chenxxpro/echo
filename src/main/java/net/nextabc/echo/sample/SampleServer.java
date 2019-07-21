package net.nextabc.echo.sample;

import net.nextabc.echo.Echo;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class SampleServer {

    public static void main(String[] args) {

        final Echo echo = new Echo();

        echo.BEFORE("/hello", (req, resp) -> {
            Echo.HALT(500, "{\"error\": \"gun\"}");
        });

        echo.GROUP("/wx", (group) -> {
            group.GET("/auth", (req, resp) -> {
                resp.json("{\"token\": \"yoojia#nextabc\"}");
            });
        });

        echo.serve();
    }
}
