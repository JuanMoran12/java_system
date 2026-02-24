/*
 * Ventana de login - Diseño centrado con logo de la empresa.
 * La lógica de autenticación se mantiene intacta.
 */
package loginjuan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

class ventana {

    JTextField ent = new JTextField(20);
    JPasswordField ent2 = new JPasswordField(20);
    JPasswordField ent3 = new JPasswordField();

    JTextField ent_usuario = new JTextField();
    JTextField ent_clave = new JTextField();

    Color rojo_transparente = new Color(22, 109, 99);

    private static final Color FONDO_OSCURO = Color.WHITE;
    private static final Color FONDO_OSCURO_ABAJO = Color.WHITE;
    private static final Color CARD_BG = Color.WHITE;
    private static final Color CARD_BORDE = new Color(203, 213, 225);
    private static final Color TEXTO_TITULO = new Color(15, 23, 42);
    private static final Color TEXTO_SECUNDARIO = new Color(71, 85, 105);
    private static final Color BORDE_CAMPO = new Color(226, 232, 240);
    private static final Color BG_CAMPO = new Color(248, 250, 252);
    private static final String SEGOE = "Segoe UI";
    private static final int CARD_ANCHO = 980;
    private static final int CARD_ALTO = 560;

    JFrame login = new JFrame("Sistema de Gestión - Login");

    public ventana() {
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setSize(440, 620);
        login.setLocationRelativeTo(null);
        login.setResizable(true);
        login.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel root = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, FONDO_OSCURO, 0, getHeight(), FONDO_OSCURO_ABAJO));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        root.setOpaque(true);
        login.setContentPane(root);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(40, 80, 40, 80);

        // Cuadro de login centrado, con bordes laterales visibles
        RoundedPanel card = new RoundedPanel(28);
        card.setBackground(CARD_BG);
        card.setPreferredSize(new Dimension(CARD_ANCHO, CARD_ALTO));
        card.setMinimumSize(new Dimension(CARD_ANCHO, CARD_ALTO));
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(CARD_BORDE, 2),
            new EmptyBorder(0, 0, 0, 0)));
        root.add(card, gbc);

        GradientPanel leftPanel = new GradientPanel(new Color(30, 58, 138), new Color(23, 37, 84));
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(38, 46, 38, 46));

        card.add(leftPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.CENTER);

        leftPanel.setPreferredSize(new Dimension(420, CARD_ALTO));

        GridBagConstraints l = new GridBagConstraints();
        l.gridx = 0;
        l.gridy = 0;
        l.weightx = 1;
        l.weighty = 1;
        l.fill = GridBagConstraints.BOTH;
        l.anchor = GridBagConstraints.CENTER;
        l.insets = new Insets(0, 0, 0, 0);

        JPanel leftContent = new JPanel(new GridBagLayout());
        leftContent.setOpaque(false);
        leftPanel.add(leftContent, l);

        GridBagConstraints lc = new GridBagConstraints();
        lc.gridx = 0;
        lc.gridy = 0;
        lc.weightx = 1;
        lc.fill = GridBagConstraints.HORIZONTAL;
        lc.anchor = GridBagConstraints.CENTER;
        lc.insets = new Insets(0, 0, 16, 0);

        // Intentar cargar el logo desde diferentes rutas
        ImageIcon icon = null;
        try {
            // Intentar primero desde images/
            icon = new ImageIcon("images/logo_w.png");
            if (icon.getImageLoadStatus() != java.awt.MediaTracker.COMPLETE) {
                // Si falla, intentar desde la raíz
                icon = new ImageIcon("logo_w.png");
            }
        } catch (Exception e) {
            System.err.println("Error cargando logo: " + e.getMessage());
        }
        
        Image img = (icon != null) ? icon.getImage() : null;
        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(JLabel.CENTER);
        logo.setVerticalAlignment(JLabel.CENTER);
        if (img != null && img.getWidth(null) > 0) {
            Image scaled = img.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(scaled));
        } else {
            // Fallback: mostrar texto si no se carga la imagen
            logo.setText("LOGO");
            logo.setFont(new java.awt.Font(SEGOE, java.awt.Font.BOLD, 48));
            logo.setForeground(Color.WHITE);
            System.err.println("No se pudo cargar el logo desde images/logo_w.png o logo_w.png");
        }
        leftContent.add(logo, lc);

        JLabel welcome = new JLabel("SISTEMA ERP");
        welcome.setFont(new java.awt.Font(SEGOE, java.awt.Font.BOLD, 26));
        welcome.setForeground(Color.WHITE);
        welcome.setHorizontalAlignment(JLabel.CENTER);
        lc.gridy++;
        lc.insets = new Insets(0, 0, 6, 0);
        leftContent.add(welcome, lc);

        JLabel welcome2 = new JLabel("Planificación de Recursos Empresariales en Satelite Express");
        welcome2.setFont(new java.awt.Font(SEGOE, java.awt.Font.PLAIN, 13));
        welcome2.setForeground(new Color(226, 232, 240));
        welcome2.setHorizontalAlignment(JLabel.CENTER);
        lc.gridy++;
        lc.insets = new Insets(0, 0, 0, 0);
        leftContent.add(welcome2, lc);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 0, 4, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;

        int row = 0;

        JLabel titulo = new JLabel("Iniciar sesión");
        titulo.setFont(new java.awt.Font(SEGOE, java.awt.Font.BOLD, 24));
        titulo.setForeground(TEXTO_TITULO);
        titulo.setHorizontalAlignment(JLabel.LEFT);
        c.gridy = row++;
        c.insets = new Insets(0, 0, 6, 0);
        rightPanel.add(titulo, c);

        JLabel subtitulo = new JLabel("Sistema de Gestión");
        subtitulo.setFont(new java.awt.Font(SEGOE, java.awt.Font.PLAIN, 13));
        subtitulo.setForeground(TEXTO_SECUNDARIO);
        subtitulo.setHorizontalAlignment(JLabel.LEFT);
        c.gridy = row++;
        c.insets = new Insets(0, 0, 22, 0);
        rightPanel.add(subtitulo, c);
        c.insets = new Insets(6, 0, 6, 0);

        JLabel lbUsuario = new JLabel("Usuario");
        lbUsuario.setFont(new java.awt.Font(SEGOE, java.awt.Font.PLAIN, 13));
        lbUsuario.setForeground(TEXTO_SECUNDARIO);
        c.gridy = row++;
        rightPanel.add(lbUsuario, c);

        ent.setColumns(16);
        ent.setFont(new java.awt.Font(SEGOE, java.awt.Font.PLAIN, 14));
        ent.setBackground(BG_CAMPO);
        ent.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDE_CAMPO, 1),
            new EmptyBorder(12, 14, 12, 14)));
        c.gridy = row++;
        c.insets = new Insets(0, 0, 16, 0);
        rightPanel.add(ent, c);
        c.insets = new Insets(6, 0, 6, 0);

        JLabel lbClave = new JLabel("Contraseña");
        lbClave.setFont(new java.awt.Font(SEGOE, java.awt.Font.PLAIN, 13));
        lbClave.setForeground(TEXTO_SECUNDARIO);
        c.gridy = row++;
        rightPanel.add(lbClave, c);

        ent2.setColumns(16);
        ent2.setEchoChar('*');
        ent2.setFont(new java.awt.Font(SEGOE, java.awt.Font.PLAIN, 14));
        ent2.setBackground(BG_CAMPO);
        ent2.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDE_CAMPO, 1),
            new EmptyBorder(12, 14, 12, 14)));
        c.gridy = row++;
        c.insets = new Insets(0, 0, 10, 0);
        rightPanel.add(ent2, c);
        c.insets = new Insets(6, 0, 6, 0);

        JCheckBox verClave = new JCheckBox("Mostrar contraseña");
        verClave.setOpaque(false);
        verClave.setFont(new java.awt.Font(SEGOE, java.awt.Font.PLAIN, 12));
        verClave.setForeground(TEXTO_SECUNDARIO);
        verClave.addActionListener(e -> ent2.setEchoChar(verClave.isSelected() ? (char) 0 : '*'));
        c.gridy = row++;
        c.insets = new Insets(0, 0, 24, 0);
        rightPanel.add(verClave, c);
        c.insets = new Insets(6, 0, 6, 0);

        RoundButton btnEntrar = new RoundButton("Iniciar sesión", 14);
        btnEntrar.setFont(new java.awt.Font(SEGOE, java.awt.Font.BOLD, 15));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setPreferredSize(new Dimension(280, 50));
        btnEntrar.setGradientColors(new Color(30, 58, 138), new Color(23, 37, 84));
        btnEntrar.setHoverGradientColors(new Color(29, 78, 216), new Color(30, 58, 138));
        btnEntrar.setShadowColor(new Color(30, 58, 138, 80));
        btnEntrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEntrar.addActionListener(evt -> {
            try {
                LeeButtonEjecuta(evt);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ventana.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        c.gridy = row++;
        rightPanel.add(btnEntrar, c);

        login.setVisible(true);
    }

    public void LeeButtonEjecuta(java.awt.event.ActionEvent evt) throws ClassNotFoundException, SQLException {
        String vv_nombre = ent.getText().toLowerCase();
        String vv_clave = ent2.getText().toLowerCase();
        String usuarioEncontrado = null;
        String rolEncontrado = null;
        int idUsuarioEncontrado = -1;
        try {
            Class.forName("org.sqlite.JDBC");

            Connection con = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");

            // Usar PreparedStatement para evitar SQL injection
            String sql = "SELECT id_usuario, usuario, clave, rol FROM login WHERE usuario = ? AND clave = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, vv_nombre);
            pstmt.setString(2, vv_clave);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                idUsuarioEncontrado = rs.getInt("id_usuario");
                usuarioEncontrado = rs.getString("usuario");
                rolEncontrado = rs.getString("rol");
                System.out.println("Login info: id=" + idUsuarioEncontrado + ", usuario=" + usuarioEncontrado + ", rol="
                        + rolEncontrado);
            }

            if (usuarioEncontrado == null) {
                JOptionPane.showMessageDialog(ent, "Login Incorrecto");
            } else {
                // Registrar login en auditoría
                AuditLogger.registrar(idUsuarioEncontrado, "Login", "LOGIN");

                // Pasar usuario, rol e id al menú principal
                Opciones llamar_menu = new Opciones(usuarioEncontrado, rolEncontrado, idUsuarioEncontrado);
                login.dispose();
            }

            rs.close();
            pstmt.close();
            con.close();

        } catch (HeadlessException | ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    public void NuevoUsuario(java.awt.event.ActionEvent evt) throws ClassNotFoundException, SQLException {
        JFrame ventana3 = new JFrame();
        ventana3.setSize(400, 400);
        ventana3.setLocationRelativeTo(null);
        ventana3.getContentPane().setBackground(rojo_transparente);
        ventana3.setLayout(null);

        // panel
        JPanel panel_centrado = new JPanel();
        panel_centrado.setBounds(50, 50, 280, 280);
        panel_centrado.setBackground(Color.white);
        panel_centrado.setLayout(null);

        JLabel etq_usuario = new JLabel("Nuevo Usuario");
        etq_usuario.setBounds(90, 20, 100, 40);

        JLabel etq_clave = new JLabel("Nueva COntraseña");
        etq_clave.setBounds(80, 120, 200, 40);

        // entradas
        ent_usuario.setBounds(65, 60, 150, 40);

        ent_clave.setBounds(65, 160, 150, 40);

        JButton boton_cambiar = new JButton("Agregar Usuario");
        boton_cambiar.setBounds(65, 230, 150, 40);

        ventana3.add(panel_centrado);
        // etiquetas en el panel
        panel_centrado.add(etq_usuario);
        panel_centrado.add(etq_clave);
        // entradas en el panel
        panel_centrado.add(ent_usuario);
        panel_centrado.add(ent_clave);
        panel_centrado.add(boton_cambiar);

        ventana3.setVisible(true);
    }
}

// Panel que pinta una imagen de fondo escalada al tamaño disponible
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        setOpaque(false);
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

// Panel con fondo en gradiente vertical para consistencia con Opciones.java
class GradientPanel extends JPanel {
    private final Color topColor;
    private final Color bottomColor;

    public GradientPanel(Color topColor, Color bottomColor) {
        this.topColor = topColor;
        this.bottomColor = bottomColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, topColor, 0, getHeight(), bottomColor);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
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
    private Color gradientStart = new Color(52, 152, 219);
    private Color gradientEnd = new Color(37, 99, 235);
    private Color hoverGradientStart;
    private Color hoverGradientEnd;
    private Color shadowColor = new Color(0, 0, 0, 60);
    private boolean hover;

    public RoundButton(String text, int cornerRadius) {
        super(text);
        this.cornerRadius = Math.max(0, cornerRadius);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setForeground(Color.WHITE);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    public void setGradientColors(Color start, Color end) {
        this.gradientStart = start;
        this.gradientEnd = end;
        repaint();
    }

    public void setHoverGradientColors(Color start, Color end) {
        this.hoverGradientStart = start;
        this.hoverGradientEnd = end;
        repaint();
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();

        if (shadowColor != null) {
            g2.setColor(shadowColor);
            g2.fillRoundRect(4, 6, width - 8, height - 6, cornerRadius, cornerRadius);
        }

        Color start = hover && hoverGradientStart != null ? hoverGradientStart : gradientStart;
        Color end = hover && hoverGradientEnd != null ? hoverGradientEnd : gradientEnd;

        if (start != null && end != null) {
            g2.setPaint(new GradientPaint(0, 0, start, width, height, end));
        } else {
            g2.setColor(getBackground() != null ? getBackground() : new Color(59, 130, 246));
        }

        g2.fillRoundRect(0, 0, width - 4, height - 6, cornerRadius, cornerRadius);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public void paintBorder(Graphics g) {
        // Sin borde para un look limpio
    }
}








