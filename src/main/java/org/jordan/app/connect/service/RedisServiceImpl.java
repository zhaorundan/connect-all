package org.jordan.app.connect.service;

import org.jordan.app.connect.model.ConfigParam;
import org.jordan.app.connect.model.RedisConfigs;

import java.util.List;

public class RedisServiceImpl {

    private static RedisServiceImpl redisService = new RedisServiceImpl();
    private RedisServiceImpl(){}
    public static RedisServiceImpl getInstance() {
        return redisService;
    }

    private RedisConfigs redisConfigs = RedisConfigs.getInstance();

    public void saveConfig(ConfigParam configParam) {
        redisConfigs.saveConfig(configParam);
    }

    public void delConfig(String configId) {
        redisConfigs.delConfig(configId);
    }

    public boolean testConnection(String configId) throws Exception {


        return false;
    }

    public List<String> listDatabases(String jdbcId) {

        return null;
    }

    public List<String> listTablesOfDatabase(String database, String jdbcId) {

        return null;
    }

    /**
     * 查询表内容
     *
     * @param jdbcId
     * @param database
     * @param table
     */
    public void listPagingDataOfTable(String jdbcId, String database, String table) {

    }

    /**
     * 获取表的所有列
     *
     * @param jdbcId
     * @param tableName
     * @return
     */
    public List<String> listColumnOfTable(String jdbcId, String database, String tableName) {

        return null;
    }

    public List<ConfigParam> initConfig() {
        RedisConfigs.getInstance().initConfig();
        return RedisConfigs.getInstance().getConfigs();
    }

}
