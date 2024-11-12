package juanpablo.ayala.recuperacionextr_juanpablo20200135.recyclerviewhelpers

import android.annotation.SuppressLint
import android.content.Intent
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import juanpablo.ayala.recuperacionextr_juanpablo20200135.DetalleLibros
import juanpablo.ayala.recuperacionextr_juanpablo20200135.R
import juanpablo.ayala.recuperacionextr_juanpablo20200135.modelo.ClaseConexion
import juanpablo.ayala.recuperacionextr_juanpablo20200135.modelo.Libros
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    fun actualizarLibro(tituloLibro: String, autorLibro: String, añoPublicacion: Int, estadoLibro: String, ISBM: Int, generoLibro: String,
                        paginasLibro: Int, editorialLibro: String, UUID_Libro: String) {
        //Creo una "coroutina"
        GlobalScope.launch(Dispatchers.IO) {
            //1- Creo un objeto de la clase conexión
            val objConexion = ClaseConexion().cadenaConexion()
            //2- Creo una variable que contenga un PrepareStatement
            val updateEnfermero = objConexion?.prepareStatement(
                "update tbLibros set nombreLibro = ?, autorLibro = ?, añoPublicacion = ?, " +
                        "estadoLibro = ?, ISBM = ?, generoLibro = ?, paginasLibro = ?, editorialLibro = ? where UUID_Enfermero = ?"
            )!!
            updateEnfermero.setString(1, tituloLibro)
            updateEnfermero.setString(2, autorLibro)
            updateEnfermero.setInt(3, añoPublicacion)
            updateEnfermero.setString(4, estadoLibro)
            updateEnfermero.setInt(5, ISBM)
            updateEnfermero.setString(6, generoLibro)
            updateEnfermero.setInt(7, paginasLibro)
            updateEnfermero.setString(8, editorialLibro)
            updateEnfermero.setString(9, UUID_Libro)
        }
    }
    //TODO:Creo la funcion para eliminar los libros en la base de datos///////////
    fun eliminarLibro (UUID_Libro: String, posicion: Int) {
        // Actualizar lista de datos y notificar al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)
        try {
            GlobalScope.launch(Dispatchers.IO) {
                //1- Creo un objeto de la clase conexion
                val objConexion = ClaseConexion().cadenaConexion()
                val delLibro = objConexion?.prepareStatement("delete from tbLibros where UUID_Libro = ?")!!
                delLibro.setString(1, UUID_Libro)
                delLibro.executeUpdate()

                //2-Hago un commit para que me amarre
                val commit = objConexion.prepareStatement("commit")!!
                commit.executeUpdate()
            }
            Datos = listaDatos.toList()
            //Quito los datos de la lista
            notifyItemRemoved(posicion)
            //Le notifico al adaptador
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }

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
                eliminarLibro(item.UUID_Libro, position)
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

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar")
            builder.setMessage("¿Desea editar el registro?")

            //Agregar cuadros de texto para que el usuario escriba los nuevos datos
            // Crear un layout para los cuadros de texto
            val layout = LinearLayout(context)
            layout.orientation = LinearLayout.VERTICAL
            //Creando los cuadros de texto
            val inputTitulo = EditText(context)
            inputTitulo.hint = item.tituloLibro
            layout.addView(inputTitulo)

            val inputAutor = EditText(context)
            inputAutor.hint = item.autorLibro
            layout.addView(inputAutor)

            val inputAño = EditText(context)
            inputAño.hint = item.añoPublicacion.toString()
            inputAño.inputType = InputType.TYPE_CLASS_NUMBER
            layout.addView(inputAño)

            val inputEstado = EditText(context)
            inputEstado.hint = item.estadoLibro
            layout.addView(inputEstado)

            val inputISBM = EditText(context)
            inputISBM.hint = item.ISBM.toString()
            inputISBM.inputType = InputType.TYPE_CLASS_NUMBER
            layout.addView(inputISBM)

            val inputGenero = EditText(context)
            inputGenero.hint = item.generoLibro
            layout.addView(inputGenero)

            val inputPaginas = EditText(context)
            inputPaginas.hint = item.paginasLibro.toString()
            inputPaginas.inputType = InputType.TYPE_CLASS_NUMBER
            layout.addView(inputPaginas)

            val inputEditorial = EditText(context)
            inputEditorial.hint = item.editorialLibro
            layout.addView(inputEditorial)

            //Agrego al Layout al AlertDialog
            builder.setView(layout)

            builder.setPositiveButton("Guardar Cambios") { dialog, which ->
                actualizarLibro(inputTitulo.text.toString(), inputAutor.text.toString(), inputAño.text.toString().toInt(), inputEstado.text.toString(),
                    inputISBM.text.toString().toInt(), inputGenero.text.toString(), inputPaginas.text.toString().toInt(), inputEditorial.text.toString(), item.UUID_Libro)
            }
            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        //TODO: Paso los detalles de la tabla al darle clic a la card
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleLibros::class.java)
            intent.putExtra("UUID_Libro", item.UUID_Libro)
            intent.putExtra("tituloLibro", item.tituloLibro)
            intent.putExtra("autorLibro", item.UUID_Libro)
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