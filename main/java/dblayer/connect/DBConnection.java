package dblayer.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//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
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bank";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
