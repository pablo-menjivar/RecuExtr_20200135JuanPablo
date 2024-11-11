package juanpablo.ayala.recuperacionextr_juanpablo20200135

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import juanpablo.ayala.recuperacionextr_juanpablo20200135.modelo.ClaseConexion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.sql.SQLException

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //1- Mandamos a traer a todos los elementos de la vista
        val txtNombreLogin = findViewById<EditText>(R.id.txtNombreLogin)
        val txtApellidoLogin = findViewById<EditText>(R.id.txtApellidoLogin)
        val txtCorreoLogin = findViewById<EditText>(R.id.txtCorreoLogin)
        val txtPasswordLogin = findViewById<EditText>(R.id.txtPasswordLogin)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val btnRegistrarme = findViewById<Button>(R.id.btnRegistrarme)

        fun hashSHA256(input: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
        //2- Programo los botones
        btnIngresar.setOnClickListener {
            val pantallaPrincipal = Intent(this, MainActivity::class.java)
            //Dentro de una coroutina hago un select en la base de datos
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    //1-Creo un objeto de la clase conexion
                    val objConexion = ClaseConexion().cadenaConexion()
                    //2- Creo una variable que contenga un PrepareStatement
                    //MUCHA ATENCION! hace un select where el correo y la contraseña sean iguales a los que el usuario escribe
                    //Si el select encuentra un resultado es por que el usuario y contraseña si están
                    //en la base de datos, si se equivoca al escribir algo, no encontrará nada el select
                    val comprobarUsuario = objConexion?.prepareStatement("SELECT * FROM tbUsuarios WHERE nombreUsuario = ? AND apellidoUsuario = ? AND correoElectronico = ? AND clave = ?")!!
                    comprobarUsuario.setString(1, txtNombreLogin.text.toString())
                    comprobarUsuario.setString(2, txtApellidoLogin.text.toString())
                    comprobarUsuario.setString(3, txtCorreoLogin.text.toString())
                    comprobarUsuario.setString(4, contraseñaEncriptada)
                    val resultado = comprobarUsuario.executeQuery()
                    //Si encuentra un resultado
                    if (resultado.next()) {
                        startActivity(pantallaPrincipal)
                    } else {
                        println("Usuario no encontrado, verifique las credenciales")
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                    println("Error de SQL: ${e.message}")
                }
            }
        }
        btnRegistrarme.setOnClickListener {
            //Cambio de pantalla
            val pantallaRegistrarme = Intent(this, Registro::class.java)
            startActivity(pantallaRegistrarme)
        }
    }
}