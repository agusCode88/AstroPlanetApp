package com.example.astroplanetapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astroplanetapp.adapters.AdapterPlanetRecycler
import com.example.astroplanetapp.databinding.ActivityMainBinding
import com.example.astroplanetapp.interfaces.listernerRecyclerPlanet
import com.example.astroplanetapp.models.Planet

class MainActivity : AppCompatActivity(),listernerRecyclerPlanet {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapterPlanet: AdapterPlanetRecycler
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mLayoutManagerLinear: LinearLayoutManager
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.addButton?.setOnClickListener {
            addPlanet()
        }
        setupRecyclerLinearView()

    }

    // Metodo que agrega un elemento a la lista mediante el adaptador
    private fun addPlanet(){

        var planet:Planet = Planet(nombre=mBinding.planetInput.text.toString())
        mAdapterPlanet.add(planet)
        mAdapterPlanet.notifyDataSetChanged()
        mBinding.planetInput.text.clear()

    }

    // Metodo que setea el recyclerView en forma de grilla
        private fun setupRecyclerView() {

            mAdapterPlanet = AdapterPlanetRecycler(mutableListOf(),this)
            mLayoutManager = GridLayoutManager(this,2)
            mBinding.recyclerPlanetas.apply {
                setHasFixedSize(true)
                layoutManager = mLayoutManager
                adapter = mAdapterPlanet
            }

        swipeHelper()

        }

    // Metodo que setea el recyclerView en forma de Filas
    private fun setupRecyclerLinearView() {

        mAdapterPlanet = AdapterPlanetRecycler(mutableListOf(),this)
        mLayoutManagerLinear = LinearLayoutManager(this)

        mBinding.recyclerPlanetas.apply {
            setHasFixedSize(true)
            layoutManager =mLayoutManagerLinear
            adapter = mAdapterPlanet
        }

        swipeHelper()
    }

    override fun onClickListener(planet: Planet) {

    }

    /*
    Codigo para borrar un elemento de la lista a traves del gesto de swipe

     */
    private fun swipeHelper() {
        val swipeHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    mAdapterPlanet.remove(viewHolder.adapterPosition)
                }
            })
        swipeHelper.attachToRecyclerView(mBinding.recyclerPlanetas)
    }

}