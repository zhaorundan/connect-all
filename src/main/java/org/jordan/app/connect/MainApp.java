package org.jordan.app.connect;

import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jordan.app.connect.utils.JavaFxViewUtil;
import org.jordan.app.connect.utils.ThreadPoolUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/22下午3:44
 */
@SpringBootApplication
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Connect All");
        initRootLayout();
        Thread.currentThread().setUncaughtExceptionHandler((thread,throwable)->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning ");
            alert.setHeaderText(null);
            alert.setContentText("连接失败!\n"+throwable.getMessage());
            alert.showAndWait();
        });
    }

    public void initRootLayout() throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
        JFXDecorator decorator = JavaFxViewUtil.getJFXDecorator(primaryStage,"Connect All" , "",parent);
        decorator.setOnCloseButtonAction(()->{System.exit(0);});
        Scene scene = JavaFxViewUtil.getJFXDecoratorScene(decorator);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Connect All");
//        primaryStage.getIcons().add(new Image("/images/icon.jpg"));//图标
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch( args);
    }

}
