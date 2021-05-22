package com.example.tfg_geofamily.ui.upload_file

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.tfg_geofamily.databinding.FragmentUploadBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


class UploadFragment : Fragment() {

    private lateinit var binding: FragmentUploadBinding
    lateinit var mUploadBtn: Button
    lateinit var mStorage: StorageReference
    lateinit var mImageView: ImageView
    private var GALLERY_INTENT = 1
    private lateinit var mProgressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUploadBinding.inflate(inflater, container, false)

        mStorage = FirebaseStorage.getInstance().reference

        mProgressDialog = ProgressDialog(context)

        mImageView = binding.imageViewStorage
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
        if (requestCode == GALLERY_INTENT && resultCode == Activity.RESULT_OK) {
            var descargarFoto: Uri = Uri.EMPTY
            mProgressDialog.setTitle("Subiendo...")
            mProgressDialog.setMessage("Subiendo foto a firebase")
            mProgressDialog.setCancelable(false)
            mProgressDialog.show()

            GlobalScope.launch(Dispatchers.IO) {

                var uri: Uri? = data?.data
                var filePath: StorageReference =
                    mStorage.child("fotos").child(uri?.lastPathSegment.toString())
                var file = filePath.putFile(uri!!).await()
                delay(3000)
                if(file.task.isComplete){
                    mProgressDialog.dismiss()
                    descargarFoto = file.storage.downloadUrl.await()
//                    Glide.with(this@UploadFragment)
//                        .load(descargarFoto)
//                        .fitCenter()
//                        .centerCrop()
//                        .into(mImageView)
                    Log.e("klk", descargarFoto.toString())
                    Glide.with(this@UploadFragment)
                        .asBitmap()
                        .load(descargarFoto)
                        .fitCenter()
                        .centerCrop()
                        .into(BitmapImageViewTarget(mImageView)
                    )
                }

            }

//            Toast.makeText(context, descargarFoto.path, Toast.LENGTH_SHORT).show()

        }

    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UploadFragment().apply {}
    }
}