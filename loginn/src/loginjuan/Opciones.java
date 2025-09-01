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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Lenovo
 */
class Opciones {
    // Entradas para borrar
    JTextField ent_dluser = new JTextField();
    JTextField ent_dlpasw = new JTextField();

    // El menú principal
    JFrame opciones = new JFrame("Sistema de Gestión");
    
    public String cli;
    
    public Opciones(String[] fila, Color Azul, Color azul_claro) {
        
        // cli = cedula_del_cliente;
        opciones.setSize(900, 650);
        opciones.setDefaultCloseOperation(opciones.DISPOSE_ON_CLOSE);
        opciones.setLocationRelativeTo(null);
        opciones.setLayout(null);
        opciones.getContentPane().setBackground(new Color(245, 245, 245));

        // Panel lateral
        JPanel panel_lateral = new JPanel();
        panel_lateral.setBounds(0, 0, 250, 650);
        panel_lateral.setBackground(new Color(52, 73, 94));
        panel_lateral.setLayout(null);
        
        // Panel superior
        JPanel panel_superior = new JPanel();
        panel_superior.setBounds(250, 0, 650, 80);
        panel_superior.setBackground(new Color(52, 73, 94));
        panel_superior.setLayout(null);
        
        // Título del sistema
        JLabel titulo = new JLabel("SISTEMA DE GESTIÓN");
        titulo.setBounds(20, 20, 160, 30);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel_lateral.add(titulo);
        
        // Información del usuario
        JLabel userInfo = new JLabel("Usuario: " + fila[1]);
        userInfo.setBounds(20, 60, 160, 20);
        userInfo.setForeground(Color.WHITE);
        panel_lateral.add(userInfo);
        
        // Línea separadora
        JPanel separador = new JPanel();
        separador.setBounds(20, 90, 160, 2);
        separador.setBackground(Color.WHITE);
        panel_lateral.add(separador);
        
        // Título del panel superior
        JLabel tituloPrincipal = new JLabel("Panel de Control");
        tituloPrincipal.setBounds(20, 20, 300, 40);
        tituloPrincipal.setForeground(Color.WHITE);
        tituloPrincipal.setFont(new Font("Arial", Font.BOLD, 24));
        panel_superior.add(tituloPrincipal);
        
        // Panel central
        JPanel panel_central = new JPanel();
        panel_central.setBounds(270, 100, 610, 530);
        panel_central.setBackground(Color.WHITE);
        panel_central.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        panel_central.setLayout(null);
        
        // Mensaje de bienvenida en el panel central
        JLabel bienvenida = new JLabel("Bienvenido al Sistema de Gestión");
        bienvenida.setBounds(100, 100, 400, 40);
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bienvenida.setForeground(new Color(52, 73, 94));
        bienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        panel_central.add(bienvenida);
        
        JLabel instrucciones = new JLabel("Seleccione una opción del menú lateral");
        instrucciones.setBounds(100, 150, 400, 30);
        instrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        instrucciones.setForeground(new Color(44, 62, 80));
        instrucciones.setHorizontalAlignment(SwingConstants.CENTER);
        panel_central.add(instrucciones);

        // Botones del menú lateral con mejor estilo
        JButton boton_clientes = new JButton("Clientes");
        boton_clientes.setBounds(25, 120, 200, 45);
        boton_clientes.setBackground(new Color(41, 128, 185));
        boton_clientes.setForeground(Color.WHITE);
        boton_clientes.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton_clientes.setFocusPainted(false);
        boton_clientes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        boton_clientes.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientes(e, opciones, Azul);
            }
        });
        panel_lateral.add(boton_clientes);

        JButton boton_prod = new JButton("Productos");
        boton_prod.setBounds(25, 180, 200, 45);
        boton_prod.setBackground(new Color(41, 128, 185));
        boton_prod.setForeground(Color.WHITE);
        boton_prod.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton_prod.setFocusPainted(false);
        boton_prod.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        boton_prod.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    productos(e);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Opciones.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        panel_lateral.add(boton_prod);
        
        JButton eliminar = new JButton("Eliminar Usuario");
        eliminar.setBounds(25, 240, 200, 45);
        eliminar.setBackground(new Color(41, 128, 185));
        eliminar.setForeground(Color.WHITE);
        eliminar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        eliminar.setFocusPainted(false);
        eliminar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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
        panel_lateral.add(eliminar);

        // Agregar componentes al frame
        opciones.add(panel_lateral);
        opciones.add(panel_superior);
        opciones.add(panel_central);
        
        opciones.setVisible(true);
    }
    
    public void clientes(ActionEvent e, JFrame menu, Color Azul) {
        Clientes llamar_clientes = new Clientes(Azul, opciones);
    }
    public void productos(ActionEvent e) throws ClassNotFoundException, SQLException {
        Productos llamar_productos = new Productos(cli);
        llamar_productos.MostrarProductos();
    }
    
    public void BorrarUsuario(java.awt.event.ActionEvent evt) throws  ClassNotFoundException{
        JFrame ventana4 = new JFrame("Borrar Usuario");
        ventana4.setSize(500, 450);
        ventana4.setLocationRelativeTo(null);
        ventana4.setLayout(null);
        ventana4.getContentPane().setBackground(new Color(52, 73, 94));

        JPanel panel_borrar = new JPanel();
        panel_borrar.setBounds(50, 50, 400, 350);
        panel_borrar.setBackground(Color.WHITE);
        panel_borrar.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        panel_borrar.setLayout(null);
        
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(50, 30, 200, 20);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(new Color(52, 73, 94));
        panel_borrar.add(lblUsuario);

        ent_dluser.setBounds(50, 60, 300, 35);
        ent_dluser.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        ent_dluser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(50, 110, 200, 20);
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(new Color(52, 73, 94));
        panel_borrar.add(lblPassword);

        ent_dlpasw.setBounds(50, 140, 300, 35);
        ent_dlpasw.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        ent_dlpasw.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel_borrar.add(ent_dlpasw);
        panel_borrar.add(ent_dluser);
        ventana4.add(panel_borrar);
        ventana4.setVisible(true);

    }
}
