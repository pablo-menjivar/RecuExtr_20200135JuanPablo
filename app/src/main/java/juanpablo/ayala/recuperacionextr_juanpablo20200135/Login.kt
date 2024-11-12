package juanpablo.ayala.recuperacionextr_juanpablo20200135

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import juanpablo.ayala.recuperacionextr_juanpablo20200135.modelo.ClaseConexion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val txtEdadLogin = findViewById<EditText>(R.id.txtEdadLogin)
        val txtCorreoLogin = findViewById<EditText>(R.id.txtCorreoLogin)
        val txtPasswordLogin = findViewById<EditText>(R.id.txtPasswordLogin)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val btnRegistrarme = findViewById<Button>(R.id.btnRegistrarme)
        val imgVerPassword = findViewById<ImageView>(R.id.imgVerPassword2)

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
                    //Encripto la contraseña usando la función de arriba
                    val contraseñaEncriptada = hashSHA256(txtPasswordLogin.text.toString())
                    //2- Creo una variable que contenga un PrepareStatement
                    val comprobarUsuario = objConexion?.prepareStatement("SELECT * FROM tbUsuarios WHERE nombreUsuario = ? AND apellidoUsuario = ? AND edadUsuario = ? AND correoElectronico = ? AND clave = ?")!!
                    comprobarUsuario.setString(1, txtNombreLogin.text.toString())
                    comprobarUsuario.setString(2, txtApellidoLogin.text.toString())
                    comprobarUsuario.setInt(3,txtEdadLogin.text.toString().toInt())
                    comprobarUsuario.setString(4, txtCorreoLogin.text.toString())
                    comprobarUsuario.setString(5, contraseñaEncriptada)
                    val resultado = comprobarUsuario.executeQuery()
                    //Si encuentra un resultado
                    if (resultado.next()) {
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@Login, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        }
                        startActivity(pantallaPrincipal)
                    } else {
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@Login, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                            println("contraseña $contraseñaEncriptada")
                        }
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
        //Programo el boton para mostrar u ocultar la contraseña
        imgVerPassword.setOnClickListener {
            if (txtPasswordLogin.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                txtPasswordLogin.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                txtPasswordLogin.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            //Para que no me cambie la fuente a `monospace`
            txtPasswordLogin.typeface = Typeface.DEFAULT
        }
    }
}