package org.jordan.app.connect.service;


import com.google.common.collect.Lists;
import org.jordan.app.connect.connector.ConnectionConfigs;
import org.jordan.app.connect.connector.Connections;
import org.jordan.app.connect.model.ConfigParam;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/23下午10:50
 */
public class MysqlServiceImpl {

    public boolean testConnection(ConfigParam jdbcParam) throws Exception {
        Connection conn = Connections.getConnection(jdbcParam);
        ResultSet rs = conn.createStatement().executeQuery("select 1");
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public List<String> listDatabases(String jdbcId) {
        Connection connection = Connections.getConnection(ConnectionConfigs.configs.get(jdbcId));
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

    public List<String> listTablesOfDatabase(String database, String jdbcId) {
        List<String> tables = Lists.newArrayList();
        Connection connection = Connections.getPool().get(jdbcId);
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

    /**
     * 查询表内容
     *
     * @param jdbcId
     * @param database
     * @param table
     */
    public void listPagingDataOfTable(String jdbcId, String database, String table) {
        Connection connection = Connections.getPool().get(jdbcId);
        try {
            DatabaseMetaData meta = connection.getMetaData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取表的所有列
     *
     * @param jdbcId
     * @param tableName
     * @return
     */
    public List<String> listColumnOfTable(String jdbcId, String database, String tableName) {
        List<String> columns = Lists.newArrayList();
        Connection connection = Connections.getPool().get(jdbcId);
        try {
            ResultSet set = connection.getMetaData().getColumns(database, null, tableName, null);
            while (set.next()) {
                columns.add(set.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }


}
