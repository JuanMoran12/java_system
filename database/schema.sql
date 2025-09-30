
-- Table: clientes
CREATE TABLE IF NOT EXISTS clientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cedula TEXT NOT NULL UNIQUE,
    nombre TEXT NOT NULL,
    apellido TEXT NOT NULL,
    telefono TEXT,
    direccion TEXT
);

-- Table: productos
CREATE TABLE IF NOT EXISTS productos (
    id_prod INTEGER PRIMARY KEY AUTOINCREMENT,
    nom_prod TEXT NOT NULL,
    pre_prod REAL NOT NULL CHECK(pre_prod >= 0),
    exi_prod INTEGER NOT NULL DEFAULT 0 CHECK(exi_prod >= 0)
);

-- Table: login
CREATE TABLE IF NOT EXISTS login (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario TEXT NOT NULL UNIQUE,
    clave TEXT NOT NULL
);

-- Table: facturas
CREATE TABLE IF NOT EXISTS facturas (
    id_factura INTEGER PRIMARY KEY AUTOINCREMENT,
    fec_factura TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ced_factura INTEGER NOT NULL,
    tipo_pago TEXT NOT NULL,
    monto_factura REAL NOT NULL CHECK(monto_factura >= 0),
    FOREIGN KEY (ced_factura) REFERENCES clientes(id) ON DELETE RESTRICT
);

-- Table: carrito
CREATE TABLE IF NOT EXISTS carrito (
    car_id INTEGER PRIMARY KEY AUTOINCREMENT,
    car_ced INTEGER NOT NULL,
    car_pro INTEGER NOT NULL,
    car_can INTEGER NOT NULL CHECK(car_can > 0),
    car_factura INTEGER,
    FOREIGN KEY (car_ced) REFERENCES clientes(id) ON DELETE CASCADE,
    FOREIGN KEY (car_pro) REFERENCES productos(id_prod) ON DELETE CASCADE,
    FOREIGN KEY (car_factura) REFERENCES facturas(id_factura) ON DELETE SET NULL,
    CHECK (car_factura IS NOT NULL OR car_factura IS NULL)  -- Allows NULL for items not yet in an invoice
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_clientes_cedula ON clientes(cedula);
CREATE INDEX IF NOT EXISTS idx_productos_nombre ON productos(nom_prod);
CREATE INDEX IF NOT EXISTS idx_facturas_fecha ON facturas(fec_factura);
CREATE INDEX IF NOT EXISTS idx_carrito_cedula ON carrito(car_ced);
CREATE INDEX IF NOT EXISTS idx_carrito_factura ON carrito(car_factura);

-- Insert default admin user (username: admin, password: admin123)
INSERT OR IGNORE INTO login (usuario, clave) VALUES ('admin', 'admin123');