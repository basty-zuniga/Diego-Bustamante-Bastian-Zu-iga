package com.example.MPAZ

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.MPAZ.ui.theme.EscuelaEscritoraMarcelaPazTheme
import com.example.colegio.data.ReservaRepository
import com.example.colegio.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm = LoginViewModel(ReservaRepository(applicationContext))
        setContent {
            EscuelaEscritoraMarcelaPazTheme {
                PantallaLogin(vm, onLogin = { rol ->
                    if (rol == "ADMIN") startActivity(Intent(this, AdminActivity::class.java))
                    else startActivity(Intent(this, UsuarioActivity::class.java))
                })
            }
        }
    }
}

@Composable
fun PantallaLogin(vm: LoginViewModel, onLogin: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Escuela Marcela Paz", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(if (vm.isRegister) "Registro" else "Iniciar Sesión", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = vm.correo,
            onValueChange = { vm.onCorreo(it) },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,          // Letra blanca al escribir
                unfocusedTextColor = Color.White,        // Letra blanca
                focusedContainerColor = Color(0xFF1E293B),   // Fondo gris oscuro dentro de la caja
                unfocusedContainerColor = Color(0xFF1E293B), // Fondo gris oscuro dentro de la caja
                focusedLabelColor = Color(0xFF60A5FA),      // Etiqueta azul al hacer clic
                unfocusedLabelColor = Color(0xFF94A3B8),    // Etiqueta gris claro
                focusedBorderColor = Color(0xFF60A5FA),     // Borde azul
                unfocusedBorderColor = Color(0xFF475569)    // Borde gris
            )
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = vm.contrasena,
            onValueChange = { vm.onPass(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,          // Letra blanca
                unfocusedTextColor = Color.White,        // Letra blanca
                focusedContainerColor = Color(0xFF1E293B),   // Fondo gris oscuro
                unfocusedContainerColor = Color(0xFF1E293B), // Fondo gris oscuro
                focusedLabelColor = Color(0xFF60A5FA),      // Etiqueta azul
                unfocusedLabelColor = Color(0xFF94A3B8),    // Etiqueta gris claro
                focusedBorderColor = Color(0xFF60A5FA),     // Borde azul
                unfocusedBorderColor = Color(0xFF475569)    // Borde gris
            )
        )


        if (vm.isRegister) {
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = vm.rol == "DOCENTE", onClick = { vm.onRol("DOCENTE") })
                Text("Docente")
                Spacer(Modifier.width(16.dp))
                RadioButton(selected = vm.rol == "ADMIN", onClick = { vm.onRol("ADMIN") })
                Text("Administrador")
            }
        }

        if (vm.error != null) {
            Spacer(Modifier.height(16.dp))
            Text(vm.error!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                if (vm.isRegister) vm.registrar() else vm.login()?.let { onLogin(it.rol) }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(if (vm.isRegister) "Crear Cuenta" else "Entrar")
        }

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = { vm.toggleMode() }) {
            Text(if (vm.isRegister) "¿Ya tienes cuenta? Inicia sesión" else "¿No tienes cuenta? Regístrate")
        }
    }
}