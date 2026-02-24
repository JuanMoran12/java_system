package loginjuan;


import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;


public class Compras {

    private JTable tablaDetalle;
    private DefaultTableModel modeloDetalle;
    private JTable tablaHistorial;
    private DefaultTableModel modeloHistorial;
    private JComboBox<String> comboProveedores;
    private JComboBox<String> comboProductos;
    private JTextField txtCantidad;
    private JTextField txtCostoUnitario;
    private JLabel lblTotal;

    private ArrayList<DetalleCompraItem> itemsCompra = new ArrayList<>();
    private double totalCompra = 0.0;
    private java.util.HashMap<Integer, Double> mapPrecios = new java.util.HashMap<>();

    // Colores profesionales
    private final Color COLOR_FONDO = new Color(243, 246, 250);
    private final Color COLOR_PRIMARIO = new Color(52, 152, 219);
    private final Color COLOR_SECUNDARIO = new Color(46, 64, 83);
    private final Color COLOR_ACCION = new Color(52, 152, 219);
    private final Color COLOR_ACCION_HOVER = new Color(41, 128, 185);
    private final Color COLOR_PELIGRO = new Color(231, 76, 60);
    private final Color COLOR_EXITO = new Color(46, 204, 113);
    private final Color COLOR_TEXTO = new Color(52, 73, 94);
    private final Color COLOR_BORDE = new Color(220, 220, 220);
    private final Color COLOR_FONDO_CAMPO = new Color(248, 249, 250);

    // Clase interna para manejar items de compra
    private class DetalleCompraItem {
        int idProducto;
        String nombreProducto;
        int cantidad;
        double costoUnitario;
        double subtotal;

        DetalleCompraItem(int idProducto, String nombreProducto, int cantidad, double costoUnitario) {
            this.idProducto = idProducto;
            this.nombreProducto = nombreProducto;
            this.cantidad = cantidad;
            this.costoUnitario = costoUnitario;
            this.subtotal = cantidad * costoUnitario;
        }
    }

    public JPanel getComprasPanel(int parentW, int parentH) {
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(0, 0, parentW, parentH);
        mainPanel.setBackground(COLOR_FONDO);
        mainPanel.setLayout(null);
        // Tarjeta principal

        int cardMargin = 30;
        RoundedPanel card = new RoundedPanel(24);
        int cardW = parentW - (2 * cardMargin);
        int cardH = parentH - (2 * cardMargin);
        card.setBounds(cardMargin, cardMargin, cardW, cardH);
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(25, 35, 35, 35)));

        mainPanel.add(card);

        int internalPaddingX = 30;
        int currentY = 20;

        JLabel titulo = new JLabel("🚚 Compras y Entrada de Mercancía");
        titulo.setBounds(internalPaddingX, currentY, 500, 40);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(COLOR_TEXTO);
        card.add(titulo);


        RoundButton btnHistorial = new RoundButton("📋 Historial", 18);
        btnHistorial.setBounds(cardW - internalPaddingX - 150, currentY + 5, 150, 35);
        btnHistorial.setBackground(COLOR_SECUNDARIO);
        btnHistorial.setForeground(Color.WHITE);
        btnHistorial.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btnHistorial.setBorderPainted(false);
        btnHistorial.setFocusPainted(false);
        btnHistorial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHistorial.addActionListener(e -> mostrarHistorial());
        card.add(btnHistorial);

        currentY += 60;
        // Panel de información
        RoundedPanel infoPanel = new RoundedPanel(12);
        infoPanel.setBounds(internalPaddingX, currentY, cardW - (2 * internalPaddingX), 60);
        infoPanel.setBackground(new Color(219, 234, 254));
        infoPanel.setBorder(BorderFactory.createLineBorder(new Color(59, 130, 246), 2));
        infoPanel.setLayout(null);

        card.add(infoPanel);

        JLabel infoIcon = new JLabel("ℹ️");
        infoIcon.setBounds(15, 10, 40, 40);
        infoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        infoPanel.add(infoIcon);

        JLabel infoText = new JLabel("Registre las facturas de compra para actualizar automáticamente el inventario");
        infoText.setBounds(60, 20, 700, 20);
        infoText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoText.setForeground(new Color(30, 64, 175));
        infoPanel.add(infoText);

        currentY += 75;

        // Sección: Datos de la compra
        JLabel lblDatos = new JLabel("DATOS DE LA COMPRA");
        lblDatos.setBounds(internalPaddingX, currentY, 300, 25);
        lblDatos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDatos.setForeground(COLOR_SECUNDARIO);
        card.add(lblDatos);

        currentY += 30;

        // Proveedor
        JLabel lblProveedor = new JLabel("Proveedor:");
        lblProveedor.setBounds(internalPaddingX, currentY, 120, 30);
        lblProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(lblProveedor);

        comboProveedores = new JComboBox<>();
        comboProveedores.setBounds(internalPaddingX + 130, currentY, 350, 35);
        comboProveedores.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(comboProveedores);

        cargarProveedores();

        // Fecha
        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(internalPaddingX + 500, currentY, 80, 30);
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        card.add(lblFecha);

        JLabel lblFechaValor = new JLabel(LocalDate.now().toString());
        lblFechaValor.setBounds(internalPaddingX + 560, currentY, 150, 30);
        lblFechaValor.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFechaValor.setForeground(COLOR_SECUNDARIO);

        card.add(lblFechaValor);

        currentY += 50;
        // Sección: Agregar productos
        JLabel lblAgregar = new JLabel("AGREGAR PRODUCTOS A LA COMPRA");
        lblAgregar.setBounds(internalPaddingX, currentY, 400, 25);
        lblAgregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAgregar.setForeground(COLOR_SECUNDARIO);

        card.add(lblAgregar);

        currentY += 30;

        // Panel de entrada de productos
        RoundedPanel addPanel = new RoundedPanel(12);
        addPanel.setBounds(internalPaddingX, currentY, cardW - (2 * internalPaddingX), 70);
        addPanel.setBackground(new Color(249, 250, 251));
        addPanel.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        addPanel.setLayout(null);

        card.add(addPanel);

        JLabel lblProducto = new JLabel("Producto:");
        lblProducto.setBounds(15, 8, 80, 25);
        lblProducto.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        addPanel.add(lblProducto);

        comboProductos = new JComboBox<>();
        comboProductos.setBounds(15, 33, 280, 30);
        comboProductos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addPanel.add(comboProductos);

        cargarProductos();

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(310, 8, 80, 25);
        lblCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addPanel.add(lblCantidad);

        txtCantidad = new JTextField();
        txtCantidad.setBounds(310, 33, 100, 30);
        txtCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCantidad.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        addPanel.add(txtCantidad);

        JLabel lblCosto = new JLabel("Costo Unit.:");
        lblCosto.setBounds(425, 8, 80, 25);
        lblCosto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addPanel.add(lblCosto);

        txtCostoUnitario = new JTextField();
        txtCostoUnitario.setBounds(425, 33, 120, 30);
        txtCostoUnitario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCostoUnitario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        addPanel.add(txtCostoUnitario);

        RoundButton btnAgregar = new RoundButton("➕ Agregar", 16);
        btnAgregar.setBounds(560, 28, 120, 35);
        btnAgregar.setBackground(COLOR_EXITO);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnAgregar.setBorderPainted(false);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregar.addActionListener(e -> agregarItem());
        addPanel.add(btnAgregar);

        currentY += 85;
        // Tabla de detalle de compra
        JLabel lblDetalle = new JLabel("DETALLE DE LA COMPRA");
        lblDetalle.setBounds(internalPaddingX, currentY, 300, 25);
        lblDetalle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDetalle.setForeground(COLOR_SECUNDARIO);
        card.add(lblDetalle);

        currentY += 30;

        modeloDetalle = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloDetalle.addColumn("ID Producto");
        modeloDetalle.addColumn("Nombre Producto");
        modeloDetalle.addColumn("Cantidad");
        modeloDetalle.addColumn("Costo Unitario");
        modeloDetalle.addColumn("Subtotal");

        tablaDetalle = new JTable(modeloDetalle);
        tablaDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaDetalle.setRowHeight(30);
        tablaDetalle.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaDetalle.getTableHeader().setBackground(COLOR_SECUNDARIO);
        tablaDetalle.getTableHeader().setForeground(Color.WHITE);
        tablaDetalle.setGridColor(new Color(230, 230, 230));
        tablaDetalle.setSelectionBackground(new Color(52, 152, 219, 50));

        JScrollPane scrollDetalle = new JScrollPane(tablaDetalle);
        scrollDetalle.setBounds(internalPaddingX, currentY,
                cardW - (2 * internalPaddingX), 200);
        scrollDetalle.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));

        card.add(scrollDetalle);

        currentY += 210;

        // Panel de totales y acciones
        RoundedPanel actionPanel = new RoundedPanel(12);
        actionPanel.setBounds(internalPaddingX, currentY,
                cardW - (2 * internalPaddingX), 60);
        actionPanel.setBackground(new Color(241, 245, 249));
        actionPanel.setLayout(null);

        card.add(actionPanel);

        RoundButton btnEliminar = new RoundButton("🗑️ Quitar Item", 16);
        btnEliminar.setBounds(15, 12, 150, 36);
        btnEliminar.setBackground(COLOR_PELIGRO);
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnEliminar.setBorderPainted(false);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminar.addActionListener(e -> quitarItem());

        actionPanel.add(btnEliminar);

        RoundButton btnLimpiar = new RoundButton("🔄 Nueva Compra", 16);
        btnLimpiar.setBounds(180, 12, 150, 36);
        btnLimpiar.setBackground(COLOR_SECUNDARIO);
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(e -> limpiarCompra());
        actionPanel.add(btnLimpiar);

        JLabel lblTotalLabel = new JLabel("TOTAL DE LA COMPRA:");
        int actionPanelW = cardW - (2 * internalPaddingX);
        lblTotalLabel.setBounds(actionPanelW - 420, 15, 200, 30);
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalLabel.setForeground(COLOR_TEXTO);
        lblTotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        actionPanel.add(lblTotalLabel);
        lblTotal = new JLabel("$0.00");
        lblTotal.setBounds(actionPanelW - 220, 15, 120, 30);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(COLOR_SECUNDARIO);
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        actionPanel.add(lblTotal);

        RoundButton btnGuardar = new RoundButton("💾 Guardar Compra", 18);
        btnGuardar.setBounds(actionPanelW - 180, 10, 165, 40);
        btnGuardar.setBackground(COLOR_EXITO);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(new Color(39, 174, 96));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(COLOR_EXITO);
            }
        });

        btnGuardar.addActionListener(e -> guardarCompra());
        actionPanel.add(btnGuardar);

        return mainPanel;
    }

    private void cargarProveedores() {

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id, nom_proveedor FROM proveedores ORDER BY nom_proveedor");

            while (rs.next()) {
                model.addElement(rs.getInt("id") + " - " + rs.getString("nom_proveedor"));
            }

            comboProveedores.setModel(model);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void cargarProductos() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        mapPrecios.clear(); // Limpiar mapa anterior
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");
            stmt = conn.createStatement();
            // Se incluye pre_prod para obtener el precio/costo base
            rs = stmt.executeQuery("SELECT id_prod, nom_prod, pre_prod FROM productos ORDER BY nom_prod");

            while (rs.next()) {
                int id = rs.getInt("id_prod");
                String nombre = rs.getString("nom_prod");
                double precio = rs.getDouble("pre_prod");

                model.addElement(id + " - " + nombre);
                mapPrecios.put(id, precio);
            }

            comboProductos.setModel(model);

            for (java.awt.event.ActionListener al : comboProductos.getActionListeners()) {
                comboProductos.removeActionListener(al);
            }
            comboProductos.addActionListener(e -> actualizarCosto());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void actualizarCosto() {
        if (comboProductos.getSelectedItem() != null) {
            String seleccion = comboProductos.getSelectedItem().toString();
            try {
                int id = Integer.parseInt(seleccion.split(" - ")[0]);
                if (mapPrecios.containsKey(id)) {
                    double precio = mapPrecios.get(id);
                    txtCostoUnitario.setText(String.valueOf(precio));
                }
            } catch (Exception e) {
                // Ignorar errores de parseo si la selección no es válida
            }
        }
    }

    private void agregarItem() {
        try {
            if (comboProductos.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Por favor seleccione un producto",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String productoSeleccionado = comboProductos.getSelectedItem().toString();
            int idProducto = Integer.parseInt(productoSeleccionado.split(" - ")[0]);
            String nombreProducto = productoSeleccionado.substring(productoSeleccionado.indexOf(" - ") + 3);

            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double costoUnitario = Double.parseDouble(txtCostoUnitario.getText().trim());

            if (cantidad <= 0 || costoUnitario <= 0) {
                JOptionPane.showMessageDialog(null, "Cantidad y costo deben ser mayores a cero",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DetalleCompraItem item = new DetalleCompraItem(idProducto, nombreProducto, cantidad, costoUnitario);
            itemsCompra.add(item);

            Object[] fila = {
                    item.idProducto,
                    item.nombreProducto,
                    item.cantidad,

                    String.format("$%.2f", item.costoUnitario),
                    String.format("$%.2f", item.subtotal)
            };

            modeloDetalle.addRow(fila);

            calcularTotal();

            txtCantidad.setText("");
            txtCostoUnitario.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese valores numéricos válidos",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void quitarItem() {
        int fila = tablaDetalle.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione un item para quitar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        itemsCompra.remove(fila);
        modeloDetalle.removeRow(fila);
        calcularTotal();
    }

    private void calcularTotal() {
        totalCompra = 0.0;
        for (DetalleCompraItem item : itemsCompra) {
            totalCompra += item.subtotal;
        }
        lblTotal.setText(String.format("$%.2f", totalCompra));
    }

    private void limpiarCompra() {
        itemsCompra.clear();
        modeloDetalle.setRowCount(0);
        totalCompra = 0.0;
        lblTotal.setText("$0.00");
        txtCantidad.setText("");
        txtCostoUnitario.setText("");
    }

    private void guardarCompra() {
        if (comboProveedores.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione un proveedor",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (itemsCompra.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor agregue al menos un producto a la compra",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Desea guardar esta compra?\nSe actualizará el stock de " + itemsCompra.size()
                        + " producto(s)\nTotal: $" + String.format("%.2f", totalCompra),
                "Confirmar Compra",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        Connection conn = null;
        PreparedStatement pstmtCompra = null;
        PreparedStatement pstmtDetalle = null;
        PreparedStatement pstmtStock = null;
        ResultSet rs = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");
            conn.setAutoCommit(false);

            String proveedorSeleccionado = comboProveedores.getSelectedItem().toString();
            int idProveedor = Integer.parseInt(proveedorSeleccionado.split(" - ")[0]);
            String fechaCompra = LocalDate.now().toString();

            String sqlCompra = "INSERT INTO compras (id_proveedor, fecha_compra, total_compra) VALUES (?, ?, ?)";
            pstmtCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
            pstmtCompra.setInt(1, idProveedor);
            pstmtCompra.setString(2, fechaCompra);
            pstmtCompra.setDouble(3, totalCompra);
            pstmtCompra.executeUpdate();

            // Obtener el ID de la compra generado
            rs = pstmtCompra.getGeneratedKeys();
            int idCompra = 0;
            if (rs.next()) {
                idCompra = rs.getInt(1);
            }

            // 2. Insertar detalles y actualizar stock
            String sqlDetalle = "INSERT INTO detalle_compras (id_compra, id_producto, cantidad, costo_unitario) VALUES (?, ?, ?, ?)";
            String sqlStock = "UPDATE productos SET exi_prod = exi_prod + ? WHERE id_prod = ?";

            pstmtDetalle = conn.prepareStatement(sqlDetalle);
            pstmtStock = conn.prepareStatement(sqlStock);

            for (DetalleCompraItem item : itemsCompra) {
                pstmtDetalle.setInt(1, idCompra);
                pstmtDetalle.setInt(2, item.idProducto);
                pstmtDetalle.setInt(3, item.cantidad);
                pstmtDetalle.setDouble(4, item.costoUnitario);
                pstmtDetalle.executeUpdate();

                // Actualizar stock
                pstmtStock.setInt(1, item.cantidad);
                pstmtStock.setInt(2, item.idProducto);
                pstmtStock.executeUpdate();
            }

            conn.commit();

            JOptionPane.showMessageDialog(null,
                    "✅ Compra guardada exitosamente!\n\n" +
                            "Compra ID: " + idCompra + "\n" +
                            "Productos actualizados: " + itemsCompra.size() + "\n" +
                            "Total: $" + String.format("%.2f", totalCompra),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCompra();

        } catch (Exception ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }

            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar la compra: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmtCompra != null)
                    pstmtCompra.close();
                if (pstmtDetalle != null)
                    pstmtDetalle.close();
                if (pstmtStock != null)
                    pstmtStock.close();
                if (conn != null)
                    conn.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void mostrarHistorial() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new java.awt.Dimension(900, 500));

        JLabel titulo = new JLabel("Historial de Compras");
        titulo.setBounds(20, 10, 400, 30);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panel.add(titulo);

        modeloHistorial = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloHistorial.addColumn("ID Compra");
        modeloHistorial.addColumn("Fecha");
        modeloHistorial.addColumn("Proveedor");
        modeloHistorial.addColumn("Total");

        tablaHistorial = new JTable(modeloHistorial);
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaHistorial.setRowHeight(28);
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaHistorial.getTableHeader().setBackground(COLOR_SECUNDARIO);
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setBounds(20, 50, 860, 420);
        panel.add(scroll);

        cargarHistorial();
        JOptionPane.showMessageDialog(null, panel, "Historial de Compras", JOptionPane.PLAIN_MESSAGE);
    }

    private void cargarHistorial() {
        modeloHistorial.setRowCount(0);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");
            stmt = conn.createStatement();

            String sql = "SELECT c.id_compra, c.fecha_compra, p.nom_proveedor, c.total_compra " +
                    "FROM compras c " +
                    "INNER JOIN proveedores p ON c.id_proveedor = p.id " +
                    "ORDER BY c.fecha_compra DESC, c.id_compra DESC";

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = {
                        rs.getInt("id_compra"),
                        rs.getString("fecha_compra"),
                        rs.getString("nom_proveedor"),
                        String.format("$%.2f", rs.getDouble("total_compra"))
                };

                modeloHistorial.addRow(fila);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}

