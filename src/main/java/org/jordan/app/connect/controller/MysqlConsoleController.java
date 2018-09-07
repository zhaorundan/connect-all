package org.jordan.app.connect.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;
import org.jordan.app.connect.service.MysqlServiceImpl;

import javax.annotation.Resource;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/28下午12:45
 */
//@Scope("prototype")
@Slf4j
@FXMLController
public class MysqlConsoleController implements Initializable {
    @FXML
    private ComboBox<String> databaseList;
    @FXML
    private TextField searchTable;
    @FXML
    private ListView<Label> tableList;
    @FXML
    private TabPane mysqlTabpane;

    @Resource
    private MysqlServiceImpl mysqlService;

    @FXML
    private TableView<ObservableList<String>> tableView;
    @FXML
    private Label jdbcId;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();

    }

    private void initData() {
//        展示所有数据库
        List<String> databases = mysqlService.listDatabases(jdbcId.getText());
        databaseList.setOnAction(event -> {
            tableList.getItems().remove(0, tableList.getItems().size());
            String tableName = databaseList.getSelectionModel().getSelectedItem();
            List<String> tables = mysqlService.listTablesOfDatabase(tableName, jdbcId.getText());
            for (String table : tables) {
                Label label = new Label();
                label.setText(table);
                tableList.getItems().addAll(label);
            }
        });
        tableList.setOnMouseClicked(event -> {
            Label label = tableList.getSelectionModel().getSelectedItem();
            String tableName = label.getText();
            writeTableView(tableName);
        });
        for (String database : databases) {
            databaseList.getItems().add(database);
        }

    }

    private void writeTableView(String tableName) {
        String database = databaseList.getSelectionModel().getSelectedItem();
        // add columns
        List<String> columnNames = mysqlService.listColumnOfTable(jdbcId.getText(), database, tableName);
        for (int i = 0; i < columnNames.size(); i++) {
            final int finalIdx = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    columnNames.get(i)
            );
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
            );
            tableView.getColumns().add(column);
        }

//        // add data
//        for (int i = 0; i < N_ROWS; i++) {
//            tableView.getItems().add(
//                    FXCollections.observableArrayList(
//                            dataGenerator.getNext(N_COLS)
//                    )
//            );
//        }

    }




}
