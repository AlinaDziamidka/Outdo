package com.example.graduationproject.presentation.achievementdetails

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentAchievementDetailsBinding
import com.example.graduationproject.domain.entity.Achievement
import com.example.graduationproject.domain.entity.UserProfile
import com.example.graduationproject.presentation.achievementdetails.adapter.CompletedAdapter
import com.example.graduationproject.presentation.achievementdetails.adapter.UncompletedAdapter
import com.google.android.gms.common.util.IOUtils.copyStream
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import com.example.graduationproject.BuildConfig
import com.example.graduationproject.presentation.photoview.PhotoView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class AchievementDetailsView : Fragment(), DialogAddPhoto.DialogAddPhotoListener {

    private val viewModel: AchievementDetailsViewModel by viewModels()
    private var _binding: FragmentAchievementDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: AchievementDetailsViewArgs by navArgs()
    private lateinit var completedAdapter: CompletedAdapter
    private lateinit var uncompletedAdapter: UncompletedAdapter
    private lateinit var completedView: RecyclerView
    private lateinit var uncompletedView: RecyclerView
    private lateinit var achievementNameView: TextView
    private lateinit var achievementDescriptionView: TextView
    private lateinit var achievementId: String
    private lateinit var userId: String
    private lateinit var completeActionView: Button
    private lateinit var currentAchievement: Achievement
    private lateinit var photoUri: Uri


    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 1001
        private const val REQUEST_IMAGE_CAPTURE = 2001
        private const val REQUEST_GALLERY_PICK = 2002
        private const val GALLERY_PERMISSION_REQUEST_CODE = 2003
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentAchievementDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setUpChallengeName()
        getUserId()
        observeCurrentAchievement()
        initAdapter()
        setUpCompletedFriends()
        setUpUncompletedFriends()
        observeCompletedFriends()
        observeUncompletedFriends()
        setUpCompleteAction()
    }

    private fun initViews() {
        achievementNameView = binding.achievementNameView
        achievementDescriptionView = binding.achievementDescriptionView
        completedView = binding.completedRecyclerView
        uncompletedView = binding.uncompletedRecyclerView
        completeActionView = binding.completeAchievementAction
    }

    private fun setUpChallengeName() {
        achievementId = args.achievementId
        viewModel.setCurrentAchievement(achievementId)
    }

    private fun getUserId() {
        val sharedPreferences =
            requireContext().getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("current_user_id", "  ") ?: "  "
    }

    private fun observeCurrentAchievement() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateCurrentAchievement.collect {
                    Log.d("observeCurrentAchievement", "New view state: $it")
                    when (it) {
                        is AchievementDetailsViewState.Success -> {
                            currentAchievement = it.data
                            setAchievementName(currentAchievement)
                            setAchievementDescription(currentAchievement)
                            Log.d(
                                "observeCurrentAchievement", "Success view state, data: ${it.data}"
                            )
                        }

                        is AchievementDetailsViewState.Loading -> {
                        }

                        is AchievementDetailsViewState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun setAchievementName(achievement: Achievement) {
        val achievementTitle =
            getString(R.string.achievement_details_title, achievement.achievementName)
        achievementNameView.text = achievementTitle
    }

    private fun setAchievementDescription(achievement: Achievement) {
        achievementDescriptionView.text = achievement.description
    }

    private fun initAdapter() {
        initCompletedAdapter()
        initUncompletedAdapter()
    }

    private fun initCompletedAdapter() {
        completedView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        completedAdapter = CompletedAdapter(mutableListOf()) { completedFriend ->
        }
        completedView.adapter = completedAdapter
    }

    private fun initUncompletedAdapter() {
        uncompletedView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        uncompletedAdapter = UncompletedAdapter(mutableListOf())
        uncompletedView.adapter = uncompletedAdapter
    }

    private fun setUpCompletedFriends() {
        viewModel.setUpCompletedFriends(achievementId)
    }

    private fun setUpUncompletedFriends() {
        viewModel.setUpUncompletedFriends(achievementId)
    }

    private fun observeCompletedFriends() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateCompletedFriends.collect {
                    Log.d("observeCompletedFriends", "New view state: $it")
                    when (it) {
                        is AchievementDetailsViewState.Success -> {
                            loadCompletedFriends(it.data)
                            Log.d("observeCompletedFriends", "Success view state, data: ${it.data}")
                        }

                        is AchievementDetailsViewState.Loading -> {
                        }

                        is AchievementDetailsViewState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun loadCompletedFriends(completedFriends: MutableList<UserProfile>) {
        completedAdapter.setCompletedFriends(completedFriends)
    }

    private fun observeUncompletedFriends() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateUncompletedFriends.collect {
                    Log.d("observeUncompletedFriends", "New view state: $it")
                    when (it) {
                        is AchievementDetailsViewState.Success -> {
                            loadUncompletedFriends(it.data)
                            Log.d(
                                "observeUncompletedFriends", "Success view state, data: ${it.data}"
                            )
                        }

                        is AchievementDetailsViewState.Loading -> {
                        }

                        is AchievementDetailsViewState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun loadUncompletedFriends(uncompletedFriends: MutableList<UserProfile>) {
        Log.d("AchievementDetailsView", "Uncompleted friends $uncompletedFriends")
        uncompletedAdapter.setUncompletedFriends(uncompletedFriends)
    }

    private fun setUpCompleteAction() {
        completeActionView.setOnClickListener {
            showAddPhotoDialog()
        }
    }

    private fun showAddPhotoDialog() {
        val dialogAddPhoto = DialogAddPhoto()
        dialogAddPhoto.show(childFragmentManager, "DialogAddPhoto")
    }

    override fun onOpenCamera() {
        when {
            requireContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                startCameraActivity()
            }

            !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                showCameraPermissionRationale()
            }

            else -> {
                requestSystemPermission()
            }
        }
    }

    private fun startCameraActivity() {
        Log.d("AchievementDetailsView", "Open Camera Clicked")
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = File(requireContext().cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        val authority = "${BuildConfig.APPLICATION_ID}.provider"
        photoUri = FileProvider.getUriForFile(requireContext(), authority, photoFile)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Log.e("AchievementDetailsView", "No app available to open camera")
        }
    }


    private fun showCameraPermissionRationale() {
        AlertDialog.Builder(requireContext()).setTitle(R.string.dialog_camera_permission_title)
            .setMessage(R.string.dialog_camera_permission_message).setPositiveButton("OK") { _, _ ->
                requestSystemPermission()
            }.setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    private fun requestSystemPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onChooseFromGallery() {
        when {
            isPermissionGranted() -> {
                openGalleryPicker()
            }

            !shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                showGalleryPermissionRationale()
            }

            else -> {
                requestGalleryPermission()
            }
        }
    }

    private fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun openGalleryPicker() {
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_GALLERY_PICK)
    }

    private fun showGalleryPermissionRationale() {
        AlertDialog.Builder(requireContext()).setTitle(R.string.dialog_gallery_permission_title)
            .setMessage(R.string.dialog_gallery_permission_message)
            .setPositiveButton("OK") { _, _ ->
                requestGalleryPermission()
            }.setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    private fun requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES), GALLERY_PERMISSION_REQUEST_CODE
            )
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCameraActivity()
                } else {
                    Log.d("AchievementDetailsView", "Camera permission denied.")
                }
            }

            GALLERY_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGalleryPicker()
                } else {
                    Log.d("AchievementDetailsView", "Read media permission denied.")
                }
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            lifecycleScope.launch {
                try {
                    when (requestCode) {
                        REQUEST_IMAGE_CAPTURE -> {
                            Log.d("AchievementDetailsView", "imageUri: $photoUri")
                            photoUri.let {
                                Log.d("AchievementDetailsView", "imageUri: $photoUri")
                                Log.d(
                                    "AchievementDetailsView", "imageUri not null: $photoUri"
                                )
                                val photoFile = uriToFile(it)
                                Log.d(
                                    "AchievementDetailsView",
                                    "Photo file created: ${photoFile.absolutePath}"
                                )
                                showPhotoView(photoUri.toString())
                                viewModel.setUpPhoto(userId, achievementId, photoFile)
                            }
                        }

                        REQUEST_GALLERY_PICK -> {
                            val imageUri = data?.data
                            imageUri?.let {
                                val imageFile = uriToFile(it)
                                showPhotoView(imageFile.toString())
                                viewModel.setUpPhoto(userId, achievementId, imageFile)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AchievementDetailsView", "Error converting URI to file", e)
                }
            }
        }
        observePhotoUploading()
    }


    private suspend fun uriToFile(uri: Uri): File = withContext(Dispatchers.IO) {
        val contentResolver = requireContext().applicationContext.contentResolver
        val file = File(requireContext().cacheDir, "temp_${System.currentTimeMillis()}.tmp")
        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                copyStream(inputStream, outputStream)
            }
        }
        file
    }

    private fun preloadImage(photoUrl: String, onSuccess: () -> Unit) {
        val imageLoader = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext()).data(photoUrl)
            .memoryCachePolicy(CachePolicy.ENABLED).target(onSuccess = { drawable ->
                onSuccess()
            }, onError = {

            }).build()
        imageLoader.enqueue(request)
    }

    private fun observePhotoUploading() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateUpload.collect {
                    Log.d("observeUploadingPhoto", "New view state: $it")
                    when (it) {
                        is AchievementDetailsViewState.Success -> {
                            Log.d(
                                "observeUploadingPhoto",
                                "Success upload view state, data: ${it.data}"
                            )
                        }

                        is AchievementDetailsViewState.Loading -> {
//                            showPhotoView(photoUri.toString())
                            Log.d("observeUploadingPhoto", "Loading upload view state")
                        }

                        is AchievementDetailsViewState.Failure -> {
                            Log.e("observeUploadingPhoto", "Failure upload view state")
                            delay(1900)
                            showErrorUploadingPhoto()
                        }
                    }
                }
            }
        }
    }

//    private fun showPhotoView(photoUrl: String) {
//        val photoViewDialog = PhotoView()
//        photoViewDialog.setPhotoUrl(photoUrl)
//        photoViewDialog.show(childFragmentManager, "PhotoDialog")
////        photoViewDialog.getPhotoUrl(photoUrl)
//    }

    private fun showPhotoView(photoUrl: String) {
        val existingDialog = childFragmentManager.findFragmentByTag("PhotoDialog") as? PhotoView
        existingDialog?.let {
            if (it.isAdded) {
                it.dismissAllowingStateLoss()
            }
        }

        preloadImage(photoUrl) {
            val transaction = childFragmentManager.beginTransaction()
            val photoViewDialog = PhotoView.newInstance(photoUrl)

            transaction.add(photoViewDialog, "PhotoDialog")
            transaction.commitNow()
        }
    }

    private fun showErrorUploadingPhoto() {
        Log.d("AchievementDetailsView", "Attempting to show error photo")
        val dialog = childFragmentManager.findFragmentByTag("PhotoDialog") as? PhotoView
        dialog?.errorUploadingPhoto()
    }
//        val photoViewDialog = PhotoView()
//        photoViewDialog.show(childFragmentManager, "PhotoDialog")
//    private fun updatePhotoView(uploadedUrl: String) {
//        val dialog = childFragmentManager.findFragmentByTag("PhotoDialog") as? PhotoView
//        dialog?.updatePhotoUrl(uploadedUrl)
//    }
}