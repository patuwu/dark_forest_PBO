package dark_forest;
import java.sql.*;

public interface actions {
    void attack(Entity host, Entity target, int atk, Connection conn);
}
