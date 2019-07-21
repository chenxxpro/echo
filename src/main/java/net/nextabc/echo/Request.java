package net.nextabc.echo;

import javax.servlet.http.HttpServletRequest;

/**
 * 封装ServletRequest对象，并提供一些快捷方法
 *
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class Request {

    private final HttpServletRequest request;
    private final long createTime;

    Request(HttpServletRequest request) {
        this.request = request;
        this.createTime = System.currentTimeMillis();
    }

    /**
     * 返回原始ServletRequest对象。
     *
     * @return ServletRequest对象
     */
    public HttpServletRequest request() {
        return request;
    }

    /**
     * 返回Request对象创建时间。返回的时间为毫秒级的时间戳。
     *
     * @return 创建时间
     */
    public long createTime() {
        return createTime;
    }

    /**
     * 返回Http方法。确保返回HttpMethod为大写。
     *
     * @return Http方法
     */
    public String method() {
        return this.request.getMethod().toUpperCase();
    }

    /**
     * 返回请求URI地址。不包括Query参数部分
     *
     * @return URI地址
     */
    public String uri() {
        return this.request.getRequestURI();
    }

    /**
     * 返回请求Host
     *
     * @return Host
     */
    public String host() {
        return this.request.getHeader("Host");
    }

    /**
     * 返回客户端Port
     *
     * @return Port
     */
    public int remotePort() {
        return this.request.getRemotePort();
    }

    /**
     * 返回客户端Address
     *
     * @return Address
     */
    public String remoteAddr() {
        return this.request.getRemoteAddr();
    }

    /**
     * 返回User-Agent
     *
     * @return User-Agent
     */
    public String userAgent() {
        return this.request.getHeader("User-Agent");
    }

    /**
     * 返回ContentType
     *
     * @return ContentType
     */
    public String contentType() {
        return this.request.getContentType();
    }
}
