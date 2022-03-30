package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu_principal.*

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)
        btnEnfermeros.setOnClickListener {
            val intent= Intent(this,VerEnfermerosActivity::class.java)
            startActivity(intent)
        }
        btnCiviles.setOnClickListener {
            val intent= Intent(this,VerCivilesActivity::class.java)
            startActivity(intent)
        }
        btnVacuna.setOnClickListener {
            val intent= Intent(this,VerVacunasActivity::class.java)
            startActivity(intent)
        }
        btnCarnet.setOnClickListener {
            val intent= Intent(this,VerCarnetVacunacionActivity::class.java)
            startActivity(intent)
        }
        btnUsuarios.setOnClickListener {
            val intent= Intent(this,VerUsuarioActivity::class.java)
            startActivity(intent)
        }
    }
}