package network;

import file.DeleteFile;
import file.ReadChat;
import file.SaveChat;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import jdbc.SelectApp;
import menu.MenuPage;
import menu.SignUp;
import tool.Tools;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static javafx.scene.paint.Color.*;

public class ClientMain extends Application {

    private ClientObserver writer;
    private BufferedReader reader;
    private AnchorPane anchorPane;
    private TextField input;
    private TextArea chat;
    private TextArea privateChat;
    private String username;
    private Text privateT;
    private MenuBar menu;
    private boolean accept;
    private boolean isPrivate;
    private static boolean isPublic;
    private Alert alert;
    private Alert req;
    private String answ;
    private String gender;
    private String intendedName;
    static ArrayList<SaveChat> savePrivate = new ArrayList<>();
    public static ArrayList<String> localList;
    public static String title;

    ReadChat readChat = new ReadChat();
    DeleteFile deleteFile;
    SelectApp select = new SelectApp();
    MenuPage menuPage = new MenuPage(this);

    public ClientMain(){
        SignUp.clients.add(this);
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        localList = new ArrayList<String>();
        setUpNetworking();
        accept = false;
        while(accept == false){
            usernameInput();
        }
        title = "Log in as: " + username;
        if(accept)
        menuPage.start(new Stage());
    }

    public void chat(Stage primaryStage) throws FileNotFoundException {
        String pathIcon1 = "file:"+System.getProperty("user.dir").replaceAll("\\\\","/")+"\\src\\pics\\female.png";
        Image femaleIcon = new Image(pathIcon1);
        String pathIcon2 = "file:"+System.getProperty("user.dir").replaceAll("\\\\","/")+"\\src\\pics\\male.png";
        Image maleIcon = new Image(pathIcon2);
        if(gender.equals("female"))
            primaryStage.getIcons().add(femaleIcon);
        else if(gender.equals("male"))
            primaryStage.getIcons().add(maleIcon);

        initView();
        Scene scene = new Scene(anchorPane, 770, 401);
        scene.getStylesheets().add("Blue.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Log in as: " + username);
        primaryStage.show();
    }

     private void usernameInput() {
        int index = 0;

        String pathBack = "file:" + System.getProperty("user.dir").replaceAll("\\\\", "/") + "\\src\\pics\\background.jpg";
        Image back = new Image(pathBack);
        boolean present= false;
        boolean same = false;

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setBackground(new Background(new BackgroundImage(back, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));

        TextField user = new TextField();
        user.setPromptText("username");
        TextField pass = new TextField();
        pass.setPromptText("password");

        gridPane.add(user, 0, 3);
        gridPane.add(pass, 2, 3);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> user.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(user.getText(), pass.getText());
            }
            return null;
        });
        dialog.showAndWait();

        String u = user.getText();
        String p = pass.getText();

        select.selectAll();
        for (int i = 0; i < select.userNames.size(); i++) {
            if (select.userNames.get(i).equals(u)) {
                present = true;
                index = i;
                if (select.passwords.get(i).equals(p)) {
                    same = true;
                    break;
                }
            }
        }
        if (present && same) {
            try {
                username = u;
                writer.update(null, username);
                accept = true;
                if(localList.contains(username)){
                    accept = false;
                    usernameAlert();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       else if(present){
           //when the user had mistake in entering password
            Dialog<Pair<String, String>> warning = new Dialog<>();
            warning.setTitle("Forgot your password?");

            ButtonType buttonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            warning.getDialogPane().getButtonTypes().add(buttonType);

            GridPane gridPane2 = new GridPane();
            gridPane2.setHgap(10);
            gridPane2.setVgap(10);
            gridPane2.setBackground(new Background(new BackgroundImage(back, BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT)));

            Text forgot = new Text("if you answer to the question below correctly,\nwe will send your password.");
            forgot.setFont(Font.font("Helvetica", FontWeight.BOLD, 17));
            forgot.setFill(GAINSBORO);
            Text question = new Text();
            question.setFont(Font.font("cursive", FontWeight.BOLD, 16));
            question.setFill(PLUM);
            question.setText(select.questions.get(index));
            question.setUnderline(true);
            TextField answer = new TextField();
            answer.setMaxWidth(110);
            answer.setPromptText("enter answer");

            gridPane2.add(forgot, 0, 2);
            gridPane2.add(question, 0, 5);
            gridPane2.add(answer,0,7);

            warning.getDialogPane().setContent(gridPane2);
            warning.setResultConverter(dialogButton -> {
                if (dialogButton == buttonType) {
                    return new Pair<>(question.getText(), answer.getText());
                }
                return null;
            });
            warning.showAndWait();
            answ = answer.getText();
       }

        //retrieve password if the answer is correct
        if(select.answers.get(index).equals(answ)) {
            Dialog<Pair<String, String>> retrieve = new Dialog<>();
            retrieve.setTitle("retrieve password");

            ButtonType b = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            retrieve.getDialogPane().getButtonTypes().add(b);

            GridPane gridPane3 = new GridPane();
            gridPane3.setHgap(10);
            gridPane3.setVgap(10);
            gridPane3.setBackground(new Background(new BackgroundImage(back, BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT)));
            Text explain = new Text();
            explain.setText("Dear \""+ u+"\" your password is: ");
            explain.setFont(Font.font("cursive", FontWeight.BOLD, 17));
            explain.setFill(LIGHTGREY);
            gridPane3.add(explain,0,0);
            Text password = new Text(select.passwords.get(index));
            password.setFont(Font.font("cursive", FontWeight.BOLD, 17));
            password.setFill(LIGHTPINK);
            password.setUnderline(true);
            gridPane3.add(password,0,1);

            retrieve.getDialogPane().setContent(gridPane3);
            retrieve.showAndWait();
        }

       select.selectGender();
       if(select.genders.get(index).equals("female")) gender = "female";
       else if(select.genders.get(index).equals("male")) gender = "male";
     }

    private void usernameAlert(){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Duplicate Username!");
        alert.setHeaderText("This username has been logged in");
        alert.showAndWait();
    }

    private void request(String user, String applicant){
        req = new Alert(Alert.AlertType.INFORMATION);
        req.setTitle("a message for :"+user);
        req.setHeaderText(applicant +" starts private chat with you");
        req.showAndWait();
    }

    @SuppressWarnings("not resolve")
    public void initView() throws FileNotFoundException {
        //chat area
        anchorPane = new AnchorPane();
        ArrayList<Node> elements = new ArrayList<Node>();

        menu = new MenuBar();
        menu.setPrefSize(770, 32);

        Menu onlinePeople = new Menu("online people");
        for(int i=0;i<SignUp.clients.size();i++) {
            if (!SignUp.clients.get(i).getUsername().equals(username)) {
                MenuItem item = new MenuItem(SignUp.clients.get(i).getUsername());
                int index = i;
                item.setOnAction(event -> {
                    intendedName = SignUp.clients.get(index).getUsername();
                    request(intendedName, username);
                    isPrivate = true;
                    SignUp.clients.get(index).isPrivate = true;
                    privateChat.appendText("*** private chat with \""+intendedName+"\"");
                    privateChat.appendText(new SimpleDateFormat(" EEEEE, MMMMM d h:mm a").format(Calendar.getInstance().getTime())+"\n");
                    SignUp.clients.get(index).privateChat.appendText("*** private chat with \""+username+"\"");
                    SignUp.clients.get(index).privateChat.appendText(new SimpleDateFormat(" EEEEE, MMMMM d h:mm a")
                            .format(Calendar.getInstance().getTime())+"\n");
                    SaveChat saveUser = new SaveChat(username);
                    SaveChat saveIntended = new SaveChat(intendedName);
                    try {//write chats in file for intended user
                        saveUser.saveInfo("*** private chat with \""+intendedName+"\"");
                        saveUser.saveInfo(new SimpleDateFormat(" EEEEE, MMMMM d h:mm a")
                                .format(Calendar.getInstance().getTime())+"\n");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {//write chats in file for current user
                        saveIntended.saveInfo("*** private chat with \""+username+"\"");
                        saveIntended.saveInfo(new SimpleDateFormat(" EEEEE, MMMMM d h:mm a")
                                .format(Calendar.getInstance().getTime())+"\n");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    if(!savePrivate.contains(saveUser))
                        savePrivate.add(saveUser);
                    if(!savePrivate.contains(saveIntended))
                        savePrivate.add(saveIntended);
                });
                onlinePeople.getItems().add(item);
            }
        }

        Menu contacts = new Menu("contacts");
        for (int i=0;i<menuPage.contacts.size();i++) {
            MenuItem item = new MenuItem(menuPage.contacts.get(i));
            int index = i;
            item.setOnAction(event -> {
                intendedName = SignUp.clients.get(index).getUsername();
                request(intendedName, username);
                isPrivate = true;
                SignUp.clients.get(index).isPrivate = true;
                privateChat.appendText("*** private chat with \""+intendedName+"\"");
                privateChat.appendText(new SimpleDateFormat(" EEEEE, MMMMM d h:mm a").format(Calendar.getInstance().getTime())+"\n");
                SignUp.clients.get(index).privateChat.appendText("*** private chat with \""+username+"\"");
                SignUp.clients.get(index).privateChat.appendText(new SimpleDateFormat(" EEEEE, MMMMM d h:mm a")
                        .format(Calendar.getInstance().getTime())+"\n");
                SaveChat saveUser = new SaveChat(username);
                try {
                    saveUser.saveInfo("*** private chat with \""+intendedName+"\"");
                    saveUser.saveInfo(new SimpleDateFormat(" EEEEE, MMMMM d h:mm a")
                            .format(Calendar.getInstance().getTime())+"\n");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                SaveChat saveIntended = new SaveChat(intendedName);
                try {
                    saveIntended.saveInfo("*** private chat with \""+username+"\"");
                    saveIntended.saveInfo(new SimpleDateFormat(" EEEEE, MMMMM d h:mm a")
                            .format(Calendar.getInstance().getTime())+"\n");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if(!savePrivate.contains(saveUser))
                    savePrivate.add(saveUser);
                if(!savePrivate.contains(saveIntended))
                    savePrivate.add(saveIntended);

            });
            contacts.getItems().add(item);
        }
        MenuItem backToPublic = new MenuItem("public chat");
        backToPublic.setOnAction(event -> {
            isPrivate = false;
            isPublic = true;
        });
        Menu prv = new Menu("start private chat with...");
        prv.getItems().addAll(onlinePeople,contacts);
        Menu options = new Menu("Options");
        options.getItems().addAll(prv,backToPublic);
        //clear private chat from chat area and file
        Menu clear = new Menu("Clear");
        MenuItem clearPrivate = new MenuItem("clear private chat");
        clearPrivate.setOnAction(event -> {
            privateChat.clear();
            deleteFile = new DeleteFile(username);
        });
        //clear public chat from chat area and file
        MenuItem clearPublic = new MenuItem("clear public chat");
        clearPublic.setOnAction(event -> {
            chat.clear();
            deleteFile = new DeleteFile(username+"Public");
        });
        clear.getItems().addAll(clearPrivate,clearPublic);
        menu.getMenus().addAll(options,clear);
        elements.add(menu);

        //input to entering the message
        input = new TextField();
        input.setPrefSize(524, 76);
        input.setPromptText("Enter message");
        input.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    writer.update(null, username + ": " + input.getText());
                    input.clear();
                }
            }
        });
        elements.add(input);

        chat = new TextArea();
        chat.setPrefSize(374, 246);
        chat.setWrapText(true);
        chat.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        chat.setEditable(false);
        elements.add(chat);

        privateChat = new TextArea();
        privateChat.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        privateChat.setPrefSize(374, 246);
        privateChat.setWrapText(true);
        elements.add(privateChat);

        readChat.readInfo(privateChat,username);
        readChat.readInfo(chat,username+"Public");

        Button sendButton = new Button();
        sendButton.setPrefSize(61, 76);
        sendButton.setText("Send");
        elements.add(sendButton);
        sendButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                writer.update(null, username + ": " + input.getText());
                input.clear();

            }

        });

        ComboBox<String> kaomoji = new ComboBox<String>();
        kaomoji.setPrefSize(161, 31);
        kaomoji.getItems().addAll("♥‿♥","◉_◉","ヽ(`⌒´メ)ノ","( ͡° ͜ʖ ͡°)","̿̿ ̿̿ ̿̿ ̿'̿'\\̵͇̿̿\\з= ( ▀ ͜͞ʖ▀) =ε/̵͇̿̿/’̿’̿ ̿ ̿̿ ̿̿ ̿̿", "(눈_눈)","ಠ_ಠ", "(◕‿◕)♡", "ლ(¯ロ¯ლ)","ლ(ಠ_ಠ ლ","¯\\_(ツ)_/¯",
                "(⊃｡•́‿•̀｡)⊃","(▀̿Ĺ̯▀̿ ̿)","(╯°□°)╯︵ ┻━┻ ","ʕ •ᴥ• ʔ?");
        kaomoji.setPromptText("Pick your emoji!~");
        kaomoji.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                input.setText(input.getText() + newValue);
            }
        });
        elements.add(kaomoji);

        Text publicT = new Text();
        publicT.setText("Public Chat");
        elements.add(publicT);

        privateT = new Text();
        privateT.setText("Private Chat");
        elements.add(privateT);

        anchorPane.getChildren().addAll(elements);
        AnchorPane.setTopAnchor(menu, 0.0);
        AnchorPane.setLeftAnchor(menu, 0.0);
        AnchorPane.setTopAnchor(privateChat, 59.0);
        AnchorPane.setLeftAnchor(privateChat, 10.0);
        AnchorPane.setTopAnchor(kaomoji, 333.0);
        AnchorPane.setLeftAnchor(kaomoji, 602.0);
        AnchorPane.setTopAnchor(sendButton, 314.0);
        AnchorPane.setLeftAnchor(sendButton, 538.0);
        AnchorPane.setTopAnchor(chat, 59.0);
        AnchorPane.setLeftAnchor(chat, 389.0);
        AnchorPane.setTopAnchor(input, 314.0);
        AnchorPane.setLeftAnchor(input, 10.0);
        AnchorPane.setTopAnchor(publicT, 40.0);
        AnchorPane.setLeftAnchor(publicT,389.0);
        AnchorPane.setTopAnchor(privateT, 40.0);
        AnchorPane.setLeftAnchor(privateT, 10.0);
    }

    private void setUpNetworking() throws Exception {
        @SuppressWarnings("resource")
        Socket socket = new Socket(InetAddress.getByName(null), 8000);
        InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
        reader = new BufferedReader(streamReader);
        writer = new ClientObserver(socket.getOutputStream());
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class IncomingReader implements Runnable {
        public void run() {
            String message;

            try {
                while ((message = reader.readLine()) != null) {
                    if (accept) {
                        displayMessage(message);
                    } else
                        localList.add(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayMessage(String input) throws FileNotFoundException {
        int index =0;
        int userIndex = 0;

        for(int i=0;i<savePrivate.size();i++){
            if(savePrivate.get(i).getFileName().equals(username))
                userIndex = i;
        }

        if(intendedName!=null) {
            for(int i=0;i<SignUp.clients.size();i++){
                if(SignUp.clients.get(i).equals(intendedName))
                    index = i;
            }
        }

        if(isPrivate && SignUp.clients.get(index).isPrivate && !isPublic){
            privateChat.appendText("\n" + input);
            savePrivate.get(userIndex).saveInfo(input + "\n");
        }
        else if(isPublic){
            SaveChat savePublic = new SaveChat(username+"Public");
            chat.appendText(new SimpleDateFormat("EEEEE, MMMMM " + "d h:mm a").format(Calendar.getInstance().getTime())+"\n");
            chat.appendText(input + "\n");
            savePublic.saveInfo(new SimpleDateFormat("EEEEE, MMMMM " + "d h:mm a").format(Calendar.getInstance().getTime())+"\n");
            savePublic.saveInfo(input + "\n");

        }
    }
}
