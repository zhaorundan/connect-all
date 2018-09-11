package org.jordan.app.connect.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jordan.app.connect.connector.ConnectionConfigs;
import org.jordan.app.connect.model.ConfigParam;
import org.jordan.app.connect.utils.MyFileUtils;
import org.jordan.app.connect.utils.ShortUUID;
import org.jordan.app.connect.utils.XmlUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ConfigServiceImpl {


    public String save(ConfigParam config) {
        if (StringUtils.isBlank(config.getId())) {
            config.setId(ShortUUID.generateShortUuid());
            config.setCreateTime(new Date());
            config.setUpdateTime(new Date());
        } else {
            config.setUpdateTime(new Date());
        }
        ConnectionConfigs.configs.put(config.getId(), config);
        persistenConfig();
        return config.getId();
    }
    private void persistenConfig() {
        String path = MyFileUtils.getUserDir();
        File configFile = new File(path + File.separator + "config" + File.separator + "mysql" + File.separator + ConnectionConfigs.configFileName + ".xml");
        try {
            String jdbcConfigXml = XmlUtils.beanToXml(ConnectionConfigs.configs);
            FileUtils.writeStringToFile(configFile, jdbcConfigXml, "utf-8", false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
