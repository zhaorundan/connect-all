package org.jordan.app.connect.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/28下午12:45
 */
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("aaa");
    }




}
