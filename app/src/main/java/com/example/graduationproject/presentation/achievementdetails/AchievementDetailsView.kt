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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
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
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
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
    private lateinit var achievementNameShimmerLayout: ShimmerFrameLayout
    private lateinit var achievementDescriptionShimmerLayout: ShimmerFrameLayout
    private lateinit var completedShimmerLayout: ShimmerFrameLayout
    private lateinit var uncompletedShimmerLayout: ShimmerFrameLayout
    private lateinit var completedErrorView: CardView
    private lateinit var updateCompletedError: LinearLayout
    private lateinit var uncompletedErrorView: CardView
    private lateinit var updateUncompletedError: LinearLayout
    private lateinit var addPhotoHintView: TextView

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
        setUpErrorUpdateAction()
    }

    private fun initViews() {
        achievementNameView = binding.achievementNameView
        achievementDescriptionView = binding.achievementDescriptionView
        completedView = binding.completedRecyclerView
        uncompletedView = binding.uncompletedRecyclerView
        completeActionView = binding.completeAchievementAction
        achievementNameShimmerLayout = binding.achievementNameShimmerLayout
        achievementDescriptionShimmerLayout = binding.achievementDescriptionShimmerLayout
        completedShimmerLayout = binding.completedShimmerLayout
        uncompletedShimmerLayout = binding.uncompletedShimmerLayout
        completedErrorView = binding.completedErrorView.errorRootContainer
        uncompletedErrorView = binding.uncompletedErrorView.errorRootContainer
        updateCompletedError = binding.completedErrorView.updateContainer
        updateUncompletedError = binding.uncompletedErrorView.updateContainer
        addPhotoHintView = binding.addPhotoHintView
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
                    when (it) {
                        is AchievementDetailsViewState.Success -> {
                            currentAchievement = it.data
                            setAchievementName(currentAchievement)
                            setAchievementDescription(currentAchievement)
                        }

                        is AchievementDetailsViewState.Loading -> {
                            startShimmer(achievementNameShimmerLayout, achievementNameView)
                            startShimmer(
                                achievementDescriptionShimmerLayout,
                                achievementDescriptionView
                            )
                        }

                        is AchievementDetailsViewState.Failure -> {
                            stopShimmer(achievementNameShimmerLayout, achievementNameView)
                            stopShimmer(
                                achievementDescriptionShimmerLayout,
                                achievementDescriptionView
                            )
                        }
                    }
                }
            }
        }
    }

    private fun startShimmer(shimmerLayout: ShimmerFrameLayout, view: View) {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        view.visibility = View.GONE
    }

    private fun stopShimmer(shimmerLayout: ShimmerFrameLayout, view: View) {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        view.visibility = View.VISIBLE
    }

    private fun setAchievementName(achievement: Achievement) {
        val achievementTitle =
            getString(R.string.achievement_details_title, achievement.achievementName)
        achievementNameView.text = achievementTitle
        stopShimmer(achievementNameShimmerLayout, achievementNameView)
    }

    private fun setAchievementDescription(achievement: Achievement) {
        achievementDescriptionView.text = achievement.description
        stopShimmer(achievementDescriptionShimmerLayout, achievementDescriptionView)
    }

    private fun initAdapter() {
        initCompletedAdapter()
        initUncompletedAdapter()
    }

    private fun initCompletedAdapter() {
        completedView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        completedAdapter = CompletedAdapter(mutableListOf()) { completedFriend ->
            setUpFriendsDetailsAction(completedFriend.userId)
        }
        completedView.adapter = completedAdapter
    }

    private fun setUpFriendsDetailsAction(friendId: String) {
        lifecycleScope.launch {
            viewModel.getPhoto(friendId, achievementId)

            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateDownload.collect { viewState ->
                    when (viewState) {
                        is AchievementDetailsViewState.Success -> {
                            showFriendPhoto(viewState.data, false)
                            cancel()
                        }

                        is AchievementDetailsViewState.Loading -> {
                        }

                        is AchievementDetailsViewState.Failure -> {
                            showErrorUploadingPhoto()
                            cancel()
                        }
                    }
                }
            }
        }
    }

    private fun showFriendPhoto(photoUrl: String, isLoad: Boolean) {
        val transaction = childFragmentManager.beginTransaction()
        val existingDialog = childFragmentManager.findFragmentByTag("PhotoDialog")
        if (existingDialog != null && existingDialog is DialogFragment) {
            transaction.remove(existingDialog)
        }

        val photoViewDialog = PhotoView.newInstance(photoUrl, isLoad)
        transaction.add(photoViewDialog, "PhotoDialog")
        transaction.commitAllowingStateLoss()
    }

    private fun showErrorUploadingPhoto() {
        val dialog = childFragmentManager.findFragmentByTag("PhotoDialog") as? PhotoView
        dialog?.errorUploadingPhoto()
        dialog?.hideProgress()
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
                    when (it) {
                        is AchievementDetailsViewState.Success -> {
                            loadCompletedFriends(it.data)
                        }

                        is AchievementDetailsViewState.Loading -> {
                            startShimmer(completedShimmerLayout, completedView)
                        }

                        is AchievementDetailsViewState.Failure -> {
                            handleOnCompletedFriendsFailure()
                        }
                    }
                }
            }
        }
    }

    private fun loadCompletedFriends(completedFriends: MutableList<UserProfile>) {
        completedAdapter.setCompletedFriends(completedFriends)
        checkIfAchievementCompleted(completedFriends)
        stopShimmer(completedShimmerLayout, completedView)
    }

    private fun checkIfAchievementCompleted(completedFriends: MutableList<UserProfile>) {
        completedFriends.forEach { completedFriend ->
            if (completedFriend.userId == userId) {
                completeActionView.visibility = View.GONE
                addPhotoHintView.visibility = View.GONE
            }
        }
    }

    private fun handleOnCompletedFriendsFailure() {
        stopShimmer(completedShimmerLayout, completedView)
        completedErrorView.visibility = View.VISIBLE
    }

    private fun observeUncompletedFriends() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStateUncompletedFriends.collect {
                    when (it) {
                        is AchievementDetailsViewState.Success -> {
                            loadUncompletedFriends(it.data)
                        }

                        is AchievementDetailsViewState.Loading -> {
                            startShimmer(uncompletedShimmerLayout, uncompletedView)
                        }

                        is AchievementDetailsViewState.Failure -> {
                            handleOnUncompletedFriendsFailure()
                        }
                    }
                }
            }
        }
    }

    private fun loadUncompletedFriends(uncompletedFriends: MutableList<UserProfile>) {
        uncompletedAdapter.setUncompletedFriends(uncompletedFriends)
        stopShimmer(uncompletedShimmerLayout, uncompletedView)
    }

    private fun handleOnUncompletedFriendsFailure() {
        stopShimmer(uncompletedShimmerLayout, uncompletedView)
        uncompletedErrorView.visibility = View.VISIBLE
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
                    Log.e("AchievementDetailsView", "Camera permission denied.")
                }
            }

            GALLERY_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGalleryPicker()
                } else {
                    Log.e("AchievementDetailsView", "Read media permission denied.")
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
                            photoUri.let {
                                val photoFile = uriToFile(it)
                                showPhotoView(photoUri.toString(), true)
                                viewModel.setUpPhoto(userId, achievementId, photoFile)
                                observePhotoUploading()
                            }
                        }

                        REQUEST_GALLERY_PICK -> {
                            val imageUri = data?.data
                            imageUri?.let {
                                val imageFile = uriToFile(it)
                                showPhotoView(imageFile.toString(), true)
                                viewModel.setUpPhoto(userId, achievementId, imageFile)
                                observePhotoUploading()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AchievementDetailsView", "Error converting URI to file", e)
                }
            }
        }
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

    private fun showPhotoView(photoUrl: String, isLoad: Boolean) {
        preloadImage(photoUrl) {
            val transaction = childFragmentManager.beginTransaction()
            val photoViewDialog = PhotoView.newInstance(photoUrl, isLoad)

            transaction.add(photoViewDialog, "PhotoDialog")
            transaction.commitNow()
        }
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
                    when (it) {
                        is AchievementDetailsViewState.Success -> {
                            hideProgress()
                            cancel()
                        }

                        is AchievementDetailsViewState.Loading -> {
                        }

                        is AchievementDetailsViewState.Failure -> {
                            delay(2200)
                            showErrorUploadingPhoto()
                            cancel()
                        }
                    }
                }
            }
        }
    }

    private fun hideProgress() {
        val dialog =
            childFragmentManager.findFragmentByTag("PhotoDialog") as? PhotoView
        dialog?.hideProgress()
        addPhotoHintView.visibility = View.GONE
        completeActionView.visibility = View.GONE
    }


    private fun setUpErrorUpdateAction() {
        onClickCompletedUpdateAction()
        onClickUncompletedUpdateAction()
    }

    private fun onClickCompletedUpdateAction() {
        updateCompletedError.setOnClickListener {
            completedErrorView.visibility = View.GONE
            setUpCompletedFriends()
        }
    }

    private fun onClickUncompletedUpdateAction() {
        updateUncompletedError.setOnClickListener {
            uncompletedErrorView.visibility = View.GONE
            setUpUncompletedFriends()
        }
    }
}