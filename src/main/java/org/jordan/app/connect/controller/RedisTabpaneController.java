package org.jordan.app.connect.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jordan.app.connect.model.ConfigParam;
import org.jordan.app.connect.model.MysqlConfigs;
import org.jordan.app.connect.model.RedisConfigs;
import org.jordan.app.connect.service.RedisServiceImpl;
import org.jordan.app.connect.utils.ShortUUID;
import org.jordan.app.connect.view.RedisTabpaneView;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
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
            deleteConfig(label.getId());
            redisListview.getItems().remove(label);
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
    public void addConsoleTab(String tabName, String jdbcId) throws Exception {
        if (redisTabpane.getTabs().size() > 0) {
            for (Tab tab : redisTabpane.getTabs()) {
                if (tab.getId().equalsIgnoreCase("tab" + jdbcId)) {
                    redisTabpane.getSelectionModel().select(tab);
                    return;
                }
            }
        }

        Tab tab = new Tab();
        tab.setText(tabName);
        tab.setClosable(true);
        tab.setId("tab"+jdbcId);
        tab.setOnClosed(event -> {
//            mysqlTabpane.getTabs().clear();
        });

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/RedisConsole.fxml"));
        SplitPane splitPane = fxmlLoader.load();
        RedisConsoleController consoleController = fxmlLoader.getController();
        consoleController.setConfigId(jdbcId);

        tab.setContent(splitPane);

        consoleController.setConfigId(jdbcId);

        redisTabpane.getTabs().add(tab);
        redisTabpane.getSelectionModel().select(tab);
        consoleController.initData();
        consoleController.initView();
    }
    private void deleteConfig(String configId) {
        RedisServiceImpl.getInstance().delConfig(configId);
        if (CollectionUtils.isEmpty(RedisConfigs.CONFIG.values())) {
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

    public void clickview(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (event.getTarget() instanceof Label)) {
            log.info("listview dubbo click");
        } else {
            Label label = redisListview.getSelectionModel().getSelectedItem();
            if (label == null) {
                return;
            }
            ConfigParam jdbcParam = MysqlConfigs.CONFIG.get(label.getId());
            if (jdbcParam == null) {
                return;
            }
            connName.setText(jdbcParam.getName());
            host.setText(jdbcParam.getHost());
            password.setText(jdbcParam.getPassword());
            port.setText(jdbcParam.getPort().toString());
            isSSL.setSelected(jdbcParam.isUseSSL());
            if (StringUtils.isNotBlank(jdbcParam.getId())) {
                configId.setText(jdbcParam.getId());
            }
        }

    }

    public void addConfig() {
        resetConfig(false);
    }

    public void connectTest() {
        try {
            Label label = redisListview.getSelectionModel().getSelectedItem();
            RedisServiceImpl.getInstance().testConnection(label.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("success");
            alert.setHeaderText(null);
            alert.setContentText("连接成功!");

            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning ");
            alert.setHeaderText(null);
            alert.setContentText("连接失败!\n"+e.getMessage());

            alert.showAndWait();
        }
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
                ConfigParam configParam = RedisConfigs.CONFIG.get(jdbcParam.getId());
                jdbcParam.setCreateTime(configParam.getCreateTime());
                jdbcParam.setUpdateTime(new Date());
                Label label = redisListview.getSelectionModel().getSelectedItem();
                if (label != null) {
                    label.setText(jdbcParam.getName());
                }
            }
            RedisServiceImpl.getInstance().saveConfig(jdbcParam);
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
        jdbcParam.setHost(host.getText());
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
