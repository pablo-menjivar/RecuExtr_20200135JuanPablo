package juanpablo.ayala.recuperacionextr_juanpablo20200135

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import juanpablo.ayala.recuperacionextr_juanpablo20200135.modelo.ClaseConexion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //1- Mando a llamar a todos los elementos de la vista
        val imgAtrasregistrarse = findViewById<ImageView>(R.id.imgAtrasregistrarse)
        val txtNombreRegistro = findViewById<EditText>(R.id.txtNombreRegistro)
        val txtApellidoRegistro = findViewById<EditText>(R.id.txtApellidoRegistro)
        val txtCorreoRegistro = findViewById<EditText>(R.id.txtCorreoRegistro)
        val txtPasswordRegistro = findViewById<EditText>(R.id.txtPasswordRegistro)
        val txtConfirmarPassword = findViewById<EditText>(R.id.txtConfirmarPassword)
        val imgVerPassword = findViewById<ImageView>(R.id.imgVerPassword)
        val imgVerConfirmacionPassword = findViewById<ImageView>(R.id.imgVerConfirmacionPassword)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val btnRegresarLogin = findViewById<Button>(R.id.btnRegresarLogin)
        //2- Programo los botones para crear cuenta, ir a inicio de sesión y mostrar y ocultar contraseña
        btnCrearCuenta.setOnClickListener{
            // Guardo en variables los valores que escribió el usuario
            val nombre = txtNombreRegistro.text.toString()
            val apellido = txtApellidoRegistro.text.toString()
            val correo = txtCorreoRegistro.text.toString()
            val contraseña = txtPasswordRegistro.text.toString()
            val confirmacion = txtConfirmarPassword.text.toString()

            var errores = false
            //Validaciones
            //TODO: Primero verificamos que ningun campo este vacio/////////////
            if (nombre.isEmpty()) {
                txtNombreRegistro.error = "El nombre es obligatorio"
                errores = true
            } else {
                txtNombreRegistro.error = null
            }
            if (apellido.isEmpty()) {
                txtApellidoRegistro.error = "El apellido es obligatorio"
                errores = true
            } else {
                txtApellidoRegistro.error = null
            }
            if (correo.isEmpty()) {
                txtCorreoRegistro.error = "El correo es obligatorio"
                errores = true
            } else {
                txtCorreoRegistro.error = null
            }
            if (contraseña.isEmpty()) {
                txtPasswordRegistro.error = "Debe de ingresar una contraseña"
                errores = true
            } else {
                txtPasswordRegistro.error = null
            }
            if (confirmacion.isEmpty()) {
                txtConfirmarPassword.error = "Debe confirmar su contraseña"
                errores = true
            }
            //TODO: Segundo verificamos que el correo tenga formato de correo electronico/////////////
            if (!correo.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+[.][a-z]+"))) {
                txtCorreoRegistro.error = "El correo no tiene un formato válido"
                errores = true
            } else {
                txtCorreoRegistro.error = null
            }
            //TODO: Tercero se asegura que la contraseña tenga mas de ocho caracteres/////////////
            if (contraseña.length <= 8) {
                txtPasswordRegistro.error = "La contraseña debe tener más de 8 caracteres"
                errores = true
            } else {
                txtPasswordRegistro.error = null
            }
            if (confirmacion.length <= 8) {
                txtConfirmarPassword.error = "La contraseña debe tener más de 8 caracteres"
                errores = true
            } else {
                txtConfirmarPassword.error = null
            }

            if (errores) {
                Toast.makeText(this@Registro, "Datos no guardados", Toast.LENGTH_SHORT).show()
            }
            else {
                GlobalScope.launch(Dispatchers.IO) {
                    //Creo un objeto de la clase conexion
                    val objConexion = ClaseConexion().cadenaConexion()
                    //Creo una variable que contenga un PrepareStatement
                    val crearUsuario = objConexion?.prepareStatement("INSERT INTO tbUsuarios(UUID_usuario, nombreUsuario, apellidoUsuario, correoElectronico, clave) VALUES (?, ?, ?, ?, ?)")!!
                    crearUsuario.setString(1, UUID.randomUUID().toString())
                    crearUsuario.setString(2, txtNombreRegistro.text.toString())
                    crearUsuario.setString(3, txtApellidoRegistro.text.toString())
                    crearUsuario.setString(4, txtCorreoRegistro.text.toString())
                    crearUsuario.setString(5, txtPasswordRegistro.text.toString())
                    crearUsuario.executeUpdate()
                    withContext(Dispatchers.Main) {
                        //Abro otra corrutina o "Hilo" para mostrar un mensaje y limpiar los campos
                        //Lo hago en el Hilo Main por que el hilo IO no permite mostrar nada en pantalla
                        Toast.makeText(this@Registro, "Usuario creado", Toast.LENGTH_SHORT).show()
                        txtNombreRegistro.setText("")
                        txtApellidoRegistro.setText("")
                        txtCorreoRegistro.setText("")
                        txtPasswordRegistro.setText("")
                        txtConfirmarPassword.setText("")
                    }
                }
            }
            //Programo los botones para mostrar u ocultar la contraseña
            imgVerPassword.setOnClickListener {
                if (txtPasswordRegistro.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    txtPasswordRegistro.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                } else {
                    txtPasswordRegistro.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
            imgVerConfirmacionPassword.setOnClickListener {
                if (txtConfirmarPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    txtConfirmarPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                } else {
                    txtConfirmarPassword.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }
        //Al darle clic a la flechita de arriba - Regresar al Login
        imgAtrasregistrarse.setOnClickListener {
            val pantallaLogin = Intent(this, Login::class.java)
            startActivity(pantallaLogin)
        }
        //Al darle clic a al boton que ya tengo una cuenta - Regresar al Login
        btnRegresarLogin.setOnClickListener {
            val pantallaLogin = Intent(this, Login::class.java)
            startActivity(pantallaLogin)
        }
    }
}