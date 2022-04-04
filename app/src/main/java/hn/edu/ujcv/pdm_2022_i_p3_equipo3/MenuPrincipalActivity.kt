package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu_principal.*

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)
        btnUnidadVacunacion.setOnClickListener {
            val intent= Intent(this,VerUnidadesActivity::class.java)
            startActivity(intent)
        }
        btnEnfermeros.setOnClickListener {
            val intent= Intent(this,VerEnfermerosActivity::class.java)
            startActivity(intent)
        }
        btnComorbilidad.setOnClickListener {
            val intent= Intent(this,VerComorbilidadesActivity::class.java)
            startActivity(intent)
        }
        btnCiviles.setOnClickListener {
            val intent= Intent(this,VerCivilesActivity::class.java)
            startActivity(intent)
        }
        btnFabricantes.setOnClickListener {
            val intent= Intent(this,VerFabricantesActivity::class.java)
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
        btnRegionSanitaria.setOnClickListener {
            val intent = Intent(this, VerRegionSanitariaActivity::class.java)
            startActivity(intent)
        }
        btnMunicipio.setOnClickListener {
            val intent = Intent(this, VerMunicipiosActivity::class.java)
            startActivity(intent)
        }
        btnEstablecimientos.setOnClickListener {
            val intent = Intent(this, VerEstablecimientosActivity::class.java)
            startActivity(intent)
        }
        btnCentros.setOnClickListener {
            val intent = Intent(this, VerCentroVacunacionActivity::class.java)
            startActivity(intent)
        }
        btnDoctores.setOnClickListener {
            val intent = Intent(this, VerDoctoresActivity::class.java)
            startActivity(intent)
        }
    }
}