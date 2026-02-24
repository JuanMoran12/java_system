package loginjuan;

import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Ventas {

    private JPanel panelCentral;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // Colores
    private final Color COLOR_FONDO = new Color(248, 250, 252);
    private final Color COLOR_PRIMARIO = new Color(15, 23, 42);
    private final Color COLOR_SECUNDARIO = new Color(52, 73, 94);
    private final Color COLOR_ACCION = new Color(52, 152, 219);
    private final Color COLOR_EXITO = new Color(46, 204, 113);
    private final Color COLOR_TEXTO = new Color(30, 41, 59);

    public JPanel getVentasPanel(int parentW, int parentH) {
        panelCentral = new JPanel();
        panelCentral.setBounds(0, 0, parentW, parentH);
        panelCentral.setBackground(COLOR_FONDO);
        panelCentral.setLayout(null);

        // --- BARRA SUPERIOR DE NAVEGACIÓN ---
        int navHeight = 60;
        int navY = 20;
        int navMargin = 30;

        JPanel navPanel = new RoundedPanel(15);
        navPanel.setBounds(navMargin, navY, parentW - (2 * navMargin), navHeight);
        navPanel.setBackground(Color.WHITE);
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelCentral.add(navPanel);

        // Botones de navegación
        RoundButton btnGanancias = createNavButton("💰 Ganancias Diarias");
        RoundButton btnRanking = createNavButton("🔥 Productos Top");
        RoundButton btnCompras = createNavButton("🚚 Compras");
        RoundButton btnFacturas = createNavButton("📄 Facturas");

        navPanel.add(btnGanancias);
        navPanel.add(btnRanking);
        navPanel.add(btnCompras);
        navPanel.add(btnFacturas);

        // --- CONTENIDO (Cartas) ---
        int contentY = navY + navHeight + 20;
        int contentH = parentH - contentY - 20;

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBounds(navMargin, contentY, parentW - (2 * navMargin), contentH);
        cardPanel.setOpaque(false);
        panelCentral.add(cardPanel);

        // Agregar las vistas
        cardPanel.add(createGananciasPanel(), "GANANCIAS");
        cardPanel.add(createRankingPanel(), "RANKING");
        cardPanel.add(createHistorialComprasPanel(), "COMPRAS");
        cardPanel.add(createFacturasPanel(), "FACTURAS");

        // Listeners
        btnGanancias.addActionListener(e -> cardLayout.show(cardPanel, "GANANCIAS"));
        btnRanking.addActionListener(e -> cardLayout.show(cardPanel, "RANKING"));
        btnCompras.addActionListener(e -> {
            cardLayout.show(cardPanel, "COMPRAS");
            // Recargar tabla de compras si es necesario
        });
        btnFacturas.addActionListener(e -> cardLayout.show(cardPanel, "FACTURAS"));

        return panelCentral;
    }

    private RoundButton createNavButton(String text) {
        RoundButton btn = new RoundButton(text, 14);
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setBackground(COLOR_SECUNDARIO);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    // --- VISTA 1: GANANCIAS DIARIAS ---
    private JPanel createGananciasPanel() {
        JPanel p = new RoundedPanel(20);
        p.setBackground(Color.WHITE);
        p.setLayout(new BorderLayout(20, 20)); // Padding interno
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Análisis Financiero Básico - Ventas Diarias");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(COLOR_TEXTO);
        p.add(lblTitle, BorderLayout.NORTH);

        DefaultTableModel infoModel = new DefaultTableModel(new String[] { "Fecha", "Total Ventas ($)" }, 0);
        JTable table = createProfessionalTable(infoModel);

        cargarGanancias(infoModel);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void cargarGanancias(DefaultTableModel m) {
        m.setRowCount(0);
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:ventasdb.db")) {
            String sql = "SELECT date(fec_factura) as fecha, SUM(monto_factura) as total FROM facturas GROUP BY date(fec_factura) ORDER BY fecha DESC";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                m.addRow(new Object[] { rs.getString("fecha"), String.format("$%.2f", rs.getDouble("total")) });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- VISTA 2: RANKING PRODUCTOS ---
    private JPanel createRankingPanel() {
        JPanel p = new RoundedPanel(20);
        p.setBackground(Color.WHITE);
        p.setLayout(new BorderLayout(20, 20));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Ranking de Productos Más Vendidos");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(COLOR_TEXTO);
        p.add(lblTitle, BorderLayout.NORTH);

        DefaultTableModel infoModel = new DefaultTableModel(
                new String[] { "Producto", "Unidades Vendidas" }, 0);
        JTable table = createProfessionalTable(infoModel);

        cargarRanking(infoModel);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void cargarRanking(DefaultTableModel m) {
        m.setRowCount(0);
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:ventasdb.db")) {
            String sql = "SELECT p.nom_prod, SUM(c.car_can) as cant " +
                    "FROM carrito c " +
                    "JOIN productos p ON c.car_pro = p.id_prod " +
                    "GROUP BY c.car_pro " +
                    "ORDER BY cant DESC LIMIT 20";

            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                m.addRow(new Object[] {
                        rs.getString("nom_prod"),
                        rs.getInt("cant")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error carga ranking: " + e.getMessage());
        }
    }

    // --- VISTA 3: HISTORIAL COMPRAS ---
    private JPanel createHistorialComprasPanel() {
        JPanel p = new RoundedPanel(20);
        p.setBackground(Color.WHITE);
        p.setLayout(new BorderLayout(20, 20));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblTitle = new JLabel("Historial de Compras y Abastecimiento");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(COLOR_TEXTO);
        top.add(lblTitle, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Actualizar");
        btnRefresh.addActionListener(e -> cargarCompras(
                (DefaultTableModel) ((JTable) ((JScrollPane) p.getComponent(1)).getViewport().getView()).getModel()));
        top.add(btnRefresh, BorderLayout.EAST);
        p.add(top, BorderLayout.NORTH);

        DefaultTableModel infoModel = new DefaultTableModel(new String[] { "ID", "Fecha", "Proveedor", "Total" }, 0);
        JTable table = createProfessionalTable(infoModel);

        cargarCompras(infoModel);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void cargarCompras(DefaultTableModel m) {
        m.setRowCount(0);
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:ventasdb.db")) {
            String sql = "SELECT c.id_compra, c.fecha_compra, p.nom_proveedor, c.total_compra " +
                    "FROM compras c JOIN proveedores p ON c.id_proveedor = p.id " +
                    "ORDER BY c.fecha_compra DESC";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                m.addRow(new Object[] {
                        rs.getInt("id_compra"),
                        rs.getString("fecha_compra"),
                        rs.getString("nom_proveedor"),
                        String.format("$%.2f", rs.getDouble("total_compra"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- VISTA 4: FACTURAS (VENTAS) ---
    private JPanel createFacturasPanel() {
        JPanel p = new RoundedPanel(20);
        p.setBackground(Color.WHITE);
        p.setLayout(new BorderLayout(20, 20));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Filtros
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Buscar Cédula");

        DefaultTableModel infoModel = new DefaultTableModel(
                new String[] { "ID Factura", "Fecha", "Cédula Cliente", "Total", "M. Pago" }, 0);

        btnSearch.addActionListener(e -> cargarFacturas(infoModel, txtSearch.getText()));

        filterPanel.add(new JLabel("Cédula Cliente:"));
        filterPanel.add(txtSearch);
        filterPanel.add(btnSearch);

        p.add(filterPanel, BorderLayout.NORTH);

        JTable table = createProfessionalTable(infoModel);
        cargarFacturas(infoModel, "");

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void cargarFacturas(DefaultTableModel m, String clientFilter) {
        m.setRowCount(0);
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:ventasdb.db")) {
            String sql = "SELECT * FROM facturas";
            if (!clientFilter.isEmpty()) {
                sql += " WHERE ced_factura LIKE '%" + clientFilter + "%'";
            }
            sql += " ORDER BY fec_factura DESC";

            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                m.addRow(new Object[] {
                        rs.getInt("id_factura"),
                        rs.getString("fec_factura"),
                        rs.getInt("ced_factura"),
                        String.format("$%.2f", rs.getDouble("monto_factura")),
                        rs.getString("tipo_pago")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error facturas: " + e.getMessage());
        }
    }

    // --- UTILIDADES ---
    private JTable createProfessionalTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(COLOR_PRIMARIO); // Azul oscuro
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(224, 242, 254));
        table.setSelectionForeground(COLOR_TEXTO);
        table.setGridColor(new Color(241, 245, 249));

        // Centrar celdas
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);

        return table;
    }
}
