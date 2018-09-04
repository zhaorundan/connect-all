package org.jordan.app.connect.exception;

/**
 * @author zhaord
 * @Description:
 * @date 2018/9/3下午4:34
 */
public class JDBCException extends RuntimeException {
    public JDBCException(String message) {
        super(message);
    }

    public JDBCException() {
    }
}
