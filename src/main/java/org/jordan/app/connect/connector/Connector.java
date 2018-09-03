package org.jordan.app.connect.connector;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/23下午10:49
 */
public interface Connector<T> {
    T connect();
}
