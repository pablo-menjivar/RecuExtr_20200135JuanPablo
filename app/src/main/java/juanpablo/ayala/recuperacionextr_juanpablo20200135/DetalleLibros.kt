package juanpablo.ayala.recuperacionextr_juanpablo20200135

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetalleLibros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_libros)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Recibo los valores que me paso desde el Adaptador
        val uuidLibro = intent.getStringExtra("UUID_Libro")
        val tituloLibro = intent.getStringExtra("tituloLibro")
        val autorLibro = intent.getStringExtra("autorLibro")
        val añoPublicacion = intent.getIntExtra("añoPublicacion", 0)
        val estadoLibro = intent.getStringExtra("estadoLibro")
        val ISBM = intent.getIntExtra("ISBM", 0)
        val generoLibro = intent.getStringExtra("generoLibro")
        val paginasLibro = intent.getIntExtra("paginasLibro", 0)
        val editorialLibro = intent.getStringExtra("editorialLibro")
        //Mando a llamar todos los elementos de la pantalla
        val btnBack = findViewById<ImageView>(R.id.imgBack)
        val txtUUIDLibro = findViewById<TextView>(R.id.txtUUIDLibroDetail)
        val txtTituloLibro = findViewById<TextView>(R.id.txtTituloLibroDetail)
        val txtAutorLibro = findViewById<TextView>(R.id.txtAutorLibroDetail)
        val txtAñoPublicacion = findViewById<TextView>(R.id.txtAñoPublicacionDetail)
        val txtEstadoLibro = findViewById<TextView>(R.id.txtEstadoLibroDetail)
        val txtISBM = findViewById<TextView>(R.id.txtISBMDetail)
        val txtGeneroLibro = findViewById<TextView>(R.id.txtGeneroLibroDetail)
        val txtPaginasLibro = findViewById<TextView>(R.id.txtPaginasLibroDetail)
        val txtEditorialLibro = findViewById<TextView>(R.id.txtEditorialLibroDetail)
        //Asigno los datos recibidos a mis TextView
        txtUUIDLibro.text = uuidLibro
        txtTituloLibro.text = tituloLibro
        txtAutorLibro.text = autorLibro
        txtAñoPublicacion.text = añoPublicacion.toString()
        txtEstadoLibro.text = estadoLibro
        txtISBM.text = ISBM.toString()
        txtGeneroLibro.text = generoLibro
        txtPaginasLibro.text = paginasLibro.toString()
        txtEditorialLibro.text = editorialLibro
        btnBack.setOnClickListener {
            val pantallaLibro = Intent(this, MainActivity::class.java)
            startActivity(pantallaLibro)
        }
    }
}