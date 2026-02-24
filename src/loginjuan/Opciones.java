package loginjuan;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Lenovo
 */
class Opciones {
    // Entradas para borrar
    JTextField ent_dluser = new JTextField();
    JTextField ent_dlpasw = new JPasswordField();

    // Colores que coinciden exactamente con el login (ventana.java)
    private final Color COLOR_FONDO_PRINCIPAL = Color.WHITE;
    private final Color COLOR_BOTON_CERRAR = new Color(153, 27, 27);
    private final Color COLOR_BOTON_CERRAR_HOVER = new Color(127, 17, 17);
    private final Color COLOR_TEXTO_PRINCIPAL = new Color(15, 23, 42);
    private final Color COLOR_TEXTO_SECUNDARIO = new Color(71, 85, 105);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_ESTADISTICAS = new Color(248, 250, 252);
    private final Color COLOR_ACCENT = new Color(30, 58, 138); // Mismo azul del login
    private final Color COLOR_SIDEBAR_GRADIENT_TOP = new Color(30, 58, 138); // Igual al login
    private final Color COLOR_SIDEBAR_GRADIENT_BOTTOM = new Color(23, 37, 84); // Igual al login
    private final Color COLOR_PLACEHOLDER_BG = new Color(248, 250, 252);
    private final Color COLOR_BORDE_CARD = new Color(203, 213, 225);

    // El menú principal
    JFrame opciones = new JFrame("Sistema de Gestión");
    JPanel panel_central;

    public String cli;
    private String rolUsuario; // Rol del usuario: 'admin' o 'vendedor'
    private String nombreUsuario; // Nombre del usuario logueado
    private int idUsuario; // ID del usuario en la tabla login

    // Método auxiliar para crear botones del menú con diseño que coincide con el login
    private RoundButton createMenuButton(String text, int x, int y, int width, int height) {
        RoundButton button = new RoundButton(text, 14);
        button.setBounds(x, y, width, height);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        // Usar los mismos colores del botón del login
        button.setGradientColors(new Color(30, 41, 59, 180), new Color(15, 23, 42, 220));
        button.setHoverGradientColors(new Color(29, 78, 216), new Color(30, 58, 138));
        button.setShadowColor(new Color(0, 0, 0, 50));

        // Padding interno para el texto
        button.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));

        return button;
    }

    // Método para obtener el resumen mensual de la base de datos
    private String[] obtenerResumenMensual() {
        String[] datos = { "—", "0", "0" }; // [0] = periodo, [1] = clientes, [2] = facturas

        Connection conn = null;
        java.sql.Statement stmt = null;
        java.sql.ResultSet rs = null;
        java.sql.ResultSet rsPeriodo = null;

        try {
            // Cargar el controlador JDBC
            Class.forName("org.sqlite.JDBC");

            // Establecer conexión con la base de datos
            conn = DriverManager.getConnection("jdbc:sqlite:c:ventasdb.db");
            stmt = conn.createStatement();

            // ÚNICA consulta: resumen del mes actual (zona local)
            String sql = "SELECT " +
                    "  strftime('%Y-%m', f.fec_factura, 'localtime') AS periodo, " +
                    "  COUNT(DISTINCT f.ced_factura) AS total_clientes, " +
                    "  COUNT(f.id_factura) AS total_facturas " +
                    "FROM facturas f " +
                    "WHERE strftime('%Y-%m', f.fec_factura, 'localtime') = strftime('%Y-%m', 'now', 'localtime')";

            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                datos[0] = rs.getString("periodo");
                datos[1] = String.valueOf(rs.getInt("total_clientes"));
                datos[2] = String.valueOf(rs.getInt("total_facturas"));
            } else {
                // Si no hay datos este mes, al menos devolver el periodo actual
                rsPeriodo = stmt.executeQuery("SELECT strftime('%Y-%m', 'now', 'localtime') AS periodo");
                if (rsPeriodo.next()) {
                    datos[0] = rsPeriodo.getString("periodo");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar estadísticas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cerrar recursos
            try {
                if (rsPeriodo != null)
                    rsPeriodo.close();
            } catch (Exception e) {
            }
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
            }
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }

        return datos;
    }

    // Método para crear un panel de datos con estilo del login
    private RoundedPanel createDataPanel(String title, String value, int x, int y, int width, int height) {
        RoundedPanel panel = new RoundedPanel(28); // Mismo radio que el login
        panel.setBounds(x, y, width, height);
        panel.setBackground(COLOR_ESTADISTICAS);
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_CARD, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Título
        JLabel titleLabel = new JLabel(title.toUpperCase());
        titleLabel.setBounds(25, 20, width - 50, 25);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(COLOR_TEXTO_SECUNDARIO);
        panel.add(titleLabel);

        // Valor
        JLabel valueLabel = new JLabel(value);
        valueLabel.setBounds(25, 50, width - 50, 60);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valueLabel.setForeground(COLOR_ACCENT);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(valueLabel);

        // Indicador de tendencia (decorativo)
        JLabel trendLabel = new JLabel("↑ 12% vs mes anterior");
        trendLabel.setBounds(25, 110, width - 50, 20);
        trendLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trendLabel.setForeground(new Color(16, 185, 129)); // Verde éxito
        panel.add(trendLabel);

        return panel;
    }

    // Paneles placeholder con estilo consistente del login
    private RoundedPanel createPlaceholderPanel(String title, String description, String badgeText,
            int x, int y, int width, int height) {
        RoundedPanel panel = new RoundedPanel(28); // Mismo radio que el login
        panel.setBounds(x, y, width, height);
        panel.setBackground(COLOR_PLACEHOLDER_BG);
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_CARD, 1));

        JLabel badge = new JLabel(badgeText);
        badge.setOpaque(true);
        badge.setBackground(new Color(30, 58, 138, 30)); // Usar el azul del login
        badge.setForeground(new Color(30, 58, 138));
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setBounds(20, 20, 100, 24);
        panel.add(badge);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setBounds(20, 55, width - 40, 35);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(COLOR_TEXTO_PRINCIPAL);
        panel.add(titleLabel);

        JLabel descriptionLabel = new JLabel("<html><div style='color:#64748b;'>" + description + "</div></html>");
        descriptionLabel.setBounds(20, 95, width - 40, 60);
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(descriptionLabel);

        // "Action" placeholder
        JLabel actionLabel = new JLabel("CONFIGURAR MÓDULO →");
        actionLabel.setBounds(20, height - 45, width - 40, 25);
        actionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionLabel.setForeground(COLOR_ACCENT);
        panel.add(actionLabel);

        return panel;
    }

    public Opciones(String fila, String rol, int idUsuario) {
        this.nombreUsuario = fila;
        this.rolUsuario = (rol != null) ? rol : "vendedor";
        this.idUsuario = idUsuario;
        boolean esAdmin = "admin".equalsIgnoreCase(this.rolUsuario);
        // Configuración de la ventana principal
        opciones.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full screen
        opciones.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        opciones.setLocationRelativeTo(null);
        opciones.setLayout(null);
        opciones.getContentPane().setBackground(COLOR_FONDO_PRINCIPAL);
        opciones.setVisible(true); // Make frame visible and allow it to maximize first

        // Panel lateral (use proportional sizing so UI adapts to window size)
        int W = opciones.getWidth();
        int H = opciones.getHeight() - 20;
        // match login window proportions: lateral panel roughly 25% of width
        int lateralWidth = Math.max(280, (int) (W * 0.25));
        int centralWidth = W - lateralWidth;

        GradientPanel panel_lateral = new GradientPanel(COLOR_SIDEBAR_GRADIENT_TOP, COLOR_SIDEBAR_GRADIENT_BOTTOM);
        panel_lateral.setBounds(0, 0, lateralWidth, H);
        panel_lateral.setLayout(null);

        // Panel decorativo superior con el mismo estilo del login
        RoundedPanel headerPanel = new RoundedPanel(0);
        headerPanel.setBounds(0, 0, lateralWidth, 150);
        headerPanel.setBackground(new Color(23, 37, 84)); // Mismo color del gradiente del login
        headerPanel.setLayout(null);
        panel_lateral.add(headerPanel);

        // Logo y título mejorados (Clic para volver al Dashboard)
        ImageIcon sidebarLogo = new ImageIcon("images/logo_w.png");
        if (sidebarLogo.getImageLoadStatus() != java.awt.MediaTracker.COMPLETE) {
            sidebarLogo = new ImageIcon("logo_w.png"); // Fallback
        }
        java.awt.Image scaledSidebar = sidebarLogo.getImage().getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledSidebar));
        iconLabel.setBounds(Math.max(20, lateralWidth / 2 - 35), 25, 70, 70);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showDashboard();
            }
        });
        headerPanel.add(iconLabel);

        JLabel titulo = new JLabel(esAdmin ? "ADMINISTRADOR" : "VENDEDOR");
        titulo.setBounds(20, 100, lateralWidth - 40, 35);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        titulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showDashboard();
            }
        });
        headerPanel.add(titulo);

        // Información del usuario con estilo del login
        RoundedPanel userPanel = new RoundedPanel(15);
        userPanel.setBounds(20, 160, lateralWidth - 40, 50);
        userPanel.setBackground(new Color(23, 37, 84, 180)); // Usar el color del login con transparencia
        userPanel.setLayout(null);
        panel_lateral.add(userPanel);

        JLabel userIcon = new JLabel("👤");
        userIcon.setBounds(15, 10, 30, 30);
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        userPanel.add(userIcon);

        JLabel userInfo = new JLabel(
                "<html><div style='text-align: left;'>Bienvenido<br><b>" + fila + "</b></div></html>");
        userInfo.setBounds(50, 5, lateralWidth - 90, 40);
        userInfo.setForeground(Color.WHITE);
        userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userPanel.add(userInfo);

        // Línea separadora con estilo sutil del login
        JPanel separador = new JPanel();
        separador.setBounds(20, 230, lateralWidth - 40, 1);
        separador.setBackground(new Color(226, 232, 240, 100)); // Color más sutil
        panel_lateral.add(separador);

        // Menú de navegación reorganizado por áreas
        int btnH = 38; // Altura fija más pequeña
        int btnW = Math.max(200, (int) (lateralWidth * 0.85));
        int btnX = (lateralWidth - btnW) / 2;
        int startY = 245;
        int spacing = btnH + 3; // Espaciado mínimo entre botones
        int sectionSpacing = 12; // Espaciado entre secciones

        // --- PANEL PRINCIPAL ---
        JLabel lblGeneral = new JLabel("GENERAL");
        lblGeneral.setBounds(btnX + 10, startY, btnW, 18);
        lblGeneral.setForeground(new Color(148, 163, 184));
        lblGeneral.setFont(new Font("Segoe UI", Font.BOLD, 10));
        panel_lateral.add(lblGeneral);
        startY += 22;

        RoundButton btnInicio = createMenuButton("🏠 Inicio / Menu", btnX, startY, btnW, btnH);
        btnInicio.addActionListener(e -> showDashboard());
        panel_lateral.add(btnInicio);
        startY += spacing + sectionSpacing;

        // --- ÁREA COMERCIAL ---
        JLabel lblComercial = new JLabel("ÁREA COMERCIAL");
        lblComercial.setBounds(btnX + 10, startY, btnW, 18);
        lblComercial.setForeground(new Color(148, 163, 184));
        lblComercial.setFont(new Font("Segoe UI", Font.BOLD, 10));
        panel_lateral.add(lblComercial);
        startY += 22;

        RoundButton btnVentas = createMenuButton("🛒 Ventas", btnX, startY, btnW, btnH);
        btnVentas.addActionListener(e -> showProductos("", "")); // Ventas es la interfaz de facturación
        panel_lateral.add(btnVentas);
        startY += spacing;

        RoundButton btnClientes = createMenuButton("👥 Clientes", btnX, startY, btnW, btnH);
        btnClientes.addActionListener(e -> showClientes());
        panel_lateral.add(btnClientes);
        startY += spacing + sectionSpacing;

        // --- ÁREA DE LOGÍSTICA ---
        JLabel lblLogistica = new JLabel("ÁREA DE LOGÍSTICA");
        lblLogistica.setBounds(btnX + 10, startY, btnW, 18);
        lblLogistica.setForeground(new Color(148, 163, 184));
        lblLogistica.setFont(new Font("Segoe UI", Font.BOLD, 10));
        panel_lateral.add(lblLogistica);
        startY += 22;

        RoundButton btnProductos = createMenuButton("📦 Productos", btnX, startY, btnW, btnH);
        btnProductos.addActionListener(e -> showInventario()); // Productos es el catálogo/stock
        panel_lateral.add(btnProductos);
        startY += spacing;

        // Proveedores solo visible para admin
        if (esAdmin) {
            RoundButton btnProveedores = createMenuButton("🏢 Proveedores", btnX, startY, btnW, btnH);
            btnProveedores.addActionListener(e -> showProveedores());
            panel_lateral.add(btnProveedores);
            startY += spacing;
        }

        // Compras solo visible para admin
        if (esAdmin) {
            RoundButton btnCompras = createMenuButton("🚚 Compras", btnX, startY, btnW, btnH);
            btnCompras.addActionListener(e -> showCompras()); // Compras es proveedores/entrada
            panel_lateral.add(btnCompras);
            startY += spacing;
        }
        startY += sectionSpacing;

        // --- ÁREA ADMINISTRATIVA (solo admin) ---
        if (esAdmin) {
            JLabel lblAdministrativa = new JLabel("ÁREA ADMINISTRATIVA");
            lblAdministrativa.setBounds(btnX + 10, startY, btnW, 18);
            lblAdministrativa.setForeground(new Color(148, 163, 184));
            lblAdministrativa.setFont(new Font("Segoe UI", Font.BOLD, 10));
            panel_lateral.add(lblAdministrativa);
            startY += 22;

            RoundButton btnReportes = createMenuButton("📄 Reportes", btnX, startY, btnW, btnH);
            btnReportes.addActionListener(e -> showVentas()); // Reportes es el histórico
            panel_lateral.add(btnReportes);
            startY += spacing;

            RoundButton btnLogs = createMenuButton("📋 Logs del Sistema", btnX, startY, btnW, btnH);
            btnLogs.addActionListener(e -> showLogs());
            panel_lateral.add(btnLogs);
            startY += spacing;
        }

        // Botón de cierre de sesión con estilo destacado (posicionado dinámicamente)
        int btnCerrarY = H - btnH - 25; // 25px desde el fondo
        RoundButton btnCerrarSesion = createMenuButton("🚪 Cerrar Sesión", btnX, btnCerrarY, btnW, btnH);
        btnCerrarSesion.setGradientColors(COLOR_BOTON_CERRAR, new Color(185, 28, 28));
        btnCerrarSesion.setHoverGradientColors(new Color(220, 38, 38), COLOR_BOTON_CERRAR_HOVER);
        btnCerrarSesion.setShadowColor(new Color(185, 28, 28, 120));
        btnCerrarSesion.addActionListener(e -> opciones.dispose());
        panel_lateral.add(btnCerrarSesion);

        // Panel central con fondo moderno
        panel_central = new JPanel();
        panel_central.setBounds(lateralWidth, 0, centralWidth, H);
        panel_central.setBackground(COLOR_FONDO_PRINCIPAL);
        panel_central.setLayout(null);

        // Agregar componentes al frame
        opciones.add(panel_lateral);
        opciones.add(panel_central);

        showDashboard();
    }

    private void addLogoCentral() {
        ImageIcon logoIcon = new ImageIcon("images/logo_b.png");
        if (logoIcon.getImageLoadStatus() != java.awt.MediaTracker.COMPLETE) {
            logoIcon = new ImageIcon("logo_b.png"); // Fallback
        }
        java.awt.Image img = logoIcon.getImage();
        java.awt.Image newImg = img.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(newImg));
        int logoW = 60;
        int logoH = 60;
        // Posicionar en la esquina superior derecha del panel central
        logoLabel.setBounds(panel_central.getWidth() - logoW - 30, 20, logoW, logoH);
        panel_central.add(logoLabel);
        panel_central.setComponentZOrder(logoLabel, 0); // Asegurar que esté al frente
    }

    private void showVentas() {
        panel_central.removeAll();
        addLogoCentral();
        Ventas ventas = new Ventas();
        JPanel ventasPanel = ventas.getVentasPanel(panel_central.getWidth(), panel_central.getHeight());
        panel_central.add(ventasPanel);
        panel_central.revalidate();
        panel_central.repaint();
    }

    private void showInventario() {
        panel_central.removeAll();
        addLogoCentral();
        ProductosCatalogo catalogoProductos = new ProductosCatalogo(this.rolUsuario, this.nombreUsuario,
                this.idUsuario);
        JPanel catalogoPanel = catalogoProductos.getProductosPanel(panel_central.getWidth(), panel_central.getHeight());
        catalogoPanel.setBounds(0, 0, panel_central.getWidth(), panel_central.getHeight());
        panel_central.add(catalogoPanel);
        panel_central.revalidate();
        panel_central.repaint();
    }

    private void showProveedores() {
        panel_central.removeAll();
        addLogoCentral();
        ProveedoresModulo proveedoresModulo = new ProveedoresModulo(this.rolUsuario, this.nombreUsuario, this.idUsuario);
        JPanel proveedoresUI = proveedoresModulo.getProveedoresPanel(panel_central.getWidth(), panel_central.getHeight());
        proveedoresUI.setBounds(0, 0, panel_central.getWidth(), panel_central.getHeight());
        panel_central.add(proveedoresUI);
        panel_central.revalidate();
        panel_central.repaint();
    }

    private void showCompras() {
        panel_central.removeAll();
        addLogoCentral();
        Compras compras = new Compras();
        JPanel comprasPanel = compras.getComprasPanel(panel_central.getWidth(), panel_central.getHeight());
        panel_central.add(comprasPanel);
        panel_central.revalidate();
        panel_central.repaint();
    }

    private void showDashboard() {
        panel_central.removeAll();
        addLogoCentral();
        // Tarjeta de contenido con estilo del login
        RoundedPanel card = new RoundedPanel(28); // Mismo radio que el login
        int cardMargin = 30;
        int cardX = cardMargin;
        int cardY = cardMargin;
        int cardW = panel_central.getWidth() - (2 * cardMargin);
        int cardH = panel_central.getHeight() - (2 * cardMargin);
        card.setBounds(cardX, cardY, cardW, cardH);
        card.setBackground(COLOR_TARJETA);
        card.setLayout(null);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_CARD, 2), // Borde de 2px como el login
                BorderFactory.createEmptyBorder(25, 35, 35, 35)));

        // Internal padding for components inside the card
        int internalPaddingX = 30;

        // Mensaje de bienvenida
        JLabel bienvenida = new JLabel("Panel de Control");
        bienvenida.setBounds(internalPaddingX, 30, cardW - (2 * internalPaddingX) - 250, 45);
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 32));
        bienvenida.setForeground(COLOR_TEXTO_PRINCIPAL);
        card.add(bienvenida);

        // Panel del precio del dólar con estilo refinado del login
        RoundedPanel dolarPanel = new RoundedPanel(18);
        int dolarWidth = 230;
        int dolarHeight = 90;
        dolarPanel.setBounds(cardW - internalPaddingX - dolarWidth - 70, 30, dolarWidth, dolarHeight);
        dolarPanel.setBackground(new Color(16, 185, 129, 20)); // Verde suave más sutil
        dolarPanel.setLayout(null);
        dolarPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(16, 185, 129, 100), 2),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        card.add(dolarPanel);

        JLabel dolarIcono = new JLabel("💵");
        dolarIcono.setBounds(10, 8, 30, 30);
        dolarIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        dolarPanel.add(dolarIcono);

        JLabel dolarTitulo = new JLabel("Precio USD");
        dolarTitulo.setBounds(45, 10, 150, 25);
        dolarTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dolarTitulo.setForeground(new Color(5, 150, 105));
        dolarPanel.add(dolarTitulo);

        // Obtener precio del dólar en un hilo separado para no bloquear la UI
        JLabel dolarPrecio = new JLabel("Cargando...");
        dolarPrecio.setBounds(10, 40, dolarWidth - 30, 35);
        dolarPrecio.setFont(new Font("Segoe UI", Font.BOLD, 28));
        dolarPrecio.setForeground(new Color(5, 150, 105));
        dolarPanel.add(dolarPrecio);

        // Cargar precio en segundo plano
        new Thread(() -> {
            String precio = DolarAPIClient.obtenerPrecioDolar();
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (precio.equals("No disponible") || precio.equals("Error") || precio.equals("Timeout")) {
                    dolarPrecio.setText("—");
                    dolarPrecio.setFont(new Font("Segoe UI", Font.BOLD, 24));
                    dolarTitulo.setText("USD (offline)");
                } else {
                    dolarPrecio.setText("Bs. " + precio);
                }
            });
        }).start();

        JLabel instrucciones = new JLabel("Bienvenido al Sistema de Gestión - Gestione su negocio de manera eficiente");
        instrucciones.setBounds(internalPaddingX, 80, cardW - (2 * internalPaddingX), 30);
        instrucciones.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        instrucciones.setForeground(COLOR_TEXTO_SECUNDARIO);
        card.add(instrucciones);

        // Obtener estadísticas del mes
        String[] datosMensuales = obtenerResumenMensual();
        String periodo = datosMensuales[0];
        String totalClientes = datosMensuales[1];
        String totalFacturas = datosMensuales[2];

        // Primera fila: KPIs principales
        int panelY = 130;
        int panelHeight = Math.max(180, (int) (cardH * 0.24));
        int panelSpacing = 20;
        int panelWidth = (cardW - (2 * internalPaddingX) - (2 * panelSpacing)) / 3;

        // Primer panel: Período
        RoundedPanel panel1 = createDataPanel("📅 Período", periodo, internalPaddingX, panelY, panelWidth, panelHeight);
        card.add(panel1);

        // Segundo panel: Clientes
        RoundedPanel panel2 = createDataPanel("👥 Clientes", totalClientes,
                internalPaddingX + panelWidth + panelSpacing, panelY, panelWidth, panelHeight);
        card.add(panel2);

        // Tercer panel: Facturas
        RoundedPanel panel3 = createDataPanel("📄 Facturas", totalFacturas,
                internalPaddingX + (panelWidth + panelSpacing) * 2, panelY, panelWidth, panelHeight);
        card.add(panel3);

        // Segunda fila: placeholders para KPIs futuros
        int placeholderY = panelY + panelHeight + 40;
        int placeholderHeight = Math.max(200, (int) (cardH * 0.28));
        int placeholderSpacing = 24;
        int placeholderWidth = (cardW - (2 * internalPaddingX) - placeholderSpacing) / 2;

        RoundedPanel placeholder1 = createPlaceholderPanel(
                "Pronóstico de Ventas",
                "Agrega aquí la proyección de ingresos para los próximos 30 días o integra tus KPIs de ventas.",
                "Próximamente",
                internalPaddingX,
                placeholderY,
                placeholderWidth,
                placeholderHeight);
        card.add(placeholder1);

        RoundedPanel placeholder2 = createPlaceholderPanel(
                "Nivel de Inventario",
                "Reserva espacio para destacar productos críticos, rotación y alertas de abastecimiento.",
                "Widget libre",
                internalPaddingX + placeholderWidth + placeholderSpacing,
                placeholderY,
                placeholderWidth,
                placeholderHeight);
        card.add(placeholder2);

        // Texto descriptivo
        int descY = placeholderY + placeholderHeight + 30;
        JLabel lblDescripcion = new JLabel(
                "<html><div style='text-align: center;'>Seleccione una opción del menú lateral para comenzar<br>o personalice estos espacios con nuevos indicadores clave.</div>");
        lblDescripcion.setBounds(internalPaddingX + 20, descY, cardW - (2 * internalPaddingX + 40), 60);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescripcion.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblDescripcion.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(lblDescripcion);

        panel_central.add(card);
        panel_central.revalidate();
        panel_central.repaint();
    }

    private void showClientes() {
        panel_central.removeAll();
        addLogoCentral();
        ClientesModulo clientesModulo = new ClientesModulo(this.rolUsuario, this.nombreUsuario, this.idUsuario);

        // Configurar listener para cuando se inicie una venta
        clientesModulo.setOnIniciarVentaListener((cedula, nombreCompleto) -> {
            showProductos(cedula, nombreCompleto);
        });

        JPanel clientesUI = clientesModulo.getClientesPanel(panel_central.getWidth(), panel_central.getHeight());
        clientesUI.setBounds(0, 0, panel_central.getWidth(), panel_central.getHeight());
        panel_central.add(clientesUI);
        panel_central.revalidate();
        panel_central.repaint();
    }

    public void showProductos(String cedulaCliente, String nombreCliente) {
        panel_central.removeAll();
        addLogoCentral();
        Productos productos = new Productos(cedulaCliente, nombreCliente, this.rolUsuario, this.nombreUsuario,
                this.idUsuario);
        JPanel prodPanel = productos.getEmbedPanel(panel_central.getWidth(), panel_central.getHeight());
        prodPanel.setBounds(0, 0, panel_central.getWidth(), panel_central.getHeight());
        panel_central.add(prodPanel);
        panel_central.revalidate();
        panel_central.repaint();
    }

    private void showLogs() {
        panel_central.removeAll();
        addLogoCentral();
        LogsModulo logsModulo = new LogsModulo();
        JPanel logsPanel = logsModulo.getLogsPanel(panel_central.getWidth(), panel_central.getHeight());
        logsPanel.setBounds(0, 0, panel_central.getWidth(), panel_central.getHeight());
        panel_central.add(logsPanel);
        panel_central.revalidate();
        panel_central.repaint();
    }

}
