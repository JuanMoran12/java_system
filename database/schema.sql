-- Table: clientes

CREATE TABLE IF NOT EXISTS clientes (

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    cedula TEXT UNIQUE NOT NULL,

    nombre TEXT NOT NULL,

    apellido TEXT NOT NULL,

    telefono TEXT,

    direccion TEXT

);



-- Table: proveedores

CREATE TABLE IF NOT EXISTS proveedores (

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    rif TEXT UNIQUE NOT NULL,

    nom_proveedor TEXT NOT NULL,

    tlf_proveedor TEXT,

    dir_proveedor TEXT

);



-- Table: productos

CREATE TABLE IF NOT EXISTS productos (

    id_prod INTEGER PRIMARY KEY AUTOINCREMENT,

    nom_prod TEXT NOT NULL,

    pre_prod REAL NOT NULL,

    exi_prod REAL NOT NULL,

    stock_minimo INTEGER DEFAULT 5,

    id_proveedor INTEGER NOT NULL

);



-- Table: login

CREATE TABLE IF NOT EXISTS login (

    id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,

    usuario TEXT UNIQUE NOT NULL,

    clave TEXT NOT NULL,

    rol TEXT DEFAULT 'vendedor'

);



-- Table: facturas

CREATE TABLE IF NOT EXISTS facturas (

    id_factura INTEGER PRIMARY KEY AUTOINCREMENT,

    fec_factura DATETIME NOT NULL,

    ced_factura INTEGER NOT NULL,

    tipo_pago TEXT,

    monto_factura REAL NOT NULL,

    FOREIGN KEY (ced_factura) REFERENCES clientes(id)

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

    CHECK (car_factura IS NOT NULL OR car_factura IS NULL)

);



-- Table: compras

CREATE TABLE IF NOT EXISTS compras (

    id_compra INTEGER PRIMARY KEY AUTOINCREMENT,

    id_proveedor INTEGER NOT NULL,

    fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP,

    total_compra REAL NOT NULL,

    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id)

);



-- Table: detalle_compras

CREATE TABLE IF NOT EXISTS detalle_compras (

    id_detalle INTEGER PRIMARY KEY AUTOINCREMENT,

    id_compra INTEGER,

    id_producto INTEGER,

    cantidad INTEGER,

    costo_unitario REAL,

    FOREIGN KEY (id_compra) REFERENCES compras(id_compra),

    FOREIGN KEY (id_producto) REFERENCES productos(id_prod)

);



-- Table: audit_log

CREATE TABLE IF NOT EXISTS audit_log (

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    id_usuario INTEGER,

    modulo TEXT,

    accion TEXT CHECK(accion IN (

        'LOGIN', 

        'LOGOUT', 

        'CREAR_CLIENTE', 

        'EDITAR_CLIENTE', 

        'ELIMINAR_CLIENTE', 

        'CREAR_PRODUCTO', 

        'EDITAR_PRODUCTO', 

        'ELIMINAR_PRODUCTO', 

        'TRANSACCION'

    )),

    FOREIGN KEY (id_usuario) REFERENCES login(id_usuario) ON DELETE SET NULL

);