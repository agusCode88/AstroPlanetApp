package com.example.astroplanetapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.astroplanetapp.databinding.ItemPlanetReclyBinding
import com.example.astroplanetapp.interfaces.listernerRecyclerPlanet
import com.example.astroplanetapp.models.Planet


class AdapterPlanetRecycler(
    private var listPlanet: MutableList<Planet>,
    private val planetListener: listernerRecyclerPlanet
) : RecyclerView.Adapter<AdapterPlanetRecycler.ViewHolderPlanet>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPlanet {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlanetReclyBinding.inflate(inflater, parent, false)

        return ViewHolderPlanet(binding)

    }

    override fun getItemCount(): Int = listPlanet.size

    override fun onBindViewHolder(holder: ViewHolderPlanet, position: Int) {
        val planet = listPlanet[position]
        holder.bind(planet)
    }

    fun add(planet: Planet) {
        listPlanet.add(planet)
        notifyDataSetChanged()
    }

    fun remove(adapterPosition: Int) {
        listPlanet.removeAt(adapterPosition)
        notifyItemChanged(adapterPosition)
        notifyDataSetChanged()
    }

    fun setPlanetList(planets: MutableList<Planet>) {

        listPlanet = planets
        notifyDataSetChanged()

    }

    fun updatePlanetFavorite(planet: Planet) {

        val index = listPlanet.lastIndexOf(planet)
        if (index != -1) {
            listPlanet.set(index, planet)
            notifyDataSetChanged()
        }

    }

    fun deletePlanet(planet: Planet) {

        val index = listPlanet.lastIndexOf(planet)
        if (index != -1) {
            listPlanet.removeAt(index)
            notifyItemChanged(index)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolderPlanet(private val binding: ItemPlanetReclyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(planet: Planet) {

            binding.txtName.text = planet.nombre
//            Picasso.get().load(flight.imagen).fit().into(binding.cityImage)
//            binding.root.setOnClickListener { flightListener.onClick(flight,adapterPosition) }


            with(binding.root){
                setOnLongClickListener { planetListener.onDeletePlanet(planet)
                    true }
            }

            binding.btnFavorite.isChecked = planet.isFavorite

            binding.btnFavorite.setOnClickListener {
                planet.isFavorite = !planet.isFavorite
                planetListener.onclickFavoriteListener(planet)

            }

        }
    }

}