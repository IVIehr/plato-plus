package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertApp {

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:"+"file:"+System.getProperty("user.dir").replaceAll("\\\\","/")+"\\src\\db\\users.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
    public void insert(String username,String password,String gender,String email,String question,
                       String answer,String win,String tie,String lose) {

        String sql = "INSERT INTO Users(username,password,gender,email,question,answer,win,tie,lose) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3,gender);
            pstmt.setString(4,email);
            pstmt.setString(5,question);
            pstmt.setString(6,answer);
            pstmt.setString(7,win);
            pstmt.setString(8,tie);
            pstmt.setString(9,lose);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}