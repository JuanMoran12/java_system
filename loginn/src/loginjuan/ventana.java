/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginjuan;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author USER
 */
class ventana {
    
    JTextField ent = new JTextField();
    JPasswordField ent2 = new JPasswordField();
    
    //entradas para insertar datos
    JTextField ent_usuario = new JTextField();
    JTextField ent_clave = new JTextField();
    
                    
    //color personalizado de la clase Color
    Color rojo_transparente = new Color(22, 109, 99); // Rojo semi-transparente
    Color Azul = new Color(36, 55, 189);
    Color azul_oscuro = new Color(40, 53, 96);
    Color azul_claro = new Color(176, 206, 255);
    Color grios_oscuro = new Color(65, 71, 78);
    Color gris_azul = new Color(51, 63, 85);  
    Color morado_claro = new Color(99, 23, 112);
    Color morado_oscuro = new Color(78, 34, 88);
   
    
    public ventana() {
        JFrame login = new JFrame("Sistema de Gestión - Login");
        login.setSize(900, 650);
        login.setLocationRelativeTo(null);
        login.setLayout(null);
        
        // Panel lateral para el menú
        JPanel panelLateral = new JPanel();
        panelLateral.setBounds(0, 0, 250, 650);
        panelLateral.setBackground(new Color(52, 73, 94));
        panelLateral.setLayout(null);
        login.add(panelLateral);
        
        // Panel central para el contenido
        JPanel panelCentral = new JPanel();
        panelCentral.setBounds(250, 0, 650, 650);
        panelCentral.setBackground(new Color(245, 245, 245));
        panelCentral.setLayout(null);
        login.add(panelCentral);
        
        // Título del formulario
        JLabel etqTitulo = new JLabel("Acceso al Sistema");
        etqTitulo.setBounds(30, 30, 200, 30);
        etqTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        etqTitulo.setForeground(Color.WHITE);
        panelLateral.add(etqTitulo);
        
        // Widgets con estilo moderno
        JLabel etq = new JLabel("Usuario");
        etq.setBounds(200, 100, 150, 30);
        etq.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        etq.setForeground(new Color(52, 73, 94));
        panelCentral.add(etq);
        
        JLabel etq2 = new JLabel("Contraseña");
        etq2.setBounds(200, 180, 150, 30);
        etq2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        etq2.setForeground(new Color(52, 73, 94));
        panelCentral.add(etq2);
        
        // Entradas con estilo moderno
        ent.setBounds(200, 130, 250, 40);
        ent.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panelCentral.add(ent);
        
        ent2.setBounds(200, 210, 250, 40);
        ent2.setEchoChar('•');
        ent2.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panelCentral.add(ent2);
        
        // Botón de ingreso con estilo moderno
        JButton LeeButton = new JButton("Ingresar");
        LeeButton.setBounds(200, 280, 250, 45);
        LeeButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        LeeButton.setBackground(new Color(52, 152, 219));
        LeeButton.setForeground(Color.WHITE);
        LeeButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        LeeButton.setFocusPainted(false);
        panelCentral.add(LeeButton);
        LeeButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                        {try {
                                LeeButtonEjecuta(evt);
                        } catch (ClassNotFoundException ex) {
                                Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                                Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
                        }
}
                    });
        
        // Imagen o logo decorativo en el panel lateral
        JLabel logoLabel = new JLabel("Sistema de Ventas");
        logoLabel.setBounds(30, 100, 190, 30);
        logoLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        logoLabel.setForeground(Color.WHITE);
        panelLateral.add(logoLabel);
        
        // Información de copyright en el panel lateral
        JLabel copyrightLabel = new JLabel("© 2023 Sistema de Gestión");
        copyrightLabel.setBounds(30, 600, 190, 20);
        copyrightLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 10));
        copyrightLabel.setForeground(new Color(189, 195, 199));
        panelLateral.add(copyrightLabel);

        login.setVisible(true);
    }
    
    public void LeeButtonEjecuta(java.awt.event.ActionEvent evt) throws ClassNotFoundException, SQLException{
        String vv_nombre = ent.getText().toLowerCase();
        String vv_clave = ent2.getText().toLowerCase();
        String[] fila = new String[3];
        String fila1 = null;
        try{   
                Class.forName("org.sqlite.JDBC");
                
                Connection con=DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");  

                Statement stmt=con.createStatement();

                System.out.println("error "+ent);
             
                String v_resul3="SELECT usuario, clave FROM login WHERE usuario LIKE '" + vv_nombre + "' AND clave LIKE '" + vv_clave + "'";

                System.out.println(v_resul3);
                ResultSet rs=stmt.executeQuery(v_resul3);
                while (rs.next()) {
                    fila1 = "Algo";
                    // fila[0] = rs.getString(1);
                    fila[1] = rs.getString(1);
                    fila[2] = rs.getString(2);
                    System.out.println(fila[0]+" "+fila[1]+" "+fila[2]);
     
                }
                if (fila1 == null) { JOptionPane.showMessageDialog(ent, "Login Incorrecto");} else
                {
                    //JOptionPane.showMessageDialog(ent, "Login Correcto");
                    //llamada a la clase para abrir el menu
                    Opciones llamar_menu = new Opciones(fila, azul_claro, azul_oscuro);
                };
                 
                con.close(); 
                
            }catch(HeadlessException | ClassNotFoundException | SQLException e){ System.out.println("Error: "+e);} 
    }
    
    public void NuevoUsuario(java.awt.event.ActionEvent evt) throws  ClassNotFoundException, SQLException{
        JFrame ventana3 = new JFrame();
        ventana3.setSize(400, 400);
        ventana3.setLocationRelativeTo(null);
        ventana3.getContentPane().setBackground(rojo_transparente);
        ventana3.setLayout(null);
                
                //panel 
        JPanel panel_centrado = new JPanel();
        panel_centrado.setBounds(50, 50, 280, 280);
        panel_centrado.setBackground(Color.white);
        panel_centrado.setLayout(null);
                
        JLabel etq_usuario = new JLabel("Nuevo Usuario");
        etq_usuario.setBounds(90, 20, 100, 40);
                
        JLabel etq_clave = new JLabel("Nueva COntraseña");
        etq_clave.setBounds(80, 120, 200, 40);
                
                //entradas               
        ent_usuario.setBounds(65, 60, 150, 40);
                
        ent_clave.setBounds(65, 160, 150, 40);
                
        JButton boton_cambiar = new JButton("Agregar Usuario");
        boton_cambiar.setBounds(65, 230, 150, 40);
        
            ventana3.add(panel_centrado);
            //etiquetas en el panel
            panel_centrado.add(etq_usuario);
            panel_centrado.add(etq_clave);
            //entradas en el panel
            panel_centrado.add(ent_usuario);
            panel_centrado.add(ent_clave);
            panel_centrado.add(boton_cambiar);
            
            ventana3.setVisible(true);
    }
}