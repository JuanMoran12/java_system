package loginjuan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditLogger {

    public static void registrar(int idUsuario, String modulo, String accion) {
        String sql = "INSERT INTO audit_log (id_usuario, modulo, accion) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.setString(2, modulo);
            pstmt.setString(3, accion);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al registrar auditoría: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
