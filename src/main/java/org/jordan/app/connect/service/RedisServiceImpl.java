package org.jordan.app.connect.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jordan.app.connect.exception.ConnectionException;
import org.jordan.app.connect.model.ConfigParam;
import org.jordan.app.connect.model.RedisConfigs;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.List;

@Slf4j
public class RedisServiceImpl {
    private static RedisServiceImpl redisService = new RedisServiceImpl();

    private RedisServiceImpl() {
    }

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

    public Jedis getConnection(String configId) {
        Jedis jedis = RedisConfigs.connections.get(configId);
        if (jedis == null) {
            ConfigParam configParam = RedisConfigs.CONFIG.get(configId);
            jedis = new Jedis(configParam.getHost(), configParam.getPort());
            if (StringUtils.isNotBlank(configParam.getPassword())) {

                jedis.auth(configParam.getPassword());
            }
            if (jedis == null) {
                throw new ConnectionException("jediscli connect failed");
            }

            RedisConfigs.connections.put(configId, jedis);
        }
        return RedisConfigs.connections.get(configId);
    }


    public boolean testConnection(String configId) throws Exception {

        Jedis jedis = RedisConfigs.connections.get(configId);
        if (jedis == null) {
            jedis = getConnection(configId);
            try {
                jedis.ping();
            } catch (Exception e) {
                e.printStackTrace();
                throw new ConnectionException("jediscli connect test failed");
            }
        }
        return false;
    }

    public Jedis getJedis(String configId) {
        if (MapUtils.isEmpty(RedisConfigs.connections)) {
            getConnection(configId);
        }
        if (RedisConfigs.connections.get(configId) == null) {
            getConnection(configId);
        }
        return RedisConfigs.connections.get(configId);
    }

    public int listDatabases(String configId) {
        Jedis jedis = getJedis(configId);
        String databaseCount = jedis.configGet("databases").get(1);

        return Integer.valueOf(databaseCount);
    }
    public long getDbSize(String configId,int dbIndex) {
        Jedis jedis = getJedis(configId);
        jedis.select(dbIndex);
        return jedis.dbSize();
    }


    public List<ConfigParam> initConfig() {
        RedisConfigs.getInstance().initConfig();
        return RedisConfigs.getInstance().getConfigs();
    }

    public void listDataWithPage(String configId) {
        Jedis jedis = getJedis(configId);
        ScanParams scanParams = new ScanParams().count(50).match("*");
        String cur = ScanParams.SCAN_POINTER_START;

        do {
            ScanResult<String> scanResult = jedis.scan(cur, scanParams);
            // work with result
            scanResult.getResult();
            cur = scanResult.getStringCursor();

        } while (!cur.equals(ScanParams.SCAN_POINTER_START));

        log.info("db size:{}",jedis.getDB());
    }

}
