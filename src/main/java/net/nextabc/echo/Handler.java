package net.nextabc.echo;

/**
 * 请求处理接口
 *
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
@FunctionalInterface
public interface Handler {

    /**
     * 处理请求，返回响应
     *
     * @param request  请求对象
     * @param response 响应对象
     * @throws Exception 处理抛出异常
     */
    void process(Request request, Response response) throws Exception;

}
