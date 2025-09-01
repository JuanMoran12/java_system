/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginjuan;

import java.awt.Color;
import static java.awt.Color.white;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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

    JFrame ventana7 = new JFrame("Factura");
    JTextField ent_fac2 = new JTextField();
    JTextField ent_direccion = new JTextField(); // Campo para dirección
    JTextField ent_telefono = new JTextField();  // Campo para teléfono
    JTextField ent_fecha = new JTextField();     // Campo para fecha
    Object contenido = new Object();
    
    //nombre separado
    String nombre = new String();
    String apellido = new String();
    
    //dropdown para escoger el metodo de pago
    JComboBox<String> drop_metodo = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia"});
    
    //monto total de la factura con iva
    String txtfinal = new String();

    public Factura(String var_ced, DefaultTableModel mod_prod, JTable tabla_prod, JScrollPane scroll_prod, JTextField ent_precio_cantidad) throws ClassNotFoundException, SQLException {
        ventana7.setSize(550, 680);
        ventana7.setLocationRelativeTo(null);
        ventana7.setLayout(null);
        ventana7.getContentPane().setBackground(Color.white);
        
        scroll_prod.setBounds(50, 320, 450, 150);
        ventana7.add(scroll_prod);
        

        JPanel panel_sup = new JPanel();
        panel_sup.setBounds(0, 0, 550, 100);
        panel_sup.setBackground(Color.BLUE);

        JLabel etq_fac1 = new JLabel("Cédula");
        etq_fac1.setBounds(50, 120, 150, 30);

        JTextField ent_fac1 = new JTextField();
        ent_fac1.setBounds(150, 120, 120, 30);
        ent_fac1.setText(var_ced);
        ent_fac1.setEditable(false);
        
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
        JLabel etq_nombre = new JLabel("Nombre");
        etq_nombre.setBounds(50, 160, 150, 30);

        ent_fac2.setBounds(150, 160, 120, 30);
        ent_fac2.setEditable(false);

        JLabel etq_direccion = new JLabel("Dirección");
        etq_direccion.setBounds(50, 200, 150, 30);

        ent_direccion.setBounds(150, 200, 120, 30);
        ent_direccion.setEditable(false);

        JLabel etq_telefono = new JLabel("Teléfono");
        etq_telefono.setBounds(50, 240, 150, 30);

        ent_telefono.setBounds(150, 240, 120, 30);
        ent_telefono.setEditable(false);

        JLabel etq_fecha = new JLabel("Fecha");
        etq_fecha.setBounds(50, 280, 150, 30);

        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ent_fecha.setText(fechaActual.format(formato));
        ent_fecha.setBounds(150, 280, 120, 30);
        ent_fecha.setEditable(false); 
        
        JLabel etq_metodoPago = new JLabel("Método de Pago:");
        etq_metodoPago.setBounds(30, 500, 150, 30);
        ventana7.add(etq_metodoPago);

        drop_metodo.setBounds(130, 500, 120, 30);
        ventana7.add(drop_metodo);
        
        ent_precio_cantidad.setBounds(300, 500, 100, 30);
        ent_precio_cantidad.setEditable(false);
        
        JButton pagar = new JButton("Pagar");
        pagar.setBounds(410, 500, 100, 30);
        
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

        // Agrega componentes a la ventana
        ventana7.add(panel_sup);
        ventana7.add(etq_fac1);
        ventana7.add(ent_fac1);
        ventana7.add(etq_nombre);
        ventana7.add(ent_fac2);
        ventana7.add(etq_direccion);
        ventana7.add(ent_direccion);
        ventana7.add(etq_telefono);
        ventana7.add(ent_telefono);
        ventana7.add(etq_fecha);
        ventana7.add(ent_fecha);
        ventana7.add(pagar);
        ventana7.add(ent_precio_cantidad);

        ventana7.setVisible(true);
    
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
                    
                    JButton boton_imprimir = new JButton("Imprimir Factura");
                    boton_imprimir.setBounds(350, 550, 150, 30);
                    ventana7.add(boton_imprimir);
                    
                    JLabel etq_iva = new JLabel("IVA:");
                    etq_iva.setBounds(30, 550, 50, 30);
                    ventana7.add(etq_iva);

                    JTextField txtIva = new JTextField();
                    txtIva.setBounds(160, 550, 80, 30);
                    txtIva.setEditable(false);
                    
                    DecimalFormat df = new DecimalFormat("#.00");
                    String txtivita = df.format(precio_iva);
                    txtivita = Double.toString(precio_iva);
                    txtIva.setText(txtivita);
                    ventana7.add(txtIva);

                    JLabel etq_total = new JLabel("Total a pagar:");
                    etq_total.setBounds(30, 590, 120, 30);                    
                    ventana7.add(etq_total);

                    JTextField totalPagar = new JTextField();
                    totalPagar.setBounds(160, 590, 100, 30);
                    totalPagar.setEditable(false);
                    txtfinal = Double.toString(precio_con_iva);
                    totalPagar.setText(txtfinal); 
                    ventana7.add(totalPagar);
                    
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



        

    


