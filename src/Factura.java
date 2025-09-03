// src/Factura.java

import javax.swing.*;
import java.awt.*;

public class Factura {

    private JFrame frame;
    private JLabel nombreEmpresaLabel;
    private JLabel facturaFechaLabel;
    private JLabel totalLabel;

    public Factura(String nombreEmpresa, String facturaFecha, double total) {
        initialize(nombreEmpresa, facturaFecha, total);
    }

    private void initialize(String nombreEmpresa, String facturaFecha, double total) {
        frame = new JFrame();
        frame.setTitle("Factura");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());

        nombreEmpresaLabel = new JLabel("Nombre Empresa: " + nombreEmpresa);
        frame.getContentPane().add(nombreEmpresaLabel);

        facturaFechaLabel = new JLabel("Fecha: " + facturaFecha);
        frame.getContentPane().add(facturaFechaLabel);

        totalLabel = new JLabel("Total: " + total + " €");
        frame.getContentPane().add(totalLabel);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Example usage
        SwingUtilities.invokeLater(() -> {
            new Factura("Example Company", "01/02/2024", 150.00);
        });
    }
}
