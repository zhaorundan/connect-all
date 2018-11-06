package org.jordan.app.connect.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

/**
 * @author zhaord
 * @Description:
 * @date 2018/9/3下午6:15
 */
public abstract class MainView implements Initializable {
    @FXML
    protected BorderPane mainBorderPane;
    @FXML
    protected MenuItem addMysql;
    @FXML
    protected MenuItem addRedis;
    @FXML
    protected MenuItem addMongo;
    @FXML
    protected MenuItem addZookeeper;
}
