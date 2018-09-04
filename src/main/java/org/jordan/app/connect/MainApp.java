package org.jordan.app.connect;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jordan.app.connect.controller.MainController;
import org.jordan.app.connect.view.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/22下午3:44
 */
@SpringBootApplication
public class MainApp extends AbstractJavaFxApplicationSupport {

    private Stage primaryStage;
    private BorderPane rootLayout;
    @Override
    public void start(Stage primaryStage) throws Exception {
        super.start(primaryStage);
//        this.primaryStage = primaryStage;
//        this.primaryStage.setTitle("AddressApp");

//        initRootLayout();
//        initRootLayoutForController();
        Thread.currentThread().setUncaughtExceptionHandler((thread,throwable)->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning ");
            alert.setHeaderText(null);
            alert.setContentText("连接失败!\n"+throwable.getMessage());
            alert.showAndWait();
        });
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/org/jordan/app/connect/fxml/Main.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            MainController mainController = loader.getController();
            mainController.setMainBorderPane(rootLayout);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRootLayoutForController() {

    }

    public static void main(String[] args) {
//        launch(args);
        launch(MainApp.class, MainView.class, args);
    }

}
