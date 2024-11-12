package juanpablo.ayala.recuperacionextr_juanpablo20200135

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import juanpablo.ayala.recuperacionextr_juanpablo20200135.modelo.ClaseConexion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class addLibros : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_add_libros, container, false)
        //1-Mando a llamar a los elementos de la vista
        val btnGuardar = root.findViewById<Button>(R.id.btnGuardar)
        val txtTituloLibro = root.findViewById<EditText>(R.id.txtTituloLibro)
        val txtAutorLibro = root.findViewById<EditText>(R.id.txtAutorLibro)
        val txtAñoPublicacion = root.findViewById<EditText>(R.id.txtAñoPublicacion)
        val txtEstadoLibro = root.findViewById<EditText>(R.id.txtEstadoLibro)
        val txtISBM = root.findViewById<EditText>(R.id.txtISBM)
        val txtGeneroLibro = root.findViewById<EditText>(R.id.txtGeneroLibro)
        val txtPaginasLibro = root.findViewById<EditText>(R.id.txtPaginasLibro)
        val txtEditorialLibro = root.findViewById<EditText>(R.id.txtEditorialLibro)
        //2-Programo el boton de guardar
        btnGuardar.setOnClickListener {
            //Guardo en variables los valores que ingreso el usuario
            val tituloLibro = txtTituloLibro.text.toString()
            val autorLibro = txtAutorLibro.text.toString()
            val añoPublicacion = txtAñoPublicacion.text.toString().toIntOrNull()
            val estadoLibro = txtEstadoLibro.text.toString()
            val isbm = txtISBM.text.toString().toIntOrNull()
            val generoLibro = txtGeneroLibro.text.toString()
            val paginasLibro = txtPaginasLibro.text.toString().toIntOrNull()
            val editorialLibro = txtEditorialLibro.text.toString()

            var errores = false
            //Agrego validaciones
            //TODO: Primero verificamos que ningun campo este vacio/////////////
            if (tituloLibro.isEmpty()) {
                txtTituloLibro.error = "El titulo es obligatorio"
                errores = true
            } else { txtTituloLibro.error = null }
            if (autorLibro.isEmpty()) {
                txtAutorLibro.error = "El autor es obligatorio"
                errores = true
            } else { txtAutorLibro.error = null }
            if (añoPublicacion.toString().isEmpty()) {
                txtAñoPublicacion.error = "El año de publicacion es obligatorio"
                errores = true
            } else { txtAñoPublicacion.error = null }
            if (estadoLibro.isEmpty()) {
                txtEstadoLibro.error = "El estado es obligatorio"
                errores = true
            } else { txtEstadoLibro.error = null }
            if (generoLibro.isEmpty()) {
                txtGeneroLibro.error = "El género es obligatorio"
                errores = true
            } else { txtGeneroLibro.error = null }
            if (paginasLibro.toString().isEmpty()) {
                txtPaginasLibro.error = "El número de páginas es obligatorio"
                errores = true
            } else { txtPaginasLibro.error = null }
            if (editorialLibro.isEmpty()) {
                txtEditorialLibro.error = "La editorial es obligatoria"
                errores = true
            } else { txtEditorialLibro.error = null }
            // TODO: Segundo verificamos que el año de publicacion no sobrepase el año actual y que solo contenga numeros//////////
            if (añoPublicacion !in 0..2024) {
                txtAñoPublicacion.error = "El año de publicación debe estar entre 0 y 2024"
                errores = true
            } else { txtAñoPublicacion.error = null }
            //TODO: Tercero verificamos que el IBSN no contenga mas de 13 cifras//////////
            if (isbm !in 0..999999999) {
                txtISBM.error = "El ISBN debe contener no puede contener mas de 9 cifras"
                errores = true
            } else { txtISBM.error = null }
            // TODO: Cuarto verificamos el numero de paginas no contenga mas de 4 cifras y que solo contenga numeros//////////
            if (paginasLibro !in 0..9999) {
                txtPaginasLibro.error = "El número de páginas debe contener solo números"
                errores = true
            } else { txtPaginasLibro.error = null }
            if (errores) {
                Toast.makeText(requireContext(), "Datos no guardados", Toast.LENGTH_SHORT).show()
            }
            if (!errores) {
                //Creo un dialogo de confirmacion
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Confirmación")
                builder.setMessage("¿Desea guardar los datos?")
                builder.setIcon(R.drawable.ic_check)
                //Si el usuario elige que si
                builder.setPositiveButton("Sí") { dialog, which ->
                    CoroutineScope(Dispatchers.IO).launch {
                        //Creo un objeto de la clase conexion
                        val objConexion = ClaseConexion().cadenaConexion()
                        //Creo una variable que contenga un PrepareStatement
                        val añadirLibro = objConexion?.prepareStatement("INSERT INTO tbLibros(UUID_Libro, tituloLibro, autorLibro, añoPublicacion, estadoLibro, ISBM, generoLibro, paginasLibro, editorialLibro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")!!
                        añadirLibro.setString(1, UUID.randomUUID().toString())
                        añadirLibro.setString(2, txtTituloLibro.text.toString())
                        añadirLibro.setString(3, txtAutorLibro.text.toString())
                        añadirLibro.setInt(4, txtAñoPublicacion.text.toString().toInt())
                        añadirLibro.setString(5, txtEstadoLibro.text.toString())
                        añadirLibro.setInt(6, txtISBM.text.toString().toInt())
                        añadirLibro.setString(7, txtGeneroLibro.text.toString())
                        añadirLibro.setInt(8, txtPaginasLibro.text.toString().toInt())
                        añadirLibro.setString(9, txtEditorialLibro.text.toString())
                        añadirLibro.executeUpdate()
                        //Muestro un mensaje y se limpian los campos
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Libro guardado", Toast.LENGTH_SHORT).show()
                            txtTituloLibro.setText("")
                            txtAutorLibro.setText("")
                            txtAñoPublicacion.setText("")
                            txtEstadoLibro.setText("")
                            txtISBM.setText("")
                            txtGeneroLibro.setText("")
                            txtPaginasLibro.setText("")
                            txtEditorialLibro.setText("")
                        }
                    }
                }
                //Si el usuario elige que no
                builder.setNegativeButton("No") { dialog, which ->
                    Toast.makeText(requireContext(), "Datos no guardados", Toast.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
        return root
    }
}