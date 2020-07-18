package jdbc;

import menu.User;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class SelectApp {

     public ArrayList<String> userNames = new ArrayList<String>();
     public ArrayList<String> passwords = new ArrayList<String>();
     public ArrayList<String> questions = new ArrayList<String>();
     public ArrayList<String> answers = new ArrayList<String>();
     public ArrayList<String> emails = new ArrayList<String>();
     public ArrayList<String> genders = new ArrayList<String>();
     public ArrayList<Integer> wins = new ArrayList<>();
     public ArrayList<Integer> loses = new ArrayList<>();
     public ArrayList<Integer> ties = new ArrayList<>();

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:"+"file:"+System.getProperty("user.dir").replaceAll("\\\\","/")+"\\src\\db\\users.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void selectAll(){
        String sql = "SELECT username, password, question, answer FROM Users";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {

                userNames.add(rs.getString("username"));
                passwords.add(rs.getString("password"));
                questions.add(rs.getString("question"));
                answers.add(rs.getString("answer"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectEmail(){
        String sql = "SELECT email FROM Users";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            for(int i=0;rs.next();i++){
                emails.add(rs.getString("email"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectGender(){
        String sql = "SELECT gender FROM Users";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            for(int i=0;rs.next();i++){
                genders.add(rs.getString("gender"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectGameInfo(){
        String sql = "SELECT tie,lose,win FROM Users";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            wins.clear();
            ties.clear();
            loses.clear();
            for(int i=0;rs.next();i++){

                String t = rs.getString("tie");
                String l = rs.getString("lose");
                String w = rs.getString("win");
                ties.add(Integer.parseInt(t));
                loses.add(Integer.parseInt(l));
                wins.add(Integer.parseInt(w));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
