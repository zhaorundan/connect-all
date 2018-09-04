package org.jordan.app.connect.service;


import org.jordan.app.connect.connector.ConnectionPool;
import org.jordan.app.connect.model.JDBCParam;
import org.jordan.app.connect.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/23下午10:50
 */
@Service("mysqlService")
public class MysqlServiceImpl {
    private ConnectionPool connectionPool;

    public boolean testConnection(JDBCParam jdbcParam) throws Exception{
        Connection conn = ConnectionPool.getConnection(jdbcParam);
        ResultSet rs = conn.createStatement().executeQuery("select 1");
        if (rs.next()) {
            return true;
        }
        return false;
    }

}
