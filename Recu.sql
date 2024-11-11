CREATE TABLE tbLibros (
UUID_Libro VARCHAR2(100) PRIMARY KEY,
tituloLibro VARCHAR2(75) NOT NULL,
autorLibro VARCHAR2(50) NOT NULL,
añoPublicacion NUMBER(4,0) NOT NULL,
estadoLibro VARCHAR2(15) NOT NULL CHECK (estadoLibro IN ('nuevo', 'usado', 'seminuevo')),
ISBM VARCHAR2(30) NOT NULL,
generoLibro VARCHAR2(50) NOT NULL,
paginasLibro INT NOT NULL,
editorialLibro VARCHAR2(100) NOT NULL
);

CREATE TABLE tbUsuarios (
UUID_Usuario VARCHAR2(100) PRIMARY KEY,
nombreUsuario VARCHAR2(100) NOT NULL,
apellidoUsuario VARCHAR2(100) NOT NULL,
correoElectronico VARCHAR2(100) NOT NULL,
clave VARCHAR2(100) NOT NULL
);