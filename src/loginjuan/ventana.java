/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginjuan;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

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
        login.setSize(1000, 700);
        login.setLocationRelativeTo(null);
        login.setLayout(null);
        login.getContentPane().setBackground(new Color(240, 242, 245));
        
        // Panel lateral para el menú
        JPanel panelLateral = new JPanel();
        panelLateral.setBounds(0, 0, 350, 700);
        panelLateral.setBackground(new Color(52, 73, 94));
        panelLateral.setLayout(null);
        login.add(panelLateral);
        
        // Panel central para el contenido
        JPanel panelCentral = new JPanel();
        panelCentral.setBounds(350, 0, 650, 700);
        panelCentral.setBackground(new Color(245, 247, 250));
        panelCentral.setLayout(null);
        login.add(panelCentral);
        
        // Logo y título en el panel lateral
        JLabel iconLabel = new JLabel("🔒");
        iconLabel.setBounds(30, 100, 60, 60);
        iconLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 40));
        iconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelLateral.add(iconLabel);
        
        JLabel etqTitulo = new JLabel("Bienvenido");
        etqTitulo.setBounds(30, 170, 290, 40);
        etqTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 28));
        etqTitulo.setForeground(Color.WHITE);
        panelLateral.add(etqTitulo);
        
        JLabel subtitulo = new JLabel("Inicia sesión para continuar");
        subtitulo.setBounds(30, 210, 290, 25);
        subtitulo.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        subtitulo.setForeground(new Color(200, 200, 200));
        panelLateral.add(subtitulo);
        
        // Tarjeta de inicio de sesión con bordes redondeados
        RoundedPanel loginCard = new RoundedPanel(24);
        loginCard.setBounds(100, 150, 450, 400);
        loginCard.setBackground(Color.WHITE);
        loginCard.setLayout(null);
        loginCard.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        panelCentral.add(loginCard);
        
        // Título del formulario
        JLabel loginTitle = new JLabel("Iniciar Sesión");
        loginTitle.setBounds(30, 10, 390, 40);
        loginTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        loginTitle.setForeground(new Color(52, 73, 94));
        loginCard.add(loginTitle);
        
        // Campo de usuario
        JLabel etq = new JLabel("Usuario");
        etq.setBounds(30, 70, 300, 25);
        etq.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        etq.setForeground(new Color(80, 80, 80));
        loginCard.add(etq);
        
        ent.setBounds(30, 95, 390, 45);
        ent.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 15));
        ent.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        loginCard.add(ent);
        
        // Campo de contraseña
        JLabel etq2 = new JLabel("Contraseña");
        etq2.setBounds(30, 160, 300, 25);
        etq2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        etq2.setForeground(new Color(80, 80, 80));
        loginCard.add(etq2);
        
        ent2.setBounds(30, 185, 390, 45);
        ent2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 15));
        ent2.setEchoChar('•');
        ent2.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        loginCard.add(ent2);
        
        // Recordar usuario
        JCheckBox rememberMe = new JCheckBox("Recordar mi usuario");
        rememberMe.setBounds(30, 245, 200, 25);
        rememberMe.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        rememberMe.setBackground(Color.WHITE);
        rememberMe.setFocusPainted(false);
        //loginCard.add(rememberMe);
        
        RoundButton LeeButton = new RoundButton("Iniciar Sesión", 18);
        LeeButton.setBounds(30, 290, 390, 50);
        LeeButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 15));
        LeeButton.setForeground(Color.WHITE);
        LeeButton.setBackground(new Color(52, 152, 219));
        LeeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        LeeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                LeeButton.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                LeeButton.setBackground(new Color(52, 152, 219));
            }
        });
        
        loginCard.add(LeeButton);
        
        // Enlace para recuperar contraseña
        JLabel forgotPassword = new JLabel("¿Olvidaste tu contraseña?");
        forgotPassword.setBounds(30, 350, 390, 20);
        forgotPassword.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        forgotPassword.setForeground(new Color(52, 152, 219));
        forgotPassword.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        forgotPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        //loginCard.add(forgotPassword);
        
        // Pie de página en el panel lateral
        JPanel footerPanel = new JPanel();
        footerPanel.setBounds(30, 650, 350, 50);
        footerPanel.setBackground(new Color(45, 62, 80));
        footerPanel.setLayout(new java.awt.BorderLayout());
        
        JLabel copyrightLabel = new JLabel(" 2023 Sistema de Gestión - Versión 1.0");
        copyrightLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
        copyrightLabel.setForeground(new Color(150, 150, 150));
        copyrightLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        footerPanel.add(copyrightLabel, java.awt.BorderLayout.CENTER);
        
        panelLateral.add(footerPanel);
        
        // Acción del botón de login
        LeeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    LeeButtonEjecuta(evt);
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

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

// Panel con esquinas redondeadas reutilizable para una UI más moderna
class RoundedPanel extends JPanel {
    private final int cornerRadius;

    public RoundedPanel(int cornerRadius) {
        super();
        this.cornerRadius = Math.max(0, cornerRadius);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius * 2, cornerRadius * 2);
        g2.dispose();
        super.paintComponent(g);
    }
}

// Botón con fondo redondeado
class RoundButton extends JButton {
    private final int cornerRadius;

    public RoundButton(String text, int cornerRadius) {
        super(text);
        this.cornerRadius = Math.max(0, cornerRadius);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    public void paintBorder(Graphics g) {
        // Sin borde para un look limpio
    }
}



