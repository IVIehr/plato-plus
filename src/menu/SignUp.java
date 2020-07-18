package menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdbc.InsertApp;
import jdbc.SelectApp;
import network.ClientMain;
import java.io.File;
import java.util.ArrayList;
import static javafx.scene.paint.Color.*;

public class SignUp {

    public static ArrayList<ClientMain> clients = new ArrayList<ClientMain>();
    public static ArrayList<User> users = new ArrayList<User>();
    private boolean passIsCorrect;
    private boolean emailIsCorrect;
    private boolean repetitiveEmail;
    private boolean repetitiveUserName;
    private boolean isFill;
    private String question;
    private String gender;
    InsertApp newData = new InsertApp();
    SelectApp select = new SelectApp();

    public GridPane signUp(){
        //error sound
        String errorBip =System.getProperty("user.dir")+"\\src\\menu\\error.wav";
        Media hitError = new Media(new File(errorBip).toURI().toString());
        MediaPlayer playError = new MediaPlayer(hitError);
        //confirm sound
        String confirmBip =System.getProperty("user.dir")+"\\src\\menu\\confirm.wav";
        Media hitConfirm = new Media(new File(confirmBip).toURI().toString());
        MediaPlayer playConfirm = new MediaPlayer(hitConfirm);

        Text logIn = new Text("Log in");
        logIn.setFont(Font.font("Helvetica", FontWeight.BOLD,17));
        logIn.setFill(ORCHID);
        logIn.setOnMouseEntered(event ->logIn.setFill(MIDNIGHTBLUE));
        logIn.setOnMouseExited(event ->logIn.setFill(ORCHID));
        logIn.setOnMouseClicked(event -> {

            ClientMain client = new ClientMain();
            try {
                client.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        //create root for sign up textFields
        GridPane signUpPane = new GridPane();
        signUpPane.setPadding(new Insets(15));
        signUpPane.setHgap(5);
        signUpPane.setVgap(5);
        signUpPane.setAlignment(Pos.CENTER);
        Text ask = new Text("Have an account?");
        ask.setFont(Font.font("Courier", FontWeight.BOLD,18));
        ask.setFill(LIGHTSTEELBLUE);

        signUpPane.add(new Text("Enter an username:"), 0, 0);
        TextField username = new TextField();
        signUpPane.add(username, 1, 0);
        signUpPane.add(new Text("create a password:"), 0, 1);
        TextField password = new TextField();
        signUpPane.add(password, 1, 1);
        signUpPane.add(new Text("Enter your email:"), 0, 3);
        TextField email = new TextField();
        signUpPane.add(email, 1, 3);
        signUpPane.add(new Text("choose a question:"), 0, 5);

        ComboBox<String> questions = new ComboBox<String>();
        questions.getItems().addAll(
                "what is your favorite color?",
                "who is your best friend?",
                "what is your favorite food?",
                "what is your favorite animal?",
                "what is your favorite movie?",
                "What is your favorite book to read?"
        );

        questions.setOnAction(event -> {
            question = questions.getValue();
        });
        questions.setMaxWidth(300);

        ComboBox<String> genders = new ComboBox<String>();
        genders.getItems().addAll("female","male");

        genders.setOnAction(event -> {
            gender = genders.getValue();
        });
        signUpPane.add(questions, 1, 5);
        signUpPane.add(new Text("Answer:"), 0, 6);
        TextField answer = new TextField();
        signUpPane.add(answer, 1, 6);
        signUpPane.add(new Text("Gender:"), 0, 7);
        signUpPane.add(genders, 1, 7);
        signUpPane.add(ask, 0, 13);
        signUpPane.add(logIn,1,13);

        Button submit = new Button("Create Account");
        submit.setStyle
                (
                        "-fx-background-color: #9fdee2;"
                                + "-fx-border-style: solid inside;"
                                + "-fx-border-width: 2;"
                                + "-fx-border-insets: 2;"
                                + "-fx-border-radius: 2;"
                                + "-fx-border-color: #3367c6;"
                );
        signUpPane.add(submit,1,9);

        submit.setOnAction(event -> {
            String thisEmail = email.getText();
            String thisUserName = username.getText();
            //check length of password
            if(password.getText().length()<8){
                playError.play();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "a password must have at least 8 characters", ButtonType.OK);
                alert.showAndWait();
                passIsCorrect = false;
            }
            else passIsCorrect = true;

            //check correct format of email
            if(!(email.getText().endsWith("@gmail.com") || email.getText().endsWith("@yahoo.com")
                    || email.getText().endsWith("@um.ac.ir") || email.getText().endsWith("@outlook.com"))){
                playError.play();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Email format is not correct", ButtonType.OK);
                alert.showAndWait();
                emailIsCorrect = false;
            }
            else emailIsCorrect = true;

            //check unique email
            select.selectEmail();
            for (String inputEmail : select.emails) {
                if (inputEmail.equals(thisEmail)) {
                    repetitiveEmail = true;
                    break;
                }
                else repetitiveEmail = false;
            }

            if(repetitiveEmail){
                playError.play();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "This email has signed up before", ButtonType.OK);
                alert.showAndWait();
            }

            //check unique username
            select.selectAll();
            for (String inputUsers : select.userNames) {
                if (inputUsers.equals(thisUserName)) {
                    repetitiveUserName = true;
                    break;
                }
                else repetitiveUserName = false;
            }

            if(repetitiveUserName){
                playError.play();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Sorry, this username is already taken", ButtonType.OK);
                alert.showAndWait();
            }

            if(username.getText().equals("")||password.getText().equals("")|| email.getText().equals("")
                    ||question.equals("")||answer.getText().equals("")||gender.equals("")){
                isFill = true;
                playError.play();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Please fill all the blanks", ButtonType.OK);
                alert.showAndWait();
            }
            //enter data to database
            playError.stop();
            if(passIsCorrect && emailIsCorrect && !repetitiveEmail && !repetitiveUserName && !isFill) {
                newData.insert(username.getText(), password.getText(),gender,email.getText(),
                        question,answer.getText(),String.valueOf(0),String.valueOf(0),String.valueOf(0));
                users.add(new User(thisUserName,password.getText(),thisEmail,question,answer.getText(),gender));
                playConfirm.play();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "you sign up in \"PLATOPLUS\" successfully!", ButtonType.OK);
                    alert.showAndWait();
                    username.clear();
                    password.clear();
                    questions.setValue("");
                    email.clear();
                    answer.clear();
                    genders.setValue("");
            }
        });
        return signUpPane;
    }
}
