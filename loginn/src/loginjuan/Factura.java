/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginjuan;

import java.awt.Color;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
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
 * @author juanm
 */
class Factura {

    JFrame ventana7 = new JFrame("Sistema de Gestión - Factura");
    JTextField ent_fac2 = new JTextField();
    JTextField ent_direccion = new JTextField(); // Campo para dirección
    JTextField ent_telefono = new JTextField();  // Campo para teléfono
    JTextField ent_fecha = new JTextField();     // Campo para fecha
    Object contenido = new Object();
    
    // Color scheme consistent with other JFrames
    Color color_principal = new Color(52, 73, 94);
    Color color_secundario = new Color(245, 245, 245);
    Color color_acento = new Color(41, 128, 185);
    
    // Panels for modern structure
    JPanel panelLateral;
    JPanel panelCentral;
    JPanel panelInferior;
    
    // Components that need to be accessed globally
    JButton boton_imprimir;
    JTextField txtIva;
    JTextField totalPagar;
    JLabel etq_iva;
    JLabel etq_total;
    
    //nombre separado
    String nombre = new String();
    String apellido = new String();
    
    //dropdown para escoger el metodo de pago
    JComboBox<String> drop_metodo = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia"});
    
    //monto total de la factura con iva
    String txtfinal = new String();

    public Factura(String var_ced, Double total, String nombre) {
        // Main frame setup
        ventana7.setSize(900, 650);
        ventana7.setLocationRelativeTo(null);
        ventana7.setLayout(null);
        ventana7.getContentPane().setBackground(color_secundario);
        
        ventana7.setVisible(true);
    }
    
    private void setupPanels() {
        // Left sidebar panel
        panelLateral = new JPanel();
        panelLateral.setBounds(0, 0, 250, 700);
        panelLateral.setBackground(color_principal);
        panelLateral.setLayout(null);
        
        // Central content panel
        panelCentral = new JPanel();
        panelCentral.setBounds(250, 80, 750, 500);
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setLayout(null);
        panelCentral.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        
        // Bottom panel for invoice summary
        panelInferior = new JPanel();
        panelInferior.setBounds(250, 580, 750, 120);
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setLayout(null);
        panelInferior.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        
        // Header panel
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBounds(250, 0, 750, 80);
        panelSuperior.setBackground(color_principal);
        panelSuperior.setLayout(null);
        
        ventana7.add(panelLateral);
        ventana7.add(panelSuperior);
        ventana7.add(panelCentral);
        ventana7.add(panelInferior);
    }
    
    private void setupHeader() {
        JPanel panelSuperior = (JPanel) ventana7.getComponentAt(250, 0);
        
        JLabel tituloFactura = new JLabel("FACTURA DE VENTA");
        tituloFactura.setBounds(30, 25, 300, 30);
        tituloFactura.setForeground(Color.WHITE);
        tituloFactura.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panelSuperior.add(tituloFactura);
        
        // Sidebar title
        JLabel tituloSistema = new JLabel("SISTEMA DE GESTIÓN");
        tituloSistema.setBounds(25, 30, 200, 30);
        tituloSistema.setForeground(Color.WHITE);
        tituloSistema.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelLateral.add(tituloSistema);
        
        JPanel separador = new JPanel();
        separador.setBounds(25, 70, 200, 2);
        separador.setBackground(Color.WHITE);
        panelLateral.add(separador);
    }
    
    private void setupClientInfo(String var_ced) throws ClassNotFoundException, SQLException {
        // Client information section
        JLabel lblClienteInfo = new JLabel("INFORMACIÓN DEL CLIENTE");
        lblClienteInfo.setBounds(30, 20, 300, 25);
        lblClienteInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblClienteInfo.setForeground(color_principal);
        panelCentral.add(lblClienteInfo);
        
        JLabel etq_fac1 = new JLabel("Cédula:");
        etq_fac1.setBounds(30, 60, 100, 25);
        etq_fac1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etq_fac1.setForeground(color_principal);
        panelCentral.add(etq_fac1);

        JTextField ent_fac1 = new JTextField();
        ent_fac1.setBounds(140, 60, 150, 30);
        ent_fac1.setText(var_ced);
        ent_fac1.setEditable(false);
        ent_fac1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ent_fac1.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        ent_fac1.setBackground(color_secundario);
        panelCentral.add(ent_fac1);
        
        // Carga datos del cliente si existe la cédula
        if (!var_ced.isEmpty()) {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection BD_cliente = DriverManager.getConnection("jdbc:mariadb://localhost:3306/ventasdb");
            String query_cliente = "SELECT nombre, apellido, direccion, telefono FROM clientes WHERE cedula = ?";
            PreparedStatement ps = BD_cliente.prepareStatement(query_cliente);
            ps.setString(1, var_ced);
            ResultSet resultado = ps.executeQuery();

            if (resultado.next()) {
                String nombreCompleto = resultado.getString("nombre") + " " + resultado.getString("apellido");

                String[] nombres = nombreCompleto.split(" ");

                nombre = nombres[0]; 
                apellido = nombres[1]; 
                
                ent_fac2.setText(nombreCompleto);
                ent_direccion.setText(resultado.getString("direccion"));
                ent_telefono.setText(resultado.getString("telefono"));
                
                System.out.println(nombre + " " + apellido);
            }
        }
        
        // Client name field
        JLabel etq_nombre = new JLabel("Nombre:");
        etq_nombre.setBounds(320, 60, 100, 25);
        etq_nombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etq_nombre.setForeground(color_principal);
        panelCentral.add(etq_nombre);

        ent_fac2.setBounds(430, 60, 200, 30);
        ent_fac2.setEditable(false);
        ent_fac2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ent_fac2.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        ent_fac2.setBackground(color_secundario);
        panelCentral.add(ent_fac2);

        // Address field
        JLabel etq_direccion = new JLabel("Dirección:");
        etq_direccion.setBounds(30, 100, 100, 25);
        etq_direccion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etq_direccion.setForeground(color_principal);
        panelCentral.add(etq_direccion);

        ent_direccion.setBounds(140, 100, 200, 30);
        ent_direccion.setEditable(false);
        ent_direccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ent_direccion.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        ent_direccion.setBackground(color_secundario);
        panelCentral.add(ent_direccion);

        // Phone field
        JLabel etq_telefono = new JLabel("Teléfono:");
        etq_telefono.setBounds(370, 100, 100, 25);
        etq_telefono.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etq_telefono.setForeground(color_principal);
        panelCentral.add(etq_telefono);

        ent_telefono.setBounds(480, 100, 150, 30);
        ent_telefono.setEditable(false);
        ent_telefono.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ent_telefono.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        ent_telefono.setBackground(color_secundario);
        panelCentral.add(ent_telefono);

        // Date field
        JLabel etq_fecha = new JLabel("Fecha:");
        etq_fecha.setBounds(30, 140, 100, 25);
        etq_fecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etq_fecha.setForeground(color_principal);
        panelCentral.add(etq_fecha);

        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ent_fecha.setText(fechaActual.format(formato));
        ent_fecha.setBounds(140, 140, 150, 30);
        ent_fecha.setEditable(false);
        ent_fecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ent_fecha.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        ent_fecha.setBackground(color_secundario);
        panelCentral.add(ent_fecha);
    }
    
    private void setupProductsTable(JScrollPane scroll_prod) {
        JLabel lblProductos = new JLabel("PRODUCTOS SELECCIONADOS");
        lblProductos.setBounds(30, 180, 300, 25);
        lblProductos.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblProductos.setForeground(color_principal);
        panelCentral.add(lblProductos);
        
        scroll_prod.setBounds(30, 210, 690, 200);
        scroll_prod.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        panelCentral.add(scroll_prod);
    }
    
    private void setupPaymentSection(JTextField ent_precio_cantidad, String var_ced, DefaultTableModel mod_prod) {
        JLabel lblPago = new JLabel("INFORMACIÓN DE PAGO");
        lblPago.setBounds(30, 420, 300, 25);
        lblPago.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPago.setForeground(color_principal);
        panelCentral.add(lblPago);
        
        JLabel etq_metodoPago = new JLabel("Método de Pago:");
        etq_metodoPago.setBounds(30, 450, 150, 25);
        etq_metodoPago.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etq_metodoPago.setForeground(color_principal);
        panelCentral.add(etq_metodoPago);

        drop_metodo.setBounds(180, 450, 150, 30);
        drop_metodo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        drop_metodo.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        panelCentral.add(drop_metodo);
        
        JLabel etq_subtotal = new JLabel("Subtotal:");
        etq_subtotal.setBounds(370, 450, 100, 25);
        etq_subtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etq_subtotal.setForeground(color_principal);
        panelCentral.add(etq_subtotal);
        
        ent_precio_cantidad.setBounds(470, 450, 120, 30);
        ent_precio_cantidad.setEditable(false);
        ent_precio_cantidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ent_precio_cantidad.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        ent_precio_cantidad.setBackground(color_secundario);
        panelCentral.add(ent_precio_cantidad);
        
        JButton pagar = new JButton("PROCESAR PAGO");
        pagar.setBounds(600, 450, 120, 35);
        pagar.setBackground(color_acento);
        pagar.setForeground(Color.WHITE);
        pagar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pagar.setFocusPainted(false);
        pagar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelCentral.add(pagar);
        
        pagar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Ventas(e, var_ced, ent_direccion, ent_telefono, contenido, mod_prod, ent_precio_cantidad, drop_metodo);
                } catch (SQLException ex) {
                    Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void setupInvoiceSummary() {
        // Initially hidden invoice summary components
        etq_iva = new JLabel("IVA (16%):");
        etq_iva.setBounds(30, 20, 100, 25);
        etq_iva.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etq_iva.setForeground(color_principal);
        etq_iva.setVisible(false);
        panelInferior.add(etq_iva);

        txtIva = new JTextField();
        txtIva.setBounds(140, 20, 120, 30);
        txtIva.setEditable(false);
        txtIva.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtIva.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        txtIva.setBackground(color_secundario);
        txtIva.setVisible(false);
        panelInferior.add(txtIva);

        etq_total = new JLabel("Total a Pagar:");
        etq_total.setBounds(300, 20, 120, 25);
        etq_total.setFont(new Font("Segoe UI", Font.BOLD, 16));
        etq_total.setForeground(color_principal);
        etq_total.setVisible(false);
        panelInferior.add(etq_total);

        totalPagar = new JTextField();
        totalPagar.setBounds(430, 20, 150, 35);
        totalPagar.setEditable(false);
        totalPagar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalPagar.setBorder(BorderFactory.createLineBorder(color_acento, 2));
        totalPagar.setBackground(Color.WHITE);
        totalPagar.setForeground(color_acento);
        totalPagar.setVisible(false);
        panelInferior.add(totalPagar);
        
        boton_imprimir = new JButton("IMPRIMIR FACTURA");
        boton_imprimir.setBounds(600, 20, 140, 35);
        boton_imprimir.setBackground(new Color(39, 174, 96));
        boton_imprimir.setForeground(Color.WHITE);
        boton_imprimir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton_imprimir.setFocusPainted(false);
        boton_imprimir.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        boton_imprimir.setVisible(false);
        panelInferior.add(boton_imprimir);
    }

    public void Ventas(ActionEvent e, String var_ced, JTextField ent_direccion, JTextField ent_telefono, Object contenido, DefaultTableModel mod_prod, JTextField ent_precio_cantidad, JComboBox drop_metodo) throws SQLException {
        int var = mod_prod.getRowCount();
        System.out.println("Cantidad de filas: " + var);

        String[] filas_prod = new String[6];

        Connection conP = DriverManager.getConnection("jdbc:mariadb://localhost:3306/ventasdb", "root", "12345678");
        Statement datos_producto = conP.createStatement();

        for (int i = 0; i < var; i++) {
            contenido = mod_prod.getValueAt(i, 0);
            Object otraColumna = mod_prod.getValueAt(i, 2);
            System.out.println("Código del producto procedente de las filas de la tabla: " + contenido + "cantidad del producto:  "+ otraColumna);

            // Consulta para obtener los detalles del producto
            String consulta = "SELECT * FROM productos WHERE id_prod LIKE \"" + contenido + "\"";
            ResultSet resultado = datos_producto.executeQuery(consulta);

            // Procesar los resultados de la consulta
            while (resultado.next()) {
                filas_prod[0] = resultado.getString(1); 
                filas_prod[1] = resultado.getString(2); 
                filas_prod[2] = resultado.getString(3); 
                                
                int cantidad = Integer.parseInt(otraColumna.toString());
                double precio = Double.parseDouble(filas_prod[2]);

                double netProd = cantidad * precio;
                System.out.println("Total (net_prod): " + netProd);
                
                System.out.println("elementos para imprimir: " + var_ced + " "+ ent_direccion.getText() + " "+ ent_telefono.getText() + " "+ contenido 
                        + " "+ filas_prod[0]+ " "+ precio + " "+ cantidad + " "+ netProd);
                
                Connection conC = DriverManager.getConnection("jdbc:mariadb://localhost:3306/ventasdb", "root", "12345678");

                PreparedStatement consulta_ventas = conC.prepareStatement("insert into ventas (ced_cliente, nom_cliente, ape_cliente, cod_prod, nom_prod, pre_prod, can_prod, net_prod) values(?,?,?,?,?,?,?,?)");

                consulta_ventas.setString(1, var_ced);
                consulta_ventas.setString(2, ent_direccion.getText()); 
                consulta_ventas.setString(3, ent_telefono.getText()); 
                consulta_ventas.setString(4, filas_prod[0]);
                consulta_ventas.setString(5, filas_prod[1]);
                consulta_ventas.setDouble(6, precio); 
                consulta_ventas.setInt(7, cantidad); 
                consulta_ventas.setDouble(8, netProd); 
                
                int filas_afectadas = consulta_ventas.executeUpdate();

                if (filas_afectadas == 0){
                    System.out.println("no se inserto en la tabla ventas");

                } else{
                    System.out.println("factura hecha");}
                    
                    double prec_tot = Double.parseDouble(ent_precio_cantidad.getText());
                    
                    double precio_iva = prec_tot * 0.16;
                    
                    double precio_con_iva = prec_tot + precio_iva;
                    
                    System.out.println("precio total con iva: "+ precio_iva);
                    
                    // Show invoice summary components that were pre-created
                    DecimalFormat df = new DecimalFormat("#.00");
                    String txtivita = df.format(precio_iva);
                    txtIva.setText(txtivita);
                    etq_iva.setVisible(true);
                    txtIva.setVisible(true);

                    txtfinal = df.format(precio_con_iva);
                    totalPagar.setText("$" + txtfinal);
                    etq_total.setVisible(true);
                    totalPagar.setVisible(true);
                    boton_imprimir.setVisible(true);
                    
                    // Refresh the display
                    panelInferior.revalidate();
                    panelInferior.repaint();
                    ventana7.revalidate();
                    ventana7.repaint();
                   
                    boton_imprimir.addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            ImprimirFactura(e, drop_metodo, var_ced, txtfinal);
                        } catch (SQLException ex) {
                            Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Factura.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                        });
                    }
        }
    }
    
    public void ImprimirFactura(ActionEvent e, JComboBox drop_metodo, String var_ced, String txtfinal) throws SQLException, ParseException, IOException {

        String metodoPago = (String) drop_metodo.getSelectedItem();
        double txtfinal2 = Double.parseDouble(txtfinal);
        String fechaTexto = ent_fecha.getText();

        System.out.println("Fecha ingresada: " + fechaTexto);
        System.out.println("fecha: " + fechaTexto + " ced cliente: " + var_ced + " tipo de pago: " + metodoPago + " monto factura: " + txtfinal);

        // Parsear el texto de la fecha
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd"); // Ajusta el formato según tu entrada
        java.util.Date fechaUtil = formatoFecha.parse(fechaTexto); // Convierte la cadena a java.util.Date
        java.sql.Date fechaSQL = new java.sql.Date(fechaUtil.getTime()); // Convierte a java.sql.Date

        Connection conF = DriverManager.getConnection("jdbc:mariadb://localhost:3306/ventasdb", "root", "12345678");

        PreparedStatement consulta_factura = conF.prepareStatement("insert into facturas (fec_factura, ced_factura, tipo_pago, monto_factura) values(?, ?, ?, ?)");

        consulta_factura.setDate(1, fechaSQL); // Inserta la fecha como java.sql.Date
        consulta_factura.setString(2, var_ced);
        consulta_factura.setString(3, metodoPago);
        consulta_factura.setDouble(4, txtfinal2);

        int filas_movidas = consulta_factura.executeUpdate();

        if (filas_movidas == 0) {
            System.out.println("No se realizó la factura");
        } else {
            System.out.println("Factura se realizó correctamente");
                    
            /*String archivoSalida = "factura_"  + ".txt";
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))
            writer.write("Factura ID: " );
            writer.newLine();
            writer.write("Fecha: " );
            writer.newLine();
            writer.write("Cédula del Cliente: " );
            writer.newLine();
            writer.write("Tipo de Pago: " );
            writer.newLine();
            writer.write("Monto: " );
            writer.newLine();
            System.out.println("Factura exportada correctamente al archivo: " );
*/
    }
        
    }

}



        

    


