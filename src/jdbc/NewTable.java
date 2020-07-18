package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class NewTable {

    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:"+"file:"+System.getProperty("user.dir").replaceAll("\\\\","/")+"\\src\\db\\users.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                + "	username text PRIMARY KEY,\n"
                + "	password text NOT NULL ,\n"
                + "	gender text NOT NULL,\n"
                + "	email text NOT NULL UNIQUE ,\n"
                + "	question text Not NUll,\n"
                + "	answer text Not NUll,\n"
                + "	win text Not NUll,\n"
                + "	tie text Not NUll,\n"
                + " lose text Not NUll\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        createNewTable();
    }

}
