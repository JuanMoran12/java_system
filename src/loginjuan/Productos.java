
package loginjuan;

import java.awt.Color;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lenovo
 */
class Productos {
    JPanel panelCentral = new JPanel();
    Color rojo_transparente = new Color(22, 109, 99); 
    Color azul_oscuro = new Color(40, 53, 96);
    Color azul_claro = new Color(176, 206, 255);
    Color grios_oscuro = new Color(65, 71, 78);
    Color gris_azul = new Color(51, 63, 85);
    Color color_principal = new Color(52, 73, 94);
    Color color_secundario = new Color(245, 245, 245);
    
    JTextField ent_nom = new JTextField();
    JTextField ent_pre = new JTextField();       
    JTextField ent_exi = new JTextField();
    JTextField ent_codigo = new JTextField();
    
    DefaultTableModel modelo = new DefaultTableModel();
    
    JTable tabla = new JTable(modelo);
    
    JFrame ventana6 = new JFrame("Sistema de Gestión - Productos");
    private JFrame parentWindow;  
    
    JTextField ent_cant = new JTextField();
    
    public String cantidad;
    public String leer_nom;
    public String codigo_prod;
    
    //cedula del cliente
    public String var_ced;
    public String var_nom;
    
    DefaultTableModel modelo2 = new DefaultTableModel();
    JTable tabla2 = new JTable(modelo2);
    JScrollPane scrollPane2 = new JScrollPane(tabla2);

    //variable para insertar por click
    String nombre = new String();
    String precio = new String();
    String stock = new String();
    String id = new String();
   
    String variable_insertar = new String();
    String variable_insertar2 = new String();
    String variable_eliminar = new String();
    
    //ultima tabla
    DefaultTableModel mod_prod = new DefaultTableModel();
    JTable tabla_prod = new JTable(mod_prod);
    JScrollPane scroll_prod = new JScrollPane(tabla_prod);//es la barra pa deslizar
    
    //variables de la ultima tabla
    String id_prod = new String();
    String nombrecito = new String();
    String prec = new String();  
    
    //precio total
    JTextField ent_precio_cantidad = new JTextField();
    
    String ced_carrito = new String();
    String cod_carrito = new String();   
    
    ArrayList<Double> listaPrecios = new ArrayList<>();
    ArrayList<Double> listaSubtotal = new ArrayList<>();
    ArrayList<Double> listaImpuesto = new ArrayList<>();
    ArrayList<Integer> listaCantidades = new ArrayList<>();
    ArrayList<Double> listaProductos = new ArrayList<>();
    ArrayList<String> listaNombre = new ArrayList<>();

    public Productos(String cedula_del_cliente, String nombre_del_cliente) {
        this.parentWindow = parentWindow;  // Guardar referencia a la ventana padre
        
        var_ced = cedula_del_cliente;   
        var_nom = nombre_del_cliente;    
        System.out.println(var_ced);
                
        ventana6.setSize(1000, 700);  // Aumentar el tamaño para coincidir con las otras ventanas
        ventana6.setLocationRelativeTo(null);
        ventana6.setLayout(null);
        ventana6.getContentPane().setBackground(new Color(245, 245, 245));
        
        JPanel panelLateral = new JPanel();
        panelLateral.setBounds(0, 0, 300, 700);  // Hacer el panel más angosto
        panelLateral.setBackground(new Color(52, 73, 94));
        panelLateral.setLayout(null);
        
        JLabel tituloSistema = new JLabel("Gestión de Productos");
        tituloSistema.setBounds(20, 20, 260, 30);
        tituloSistema.setForeground(Color.WHITE);
        tituloSistema.setFont(new Font("Arial", Font.BOLD, 18));
        panelLateral.add(tituloSistema);
        
        ventana6.add(panelLateral);
        
        panelCentral.setBounds(300, 0, 700, 700);  // Ajustar para el panel lateral más angosto
        panelCentral.setBackground(new Color(245, 247, 250));
        panelCentral.setLayout(null);
        ventana6.add(panelCentral);
        
        // Mostrar la información del cliente
        JLabel etq_cliente_actual = new JLabel("Cliente: " + nombre_del_cliente);
        etq_cliente_actual.setBounds(20, 60, 260, 20);
        etq_cliente_actual.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        etq_cliente_actual.setForeground(Color.WHITE);
        panelLateral.add(etq_cliente_actual);
        
        JLabel etq_cedula = new JLabel("Cédula: " + cedula_del_cliente);
        etq_cedula.setBounds(20, 80, 260, 20);
        etq_cedula.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
        etq_cedula.setForeground(new Color(200, 200, 200));
        panelLateral.add(etq_cedula);
        
        // Widgets del formulario
        int yPos = 120;  // Posición Y inicial
        int fieldHeight = 25;  // Altura de los campos
        int labelSpacing = 5;  // Espacio entre etiquetas y campos
        int fieldSpacing = 40;  // Espacio entre grupos de campos
        
        // Campo Producto
        JLabel etq_nom = new JLabel("Producto");
        etq_nom.setBounds(20, yPos, 260, 20);
        etq_nom.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        etq_nom.setForeground(Color.WHITE);
        panelLateral.add(etq_nom);
        
        ent_nom.setBounds(20, yPos + 20, 260, fieldHeight);
        ent_nom.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        ent_nom.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        panelLateral.add(ent_nom);
        
        // Campo Código
        yPos += fieldHeight + fieldSpacing;
        JLabel etq_codigo = new JLabel("Código");
        etq_codigo.setBounds(20, yPos, 260, 20);
        etq_codigo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        etq_codigo.setForeground(Color.WHITE);
        panelLateral.add(etq_codigo);
        
        ent_codigo.setBounds(20, yPos + 20, 260, fieldHeight);
        ent_codigo.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        ent_codigo.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        panelLateral.add(ent_codigo);
        
        // Campo Precio
        yPos += fieldHeight + fieldSpacing;
        JLabel etq_pre = new JLabel("Precio");
        etq_pre.setBounds(20, yPos, 260, 20);
        etq_pre.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        etq_pre.setForeground(Color.WHITE);
        panelLateral.add(etq_pre);
        
        ent_pre.setBounds(20, yPos + 20, 260, fieldHeight);
        ent_pre.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        ent_pre.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        panelLateral.add(ent_pre);
        
        // Campo Existencias
        yPos += fieldHeight + fieldSpacing;
        JLabel etq_exi = new JLabel("Existencias");
        etq_exi.setBounds(20, yPos, 260, 20);
        etq_exi.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        etq_exi.setForeground(Color.WHITE);
        panelLateral.add(etq_exi);
        
        ent_exi.setBounds(20, yPos + 20, 260, fieldHeight);
        ent_exi.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        ent_exi.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        panelLateral.add(ent_exi);
        
        // Botón Buscar Producto
        yPos += fieldHeight + fieldSpacing + 10;
        JButton buscar_prod = new JButton("Buscar");
        buscar_prod.setBounds(20, yPos, 260, 35);
        buscar_prod.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        buscar_prod.setBackground(new Color(46, 204, 113));
        buscar_prod.setForeground(Color.WHITE);
        buscar_prod.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buscar_prod.setFocusPainted(false);
        buscar_prod.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        // Efecto hover
        buscar_prod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buscar_prod.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buscar_prod.setBackground(new Color(46, 204, 113));
            }
        });
        panelLateral.add(buscar_prod);
        
        buscar_prod.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //metodo para leer productos
                    LeerProductos();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        // Sección de cantidad y carrito
        yPos += 45;  // Espacio después del botón buscar
        JLabel etq_cant = new JLabel("Cantidad");
        etq_cant.setBounds(20, yPos, 260, 20);
        etq_cant.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        etq_cant.setForeground(Color.WHITE);
        panelLateral.add(etq_cant);
        
        ent_cant.setBounds(20, yPos + 20, 120, fieldHeight);
        ent_cant.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        ent_cant.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        panelLateral.add(ent_cant);
        
        // Botón Agregar al Carrito
        JButton boton_aceptar = new JButton("Agregar");
        boton_aceptar.setBounds(150, yPos + 18, 130, 30);
        boton_aceptar.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        boton_aceptar.setBackground(new Color(52, 152, 219));
        boton_aceptar.setForeground(Color.WHITE);
        boton_aceptar.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        boton_aceptar.setFocusPainted(false);
        boton_aceptar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        // Efecto hover
        boton_aceptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton_aceptar.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton_aceptar.setBackground(new Color(52, 152, 219));
            }
        });
        panelLateral.add(boton_aceptar);

        boton_aceptar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AceptarCantidad();
            }
        });
        
        modelo2.addColumn("Cédula cliente");
        modelo2.addColumn("Código");
        modelo2.addColumn("Cantidad");
        
        // Components are already added to their respective panels
        // No need to add them directly to ventana6
        ventana6.setVisible(true);
            }
   
    public void LeerProductos() throws ClassNotFoundException, SQLException{
        
        System.out.println("aqui leo los producto");
        
        String[] filas2 = new String[4];
        String leer_filas = null;
        
        Connection conBD2 = null;
        PreparedStatement pstmt = null;
        ResultSet resultado_prod = null;
        
        try {
            leer_nom = ent_nom.getText();
            
            Class.forName("org.sqlite.JDBC");

            //clase connection para conectar con la url
            conBD2 = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");
            conBD2.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            // Configurar para evitar bloqueos
            conBD2.createStatement().execute("PRAGMA busy_timeout = 5000;");
            conBD2.createStatement().execute("PRAGMA journal_mode = WAL;");

            // Use parameterized query to prevent SQL injection
            String consulta2 = "SELECT * FROM productos WHERE nom_prod = ?";
            pstmt = conBD2.prepareStatement(consulta2);
            pstmt.setString(1, leer_nom);
            
            resultado_prod = pstmt.executeQuery();
            
            while(resultado_prod.next()){
                
                leer_filas = "algoooooo";
                
                filas2[0] = resultado_prod.getString(1);
                filas2[1] = resultado_prod.getString(2);
                filas2[2] = resultado_prod.getString(3);
                filas2[3] = resultado_prod.getString(4);
                
                System.out.println(filas2[0] + " " + filas2[1] + " " + filas2[2]+ " "+ filas2[3]);
                System.out.println("si existe el producto");
                
                codigo_prod = filas2[0];
                
                if (leer_filas != null){
                    
                    ent_codigo.setText(codigo_prod);
                    ent_pre.setText(filas2[2]);
                    ent_exi.setText(filas2[3]);

                    JOptionPane.showMessageDialog(null, "Producto no encontrado");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            // Close resources in finally block to ensure they are always closed
            if (resultado_prod != null) {
                try {
                    resultado_prod.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conBD2 != null) {
                try {
                    conBD2.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void MostrarProductos() throws SQLException {
        String[] filas2 = new String[4];
        Connection conBD3 = null;
        Statement consulta_buscar2 = null;
        ResultSet resultado_mostrar = null;

        try {
            // Clear any existing components in the panel
            panelCentral.removeAll();
            
            // Add a title to the panel
            JLabel titleLabel = new JLabel("Catálogo de Productos");
            titleLabel.setBounds(30, 15, 300, 30);
            titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
            titleLabel.setForeground(color_principal);
            panelCentral.add(titleLabel);
            
            // Configuración del modelo de tabla con estilo mejorado
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Precio");
            modelo.addColumn("Stock");

            Class.forName("org.sqlite.JDBC");
            conBD3 = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");
            conBD3.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            consulta_buscar2 = conBD3.createStatement();
            String consulta3 = "SELECT * FROM productos";
            resultado_mostrar = consulta_buscar2.executeQuery(consulta3);

            while (resultado_mostrar.next()) {
                filas2[0] = resultado_mostrar.getString(1);
                filas2[1] = resultado_mostrar.getString(2);
                filas2[2] = resultado_mostrar.getString(3);
                filas2[3] = resultado_mostrar.getString(4);
                modelo.addRow(filas2);
            }
            
            // Tabla con estilo mejorado
            tabla.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
            tabla.setRowHeight(25);
            tabla.getTableHeader().setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
            tabla.getTableHeader().setBackground(color_principal);
            tabla.getTableHeader().setForeground(color_secundario);
            tabla.setGridColor(new Color(230, 230, 230));
            tabla.setSelectionBackground(new Color(213, 245, 227));
            
            // Create a scroll pane for the table
            JScrollPane scrollPane = new JScrollPane(tabla);
            scrollPane.setBounds(30, 50, 590, 300);
            scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
            panelCentral.add(scrollPane);
            
            // Add instructions label
            JLabel instructionsLabel = new JLabel("Seleccione un producto y especifique la cantidad para agregar al carrito");
            instructionsLabel.setBounds(30, 360, 590, 25);
            instructionsLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 14));
            instructionsLabel.setForeground(new Color(52, 73, 94));
            panelCentral.add(instructionsLabel);
            
            // Force panel to repaint
            panelCentral.revalidate();
            panelCentral.repaint();
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error al cargar productos: " + ex.getMessage());
        } finally {
            // Close resources in finally block to ensure they are always closed
            if (resultado_mostrar != null) try { resultado_mostrar.close(); } catch (SQLException ex) { }
            if (consulta_buscar2 != null) try { consulta_buscar2.close(); } catch (SQLException ex) { }
            if (conBD3 != null) try { conBD3.close(); } catch (SQLException ex) { }
        }
        
        // Add MouseListener to the table if it exists
        if (tabla != null) {
            tabla.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e) {
                        PonerFilas();
                    }
                });
            };
        //ventana6.setSize(700, 400);
        ventana6.setVisible(true);     
        
        conBD3.close();
        consulta_buscar2.close();    
        }

public void PonerFilas() {
    int fila_seleccionada = tabla.getSelectedRow();//metodo para seleccionar fila, devuelve -1 sino es seleccionada 

    if (fila_seleccionada != -1) {

        id = tabla.getValueAt(fila_seleccionada, 0).toString();
        nombre = tabla.getValueAt(fila_seleccionada, 1).toString();
        precio = tabla.getValueAt(fila_seleccionada, 2).toString();
        stock = tabla.getValueAt(fila_seleccionada, 3).toString();

        ent_codigo.setText(id);
        ent_nom.setText(nombre);
        ent_pre.setText(precio);
        ent_exi.setText(stock);
    }
}

public void AceptarCantidad(){
        String cant = ent_cant.getText(); 
        String nuevo_cod = ent_codigo.getText();
        System.err.println(""  + var_ced + " " + nuevo_cod + " " + cant);

        if (var_ced.isEmpty() && nuevo_cod.isEmpty() && cant.isEmpty()){
            JOptionPane.showMessageDialog(null, "Por favor, ingrese los datos.");
            return;
        }
        
        try {
            insertarEnCarrito(var_ced, nuevo_cod, cant);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
    
public void insertarEnCarrito(String cedula, String codigoProducto, String cantidad) throws SQLException, ClassNotFoundException {
    
    // Panel para contener los botones y el combo box
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(null);
    buttonPanel.setBounds(30, 570, 640, 60);
    buttonPanel.setOpaque(false); // Hacer el panel transparente
    
    // Botón Eliminar más pequeño
    JButton boton_eliminar = new JButton("Eliminar");
    boton_eliminar.setBounds(0, 10, 150, 35);
    boton_eliminar.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
    boton_eliminar.setBackground(new Color(231, 76, 60)); // Rojo
    boton_eliminar.setForeground(Color.WHITE);
    boton_eliminar.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
    boton_eliminar.setFocusPainted(false);
    boton_eliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    
    // Efecto hover para el botón eliminar
    boton_eliminar.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            boton_eliminar.setBackground(new Color(192, 57, 43));
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            boton_eliminar.setBackground(new Color(231, 76, 60));
        }
    });
    
    boton_eliminar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Eliminar();
            tabla2.revalidate();
            tabla2.repaint();
        }
    });
    
    // Combo box para métodos de pago
    JLabel etq_metodo_pago = new JLabel("Método de pago:");
    etq_metodo_pago.setBounds(160, 0, 120, 20);
    etq_metodo_pago.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
    etq_metodo_pago.setForeground(new Color(70, 70, 70));
    
    JComboBox<String> comboMetodoPago = new JComboBox<>();
    comboMetodoPago.setBounds(160, 20, 170, 30);
    comboMetodoPago.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
    comboMetodoPago.addItem("Efectivo");
    comboMetodoPago.addItem("Tarjeta de Crédito");
    comboMetodoPago.addItem("Tarjeta de Débito");
    comboMetodoPago.addItem("Transferencia");
    comboMetodoPago.setSelectedIndex(0);
    
    // Botón Pagar más pequeño
    JButton boton_pagar = new JButton("Pagar");
    boton_pagar.setBounds(340, 10, 150, 35);
    boton_pagar.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
    boton_pagar.setBackground(new Color(46, 204, 113)); // Verde
    boton_pagar.setForeground(Color.WHITE);
    boton_pagar.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
    boton_pagar.setFocusPainted(false);
    boton_pagar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    
    // Efecto hover para el botón pagar
    boton_pagar.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            boton_pagar.setBackground(new Color(39, 174, 96));
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            boton_pagar.setBackground(new Color(46, 204, 113));
        }
    });

    // Agregar componentes al panel
    buttonPanel.add(boton_eliminar);
    buttonPanel.add(etq_metodo_pago);
    buttonPanel.add(comboMetodoPago);
    buttonPanel.add(boton_pagar);
    
    // Agregar el panel al panel central
    panelCentral.add(buttonPanel);
    panelCentral.revalidate();
    panelCentral.repaint();

    boton_pagar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String metodoPago = (String)comboMetodoPago.getSelectedItem();
            // Aquí puedes usar el método de pago seleccionado si es necesario
            // Por ejemplo, pasarlo a la función SumarFacturar()
            SumarFacturar(metodoPago);
        }
    });
    
    System.out.println("cedula: "+ cedula + " codigo: " + codigoProducto + " cantidad: "+ cantidad);

    Connection con_cant = null;
    PreparedStatement int_cant = null;
    PreparedStatement pst_producto = null;
    ResultSet rs_producto = null;

    if (cantidad.isEmpty()){
        JOptionPane.showMessageDialog(null, "Por favor, ingrese una cantidad.");
        return;
    }
    // Configurar la tabla del carrito en el método AceptarCantidad
    scrollPane2.setBounds(30, 400, 590, 150);
    scrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(
        javax.swing.BorderFactory.createLineBorder(new Color(52, 73, 94), 1),
        "Carrito de Compras",
        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
        javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14),
        new Color(52, 73, 94)));
    
    // Mejorar el estilo de la tabla del carrito
    tabla2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
    tabla2.setRowHeight(25);
    tabla2.getTableHeader().setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
    tabla2.getTableHeader().setBackground(color_principal);
    tabla2.getTableHeader().setForeground(color_secundario);
    tabla2.setGridColor(new Color(230, 230, 230));
    tabla2.setSelectionBackground(new Color(213, 245, 227));
    
    // Agregar la tabla al panel central en lugar de a la ventana directamente
    panelCentral.add(scrollPane2);
    
    try {
        //String pre = ent_pre.getText();
        //SumarFacturar(pre, cantidad);

        Double subtotal = Double.parseDouble(precio) * Integer.parseInt(cantidad);
        Double impuesto = subtotal * 0.16;
        Double total = subtotal + impuesto;
        
        listaPrecios.add(Double.parseDouble(precio));
        listaCantidades.add(Integer.parseInt(cantidad));
        listaProductos.add(total);
        listaNombre.add(ent_nom.getText());
        System.out.println("producto agregado a la lista " + total + "lista: " + listaProductos);

        Class.forName("org.sqlite.JDBC");
        // Corregir la ruta de la base de datos para asegurar que sea la correcta
        con_cant = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");
        
        // Configurar para evitar bloqueos de base de datos con tiempos de espera más largos
        // IMPORTANTE: Configurar PRAGMA antes de cambiar autoCommit
        Statement stmt = con_cant.createStatement();
        stmt.execute("PRAGMA busy_timeout = 10000;"); // Aumentar el tiempo de espera a 10 segundos
        stmt.execute("PRAGMA journal_mode = WAL;"); // Write-Ahead Logging para mejor concurrencia
        stmt.execute("PRAGMA synchronous = NORMAL;"); // Reducir la sincronización con el disco
        stmt.execute("PRAGMA locking_mode = NORMAL;"); // Optimizar el manejo de bloqueos
        stmt.execute("PRAGMA cache_size = 5000;"); // Aumentar el tamaño de la caché
        stmt.close();
        
        // Configuraciones mejoradas para evitar bloqueos de base de datos
        con_cant.setAutoCommit(false); // Desactivar autocommit para manejar la transacción manualmente

        int_cant = con_cant.prepareStatement("INSERT INTO carrito(car_ced, car_pro, car_can) VALUES(?,?,?)");
    
        int_cant.setString(1, cedula);
        int_cant.setString(2, codigoProducto);
        int_cant.setString(3, cantidad);
    
        int filas_actualizadas = int_cant.executeUpdate();
        con_cant.commit(); // Confirmar la transacción
        
        if (filas_actualizadas > 0) {
            String[] filas3 = new String[3];
            filas3[0] = cedula;
            //filas3[1] = codigoProducto + " - " + ent_nom.getText();
            filas3[1] = codigoProducto;
            filas3[2] = cantidad;
            
            if (modelo2.getColumnCount() == 0) {
                modelo2.addColumn("Cliente");
                modelo2.addColumn("Producto");
                modelo2.addColumn("Cantidad");
            }
            
            modelo2.addRow(filas3);
            tabla2.setModel(modelo2);

            ent_cant.setText("");
            ent_nom.setText("");
            ent_codigo.setText("");
            ent_pre.setText("");
            ent_exi.setText("");
            
            codigo_prod = ""; 
            
            tabla2.revalidate();
            tabla2.repaint();
            
            System.out.println("Producto agregado al carrito y a la tabla visual");

        } else {
            System.out.println("Registro mal agregado");
            JOptionPane.showMessageDialog(null, "Error al agregar el producto al carrito.");
        } 
    } catch (SQLException ex) {
        if (con_cant != null) {
            try {
                con_cant.rollback();
            } catch (SQLException e) {
                Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, "Error al hacer rollback", e);
            }
        }
        Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Error de base de datos: " + ex.getMessage());
    } finally {
        // Always close resources in finally block
        if (rs_producto != null) try { rs_producto.close(); } catch (SQLException e1) { }
        if (pst_producto != null) try { pst_producto.close(); } catch (SQLException e1) { }
        if (int_cant != null) try { int_cant.close(); } catch (SQLException e1) { }
        if (con_cant != null) try { con_cant.close(); } catch (SQLException e1) { }
    }

}

public void Eliminar() {
    
    int fila_seleccionada = tabla2.getSelectedRow();
    System.out.println("fila seleccioanada en el metodo eliminar " + fila_seleccionada);

    if (fila_seleccionada != -1) {
        String idProducto = tabla2.getValueAt(fila_seleccionada, 1).toString(); // Get the product code (column 1)
        
        int opcion = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el producto?", "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION);
        
        variable_eliminar = idProducto;
        
        if (opcion == JOptionPane.YES_OPTION) {
            Connection conBD = null;
            PreparedStatement stmt = null;
            
            try {
                Class.forName("org.sqlite.JDBC");
                conBD = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");
                
                // Set transaction isolation to avoid locking
                conBD.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                
                // Use parameterized query to prevent SQL injection
                String consultaEliminar = "DELETE FROM carrito WHERE car_pro = ?";
                stmt = conBD.prepareStatement(consultaEliminar);
                stmt.setString(1, variable_eliminar);

                int filasAfectadas = stmt.executeUpdate();
                
                if (filasAfectadas > 0) {

                    DefaultTableModel modelo2 = (DefaultTableModel) tabla2.getModel();
                    modelo2.removeRow(fila_seleccionada);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el producto.");
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + ex.getMessage());
            } finally {
                // Always close resources in finally block
                if (stmt != null) try { stmt.close(); } catch (SQLException ex) { }
                if (conBD != null) try { conBD.close(); } catch (SQLException ex) { }
            }
        }
    }
    }

public void SumarFacturar(String MetodoPago) {
    double sumaTotal = 0.0;
    for (Double numero : listaProductos) {
        sumaTotal += numero;
    }

    System.out.println("La suma de la lista es: " + sumaTotal + " al cliente: " + var_nom);
    
    // Create the invoice with the total amount (including tax)
    Factura fac = new Factura(var_ced, sumaTotal, var_nom, MetodoPago);
    
    // Add all items to the invoice
    for (int i = 0; i < listaPrecios.size(); i++) {
        fac.agregarLinea("Item " + (i+1) + " - " + listaNombre.get(i), 
                         listaPrecios.get(i), 
                         listaCantidades.get(i), 
                         listaProductos.get(i));
    }
    
    fac.mostrar();
    
    // Debug output
    System.out.println("Productos en la factura:");
    for (int i = 0; i < listaPrecios.size(); i++) {
        System.out.printf("Item %d: %s x %d = %.2f%n", 
            i+1, 
            listaNombre.get(i), 
            listaCantidades.get(i), 
            listaProductos.get(i));
    }
    System.out.printf("Total facturado: %.2f%n", sumaTotal);
}

}
