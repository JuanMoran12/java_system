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

public class ClientesModulo {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private OnIniciarVentaListener ventaListener;
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
    private final Color COLOR_EXITO = new Color(46, 204, 113);
    private final Color COLOR_TEXTO = new Color(52, 73, 94);
    private final Color COLOR_BORDE = new Color(220, 220, 220);
    private final Color COLOR_FONDO_CAMPO = new Color(248, 249, 250);

    // Interface para comunicar con Opciones
    public interface OnIniciarVentaListener {
        void onIniciarVenta(String cedula, String nombreCompleto);
    }

    public void setOnIniciarVentaListener(OnIniciarVentaListener listener) {
        this.ventaListener = listener;
    }

    public ClientesModulo(String rol, String usuario, int idUsuario) {
        this.rolUsuario = (rol != null) ? rol : "vendedor";
        this.nombreUsuario = usuario;
        this.idUsuario = idUsuario;
    }

    public JPanel getClientesPanel(int parentW, int parentH) {
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

        // Título
        JLabel titulo = new JLabel("👥 Gestión de Clientes");
        titulo.setBounds(internalPaddingX, currentY, 400, 40);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(COLOR_TEXTO);
        card.add(titulo);
        currentY += 60;

        // Panel de búsqueda
        RoundedPanel searchPanel = new RoundedPanel(12);
        searchPanel.setBounds(internalPaddingX, currentY, card.getWidth() - (2 * internalPaddingX) - 70, 70);
        searchPanel.setBackground(new Color(245, 249, 252));
        searchPanel.setLayout(null);
        card.add(searchPanel);

        JLabel lblBuscar = new JLabel("🔍 Buscar cliente:");
        lblBuscar.setBounds(20, 10, 150, 25);
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblBuscar.setForeground(COLOR_TEXTO);
        searchPanel.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(20, 35, 400, 30);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txtBuscar.setBackground(Color.WHITE);
        searchPanel.add(txtBuscar);

        RoundButton btnBuscar = new RoundButton("Buscar", 14);
        btnBuscar.setBounds(435, 35, 100, 30);
        btnBuscar.setBackground(COLOR_PRIMARIO);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(e -> buscarClientes());
        searchPanel.add(btnBuscar);

        RoundButton btnMostrarTodos = new RoundButton("Mostrar Todos", 14);
        btnMostrarTodos.setBounds(550, 35, 130, 30);
        btnMostrarTodos.setBackground(COLOR_SECUNDARIO);
        btnMostrarTodos.setForeground(Color.WHITE);
        btnMostrarTodos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMostrarTodos.setBorderPainted(false);
        btnMostrarTodos.setFocusPainted(false);
        btnMostrarTodos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMostrarTodos.addActionListener(e -> {
            txtBuscar.setText("");
            cargarClientes();
        });
        searchPanel.add(btnMostrarTodos);

        currentY += 85;

        // Botones de acción
        RoundButton btnAgregar = new RoundButton("➕ Nuevo Cliente", 18);
        btnAgregar.setBounds(internalPaddingX, currentY, 170, 45);
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

        RoundButton btnEditar = new RoundButton("✏️ Editar", 18);
        btnEditar.setBounds(internalPaddingX + 180, currentY, 130, 45);
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
        btnEliminar.setBounds(internalPaddingX + 320, currentY, 130, 45);
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
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnEliminar.setEnabled(esAdmin);
        card.add(btnEliminar);

        // Botón para iniciar venta (destacado)
        RoundButton btnIniciarVenta = new RoundButton("🛒 Iniciar Venta", 18);
        btnIniciarVenta.setBounds(card.getWidth() - internalPaddingX - 180 - 70, currentY, 180, 45);
        btnIniciarVenta.setBackground(new Color(142, 68, 173)); // Púrpura
        btnIniciarVenta.setForeground(Color.WHITE);
        btnIniciarVenta.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        btnIniciarVenta.setBorderPainted(false);
        btnIniciarVenta.setFocusPainted(false);
        btnIniciarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIniciarVenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIniciarVenta.setBackground(new Color(125, 60, 152));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIniciarVenta.setBackground(new Color(142, 68, 173));
            }
        });
        btnIniciarVenta.addActionListener(e -> iniciarVenta());
        card.add(btnIniciarVenta);

        currentY += 60;

        // Tabla de clientes
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Dirección");

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

        // Renderizador para alinear texto
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(250);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(internalPaddingX, currentY,
                card.getWidth() - (2 * internalPaddingX) - 70,
                card.getHeight() - currentY - internalPaddingX);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        card.add(scroll);

        // Cargar datos
        cargarClientes();

        return mainPanel;
    }

    private void cargarClientes() {
        modelo.setRowCount(0);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                    "SELECT id, cedula, nombre, apellido, telefono, direccion FROM clientes ORDER BY nombre, apellido");

            while (rs.next()) {
                Object[] fila = {
                        rs.getInt("id"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono") != null ? rs.getString("telefono") : "—",
                        rs.getString("direccion") != null ? rs.getString("direccion") : "—"
                };
                modelo.addRow(fila);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar clientes: " + ex.getMessage(),
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

    private void buscarClientes() {
        String busqueda = txtBuscar.getText().trim();
        if (busqueda.isEmpty()) {
            cargarClientes();
            return;
        }

        modelo.setRowCount(0);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");

            String sql = "SELECT id, cedula, nombre, apellido, telefono, direccion FROM clientes " +
                    "WHERE cedula LIKE ? OR nombre LIKE ? OR apellido LIKE ? OR telefono LIKE ? " +
                    "ORDER BY nombre, apellido";

            pstmt = conn.prepareStatement(sql);
            String param = "%" + busqueda + "%";
            pstmt.setString(1, param);
            pstmt.setString(2, param);
            pstmt.setString(3, param);
            pstmt.setString(4, param);

            rs = pstmt.executeQuery();

            boolean encontro = false;
            while (rs.next()) {
                Object[] fila = {
                        rs.getInt("id"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono") != null ? rs.getString("telefono") : "—",
                        rs.getString("direccion") != null ? rs.getString("direccion") : "—"
                };
                modelo.addRow(fila);
                encontro = true;
            }

            if (!encontro) {
                JOptionPane.showMessageDialog(null,
                        "No se encontraron clientes con ese criterio de búsqueda",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al buscar clientes: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void mostrarFormularioAgregar() {
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridLayout(5, 2, 10, 10));

        JLabel lblCedula = new JLabel("Cédula:");
        JTextField txtCedula = new JTextField();

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField();

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField();

        JLabel lblDireccion = new JLabel("Dirección:");
        JTextField txtDireccion = new JTextField();

        panel.add(lblCedula);
        panel.add(txtCedula);
        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblApellido);
        panel.add(txtApellido);
        panel.add(lblTelefono);
        panel.add(txtTelefono);
        panel.add(lblDireccion);
        panel.add(txtDireccion);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Agregar Nuevo Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String cedula = txtCedula.getText().trim();
                String nombre = txtNombre.getText().trim();
                String apellido = txtApellido.getText().trim();
                String telefono = txtTelefono.getText().trim();
                String direccion = txtDireccion.getText().trim();

                if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Cédula, nombre y apellido son campos obligatorios",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connection conn = null;
                PreparedStatement pstmt = null;

                try {
                    Class.forName("org.sqlite.JDBC");
                    conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");
                    conn.setAutoCommit(false);

                    String sql = "INSERT INTO clientes (cedula, nombre, apellido, telefono, direccion) VALUES (?, ?, ?, ?, ?)";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, cedula);
                    pstmt.setString(2, nombre);
                    pstmt.setString(3, apellido);
                    pstmt.setString(4, telefono.isEmpty() ? null : telefono);
                    pstmt.setString(5, direccion.isEmpty() ? null : direccion);

                    pstmt.executeUpdate();
                    conn.commit();

                    AuditLogger.registrar(idUsuario, "Clientes", "CREAR_CLIENTE");

                    JOptionPane.showMessageDialog(null, "Cliente agregado exitosamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarClientes();

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
                    "Por favor seleccione un cliente para editar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
        String cedulaActual = tabla.getValueAt(fila, 1).toString();
        String nombreActual = tabla.getValueAt(fila, 2).toString();
        String apellidoActual = tabla.getValueAt(fila, 3).toString();
        String telefonoActual = tabla.getValueAt(fila, 4).toString();
        String direccionActual = tabla.getValueAt(fila, 5).toString();

        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridLayout(5, 2, 10, 10));

        JLabel lblCedula = new JLabel("Cédula:");
        JTextField txtCedula = new JTextField(cedulaActual);

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(nombreActual);

        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField(apellidoActual);

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField(telefonoActual.equals("—") ? "" : telefonoActual);

        JLabel lblDireccion = new JLabel("Dirección:");
        JTextField txtDireccion = new JTextField(direccionActual.equals("—") ? "" : direccionActual);

        panel.add(lblCedula);
        panel.add(txtCedula);
        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblApellido);
        panel.add(txtApellido);
        panel.add(lblTelefono);
        panel.add(txtTelefono);
        panel.add(lblDireccion);
        panel.add(txtDireccion);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Editar Cliente #" + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String cedula = txtCedula.getText().trim();
                String nombre = txtNombre.getText().trim();
                String apellido = txtApellido.getText().trim();
                String telefono = txtTelefono.getText().trim();
                String direccion = txtDireccion.getText().trim();

                if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Cédula, nombre y apellido son campos obligatorios",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connection conn = null;
                PreparedStatement pstmt = null;

                try {
                    Class.forName("org.sqlite.JDBC");
                    conn = DriverManager.getConnection("jdbc:sqlite:ventasdb.db");
                    conn.setAutoCommit(false);

                    String sql = "UPDATE clientes SET cedula = ?, nombre = ?, apellido = ?, telefono = ?, direccion = ? WHERE id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, cedula);
                    pstmt.setString(2, nombre);
                    pstmt.setString(3, apellido);
                    pstmt.setString(4, telefono.isEmpty() ? null : telefono);
                    pstmt.setString(5, direccion.isEmpty() ? null : direccion);
                    pstmt.setInt(6, id);

                    int filasAfectadas = pstmt.executeUpdate();

                    if (filasAfectadas > 0) {
                        conn.commit();
                        AuditLogger.registrar(idUsuario, "Clientes", "EDITAR_CLIENTE");
                        JOptionPane.showMessageDialog(null, "Cliente actualizado exitosamente",
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarClientes();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo actualizar el cliente",
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

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al actualizar: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarCliente() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null,
                    "Por favor seleccione un cliente para eliminar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
        String nombre = tabla.getValueAt(fila, 2).toString() + " " + tabla.getValueAt(fila, 3).toString();

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de que desea eliminar el cliente '" + nombre + "'?\nEsta acción no se puede deshacer.",
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

            String sql = "DELETE FROM clientes WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                conn.commit();
                AuditLogger.registrar(idUsuario, "Clientes", "ELIMINAR_CLIENTE");
                JOptionPane.showMessageDialog(null, "Cliente eliminado exitosamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarClientes();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo eliminar el cliente",
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

    private void iniciarVenta() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null,
                    "Por favor seleccione un cliente para iniciar la venta",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cedula = tabla.getValueAt(fila, 1).toString();
        String nombre = tabla.getValueAt(fila, 2).toString();
        String apellido = tabla.getValueAt(fila, 3).toString();
        String nombreCompleto = nombre + " " + apellido;

        if (ventaListener != null) {
            ventaListener.onIniciarVenta(cedula, nombreCompleto);
        }
    }
}
