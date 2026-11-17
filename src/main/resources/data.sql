-- ==========================================================
-- Datos semilla para ambiente DEV (H2)
-- Los usuarios son creados por DataInitializer con BCrypt al arrancar:
--   admin   / admin123     (ROLE_ADMIN)
--   operador/ oper123      (ROLE_OPERADOR)
--   consulta/ cons123      (ROLE_CONSULTA)
-- ==========================================================

INSERT INTO dependencia (nombre, descripcion, ubicacion, activa) VALUES
('Despacho del Alcalde',   'Oficina principal del alcalde',          'Piso 3 - Palacio Municipal', TRUE),
('Secretaria de Hacienda', 'Area financiera del municipio',          'Piso 2 - Palacio Municipal', TRUE),
('Secretaria de Gobierno', 'Seguridad y convivencia ciudadana',      'Piso 2 - Palacio Municipal', TRUE),
('Secretaria de Salud',    'Programas de salud publica',             'Calle 5 # 3-20',             TRUE);

INSERT INTO categoria (nombre, descripcion, vida_util_meses, activa) VALUES
('Equipos de computo', 'Computadores, laptops, monitores',       60,  TRUE),
('Mobiliario',         'Escritorios, sillas, archivadores',      120, TRUE),
('Vehiculos',          'Vehiculos oficiales del municipio',      120, TRUE),
('Equipos de oficina', 'Impresoras, scanners, fotocopiadoras',    48, TRUE);

INSERT INTO proveedor (nit, razon_social, telefono, correo, direccion, activo) VALUES
('900123456-1', 'Soluciones Tecnologicas SAS',  '6013001001', 'ventas@soltec.co',    'Cra 7 # 20-30',  TRUE),
('890234567-2', 'Muebles del Oriente Ltda',     '6012002002', 'info@muebor.co',      'Av 15 # 100-50', TRUE),
('860345678-3', 'Automotores Nacionales SA',    '6013003003', 'contacto@autonal.co', 'Cl 80 # 50-10',  TRUE);
