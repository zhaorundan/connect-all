package org.jordan.app.connect.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.jordan.app.connect.utils.MyFileUtils;
import org.jordan.app.connect.utils.XmlUtils;
import redis.clients.jedis.Jedis;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

@Setter
@Getter
@ToString
@XmlRootElement
@EqualsAndHashCode
public class RedisConfigs extends ConnectConfig implements Serializable {
    private static final long serialVersionUID = -4458074172372300213L;
    private RedisConfigs(){}

    public static Map<String, Jedis> connections = Maps.newConcurrentMap();

    private static RedisConfigs redisConfigs = new RedisConfigs();
    public static RedisConfigs getInstance() {
        return redisConfigs;
    }
    @Override
    public void saveConfig(ConfigParam configParam) {
        CONFIG.put(configParam.getId(), configParam);
        if (CollectionUtils.isNotEmpty(getConfigs())) {
            getConfigs().clear();
            CONFIG.forEach((k, v) -> {
                ConfigParam param = CONFIG.get(k);
                if (param.getType().equals(ConfigParam.ConfigType.REDIS)) {
                    getConfigs().add(CONFIG.get(k));
                }
            });
            Collections.sort(getConfigs(), Comparator.comparing(ConfigParam::getCreateTime));

        } else {
            setConfigs(Lists.newArrayList());
            getConfigs().add(configParam);
        }
        save(getConfigs(),configParam.getType());
    }

    @Override
    public void initConfig() {
        File configFile = new File(MyFileUtils.getUserDir() + File.separator + "config" + File.separator + ConfigParam.ConfigType.REDIS + ".xml");
        if (configFile.exists()) {
            try {
                RedisConfigs jdbcParam = XmlUtils.xmlToBean(FileUtils.readFileToString(configFile, "utf-8"), RedisConfigs.class);
                if (jdbcParam != null) {
                    for (ConfigParam config : jdbcParam.getConfigs()) {
                        CONFIG.put(config.getId(), config);
                    }
                    setConfigs(jdbcParam.getConfigs());

                    Collections.sort(getConfigs(), Comparator.comparing(ConfigParam::getCreateTime));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
