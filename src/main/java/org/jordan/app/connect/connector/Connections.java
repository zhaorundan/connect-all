package org.jordan.app.connect.connector;

import org.apache.commons.lang3.StringUtils;
import org.jordan.app.connect.exception.JDBCException;
import org.jordan.app.connect.model.ConfigParam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/27下午7:12
 */
public class Connections {
    private static Map<String, Connection> pool = new ConcurrentHashMap<>();

    public static Connection getConnection(ConfigParam jdbcParam) {

        Connection connection = pool.get(jdbcParam.getId());
        if (connection == null) {
            connection = createConnection(jdbcParam);
            pool.put(jdbcParam.getId(), connection);
        }
        return connection;
    }

    public static Map<String, Connection> getPool() {
        return pool;
    }

    public static Connection getConnection(String jdbcId) {
        ConfigParam jdbcParam = ConnectionConfigs.configs.get(jdbcId);
        return getConnection(jdbcParam);
    }


    private  static Connection createConnection(ConfigParam jdbcParam)  {
        Connection conn = null ;
        String url = "jdbc:mysql://"+jdbcParam.getHost()+":"+jdbcParam.getPort();
        if (StringUtils.isNotBlank(jdbcParam.getDatebase())) {
            url = url + "/" + jdbcParam.getDatebase();
        }
        url = url + "?zeroDateTimeBehavior=convertToNull&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true&amp;failOverReadOnly=false";
        if (jdbcParam.isUseSSL()) {
            url = url + "&amp;useSSL=true";
        } else {
            url = url + "&amp;useSSL=false";
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, jdbcParam.getUserName(), jdbcParam.getPassword());
        } catch (Exception e) {
            throw new JDBCException("创建jdbc连接失败");
        }

        return conn;
    }


}
