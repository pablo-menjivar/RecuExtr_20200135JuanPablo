CREATE TABLE tbLibros (
UUID_Libro VARCHAR2(100) PRIMARY KEY,
tituloLibro VARCHAR2(75) NOT NULL,
autorLibro VARCHAR2(50) NOT NULL,
aņoPublicacion INT NOT NULL,
estadoLibro VARCHAR2(15) NOT NULL CHECK (LOWER(estadoLibro) IN ('nuevo', 'usado', 'seminuevo')),
ISBM INT NOT NULL,
generoLibro VARCHAR2(50) NOT NULL,
paginasLibro INT NOT NULL,
editorialLibro VARCHAR2(100) NOT NULL
);

CREATE TABLE tbUsuarios (
UUID_Usuario VARCHAR2(100) PRIMARY KEY,
nombreUsuario VARCHAR2(100) NOT NULL,
apellidoUsuario VARCHAR2(100) NOT NULL,
edadUsuario INT NOT NULL,
correoElectronico VARCHAR2(100) NOT NULL,
clave VARCHAR2(100) NOT NULL
);

select * from tbUsuarios;
select * from tbLibros;


