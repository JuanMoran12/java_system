package loginjuan;

import java.awt.Color;
import java.awt.Font;
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

    private JTextField ent_buscar;
    private JTextField ent_cedula;
    private JTextField ent_cliente;
    private JTextField ent_apellido;
    private JTextField ent_telefono;
    private JTextField ent_direccion;

    // Componentes de la interfaz
    private JPanel buttonPanel;
    private JButton boton_producto;
    private JButton agregar_cliente;
    private JFrame ventana5;  // Mover la declaración de la ventana al nivel de clase
    
    // Datos del cliente
    public String cedula_del_cliente;

    // Método auxiliar para crear botones con estilo consistente
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
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
    
    // Método para crear campos de texto con estilo
    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(220, 220, 220)),
            javax.swing.BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return textField;
    }
    
    // Método para crear etiquetas
    private JLabel createLabel(String text, int x, int y, int width, int height, int style) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(new Font("Arial", style, 14));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    public Clientes(Color Azul) {
        // Configuración de la ventana principal
        ventana5 = new JFrame("Sistema de Gestión - Clientes");
        ventana5.setSize(1000, 700);
        ventana5.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana5.setLocationRelativeTo(null);
        ventana5.setLayout(null);
        ventana5.getContentPane().setBackground(new Color(245, 247, 250));
        
        // Panel lateral
        JPanel panelLateral = new JPanel();
        panelLateral.setBounds(0, 0, 350, 700);
        panelLateral.setBackground(new Color(52, 73, 94));
        panelLateral.setLayout(null);
        
        // Logo y título
        JLabel iconLabel = new JLabel("👥");
        iconLabel.setBounds(30, 30, 60, 60);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        iconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelLateral.add(iconLabel);
        
        JLabel tituloSistema = new JLabel("Sistema de Gestión");
        tituloSistema.setBounds(30, 100, 290, 40);
        tituloSistema.setForeground(Color.WHITE);
        tituloSistema.setFont(new Font("Arial", Font.BOLD, 24));
        panelLateral.add(tituloSistema);
        
        // Botón para volver al menú de opciones
        JButton btnVolver = createButton("← Volver al Menú", 30, 170, 290, 40);
        btnVolver.setBackground(new Color(41, 128, 185));
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana5.dispose();
                Opciones opciones = new Opciones(new String[]{"", ""}, new Color(52, 73, 94), new Color(52, 152, 219));
            }
        });
        //panelLateral.add(btnVolver);
        
        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setBounds(350, 0, 650, 700);
        panelCentral.setBackground(new Color(245, 247, 250));
        panelCentral.setLayout(null);
        
        // Tarjeta de contenido
        JPanel card = new JPanel();
        card.setBounds(50, 50, 550, 600);
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(230, 230, 230)),
            javax.swing.BorderFactory.createEmptyBorder(20, 30, 30, 30)
        ));
        
        // Título de la sección
        JLabel tituloClientes = new JLabel("Gestión de Clientes");
        tituloClientes.setBounds(30, 20, 490, 40);
        tituloClientes.setFont(new Font("Arial", Font.BOLD, 24));
        tituloClientes.setForeground(new Color(52, 73, 94));
        card.add(tituloClientes);
        
        // Sección de búsqueda
        JPanel searchPanel = new JPanel();
        searchPanel.setBounds(30, 80, 490, 60);
        searchPanel.setBackground(new Color(245, 249, 252));
        searchPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(230, 236, 240)));
        searchPanel.setLayout(null);
        
        JLabel etq_buscar = createLabel("Buscar por cédula:", 20, 15, 150, 30, Font.PLAIN);
        searchPanel.add(etq_buscar);
        
        ent_buscar = createTextField(150, 15, 200, 30);
        searchPanel.add(ent_buscar);
        
        JButton btnBuscar = createButton("Buscar", 370, 15, 100, 30);
        searchPanel.add(btnBuscar);
        
        card.add(searchPanel);
        
        // Formulario de cliente
        JPanel formPanel = new JPanel();
        formPanel.setBounds(30, 170, 490, 380);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(230, 236, 240)));
        formPanel.setLayout(null);
        
        JLabel etq_form_title = new JLabel("Información del Cliente");
        etq_form_title.setBounds(20, 15, 450, 30);
        etq_form_title.setFont(new Font("Arial", Font.BOLD, 18));
        etq_form_title.setForeground(new Color(52, 73, 94));
        formPanel.add(etq_form_title);        
        
        // Campos del formulario
        JLabel etq_cedula = createLabel("Cédula:", 20, 60, 100, 25, Font.PLAIN);
        formPanel.add(etq_cedula);
        
        ent_cedula = createTextField(140, 55, 300, 35);
        formPanel.add(ent_cedula);
        
        JLabel etq_cliente = createLabel("Nombres:", 20, 110, 100, 25, Font.PLAIN);
        formPanel.add(etq_cliente);
        
        ent_cliente = createTextField(140, 105, 300, 35);
        formPanel.add(ent_cliente);
        
        JLabel etq_apellido = createLabel("Apellidos:", 20, 160, 100, 25, Font.PLAIN);
        formPanel.add(etq_apellido);
        
        ent_apellido = createTextField(140, 155, 300, 35);
        formPanel.add(ent_apellido);
        
        JLabel etq_telefono = createLabel("Teléfono:", 20, 210, 100, 25, Font.PLAIN);
        formPanel.add(etq_telefono);
        
        ent_telefono = createTextField(140, 205, 300, 35);
        formPanel.add(ent_telefono);
        
        JLabel etq_direccion = createLabel("Dirección:", 20, 260, 100, 25, Font.PLAIN);
        formPanel.add(etq_direccion);
        
        ent_direccion = createTextField(140, 255, 300, 35);
        formPanel.add(ent_direccion);
        
        // Botones de acción
        buttonPanel = new JPanel();
        buttonPanel.setBounds(20, 310, 450, 50);
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(null);
        
        agregar_cliente = createButton("Agregar Cliente", 0, 0, 150, 40);
        buttonPanel.add(agregar_cliente);
        
        // Inicializar el botón de productos (inicialmente oculto)
        boton_producto = createButton("Ver Productos", 160, 0, 150, 40);
        boton_producto.setVisible(false);
        buttonPanel.add(boton_producto);
        
        formPanel.add(buttonPanel);
        card.add(formPanel);
        
        // Agregar componentes principales
        panelCentral.add(card);
        ventana5.add(panelLateral);
        ventana5.add(panelCentral);
        agregar_cliente.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        agregar_cliente.setBackground(new Color(46, 204, 113));
        agregar_cliente.setForeground(Color.WHITE);
        agregar_cliente.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        agregar_cliente.setFocusPainted(false);
        buttonPanel.add(agregar_cliente);
        
        // Add action listeners
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
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
                
                // Mostrar el botón de productos
                boton_producto.setVisible(true);
                boton_producto.repaint();

                boton_producto.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //String nombre_cliente = ent_cliente.getText();
                        Productos ced_var = new Productos(ent_cedula.getText(), ent_cliente.getText());
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
                // Ocultar el botón de productos si el cliente no existe
                boton_producto.setVisible(false);
                
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
        
        int filas_afectadas = 1; 

        if (filas_afectadas == 0){

            System.out.println("cliente no registrado correctamente");

        } else{
            System.out.println("cliente incluido");

            this.cedula_del_cliente = ced;

            boton_producto.setVisible(true);

            if (boton_producto.getActionListeners().length == 0) {
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
            
            boton_producto.repaint();
            buttonPanel.revalidate();
            buttonPanel.repaint();
            ventana5.revalidate();
            ventana5.repaint();
        }
    }
    
    public void productos(ActionEvent e) throws ClassNotFoundException, SQLException {
        String nombre_cliente = ent_cliente.getText();
        Productos llamar_productos2 = new Productos(cedula_del_cliente, nombre_cliente);
        llamar_productos2.MostrarProductos();
    }
    
    }
