package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateApp {

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

    public void update(String username,String win,String tie,String lose) {
        String sql = "UPDATE Users SET win = ? , "
                + "tie = ? ,"
                + "lose = ? "
                + "WHERE username = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, win);
            pstmt.setString(2, tie);
            pstmt.setString(3,lose);
            pstmt.setString(4,username);

            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
