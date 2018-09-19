package org.jordan.app.connect.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jordan.app.connect.exception.ConnectionException;
import org.jordan.app.connect.model.ConfigParam;
import org.jordan.app.connect.model.Pager;
import org.jordan.app.connect.model.RedisConfigs;
import org.jordan.app.connect.model.RedisData;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
            jedis = new Jedis(configParam.getHost(), configParam.getPort(), Integer.MAX_VALUE);
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

    public long getDbSize(String configId, int dbIndex) {
        Jedis jedis = getJedis(configId);
        jedis.select(dbIndex);
        return jedis.dbSize();
    }


    public List<ConfigParam> initConfig() {
        RedisConfigs.getInstance().initConfig();
        return RedisConfigs.getInstance().getConfigs();
    }

    private Pipeline pipeline;

    public Pager<RedisData> listDataWithPager(String configId, int dbIndex) {
        return listDataWithPager(configId, dbIndex, "", false);
    }

    public Pager<RedisData> listDataWithPager(String configId, int dbIndex, String searchKey, boolean isFuzzy) {
        Pager<RedisData> pager = new Pager<>();
        Jedis jedis = getJedis(configId);
        jedis.select(dbIndex);
        long dbsize = jedis.dbSize();
        if (dbsize == 0) {
            return new Pager();
        }
        ScanParams scanParams ;
        if (StringUtils.isNotBlank(searchKey)) {
            if (isFuzzy) {
                scanParams = new ScanParams().count(Pager.PAGE_SIZE).match(searchKey);
            } else {
                scanParams = new ScanParams().count(Pager.PAGE_SIZE).match("*" + searchKey + "*");
            }

        } else {
            scanParams = new ScanParams().count(Pager.PAGE_SIZE).match("*");
        }

        String cur = ScanParams.SCAN_POINTER_START;

        ScanResult<String> scanResult = jedis.scan(cur, scanParams);
        // work with result
        List<String> keys = scanResult.getResult();
        cur = scanResult.getStringCursor();
        List<RedisData> redisDataList = Lists.newArrayList();
        pipeline = jedis.pipelined();
        Map<String, Response<String>> responseMap = Maps.newHashMap();
        for (String key : keys) {
            responseMap.put(key, pipeline.type(key));
        }
        pipeline.clear();
        for (String s : responseMap.keySet()) {
            redisDataList.add(new RedisData().setColumnData(responseMap.get(s).get(), s));
        }
        pipeline.sync();
        pager.setResult(redisDataList);
        pager.setCursor(cur);
        pager.setTotalCount(dbsize);
        log.info("db size:{}", dbsize);

        return pager;
    }

    public Pager<RedisData> listValueOfKey(String keytype, String key, String configId, int dbIndex) {
        Pager<RedisData> pager = new Pager<>();
        List<RedisData> redisDataList = Lists.newArrayList();
        try {
            Jedis jedis = getJedis(configId);
            jedis.select(dbIndex);
            String value;
            switch (keytype.toLowerCase()) {
                case "string":
                    value = jedis.get(key);
                    redisDataList.add(new RedisData().setStringValue(keytype, new String(value.getBytes("utf-8"))));
                    break;
                case "hash":
                    Map<String, String> valueMap = jedis.hgetAll(key);
                    for (String feild : valueMap.keySet()) {
                        redisDataList.add(new RedisData().setHashValue(keytype, new String(feild.getBytes("utf-8")), new String(valueMap.get(feild).getBytes("utf-8"))));
                    }
                    break;
                case "set":
                    Set<String> setValues = jedis.smembers(key);
                    for (String setValue : setValues) {
                        redisDataList.add(new RedisData().setStringValue(keytype, new String(setValue.getBytes("utf-8"))));
                    }
                    break;
                case "list":
                    List<String> listValues = jedis.lrange(key, 0, -1);
                    for (String listValue : listValues) {
                        redisDataList.add(new RedisData().setStringValue(keytype, new String(listValue.getBytes("utf-8"))));
                    }
                    break;
                case "zset":
                    Set<Tuple> tuples = jedis.zrangeWithScores(key, 0, -1);
                    for (Tuple tuple : tuples) {
                        redisDataList.add(new RedisData().setSortedSetValue(keytype, new String(tuple.getElement().getBytes("utf-8")), tuple.getScore()));
                    }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pager.setResult(redisDataList);
        return pager;
    }

    public Long getKeyTTL(String keytype, String key, String configId, int dbIndex) {
        Jedis jedis = getJedis(configId);
        jedis.select(dbIndex);
        Long ttl = jedis.ttl(key);
        return ttl;
    }

}
