package com.example.astroplanetapp.ui

import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astroplanetapp.R
import com.example.astroplanetapp.adapters.AdapterPlanetRecycler
import com.example.astroplanetapp.database.AstroPlanetDataBase
import com.example.astroplanetapp.databinding.ActivityMainBinding
import com.example.astroplanetapp.interfaces.PlanetDao
import com.example.astroplanetapp.interfaces.listernerRecyclerPlanet
import com.example.astroplanetapp.models.Planet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.LinkedBlockingDeque
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), listernerRecyclerPlanet {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapterPlanet: AdapterPlanetRecycler
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mLayoutManagerLinear: LinearLayoutManager
    private lateinit var mPreferences: SharedPreferences
    private var mUserNormal: String? = null
    private var mUserAdmin: String? = null
    private var mFirsTime by Delegates.notNull<Boolean>()
    private var mAstroPlanetDataBase: AstroPlanetDataBase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mPreferences = getPreferences(MODE_PRIVATE)
        // Seteando el valor verdadero por primera vez
        mFirsTime = mPreferences.getBoolean(getString(R.string.sp_firstime), true)
        mUserNormal = mPreferences.getString(getString(R.string.tipo_usuario), "")
        mUserAdmin = mPreferences.getString(getString(R.string.tipo_usuario_admin), "")

        mAstroPlanetDataBase = AstroPlanetDataBase.getDatabase(this)
        val planetDao = mAstroPlanetDataBase?.planetDao()

        if (mFirsTime) {
            makeAlertDialogMain()
        }
        if (mUserAdmin.equals("UserAdmin")) {
            setupRecyclerView(planetDao)

        } else {
            setupRecyclerLinearView(planetDao)
        }

    }

    /**
     * **********************************
     * CRUD DE PLANETAS
     * ************************************8
     */
    private fun addPlanet(planetDao: PlanetDao?) {

        var planet = Planet(nombre = mBinding.planetInput.text.toString())

//        Thread {
//            planetDao?.insertPlanet(planet)
//        }.start()

        lifecycleScope.launch(Dispatchers.IO) {
            planetDao?.insertPlanet(planet)
        }

        mAdapterPlanet.add(planet)
        //mAdapterPlanet.notifyDataSetChanged()
        mBinding.planetInput.text.clear()

    }

    private fun getAllPlanets(daoPlanet: PlanetDao?) {
//        val qeue = LinkedBlockingDeque<MutableList<Planet>>()
//        Thread{
//            val planetsList = daoPlanet?.getAllPlanets()
//            qeue.add(planetsList)
//        }.start()

        lifecycleScope.launch(Dispatchers.IO) {
            val planetList = daoPlanet?.getAllPlanets()
            planetList?.let { planets ->
                withContext(Dispatchers.Main) {
                    mAdapterPlanet.setPlanetList(planets)
                }
            }
        }

    }

    fun updatePlanet(planet: Planet){

        val planetDao = mAstroPlanetDataBase?.planetDao()
        planet.isFavorite = !planet.isFavorite
        lifecycleScope.launch(Dispatchers.IO) {
            planet?.let {

                val id = planetDao?.updatePlanet(planet)
                mAdapterPlanet.updatePlanetFavorite(planet)
            }
        }

    }

    fun deletePlanet(planet: Planet){

        val planetDao = mAstroPlanetDataBase?.planetDao()
        lifecycleScope.launch(Dispatchers.IO) {
            planet?.let {

                val id = planetDao?.deletePlanet(planet)
                mAdapterPlanet.deletePlanet(planet)
            }
        }
    }


    // Metodo que setea el recyclerView en forma de Filas
    private fun makeAlertDialogMain() {

        val alertDialog = MaterialAlertDialogBuilder(this)

        alertDialog.setTitle(getString(R.string.app_name))
        // alertDialog.setView(R.layout.alert_dialog_user)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(getString(R.string.user_registrer),
            DialogInterface.OnClickListener { dialog, _ ->

                mPreferences.edit().putBoolean(getString(R.string.sp_firstime), false).apply()
                mPreferences.edit().putString(getString(R.string.tipo_usuario), "UserNormal")
                    .apply()
                mFirsTime = false
                mUserNormal = "UserNormal"

                dialog.dismiss()

            })

        alertDialog.setNeutralButton(getString(R.string.admin_user),
            DialogInterface.OnClickListener { dialog, _ ->

                mPreferences.edit().putString(getString(R.string.admin_user), "UserAdmin").apply()
                mPreferences.edit().putBoolean(getString(R.string.sp_firstime), false).apply()
                mFirsTime = false
                mUserAdmin = "UserAdmin"

                dialog.dismiss()

            })
            .show()
    }

    //   Metodo que setea el recyclerView en forma de grilla
    private fun setupRecyclerView(daoPlanet: PlanetDao?) {

        mAdapterPlanet = AdapterPlanetRecycler(mutableListOf(), this)
        mLayoutManager = GridLayoutManager(this, 2)

        getAllPlanets(daoPlanet)

        mBinding.recyclerPlanetas.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mAdapterPlanet
        }
        swipeHelper()

        mBinding.addButton?.setOnClickListener {
            addPlanet(daoPlanet)
        }

    }

    private fun setupRecyclerLinearView(planetDao: PlanetDao?) {

        mAdapterPlanet = AdapterPlanetRecycler(mutableListOf(), this)
        mLayoutManagerLinear = LinearLayoutManager(this)

        getAllPlanets(planetDao)

        mBinding.recyclerPlanetas.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManagerLinear
            adapter = mAdapterPlanet
        }
        swipeHelper()

        mBinding.addButton?.setOnClickListener {
            addPlanet(planetDao)
        }

    }

    /**
     *
     *
     * OnclickListeners de la actividad
     *
     *  Click para el cardViewCompleto
     *  Click para el boton favoritos
     *
     *
     */
    override fun onClickListener(planet: Planet) {

    }

    override fun onclickFavoriteListener(planet: Planet) {
        updatePlanet(planet)
    }

    override fun onDeletePlanet(planet: Planet) {
        deletePlanet(planet)
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


/**
 * Queda por implementar
 *
 * Actualizar un planeta
 * Eliminar un planeta
 *
 */
