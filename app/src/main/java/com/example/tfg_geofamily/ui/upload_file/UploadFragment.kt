package com.example.tfg_geofamily.ui.upload_file

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tfg_geofamily.databinding.FragmentUploadBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadFragment : Fragment() {

    private lateinit var binding: FragmentUploadBinding
    lateinit var mUploadBtn: Button
    lateinit var mStorage: StorageReference
    private var GALLERY_INTENT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUploadBinding.inflate(inflater, container, false)

        mStorage = FirebaseStorage.getInstance().reference

        mUploadBtn = binding.btnSubir

        mUploadBtn.setOnClickListener {
            var intent: Intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY_INTENT)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_INTENT && resultCode == Activity.RESULT_OK){
            var uri: Uri? = data?.data
            var filePath: StorageReference = mStorage.child("fotos").child(uri?.lastPathSegment.toString())
            filePath.putFile(uri!!).addOnSuccessListener {
                Toast.makeText(context, "Foto subida correctamente", Toast.LENGTH_SHORT).show()
            }


        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UploadFragment().apply {}
    }
}