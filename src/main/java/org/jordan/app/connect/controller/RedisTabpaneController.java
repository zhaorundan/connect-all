package org.jordan.app.connect.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jordan.app.connect.model.ConfigParam;
import org.jordan.app.connect.model.MysqlConfigs;
import org.jordan.app.connect.service.MysqlServiceImpl;
import org.jordan.app.connect.service.RedisServiceImpl;
import org.jordan.app.connect.utils.ShortUUID;
import org.jordan.app.connect.view.RedisTabpaneView;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class RedisTabpaneController extends RedisTabpaneView {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initConfigs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ContextMenu menu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        MenuItem open = new MenuItem("Open");

        menu.getItems().addAll(delete,open);
        redisListview.setContextMenu(menu);

        delete.setOnAction(  event ->{
            Label label = redisListview.getSelectionModel().getSelectedItem();
            redisListview.getItems().remove(label);
            deleteConfig(label.getId());
        });

        open.setOnAction( event -> {
            Label label = redisListview.getSelectionModel().getSelectedItem();
            try {
                addConsoleTab(label.getText(), label.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public Tab addConsoleTab(String tabName, String jdbcId) throws Exception {
        Tab tab = new Tab();
        tab.setText(tabName);
        tab.setClosable(true);
        tab.setId("tab"+jdbcId);
        tab.setOnClosed(event -> {
//            mysqlTabpane.getTabs().clear();
        });

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/RedisTabpane.fxml"));
        SplitPane anchorPane = fxmlLoader.load();
        MysqlConsoleController consoleController = fxmlLoader.getController();
        consoleController.setJdbcId(jdbcId);

        tab.setContent(anchorPane);

        consoleController.setJdbcId(jdbcId);

        redisTabpane.getTabs().add(tab);
        redisTabpane.getSelectionModel().select(tab);
        consoleController.initData();
        consoleController.initView();
        return tab;
    }
    private void deleteConfig(String configId) {
        MysqlServiceImpl.getInstance().delConfig(configId);
        if (CollectionUtils.isEmpty(MysqlConfigs.CONFIG.values())) {
            resetConfig(false);
        }

    }
    private void initConfigs(){

        List<ConfigParam> configParams = RedisServiceImpl.getInstance().initConfig();
        if (CollectionUtils.isNotEmpty(configParams)) {
            for (ConfigParam configParam : configParams) {
                Label label = new Label();
                label.setId(configParam.getId());
                label.setText(configParam.getName());
                redisListview.getItems().add(label);
            }
        }
    }

    public void clickview() {

    }

    public void addConfig() {
        resetConfig();
    }

    public void connectTest() {

    }
    public void saveConfig() {
        try {
            ConfigParam jdbcParam = getConfigParam();
            jdbcParam.setType(ConfigParam.ConfigType.REDIS);
            if (StringUtils.isBlank(configId.getText())) {
                jdbcParam.setId(ShortUUID.generateShortUuid().toLowerCase());
                jdbcParam.setCreateTime(new Date());
                jdbcParam.setUpdateTime(new Date());
                Label label = new Label();
                label.setText(jdbcParam.getName());
                label.setId(jdbcParam.getId());
                redisListview.getItems().add(label);
                redisListview.getSelectionModel().select(label);
            } else {
                ConfigParam configParam = MysqlConfigs.CONFIG.get(jdbcParam.getId());
                jdbcParam.setCreateTime(configParam.getCreateTime());
                jdbcParam.setUpdateTime(new Date());
                Label label = redisListview.getSelectionModel().getSelectedItem();
                if (label != null) {
                    label.setText(jdbcParam.getName());
                }
            }
            MysqlServiceImpl.getInstance().saveConfig(jdbcParam);
            configId.setText(jdbcParam.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ConfigParam getConfigParam() {
        ConfigParam jdbcParam = new ConfigParam();
        jdbcParam.setName(connName.getText());
        jdbcParam.setPassword(password.getText());
        jdbcParam.setPort(StringUtils.isBlank(port.getText()) ? 3306 : Integer.valueOf(port.getText()));
        jdbcParam.setUseSSL(isSSL.isSelected());
        if (StringUtils.isNotBlank(configId.getText())) {
            jdbcParam.setId(configId.getText());
        }
        return jdbcParam;
    }


    public void resetConfig() {
        resetConfig(true);
    }

    /**
     * 重置按钮
     * @param exists
     */
    private void resetConfig(boolean exists) {
        connName.setText("");
        host.setText("");
        password.setText("");
        port.setText("");
        isSSL.setSelected(false);
        if (!exists) {
            configId.setText("");
        }
    }
}
