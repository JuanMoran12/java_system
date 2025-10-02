package loginjuan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.DriverManager;  
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.sql.Connection;

public class Factura {

    private final JFrame frame;
    private final JLabel lblSubtotal;
    private final JLabel lblImpuesto;
    private final JLabel lblTotal;
    private final DefaultTableModel modelo;

    private final NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "VE"));
    private static final AtomicInteger SECUENCIA = new AtomicInteger(2540); // autoincremento simple

    public Factura(String cedulaCliente, double totalCalculado, String nombreCliente, String metodoDePago) {
        frame = new JFrame("Factura");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 700);  // Ajustado para coincidir con las otras ventanas
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245, 247, 250));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94)); // Color principal de la aplicación
        header.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel titulo = new JLabel("Sistema de Gestión");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        
        JLabel subtitulo = new JLabel("Factura de Venta");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(200, 200, 200));
        
        JPanel tituloPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        tituloPanel.setOpaque(false);
        tituloPanel.add(titulo);
        tituloPanel.add(subtitulo);
        
        header.add(tituloPanel, BorderLayout.WEST);
        
        // Número de factura en la esquina superior derecha (tomado de la BD)
        int numeroFactura = obtenerSiguienteNumeroFactura();
        JLabel numFactura = new JLabel("Factura #" + numeroFactura);
        numFactura.setFont(new Font("Arial", Font.BOLD, 16));
        numFactura.setForeground(Color.WHITE);
        header.add(numFactura, BorderLayout.EAST);
        
        frame.add(header, BorderLayout.NORTH);

        // --- Contenido principal ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Panel de información del cliente y factura
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        infoPanel.setBackground(new Color(245, 247, 250));

        // Panel de información del cliente
        RoundedPanel clientePanel = new RoundedPanel(16);
        clientePanel.setLayout(new BorderLayout());
        clientePanel.setBackground(Color.WHITE);
        clientePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel clienteTitulo = new JLabel("INFORMACIÓN DEL CLIENTE");
        clienteTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        clienteTitulo.setForeground(new Color(100, 100, 100));
        clienteTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JPanel clienteInfo = new JPanel(new GridLayout(3, 1, 5, 5));
        clienteInfo.setBackground(Color.WHITE);
        
        JLabel nombreLabel = new JLabel("Nombre: " + nombreCliente);
        nombreLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JLabel cedulaLabel = new JLabel("Cédula: " + (cedulaCliente == null ? "—" : cedulaCliente));
        cedulaLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        
        clienteInfo.add(clienteTitulo);
        clienteInfo.add(nombreLabel);
        clienteInfo.add(cedulaLabel);
        
        clientePanel.add(clienteInfo, BorderLayout.NORTH);

        // Panel de información de factura
        RoundedPanel facturaPanel = new RoundedPanel(16);
        facturaPanel.setLayout(new BorderLayout());
        facturaPanel.setBackground(Color.WHITE);
        facturaPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel facturaTitulo = new JLabel("INFORMACIÓN DE FACTURA");
        facturaTitulo.setFont(new Font("Arial", Font.BOLD, 12));
        facturaTitulo.setForeground(new Color(100, 100, 100));
        facturaTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JPanel facturaInfo = new JPanel(new GridLayout(3, 1, 5, 5));
        facturaInfo.setBackground(Color.WHITE);
        
        String fecha = LocalDate.now().toString();
        JLabel fechaLabel = new JLabel("Fecha: " + fecha);
        fechaLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JLabel metodoPagoLabel = new JLabel("Método de pago: " + metodoDePago);
        metodoPagoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        
        facturaInfo.add(facturaTitulo);
        facturaInfo.add(fechaLabel);
        facturaInfo.add(metodoPagoLabel);
        
        facturaPanel.add(facturaInfo, BorderLayout.NORTH);

        infoPanel.add(clientePanel);
        infoPanel.add(facturaPanel);
        
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // --- Tabla de productos ---
        String[] columnas = {"Ítem", "Descripción", "Precio", "Cant.", "Total"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Integer.class;
                    case 2, 4 -> Double.class;
                    case 3 -> Integer.class;
                    default -> String.class;
                };
            }
        };
        
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Arial", Font.PLAIN, 13));
        tabla.setShowGrid(true);
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setIntercellSpacing(new Dimension(0, 0));
        
        JTableHeader headerTable = tabla.getTableHeader();
        headerTable.setBackground(new Color(52, 73, 94));
        headerTable.setForeground(Color.WHITE);
        headerTable.setFont(new Font("Arial", Font.BOLD, 13));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        tabla.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 0, 20, 0),
            BorderFactory.createLineBorder(new Color(220, 220, 220))
        ));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        RoundedPanel totalesPanel = new RoundedPanel(16);
        totalesPanel.setLayout(new BorderLayout());
        totalesPanel.setBackground(Color.WHITE);
        totalesPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JPanel totalesGrid = new JPanel(new GridLayout(3, 2, 10, 8));
        totalesGrid.setBackground(Color.WHITE);
        
        // Estilo para las etiquetas de totales
        JLabel subtotalLabel = new JLabel("SUBTOTAL:");
        subtotalLabel.setFont(new Font("Arial", Font.BOLD, 13));
        subtotalLabel.setForeground(new Color(70, 70, 70));
        
        JLabel ivaLabel = new JLabel("IVA (16%):");
        ivaLabel.setFont(new Font("Arial", Font.BOLD, 13));
        ivaLabel.setForeground(new Color(70, 70, 70));
        
        JLabel totalLabel = new JLabel("TOTAL A PAGAR:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setForeground(new Color(40, 40, 40));
        
        double subtotal = totalCalculado / 1.16; // Obtener el subtotal (sin IVA)
        double iva = totalCalculado - subtotal;  // Calcular el IVA
        
        lblSubtotal = textoMoneda(subtotal);
        lblSubtotal.setFont(new Font("Arial", Font.PLAIN, 13));
        
        lblImpuesto = textoMoneda(iva);
        lblImpuesto.setFont(new Font("Arial", Font.PLAIN, 13));
        
        lblTotal = textoMoneda(totalCalculado);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        
        totalesGrid.add(subtotalLabel);
        totalesGrid.add(lblSubtotal);
        totalesGrid.add(ivaLabel);
        totalesGrid.add(lblImpuesto);
        totalesGrid.add(totalLabel);
        totalesGrid.add(lblTotal);
        
        JPanel totalesContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalesContainer.setBackground(Color.WHITE);
        totalesContainer.add(totalesGrid);
        
        totalesPanel.add(totalesContainer, BorderLayout.EAST);
        
        JLabel gracias = new JLabel("¡Gracias por su compra!");
        gracias.setFont(new Font("Arial", Font.ITALIC, 13));
        gracias.setForeground(new Color(100, 100, 100));
        
        JPanel graciasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        graciasPanel.setBackground(Color.WHITE);
        graciasPanel.add(gracias);
        
        totalesPanel.add(graciasPanel, BorderLayout.WEST);
        
        mainPanel.add(totalesPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel, BorderLayout.CENTER);

        insertarFactura(fecha, cedulaCliente, metodoDePago, totalCalculado);
    }

    private int obtenerSiguienteNumeroFactura() {
        int siguiente = 1; // valor por defecto si la tabla está vacía
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT MAX(id_factura) AS max_id FROM facturas");
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                if (!rs.wasNull()) {
                    siguiente = maxId + 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return siguiente;
    }

    public void insertarFactura(String fecha, String cedula, String tipoPago, Double monto) {
        // Declarar las variables Connection y PreparedStatement
        Connection con_cant = null;
        PreparedStatement int_cant = null;
        
        try {
        Class.forName("org.sqlite.JDBC");
        con_cant = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");
        
        // Configurar para evitar bloqueos de base de datos con tiempos de espera más largos
        // IMPORTANTE: Configurar PRAGMA antes de cambiar autoCommit
        Statement stmt = con_cant.createStatement();
        stmt.execute("PRAGMA busy_timeout = 10000;"); // Aumentar el tiempo de espera a 10 segundos
        stmt.execute("PRAGMA journal_mode = WAL;"); // Write-Ahead Logging para mejor concurrencia
        stmt.execute("PRAGMA synchronous = NORMAL;"); // Reducir la sincronización con el disco
        stmt.execute("PRAGMA locking_mode = NORMAL;"); // Optimizar el manejo de bloqueos
        stmt.execute("PRAGMA cache_size = 5000;"); // Aumentar el tamaño de la caché
        stmt.close();
        
        con_cant.setAutoCommit(false); // Desactivar autocommit para manejar la transacción manualmente

        String sql = "INSERT INTO facturas(fec_factura, ced_factura, tipo_pago, monto_factura) VALUES(?, ?, ?, ?)";
        int_cant = con_cant.prepareStatement(sql);
    
        int_cant.setString(1, fecha);  // Fecha de la factura
        
        if (cedula == null || cedula.trim().isEmpty()) {
            int_cant.setNull(2, java.sql.Types.VARCHAR);
        } else {
            int_cant.setString(2, cedula);  // Cédula del cliente
        }
        
        int_cant.setString(3, tipoPago);  // Tipo de pago
        
        int_cant.setDouble(4, monto);
        
        int filasAfectadas = int_cant.executeUpdate();
        System.out.println("Filas afectadas: " + filasAfectadas);
        
        con_cant.commit();
    
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    }

    public void mostrar() { frame.setVisible(true); }

    public void agregarLinea(String descripcion, double precio, int cantidad, double total) {
        int item = modelo.getRowCount() + 1;
        modelo.addRow(new Object[]{item, descripcion, precio, cantidad, total});
    }

    public void recalcularTotales(double iva) {
        double subtotal = 0.0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            subtotal += ((Number) modelo.getValueAt(i, 4)).doubleValue();
        }
        double impuesto = subtotal * iva;
        double total = subtotal + impuesto;

        lblSubtotal.setText(moneda.format(subtotal));
        lblImpuesto.setText(moneda.format(impuesto));
        lblTotal.setText(moneda.format(total));

    }

    private JLabel textoMoneda(double v) {
        JLabel l = new JLabel(moneda.format(v));
        l.setFont(new Font("Arial", Font.PLAIN, 13));
        l.setForeground(new Color(40, 40, 40));
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }
    
}
