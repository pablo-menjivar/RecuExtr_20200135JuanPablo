package juanpablo.ayala.recuperacionextr_juanpablo20200135.recyclerviewhelpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import juanpablo.ayala.recuperacionextr_juanpablo20200135.DetalleLibros
import juanpablo.ayala.recuperacionextr_juanpablo20200135.R
import juanpablo.ayala.recuperacionextr_juanpablo20200135.modelo.ClaseConexion
import juanpablo.ayala.recuperacionextr_juanpablo20200135.modelo.Libros
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
class Adaptador(var Datos: List<Libros>): RecyclerView.Adapter<ViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    fun actualicePantalla(UUID: String, nuevoTitulo: String, nuevoAutor: String, nuevoAño: Int, nuevoEstado: String, nuevoISBM: Int, nuevoGenero: String,
                          nuevoNumPaginas: Int, nuevaEditorial: String) {
        val index = Datos.indexOfFirst { it.UUID_Libro == UUID }
        Datos[index].tituloLibro = nuevoTitulo
        Datos[index].autorLibro = nuevoAutor
        Datos[index].añoPublicacion = nuevoAño
        Datos[index].estadoLibro = nuevoEstado
        Datos[index].ISBM = nuevoISBM
        Datos[index].generoLibro = nuevoGenero
        Datos[index].paginasLibro = nuevoNumPaginas
        Datos[index].editorialLibro = nuevaEditorial
        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
    }
    //TODO:Creo la funcion para actualizar los libros en la base de datos///////////
    fun actualizarLibro(context: Context, tituloLibro: String, autorLibro: String, añoPublicacion: Int, estadoLibro: String, ISBM: Int, generoLibro: String,
                        paginasLibro: Int, editorialLibro: String, UUID_Libro: String) {
        //Creo una "coroutina"
        GlobalScope.launch(Dispatchers.IO) {
            try {
                //1- Creo un objeto de la clase conexión
                val objConexion = ClaseConexion().cadenaConexion()
                //2- Creo una variable que contenga un PrepareStatement
                val updateEnfermero = objConexion?.prepareStatement(
                    "update tbLibros set tituloLibro = ?, autorLibro = ?, añoPublicacion = ?, estadoLibro = ?, ISBM = ?, generoLibro = ?, paginasLibro = ?, " +
                            "editorialLibro = ? where UUID_Libro = ?")!!
                updateEnfermero.setString(1, tituloLibro)
                updateEnfermero.setString(2, autorLibro)
                updateEnfermero.setInt(3, añoPublicacion)
                updateEnfermero.setString(4, estadoLibro)
                updateEnfermero.setInt(5, ISBM)
                updateEnfermero.setString(6, generoLibro)
                updateEnfermero.setInt(7, paginasLibro)
                updateEnfermero.setString(8, editorialLibro)
                updateEnfermero.setString(9, UUID_Libro)

                updateEnfermero.executeUpdate()
                //3- Hago un commit para que me guarde los cambios
                val commit = objConexion.prepareStatement("commit")!!
                commit.executeUpdate()
                withContext(Dispatchers.Main) {
                    //4- Actualizo la pantalla
                    actualicePantalla(UUID_Libro, tituloLibro, autorLibro, añoPublicacion, estadoLibro, ISBM, generoLibro, paginasLibro, editorialLibro)
                    Toast.makeText(context, "Se ha actualizado el libro", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error al actualizar el libro", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //TODO:Creo la funcion para eliminar los libros en la base de datos///////////
    fun eliminarLibro (context: Context, UUID_Libro: String, posicion: Int) {
        // Actualizar lista de datos y notificar al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                //1- Creo un objeto de la clase conexion
                val objConexion = ClaseConexion().cadenaConexion()
                val delLibro = objConexion?.prepareStatement("delete from tbLibros where UUID_Libro = ?")!!
                delLibro.setString(1, UUID_Libro)
                delLibro.executeUpdate()

                //2-Hago un commit para que me amarre
                val commit = objConexion.prepareStatement("commit")!!
                commit.executeUpdate()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Se ha eliminado el libro", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error al eliminar el libro", Toast.LENGTH_SHORT).show()
                }
            }
        }
        Datos = listaDatos.toList()
        //Quito los datos de la lista
        notifyItemRemoved(posicion)
        //Le notifico al adaptador
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_libros, parent, false)

        return ViewHolder(vista)
    }
    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = Datos[position]
        holder.txtTituloLibro.text = item.tituloLibro
        holder.txtAutorLibro.text = item.autorLibro
        holder.txtAñoPublicacionLibro.text = item.añoPublicacion.toString()
        holder.txtEstadoLibro.text = item.estadoLibro
        holder.txtISBMLibro.text = item.ISBM.toString()
        holder.txtGeneroLibro.text = item.generoLibro
        holder.txtNumPaginasLibro.text = item.paginasLibro.toString()
        holder.txtEditorialLibro.text = item.editorialLibro
        holder.btnEliminarLibro.setOnClickListener {
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Quiere eliminar este elemento?")

            builder.setPositiveButton("Sí") { dialog, which ->
                eliminarLibro(context, item.UUID_Libro, position)
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        holder.btnEditarLibro.setOnClickListener {
            //Creo un AlertDialog
            val context = holder.itemView.context

            //Agregar cuadros de texto para que el usuario escriba los nuevos datos
            // Crear el contenedor para los EditTexts
            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(50, 40, 50, 10) // Padding opcional para mejorar el diseño
            }
            //Creando los cuadros de texto
            val editTextTitulo = EditText(context).apply {
                hint = "Nombre actual: ${item.tituloLibro}"
                setText(item.tituloLibro) //Mostrar el titulo actual
                inputType = InputType.TYPE_CLASS_TEXT
            }
            val editTextAutor = EditText(context).apply {
                hint = "Autor actual: ${item.autorLibro}"
                setText(item.autorLibro) //Mostrar el autor actual
                inputType = InputType.TYPE_CLASS_TEXT
            }
            val editTextAño = EditText(context).apply {
                hint = "Año actual: ${item.añoPublicacion}"
                setText(item.añoPublicacion.toString()) //Mostrar el año actual
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            val editTextEstado = EditText(context).apply {
                hint = "Estado actual: ${item.estadoLibro}"
                setText(item.estadoLibro) //Mostrar el estado actual
                inputType = InputType.TYPE_CLASS_TEXT
            }
            val editTextISBM = EditText(context).apply {
                hint = "ISBM actual: ${item.ISBM}"
                setText(item.ISBM.toString()) //Mostrar el ISBM actual
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            val editTextGenero = EditText(context).apply {
                hint = "Genero actual: ${item.generoLibro}"
                setText(item.generoLibro) //Mostrar el genero actual
            }
            val editTextPaginas = EditText(context).apply {
                hint = "Paginas actual: ${item.paginasLibro}"
                setText(item.paginasLibro.toString()) //Mostrar el numero de paginas actual
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            val editTextEditorial = EditText(context).apply {
                hint = "Editorial actual: ${item.editorialLibro}"
                setText(item.editorialLibro) //Mostrar la editorial actual
            }
            //Agrego los edittext al layout
            layout.addView(editTextTitulo)
            layout.addView(editTextAutor)
            layout.addView(editTextAño)
            layout.addView(editTextEstado)
            layout.addView(editTextISBM)
            layout.addView(editTextGenero)
            layout.addView(editTextPaginas)
            layout.addView(editTextEditorial)
            //Mostrar el dialog con dos campos
            AlertDialog.Builder(context)
                .setTitle("Editar Libro")
                .setMessage("¿Deseas actualizar los campos del libro?")
                .setView(layout)
                .setPositiveButton("Sí") { dialog, which ->
                    //Obtengo los nuevos valores y actualizo
                    val nuevoTitulo = editTextTitulo.text.toString()
                    val nuevoAutor = editTextAutor.text.toString()
                    val nuevoAño = editTextAño.text.toString().toInt()
                    val nuevoEstado = editTextEstado.text.toString()
                    val nuevoISBM = editTextISBM.text.toString().toInt()
                    val nuevoGenero = editTextGenero.text.toString()
                    val nuevoNumPaginas = editTextPaginas.text.toString().toInt()
                    val nuevaEditorial = editTextEditorial.text.toString()
                    actualizarLibro(context, nuevoTitulo, nuevoAutor, nuevoAño, nuevoEstado, nuevoISBM, nuevoGenero, nuevoNumPaginas, nuevaEditorial, item.UUID_Libro)
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
        //TODO: Paso los detalles de la tabla al darle clic a la card
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleLibros::class.java)
            intent.putExtra("UUID_Libro", item.UUID_Libro)
            intent.putExtra("tituloLibro", item.tituloLibro)
            intent.putExtra("autorLibro", item.autorLibro)
            intent.putExtra("añoPublicacion", item.añoPublicacion)
            intent.putExtra("estadoLibro", item.estadoLibro)
            intent.putExtra("ISBM", item.ISBM)
            intent.putExtra("generoLibro", item.generoLibro)
            intent.putExtra("paginasLibro", item.paginasLibro)
            intent.putExtra("editorialLibro", item.editorialLibro)

            context.startActivity(intent)
        }
    }
}