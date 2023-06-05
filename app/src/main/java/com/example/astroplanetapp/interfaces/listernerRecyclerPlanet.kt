package com.example.astroplanetapp.interfaces

import com.example.astroplanetapp.models.Planet

interface listernerRecyclerPlanet {

    fun onClickListener(planetId : Int)
    fun onclickFavoriteListener(planet:Planet)
    fun onDeletePlanet(planet: Planet)

}