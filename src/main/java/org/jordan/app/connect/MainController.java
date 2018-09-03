package org.jordan.app.connect;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/22下午5:44
 */
public class MainController implements Initializable {
    private BorderPane rootLayout;
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
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/org/jordan/app/connect/MysqlTabpane.fxml"));
            TabPane tabPane =   loader.load();
            tabPane.getTabs().get(0).setText(text);
            rootLayout.setCenter(tabPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void menuEvent(ActionEvent event) {

    }

    public void setRootLayout(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
    }
}
