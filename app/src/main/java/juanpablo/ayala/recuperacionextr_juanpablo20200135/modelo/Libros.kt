package juanpablo.ayala.recuperacionextr_juanpablo20200135.modelo

data class Libros (
    val UUID_Libro: String,
    var tituloLibro: String,
    var autorLibro: String,
    var añoPublicacion: Int,
    var estadoLibro: String,
    var ISBM: String,
    var generoLibro: String,
    var paginasLibro: Int,
    var editorialLibro: String
)
