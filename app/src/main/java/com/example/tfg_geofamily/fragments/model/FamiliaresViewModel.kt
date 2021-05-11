package com.example.tfg_geofamily.fragments.model

import androidx.lifecycle.ViewModel


class FamiliaresViewModel: ViewModel() {
    private var familiarSelecionada: Familiares

    init{
        familiarSelecionada = Familiares(0.0, 0.0, "", "")
    }

    fun getFamiliarSeleccionada():Familiares{
        return familiarSelecionada
    }

    fun setFamiliarSeleccionada(familiares: Familiares){
        familiarSelecionada = familiares
    }

}