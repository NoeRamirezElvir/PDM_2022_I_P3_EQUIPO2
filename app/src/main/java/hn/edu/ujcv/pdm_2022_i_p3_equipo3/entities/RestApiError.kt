package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

data class RestApiError(val httpStatus:String, val errorMessage:String, val errorDetails:String){
}