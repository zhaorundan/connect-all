package org.jordan.app.connect.controller;

import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
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

    private TabPane mainTabpaen = new TabPane();

    @Resource
    private MysqlTabpaneController mysqlTabpaneController;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainTabpaen.setId("mysqlTabpane");
        mainTabpaen.setSide(Side.BOTTOM);

        addMysql.setOnAction((ActionEvent event)->{
            addMysqlTab(event,"MySql");
        });

        addMongo.setOnAction((ActionEvent event)->{

        });
        addRedis.setOnAction(event -> {
            addRedisTab(event);
        });

    }

    private void addRedisTab(ActionEvent event) {
        Tab redisTab = new Tab();
        redisTab.setId("redis");
        redisTab.setText("redis");
        redisTab.setClosable(true);
        mainTabpaen.getTabs().add(redisTab);
        redisTab.setOnClosed(event1 -> {
            mainTabpaen.getTabs().clear();
        });
        if (mainBorderPane.getCenter() == null) {
            mainBorderPane.setCenter(mainTabpaen);
        }

    }


    public void addMysqlTab(ActionEvent event,String text) {
        Tab mysqlTab = new Tab();
        mysqlTab.setId("mysql");
        mysqlTab.setText("mysql");
        mysqlTab.setClosable(true);
        mysqlTabpaneController.setMysqlTab(mysqlTab);
//        mysqlTabpaneController.setMysqlTabpane(mysqlTabpane);
        AnchorPane anchorPane = (AnchorPane) mysqlTabpaneView.getView();
        mysqlTab.setContent(anchorPane);
        mainTabpaen.getTabs().add(mysqlTab);
        if (mainBorderPane.getCenter() == null) {
            mainBorderPane.setCenter(mainTabpaen);
        }
        mysqlTab.setOnClosed(event1 -> {
            mainTabpaen.getTabs().clear();
        });
    }

    @FXML
    public void menuEvent(ActionEvent event) {

    }

    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }
}
