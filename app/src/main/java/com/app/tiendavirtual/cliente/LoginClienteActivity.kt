package com.app.tiendavirtual.cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.app.tiendavirtual.Constantes
import com.app.tiendavirtual.R
import com.app.tiendavirtual.databinding.ActivityLoginClienteBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginClienteBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        // Obtener el Web Client ID del google-services.json
        val webClientId = try {
            getString(R.string.default_web_client_id)
        } catch (e: Exception) {
            // Fallback: usar directamente el Web Client ID de google-services.json
            "411077828785-o1344rpse3564buua4rem1866r0flb1h.apps.googleusercontent.com"
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnLoginC.setOnClickListener {
            validarInfo()
        }

        // Iniciar sesion con una cuenta de Google
        binding.btnLoginGoogle.setOnClickListener {
            googleLogin()
        }

        binding.btnLoginTel.setOnClickListener {
            startActivity(Intent(this, LoginTelActivity::class.java))
        }

        binding.tvRegistrarC.setOnClickListener {
            startActivity(Intent(this@LoginClienteActivity, RegistroClienteActivity::class.java))
        }
    }

    private var email = ""
    private var password = ""
    private fun validarInfo() {
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.error = "Email inválido"
            binding.etEmail.requestFocus()
        } else if (password.isEmpty()){
            binding.etEmail.error = "Ingrese email"
            binding.etEmail.requestFocus()
        } else if (password.isEmpty()){
            binding.etPassword.error = "Ingrese password"
            binding.etPassword.requestFocus()
        } else {
            loginCliente()
        }
    }

    private fun loginCliente() {
        progressDialog.setMessage("Ingresando")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()
                Toast.makeText(this, "Bienvenido(a)", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "No se pudo iniciar sesión debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun googleLogin() {
        // Cerrar cualquier sesión previa de Google para evitar conflictos
        mGoogleSignInClient.signOut().addOnCompleteListener {
            val googleSignInIntent = mGoogleSignInClient.signInIntent
            googleSignInARL.launch(googleSignInIntent)
        }
    }

    private val googleSignInARL = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado ->
        if (resultado.resultCode == RESULT_OK){
            val data = resultado.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)
                if (cuenta.idToken != null) {
                    autenticacionGoogle(cuenta.idToken)
                } else {
                    Toast.makeText(this,"Error: No se pudo obtener el token de Google.",Toast.LENGTH_LONG).show()
                }
            } catch (e: ApiException){
                Toast.makeText(this,"Error de Google: ${e.statusCode} - ${e.message}",Toast.LENGTH_LONG).show()
            } catch (e: Exception){
                Toast.makeText(this, "Error inesperado: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "La operación de logueo ha sido cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun autenticacionGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { resultadoAuth ->
                if (resultadoAuth.additionalUserInfo!!.isNewUser){
                    //Si el usuario es nuevo, registrar su informacion
                    llenarInfoDB()
                } else {
                    //Si ya esta registrado
                    startActivity(Intent(this, MainActivityCliente::class.java))
                    finishAffinity()
                }
            }
            .addOnFailureListener { e->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun llenarInfoDB() {
        progressDialog.setMessage("Guardando información")

        val uid = firebaseAuth.uid
        val nombreC = firebaseAuth.currentUser?.displayName
        val emailC = firebaseAuth.currentUser?.email
        val tiempoRegistro = Constantes().obtenerTiempo()
        val datosCliente = HashMap<String, Any>()

        datosCliente["uid"] = "$uid"
        datosCliente["nombres"] = "$nombreC"
        datosCliente["email"] = "$emailC"
        datosCliente["tRegistro"] = "$tiempoRegistro"
        datosCliente["imagen"] = ""
        datosCliente["tipoUsuario"] = "cliente"

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(uid!!)
            .setValue(datosCliente)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}