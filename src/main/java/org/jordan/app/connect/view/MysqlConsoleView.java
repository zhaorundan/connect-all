package org.jordan.app.connect.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author zhaord
 * @Description:
 * @date 2018/9/3下午6:35
 */
public abstract class MysqlConsoleView implements Initializable {
    @FXML
    protected ComboBox<String> databaseList;
    @FXML
    protected TextField searchTable;
    @FXML
    protected ListView<Label> tableList;
    @FXML
    protected TabPane mysqlTabpane;
    @FXML
    protected TableView  tableView;
    @FXML
    protected Button contentButton;
    @FXML
    protected Button queryButton;


    @FXML
    protected Pane mainPane;
}
