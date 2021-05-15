package com.example.tfg_geofamily.ui.AddFamiliar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tfg_geofamily.R
import com.example.tfg_geofamily.databinding.FragmentAddFamiliarBinding
import com.example.tfg_geofamily.databinding.FragmentListBinding
import com.google.firebase.auth.FirebaseAuth

class AddFamiliarFragment : Fragment() {

    private lateinit var binding: FragmentAddFamiliarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddFamiliarBinding.inflate(inflater, container, false)

        

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFamiliarFragment().apply {

            }
    }
}