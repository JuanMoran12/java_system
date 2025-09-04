package loginjuan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class Factura {

    // --- Estado ---
    private final JFrame frame;
    private final JLabel lblCliente;
    private final JLabel lblCedula;
    private final JLabel lblFecha;
    private final JLabel lblNumero;
    private final JLabel lblSubtotal;
    private final JLabel lblImpuesto;
    private final JLabel lblTotal;
    private final DefaultTableModel modelo;

    private final NumberFormat moneda = NumberFormat.getCurrencyInstance(new Locale("es", "VE"));
    private static final AtomicInteger SECUENCIA = new AtomicInteger(2540); // autoincremento simple

    public Factura(String cedulaCliente, double totalCalculado, String nombreCliente) {

        frame = new JFrame("Factura");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // --- Encabezado con color ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(60, 120, 180)); // azul elegante
        header.setBorder(new EmptyBorder(24, 24, 16, 24));
        JLabel titulo = new JLabel("Nombre de la empresa");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 28f));
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);
        frame.add(header, BorderLayout.NORTH);

        // --- Bloque superior con datos de cliente y factura ---
        JPanel info = new JPanel(new GridLayout(1, 2, 24, 0));
        info.setBorder(new EmptyBorder(16, 24, 16, 24));
        info.setBackground(Color.WHITE);

        // Datos cliente
        JPanel pCliente = new JPanel(new GridLayout(5, 1, 4, 4));
        pCliente.setBackground(Color.WHITE);
        pCliente.add(etiqueta("Factura a"));
        lblCliente = texto(nombreCliente);
        pCliente.add(lblCliente);
        lblCedula = texto("Cédula: " + (cedulaCliente == null ? "—" : cedulaCliente));
        pCliente.add(lblCedula);

        // Datos factura
        JPanel pFactura = new JPanel(new GridLayout(4, 2, 8, 4));
        pFactura.setBackground(Color.WHITE);
        pFactura.add(etiqueta("Factura"));
        lblNumero = texto(String.valueOf(SECUENCIA.incrementAndGet()));
        pFactura.add(lblNumero);
        pFactura.add(etiqueta("Fecha"));
        lblFecha = texto(LocalDate.now().toString());
        pFactura.add(lblFecha);
        pFactura.add(etiqueta("Método de pago"));
        pFactura.add(texto("—"));
        pFactura.add(new JLabel()); pFactura.add(new JLabel());

        info.add(pCliente);
        info.add(pFactura);
        frame.add(info, BorderLayout.BEFORE_FIRST_LINE);

        // --- Tabla con estilo ---
        String[] columnas = {"Ítem", "Descripción", "Precio", "Cant.", "Total"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Integer.class;
                    case 2, 4 -> Double.class;
                    case 3 -> Integer.class;
                    default -> String.class;
                };
            }
        };
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Estilo encabezado de tabla
        JTableHeaderEstilo(tabla);

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(new EmptyBorder(0, 24, 0, 24));
        frame.add(sp, BorderLayout.CENTER);

        // --- Pie con totales ---
        JPanel pie = new JPanel(new BorderLayout());
        pie.setBorder(new EmptyBorder(12, 24, 24, 24));
        pie.setBackground(Color.WHITE);

        JLabel gracias = new JLabel("¡Gracias por su compra!");
        gracias.setFont(new Font("SansSerif", Font.ITALIC, 14));
        pie.add(gracias, BorderLayout.WEST);

        JPanel totales = new JPanel(new GridLayout(3, 2, 12, 8));
        totales.setBackground(Color.WHITE);

        totales.add(etiqueta("SUBTOTAL:"));
        lblSubtotal = textoMoneda(0.0);
        totales.add(lblSubtotal);

        totales.add(etiqueta("IVA:"));
        lblImpuesto = textoMoneda(0.0);
        totales.add(lblImpuesto);

        JLabel lblTotalTitulo = etiqueta("TOTAL:");
        lblTotalTitulo.setFont(lblTotalTitulo.getFont().deriveFont(Font.BOLD, 16f));
        totales.add(lblTotalTitulo);
        lblTotal = textoMoneda(totalCalculado);
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 16f));
        totales.add(lblTotal);

        pie.add(totales, BorderLayout.EAST);
        frame.add(pie, BorderLayout.SOUTH);
    }

    // ---------- API pública ----------
    public void mostrar() { frame.setVisible(true); }

    public void agregarLinea(String descripcion, double precio, int cantidad, double total) {
        int item = modelo.getRowCount() + 1;
        modelo.addRow(new Object[]{item, descripcion, precio, cantidad, total});
    }

    public void recalcularTotales(double iva) {
        double subtotal = 0.0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            subtotal += ((Number) modelo.getValueAt(i, 4)).doubleValue();
        }
        double impuesto = subtotal * iva;
        double total = subtotal + impuesto;

        lblSubtotal.setText(moneda.format(subtotal));
        lblImpuesto.setText(moneda.format(impuesto));
        lblTotal.setText(moneda.format(total));
    }

    // ---------- Helpers UI ----------
    private static JLabel etiqueta(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        return l;
    }

    private static JLabel texto(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return l;
    }

    private JLabel textoMoneda(double v) {
        JLabel l = new JLabel(moneda.format(v));
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return l;
    }

    private void JTableHeaderEstilo(JTable tabla) {
        tabla.getTableHeader().setBackground(new Color(60, 120, 180));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        ((DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
    }
}
