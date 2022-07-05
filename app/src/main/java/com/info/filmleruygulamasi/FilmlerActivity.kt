package com.info.filmleruygulamasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_filmler.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

class FilmlerActivity : AppCompatActivity() {

    private lateinit var filmListe:ArrayList<Filmler>
    private lateinit var adapter:FilmlerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filmler)

        val kategori = intent.getSerializableExtra("kategoriNesne") as Kategoriler

        toolbarFilmler.title = "Filmler : ${kategori.kategori_ad}"
        setSupportActionBar(toolbarFilmler)

        filmlerRv.setHasFixedSize(true)
        filmlerRv.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        tumFilmlerByKategoriId(kategori.kategori_id)

    }

    fun tumFilmlerByKategoriId(kategori_id:Int){
        var url = "http://kasimadalan.pe.hu/filmler/filmler_by_kategori_id.php"

        val istek = object: StringRequest(Request.Method.POST,url, { cevap ->

            try {
                filmListe = ArrayList()

                val jsonObject = JSONObject(cevap)
                val filmler = jsonObject.getJSONArray("filmler")

                for (i in 0 until filmler.length()){
                    val f = filmler.getJSONObject(i)

                    val k = f.getJSONObject("kategori")
                    val kategori = Kategoriler(k.getInt("kategori_id"),k.getString("kategori_ad"))


                    val y = f.getJSONObject("yonetmen")
                    val yonetmen = Yonetmenler(y.getInt("yonetmen_id"),y.getString("yonetmen_ad"))

                    val film = Filmler(f.getInt("film_id")
                                        , f.getString("film_ad")
                                        , f.getInt("film_yil")
                                        ,f.getString("film_resim")
                                        ,kategori
                                        ,yonetmen)
                    filmListe.add(film)
                }

                adapter = FilmlerAdapter(this,filmListe)

                filmlerRv.adapter = adapter

            }catch (e: Exception){
                e.printStackTrace()
            }

        }, {  }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String,String>()
                params["kategori_id"] = kategori_id.toString()
                return params
            }
        }

        Volley.newRequestQueue(this@FilmlerActivity).add(istek)
    }
}
