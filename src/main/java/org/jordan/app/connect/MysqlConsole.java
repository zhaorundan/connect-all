package org.jordan.app.connect;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.jordan.app.connect.connector.ConnectionPool;

import java.util.List;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/28下午12:45
 */
public class MysqlConsole{

    public Tab addConsoleTab(String tabName,String tabId) {
        Tab tab = new Tab();
        tab.setText(tabName);
        tab.setClosable(true);
        tab.setId(tabId);

        SplitPane splitPane = new SplitPane();
        AnchorPane left = new AnchorPane();
        AnchorPane right = new AnchorPane();

        VBox vBox = new VBox();
        TextField textField = new TextField();
        MenuButton menuButton = new MenuButton();
        //展示所有数据库
        List<String> databases = ConnectionPool.getDatabases(tabId);
        ListView<Label> listView = new ListView<>();
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                listView.getItems().remove(0,listView.getItems().size());
                MenuItem item = (MenuItem) (event.getSource());
                List<String> tables = ConnectionPool.getTablesOfDatabase(item.getText(), tabId);
                for (String table : tables) {
                    listView.getItems().addAll(new Label(table));
                }
            }
        };
        for (String database : databases) {
            MenuItem item = new MenuItem(database);
            item.setOnAction(event);
            menuButton.getItems().add(item);
        }

        vBox.getChildren().addAll(textField,menuButton,listView);

        left.getChildren().add(vBox);
        splitPane.getItems().addAll(left,right);
        tab.setContent(splitPane);
        return tab;
    }

}
