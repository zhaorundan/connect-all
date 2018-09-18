package org.jordan.app.connect.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jordan.app.connect.model.Pager;
import org.jordan.app.connect.model.RedisData;
import org.jordan.app.connect.service.RedisServiceImpl;
import org.jordan.app.connect.view.RedisConsoleView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
@Slf4j
public class RedisConsoleController extends RedisConsoleView {
    @Getter
    @Setter
    private String configId;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void initView() throws Exception {
        int databases = RedisServiceImpl.getInstance().listDatabases(configId);
        ObservableList<String> data = FXCollections.observableArrayList();
        for (int i = 0; i < databases; i++) {
            data.add("DB" + i);
        }
        redisDatabases.setItems(data);
        redisDatabases.getSelectionModel().select(0);
        redisDatabases.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)->{
            int dbIndex = Integer.valueOf(newValue.replace("DB",""));

        });
        initLeftData(0);
    }
    private final ObservableList<RedisData> data = FXCollections.observableArrayList();

    /**
     * 初始化左侧面板的数据
     * 1 key的数量
     * 2 tableview
     * 3 分页组件
     * @param dbIndex
     */
    private void initLeftData(int dbIndex) {
        Long dbSize = RedisServiceImpl.getInstance().getDbSize(configId,0);
        keysCount.setText(dbSize.toString());

        Pager<RedisData> pageData = RedisServiceImpl.getInstance().listDataWithPager(configId,dbIndex);
        List<RedisData> list = pageData.getResult();
        data.addAll(list);

        ObservableList<TableColumn> observableList = redisKeyView.getColumns();
        observableList.add(new TableColumn("keytype"));
        observableList.add(new TableColumn("key"));
        observableList.get(0).setCellValueFactory(new PropertyValueFactory("keytype"));
        observableList.get(1).setCellValueFactory(new PropertyValueFactory("key"));
        redisKeyView.setItems(data);
    }

    private ObservableList<Label> tableData = FXCollections.observableArrayList();

    public void initData() {

    }

    public void searchForKey() {
        String key = searchKey.getText();

    }

    public void createKey() {

    }

    public void deleteKey() {

    }
}
