package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Tab tabs = new Tab();
        tabs.setContent(new Controller());
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(tabs);
//        tabPane.getTabs().addAll(roaster, stats);

        primaryStage.setTitle("Student Roaster");
        primaryStage.setOpacity(0.85);

        Scene scene = new Scene(tabPane, 850, 800);
        scene.getStylesheets().add("/resource/style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
