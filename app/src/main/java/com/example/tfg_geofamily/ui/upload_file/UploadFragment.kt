package com.example.tfg_geofamily.ui.upload_file

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tfg_geofamily.R
import com.example.tfg_geofamily.databinding.FragmentMapResultBinding
import com.example.tfg_geofamily.databinding.FragmentUploadBinding

class UploadFragment : Fragment() {

    private lateinit var binding: FragmentUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUploadBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UploadFragment().apply {}
    }
}