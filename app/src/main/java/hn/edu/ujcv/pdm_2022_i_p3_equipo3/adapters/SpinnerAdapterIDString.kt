package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

class SpinnerAdapterIDString(var id:Long,var texto:String) {
    override fun toString(): String {
        return texto
    }
}