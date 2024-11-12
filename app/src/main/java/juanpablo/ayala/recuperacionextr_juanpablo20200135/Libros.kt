package juanpablo.ayala.recuperacionextr_juanpablo20200135

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import juanpablo.ayala.recuperacionextr_juanpablo20200135.modelo.ClaseConexion
import juanpablo.ayala.recuperacionextr_juanpablo20200135.modelo.Libros
import juanpablo.ayala.recuperacionextr_juanpablo20200135.recyclerviewhelpers.Adaptador
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Libros : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_libros, container, false)
        //1-Mando a llamar el RecyclerView de la vista
        val rcvLibros = root.findViewById<RecyclerView>(R.id.rcvLibros)
        //Asigno un linearlayout al RecyclerView
        rcvLibros.layoutManager = LinearLayoutManager(requireContext())
        //TODO:Creo una funcion para obtener los libros de la base de datos
        fun obtenerLibros(): List<Libros> {
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbLibros")!!
            val libros = mutableListOf<Libros>()
            while (resultSet.next()) {
                val UUID_Libro = resultSet.getString("UUID_Libro")
                val tituloLibro = resultSet.getString("tituloLibro")
                val autorLibro = resultSet.getString("autorLibro")
                val añoPublicacion = resultSet.getInt("añoPublicacion")
                val estadoLibro = resultSet.getString("estadoLibro")
                val ISBM = resultSet.getInt("ISBM")
                val generoLibro = resultSet.getString("generoLibro")
                val paginasLibro = resultSet.getInt("paginasLibro")
                val editorialLibro = resultSet.getString("editorialLibro")

                val libro = Libros(UUID_Libro, tituloLibro, autorLibro, añoPublicacion, estadoLibro, ISBM, generoLibro, paginasLibro, editorialLibro)

                libros.add(libro)
            }
            return libros
        }
        //Asigno un adaptador al RecyclerView
        //El adaptador se encarga de actualizar los datos en la lista
        CoroutineScope(Dispatchers.IO).launch {
            val librosDB = obtenerLibros()
            withContext(Dispatchers.Main) {
                val adapter = Adaptador(librosDB)
                rcvLibros.adapter = adapter
            }
        }
        return root
    }
}