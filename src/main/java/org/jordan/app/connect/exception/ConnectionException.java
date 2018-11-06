package org.jordan.app.connect.exception;

/**
 * @author zhaord
 * @Description:
 * @date 2018/9/3下午4:34
 */
public class ConnectionException extends RuntimeException {
    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException() {
    }
}
