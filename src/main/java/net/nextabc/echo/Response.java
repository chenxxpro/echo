package net.nextabc.echo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class Response {

    public static final String CONTENT_TYPE_TEXT = "text/plain; charset=utf-8";
    public static final String CONTENT_TYPE_HTML = "text/html; charset=utf-8";
    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";

    private final HttpServletResponse response;

    private final PrintWriter bodyWriter;

    Response(HttpServletResponse response) throws IOException {
        this.response = response;
        this.bodyWriter = response.getWriter();
    }

    /**
     * 返回Servlet原始Response对象
     *
     * @return HttpServletResponse
     */
    public HttpServletResponse response() {
        return response;
    }

    public Response code(int sc) {
        response.setStatus(sc);
        return this;
    }

    public int code() {
        return response.getStatus();
    }

    public Response type(String contentType) {
        response.setContentType(contentType);
        return this;
    }

    public String type() {
        return response.getContentType();
    }

    public Response header(String name, String value) {
        response.addHeader(name, value);
        return this;
    }

    public Response length(int contentLength) {
        response.setContentLength(contentLength);
        return this;
    }

    public Response encoding(String encoding) {
        response.setCharacterEncoding(encoding);
        return this;
    }

    public String encoding() {
        return response.getCharacterEncoding();
    }

    /**
     * 返回JSON数据到客户端。
     *
     * @param jsonText JSON字符数据
     */
    public void json(String jsonText) {
        json(HttpServletResponse.SC_OK, jsonText.toCharArray());
    }

    /**
     * 返回JSON数据到客户端。
     *
     * @param statusCode 状态码
     * @param jsonText   JSON字符数据
     */
    public void json(int statusCode, String jsonText) {
        json(statusCode, jsonText.toCharArray());
    }

    /**
     * 返回JSON数据到客户端。
     *
     * @param statusCode 状态码
     * @param jsonObject JSON字符数据
     */
    public void json(int statusCode, Object jsonObject) {
        // TODO
    }

    /**
     * 返回JSON数据到客户端。
     *
     * @param statusCode 状态码
     * @param jsonChars  JSON字符数据
     */
    public void json(int statusCode, char[] jsonChars) {
        code(statusCode);
        type(CONTENT_TYPE_JSON);
        bodyWriter.write(jsonChars);
    }

    /**
     * Halt立即中断请求处理，返回一个状态码和Body数据到客户端。
     *
     * @param statusCode 状态码
     * @param body       Body数据
     * @throws HaltException Halt异常
     */
    public void halt(int statusCode, String body) throws HaltException {
        throw new HaltException(statusCode, body);
    }

    //// package access

    final void flush() {
        bodyWriter.flush();
    }
}
