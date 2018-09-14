package org.jordan.app.connect.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jordan.app.connect.model.ConfigParam;
import org.jordan.app.connect.model.MysqlConfigs;
import org.jordan.app.connect.service.MysqlServiceImpl;
import org.jordan.app.connect.utils.ShortUUID;
import org.jordan.app.connect.view.MysqlTabpaneView;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/23上午9:22
 */
@Slf4j
public class MysqlTabpaneController extends MysqlTabpaneView {

    @FXML
    private Button mysqlCreate;

    @FXML
    private ListView<Label> mysqlListview;

    @FXML
    private TextField connName;
    @FXML
    private TextField host;
    @FXML
    private TextField userName;
    @FXML
    private TextField password;
    @FXML
    private TextField database;
    @FXML
    private TextField port;
    @FXML
    private CheckBox isSSL;
    @FXML
    private Button connButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label configId;


    @FXML
    private TabPane mysqlTabpane;

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
        mysqlListview.setContextMenu(menu);

        delete.setOnAction(  event ->{
            Label label = mysqlListview.getSelectionModel().getSelectedItem();
            mysqlListview.getItems().remove(label);
            deleteConfig(label.getId());
        });

        open.setOnAction( event -> {
            Label label = mysqlListview.getSelectionModel().getSelectedItem();
            try {
                addConsoleTab(label.getText(), label.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void deleteConfig(String configId) {
        MysqlServiceImpl.getInstance().delConfig(configId);
        if (CollectionUtils.isEmpty(MysqlConfigs.CONFIG.values())) {
            resetConfig(false);
        }

    }

    public void connectTest() {

        try {
            Label label = mysqlListview.getSelectionModel().getSelectedItem();
            MysqlServiceImpl.getInstance().testConnection(label.getId());
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

    private void initConfigs(){

        List<ConfigParam> configParams = MysqlServiceImpl.getInstance().initConfig();
        if (CollectionUtils.isNotEmpty(configParams)) {
            for (ConfigParam configParam : configParams) {
                Label label = new Label();
                label.setId(configParam.getId());
                label.setText(configParam.getName());
                mysqlListview.getItems().add(label);
            }
        }
    }

    /**
     * 创建按钮
     */
    public void addConfig() {
        resetConfig(false);
    }

    /**
     * 保存配置按钮
     */
    public void saveConfig() {
        try {
            ConfigParam jdbcParam = getJDBCParam();
            jdbcParam.setType(ConfigParam.ConfigType.MYSQL);
            if (StringUtils.isBlank(configId.getText())) {
                jdbcParam.setId(ShortUUID.generateShortUuid().toLowerCase());
                jdbcParam.setCreateTime(new Date());
                jdbcParam.setUpdateTime(new Date());
                Label label = new Label();
                label.setText(jdbcParam.getName());
                label.setId(jdbcParam.getId());
                mysqlListview.getItems().add(label);
                mysqlListview.getSelectionModel().select(label);
            } else {
                ConfigParam configParam = MysqlConfigs.CONFIG.get(jdbcParam.getId());
                jdbcParam.setCreateTime(configParam.getCreateTime());
                jdbcParam.setUpdateTime(new Date());
                Label label = mysqlListview.getSelectionModel().getSelectedItem();
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
        userName.setText("");
        password.setText("");
        database.setText("");
        port.setText("");
        isSSL.setSelected(false);
        if (!exists) {
            configId.setText("");
        }
    }

    @FXML
    public void clickview(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (event.getTarget() instanceof Label)) {
            log.info("listview dubbo click");
        } else {
            Label label = mysqlListview.getSelectionModel().getSelectedItem();
            if (label == null) {
                return;
            }
            ConfigParam jdbcParam = MysqlConfigs.CONFIG.get(label.getId());
            if (jdbcParam == null) {
                return;
            }
            connName.setText(jdbcParam.getName());
            host.setText(jdbcParam.getHost());
            userName.setText(jdbcParam.getUserName());
            password.setText(jdbcParam.getPassword());
            database.setText(jdbcParam.getDatebase());
            port.setText(jdbcParam.getPort().toString());
            isSSL.setSelected(jdbcParam.isUseSSL());
            if (StringUtils.isNotBlank(jdbcParam.getId())) {
                configId.setText(jdbcParam.getId());
            }
        }

    }
    private ConfigParam getJDBCParam() {
        ConfigParam jdbcParam = new ConfigParam();
        jdbcParam.setName(connName.getText());
        jdbcParam.setDatebase(database.getText());
        jdbcParam.setHost(host.getText());
        jdbcParam.setPassword(password.getText());
        jdbcParam.setPort(StringUtils.isBlank(port.getText()) ? 3306 : Integer.valueOf(port.getText()));
        jdbcParam.setUseSSL(isSSL.isSelected());
        jdbcParam.setUserName(userName.getText());
        if (StringUtils.isNotBlank(configId.getText())) {
            jdbcParam.setId(configId.getText());
        }
        return jdbcParam;
    }

    public Tab addConsoleTab(String tabName, String jdbcId) throws Exception {
        Tab tab = new Tab();
        tab.setText(tabName);
        tab.setClosable(true);
        tab.setId("tab"+jdbcId);
        tab.setOnClosed(event -> {
//            mysqlTabpane.getTabs().clear();
        });

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MysqlConsole.fxml"));
        SplitPane anchorPane = fxmlLoader.load();
        MysqlConsoleController consoleController = fxmlLoader.getController();
        consoleController.setJdbcId(jdbcId);

        tab.setContent(anchorPane);

        consoleController.setJdbcId(jdbcId);

        mysqlTabpane.getTabs().add(tab);
        mysqlTabpane.getSelectionModel().select(tab);
        consoleController.initData();
        consoleController.initView();
        return tab;
    }

}
