package com.ejer_propuestos.b5

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import com.ejer_propuestos.b5.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import javax.security.auth.Subject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var fotoCamera: ActivityResultLauncher<Intent>

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        solicitarPermisos()



        binding.telefonoInputLayout.setStartIconOnClickListener()
        {
            Toast.makeText(applicationContext, "Llamando", Toast.LENGTH_SHORT).show()
        }

        binding.mailInputLayout.setStartIconOnClickListener()
        {
            Toast.makeText(applicationContext, "Mandando Email", Toast.LENGTH_SHORT).show()
        }




        crearContacto()

        binding.botonCamara.setOnClickListener()
        {
            tomarFoto()
        }

        binding.telefonoInputLayout.setStartIconOnClickListener()
        {
            hacerLlamada()
        }

        binding.mailInputLayout.setStartIconOnClickListener()
        {
            mandarMail(binding.mailInputEditText.text.toString())
        }


    }

    private fun tomarFoto() {
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fotoCamera.launch(camaraIntent)
    }

    private fun crearContacto() {
        fotoCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == Activity.RESULT_OK)
                    binding.botonCamara.setImageBitmap(result.data?.extras?.get("data") as Bitmap)
            }
    }

    private fun mandarMail(subject: String) {

        val mandarMail = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }

        if (findViewById<TextInputEditText>(R.id.mailInputEditText).text?.isEmpty() == true) {
            val contextView = findViewById<TextInputEditText>(R.id.mailInputEditText)

            Snackbar.make(contextView, "Destinatario no puede estar en blanco",Snackbar.LENGTH_INDEFINITE)
                .setAction("ACEPTAR") {
                    // Responds to click on the action
                }
                .show()
        } else {
            startActivity(mandarMail)
        }


    }

    private fun hacerLlamada() {
        val hacerLlamada =
            Intent(ACTION_CALL, Uri.parse("tel:" + binding.telefonoInputEditText.text.toString()))
        startActivity(hacerLlamada)
    }

    val RESPUESTA_PERMISOS = 111

    @RequiresApi(Build.VERSION_CODES.M)
    private fun solicitarPermisos() {
        if (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                CALL_PHONE
            ) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                READ_VOICEMAIL
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(arrayOf(CAMERA, CALL_PHONE, READ_VOICEMAIL), RESPUESTA_PERMISOS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when {
            RESPUESTA_PERMISOS == requestCode && grantResults.isNotEmpty() -> {
                for (i in 0..permissions.size - 1) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(
                            applicationContext,
                            "Permiso Concedido" + permissions[i],
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        }
    }
}


