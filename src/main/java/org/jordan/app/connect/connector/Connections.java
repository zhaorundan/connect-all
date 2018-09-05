package org.jordan.app.connect.connector;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.jordan.app.connect.exception.JDBCException;
import org.jordan.app.connect.model.JDBCParam;
import org.jordan.app.connect.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/27下午7:12
 */
@Component
@Getter
public class Connections {
    private Map<String, Connection> pool = new ConcurrentHashMap<>();


    public  Connection getConnection(JDBCParam jdbcParam) {
        Connection connection = pool.get(jdbcParam.getId());
        if (connection == null) {
            connection = createConnection(jdbcParam);
            pool.put(jdbcParam.getId(), connection);
        }
        return connection;
    }


    private  Connection createConnection(JDBCParam jdbcParam)  {
        Connection conn = null ;
        String url = "jdbc:mysql://"+jdbcParam.getHost()+":"+jdbcParam.getPort();
        if (StringUtils.isNotBlank(jdbcParam.getDatebase())) {
            url = url + "/" + jdbcParam.getDatebase();
        }
        url = url + "?zeroDateTimeBehavior=convertToNull&amp;useUnicode=true&amp;characterEncoding=utf-8";
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
