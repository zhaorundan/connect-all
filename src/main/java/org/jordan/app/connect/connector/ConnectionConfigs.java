package org.jordan.app.connect.connector;

import com.google.common.collect.Maps;
import org.jordan.app.connect.model.ConfigParam;

import java.util.Map;

public class ConnectionConfigs {
    public static Map<String, ConfigParam> configs = Maps.newHashMap();
    public static final String configFileName = ".connectall";
}
