package com.example.graduationproject.presentation.achievementdetails

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.DialogFragmentAddPhotoBinding


class DialogAddPhoto : DialogFragment() {


    interface DialogAddPhotoListener {
        fun onOpenCamera()
        fun onChooseFromGallery()
    }

    private var _binding: DialogFragmentAddPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var openCameraActionView: Button
    private lateinit var chooseFromGalleryActionView: Button
    private var listener: DialogAddPhotoListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            parentFragment as? DialogAddPhotoListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DialogAddPhotoListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = DialogFragmentAddPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setUpClickListener()
    }

    private fun initViews() {
        openCameraActionView = binding.openCameraAction
        chooseFromGalleryActionView = binding.chooseFromGalleryAction
    }

    private fun setUpClickListener() {
        setChooseFromGalleryAction()
        setOpenCameraAction()
    }

    private fun setChooseFromGalleryAction() {
        chooseFromGalleryActionView.setOnClickListener {
            listener?.onChooseFromGallery()
            dismiss()
        }
    }

    private fun setOpenCameraAction() {
        openCameraActionView.setOnClickListener {
            listener?.onOpenCamera()
            dismiss()
        }
    }


override fun onStart() {
    super.onStart()
    val dialogWindow = dialog?.window

    dialog?.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
    )
    dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_fragment)
}

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    //    private fun Int.dpToPx(): Int {
//        val density = requireContext().resources.displayMetrics.density
//        return (this * density).toInt()
//    }

}
