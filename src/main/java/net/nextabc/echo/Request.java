package net.nextabc.echo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class Request {

    private final HttpServletRequest request;
    private final long createTime;

    Request(HttpServletRequest request) {
        this.request = request;
        this.createTime = System.currentTimeMillis();
    }

    public HttpServletRequest request() {
        return request;
    }

    public long createTime() {
        return createTime;
    }

    public String method() {
        return this.request.getMethod();
    }

    public String uri() {
        return this.request.getRequestURI();
    }

    public String host() {
        return this.request.getRequestURI();
    }

    public int port() {
        return this.request.getRemotePort();
    }

    public String remoteAddr() {
        return this.request.getRemoteAddr();
    }

    public String userAgent() {
        return this.request.getHeader("User-Agent");
    }

    public String contentType() {
        return this.request.getContentType();
    }
}
