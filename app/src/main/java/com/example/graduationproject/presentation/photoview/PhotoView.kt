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
import androidx.fragment.app.DialogFragment
import coil.load
import coil.request.CachePolicy
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.example.graduationproject.R
import com.example.graduationproject.databinding.PhotoPreviewBinding

class PhotoView : DialogFragment() {

    companion object {
        private const val PHOTO_URL = "PHOTO_URL"

        fun newInstance(photoUrl: String): PhotoView {
            val fragment = PhotoView()
            val args = Bundle()
            args.putString(PHOTO_URL, photoUrl)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: PhotoPreviewBinding? = null
    private val binding get() = _binding!!
    private var loadingPhoto = true
    private lateinit var photoView: ImageView
    private lateinit var closeAction: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = PhotoPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    private fun getPhotoUrl(onResult: (Boolean) -> Unit) {
        val photoUrl = arguments?.getString(PHOTO_URL)
        Log.d("PhotoView", "loadingPhoto: $loadingPhoto")
        Log.d("PhotoView", "Photo URL: $photoUrl")
        Log.d("PhotoView", "Photo drawable: ${photoView.drawable}")
            photoView.load(photoUrl) {
                placeholder(R.drawable.bg_photo_preview)
                error(R.drawable.photo_example)
                memoryCachePolicy(CachePolicy.ENABLED)
                transformations(RoundedCornersTransformation(dpToPx(100f)))
                listener(
                    onSuccess = { _, _ ->
                        Log.d("PhotoView", "Image loaded successfully")
                        onResult(true)
                    },
                    onError = { _, _ ->
                        Log.e("PhotoView", "Failed to load image")
                        onResult(false)
                    }
                )
            }
    }

    fun errorUploadingPhoto() {
        Log.e("PhotoView", "Showing error photo")
        if (::photoView.isInitialized) {
            photoView.setImageResource(R.drawable.photo_example)
            closeAction.visibility = View.VISIBLE
        } else {
            Log.e("PhotoView", "PhotoView not initialized")
        }
    }


    private fun dpToPx(dp: Float): Float {
        return dp * Resources.getSystem().displayMetrics.density
    }

    private fun setUpCloseAction() {
        closeAction.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

}
