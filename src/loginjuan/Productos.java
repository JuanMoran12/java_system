package loginjuan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Módulo de Ventas (Facturación)
 * Maneja la selección de productos, carrito de compras y generación de
 * facturas.
 */
class Productos {
    // Colores profesionales (alineados con ClientesModulo)
    private final Color COLOR_FONDO = new Color(243, 246, 250);
    private final Color COLOR_PRIMARIO = new Color(52, 152, 219);
    private final Color COLOR_SECUNDARIO = new Color(46, 64, 83);
    private final Color COLOR_EXITO = new Color(46, 204, 113);
    private final Color COLOR_PELIGRO = new Color(231, 76, 60);
    private final Color COLOR_TEXTO = new Color(52, 73, 94);
    private final Color COLOR_BORDE = new Color(220, 220, 220);
    private final Color COLOR_FONDO_CAMPO = new Color(248, 249, 250);

    // Datos del cliente seleccionado
    private String var_ced;
    private String var_nom;
    private String rolUsuario;
    private String nombreUsuario;
    private int idUsuario;

    // UI Components
    private JTextField ent_searchName = new JTextField();
    private JTextField ent_cant = new JTextField("1");
    private DefaultTableModel modelo = new DefaultTableModel();
    private JTable tabla = new JTable(modelo);

    private DefaultTableModel modeloCarrito = new DefaultTableModel();
    private JTable tablaCarrito = new JTable(modeloCarrito);
    private JComboBox<String> comboMetodoPago = new JComboBox<>();
    private JLabel lblTotal = new JLabel("$0.00");
    private JPanel panelCentral = new JPanel();

    // Listas para manejar el carrito
    private ArrayList<String> listaIds = new ArrayList<>();
    private ArrayList<Double> listaPrecios = new ArrayList<>();
    private ArrayList<Integer> listaCantidades = new ArrayList<>();
    private ArrayList<String> listaNombres = new ArrayList<>();
    private ArrayList<Double> listaSubtotales = new ArrayList<>();

    // Variables de selección actual
    private String selectedId = "";
    private String selectedNombre = "";
    private String selectedPrecio = "";
    private String selectedStock = "";
    private JLabel lblSeleccionado = new JLabel("Seleccione un producto del catálogo");

    public Productos(String cedula_del_cliente, String nombre_del_cliente, String rol, String usuario, int idUsuario) {
        this.var_ced = (cedula_del_cliente == null || cedula_del_cliente.isEmpty()) ? "GENERAL" : cedula_del_cliente;
        this.var_nom = (nombre_del_cliente == null || nombre_del_cliente.isEmpty()) ? "Cliente General"
                : nombre_del_cliente;
        this.rolUsuario = rol;
        this.nombreUsuario = usuario;
        this.idUsuario = idUsuario;

        // Configurar modelos de tabla
        configurarTablas();
    }

    private void configurarTablas() {
        // Tabla de catálogo
        modelo.addColumn("ID");
        modelo.addColumn("Producto");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");

        // Tabla de carrito
        modeloCarrito.addColumn("Producto");
        modeloCarrito.addColumn("Cant.");
        modeloCarrito.addColumn("P. Unit.");
        modeloCarrito.addColumn("Subtotal");
    }

    public JPanel getEmbedPanel(int parentW, int parentH) {
        panelCentral.removeAll();
        panelCentral.setBounds(0, 0, parentW, parentH);
        panelCentral.setBackground(COLOR_FONDO);
        panelCentral.setLayout(null);

        // Tarjeta principal (proporcional al tamaño disponible)
        int cardMargin = 30;
        RoundedPanel card = new RoundedPanel(24);
        card.setBounds(cardMargin, cardMargin, parentW - (2 * cardMargin), parentH - (2 * cardMargin));
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(20, 25, 25, 25)));
        panelCentral.add(card);

        // --- ENCABEZADO: Título e Info del Cliente ---
        JLabel titleLabel = new JLabel("🛒 Punto de Venta");
        titleLabel.setBounds(25, 20, 300, 35);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(COLOR_SECUNDARIO);
        card.add(titleLabel);

        // Info del Cliente Seleccionado
        RoundedPanel clientInfoCard = new RoundedPanel(12);
        clientInfoCard.setBounds(card.getWidth() - 380, 15, 350, 50);
        clientInfoCard.setBackground(new Color(236, 254, 255));
        clientInfoCard.setBorder(BorderFactory.createLineBorder(new Color(165, 243, 252), 1));
        clientInfoCard.setLayout(null);
        card.add(clientInfoCard);

        JLabel lblClient = new JLabel("👤 " + var_nom);
        lblClient.setBounds(15, 5, 320, 20);
        lblClient.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblClient.setForeground(new Color(8, 145, 178));
        clientInfoCard.add(lblClient);

        JLabel lblCedula = new JLabel("Cédula: " + var_ced);
        lblCedula.setBounds(15, 25, 320, 15);
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCedula.setForeground(new Color(8, 145, 178));
        clientInfoCard.add(lblCedula);

        // --- BUSQUEDA Y CATALOGO (Izquierda) ---
        JLabel lblCatalogo = new JLabel("CATÁLOGO DE PRODUCTOS");
        lblCatalogo.setBounds(25, 80, 250, 20);
        lblCatalogo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCatalogo.setForeground(COLOR_TEXTO);
        card.add(lblCatalogo);

        ent_searchName.setBounds(25, 105, 310, 40);
        ent_searchName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ent_searchName.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        ent_searchName.setBackground(COLOR_FONDO_CAMPO);
        card.add(ent_searchName);

        RoundButton btnSearch = new RoundButton("🔍", 10);
        btnSearch.setBounds(340, 105, 50, 40);
        btnSearch.setBackground(COLOR_PRIMARIO);
        btnSearch.addActionListener(e -> buscarProductos());
        card.add(btnSearch);

        // Tabla de Productos
        JScrollPane scrollCatalogo = configurarScrollTabla(tabla);
        scrollCatalogo.setBounds(25, 155, 365, 340);
        card.add(scrollCatalogo);

        // Etiqueta de selección
        lblSeleccionado.setBounds(25, 500, 365, 25);
        lblSeleccionado.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSeleccionado.setForeground(COLOR_PRIMARIO);
        card.add(lblSeleccionado);

        // Controles de cantidad
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(25, 530, 70, 35);
        lblCantidad.setFont(new Font("Segoe UI", Font.BOLD, 13));
        card.add(lblCantidad);

        ent_cant.setBounds(95, 530, 60, 35);
        ent_cant.setHorizontalAlignment(SwingConstants.CENTER);
        ent_cant.setBorder(new LineBorder(COLOR_BORDE, 1, true));
        card.add(ent_cant);

        RoundButton btnAdd = new RoundButton("➕ Agregar al Carrito", 14);
        btnAdd.setBounds(165, 530, 225, 35);
        btnAdd.setBackground(COLOR_EXITO);
        btnAdd.addActionListener(e -> agregarAlCarrito());
        card.add(btnAdd);

        // --- CARRITO Y PAGO (Derecha) ---
        JLabel lblCarrito = new JLabel("CARRITO DE COMPRAS");
        lblCarrito.setBounds(500, 80, 250, 20);
        lblCarrito.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCarrito.setForeground(COLOR_TEXTO);
        card.add(lblCarrito);

        JScrollPane scrollCarrito = configurarScrollTabla(tablaCarrito);
        scrollCarrito.setBounds(500, 105, 470, 310);
        card.add(scrollCarrito);

        // Botones de control del carrito
        RoundButton btnQuitar = new RoundButton("🗑️ Quitar Item", 12);
        btnQuitar.setBounds(500, 425, 120, 30);
        btnQuitar.setBackground(COLOR_PELIGRO);
        btnQuitar.addActionListener(e -> eliminarDelCarrito());
        card.add(btnQuitar);

        // Panel de Totales
        RoundedPanel totalsCard = new RoundedPanel(12);
        totalsCard.setBounds(500, 465, 470, 110);
        totalsCard.setBackground(new Color(248, 250, 252));
        totalsCard.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        totalsCard.setLayout(null);
        card.add(totalsCard);

        JLabel lblPago = new JLabel("Método de Pago:");
        lblPago.setBounds(15, 15, 120, 25);
        lblPago.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalsCard.add(lblPago);

        comboMetodoPago.addItem("Efectivo");
        comboMetodoPago.addItem("Transferencia");
        comboMetodoPago.addItem("Tarjeta Débito");
        comboMetodoPago.addItem("Tarjeta Crédito");
        comboMetodoPago.setBounds(15, 45, 180, 30);
        totalsCard.add(comboMetodoPago);

        JLabel lblTotalLabel = new JLabel("TOTAL A PAGAR:");
        lblTotalLabel.setBounds(250, 20, 200, 20);
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalsCard.add(lblTotalLabel);

        lblTotal.setBounds(250, 45, 200, 40);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTotal.setForeground(COLOR_EXITO);
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        totalsCard.add(lblTotal);

        // Botón Finalizar Venta (Corregido centrado relativo)
        RoundButton btnVender = new RoundButton("💳 PROCESAR FACTURA", 18);
        btnVender.setBounds(500, 585, 470, 45);
        btnVender.setBackground(COLOR_SECUNDARIO);
        btnVender.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVender.addActionListener(e -> procesarVenta());
        card.add(btnVender);

        // Cargar datos iniciales
        cargarProductosIniciales();

        // Listener para selección de tabla mejorado
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabla.getSelectedRow();
                if (row != -1) {
                    selectedId = tabla.getValueAt(row, 0).toString();
                    selectedNombre = tabla.getValueAt(row, 1).toString();
                    selectedPrecio = tabla.getValueAt(row, 2).toString().replace("$", "").replace(",", "");
                    selectedStock = tabla.getValueAt(row, 3).toString();

                    lblSeleccionado
                            .setText("✓ " + selectedNombre + " ($" + selectedPrecio + ") | Stock: " + selectedStock);
                    lblSeleccionado.setForeground(COLOR_EXITO);
                }
            }
        });

        return panelCentral;
    }

    private JScrollPane configurarScrollTabla(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(30);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(COLOR_SECUNDARIO);
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionBackground(new Color(52, 152, 219, 40));
        t.setGridColor(new Color(241, 245, 249));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) {
            if (i != 1)
                t.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scroll = new JScrollPane(t);
        scroll.setBorder(new LineBorder(COLOR_BORDE, 1));
        return scroll;
    }

    private void resetSelection() {
        selectedId = "";
        selectedNombre = "";
        selectedPrecio = "";
        selectedStock = "";
        lblSeleccionado.setText("Seleccione un producto del catálogo");
        lblSeleccionado.setForeground(COLOR_PRIMARIO);
    }

    private void cargarProductosIniciales() {
        modelo.setRowCount(0);
        resetSelection();
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db")) {
            String sql = "SELECT id_prod, nom_prod, pre_prod, exi_prod FROM productos WHERE exi_prod > 0 ORDER BY nom_prod";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                modelo.addRow(new Object[] {
                        rs.getString(1),
                        rs.getString(2),
                        "$" + rs.getString(3),
                        rs.getString(4)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void buscarProductos() {
        String busqueda = ent_searchName.getText().trim();
        modelo.setRowCount(0);
        resetSelection();
        try (Connection con = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db")) {
            String sql = "SELECT id_prod, nom_prod, pre_prod, exi_prod FROM productos WHERE nom_prod LIKE ? AND exi_prod > 0 ORDER BY nom_prod";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "%" + busqueda + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[] {
                        rs.getString(1),
                        rs.getString(2),
                        "$" + rs.getString(3),
                        rs.getString(4)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void agregarAlCarrito() {
        if (selectedId == null || selectedId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto de la tabla antes de agregarlo.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cantidad = 0;
        int stock = 0;
        double precioValue = 0.0;

        // 1. Validar Cantidad ingresada
        try {
            String cantText = ent_cant.getText().trim();
            int entero = Integer.parseInt(cantText);
            if (cantText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese la cantidad deseada.");
                return;
            }
            cantidad = entero;
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a cero.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "La cantidad ingresada '" + ent_cant.getText() + "' no es un número válido.",
                    "Cantidad Inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validar Datos del Producto Seleccionado
        try {
            // Limpieza profunda de precio
            String cleanPrecio = selectedPrecio.replace("$", "").replace(",", "").trim();
            precioValue = Double.parseDouble(cleanPrecio);

            // CORRECCIÓN CRÍTICA: El stock puede venir como "28.0" desde la BD
            // Primero lo leemos como Double y luego lo convertimos a int
            String cleanStock = selectedStock.trim();
            double stockDouble = Double.parseDouble(cleanStock);
            stock = (int) stockDouble;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error interno con los datos del producto seleccionado.\n" +
                    "Precio: " + selectedPrecio + "\nStock: " + selectedStock
                    + "\n\nIntente seleccionar otro producto.",
                    "Error de Producto", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error parseando: Precio=" + selectedPrecio + ", Stock=" + selectedStock);
            return;
        }

        // 3. Validar Disponibilidad
        if (cantidad > stock) {
            JOptionPane.showMessageDialog(null, "No hay suficiente stock disponible (Máx: " + stock + ").",
                    "Stock insuficiente", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double subtotal = precioValue * cantidad;

            // Agregar a las listas internas
            listaIds.add(selectedId);
            listaNombres.add(selectedNombre);
            listaPrecios.add(precioValue);
            listaCantidades.add(cantidad);
            listaSubtotales.add(subtotal);

            // Actualizar tabla visual del carrito
            modeloCarrito.addRow(new Object[] {
                    selectedNombre,
                    cantidad,
                    String.format("$%.2f", precioValue),
                    String.format("$%.2f", subtotal)
            });

            actualizarTotal();
            ent_cant.setText("1");
            resetSelection();
            tabla.clearSelection();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar al carrito: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarDelCarrito() {
        int row = tablaCarrito.getSelectedRow();
        if (row != -1) {
            listaIds.remove(row);
            listaNombres.remove(row);
            listaPrecios.remove(row);
            listaCantidades.remove(row);
            listaSubtotales.remove(row);
            modeloCarrito.removeRow(row);
            actualizarTotal();
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un item del carrito para eliminarlo.");
        }
    }

    private void actualizarTotal() {
        double total = 0;
        for (double sub : listaSubtotales)
            total += sub;
        lblTotal.setText(String.format("$%.2f", total));
    }

    private void procesarVenta() {
        if (listaSubtotales.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El carrito está vacío. Agregue productos antes de facturar.",
                    "Venta vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Desea procesar la venta por un total de " + lblTotal.getText() + "?",
                "Confirmar Venta", JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION)
            return;

        // --- 1. ACTUALIZAR STOCK EN BASE DE DATOS (Transacción) ---
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");
            con.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar en FACTURAS
            String sqlFactura = "INSERT INTO facturas (fec_factura, ced_factura, tipo_pago, monto_factura) VALUES (datetime('now', 'localtime'), ?, ?, ?)";
            PreparedStatement pstmtFactura = con.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);

            int cedulaInt = 0;
            try {
                cedulaInt = Integer.parseInt(var_ced);
            } catch (Exception e) {
                cedulaInt = 0;
            }

            pstmtFactura.setInt(1, cedulaInt);
            pstmtFactura.setString(2, (String) comboMetodoPago.getSelectedItem());
            pstmtFactura.setDouble(3, Double.parseDouble(lblTotal.getText().replace("$", "").replace(",", "")));
            pstmtFactura.executeUpdate();

            // Obtener ID (id_factura)
            ResultSet rsKeys = pstmtFactura.getGeneratedKeys();
            int idFactura = 0;
            if (rsKeys.next()) {
                idFactura = rsKeys.getInt(1);
            }

            // 2. Insertar Detalle en CARRITO y Actualizar STOCK
            String sqlDetalle = "INSERT INTO carrito (car_factura, car_pro, car_can, car_ced) VALUES (?, ?, ?, ?)";
            String sqlStock = "UPDATE productos SET exi_prod = exi_prod - ? WHERE id_prod = ?";

            PreparedStatement pstmtDetalle = con.prepareStatement(sqlDetalle);
            PreparedStatement pstmtStock = con.prepareStatement(sqlStock);

            for (int i = 0; i < listaIds.size(); i++) {
                // Detalle
                pstmtDetalle.setInt(1, idFactura);
                pstmtDetalle.setInt(2, Integer.parseInt(listaIds.get(i)));
                pstmtDetalle.setInt(3, listaCantidades.get(i));
                pstmtDetalle.setInt(4, cedulaInt);
                pstmtDetalle.executeUpdate();

                // Stock
                pstmtStock.setInt(1, listaCantidades.get(i));
                pstmtStock.setString(2, listaIds.get(i));
                pstmtStock.executeUpdate();
            }

            con.commit(); // Todo bien, guardar cambios
            System.out.println("Inventario actualizado correctamente.");

            // Auditoría: Registrar la transacción
            AuditLogger.registrar(idUsuario, "Ventas", "TRANSACCION");

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Error, revertir cambios
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(null, "Error crítico al actualizar el stock: " + e.getMessage(),
                    "Error de Inventario", JOptionPane.ERROR_MESSAGE);
            return; // No proceder con la factura si el stock falló
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // --- 2. GENERAR FACTURA VISUAL ---
        double totalFinal = 0;
        for (double sub : listaSubtotales)
            totalFinal += sub;

        String metodo = (String) comboMetodoPago.getSelectedItem();
        Factura fac = new Factura(var_ced, totalFinal, var_nom, metodo);
        for (int i = 0; i < listaNombres.size(); i++) {
            fac.agregarLinea(listaNombres.get(i), listaPrecios.get(i), listaCantidades.get(i), listaSubtotales.get(i));
        }
        fac.mostrar();

        // Limpiar todo después de la venta exitosa
        limpiarVenta();
        JOptionPane.showMessageDialog(null, "Venta procesada e inventario actualizado con éxito.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarVenta() {
        listaIds.clear();
        listaNombres.clear();
        listaPrecios.clear();
        listaCantidades.clear();
        listaSubtotales.clear();
        modeloCarrito.setRowCount(0);
        actualizarTotal();
        cargarProductosIniciales();
        resetSelection();
    }
}
