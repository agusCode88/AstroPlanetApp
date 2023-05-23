package com.example.astroplanetapp.models

data class Planet(val id:Int=0,
                  var nombre:String ,
                  var imagen:String="",
                  var superficie:Double= 0.0,
                  var isFavorite:Boolean=false)
