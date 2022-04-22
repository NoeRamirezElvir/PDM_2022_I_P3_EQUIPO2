package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//ricoy: 192.168.1.3
//noe: 192.168.0.12
class RestEngine {
    companion object{
        fun buildService(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            return Retrofit.Builder()
                .baseUrl("http://192.168.1.3:8080/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
    }
}