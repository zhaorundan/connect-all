package org.jordan.app.connect.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jordan.app.connect.connector.Connections;
import org.jordan.app.connect.service.MysqlServiceImpl;
import org.jordan.app.connect.view.MysqlQueryView;

import javax.annotation.Resource;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private TableView  tableView;

    @Getter
    @Setter
    private String jdbcId;

    @Resource
    private Connections connections;
    @FXML
    private Button contentButton;
    @FXML
    private Button queryButton;


    @Resource
    private MysqlQueryController mysqlQueryController;
    @Resource
    private MysqlQueryView mysqlQueryView;
    @FXML
    private Pane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize");


    }

    public void initView() {
        VBox vBox = (VBox) mysqlQueryView.getView();
        mainPane.getChildren().add(vBox);
        vBox.setVisible(false);

        contentButton.setOnMouseClicked(event -> {
            if (!tableView.isVisible()) {
                tableView.setVisible(true);
                vBox.setVisible(false);
            }
        });
        queryButton.setOnMouseClicked(event -> {
            if (tableView.isVisible()) {
                tableView.setVisible(false);
                vBox.setVisible(true);
            }
        });
    }

    public void initData() {
        //展示所有数据库
        List<String> databases = mysqlService.listDatabases(jdbcId);
        databaseList.setOnAction(event -> {
            tableList.getItems().remove(0, tableList.getItems().size());
            String tableName = databaseList.getSelectionModel().getSelectedItem();
            List<String> tables = mysqlService.listTablesOfDatabase(tableName, jdbcId);
            for (String table : tables) {
                Label label = new Label();
                label.setText(table);
                tableList.getItems().addAll(label);
            }
        });
        tableList.setOnMouseClicked(event -> {
            tableView.getColumns().remove(0,tableView.getColumns().size());
            String tableName = getTableName();
            buildTableview(tableName);
        });

        for (String database : databases) {
            databaseList.getItems().add(database);
        }


    }

    public String getTableName() {
        return tableList.getSelectionModel().getSelectedItem().getText();
    }
    public String getDatabase() {
        return databaseList.getSelectionModel().getSelectedItem();
    }
    private void buildTableview(String tableName) {
        String database = getDatabase();
        String sql = "select * from "+database+"."+tableName+" limit 100";
        Connection connection = connections.getPool().get(jdbcId);
        try {
            ResultSet rs = connection.createStatement().executeQuery(sql);
            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));

                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
                log.info("Column [{}] ",i);
            }
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }
            tableView.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // add columns
//        List<String> columnNames = mysqlService.listColumnOfTable(jdbcId, database, tableName);
//        for (int i = 0; i < columnNames.size(); i++) {
//            final int finalIdx = i;
//            TableColumn<ObservableList<String>, String> column = new TableColumn<>(
//                    columnNames.get(i)
//            );
//            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
//            );
//            tableView.getColumns().add(column);
//        }
//        StringBuilder sb = new StringBuilder();
//        for (String columnName : columnNames) {
//
//        }

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
