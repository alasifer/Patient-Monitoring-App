package BaseSystem;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;



/**
 * the driver class that runs the application
 *
 * @authors: AHMED ABDULWAHID OMAR ALASAIFER & HOW CARL KIT
 * Monash University Malaysia
 * @last_edited: 14/May/2019
 *
 */
public class Main extends Application{



    public static void main(String[] args) {
        launch(args);

    }

    /**
     * this is an Overridden method that initialize the javafx GUI
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Pane newLoadPane = loader.load();
        ScrollPane root = new ScrollPane();
        root.setContent(newLoadPane);
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Assignment 2");
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

}



