package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EmpleadosDataCollectionItem
import kotlinx.android.synthetic.main.activity_menu_principal.*

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)
        btnUnidadVacunacion.setOnClickListener {
            val intent= Intent(this,VerUnidadesActivity::class.java)
            startActivity(intent)
        }
        obtenerUsuario()
        btnEmpleados.setOnClickListener {
            val intent= Intent(this,VerEmpleadosActivity::class.java)
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
        btnCargos.setOnClickListener {
            val intent = Intent(this, VerCargosActivity::class.java)
            startActivity(intent)
        }
        btnPaises.setOnClickListener{
            val intent = Intent(this, VerPaisesActivity::class.java)
            startActivity(intent)
        }
        btnCivilComorb.setOnClickListener{
            val intent = Intent(this, VerCivilComorbilidadActivity::class.java)
            startActivity(intent)
        }
    }
    @SuppressLint("SetTextI18n")
    fun obtenerUsuario(){
        val intent = intent
        val objeto: EmpleadosDataCollectionItem? = intent.getParcelableExtra("usuario")
        if (objeto != null) {
            txvBienvenida.text = "Bienvenido: ${objeto.nombre}"
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}