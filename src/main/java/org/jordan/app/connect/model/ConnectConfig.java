package org.jordan.app.connect.model;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jordan.app.connect.utils.MyFileUtils;
import org.jordan.app.connect.utils.XmlUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class ConnectConfig {

    public static Map<String, ConfigParam> CONFIG = Maps.newHashMap();
    protected List<ConfigParam> configs;
    public static String getConfigFileName(ConfigParam.ConfigType type) {
        return StringUtils.join(type, "-connect");
    }
    protected void save(List<ConfigParam> configs, ConfigParam.ConfigType configType){
        File configFile = new File(MyFileUtils.getUserDir() + File.separator + "config" + File.separator + configType + ".xml");
        try {
            String jdbcConfigXml = XmlUtils.beanToXml(this);
            FileUtils.writeStringToFile(configFile, jdbcConfigXml, "utf-8", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public abstract void saveConfig(ConfigParam configParam);

    public void delConfig(String configId) {
        ConfigParam configParam = CONFIG.get(configId);
        CONFIG.remove(configId);
        getConfigs().remove(configParam);
        save(getConfigs(),configParam.getType());
    }

    public abstract void initConfig();
}
