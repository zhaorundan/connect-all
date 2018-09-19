package org.jordan.app.connect.controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jordan.app.connect.model.Pager;
import org.jordan.app.connect.model.RedisData;
import org.jordan.app.connect.service.MysqlServiceImpl;
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
    private int dbIndex = 0;
    private String keytype;
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
            dbIndex = Integer.valueOf(newValue.replace("DB", ""));
        });
        initKeyTableColumn();
        initKeyTableData(dbIndex);
        redisKeyView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RedisData redisData = (RedisData) newValue;
                Pager<RedisData> values = RedisServiceImpl.getInstance().listValueOfKey(redisData.getKeytype(), redisData.getKey(), configId, dbIndex);
                keytype = redisData.getKeytype();
                keyTab.setText("DB"+dbIndex+":"+redisData.getKey());
                keyName.setText(redisData.getKey());
                keyTTL.setText(RedisServiceImpl.getInstance().getKeyTTL(redisData.getKeytype(), redisData.getKey(), configId, dbIndex).toString());
                setValueTableData(values);
                redisKeyType.setText(redisData.getKeytype());
                keyTTL.setText(values.getTtl().toString());
            } else {

            }
        });
    }
    private final ObservableList<RedisData> data = FXCollections.observableArrayList();
    private final ObservableList<RedisData> stringData = FXCollections.observableArrayList();
    private final ObservableList<RedisData> setData = FXCollections.observableArrayList();
    private final ObservableList<RedisData> hashData = FXCollections.observableArrayList();
    private final ObservableList<RedisData> sortsetData = FXCollections.observableArrayList();
    /**
     * 初始化左侧面板的数据
     * 1 key的数量
     * 2 tableview
     * 3 分页组件
     */
    private void initKeyTableColumn() {
        Long dbSize = RedisServiceImpl.getInstance().getDbSize(configId,dbIndex);
        keysCount.setText(dbSize.toString());

        ObservableList<TableColumn> observableList = redisKeyView.getColumns();
        observableList.add(new TableColumn("keytype"));
        observableList.add(new TableColumn("key"));
        observableList.get(0).setCellValueFactory(new PropertyValueFactory("keytype"));
        observableList.get(1).setCellValueFactory(new PropertyValueFactory("key"));
        redisKeyView.setItems(data);
    }

    private void initKeyTableData(int dbIndex) {
        if (!data.isEmpty()) {
            data.clear();
        }
        Pager<RedisData> pageData = RedisServiceImpl.getInstance().listDataWithPager(configId,dbIndex);
        List<RedisData> list = pageData.getResult();
        data.addAll(list);
    }

    private void setValueTableData(Pager<RedisData> values) {
        ObservableList<TableColumn> observableList = redisValueView.getColumns();
        if (!observableList.isEmpty()) {
            observableList.clear();
        }

        if (CollectionUtils.isNotEmpty(values.getResult())) {
            redisValueView.getColumns().removeAll();
            switch (keytype.toLowerCase()) {
                case "string":
                    buildStringData(observableList, values);
                    break;
                case "list":
                    buildStringData(observableList, values);
                    break;
                case "set":
                    buildStringData(observableList, values);
                    break;
                case "hash":
                    if (CollectionUtils.isNotEmpty(hashData)) {
                        hashData.clear();
                    }
                    TableColumn fieldColumn = new TableColumn("field");
                    fieldColumn.setPrefWidth(100);
                    TableColumn valueColumn = new TableColumn("value");
                    valueColumn.setPrefWidth(300);
                    observableList.addAll(fieldColumn,valueColumn);
                    observableList.get(0).setCellValueFactory(new PropertyValueFactory("field"));
                    observableList.get(1).setCellValueFactory(new PropertyValueFactory("value"));
                    hashData.addAll(values.getResult());
                    redisValueView.setItems(hashData);
                    break;
                case "zset":
                    if (CollectionUtils.isNotEmpty(sortsetData)) {
                        sortsetData.clear();
                    }
                    TableColumn value = new TableColumn("value");
                    value.setPrefWidth(100);
                    TableColumn score = new TableColumn("score");
                    score.setPrefWidth(300);
                    observableList.addAll(value,score);
                    observableList.get(0).setCellValueFactory(new PropertyValueFactory("value"));
                    observableList.get(1).setCellValueFactory(new PropertyValueFactory("score"));
                    sortsetData.addAll(values.getResult());
                    redisValueView.setItems(sortsetData);
                    break;

            }
        }

    }

    private void buildStringData(ObservableList<TableColumn> observableList, Pager<RedisData> values) {

        if (CollectionUtils.isNotEmpty(stringData)) {
            stringData.clear();
        }
        TableColumn tableColumn = new TableColumn("value");
        tableColumn.setPrefWidth(300);
        tableColumn.setMaxWidth(Double.MAX_VALUE);
        observableList.add(tableColumn);
        observableList.get(0).setCellValueFactory(new PropertyValueFactory("value"));
        stringData.addAll(values.getResult());
        redisValueView.setItems(stringData);
    }

    public void initData() {

    }

    public void searchForKey() {
        String key = searchKey.getText();
        Pager<RedisData> pager ;
        if (StringUtils.isBlank(key)) {
            boolean isFuzzy = searchHit.isSelected();
            pager = RedisServiceImpl.getInstance().listDataWithPager(configId, dbIndex, key, isFuzzy);
        } else {
            pager = RedisServiceImpl.getInstance().listDataWithPager(configId, dbIndex);
        }
        if (!data.isEmpty()) {
            data.clear();
        }
        List<RedisData> list = pager.getResult();
        data.addAll(list);
    }

    public void createKey() {
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddKeyDialog.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            Scene scene = new Scene(anchorPane, 300, 200);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            fxmlLoader.getController();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteKey() {

    }

    public void updateKeyTTL() {

    }



}
