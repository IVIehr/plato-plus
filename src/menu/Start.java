package menu;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import static javafx.scene.paint.Color.*;

public class Start extends Application {

    SignUp signUpProcess = new SignUp();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PlatoPlus");
        //set icon
        String pathIcon = path("icon.png");
        Image icon = new Image(pathIcon);
        primaryStage.getIcons().add(icon);
        //name pic
        String pathName = path("name2.gif");
        Image name = new Image(pathName);
        ImageView nameImageView = new ImageView(name);
        //user name pic
        String pathUserName = path("account2.gif");
        Image userName = new Image(pathUserName);
        ImageView userNameImageView = new ImageView(userName);

        //create basis root
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #335467;");
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);

        Line line1 = createLine();
        Line line5 = createLine();

        /** home page*/
        Text states = new Text();
        states.setText("\n\nWelcome to PlatoPlus!\n"
        +"First, create an account to use the application.\n"
        +"If you had an account, login at \"Account\" page.\n"
        +"While you logging in, an application will opens for you that you can\n"
        +"Play the games in its \"Games\" page and communicate with\n"
        +"Other people in its \"Chats\" page.");
        states.setTextAlignment(TextAlignment.CENTER);
        states.setFont(Font.font("Helvetica", FontWeight.BOLD,17));
        states.setFill(LIGHTSTEELBLUE);

        //create menubar buttons
        Button home = new Button();
        home.setStyle("-fx-background-color:#335467;"+"-fx-background-image: url('home3.jpg');");
        home.setMinWidth(60);
        home.setMinHeight(60);
        home.setOnMouseEntered( e -> home.setStyle("-fx-background-color:#335467;"+
                "-fx-background-image: url('home4.jpg');"));
        home.setOnMouseExited( e -> home.setStyle("-fx-background-color:#335467;"+
                "-fx-background-image: url('home3.jpg');"));

        /** user Page*/
        //create userButton
        Button user = new Button();
        user.setStyle("-fx-background-color:#335467;"+"-fx-background-image: url('user3.jpg');");
        user.setMinWidth(60);
        user.setMinHeight(60);
        user.setOnMouseEntered( e -> user.setStyle("-fx-background-color:#335467;"+
                "-fx-background-image: url('user4.jpg');"));
        user.setOnMouseExited( e -> user.setStyle("-fx-background-color:#335467;"+
                "-fx-background-image: url('user3.jpg');"));
        //root of sign up
        GridPane signUpPane = signUpProcess.signUp();

        //create HBox for menuItems
        HBox menuBar = new HBox();
        menuBar.setAlignment(Pos.BOTTOM_CENTER);
        menuBar.setPadding(new Insets(150,0,12,0));
        menuBar.setSpacing(30);

        /** button actions*/
        home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.getChildren().clear();
                menuBar.setPadding(new Insets(150,0,12,0));
                root.getChildren().addAll(nameImageView,line1,states,menuBar);
            }
        });

        user.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.getChildren().clear();
                menuBar.setPadding(new Insets(12,0,36,0));
                root.getChildren().addAll(userNameImageView,line5,signUpPane,menuBar);
            }
        });

        menuBar.getChildren().addAll(home,user);
        root.getChildren().addAll(nameImageView,line1,states,menuBar);
        Scene scene = new Scene(root,960,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }
    public Line createLine(){
        Line line = new Line();
        line.setStroke(GAINSBORO);
        line.setStartX(-166);
        line.setEndX(166);
        return line;
    }
    public String path(String path){
        return "file:"+System.getProperty("user.dir").replaceAll("\\\\","/")+"\\src\\pics\\"+path;
    }

    public static void main(String args[])
    {// launch the application
        launch(args);
    }
}

