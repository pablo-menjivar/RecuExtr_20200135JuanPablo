package juanpablo.ayala.recuperacionextr_juanpablo20200135.recyclerviewhelpers

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import juanpablo.ayala.recuperacionextr_juanpablo20200135.R
import org.w3c.dom.Text

class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
    var txtTituloLibro = view.findViewById<TextView>(R.id.text_titulo)
    var txtAutorLibro = view.findViewById<TextView>(R.id.text_autor)
    var txtAñoPublicacionLibro = view.findViewById<TextView>(R.id.text_año)
    var txtEstadoLibro = view.findViewById<TextView>(R.id.text_estado)
    var txtISBMLibro = view.findViewById<TextView>(R.id.text_isbm)
    var txtGeneroLibro = view.findViewById<TextView>(R.id.text_genero)
    var txtNumPaginasLibro = view.findViewById<TextView>(R.id.text_paginas)
    var txtEditorialLibro = view.findViewById<TextView>(R.id.text_editorial)
    val btnEditarLibro = view.findViewById<ImageView>(R.id.icon_edit)
    val btnEliminarLibro = view.findViewById<ImageView>(R.id.icon_delete)
}