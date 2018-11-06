package org.jordan.app.connect.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.jordan.app.connect.view.MainView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/22下午5:44
 */
public class MainController extends MainView {

    private TabPane mainTabpane = new TabPane();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainTabpane.setId("mysqlTabpane");
        mainTabpane.setSide(Side.BOTTOM);

        addMysql.setOnAction((ActionEvent event)->{
            try {
                addMysqlTab(event,"MySql");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        addMongo.setOnAction((ActionEvent event)->{

        });
        addRedis.setOnAction(event -> {
            try {
                addRedisTab(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void addRedisTab(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/RedisTabpane.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        RedisTabpaneController redisTabpaneController = fxmlLoader.getController();
        Tab redisTab = new Tab();
        redisTab.setId("redis");
        redisTab.setText("Redis");
        redisTab.setClosable(true);
        redisTab.setContent(anchorPane);
        mainTabpane.getTabs().add(redisTab);
        if (mainBorderPane.getCenter() == null) {
            mainBorderPane.setCenter(mainTabpane);
        }
        redisTab.setOnClosed(event1 -> {

        });
        mainTabpane.getSelectionModel().select(redisTab);
    }


    public void addMysqlTab(ActionEvent event,String text) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MysqlTabpane.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        MysqlTabpaneController mysqlTabpaneController = fxmlLoader.getController();
        Tab mysqlTab = new Tab();
        mysqlTab.setId("mysql");
        mysqlTab.setText("mysql");
        mysqlTab.setClosable(true);
        mysqlTab.setContent(anchorPane);
        mainTabpane.getTabs().add(mysqlTab);
        if (mainBorderPane.getCenter() == null) {
            mainBorderPane.setCenter(mainTabpane);
        }
        mysqlTab.setOnClosed(event1 -> {

        });
        mainTabpane.getSelectionModel().select(mysqlTab);
    }

    @FXML
    public void menuEvent(ActionEvent event) {

    }

}
