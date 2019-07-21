package net.nextabc.echo;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
final class HaltException extends RuntimeException {

    final int statusCode;

    HaltException(int statusCode, String body) {
        super(body);
        this.statusCode = statusCode;
    }

    String body() {
        return this.getMessage();
    }
}
