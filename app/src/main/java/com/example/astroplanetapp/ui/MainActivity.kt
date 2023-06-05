package com.example.astroplanetapp.ui

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import com.example.astroplanetapp.interfaces.UserDao
import com.example.astroplanetapp.interfaces.listernerRecyclerPlanet
import com.example.astroplanetapp.models.Planet
import com.example.astroplanetapp.models.User
import com.example.astroplanetapp.ui.fragments.CreateOrEditFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class MainActivity : ActionBarActivity(), listernerRecyclerPlanet {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapterPlanet: AdapterPlanetRecycler
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
        // Seteando el valor verdadero , si parte por primera vez la app.
        mFirsTime = mPreferences.getBoolean(getString(R.string.sp_firstime), true)
        mUserNormal = mPreferences.getString(getString(R.string.tipo_usuario), "")
        mUserAdmin = mPreferences.getString(getString(R.string.tipo_usuario_admin), "")

        mAstroPlanetDataBase = AstroPlanetDataBase.getDatabase(this)
        val planetDao = mAstroPlanetDataBase?.planetDao()
        val userDao = mAstroPlanetDataBase?.userDao()

        if (mFirsTime) {
            makeAlertDialogMain(userDao)
            setupRecyclerViewBasedOnUser(planetDao)
        } else {
            setupRecyclerViewBasedOnUser(planetDao)
        }

    }

    // Metodo que Muestra el popo up de seleccion de Roles
    private fun makeAlertDialogMain(userDao: UserDao?) {

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

                val usuarioAPP = User(username = "agus", password = "123")
                lifecycleScope.launch(Dispatchers.IO) {
                    val idUser = userDao?.insertUser(usuarioAPP)
                }

                dialog.dismiss()

            })

        alertDialog.setNeutralButton(getString(R.string.admin_user),
            DialogInterface.OnClickListener { dialog, _ ->

                mPreferences.edit().putString(getString(R.string.admin_user), "UserAdmin").apply()
                mPreferences.edit().putBoolean(getString(R.string.sp_firstime), false).apply()
                mFirsTime = false
                mUserAdmin = "UserAdmin"

                val usuarioAPP = User(username = "agusAdmin", password = "123")
                lifecycleScope.launch(Dispatchers.IO) {
                    val idUser = userDao?.insertUser(usuarioAPP)
                }

                dialog.dismiss()

            })
            .show()
    }

    /*
    Funcion para comprobar si se trata de un usuario con Rol de Administrador o es un usuario de la app
     */
    private fun setupRecyclerViewBasedOnUser(planetDao: PlanetDao?) {
        val isUserAdmin =
            mPreferences.getString(getString(R.string.admin_user), "") == "UserAdmin"

        if (isUserAdmin) {
            setupRecyclerViewWithLayout(GridLayoutManager(this, 2),planetDao)
        } else {
            setupRecyclerViewWithLayout(LinearLayoutManager(this),planetDao)
        }
    }

    /*
    Funcion que permite configurar el recyclerView con su adaptador, se le manda por parametro
    si lo queremos en forma de Linear o en forma de grilla.
     */
    private fun setupRecyclerViewWithLayout(layoutManager: RecyclerView.LayoutManager,
                                            planetDao: PlanetDao?) {

        mAdapterPlanet = AdapterPlanetRecycler(mutableListOf(), this)
        mBinding.recyclerPlanetas.apply {
            setHasFixedSize(true)
            this.layoutManager = layoutManager
            adapter = mAdapterPlanet
        }
        swipeHelper()
        setupAddButton(planetDao)
        setUpAddPlanetFloatingBotton()
    }

    /*
    Funcion que es escucha del boton agregar
     */
    private fun setupAddButton(planetDao: PlanetDao?) {
//        mBinding.addButton?.setOnClickListener {
//            addPlanet(planetDao)
//        }
    }

    private fun setUpAddPlanetFloatingBotton(){
        mBinding.addPlanetFB?.setOnClickListener { launchFragmentEditPlanet() }
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


    private fun launchFragmentEditPlanet(args: Bundle? = null) {
        val fragmentEditOrCreatePlanet = CreateOrEditFragment()
        val fragmentManager = supportFragmentManager
        val fragmenTransaction = fragmentManager.beginTransaction()

        if (args != null){
            fragmentEditOrCreatePlanet.arguments = args
        }

        fragmenTransaction.add(R.id.container_main, fragmentEditOrCreatePlanet)
        fragmenTransaction.commit()
        fragmenTransaction.addToBackStack(null)

    }

    /**
     * **********************************
     * CRUD DE PLANETAS
     * ************************************8
     */
    private fun addPlanet(planetDao: PlanetDao?) {
//
//        var planet = Planet(nombre = mBinding.planetInput.text.toString())
//
//        // Otra forma  de hacerelo, sin corrutinas. Son hilos nativos.
//
////        Thread {
////            planetDao?.insertPlanet(planet)
////        }.start()
//
//        // Hecho con Corrutinas
//        lifecycleScope.launch(Dispatchers.IO) {
//            planetDao?.insertPlanet(planet)
//        }
//
//        mAdapterPlanet.add(planet)
//        //mAdapterPlanet.notifyDataSetChanged()
//        mBinding.planetInput.text.clear()

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

    // Funcion que actualiza un planeta en la Base de datos, la actualizacion es el
    // cambio de estado a favorito o no, tambien se hace en el adaptador
    private fun updatePlanet(planet: Planet) {

        val planetDao = mAstroPlanetDataBase?.planetDao()
        planet.isFavorite = !planet.isFavorite
        lifecycleScope.launch(Dispatchers.IO) {
            planet?.let {
                val id = planetDao?.updatePlanet(planet)
                mAdapterPlanet.updatePlanetFavorite(planet)
            }
        }

    }

    /*
Fubncion que borra una entidad de la base de datos y en el adaptador del Recycler
 */
    private fun deletePlanet(planet: Planet) {

        val planetDao = mAstroPlanetDataBase?.planetDao()
        lifecycleScope.launch(Dispatchers.IO) {
            planet?.let {
                val id = planetDao?.deletePlanet(planet)
                mAdapterPlanet.deletePlanet(planet)
            }
        }
    }

    /**
     *
     * OnclickListeners de la actividad
     *
     *  Click para el cardViewCompleto
     *  Click para el boton favoritos
     *  Click Largo para eliminar

     */
    override fun onClickListener(planetId: Int) {
        val args = Bundle()
        args.putInt(getString(R.string.paramInt),planetId)
        launchFragmentEditPlanet(args)

    }

    override fun onclickFavoriteListener(planet: Planet) {
        updatePlanet(planet)
    }

    override fun onDeletePlanet(planet: Planet) {
        deletePlanet(planet)
    }

}


/**
 * Queda por implementar
 *
 * Actualizar un planeta
 * Eliminar un planeta
 *
 */
