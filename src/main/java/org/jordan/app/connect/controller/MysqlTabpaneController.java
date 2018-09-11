package org.jordan.app.connect.controller;

import com.google.common.collect.Lists;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jordan.app.connect.connector.ConnectionConfigs;
import org.jordan.app.connect.model.ConfigParam;
import org.jordan.app.connect.service.ConfigServiceImpl;
import org.jordan.app.connect.service.MysqlServiceImpl;
import org.jordan.app.connect.utils.MyFileUtils;
import org.jordan.app.connect.utils.ShortUUID;
import org.jordan.app.connect.utils.XmlUtils;
import org.jordan.app.connect.view.MysqlTabpaneView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
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

    private MysqlServiceImpl mysqlService = new MysqlServiceImpl();
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

    private void deleteConfig(String id) {
        String path = MyFileUtils.getUserDir();
        File configFile = new File(path + File.separator + "config" + File.separator + "mysql" + File.separator + id + ".xml");
        if (configFile.exists() && configFile.isFile()) {
            configFile.delete();
            ConnectionConfigs.configs.remove(id);
        }
        if (ConnectionConfigs.configs.isEmpty()) {
            resetConfig(false);
        }

    }

    public void connectTest() {

        try {
            Label label = mysqlListview.getSelectionModel().getSelectedItem();
            mysqlService.testConnection(ConnectionConfigs.configs.get(label.getId()));
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

    private void initConfigs() throws Exception {
        String path = MyFileUtils.getUserDir();
        File configFile = new File(path + File.separator + "config" + File.separator + "mysql");
        if (configFile.exists()) {
            List<Label> configs = Lists.newArrayList();
            if (configFile.listFiles() == null || configFile.listFiles().length == 0) {
                return;
            }
            for (File file : configFile.listFiles()) {
                Label label = new Label();
                ConfigParam jdbcParam = XmlUtils.xmlToBean(FileUtils.readFileToString(file, "utf-8"), ConfigParam.class);
                label.setText(jdbcParam.getName());
                label.setId(String.valueOf(jdbcParam.getId()));
                configs.add(label);
                ConnectionConfigs.configs.put(jdbcParam.getId(), jdbcParam);
            }
            mysqlListview.getItems().addAll(configs);
        }
    }

    /**
     * 创建按钮
     */
    public void addConfig() {
        resetConfig(false);
    }

    private ConfigServiceImpl configService = new ConfigServiceImpl();
    /**
     * 保存配置按钮
     */
    public void saveConfig() {
//        ConfigParam jdbcParam = getJDBCParam();
//        if (StringUtils.isBlank(jdbcParam.getId())) {
//            jdbcParam.setId(ShortUUID.generateShortUuid());
//            persistenConfig(jdbcParam);
//            Label label = new Label();
//            label.setText(jdbcParam.getName());
//            label.setId(jdbcParam.getId());
//            mysqlListview.getItems().add(label);
//        } else {
//            Label label = mysqlListview.getSelectionModel().getSelectedItem();
//            if (label != null) {
//                label.setText(jdbcParam.getName());
//            }
//            persistenConfig(jdbcParam);
//        }
//        configId.setText(jdbcParam.getId());
//        ConnectionConfigs.configs.put(jdbcParam.getId(), jdbcParam);
//        Iterator<Label> iterator = mysqlListview.getItems().iterator();
//        while (iterator.hasNext()) {
//            Label label = iterator.next();
//            if (label.getId().equalsIgnoreCase(jdbcParam.getId())) {
//                mysqlListview.getSelectionModel().select(label);
//            }
//        }
        ConfigParam jdbcParam = getJDBCParam();
        configService.save(jdbcParam);
        if (StringUtils.isBlank(configId.getText())) {
            Label label = new Label();
            label.setText(jdbcParam.getName());
            label.setId(jdbcParam.getId());
            mysqlListview.getItems().add(label);
        } else {
            Label label = mysqlListview.getSelectionModel().getSelectedItem();
            if (label != null) {
                label.setText(jdbcParam.getName());
            }
        }
        configId.setText(jdbcParam.getId());
        Iterator<Label> iterator = mysqlListview.getItems().iterator();
        while (iterator.hasNext()) {
            Label label = iterator.next();
            if (label.getId().equalsIgnoreCase(jdbcParam.getId())) {
                mysqlListview.getSelectionModel().select(label);
            }
        }


    }

//    private void persistenConfig(ConfigParam jdbcParam) {
//        String path = MyFileUtils.getUserDir();
//        File configFile = new File(path + File.separator + "config" + File.separator + "mysql" + File.separator + jdbcParam.getId() + ".xml");
//        try {
//            String jdbcConfigXml = XmlUtils.beanToXml(jdbcParam);
//            FileUtils.writeStringToFile(configFile, jdbcConfigXml, "utf-8", false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


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
            ConfigParam jdbcParam = ConnectionConfigs.configs.get(label.getId());
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
