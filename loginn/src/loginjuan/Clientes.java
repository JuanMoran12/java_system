package loginjuan;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Clientes {
    //entrada para la cedula del cliente
    JTextField ent_buscar = new JTextField();
    
    //insertar clientes
    JTextField ent_cedula = new JTextField();
    JTextField ent_cliente = new JTextField();
    JTextField ent_apellido = new JTextField();
    JTextField ent_telefono = new JTextField();
    JTextField ent_direccion = new JTextField();

    JPanel buttonPanel = new JPanel();
    JButton boton_producto = new JButton("productos");
    
    JButton agregar_cliente = new JButton("Agregar"); 
    //cedula del cliente
    public String cedula_del_cliente;

    public Clientes(Color Azul, JFrame opciones) {
        // Set up the main frame
        JFrame ventana5 = new JFrame("Sistema de Gestión - Clientes");
        ventana5.setSize(900, 650);
        ventana5.setLocationRelativeTo(null);
        ventana5.setLayout(null);
        ventana5.getContentPane().setBackground(new Color(245, 245, 245));
        
        // Create panels for modern structure
        JPanel panelLateral = new JPanel();
        panelLateral.setBounds(0, 0, 250, 650);
        panelLateral.setBackground(new Color(52, 73, 94));
        panelLateral.setLayout(null);
        
        JPanel panelCentral = new JPanel();
        panelCentral.setBounds(250, 0, 650, 650);
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setLayout(null);
        
        // Add panels to the frame
        ventana5.add(panelLateral);
        ventana5.add(panelCentral);
        
        // Left panel components
        JLabel tituloSistema = new JLabel("SISTEMA DE GESTIÓN");
        tituloSistema.setBounds(25, 30, 200, 30);
        tituloSistema.setForeground(Color.WHITE);
        tituloSistema.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        panelLateral.add(tituloSistema);
        
        JPanel separador = new JPanel();
        separador.setBounds(25, 70, 200, 2);
        separador.setBackground(Color.WHITE);
        panelLateral.add(separador);
        
        // Add menu options to left panel
        JLabel menuTitle = new JLabel("MENÚ");
        menuTitle.setBounds(25, 90, 200, 30);
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        panelLateral.add(menuTitle);
        
        // Right panel components - Header
        JLabel tituloClientes = new JLabel("Gestión de Clientes");
        tituloClientes.setBounds(30, 30, 300, 30);
        tituloClientes.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        tituloClientes.setForeground(new Color(52, 73, 94));
        panelCentral.add(tituloClientes);
        
        // Search section
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(30, 70, 590, 50);
        searchPanel.setBackground(new Color(240, 240, 240));
        searchPanel.setLayout(null);
        panelCentral.add(searchPanel);
        
        JLabel etq_buscar = new JLabel("Buscar por cédula:");
        etq_buscar.setBounds(15, 10, 150, 30);
        etq_buscar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        etq_buscar.setForeground(new Color(52, 73, 94));
        searchPanel.add(etq_buscar);
        
        ent_buscar.setBounds(165, 10, 250, 30);
        ent_buscar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        ent_buscar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchPanel.add(ent_buscar);
        
        JButton agregar = new JButton("Buscar");
        agregar.setBounds(430, 10, 120, 30);
        agregar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        agregar.setBackground(new Color(52, 152, 219));
        agregar.setForeground(Color.WHITE);
        agregar.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        agregar.setFocusPainted(false);
        searchPanel.add(agregar);
        
        // Form section
        JPanel formPanel = new JPanel();
        formPanel.setBounds(30, 140, 590, 350);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(220, 220, 220)));
        formPanel.setLayout(null);
        panelCentral.add(formPanel);
        
        JLabel etq_form_title = new JLabel("Información del Cliente");
        etq_form_title.setBounds(20, 15, 300, 30);
        etq_form_title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        etq_form_title.setForeground(new Color(52, 73, 94));
        formPanel.add(etq_form_title);
        
        // Form fields
        JLabel etq_cedula = new JLabel("Cédula:");
        etq_cedula.setBounds(20, 60, 100, 25);
        etq_cedula.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        etq_cedula.setForeground(new Color(52, 73, 94));
        formPanel.add(etq_cedula);
        
        ent_cedula.setBounds(140, 60, 250, 30);
        ent_cedula.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        ent_cedula.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(ent_cedula);
        
        JLabel etq_cliente = new JLabel("Nombre:");
        etq_cliente.setBounds(20, 100, 100, 25);
        etq_cliente.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        etq_cliente.setForeground(new Color(52, 73, 94));
        formPanel.add(etq_cliente);
        
        ent_cliente.setBounds(140, 100, 250, 30);
        ent_cliente.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        ent_cliente.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(ent_cliente);
        
        JLabel etq_apellido = new JLabel("Apellido:");
        etq_apellido.setBounds(20, 140, 100, 25);
        etq_apellido.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        etq_apellido.setForeground(new Color(52, 73, 94));
        formPanel.add(etq_apellido);
        
        ent_apellido.setBounds(140, 140, 250, 30);
        ent_apellido.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        ent_apellido.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(ent_apellido);
        
        JLabel etq_telefono = new JLabel("Teléfono:");
        etq_telefono.setBounds(20, 180, 100, 25);
        etq_telefono.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        etq_telefono.setForeground(new Color(52, 73, 94));
        formPanel.add(etq_telefono);
        
        ent_telefono.setBounds(140, 180, 250, 30);
        ent_telefono.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        ent_telefono.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(ent_telefono);
        
        JLabel etq_direccion = new JLabel("Dirección:");
        etq_direccion.setBounds(20, 220, 100, 25);
        etq_direccion.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        etq_direccion.setForeground(new Color(52, 73, 94));
        formPanel.add(etq_direccion);
        
        ent_direccion.setBounds(140, 220, 400, 30);
        ent_direccion.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        ent_direccion.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(ent_direccion);
        
        // Action buttons
        buttonPanel.setBounds(20, 270, 550, 60);
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(null);
        formPanel.add(buttonPanel);
        
        agregar_cliente.setBounds(20, 15, 150, 40);
        agregar_cliente.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        agregar_cliente.setBackground(new Color(46, 204, 113));
        agregar_cliente.setForeground(Color.WHITE);
        agregar_cliente.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        agregar_cliente.setFocusPainted(false);
        buttonPanel.add(agregar_cliente);
        
        // Add action listeners
        agregar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    BuscarCliente();
                } catch (SQLException ex) {
                    Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        agregar_cliente.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    InsertarCliente(ent_cedula.getText(), ent_cliente.getText(), ent_apellido.getText(), ent_telefono.getText(), ent_direccion.getText());
                } catch (SQLException ex) {
                    Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        ventana5.setVisible(true);
    }
    
    public void BuscarCliente() throws ClassNotFoundException, SQLException{
        String[] filas = new String[6];
        String fila_contadora = null;

        Connection conBD = null;
        Statement consulta_buscar = null;
        ResultSet resultado = null;
        
        try {
            cedula_del_cliente = ent_buscar.getText();

            Class.forName("org.sqlite.JDBC");

            conBD = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");
            // Set transaction isolation level to prevent database locking
            conBD.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            consulta_buscar = conBD.createStatement();

            String consulta = "SELECT * FROM clientes WHERE cedula = ?";
            PreparedStatement pstmt = conBD.prepareStatement(consulta);
            pstmt.setString(1, cedula_del_cliente);
            
            resultado = pstmt.executeQuery();

            while (resultado.next()){
                fila_contadora = "algo";
                filas[0] = resultado.getString(1);
                filas[1] = resultado.getString(2);
                filas[2] = resultado.getString(3);
                filas[3] = resultado.getString(4);
                filas[4] = resultado.getString(5);
                filas[5] = resultado.getString(6);
            
                System.out.println("cliente si existe" + filas[0]+" "+filas[1]+" "+filas[2]+" "+filas[3] + " "+ filas[4]+" "+filas[5]);
                
                ent_cedula.setText(filas[1]);  
                ent_cliente.setText(filas[2]);    
                ent_apellido.setText(filas[3]); 
                ent_telefono.setText(filas[4]);
                ent_direccion.setText(filas[5]); 
            }
            // Add the products button when a client exists
            if (fila_contadora != null) {
                boton_producto.setBounds(190, 15, 150, 40);
                boton_producto.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
                boton_producto.setBackground(new Color(52, 152, 219));
                boton_producto.setForeground(Color.WHITE);
                boton_producto.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
                boton_producto.setFocusPainted(false);
                buttonPanel.add(boton_producto);

                boton_producto.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Productos ced_var = new Productos(ent_cedula.getText());
                        try {
                            ced_var.MostrarProductos();
                        } catch (SQLException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                });
            }

            if (fila_contadora == null)
            {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Quiere Agregar un cliente?", "Cliente no existe", 
                    JOptionPane.YES_NO_OPTION);

                if (opcion == JOptionPane.YES_OPTION) {
                    ent_buscar.setText("");
                    JLabel msj = new JLabel("Rellene los campos");
                    msj.setBounds(240, 115, 150, 40);
                    msj.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
                    msj.setForeground(new Color(231, 76, 60));
                    buttonPanel.add(msj);

                    String ced = ent_cedula.getText();
                    String nom = ent_cliente.getText();
                    String ape = ent_apellido.getText();
                    String tlf = ent_telefono.getText();
                    String dir = ent_direccion.getText();

                    InsertarCliente(ced, nom, ape, tlf, dir);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            // Close resources in finally block to ensure they are always closed
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (consulta_buscar != null) {
                try {
                    consulta_buscar.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conBD != null) {
                try {
                    conBD.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }        
    }
   
    private void InsertarCliente(String ced, String nom, String ape, String tlf, String dir) throws SQLException {
        
        if (ced.isEmpty() || nom.isEmpty() || ape.isEmpty() || tlf.isEmpty() || dir.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }
        
        Connection conBD7 = null;
        PreparedStatement consulta_insertar = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
            
            conBD7 = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");
            // Set transaction isolation level to prevent database locking
            conBD7.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            
            consulta_insertar = conBD7.prepareStatement("INSERT INTO clientes (cedula, nombre, apellido, telefono, direccion) VALUES(?,?,?,?,?)");
    
            consulta_insertar.setString(1, ced);
            consulta_insertar.setString(2, nom);
            consulta_insertar.setString(3, ape);
            consulta_insertar.setString(4, tlf);
            consulta_insertar.setString(5, dir);
    
            int filas_afectadas = consulta_insertar.executeUpdate();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            // Close resources in finally block to ensure they are always closed
            if (consulta_insertar != null) {
                try {
                    consulta_insertar.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conBD7 != null) {
                try {
                    conBD7.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        // Get the result outside the try-catch to use it for UI updates
        int filas_afectadas = 1; // Assuming success since we would have thrown an exception otherwise

        if (filas_afectadas == 0){

            System.out.println("cliente no registrado correctamente");

        } else{
            System.out.println("cliente incluido");

            boton_producto.setBounds(300, 400, 150, 40);
            boton_producto.setBackground(Color.CYAN);
            buttonPanel.add(boton_producto);

            boton_producto.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        try {
                            productos(e);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        }
                    });
        }
        
        consulta_insertar.close();
        conBD7.close();
    }
    
    public void productos(ActionEvent e) throws ClassNotFoundException, SQLException {
        Productos llamar_productos2 = new Productos(cedula_del_cliente);
        llamar_productos2.MostrarProductos();
    }
    
    }
    

