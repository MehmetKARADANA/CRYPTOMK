package com.mehmetkaradana.cryptomk.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mehmetkaradana.cryptomk.R
import com.mehmetkaradana.cryptomk.adapter.RecyclerViewAdapter
import com.mehmetkaradana.cryptomk.databinding.ActivityMainBinding
import com.mehmetkaradana.cryptomk.model.cryptoModel
import com.mehmetkaradana.cryptomk.service.CryptoService
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {

    private lateinit var binding: ActivityMainBinding
    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels: ArrayList<cryptoModel>? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    private var compositeDisposable : CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        compositeDisposable = CompositeDisposable()

        /*val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager*/

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        // recyclerViewAdapter= RecyclerViewAdapter(cryptoModels)

        loadData()


    }

    fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// RxJava ile Retrofit'i bağlamak için
            .build().create(CryptoService::class.java)

        compositeDisposable?.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))

/*
        val service = retrofit.create(CryptoService::class.java)
        val call = service.getData()

        call.enqueue(object : Callback<List<cryptoModel>> {
            override fun onResponse(
                call: Call<List<cryptoModel>>,
                response: Response<List<cryptoModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        cryptoModels = ArrayList(it)
                        cryptoModels?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
                            binding.recyclerView.adapter = recyclerViewAdapter
                        }


                        /* for (cryptoModel : cryptoModel in cryptoModels!!){
                             println(cryptoModel.currency)
                             println(cryptoModel.price)
                         }*/
                    }
                }
            }

            override fun onFailure(call: Call<List<cryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })*/
    }

     private fun handleResponse(cryptoList: List<cryptoModel>){
        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let {
            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
            binding.recyclerView.adapter = recyclerViewAdapter
        }
    }

    override fun onItemClick(cryptoModel: cryptoModel) {
        Toast.makeText(this, "Clicked : ${cryptoModel.currency}", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }

}