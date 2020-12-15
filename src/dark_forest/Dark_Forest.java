package dark_forest;
import java.sql.*;

public class Dark_Forest {

    public static void main(String[] args) {
        try {
	Connection conn = DriverManager.getConnection("jdbc:sqlite:./src/save/database.db");

    	if (conn != null) {
            System.out.println("Connected to database.db");
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Game(conn).setVisible(true);
                System.out.println("hello");
            }
        });

	} catch (SQLException e) {
    	System.out.println("An error occurred");
   		e.printStackTrace();
	}
        

    }
    
}
