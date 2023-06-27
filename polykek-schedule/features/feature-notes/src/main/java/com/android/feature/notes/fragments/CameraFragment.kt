package com.android.feature.notes.fragments

import com.android.core.ui.fragments.ToolbarFragment
import com.android.core.ui.mvi.MviAction
import com.android.core.ui.mvi.MviIntent
import com.android.core.ui.mvi.MviState
import com.android.core.ui.mvi.MviViewModel

//
//private const val REQUEST_CAPTURE_IMAGE = 100
//private const val REQUEST = 504
//
//private val PERMISSIONS = arrayOf(
//        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.READ_EXTERNAL_STORAGE
//)

internal abstract class CameraFragment<I : MviIntent, S : MviState, A : MviAction, VM : MviViewModel<I, S, A>> :
    ToolbarFragment<I, S, A, VM>() {
//
//    protected val cameraClickListener = View.OnClickListener { openCameraIntent() }
//
//    private fun openCameraIntent() {
//        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        pictureIntent.resolveActivity(context.packageManager)?.let {
//            // Create the File where the photo should go
//            val photoFile = createImageFile()
//
//            val photoURI = FileProvider.getUriForFile(
//                    this,
//                    getString(R.string.provider_path),
//                    photoFile
//            )
//            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//            startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE)
//        }
//    }
//
//    override fun onViewCreatedBeforeRendering(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreatedBeforeRendering(view, savedInstanceState)
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (!hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST)
//            } else {
//                //do here
//            }
//        } else {
//            //do here
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
//            data?.extras?.let {
//                val imageBitmap = it["data"] as Bitmap?
//                MediaStore.Images.Media.insertImage(contentResolver, imageBitmap, "lalala1" , "lalal2")
//
////                mImageView.setImageBitmap(imageBitmap)
//            }
//        }
//    }
//
//    private var currentPhotoPath: String? = null
//
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//
//        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "FotoAula")
//        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//                "JPEG_${timeStamp}_",
//                ".jpg",
//                storageDir
//        ).apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            currentPhotoPath = absolutePath
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            REQUEST -> {
//                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //do here
//                } else {
//                    Toast.makeText(this, "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//
//    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
//            for (permission in permissions) {
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false
//                }
//            }
//        }
//        return true
//    }
}