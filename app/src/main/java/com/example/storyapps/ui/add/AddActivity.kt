package com.example.storyapps.ui.add

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.R
import com.example.storyapps.databinding.ActivityAddBinding
import com.example.storyapps.ui.camera.CameraActivity
import com.example.storyapps.ui.home.HomeActivity
import com.example.storyapps.ui.viewmodel.ViewModelFactory
import com.example.storyapps.utils.Utils.Companion.rotateBitmap
import com.example.storyapps.utils.Utils.Companion.showLoading
import com.example.storyapps.utils.Utils.Companion.uriToFile
import com.example.storyapps.vo.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddActivity : AppCompatActivity() {
    private lateinit var addBinding: ActivityAddBinding
    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var location: Location
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addBinding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(addBinding.root)

        token = intent.getStringExtra(EXTRA_ADD).toString()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        } else {
            getMyLastLocation()
        }

        addStoryViewModel = obtainViewModel(this)
        listener()
    }

    private fun listener() {
        with(addBinding) {
            cvAdd.setOnClickListener {
                dialogOption()
            }
            btAdd.setOnClickListener {
                upload()
            }
            ivAddBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun dialogOption() {
        val builder = Dialog(this)
        with(builder) {
            setContentView(R.layout.dialog_resources)
            findViewById<ConstraintLayout>(R.id.cl_resources_camera).setOnClickListener {
                val intent = Intent(this@AddActivity, CameraActivity::class.java)
                launcherIntentCameraX.launch(intent)
                dismiss()
            }
            findViewById<ConstraintLayout>(R.id.cl_resources_gallery).setOnClickListener {
                startGallery()
                dismiss()
            }
            show()
        }
    }

    private var getFile: File? = null
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra(EXTRA_PICTURE) as File
            val isBackCamera = it.data?.getBooleanExtra(EXTRA_BACK_CAMERA, true) as Boolean
            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path), isBackCamera
            )
            addBinding.ivAdd.visibility = View.GONE
            addBinding.ivAddPreview.visibility = View.VISIBLE
            addBinding.ivAddPreview.setImageBitmap(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.add_chose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddActivity)
            getFile = myFile
            addBinding.ivAdd.visibility = View.GONE
            addBinding.ivAddPreview.visibility = View.VISIBLE
            addBinding.ivAddPreview.setImageURI(selectedImg)
        }
    }

    private fun upload() {
        with(addBinding) {
            if (edAddDescription.text.isNullOrEmpty()) {
                edAddDescription.error = getString(R.string.add_description_empty)
            } else {
                if (getFile != null) {
                    val file = getFile as File
                    val lat = location.latitude.toString().toRequestBody("text/plain".toMediaType())
                    val lon =
                        location.longitude.toString().toRequestBody("text/plain".toMediaType())
                    val description =
                        edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo", file.name, requestImageFile
                    )
                    addStoryViewModel.addStory(imageMultipart, description, lat, lon, token)
                        .observe(this@AddActivity) {
                            when (it.status) {
                                Status.LOADING -> pbAdd.showLoading(false)
                                Status.SUCCESS -> {
                                    pbAdd.showLoading(false)
                                    if (it.data != null) {
                                        Toast.makeText(
                                            this@AddActivity, it.data.message, Toast.LENGTH_LONG
                                        ).show()
                                        val intent =
                                            Intent(this@AddActivity, HomeActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        startActivity(intent)
                                    }
                                }
                                Status.ERROR -> {
                                    pbAdd.showLoading(false)
                                    if (!it.message.isNullOrBlank()) {
                                        Toast.makeText(
                                            this@AddActivity, it.message, Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    edAddDescription.text.clear()
                                }
                            }

                        }
                } else {
                    Toast.makeText(
                        this@AddActivity, getString(R.string.add_insert_image), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this, getString(R.string.add_no_permission), Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                getMyLastLocation()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.location = location
                } else {
                    Toast.makeText(
                        this@AddActivity,
                        getString(R.string.all_location_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): AddStoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[AddStoryViewModel::class.java]
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_ADD = "extra_add_story"
        const val EXTRA_PICTURE = "picture"
        const val EXTRA_BACK_CAMERA = "isBackCamera"
    }
}