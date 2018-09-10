package org.jordan.app.connect.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jordan.app.connect.connector.Connections;
import org.jordan.app.connect.view.MysqlQueryView;

import javax.annotation.Resource;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author zhaord
 * @Description:
 * @date 2018/9/8上午10:33
 */
@Slf4j
public class MysqlQueryController extends MysqlQueryView {
    @Getter
    @FXML
    private VBox queryArea;
    @FXML
    private TextArea queryText;
    @FXML
    private Button runQuery;
    @FXML
    private TableView queryResult;

    @Setter
    private MysqlConsoleController mysqlConsoleController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runQuery.setOnMouseClicked(event -> {
            queryResult.getColumns().removeAll();
            queryResult.getItems().removeAll();
            queryResult.refresh();
            executeQuery();
        });
    }

    private void executeQuery() {
        String sql = queryText.getText();
        String jdbcId = mysqlConsoleController.getJdbcId();
        String database = mysqlConsoleController.getDatabase();
        Connection connection = Connections.getPool().get(jdbcId);
        try {
            connection.createStatement().executeQuery("use " + database);
            ResultSet rs = connection.createStatement().executeQuery(sql);
            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));

                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                        ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                queryResult.getColumns().addAll(col);
                if (log.isDebugEnabled()) {
                    log.debug("Column [{}] ", i);
                }
            }
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                if (log.isDebugEnabled()) {
                    log.debug("Row [1] added {}", row);
                }
                data.add(row);

            }
            queryResult.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
