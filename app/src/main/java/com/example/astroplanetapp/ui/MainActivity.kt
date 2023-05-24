package com.example.astroplanetapp.ui

import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astroplanetapp.R
import com.example.astroplanetapp.adapters.AdapterPlanetRecycler
import com.example.astroplanetapp.databinding.ActivityMainBinding
import com.example.astroplanetapp.interfaces.listernerRecyclerPlanet
import com.example.astroplanetapp.models.Planet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mPreferences = getPreferences(MODE_PRIVATE)

        // Seteando el valor verdadero por primera vez
        mFirsTime = mPreferences.getBoolean(getString(R.string.sp_firstime), true)
        mUserNormal = mPreferences.getString(getString(R.string.tipo_usuario), "")
        mUserAdmin = mPreferences.getString(getString(R.string.tipo_usuario_admin), "")

        if (mFirsTime) {
            makeAlertDialogMain()
        }

        if (mUserAdmin.equals("UserAdmin")) {
            setupRecyclerView()
        } else {
            setupRecyclerLinearView()
        }

    }

    // Metodo que agrega un elemento a la lista mediante el adaptador
    private fun addPlanet() {

        var planet: Planet = Planet(nombre = mBinding.planetInput.text.toString())
        mAdapterPlanet.add(planet)
        mAdapterPlanet.notifyDataSetChanged()
        // mBinding.planetInput.text.clear()

    }

    //   Metodo que setea el recyclerView en forma de grilla
    private fun setupRecyclerView() {

        mAdapterPlanet = AdapterPlanetRecycler(mutableListOf(), this)
        mLayoutManager = GridLayoutManager(this, 2)
        mBinding.recyclerPlanetas.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mAdapterPlanet
        }
        swipeHelper()

        mBinding.addButton?.setOnClickListener {
            addPlanet()
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

    private fun setupRecyclerLinearView() {

        mAdapterPlanet = AdapterPlanetRecycler(mutableListOf(), this)
        mLayoutManagerLinear = LinearLayoutManager(this)

        mBinding.recyclerPlanetas.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManagerLinear
            adapter = mAdapterPlanet
        }
        swipeHelper()

        mBinding.addButton?.setOnClickListener {
            addPlanet()
        }

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