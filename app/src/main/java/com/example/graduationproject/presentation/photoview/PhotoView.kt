package com.example.graduationproject.presentation.photoview

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import coil.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import com.example.graduationproject.R
import com.example.graduationproject.databinding.PhotoPreviewBinding

class PhotoView : DialogFragment() {

    companion object {
        private const val PHOTO_URL = "PHOTO_URL"
        private const val IS_LOAD = "IS_LOAD"

        fun newInstance(photoUrl: String, loadingPhoto: Boolean): PhotoView {
            val fragment = PhotoView()
            val args = Bundle()
            args.putString(PHOTO_URL, photoUrl)
            args.putBoolean(IS_LOAD, loadingPhoto)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: PhotoPreviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var photoView: ImageView
    private lateinit var closeAction: Button
    private lateinit var progressOverlay: ProgressBar
    private lateinit var errorView: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = PhotoPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        getPhotoUrl { isSuccess ->
            if (isSuccess) {
                closeAction.visibility = View.VISIBLE
            } else {
                closeAction.visibility = View.GONE
            }
        }
        setUpCloseAction()
    }

    private fun initViews() {
        photoView = binding.photoView
        closeAction = binding.closeActionView
        progressOverlay = binding.progressBar
        errorView = binding.errorView.errorRootContainer
    }

    private fun resetViewBeforeLoading() {
        photoView.setImageDrawable(null)
        photoView.visibility = View.GONE
        progressOverlay.visibility = View.VISIBLE
        errorView.visibility = View.GONE
    }

    private fun getPhotoUrl(onResult: (Boolean) -> Unit) {

        val photoUrl = arguments?.getString(PHOTO_URL)
        val loadingPhoto = arguments?.getBoolean(IS_LOAD, false) ?: false

        resetViewBeforeLoading()
        photoView.load(photoUrl) {
            placeholder(R.drawable.bg_photo_preview)
            memoryCachePolicy(CachePolicy.ENABLED)
            transformations(RoundedCornersTransformation(dpToPx(100f)))
            listener(
                onSuccess = { _, _ ->
                    handleOnSuccess(loadingPhoto)
                    onResult(true)
                },
                onError = { _, _ ->
                    handleOnFailure()
                    onResult(false)
                }
            )
        }
    }

    private fun handleOnSuccess(loadingPhoto: Boolean) {
        photoView.visibility = View.VISIBLE
        errorView.visibility = View.GONE

        if (loadingPhoto) {
            progressOverlay.visibility = View.VISIBLE
        } else {
            progressOverlay.visibility = View.GONE
        }
    }

    private fun handleOnFailure() {
        photoView.visibility = View.GONE
        errorView.visibility = View.VISIBLE
        progressOverlay.visibility = View.GONE
    }

    fun errorUploadingPhoto() {
        if (::photoView.isInitialized) {
            photoView.visibility = View.GONE
            errorView.visibility = View.VISIBLE
            closeAction.visibility = View.VISIBLE
            progressOverlay.visibility = View.GONE
        } else {
            Log.e("PhotoView", "PhotoView not initialized")
        }
    }

    private fun dpToPx(dp: Float): Float {
        return dp * Resources.getSystem().displayMetrics.density
    }

    private fun setUpCloseAction() {
        closeAction.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    fun hideProgress() {
        progressOverlay.visibility = View.GONE
    }

    fun showProgress() {
        progressOverlay.visibility = View.VISIBLE
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
