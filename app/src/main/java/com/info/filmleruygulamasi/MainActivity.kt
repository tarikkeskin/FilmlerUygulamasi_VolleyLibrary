package com.info.filmleruygulamasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var kategoriListe:ArrayList<Kategoriler>
    private lateinit var adapter:KategoriAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarKategori.title = "Kategoriler"
        setSupportActionBar(toolbarKategori)

        kategoriRv.setHasFixedSize(true)
        kategoriRv.layoutManager = LinearLayoutManager(this)

        tumKategoriler()
    }

    fun tumKategoriler(){
        var url = "http://kasimadalan.pe.hu/filmler/tum_kategoriler.php"

        val istek = StringRequest(Request.Method.GET,url, { cevap ->

            try {
                kategoriListe = ArrayList()

                val jsonObject = JSONObject(cevap)
                val kategoriler = jsonObject.getJSONArray("kategoriler")

                for (i in 0 until kategoriler.length()){
                    val k = kategoriler.getJSONObject(i)

                    val kategori = Kategoriler(k.getInt("kategori_id"),k.getString("kategori_ad"))

                    kategoriListe.add(kategori)
                }
                adapter = KategoriAdapter(this,kategoriListe)

                kategoriRv.adapter = adapter

            }catch (e:Exception){
                e.printStackTrace()
            }

        }, {  })

        Volley.newRequestQueue(this@MainActivity).add(istek)
    }
}
