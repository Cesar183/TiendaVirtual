package com.app.tiendavirtual

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.tiendavirtual.cliente.MainActivityCliente
import com.app.tiendavirtual.vendedor.MainActivityVendedor
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        firebaseAuth = FirebaseAuth.getInstance()

        verBienvenida()
    }

    private fun verBienvenida() {
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                startActivity(Intent(this@SplashScreenActivity, MainActivityCliente::class.java))
                finish()
            }
        }.start()
    }

    private fun comprobarTipoUsuario(){
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            //startActivity(Intent(this, RegistroClienteActivity::class.java))
            Toast.makeText(applicationContext, "No se encuentra registrado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Ha iniciado sesión", Toast.LENGTH_SHORT).show()
        }
    }
}