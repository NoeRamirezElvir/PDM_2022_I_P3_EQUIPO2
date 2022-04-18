package hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases

class Empleado(val id:Long? = 0,
               val codigo:Long? = 0,
               val dni: String?,
               val nombre:String? = "",
               val telefono:Long? = 0,
               val correo: String? = "",
               val password: String? = "",
               val cargo:Long? = 0,
               val unidad:Long? = 0) {
}