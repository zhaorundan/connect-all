package org.jordan.app.connect.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.jordan.app.connect.view.MysqlTabpaneView;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/22下午5:44
 */
@FXMLController
public class MainController implements Initializable {
    @Resource
    private MysqlTabpaneView mysqlTabpaneView;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private MenuItem addMysql;
    @FXML
    private MenuItem addRedis;
    @FXML
    private MenuItem addMongo;
    @FXML
    private MenuItem addZookeeper;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addMysql.setOnAction((ActionEvent event)->{
            MenuItem menuItem = (MenuItem) event.getSource();
            addMysqlTab(event,"MySql");
        });

        addMongo.setOnAction((ActionEvent event)->{
//            Tab tab = new Tab();
//            tab.setText("MongoDB");
//            tabPane.getTabs().add(tab);
//            tab.setClosable(true);
//            tabPane.getSelectionModel().select(tab);
        });
    }


    public void addMysqlTab(ActionEvent event,String text) {
        mainBorderPane.setCenter(mysqlTabpaneView.getView());

    }

    @FXML
    public void menuEvent(ActionEvent event) {

    }

    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }
}
