package org.jordan.app.connect.model;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.jordan.app.connect.utils.MyFileUtils;
import org.jordan.app.connect.utils.XmlUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Setter
@Getter
@ToString
@XmlRootElement
@EqualsAndHashCode
public class MysqlConfigs extends ConnectConfig implements Serializable{
    private static final long serialVersionUID = -4458074172372300213L;
    private MysqlConfigs() {

    }

    private static MysqlConfigs mysqlConfigs = new MysqlConfigs();
    public static MysqlConfigs getInstance() {
        return mysqlConfigs;
    }

    @Override
    public void saveConfig(ConfigParam configParam) {
        CONFIG.put(configParam.getId(), configParam);
        if (CollectionUtils.isNotEmpty(getConfigs())) {
            getConfigs().clear();
            CONFIG.forEach((k, v) -> {
                ConfigParam param = CONFIG.get(k);
                if (param.getType().equals(ConfigParam.ConfigType.MYSQL)) {
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
        File configFile = new File(MyFileUtils.getUserDir() + File.separator + "config" + File.separator + ConfigParam.ConfigType.MYSQL + ".xml");
        if (configFile.exists()) {
            try {
                MysqlConfigs jdbcParam = XmlUtils.xmlToBean(FileUtils.readFileToString(configFile, "utf-8"), getClass());
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
