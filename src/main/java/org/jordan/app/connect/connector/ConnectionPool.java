package org.jordan.app.connect.connector;

import com.google.common.collect.Lists;
import org.jordan.app.connect.model.JDBCParam;
import org.jordan.app.connect.utils.StringUtils;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/27下午7:12
 */
public class ConnectionPool {
    private static Map<String, Connection> pool = new ConcurrentHashMap<>();


    public static Connection getConnection(JDBCParam jdbcParam) {
        Connection connection = pool.get(jdbcParam.getId());
        if (connection == null) {
            connection = createConnection(jdbcParam);
            pool.put(jdbcParam.getId(), connection);
        }
        return connection;
    }

    private static Connection createConnection(JDBCParam jdbcParam) {
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
            conn = DriverManager.getConnection(url,jdbcParam.getUserName(),jdbcParam.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static List<String> getDatabases(String jdbcId) {
        Connection connection = pool.get(jdbcId);
        List<String> databases = Lists.newArrayList();
        try {
            ResultSet resultSet = connection.getMetaData().getCatalogs();
            while (resultSet.next()) {
                String database = resultSet.getString("TABLE_CAT");
                databases.add(database);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return databases;
    }

    public static List<String> getTablesOfDatabase(String database, String jdbcId) {
        List<String> tables = Lists.newArrayList();
        Connection connection = pool.get(jdbcId);
        try {
            ResultSet set = connection.getMetaData().getTables(database, null, null, new String[]{"TABLE"});
            while (set.next()) {

                tables.add(set.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }
}
