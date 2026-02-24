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
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Factura {

    // Paleta unificada con Login y Opciones (estética de la app)
    private static final Color COLOR_FONDO_PRINCIPAL = new Color(248, 250, 252);
    private static final Color COLOR_HEADER_TOP = new Color(15, 23, 42);
    private static final Color COLOR_HEADER_BOTTOM = new Color(2, 6, 23);
    private static final Color COLOR_TEXTO_PRINCIPAL = new Color(15, 23, 42);
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(71, 85, 105);
    private static final Color COLOR_ACCENT = new Color(29, 78, 216);
    private static final Color COLOR_BORDE = new Color(226, 232, 240);
    private static final String FONT_NAME = "Segoe UI";

    private final JFrame frame;
    private final JLabel lblSubtotal;
    private final JLabel lblImpuesto;
    private final JLabel lblTotal;
    private final DefaultTableModel modelo;
    private final JPanel printableContent;

    private final NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "VE"));
    private static final AtomicInteger SECUENCIA = new AtomicInteger(2540); // autoincremento simple

    public Factura(String cedulaCliente, double totalCalculado, String nombreCliente, String metodoDePago) {
        frame = new JFrame("Factura");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(COLOR_FONDO_PRINCIPAL);

        // Cabecera con gradiente y logo (misma estética que Login y Opciones)
        GradientPanel header = new GradientPanel(COLOR_HEADER_TOP, COLOR_HEADER_BOTTOM);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel headerWest = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerWest.setOpaque(false);
        ImageIcon logoIcon = new ImageIcon("images/logo_w.png");
        if (logoIcon.getImageLoadStatus() != java.awt.MediaTracker.COMPLETE) {
            logoIcon = new ImageIcon("logo_w.png"); // Fallback
        }
        Image logoImg = logoIcon.getImage();
        if (logoImg != null) {
            Image scaledLogo = logoImg.getScaledInstance(56, 56, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            headerWest.add(logoLabel);
        }
        JPanel tituloPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        tituloPanel.setOpaque(false);
        JLabel titulo = new JLabel("Sistema de Gestión");
        titulo.setFont(new Font(FONT_NAME, Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        JLabel subtitulo = new JLabel("Factura de Venta");
        subtitulo.setFont(new Font(FONT_NAME, Font.PLAIN, 14));
        subtitulo.setForeground(new Color(148, 163, 184));
        tituloPanel.add(titulo);
        tituloPanel.add(subtitulo);
        headerWest.add(tituloPanel);
        header.add(headerWest, BorderLayout.WEST);

        int numeroFactura = obtenerSiguienteNumeroFactura();
        JLabel numFactura = new JLabel("Factura #" + numeroFactura);
        numFactura.setFont(new Font(FONT_NAME, Font.BOLD, 16));
        numFactura.setForeground(Color.WHITE);
        header.add(numFactura, BorderLayout.EAST);

        frame.add(header, BorderLayout.NORTH);

        // --- Contenido principal ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_FONDO_PRINCIPAL);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        printableContent = mainPanel;

        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        infoPanel.setBackground(COLOR_FONDO_PRINCIPAL);

        // Panel de información del cliente
        RoundedPanel clientePanel = new RoundedPanel(16);
        clientePanel.setLayout(new BorderLayout());
        clientePanel.setBackground(Color.WHITE);
        clientePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel clienteTitulo = new JLabel("INFORMACIÓN DEL CLIENTE");
        clienteTitulo.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        clienteTitulo.setForeground(COLOR_TEXTO_SECUNDARIO);
        clienteTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JPanel clienteInfo = new JPanel(new GridLayout(2, 1, 5, 5));
        clienteInfo.setBackground(Color.WHITE);
        
        JLabel nombreLabel = new JLabel("Nombre: " + nombreCliente);
        nombreLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 13));
        nombreLabel.setForeground(COLOR_TEXTO_PRINCIPAL);
        
        JLabel cedulaLabel = new JLabel("Cédula: " + (cedulaCliente == null ? "—" : cedulaCliente));
        cedulaLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 13));
        cedulaLabel.setForeground(COLOR_TEXTO_PRINCIPAL);
        
        clienteInfo.add(nombreLabel);
        clienteInfo.add(cedulaLabel);
        
        clientePanel.add(clienteTitulo, BorderLayout.NORTH);
        clientePanel.add(clienteInfo, BorderLayout.CENTER);

        // Panel de información de factura
        RoundedPanel facturaPanel = new RoundedPanel(16);
        facturaPanel.setLayout(new BorderLayout());
        facturaPanel.setBackground(Color.WHITE);
        facturaPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel facturaTitulo = new JLabel("INFORMACIÓN DE FACTURA");
        facturaTitulo.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        facturaTitulo.setForeground(COLOR_TEXTO_SECUNDARIO);
        facturaTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JPanel facturaInfo = new JPanel(new GridLayout(2, 1, 5, 5));
        facturaInfo.setBackground(Color.WHITE);
        
        String fecha = LocalDate.now().toString();
        JLabel fechaLabel = new JLabel("Fecha: " + fecha);
        fechaLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 13));
        fechaLabel.setForeground(COLOR_TEXTO_PRINCIPAL);
        
        JLabel metodoPagoLabel = new JLabel("Método de pago: " + metodoDePago);
        metodoPagoLabel.setFont(new Font(FONT_NAME, Font.PLAIN, 13));
        metodoPagoLabel.setForeground(COLOR_TEXTO_PRINCIPAL);
        
        facturaInfo.add(fechaLabel);
        facturaInfo.add(metodoPagoLabel);
        
        facturaPanel.add(facturaTitulo, BorderLayout.NORTH);
        facturaPanel.add(facturaInfo, BorderLayout.CENTER);

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
        tabla.setFont(new Font(FONT_NAME, Font.PLAIN, 13));
        tabla.setForeground(COLOR_TEXTO_PRINCIPAL);
        tabla.setShowGrid(true);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        
        JTableHeader headerTable = tabla.getTableHeader();
        headerTable.setBackground(COLOR_HEADER_TOP);
        headerTable.setForeground(Color.WHITE);
        headerTable.setFont(new Font(FONT_NAME, Font.BOLD, 13));
        
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
            BorderFactory.createLineBorder(COLOR_BORDE)
        ));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        RoundedPanel totalesPanel = new RoundedPanel(16);
        totalesPanel.setLayout(new BorderLayout());
        totalesPanel.setBackground(Color.WHITE);
        totalesPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JPanel totalesGrid = new JPanel(new GridLayout(3, 2, 10, 8));
        totalesGrid.setBackground(Color.WHITE);
        
        JLabel subtotalLabel = new JLabel("SUBTOTAL:");
        subtotalLabel.setFont(new Font(FONT_NAME, Font.BOLD, 13));
        subtotalLabel.setForeground(COLOR_TEXTO_SECUNDARIO);
        
        JLabel ivaLabel = new JLabel("IVA (16%):");
        ivaLabel.setFont(new Font(FONT_NAME, Font.BOLD, 13));
        ivaLabel.setForeground(COLOR_TEXTO_SECUNDARIO);
        
        JLabel totalLabel = new JLabel("TOTAL A PAGAR:");
        totalLabel.setFont(new Font(FONT_NAME, Font.BOLD, 14));
        totalLabel.setForeground(COLOR_TEXTO_PRINCIPAL);
        
        double subtotal = totalCalculado / 1.16;
        double iva = totalCalculado - subtotal;
        
        lblSubtotal = textoMoneda(subtotal);
        lblSubtotal.setFont(new Font(FONT_NAME, Font.PLAIN, 13));
        
        lblImpuesto = textoMoneda(iva);
        lblImpuesto.setFont(new Font(FONT_NAME, Font.PLAIN, 13));
        
        lblTotal = textoMoneda(totalCalculado);
        lblTotal.setFont(new Font(FONT_NAME, Font.BOLD, 14));
        
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
        gracias.setFont(new Font(FONT_NAME, Font.ITALIC, 13));
        gracias.setForeground(COLOR_TEXTO_SECUNDARIO);
        
        JPanel graciasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        graciasPanel.setBackground(Color.WHITE);
        graciasPanel.add(gracias);
        
        totalesPanel.add(graciasPanel, BorderLayout.WEST);
        
        mainPanel.add(totalesPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel actionsBar = new JPanel(new BorderLayout());
        actionsBar.setBackground(Color.WHITE);
        actionsBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDE),
            new EmptyBorder(10, 30, 10, 30)
        ));

        JLabel nota = new JLabel("Documento no válido como crédito fiscal");
        nota.setFont(new Font(FONT_NAME, Font.PLAIN, 12));
        nota.setForeground(COLOR_TEXTO_SECUNDARIO);
        actionsBar.add(nota, BorderLayout.WEST);

        JPanel actionsRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsRight.setOpaque(false);

        JButton btnImprimir = new JButton("Imprimir");
        btnImprimir.setFont(new Font(FONT_NAME, Font.BOLD, 13));
        btnImprimir.setBackground(COLOR_ACCENT);
        btnImprimir.setForeground(Color.WHITE);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnImprimir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnImprimir.addActionListener(e -> imprimirFactura());

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font(FONT_NAME, Font.BOLD, 13));
        btnCerrar.setBackground(new Color(241, 245, 249));
        btnCerrar.setForeground(COLOR_TEXTO_PRINCIPAL);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> frame.dispose());

        actionsRight.add(btnCerrar);
        actionsRight.add(btnImprimir);
        actionsBar.add(actionsRight, BorderLayout.EAST);

        frame.add(actionsBar, BorderLayout.SOUTH);

        insertarFactura(fecha, cedulaCliente, metodoDePago, totalCalculado);
    }

    private void imprimirFactura() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Factura");

        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2 = (Graphics2D) graphics;
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            double scaleX = pageFormat.getImageableWidth() / printableContent.getWidth();
            double scaleY = pageFormat.getImageableHeight() / printableContent.getHeight();
            double scale = Math.min(scaleX, scaleY);

            if (scale < 1d) {
                g2.scale(scale, scale);
            }

            printableContent.printAll(g2);
            return Printable.PAGE_EXISTS;
        });

        try {
            if (job.printDialog()) {
                job.print();
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(frame, "No se pudo imprimir: " + ex.getMessage(), "Impresión", JOptionPane.ERROR_MESSAGE);
        }
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

        // Obtener la tasa del dólar actual
        String tasaDolarStr = DolarAPIClient.obtenerPrecioDolar();
        double tasaDolar = 0.0;
        
        // Intentar parsear la tasa, si falla usar un valor por defecto
        try {
            tasaDolar = Double.parseDouble(tasaDolarStr);
        } catch (NumberFormatException e) {
            System.err.println("Error al obtener tasa del dólar, usando valor por defecto 50.0");
            tasaDolar = 50.0; // Valor por defecto si la API falla
        }
        
        // Calcular el monto en bolívares
        double montoBolivares = monto * tasaDolar;

        String sql = "INSERT INTO facturas(fec_factura, ced_factura, tipo_pago, monto_factura, tasa_dolar, monto_bolivares) VALUES(?, ?, ?, ?, ?, ?)";
        int_cant = con_cant.prepareStatement(sql);
    
        int_cant.setString(1, fecha);  // Fecha de la factura
        
        if (cedula == null || cedula.trim().isEmpty()) {
            int_cant.setNull(2, java.sql.Types.VARCHAR);
        } else {
            int_cant.setString(2, cedula);  // Cédula del cliente
        }
        
        int_cant.setString(3, tipoPago);  // Tipo de pago
        int_cant.setDouble(4, monto);  // Monto en dólares
        int_cant.setDouble(5, tasaDolar);  // Tasa del dólar al momento de la venta
        int_cant.setDouble(6, montoBolivares);  // Monto en bolívares
        
        int filasAfectadas = int_cant.executeUpdate();
        System.out.println("Filas afectadas: " + filasAfectadas);
        System.out.println("Factura guardada - Monto: $" + monto + " | Tasa: " + tasaDolar + " Bs/$ | Total Bs: " + montoBolivares);
        
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
        l.setFont(new Font(FONT_NAME, Font.PLAIN, 13));
        l.setForeground(COLOR_TEXTO_PRINCIPAL);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }
    
}
