package loginjuan;

import java.awt.Color;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class LogsModulo {

    // Colores modernos consistentes con el resto de la aplicación
    private final Color COLOR_FONDO_PRINCIPAL = new Color(248, 250, 252);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_TEXTO_PRINCIPAL = new Color(15, 23, 42);
    private final Color COLOR_TEXTO_SECUNDARIO = new Color(71, 85, 105);
    private final Color COLOR_ACCENT = new Color(29, 78, 216);
    private final Color COLOR_BORDE = new Color(226, 232, 240);
    private final Color COLOR_HEADER_TABLE = new Color(241, 245, 249);

    private JTable tablaLogs;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> filtroTipo;

    public JPanel getLogsPanel(int width, int height) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(COLOR_FONDO_PRINCIPAL);
        mainPanel.setBounds(0, 0, width, height);

        // Tarjeta contenedora principal
        RoundedPanel card = new RoundedPanel(28);
        int cardMargin = 30;
        int cardX = cardMargin;
        int cardY = cardMargin;
        int cardW = width - (2 * cardMargin);
        int cardH = height - (2 * cardMargin);
        card.setBounds(cardX, cardY, cardW, cardH);
        card.setBackground(COLOR_TARJETA);
        card.setLayout(null);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(25, 35, 35, 35)));

        // Título del módulo
        JLabel titulo = new JLabel("📋 Logs del Sistema");
        titulo.setBounds(30, 30, cardW - 60, 45);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(COLOR_TEXTO_PRINCIPAL);
        card.add(titulo);

        // Subtítulo
        JLabel subtitulo = new JLabel("Registro de actividades y eventos del sistema");
        subtitulo.setBounds(30, 80, cardW - 60, 25);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitulo.setForeground(COLOR_TEXTO_SECUNDARIO);
        card.add(subtitulo);

        // Panel de filtros
        RoundedPanel filtrosPanel = new RoundedPanel(16);
        filtrosPanel.setBounds(30, 120, cardW - 60, 60);
        filtrosPanel.setBackground(COLOR_HEADER_TABLE);
        filtrosPanel.setLayout(null);
        filtrosPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        card.add(filtrosPanel);

        JLabel lblFiltro = new JLabel("Filtrar por tipo:");
        lblFiltro.setBounds(15, 15, 120, 30);
        lblFiltro.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFiltro.setForeground(COLOR_TEXTO_PRINCIPAL);
        filtrosPanel.add(lblFiltro);

        String[] tiposLog = { "Todos", "LOGIN", "LOGOUT", "CREAR_CLIENTE", "EDITAR_CLIENTE", "ELIMINAR_CLIENTE",
                "CREAR_PRODUCTO", "EDITAR_PRODUCTO", "ELIMINAR_PRODUCTO", "TRANSACCION","AJUSTE_STOCK","COMPRA_REGISTRADA"};
        filtroTipo = new JComboBox<>(tiposLog);
        filtroTipo.setBounds(135, 15, 200, 30);
        filtroTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filtroTipo.setBackground(Color.WHITE);
        filtroTipo.addActionListener(e -> cargarLogs());
        filtrosPanel.add(filtroTipo);

        // Botón de actualizar
        RoundButton btnActualizar = new RoundButton("🔄 Actualizar", 12);
        btnActualizar.setBounds(355, 15, 130, 30);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setGradientColors(new Color(30, 58, 138), new Color(23, 37, 84));
        btnActualizar.setHoverGradientColors(new Color(29, 78, 216), new Color(30, 58, 138));
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(e -> cargarLogs());
        filtrosPanel.add(btnActualizar);

        // Tabla de logs
        String[] columnas = { "ID", "Fecha/Hora", "Usuario", "Rol", "Módulo", "Acción" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaLogs = new JTable(modeloTabla);
        tablaLogs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaLogs.setRowHeight(35);
        tablaLogs.setGridColor(COLOR_BORDE);
        tablaLogs.setSelectionBackground(new Color(219, 234, 254));
        tablaLogs.setSelectionForeground(COLOR_TEXTO_PRINCIPAL);

        // Configurar ancho de columnas
        tablaLogs.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tablaLogs.getColumnModel().getColumn(1).setPreferredWidth(180); // Fecha/Hora
        tablaLogs.getColumnModel().getColumn(2).setPreferredWidth(120); // Usuario
        tablaLogs.getColumnModel().getColumn(3).setPreferredWidth(100); // Rol
        tablaLogs.getColumnModel().getColumn(4).setPreferredWidth(150); // Módulo
        tablaLogs.getColumnModel().getColumn(5).setPreferredWidth(150); // Acción

        // Estilo del header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(COLOR_HEADER_TABLE);
        headerRenderer.setForeground(COLOR_TEXTO_PRINCIPAL);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < tablaLogs.getColumnModel().getColumnCount(); i++) {
            tablaLogs.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Renderizador centralizado para todas las columnas excepto Módulo/Acción
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaLogs.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaLogs.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaLogs.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaLogs.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tablaLogs);
        scrollPane.setBounds(30, 195, cardW - 60, cardH - 240);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane);

        mainPanel.add(card);

        // Cargar logs iniciales
        cargarLogs();

        return mainPanel;
    }

    private void cargarLogs() {
        modeloTabla.setRowCount(0);

        String filtro = (String) filtroTipo.getSelectedItem();

        String sql = "SELECT a.id, a.fecha, COALESCE(l.usuario, 'Desconocido') AS usuario, "
                + "COALESCE(l.rol, '-') AS rol, a.modulo, a.accion "
                + "FROM audit_log a LEFT JOIN login l ON a.id_usuario = l.id_usuario";
        if (!"Todos".equals(filtro)) {
            sql += " WHERE a.accion = '" + filtro + "'";
        }
        sql += " ORDER BY a.fecha DESC";

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:sqlite:ventasdb.db");
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] fila = {
                        rs.getInt("id"),
                        rs.getString("fecha"),
                        rs.getString("usuario"),
                        rs.getString("rol"),
                        rs.getString("modulo"),
                        rs.getString("accion")
                };
                modeloTabla.addRow(fila);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
