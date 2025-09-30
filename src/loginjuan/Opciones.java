/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginjuan;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Lenovo
 */
class Opciones {
    // Entradas para borrar
    JTextField ent_dluser = new JTextField();
    JTextField ent_dlpasw = new JPasswordField();

    // El menú principal
    JFrame opciones = new JFrame("Sistema de Gestión");
    
    public String cli;
    
    // Método auxiliar para crear botones del menú
    private JButton createMenuButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
        });
        
        return button;
    }
    
    // Método para obtener el resumen mensual de la base de datos
    private String[] obtenerResumenMensual() {
        String[] datos = {"—", "0", "0"}; // [0] = periodo, [1] = clientes, [2] = facturas
        
        Connection conn = null;
        java.sql.Statement stmt = null;
        java.sql.ResultSet rs = null;
        java.sql.ResultSet rsPeriodo = null;
        
        try {
            // Cargar el controlador JDBC
            Class.forName("org.sqlite.JDBC");
            
            // Establecer conexión con la base de datos
            conn = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");
            stmt = conn.createStatement();
            
            // ÚNICA consulta: resumen del mes actual (zona local)
            String sql =
                "SELECT " +
                "  strftime('%Y-%m', f.fec_factura, 'localtime') AS periodo, " +
                "  COUNT(DISTINCT f.ced_factura) AS total_clientes, " +
                "  COUNT(f.id_factura) AS total_facturas " +
                "FROM facturas f " +
                "WHERE strftime('%Y-%m', f.fec_factura, 'localtime') = strftime('%Y-%m', 'now', 'localtime')";
            
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                datos[0] = rs.getString("periodo");
                datos[1] = String.valueOf(rs.getInt("total_clientes"));
                datos[2] = String.valueOf(rs.getInt("total_facturas"));
            } else {
                // Si no hay datos este mes, al menos devolver el periodo actual
                rsPeriodo = stmt.executeQuery("SELECT strftime('%Y-%m', 'now', 'localtime') AS periodo");
                if (rsPeriodo.next()) {
                    datos[0] = rsPeriodo.getString("periodo");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar estadísticas: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cerrar recursos
            try { if (rsPeriodo != null) rsPeriodo.close(); } catch (Exception e) {}
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        
        return datos;
    }
    
    // Método para agregar cajas de estadísticas
    private void addStatBox(JPanel parent, String title, String value, int x, int y) {
        JPanel box = new JPanel();
        box.setBounds(x, y, 140, 50);
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createLineBorder(new Color(230, 236, 240), 1));
        box.setLayout(null);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setBounds(10, 5, 120, 15);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(120, 120, 120));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setBounds(10, 20, 120, 25);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(new Color(52, 73, 94));
        
        box.add(titleLabel);
        box.add(valueLabel);
        parent.add(box);
    }

    public Opciones(String[] fila, Color Azul, Color azul_claro) {
        // Configuración de la ventana principal
        opciones.setSize(1000, 700);
        opciones.setDefaultCloseOperation(opciones.DISPOSE_ON_CLOSE);
        opciones.setLocationRelativeTo(null);
        opciones.setLayout(null);
        opciones.getContentPane().setBackground(new Color(245, 247, 250));

        // Panel lateral
        JPanel panel_lateral = new JPanel();
        panel_lateral.setBounds(0, 0, 350, 700);
        panel_lateral.setBackground(new Color(52, 73, 94));
        panel_lateral.setLayout(null);
        
        // Logo y título
        JLabel iconLabel = new JLabel("📊");
        iconLabel.setBounds(30, 30, 60, 60);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel_lateral.add(iconLabel);
        
        JLabel titulo = new JLabel("Sistema de Gestión");
        titulo.setBounds(30, 100, 290, 40);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel_lateral.add(titulo);
        
        // Información del usuario
        JLabel userInfo = new JLabel("Bienvenido, " + fila[1]);
        userInfo.setBounds(30, 150, 290, 25);
        userInfo.setForeground(new Color(200, 200, 200));
        userInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        panel_lateral.add(userInfo);
        
        // Línea separadora
        JPanel separador = new JPanel();
        separador.setBounds(30, 200, 290, 1);
        separador.setBackground(new Color(100, 120, 150));
        panel_lateral.add(separador);
        
        // Panel central
        JPanel panel_central = new JPanel();
        panel_central.setBounds(350, 0, 650, 700);
        panel_central.setBackground(new Color(245, 247, 250));
        panel_central.setLayout(null);
        
        // Tarjeta de contenido
        JPanel card = new JPanel();
        card.setBounds(50, 50, 550, 600);
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 30, 30)
        ));
        
        // Mensaje de bienvenida
        JLabel bienvenida = new JLabel("Panel de Control");
        bienvenida.setBounds(30, 50, 490, 40);
        bienvenida.setFont(new Font("Arial", Font.BOLD, 28));
        bienvenida.setForeground(new Color(52, 73, 94));
        card.add(bienvenida);
        
        JLabel instrucciones = new JLabel("Bienvenido al Sistema de Gestión");
        instrucciones.setBounds(30, 100, 490, 30);
        instrucciones.setFont(new Font("Arial", Font.PLAIN, 16));
        instrucciones.setForeground(new Color(100, 100, 100));
        card.add(instrucciones);
        
        // Panel de estadísticas (puedes personalizar esto)
        JPanel statsPanel = new JPanel();
        statsPanel.setBounds(30, 160, 490, 120);
        statsPanel.setBackground(new Color(245, 249, 252));
        statsPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 236, 240)));
        statsPanel.setLayout(null);
        
        JLabel statsTitle = new JLabel("Resumen Rápido");
        statsTitle.setBounds(20, 15, 200, 25);
        statsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        statsTitle.setForeground(new Color(52, 73, 94));
        statsPanel.add(statsTitle);

        // Obtener y mostrar estadísticas del mes
        String[] datosMensuales = obtenerResumenMensual();
        String periodo = datosMensuales[0];
        String totalClientes = datosMensuales[1];
        String totalFacturas = datosMensuales[2];
        
        // Mostrar el mes/año en lugar de "Clientes"
        addStatBox(statsPanel, "Período", periodo, 20, 50);
        // Mostrar total de clientes únicos
        addStatBox(statsPanel, "Clientes", totalClientes, 180, 50);
        // Mostrar total de facturas
        addStatBox(statsPanel, "Facturas", totalFacturas, 340, 50);
        
        card.add(statsPanel);
        
        // Botones de acción rápida
        JLabel quickActions = new JLabel("Acciones Rápidas");
        quickActions.setBounds(30, 310, 490, 25);
        quickActions.setFont(new Font("Arial", Font.BOLD, 16));
        quickActions.setForeground(new Color(52, 73, 94));
        card.add(quickActions);
        
        // Botones del menú lateral con mejor estilo
        JButton boton_clientes = createMenuButton("👥 Clientes", 30, 350, 490, 50);
        boton_clientes.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientes(e, opciones, Azul);
            }
        });
        card.add(boton_clientes);

        JButton boton_prod = createMenuButton("📦 Productos", 30, 410, 490, 50);
        boton_prod.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    productos(e);
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        card.add(boton_prod);

        JButton boton_inventario = createMenuButton("📦 Inventario", 30, 470, 490, 50);
        boton_inventario.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    inventario(e);
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        card.add(boton_inventario);
        
        JButton eliminar = createMenuButton("🗑️ Eliminar Usuario", 30, 470, 490, 50);
        eliminar.setBackground(new Color(231, 76, 60));
        eliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                eliminar.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                eliminar.setBackground(new Color(231, 76, 60));
            }
        });
        eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    BorrarUsuario(evt);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        //card.add(eliminar);
        
        // Pie de página
        JPanel footerPanel = new JPanel();
        footerPanel.setBounds(0, 650, 350, 50);
        footerPanel.setBackground(new Color(45, 62, 80));
        footerPanel.setLayout(new java.awt.BorderLayout());
        
        JLabel copyrightLabel = new JLabel(" 2023 Sistema de Gestión - v1.0");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        copyrightLabel.setForeground(new Color(150, 150, 150));
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(copyrightLabel, java.awt.BorderLayout.CENTER);
        
        // Agregar componentes al frame
        panel_lateral.add(footerPanel);
        panel_central.add(card);
        opciones.add(panel_lateral);
        opciones.add(panel_central);
        
        opciones.setVisible(true);
    }
    
    public void clientes(ActionEvent e, JFrame menu, Color Azul) {
        Clientes llamar_clientes = new Clientes(Azul);
    }
    public void productos(ActionEvent e) throws ClassNotFoundException, SQLException {
        Productos llamar_productos = new Productos(cli, "");
        llamar_productos.MostrarProductos();
    }
    public void inventario(ActionEvent e) throws ClassNotFoundException, SQLException {
        Inventario llamar_inventario = new Inventario();
        llamar_inventario.MostrarInventario();
    }

    public void BorrarUsuario(java.awt.event.ActionEvent evt) throws ClassNotFoundException {
        JFrame ventana4 = new JFrame("Eliminar Usuario");
        ventana4.setSize(500, 450);
        ventana4.setLocationRelativeTo(null);
        ventana4.setLayout(null);
        ventana4.getContentPane().setBackground(new Color(245, 247, 250));
        
        // Panel principal
        JPanel panel_borrar = new JPanel();
        panel_borrar.setBounds(50, 50, 400, 350);
        panel_borrar.setBackground(Color.WHITE);
        panel_borrar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 30, 30)
        ));
        panel_borrar.setLayout(null);
        
        // Título
        JLabel titulo = new JLabel("Eliminar Usuario");
        titulo.setBounds(0, 10, 340, 40);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(52, 73, 94));
        panel_borrar.add(titulo);
        
        // Línea divisoria
        JPanel linea = new JPanel();
        linea.setBounds(0, 60, 340, 1);
        linea.setBackground(new Color(230, 236, 240));
        panel_borrar.add(linea);
        
        // Campo de usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(0, 90, 200, 20);
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsuario.setForeground(new Color(80, 80, 80));
        panel_borrar.add(lblUsuario);

        ent_dluser.setBounds(0, 115, 340, 45);
        ent_dluser.setFont(new Font("Arial", Font.PLAIN, 15));
        ent_dluser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Campo de contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(0, 180, 200, 20);
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPassword.setForeground(new Color(80, 80, 80));
        panel_borrar.add(lblPassword);

        ent_dlpasw.setBounds(0, 205, 340, 45);
        ent_dlpasw.setFont(new Font("Arial", Font.PLAIN, 15));
        ent_dlpasw.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Botón de eliminar
        JButton btnEliminar = new JButton("Eliminar Usuario");
        btnEliminar.setBounds(0, 280, 340, 45);
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 15));
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setBorderPainted(false);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Efecto hover para el botón
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEliminar.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEliminar.setBackground(new Color(231, 76, 60));
            }
        });
        
        panel_borrar.add(btnEliminar);
        panel_borrar.add(ent_dluser);
        panel_borrar.add(ent_dlpasw);
        
        ventana4.add(panel_borrar);
        ventana4.setVisible(true);
    }
}
