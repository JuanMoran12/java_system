
package loginjuan;

import java.awt.Color;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lenovo
 */
class Productos {
    JPanel panelCentral = new JPanel();
        //color personalizado de la clase Color
    Color rojo_transparente = new Color(22, 109, 99); // Rojo semi-transparente
    Color azul_oscuro = new Color(40, 53, 96);
    Color azul_claro = new Color(176, 206, 255);
    Color grios_oscuro = new Color(65, 71, 78);
    Color gris_azul = new Color(51, 63, 85);
    Color color_principal = new Color(52, 73, 94);
    Color color_secundario = new Color(245, 245, 245);
    
    //Entradas para leer prductos
    JTextField ent_nom = new JTextField();
    JTextField ent_pre = new JTextField();       
    JTextField ent_exi = new JTextField();
    JTextField ent_codigo = new JTextField();
    
    DefaultTableModel modelo = new DefaultTableModel();
    
    // Crear el JTable con el modelo
    JTable tabla = new JTable(modelo);
    
    //ventana de producto con diseño mejorado
    JFrame ventana6 = new JFrame("Sistema de Gestión - Productos");
    
    //entrada para aceptar la cantidad
    JTextField ent_cant = new JTextField();
    
    //variable para cantidad, declarada como atributo de la clase public para acceder desde cualquier otra clase o metodo
    public String cantidad;
    public String leer_nom;
    public String codigo_prod;
    
    //cedula del cliente
    public String var_ced;
    
    // Configuración inicial de la tabla del carrito
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
    ArrayList<Double> listaPreciosTotales = new ArrayList<>();
    JTextField ent_precio_cantidad = new JTextField();
    
    //variables para el metodo de insertar en el carrito
    String ced_carrito = new String();
    String cod_carrito = new String();    
    
    public Productos(String cedula_del_cliente) {
        
        var_ced = cedula_del_cliente;       
        System.out.println(var_ced);
                
        ventana6.setSize(900, 650);
        ventana6.setLocationRelativeTo(null);
        ventana6.setLayout(null);
        ventana6.getContentPane().setBackground(new Color(245, 245, 245));
        
        // Panel lateral para el menú
        JPanel panelLateral = new JPanel();
        panelLateral.setBounds(0, 0, 250, 650);
        panelLateral.setBackground(new Color(52, 73, 94));
        panelLateral.setLayout(null);
        ventana6.add(panelLateral);
        
        // Panel central para el contenido
        panelCentral.setBounds(250, 0, 650, 650);
        panelCentral.setBackground(new Color(245, 245, 245));
        panelCentral.setLayout(null);
        ventana6.add(panelCentral);
        
        // Título del formulario
        JLabel etq_titulo = new JLabel("Gestión de Productos");
        etq_titulo.setBounds(30, 20, 200, 30);
        etq_titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        etq_titulo.setForeground(Color.WHITE);
        panelLateral.add(etq_titulo);
        
        // Mostrar la cédula del cliente
        JLabel etq_cliente_actual = new JLabel("Cliente: " + cedula_del_cliente);
        etq_cliente_actual.setBounds(30, 550, 200, 30);
        etq_cliente_actual.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        etq_cliente_actual.setForeground(Color.WHITE);
        panelLateral.add(etq_cliente_actual);
        
        //Widgets con estilo moderno
        JLabel etq_nom = new JLabel("Producto");
        etq_nom.setBounds(30, 70, 150, 30);
        etq_nom.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        etq_nom.setForeground(Color.WHITE);
        panelLateral.add(etq_nom);
        
        JLabel etq_codigo = new JLabel("Código");
        etq_codigo.setBounds(30, 140, 150, 30);
        etq_codigo.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        etq_codigo.setForeground(Color.WHITE);
        panelLateral.add(etq_codigo);
        
        JLabel etq_pre = new JLabel("Precio");
        etq_pre.setBounds(30, 210, 150, 30);
        etq_pre.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        etq_pre.setForeground(Color.WHITE);
        panelLateral.add(etq_pre);
        
        JLabel etq_exi = new JLabel("Existencias");
        etq_exi.setBounds(30, 280, 150, 30);
        etq_exi.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        etq_exi.setForeground(Color.WHITE);
        panelLateral.add(etq_exi);
        
        ent_nom.setBounds(30, 100, 190, 30);
        ent_nom.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panelLateral.add(ent_nom);

        ent_codigo.setBounds(30, 170, 190, 30);
        ent_codigo.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panelLateral.add(ent_codigo);
        
        ent_pre.setBounds(30, 240, 190, 30);
        ent_pre.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panelLateral.add(ent_pre);

        ent_exi.setBounds(30, 310, 190, 30);
        ent_exi.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panelLateral.add(ent_exi);
        
        JButton buscar_prod = new JButton("Buscar");
        buscar_prod.setBounds(30, 360, 190, 40);
        buscar_prod.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        buscar_prod.setBackground(new Color(52, 152, 219));
        buscar_prod.setForeground(Color.WHITE);
        buscar_prod.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buscar_prod.setFocusPainted(false);
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
        //aceptar resultado
        JLabel etq_cant = new JLabel("Elija cantidad");
        etq_cant.setBounds(30, 420, 200, 30);
        etq_cant.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        etq_cant.setForeground(Color.WHITE);
        panelLateral.add(etq_cant);
        
        ent_cant.setBounds(30, 450, 190, 30);
        ent_cant.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199)),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panelLateral.add(ent_cant);
        
        JButton boton_aceptar = new JButton("Agregar al Carrito");
        boton_aceptar.setBounds(30, 500, 190, 40);
        boton_aceptar.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        boton_aceptar.setBackground(new Color(46, 204, 113));
        boton_aceptar.setForeground(Color.WHITE);
        boton_aceptar.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        boton_aceptar.setFocusPainted(false);
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
            conBD3 = DriverManager.getConnection("jdbc:sqlite:c:/Users/Lenovo/Downloads/coding/java/java_clase/ventasdb.db");
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
public void insertarEnCarrito(String cedula, String codigoProducto, String cantidad) throws SQLException, ClassNotFoundException {
    
    System.out.println("cedula: "+ cedula + " codigo: " + codigoProducto + " cantidad: "+ cantidad);

    Connection con_cant = null;
    PreparedStatement int_cant = null;
    PreparedStatement pst_producto = null;
    ResultSet rs_producto = null;

    if (cantidad.isEmpty()){
        JOptionPane.showMessageDialog(null, "Por favor, ingrese una cantidad.");
        return;
    }
    
    try {
        Class.forName("org.sqlite.JDBC");
        con_cant = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");
        
        // Set timeout to avoid waiting indefinitely for locked database
        con_cant.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

        // Insertar en la base de datos
        int_cant = con_cant.prepareStatement("INSERT INTO carrito(car_ced, car_pro, car_can) VALUES(?,?,?)");
    
        int_cant.setString(1, cedula);
        int_cant.setString(2, codigoProducto);
        int_cant.setString(3, cantidad);
    
        int filas_actualizadas = int_cant.executeUpdate();
        
        if (filas_actualizadas > 0) {
            // Agregar la fila al JTable del carrito
            String[] filas3 = new String[3];
            filas3[0] = cedula;
            //filas3[1] = codigoProducto + " - " + ent_nom.getText();
            filas3[1] = codigoProducto;
            filas3[2] = cantidad;
            
            // Verificar si el modelo tiene las columnas necesarias
            if (modelo2.getColumnCount() == 0) {
                modelo2.addColumn("Cliente");
                modelo2.addColumn("Producto");
                modelo2.addColumn("Cantidad");
            }
            
            modelo2.addRow(filas3);
            tabla2.setModel(modelo2);
            
            // Actualizar la visualización de la tabla
            tabla2.revalidate();
            tabla2.repaint();
            
            System.out.println("Producto agregado al carrito y a la tabla visual");
        } else {
            System.out.println("Registro mal agregado");
            JOptionPane.showMessageDialog(null, "Error al agregar el producto al carrito.");
        } 
    } catch (SQLException ex) {
        Logger.getLogger(Productos.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Error de base de datos: " + ex.getMessage());
    } finally {
        // Always close resources in finally block
        if (rs_producto != null) try { rs_producto.close(); } catch (SQLException e1) { }
        if (pst_producto != null) try { pst_producto.close(); } catch (SQLException e1) { }
        if (int_cant != null) try { int_cant.close(); } catch (SQLException e1) { }
        if (con_cant != null) try { con_cant.close(); } catch (SQLException e1) { }
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

    int opcion = JOptionPane.showConfirmDialog(ventana6, "¿Desea agregar otro producto?", "Agregar Producto", JOptionPane.YES_NO_OPTION);

    if (opcion == JOptionPane.NO_OPTION) {
        ObtenerFilas();
        System.out.println("aqui se dice noooooo");
    
    } else {
        ent_cant.setText("");
        ent_nom.setText("");
        ent_codigo.setText("");
        ent_pre.setText("");
        ent_exi.setText("");
        
        codigo_prod = ""; 
        
        tabla2.revalidate();
        tabla2.repaint();
    }

    JButton boton_eliminar = new JButton("Eliminar");
    boton_eliminar.setBounds(130, 560, 190, 40);
    boton_eliminar.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
    boton_eliminar.setBackground(azul_oscuro);
    boton_eliminar.setForeground(Color.WHITE);
    boton_eliminar.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
    boton_eliminar.setFocusPainted(false);
    panelCentral.add(boton_eliminar);

    boton_eliminar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Eliminar();
            tabla2.revalidate();
            tabla2.repaint();
        }
    });

    JButton boton_pagar = new JButton("Pagar");
    boton_pagar.setBounds(350, 560, 190, 40);
    boton_pagar.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
    boton_pagar.setBackground(azul_oscuro);
    boton_pagar.setForeground(Color.WHITE);
    boton_pagar.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
    boton_pagar.setFocusPainted(false);
    panelCentral.add(boton_pagar);
    
    ventana6.revalidate();
    ventana6.repaint();
    ventana6.setVisible(true);   
}

public void Eliminar() {
    
    int fila_seleccionada = tabla2.getSelectedRow();
    System.out.println("fila seleccioanada en el metodo eliminar " + fila_seleccionada);

    if (fila_seleccionada != -1) {
        String idProducto = tabla2.getValueAt(fila_seleccionada, 1).toString(); // Get the product code (column 1)
        
        int opcion = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el producto?", "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION);
        
        // Use the actual product code from the table
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

                // Execute the delete query
                int filasAfectadas = stmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    //JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");

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

private void ObtenerFilas() throws ClassNotFoundException, SQLException {
    
    mod_prod.addColumn("Codigo");
    mod_prod.addColumn("Producto");
    mod_prod.addColumn("Cantidad");
    mod_prod.addColumn("Precio");

    tabla_prod.setBackground(Color.WHITE);
    scroll_prod.setBackground(Color.white);
    
    int  cantidad_filas = modelo2.getRowCount();

    List<String[]> filas = new ArrayList<>();

    for (int i = 0; i < cantidad_filas; i++) {
        String cedula = modelo2.getValueAt(i, 0).toString(); 
        String producto = modelo2.getValueAt(i, 1).toString(); 
        String cantidad_fila = modelo2.getValueAt(i, 2).toString(); 
        
        filas.add(new String[]{cedula, producto, cantidad_fila});
    }

List<String> fila_id = new ArrayList<>();

for (int i = 0; i < filas.size(); i++) {
    System.out.println(i);
    String[] fila = filas.get(i);

    System.out.println("Cédula: " + fila[0] + ", Producto: " + fila[1] + ", Cantidad: " + fila[2]);
            
    fila_id.add(fila[1]);

}
for (int i = 0; i < fila_id.size(); i++) {
    System.out.println("valor de: " + i);
    String id_prod = fila_id.get(i);
    System.out.println("ID almacenado: " + id_prod);
    
    Class.forName("org.sqlite.JDBC");
    Connection ConBD_id = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");
    Statement consulta_precio = ConBD_id.createStatement();
    String precio_prod = "SELECT nom_prod, pre_prod FROM productos WHERE id_prod = \"" + id_prod +"\"";
    ResultSet id_codigo = consulta_precio.executeQuery(precio_prod);

    String[] filas2 = new String[6];
    
    while (id_codigo.next()) {
        String prec_str = id_codigo.getString("pre_prod");

        double prec = Double.parseDouble(prec_str);
        // Usar la cantidad de la fila actual en lugar de la variable global
        String cantidad_actual = filas.get(i)[2];
        int cantidad_num = Integer.parseInt(cantidad_actual);

        // calcula el precio total
        double precio_total = prec * cantidad_num;
        
        listaPreciosTotales.add(precio_total);
        
        filas2[0] = id_codigo.getString(1);
        filas2[1] = id_codigo.getString(2);

        System.out.println("precio del producto: " + filas2[0] + ", nombre del producto: " + filas2[1] + ", cantidad: " + cantidad_actual);
        String nombrecito = filas2[0];            

        String[] fila_ultima = {id_prod, nombrecito, cantidad_actual, prec_str};
        mod_prod.addRow(fila_ultima);
        
        double sumaTotal = 0.0;
        
        /*for (int i = 0; i < listaPreciosTotales.size(); i++) {
sumaTotal += listaPreciosTotales.get(i);
}*/

        for (double precio : listaPreciosTotales) {
            sumaTotal += precio; 
        }

        System.out.println("La suma total de todos los precios es: " + sumaTotal);
        
        String tex_precio = Double.toString(sumaTotal);
        ent_precio_cantidad.setText(tex_precio);
                    
        Factura llamar_factura = new Factura(var_ced, mod_prod, tabla_prod, scroll_prod, ent_precio_cantidad);
    }
}
}
}
