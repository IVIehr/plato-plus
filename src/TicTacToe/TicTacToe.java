package TicTacToe;

import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jdbc.SelectApp;
import jdbc.UpdateApp;

public class TicTacToe extends Application {

    private String username;
    private int indexx;
    private int countLose;
    private int countTie ;
    Stage hostStage;
    UpdateApp app = new UpdateApp();
    SelectApp select = new SelectApp();
    Board board = new Board();
    Turn boardTurn = new Turn();

    Label cell1, cell2, cell3,
            cell4, cell5, cell6,
            cell7, cell8, cell9;
    Label[] cells;

    public TicTacToe(Stage stage, String username , int index){
        this.hostStage = stage;
        this.username = username;
        indexx = index;
    }

    @Override
    public void start(Stage primaryStage) {
        select.selectGameInfo();
        countTie = select.ties.get(indexx);
        countLose = select.loses.get(indexx);
        //set stage for first dialog
        Stage stage = new Stage();
        GridPane firstGrid = new GridPane();
        firstGrid.setId("firstDialog");
        firstGrid.setPadding(new Insets(20, 20, 20, 20));
        firstGrid.setVgap(20);
        firstGrid.setHgap(50);
        //set Icon
        String pathIcon = path("icontictac.png");
        Image icon = new Image(pathIcon);
        primaryStage.getIcons().add(icon);
        stage.getIcons().add(icon);

        //First Dialog Labels and Buttons
        Label label = new Label("Who will play first?");
        Button IWillPlay = new Button("me");
        Button YouPlay = new Button("computer");
        firstGrid.add(label, 0, 0, 2, 1);
        firstGrid.add(IWillPlay, 0, 1, 1, 1);
        firstGrid.add(YouPlay, 1, 1, 1, 1);

        //Scene for the firstDialog
        Scene sc = new Scene(firstGrid, 400, 150);
        firstGrid.setAlignment(Pos.CENTER);
        sc.getStylesheets().addAll(this.getClass().getResource("firstDialog.css").toExternalForm());
        stage.setTitle("Choose turn");
        stage.setScene(sc);
        stage.setOnCloseRequest(e -> {
            System.exit(0);
        });

        //Board scene
        GridPane grid = new GridPane();

        cell1 = new Label();
        cell2 = new Label();
        cell3 = new Label();
        cell4 = new Label();
        cell5 = new Label();
        cell6 = new Label();
        cell7 = new Label();
        cell8 = new Label();
        cell9 = new Label();

        cells = new Label[]{cell1, cell2, cell3,
                cell4, cell5, cell6,
                cell7, cell8, cell9};

        for (Label cell : cells) {
            cell.setMinSize(128, 128);
            boolean isUsed = false;
            cell.setUserData(isUsed);
        }

        grid.addRow(0, cell1, cell2, cell3);
        grid.addRow(1, cell4, cell5, cell6);
        grid.addRow(2, cell7, cell8, cell9);

        grid.setAlignment(Pos.CENTER);
        grid.setMaxSize(800, 800);
        grid.setGridLinesVisible(true);
        grid.setId("board");

        boardTurn.next = Turn.NextMove.O;
        Image OPic = new Image(getClass().getResourceAsStream("O.png"));
        Image XPic = new Image(getClass().getResourceAsStream("X.png"));

        for (Label cell : cells) {

            cell.setOnMouseClicked(event -> {
                if (!((boolean) cell.getUserData())) {
                    cell.setGraphic(new ImageView(XPic));

                    int index = -1;
                    for (int i = 0; i < cells.length; ++i) {
                        if (cell == cells[i]) {
                            index = i;
                        }
                    }

                    board.placeAMove(new Point(index / 3, index % 3), 2);
                    boolean mark = true;
                    int next = 0;
                    next = board.returnNextMove();

                    if (next != -1) {   //If the game isn't finished yet!
                        int indexCell = next;

                        cells[indexCell].setGraphic(new ImageView(OPic));
                        cells[indexCell].setUserData(mark); //Used!
                        board.placeAMove(new Point(indexCell / 3, indexCell % 3), 1);
                        cell.setUserData(mark);
                    }

                    if (board.isGameOver()) {
                        Stage stage2 = new Stage();
                        GridPane g2 = new GridPane();
                        g2.setPadding(new Insets(20, 20, 20, 20));
                        g2.setVgap(20);
                        g2.setHgap(20);
                        Label label2 = new Label();
                        if (board.hasXWon()) {
                            app.update(username,String.valueOf(0), String.valueOf(countTie),String.valueOf(countLose + 1));
                            label2.setText("You lost!");
                            stage2.setTitle("Game over!");

                        } else {
                            app.update(username,String.valueOf(0), String.valueOf(countTie+1),String.valueOf(countLose));
                            label2.setText("tie!");
                            stage2.setTitle("It's a draw!");
                        }
                        g2.add(label2, 0, 0, 3, 1);
                        Button onceMore = new Button("play again!");
                        Button quit = new Button("I quit!");
                        Button menu = new Button(" back to menu");g2.add(onceMore, 1, 1, 1, 1);
                        g2.add(quit, 2, 1, 1, 1);
                        g2.add(menu, 3, 1, 1, 1);
                        onceMore.setOnMouseClicked(q -> {
                            primaryStage.close();
                            stage2.close();
                            board.resetBoard();
                            start(new Stage());
                        });
                        menu.setOnMouseClicked(event1 -> {
                            primaryStage.setX(480);
                            primaryStage.setY(140);
                            stage2.close();
                            primaryStage.close();
                            hostStage.show();
                        });

                        quit.setOnMouseClicked(q -> {
                            System.exit(0);
                        });
                        Scene scene = new Scene(g2);
                        scene.getStylesheets().addAll(this.getClass().getResource("result.css").toExternalForm());
                        stage2.setScene(scene);
                        stage2.setOnCloseRequest(q -> {
                            primaryStage.close();
                        });
                        stage2.show();
                    }
                }
            });
        };

        //game scene
        Scene scene = new Scene(grid);
        primaryStage.setX(sc.getX()+763);
        primaryStage.setY(sc.getY()+180);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(scene);

        scene.getStylesheets().addAll(this.getClass().getResource("board.css").toExternalForm());

        //FirstWindow Action Listeners
        IWillPlay.setOnMouseClicked((event) -> {
            boardTurn.next = Turn.NextMove.X;
            stage.close();
        });

        YouPlay.setOnMouseClicked((event) -> {
            int index = new Random().nextInt(9);
            cells[index].setGraphic(new ImageView(OPic));
            cells[index].setUserData(new Boolean(true));
            board.placeAMove(new Point(index / 3, index % 3), 1);
            boardTurn.next = Turn.NextMove.X;
            stage.close();
        });
        stage.showAndWait();
        primaryStage.show();
    }
    public String path(String path){
        return "file:"+System.getProperty("user.dir").replaceAll("\\\\","/")+"\\src\\pics\\"+path;
    }
    public static void main(String[] args) {
        launch(args);
    }

}








