package loginjuan;



import java.awt.Color;

import java.awt.Component;

import java.awt.Font;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;

import javax.swing.BorderFactory;

import javax.swing.JButton;

import javax.swing.JLabel;

import javax.swing.JOptionPane;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JTable;

import javax.swing.JTextField;

import javax.swing.SwingConstants;

import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.table.DefaultTableModel;

import javax.swing.border.LineBorder;



public class ProductosCatalogo {



    private JTable tabla;

    private DefaultTableModel modelo;

    private String rolUsuario;

    private String nombreUsuario;

    private int idUsuario;



    // Colores profesionales

    private final Color COLOR_FONDO = new Color(243, 246, 250);

    private final Color COLOR_PRIMARIO = new Color(52, 152, 219);

    private final Color COLOR_SECUNDARIO = new Color(46, 64, 83);

    private final Color COLOR_ACCION = new Color(52, 152, 219);

    private final Color COLOR_ACCION_HOVER = new Color(41, 128, 185);

    private final Color COLOR_PELIGRO = new Color(231, 76, 60);

    private final Color COLOR_ALERTA = new Color(241, 196, 15);

    private final Color COLOR_TEXTO = new Color(52, 73, 94);

    private final Color COLOR_BORDE = new Color(220, 220, 220);

    private final Color COLOR_FONDO_CAMPO = new Color(248, 249, 250);

    private final Color COLOR_EXITO = new Color(46, 204, 113);



    public ProductosCatalogo(String rol, String usuario, int idUsuario) {

        this.rolUsuario = (rol != null) ? rol : "vendedor";

        this.nombreUsuario = usuario;

        this.idUsuario = idUsuario;

    }



    public JPanel getProductosPanel(int parentW, int parentH) {

        boolean esAdmin = "admin".equalsIgnoreCase(this.rolUsuario);

        JPanel mainPanel = new JPanel();

        mainPanel.setBounds(0, 0, parentW, parentH);

        mainPanel.setBackground(COLOR_FONDO);

        mainPanel.setLayout(null);



        // Tarjeta principal

        int cardMargin = 30;

        RoundedPanel card = new RoundedPanel(24);

        card.setBounds(cardMargin, cardMargin, parentW - (2 * cardMargin), parentH - (2 * cardMargin));

        card.setBackground(Color.WHITE);

        card.setLayout(null);

        card.setBorder(BorderFactory.createCompoundBorder(

                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),

                BorderFactory.createEmptyBorder(25, 35, 35, 35)));

        mainPanel.add(card);



        int internalPaddingX = 30;

        int currentY = 20;



        // Título con icono

        JLabel titulo = new JLabel("📦 Catálogo de Productos");

        titulo.setBounds(internalPaddingX, currentY, 400, 40);

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));

        titulo.setForeground(COLOR_TEXTO);

        card.add(titulo);

        currentY += 50;



        // Panel de estadísticas/alertas

        RoundedPanel statsPanel = new RoundedPanel(12);

        statsPanel.setBounds(internalPaddingX, currentY, card.getWidth() - (2 * internalPaddingX) - 70, 70);

        statsPanel.setBackground(new Color(255, 243, 224));

        statsPanel.setBorder(BorderFactory.createLineBorder(new Color(241, 196, 15), 2));

        statsPanel.setLayout(null);

        card.add(statsPanel);



        JLabel alertIcon = new JLabel("⚠️");

        alertIcon.setBounds(15, 15, 40, 40);

        alertIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));

        statsPanel.add(alertIcon);



        JLabel alertText = new JLabel("Los productos en rojo tienen stock bajo o crítico");

        alertText.setBounds(60, 10, 500, 25);

        alertText.setFont(new Font("Segoe UI", Font.BOLD, 13));

        alertText.setForeground(new Color(183, 149, 11));

        statsPanel.add(alertText);



        JLabel alertSubtext = new JLabel("Se destacan cuando el stock actual está igual o por debajo del stock mínimo");

        alertSubtext.setBounds(60, 32, 600, 20);

        alertSubtext.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        alertSubtext.setForeground(new Color(120, 120, 120));

        statsPanel.add(alertSubtext);

        currentY += 80;



        // Botones de acción

        RoundButton btnAgregar = new RoundButton("➕ Nuevo Producto", 18);

        btnAgregar.setBounds(internalPaddingX, currentY, 160, 45);

        btnAgregar.setBackground(COLOR_EXITO);

        btnAgregar.setForeground(Color.WHITE);

        btnAgregar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));

        btnAgregar.setBorderPainted(false);

        btnAgregar.setFocusPainted(false);

        btnAgregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnAgregar.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {

                btnAgregar.setBackground(new Color(39, 174, 96));

            }



            public void mouseExited(java.awt.event.MouseEvent evt) {

                btnAgregar.setBackground(COLOR_EXITO);

            }

        });

        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());

        btnAgregar.setEnabled(esAdmin);

        card.add(btnAgregar);



        RoundButton btnEditar = new RoundButton("✏️ Editar Seleccionado", 18);

        btnEditar.setBounds(internalPaddingX + 170, currentY, 190, 45);

        btnEditar.setBackground(COLOR_ACCION);

        btnEditar.setForeground(Color.WHITE);

        btnEditar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));

        btnEditar.setBorderPainted(false);

        btnEditar.setFocusPainted(false);

        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {

                if (esAdmin)

                    btnEditar.setBackground(COLOR_ACCION_HOVER);

            }



            public void mouseExited(java.awt.event.MouseEvent evt) {

                if (esAdmin)

                    btnEditar.setBackground(COLOR_ACCION);

            }

        });

        btnEditar.addActionListener(e -> mostrarFormularioEditar());

        btnEditar.setEnabled(esAdmin);

        card.add(btnEditar);



        RoundButton btnEliminar = new RoundButton("🗑️ Eliminar", 18);

        btnEliminar.setBounds(internalPaddingX + 370, currentY, 130, 45);

        btnEliminar.setBackground(COLOR_PELIGRO);

        btnEliminar.setForeground(Color.WHITE);

        btnEliminar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));

        btnEliminar.setBorderPainted(false);

        btnEliminar.setFocusPainted(false);

        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {

                if (esAdmin)

                    btnEliminar.setBackground(new Color(192, 57, 43));

            }



            public void mouseExited(java.awt.event.MouseEvent evt) {

                if (esAdmin)

                    btnEliminar.setBackground(COLOR_PELIGRO);

            }

        });

        btnEliminar.addActionListener(e -> eliminarProducto());

        btnEliminar.setEnabled(esAdmin);

        card.add(btnEliminar);



        RoundButton btnRefrescar = new RoundButton("🔄 Refrescar", 18);

        btnRefrescar.setBounds(internalPaddingX + 510, currentY, 130, 45);

        btnRefrescar.setBackground(COLOR_SECUNDARIO);

        btnRefrescar.setForeground(Color.WHITE);

        btnRefrescar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));

        btnRefrescar.setBorderPainted(false);

        btnRefrescar.setFocusPainted(false);

        btnRefrescar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnRefrescar.addActionListener(e -> cargarProductos());

        card.add(btnRefrescar);



        currentY += 60;



        // Tabla de productos

        modelo = new DefaultTableModel() {

            @Override

            public boolean isCellEditable(int row, int column) {

                return false;

            }

        };

        modelo.addColumn("ID");

        modelo.addColumn("Nombre");

        modelo.addColumn("Precio");

        modelo.addColumn("Stock Actual");

        modelo.addColumn("Stock Mínimo");

        modelo.addColumn("Estado");



        tabla = new JTable(modelo);

        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tabla.setRowHeight(35);

        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabla.getTableHeader().setBackground(COLOR_SECUNDARIO);

        tabla.getTableHeader().setForeground(Color.WHITE);

        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.setGridColor(new Color(230, 230, 230));

        tabla.setSelectionBackground(new Color(52, 152, 219, 50));

        tabla.setSelectionForeground(COLOR_TEXTO);



        // Renderizador personalizado para alertas de stock

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override

            public Component getTableCellRendererComponent(JTable table, Object value,

                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);



                try {

                    int stockActual = Integer.parseInt(table.getValueAt(row, 3).toString());

                    int stockMinimo = Integer.parseInt(table.getValueAt(row, 4).toString());



                    if (stockActual <= stockMinimo) {

                        if (!isSelected) {

                            c.setBackground(new Color(254, 226, 226));

                            c.setForeground(new Color(185, 28, 28));

                        }

                        setFont(getFont().deriveFont(Font.BOLD));

                    } else {

                        if (!isSelected) {

                            c.setBackground(Color.WHITE);

                            c.setForeground(COLOR_TEXTO);

                        }

                        setFont(getFont().deriveFont(Font.PLAIN));

                    }

                } catch (Exception e) {

                    if (!isSelected) {

                        c.setBackground(Color.WHITE);

                        c.setForeground(COLOR_TEXTO);

                    }

                }



                setHorizontalAlignment(column == 1 ? SwingConstants.LEFT : SwingConstants.CENTER);

                return c;

            }

        });



        // Ancho de columnas

        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);

        tabla.getColumnModel().getColumn(1).setPreferredWidth(250);

        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);

        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);

        tabla.getColumnModel().getColumn(4).setPreferredWidth(100);

        tabla.getColumnModel().getColumn(5).setPreferredWidth(120);



        JScrollPane scroll = new JScrollPane(tabla);

        scroll.setBounds(internalPaddingX, currentY,

                card.getWidth() - (2 * internalPaddingX) - 70,

                card.getHeight() - currentY - internalPaddingX);

        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));

        card.add(scroll);



        // Cargar datos

        cargarProductos();



        return mainPanel;

    }



    private void cargarProductos() {

        modelo.setRowCount(0);



        Connection conn = null;

        Statement stmt = null;

        ResultSet rs = null;



        try {

            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");

            stmt = conn.createStatement();

            rs = stmt.executeQuery(

                    "SELECT id_prod, nom_prod, pre_prod, exi_prod, stock_minimo FROM productos ORDER BY nom_prod");



            while (rs.next()) {

                int id = rs.getInt("id_prod");

                String nombre = rs.getString("nom_prod");

                double precio = rs.getDouble("pre_prod");

                int stockActual = rs.getInt("exi_prod");

                int stockMinimo = rs.getInt("stock_minimo");



                String estado;

                if (stockActual <= 0) {

                    estado = "❌ Agotado";

                } else if (stockActual <= stockMinimo) {

                    estado = "⚠️ Crítico";

                } else if (stockActual <= stockMinimo * 1.5) {

                    estado = "⚡ Bajo";

                } else {

                    estado = "✅ Normal";

                }



                Object[] fila = {

                        id,

                        nombre,

                        String.format("$%.2f", precio),

                        stockActual,

                        stockMinimo,

                        estado

                };

                modelo.addRow(fila);

            }



        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(null,

                    "Error al cargar productos: " + ex.getMessage(),

                    "Error", JOptionPane.ERROR_MESSAGE);

        } finally {

            try {

                if (rs != null)

                    rs.close();

                if (stmt != null)

                    stmt.close();

                if (conn != null)

                    conn.close();

            } catch (SQLException ex) {

                System.err.println("Error al cerrar conexión: " + ex.getMessage());

            }

        }

    }



    private void mostrarFormularioAgregar() {

        JPanel panel = new JPanel();

        panel.setLayout(new java.awt.GridLayout(5, 2, 10, 10));



        JLabel lblNombre = new JLabel("Nombre del Producto:");

        JTextField txtNombre = new JTextField();



        JLabel lblPrecio = new JLabel("Precio de Venta:");

        JTextField txtPrecio = new JTextField();



        JLabel lblStock = new JLabel("Stock Inicial:");

        JTextField txtStock = new JTextField();



        JLabel lblStockMin = new JLabel("Stock Mínimo:");

        JTextField txtStockMin = new JTextField();



        JLabel lblProveedor = new JLabel("ID Proveedor:");

        JTextField txtProveedor = new JTextField();



        panel.add(lblNombre);

        panel.add(txtNombre);

        panel.add(lblPrecio);

        panel.add(txtPrecio);

        panel.add(lblStock);

        panel.add(txtStock);

        panel.add(lblStockMin);

        panel.add(txtStockMin);

        panel.add(lblProveedor);

        panel.add(txtProveedor);



        int result = JOptionPane.showConfirmDialog(null, panel,

                "Agregar Nuevo Producto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);



        if (result == JOptionPane.OK_OPTION) {

            try {

                String nombre = txtNombre.getText().trim();

                double precio = Double.parseDouble(txtPrecio.getText().trim());

                int stock = Integer.parseInt(txtStock.getText().trim());

                int stockMin = Integer.parseInt(txtStockMin.getText().trim());

                int idProveedor = Integer.parseInt(txtProveedor.getText().trim());



                if (nombre.isEmpty()) {

                    JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío",

                            "Error", JOptionPane.ERROR_MESSAGE);

                    return;

                }



                Connection conn = null;

                PreparedStatement pstmt = null;



                try {

                    Class.forName("org.sqlite.JDBC");

                    conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");

                    conn.setAutoCommit(false);



                    String sql = "INSERT INTO productos (nom_prod, pre_prod, exi_prod, stock_minimo, id_proveedor) VALUES (?, ?, ?, ?, ?)";

                    pstmt = conn.prepareStatement(sql);

                    pstmt.setString(1, nombre);

                    pstmt.setDouble(2, precio);

                    pstmt.setInt(3, stock);

                    pstmt.setInt(4, stockMin);

                    pstmt.setInt(5, idProveedor);



                    pstmt.executeUpdate();

                    conn.commit();



                    AuditLogger.registrar(idUsuario, "Productos", "CREAR_PRODUCTO");



                    JOptionPane.showMessageDialog(null, "Producto agregado exitosamente",

                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    cargarProductos();



                } catch (SQLException ex) {

                    if (conn != null)

                        conn.rollback();

                    throw ex;

                } finally {

                    if (pstmt != null)

                        pstmt.close();

                    if (conn != null)

                        conn.close();

                }



            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(null, "Por favor ingrese valores numéricos válidos",

                        "Error", JOptionPane.ERROR_MESSAGE);

            } catch (Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage(),

                        "Error", JOptionPane.ERROR_MESSAGE);

            }

        }

    }



    private void mostrarFormularioEditar() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(null,

                    "Por favor seleccione un producto para editar",

                    "Advertencia", JOptionPane.WARNING_MESSAGE);

            return;

        }



        int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

        String nombreActual = tabla.getValueAt(fila, 1).toString();

        String precioActual = tabla.getValueAt(fila, 2).toString().replace("$", "").replace(",", "");



        JPanel panel = new JPanel();

        panel.setLayout(new java.awt.GridLayout(2, 2, 10, 10));



        JLabel lblNombre = new JLabel("Nombre del Producto:");

        JTextField txtNombre = new JTextField(nombreActual);



        JLabel lblPrecio = new JLabel("Precio de Venta:");

        JTextField txtPrecio = new JTextField(precioActual);



        panel.add(lblNombre);

        panel.add(txtNombre);

        panel.add(lblPrecio);

        panel.add(txtPrecio);



        int result = JOptionPane.showConfirmDialog(null, panel,

                "Editar Producto #" + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);



        if (result == JOptionPane.OK_OPTION) {

            try {

                String nombre = txtNombre.getText().trim();

                double precio = Double.parseDouble(txtPrecio.getText().trim());



                if (nombre.isEmpty()) {

                    JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío",

                            "Error", JOptionPane.ERROR_MESSAGE);

                    return;

                }



                Connection conn = null;

                PreparedStatement pstmt = null;



                try {

                    Class.forName("org.sqlite.JDBC");

                    conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");

                    conn.setAutoCommit(false);



                    String sql = "UPDATE productos SET nom_prod = ?, pre_prod = ? WHERE id_prod = ?";

                    pstmt = conn.prepareStatement(sql);

                    pstmt.setString(1, nombre);

                    pstmt.setDouble(2, precio);

                    pstmt.setInt(3, id);



                    int filasAfectadas = pstmt.executeUpdate();



                    if (filasAfectadas > 0) {

                        conn.commit();

                        AuditLogger.registrar(idUsuario, "Productos", "EDITAR_PRODUCTO");

                        JOptionPane.showMessageDialog(null, "Producto actualizado exitosamente",

                                "Éxito", JOptionPane.INFORMATION_MESSAGE);

                        cargarProductos();

                    } else {

                        JOptionPane.showMessageDialog(null, "No se pudo actualizar el producto",

                                "Error", JOptionPane.ERROR_MESSAGE);

                    }



                } catch (SQLException ex) {

                    if (conn != null)

                        conn.rollback();

                    throw ex;

                } finally {

                    if (pstmt != null)

                        pstmt.close();

                    if (conn != null)

                        conn.close();

                }



            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(null, "El precio debe ser un número válido",

                        "Error", JOptionPane.ERROR_MESSAGE);

            } catch (Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(null, "Error al actualizar: " + ex.getMessage(),

                        "Error", JOptionPane.ERROR_MESSAGE);

            }

        }

    }



    private void eliminarProducto() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(null,

                    "Por favor seleccione un producto para eliminar",

                    "Advertencia", JOptionPane.WARNING_MESSAGE);

            return;

        }



        int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

        String nombre = tabla.getValueAt(fila, 1).toString();



        int confirmacion = JOptionPane.showConfirmDialog(null,

                "¿Está seguro de que desea eliminar el producto '" + nombre + "'?\nEsta acción no se puede deshacer.",

                "Confirmar Eliminación",

                JOptionPane.YES_NO_OPTION,

                JOptionPane.WARNING_MESSAGE);



        if (confirmacion != JOptionPane.YES_OPTION) {

            return;

        }



        Connection conn = null;

        PreparedStatement pstmt = null;



        try {

            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");

            conn.setAutoCommit(false);



            String sql = "DELETE FROM productos WHERE id_prod = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);



            int filasAfectadas = pstmt.executeUpdate();



            if (filasAfectadas > 0) {

                conn.commit();

                AuditLogger.registrar(idUsuario, "Productos", "ELIMINAR_PRODUCTO");

                JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente",

                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                cargarProductos();

            } else {

                JOptionPane.showMessageDialog(null, "No se pudo eliminar el producto",

                        "Error", JOptionPane.ERROR_MESSAGE);

            }



        } catch (SQLException ex) {

            if (conn != null) {

                try {

                    conn.rollback();

                } catch (SQLException rollbackEx) {

                    System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());

                }

            }

            ex.printStackTrace();

            JOptionPane.showMessageDialog(null, "Error al eliminar: " + ex.getMessage(),

                    "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(),

                    "Error", JOptionPane.ERROR_MESSAGE);

        } finally {

            try {

                if (pstmt != null)

                    pstmt.close();

                if (conn != null)

                    conn.close();

            } catch (SQLException ex) {

                System.err.println("Error al cerrar conexión: " + ex.getMessage());

            }

        }

    }

}

