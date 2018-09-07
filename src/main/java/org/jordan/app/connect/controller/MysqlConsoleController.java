package org.jordan.app.connect.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jordan.app.connect.service.MysqlServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/28下午12:45
 */
@Scope("prototype")
@Slf4j
@FXMLController
public class MysqlConsoleController implements Initializable {
    @FXML
    private ComboBox<String> databaseList;
    @FXML
    private TextField searchTable;
    @FXML
    private ListView<Label> tableLIst;
    @FXML
    private TabPane mysqlTabpane;
    @Setter
    @Getter
    private String configId;

    @Resource
    private MysqlServiceImpl mysqlService;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
    }

    private void initData() {
//        展示所有数据库
        List<String> databases = mysqlService.getDatabases(getConfigId());
        databaseList.setOnAction(event -> {
            tableLIst.getItems().remove(0, tableLIst.getItems().size());
            String tableName = databaseList.getSelectionModel().getSelectedItem();
            List<String> tables = mysqlService.getTablesOfDatabase(tableName, getConfigId());
            for (String table : tables) {
                tableLIst.getItems().addAll(new Label(table));
            }
        });
        tableLIst.setOnMouseClicked(event -> {
            Label label = tableLIst.getSelectionModel().getSelectedItem();

        });
        for (String database : databases) {
            databaseList.getItems().add(database);
        }
    }




}
