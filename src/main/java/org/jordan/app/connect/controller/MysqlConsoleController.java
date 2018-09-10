package org.jordan.app.connect.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jordan.app.connect.connector.Connections;
import org.jordan.app.connect.service.MysqlServiceImpl;
import org.jordan.app.connect.view.MysqlConsoleView;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/28下午12:45
 */
@Slf4j
public class MysqlConsoleController extends MysqlConsoleView {
    private MysqlServiceImpl mysqlService = new MysqlServiceImpl();

    @Getter
    @Setter
    private String jdbcId;

    private ObservableList<ObservableList> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize");

    }

    public void searchTableEvent(ActionEvent event) {
        searchTable.getText();
    }

    public void initView() throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MysqlQuery.fxml"));
        AnchorPane vBox = fxmlLoader.load();

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
        MysqlQueryController controller = fxmlLoader.getController();
        controller.setMysqlConsoleController(this);

    }

    private ObservableList<Label> tableData = FXCollections.observableArrayList();

    public void initData() {
        //展示所有数据库
        List<String> databases = mysqlService.listDatabases(jdbcId);

        for (String database : databases) {
            databaseList.getItems().add(database);
        }
        initTableListEvent();
        initTableViewEvent();
    }

    private void initTableListEvent() {
        ContextMenu menu = new ContextMenu();
        MenuItem copy = new MenuItem("Copy");

        menu.getItems().addAll(copy);
        tableList.setContextMenu(menu);

        copy.setOnAction(  event ->{
            Label label = tableList.getSelectionModel().getSelectedItem();
            final ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(label.getText());
            // set clipboard content
            Clipboard.getSystemClipboard().setContent(clipboardContent);
        });
    }

    private void initTableViewEvent() {
        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Menu 1");
        cm.getItems().add(mi1);
        MenuItem mi2 = new MenuItem("Menu 2");
        cm.getItems().add(mi2);


    }

    public void buildDataBaseList(ActionEvent event) {
        try {
            tableList.getItems().clear();
            String database = getDatabase();
            List<String> tables = mysqlService.listTablesOfDatabase(database, jdbcId);
            tableData.clear();
            for (String table : tables) {
                Label label = new Label();
                label.setText(table);
                tableData.add(label);
            }
            tableList.getItems().addAll(tableData);
            tableList.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void buildTableView(MouseEvent event) {
        try {
            tableView.getColumns().clear();
            tableView.getItems().clear();
            data.clear();
            setTableview();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getTableName() {
        return tableList.getSelectionModel().getSelectedItem().getText();
    }

    public String getDatabase() {
        return databaseList.getSelectionModel().getSelectedItem();
    }

    private void setTableview() throws Exception{
        String database = getDatabase();
        String tableName = getTableName();
        String sql = "select * from `" + database + "`." + tableName + " limit 30";
        Connection connection = Connections.getConnection(jdbcId);

        ResultSet rs = connection.createStatement().executeQuery(sql);

        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            //We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));

            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            col.setCellFactory(TextFieldTableCell.forTableColumn());


            tableView.getColumns().add(col);
            if (log.isDebugEnabled()) {
                log.debug("Column [{}] ", i);
            }
        }
        while (rs.next()) {
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                //Iterate Column
                row.add(rs.getString(i)==null?"":rs.getString(i));

            }
            if (log.isDebugEnabled()) {
                log.debug("Row [1] added {} ", row);
            }
            data.add(row);

        }
        tableView.setItems(data);
    }


}
