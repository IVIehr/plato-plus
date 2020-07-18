package menu;
import TicTacToe.TicTacToe;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdbc.DeleteApp;
import jdbc.SelectApp;
import network.ClientMain;
import tool.Tools;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import static javafx.scene.paint.Color.*;

public class MenuPage extends Application {

    public ArrayList<String> contacts = new ArrayList<String>();
    SelectApp selectApp = new SelectApp();
    DeleteApp deleteApp = new DeleteApp();
    Tools tools = new Tools();
    ClientMain client;

    public MenuPage(ClientMain client){
        this.client = client;
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle(ClientMain.title);
        //set icon
        String pathIcon1 = path("female.png");
        Image femaleIcon = new Image(pathIcon1);
        String pathIcon2 = path("male.png");
        Image maleIcon = new Image(pathIcon2);
       if(client.getGender().equals("female"))
            primaryStage.getIcons().add(femaleIcon);
        else if(client.getGender().equals("male"))
            primaryStage.getIcons().add(maleIcon);
        //name pic
        String pathName = path("name.gif");
        Image name = new Image(pathName);
        ImageView nameImageView = new ImageView(name);
        //game name pic
        String pathGameName = path("game.gif");
        Image gameName = new Image(pathGameName);
        ImageView gameNameImageView = new ImageView(gameName);
        //chat name pic
        String pathPeople = path("people.gif");
        Image peoplePic = new Image(pathPeople);
        ImageView peopleImageView = new ImageView(peoplePic);
        // click sound
        String bip =System.getProperty("user.dir")+"\\src\\menu\\mouseclick.mp3";
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);

        //create root
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #ffffff;");
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);

        Line line1 = createLine();
        Line line3 = createLine();
        Line line4 = createLine();
        Line line6 = createLine();

        /** home page*/
        //create homeButton
        Button home = new Button();
        home.setStyle("-fx-background-color:#ffffff;"+"-fx-background-image: url('home.jpg');");
        home.setMinWidth(60);
        home.setMinHeight(60);
        home.setOnMouseEntered( e -> home.setStyle("-fx-background-color:#ffffff;"+
                "-fx-background-image: url('home2.jpg');"));
        home.setOnMouseExited( e -> home.setStyle("-fx-background-color:#ffffff;"+
                "-fx-background-image: url('home.jpg');"));
        Label label = new Label("online users\t\t\t\t\t\t\t\t");
        GridPane online = new GridPane();
        online.setHgap(5);

        //show online people
       for(int i=0;i<SignUp.clients.size();i++) {
            Text onlinePeople = new Text(SignUp.clients.get(i).getUsername()+" | ");
            onlinePeople.setFont(Font.font("Helvetica", FontWeight.BOLD,15));
            onlinePeople.setFill(TURQUOISE);
            online.add(onlinePeople,i,0);
       }
        Line line2 = createLine();
        online.setAlignment(Pos.CENTER);

        Button logout = new Button("logout");
        logout.setStyle(
                "-fx-text-fill: #53a5de;"
                        +"-fx-background-color:#ffffff;"
                        +"-fx-font-size: 15px;"
                        +"-fx-font-weight: bold;"
        );
        logout.setOnAction(event -> {
            primaryStage.close();
            SignUp.clients.remove(client);
            ClientMain.localList.remove(client.getUsername());
        });
        logout.setOnMouseEntered(event -> logout.setStyle(
                "-fx-text-fill: #2b6d5c;"
                        +"-fx-background-color:#ffffff;"
                        +"-fx-font-size: 15px;"
                        +"-fx-font-weight: bold;"
        ));
        logout.setOnMouseExited(event -> logout.setStyle(
                "-fx-text-fill: #53a5de;"
                        +"-fx-background-color:#ffffff;"
                        +"-fx-font-size: 15px;"
                        +"-fx-font-weight: bold;"
        ));

        Line line7 = createLine();
        //create delete account button
        Button deleteAcc = new Button("Delete Account!");
        deleteAcc.setStyle(
                "-fx-text-fill: #b40000;"
               +"-fx-background-color:#ffffff;"
               +"-fx-font-size: 15px;"
               +"-fx-font-weight: bold;"
        );
        deleteAcc.setOnMouseEntered(event -> deleteAcc.setStyle(
                "-fx-text-fill: #2d4b5c;"
                        +"-fx-background-color:#ffffff;"
                        +"-fx-font-size: 15px;"
                        +"-fx-font-weight: bold;"
        ));
        deleteAcc.setOnMouseExited(event -> deleteAcc.setStyle(
                "-fx-text-fill: #b40000;"
                        +"-fx-background-color:#ffffff;"
                        +"-fx-font-size: 15px;"
                        +"-fx-font-weight: bold;"
        ));
        deleteAcc.setOnAction(event -> {
            primaryStage.close();
            deleteApp.delete(client.getUsername());
            SignUp.clients.remove(client);
        });


        /** game Page*/
        //create gameButton
        Button games = new Button();
        games.setStyle("-fx-background-color:#ffffff;"+"-fx-background-image: url('game.jpg');");
        games.setMinWidth(60);
        games.setMinHeight(60);
        games.setOnMouseEntered( e -> games.setStyle("-fx-background-color:#ffffff;"+"-fx-background-image: url('game2.jpg');"));
        games.setOnMouseExited( e -> games.setStyle("-fx-background-color:#ffffff;"+"-fx-background-image: url('game.jpg');"));
        //root to choose game
        HBox chooseTicTac = new HBox();
        chooseTicTac.setAlignment(Pos.CENTER);
        chooseTicTac.setSpacing(20);
        chooseTicTac.setOnMouseEntered( e -> {mediaPlayer.play();
            chooseTicTac.setBlendMode(BlendMode.DIFFERENCE); });
        chooseTicTac.setOnMouseExited(e -> {mediaPlayer.stop();
            chooseTicTac.setBlendMode(BlendMode.MULTIPLY);
        });
        Button ticTacButton = new Button();
        ticTacButton.setStyle("-fx-background-image: url('TicTacToe.jpg');"
                + "-fx-background-position: 80%;");
        ticTacButton.setMinWidth(40);
        ticTacButton.setMinHeight(40);
        Text ticTacName = new Text("tic tac toe");
        ticTacName.setFont(Font.font("Helvetica", FontWeight.BOLD,17));
        ticTacName.setFill(GREY);
        chooseTicTac.getChildren().addAll(ticTacButton,ticTacName);
        GridPane info = new GridPane();
        info.setAlignment(Pos.CENTER);
        info.setVgap(5);


        /** people Page*/
        //create chatButton
        Button people = new Button();
        people.setStyle("-fx-background-color:#ffffff;"+
                "-fx-background-image: url('chat.jpg');");
        people.setMinWidth(60);
        people.setMinHeight(60);
        people.setOnMouseEntered( e -> people.setStyle("-fx-background-color:#ffffff;"+
                "-fx-background-image: url('chat2.jpg');"));
        people.setOnMouseExited( e -> people.setStyle("-fx-background-color:#ffffff;"+
                "-fx-background-image: url('chat.jpg');"));
        //set on client and open chat page
        Button click = new Button("start chat");
        click.setOnAction(event -> {
            try {
                client.chat(new Stage());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        click.setStyle(
                "-fx-text-fill: #ffffff;"
        +"-fx-font-size: 14px;"
        +"-fx-background-color: #44748d;");

        click.setOnMouseEntered(event -> click.setStyle("-fx-background-color:rgba(213,179,250,0.84);"));
        click.setOnMouseExited(event -> click.setStyle("-fx-background-color:#44748d;") );
        Line line5 = createLine();
        GridPane contactList = new GridPane();
        contactList.setAlignment(Pos.CENTER);
        contactList.setVgap(5);

        //add a new contact
        MenuBar addNewContact = new MenuBar() {{
            Menu add = new Menu("add a new contact(+)") {{
                selectApp.selectAll();
                selectApp.selectGender();
                //list of all people that signed up in system so far
                for(int i=0;i<selectApp.userNames.size();i++) {
                    if (!selectApp.userNames.get(i).equals(client.getUsername())) {
                        MenuItem person = new MenuItem(selectApp.userNames.get(i));
                        int index = i;
                        person.setOnAction(e -> {
                            Text contact = new Text(selectApp.userNames.get(index));
                            contactList.add(contact, 0, index + 1);
                            contacts.add(contact.getText());

                            if (selectApp.genders.get(index).equals("female")) {
                                ImageView femalePic = new ImageView(femaleIcon);
                                femalePic.setFitWidth(35);
                                femalePic.setFitHeight(35);
                                contactList.add(femalePic, 1, index + 1);
                            } else if (selectApp.genders.get(index).equals("male")) {
                                ImageView malePic = new ImageView(maleIcon);
                                malePic.setFitWidth(35);
                                malePic.setFitHeight(35);
                                contactList.add(malePic, 1, index + 1);
                            }

                        });
                        getItems().add(person);
                    }
                }
            }};
            getMenus().add(add);
        }};
        contactList.add(addNewContact,0,0);
         addNewContact.setStyle(
                "-fx-text-fill: #ffffff;"
                        +"-fx-background-color: rgba(113,93,130,0.31);");

        //create HBox for menuItems
        HBox menuBar = new HBox();
        menuBar.setAlignment(Pos.BOTTOM_CENTER);
        menuBar.setPadding(new Insets(190,0,37,0));
        menuBar.setSpacing(30);


        /** button actions*/

        home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                root.getChildren().clear();
                menuBar.setPadding(new Insets(190,0,37,0));
                root.getChildren().addAll(nameImageView,line1,label,online,line2,logout,line7,deleteAcc,menuBar);

                //update online people
                online.getChildren().clear();
                for(int i=0;i<SignUp.clients.size();i++) {
                    Text onlinePeople = new Text(SignUp.clients.get(i).getUsername()+" | ");
                    onlinePeople.setFont(Font.font("Helvetica", FontWeight.BOLD,15));
                    onlinePeople.setFill(TURQUOISE);
                    online.add(onlinePeople,i,0);

                }
            }
        });

        games.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                root.getChildren().clear();
                menuBar.setPadding(new Insets(220,0,38,0));
                root.getChildren().addAll(gameNameImageView,line3,chooseTicTac,line4,info,menuBar);

                selectApp.selectAll();
                int index = tools.findIndex(selectApp.userNames,client.getUsername());
                //start game
                chooseTicTac.setOnMouseClicked(event1 -> {
                    TicTacToe tic = new TicTacToe(primaryStage,client.getUsername(),index);
                    primaryStage.hide();
                    tic.start(new Stage());
                });

                //display the information of game such as the number times that user win ,tie or lose
                selectApp.selectGameInfo();
                info.getChildren().clear();
                Text win = new Text("You win \""+selectApp.wins.get(index)+"\" times in playing tic tac toe with computer");
                Text ties = new Text("You tie \""+selectApp.ties.get(index)+"\" times in playing tic tac toe with computer");
                Text loses = new Text("You lose \""+selectApp.loses.get(index)+"\" times in playing tic tac toe with computer");
                win.setFont(Font.font("cursive", FontWeight.BOLD,15));
                win.setFill(LIGHTSTEELBLUE);
                ties.setFont(Font.font("cursive", FontWeight.BOLD,15));
                ties.setFill(DARKBLUE);
                loses.setFont(Font.font("cursive", FontWeight.BOLD,15));
                loses.setFill(DARKRED);
                info.add(win,0,2);
                info.add(ties,0,4);
                info.add(loses,0,6);
            }
        });

        people.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                root.getChildren().clear();
                menuBar.setPadding(new Insets(221,0,70,0));
                root.getChildren().addAll(peopleImageView,line6,contactList,line5,click,menuBar);
            }
        });

        menuBar.getChildren().addAll(home,people,games);
        root.getChildren().addAll(nameImageView,line1,label,online,line2,logout,line7,deleteAcc,menuBar);
        Scene scene = new Scene(root,950,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            SignUp.clients.remove(client);
            ClientMain.localList.remove(client.getUsername());
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
