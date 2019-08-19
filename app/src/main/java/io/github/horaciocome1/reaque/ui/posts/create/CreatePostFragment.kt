package io.github.horaciocome1.reaque.ui.posts.create

import android.Manifest
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.databinding.FragmentCreatePostBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.util.Constants
import io.github.horaciocome1.reaque.util.InjectorUtils
import io.github.horaciocome1.reaque.util.OnFocusChangeListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_create_post.*

class CreatePostFragment : Fragment() {

    lateinit var binding: FragmentCreatePostBinding

    private val viewModel: CreatePostViewModel by lazy {
        val factory = InjectorUtils.createPostViewModelFactory
        ViewModelProviders.of(this, factory)[CreatePostViewModel::class.java]
    }

    private lateinit var selectTopicBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var selectPicBehavior: BottomSheetBehavior<MaterialCardView>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDraft()
        binding.viewmodel = viewModel
        select_pic_from_gallery_button.setOnClickListener {
            val permission = ContextCompat.checkSelfPermission(
                activity as MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (permission == PackageManager.PERMISSION_GRANTED)
                pickImageFromGallery()
            else
                requestStoragePermission()
        }
        OnFocusChangeListener(context).let {
            title_edittext?.onFocusChangeListener = it
            message_edittext?.onFocusChangeListener = it
        }
        selectTopicBehavior = BottomSheetBehavior.from(select_topics_bottomsheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            skipCollapsed = true
        }
        selectPicBehavior = BottomSheetBehavior.from(select_pic_bottomsheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            skipCollapsed = true
        }
        topics_recyclerview?.addOnItemClickListener { _, position ->
            binding.topics?.let {
                if (it.isNotEmpty()) {
                    selectTopicBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    viewModel.post.topic = it[position]
                    binding.viewmodel = viewModel
                    create_button.isEnabled = viewModel.isPostReady
                    saveDraft()
                }
            }
        }
        select_topic_button?.setOnClickListener {
            selectTopicBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        select_pic_button?.setOnClickListener {
            selectPicBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        toolbar?.setNavigationOnClickListener {
            viewModel.navigateUp(it)
            saveDraft()
        }
        create_button?.setOnClickListener {
            binding.viewmodel = viewModel.create(it)
        }
        preview_button?.setOnClickListener {
            val directions = CreatePostFragmentDirections.actionOpenPreviewMessage(
                viewModel.post.message
            )
            view.findNavController().navigate(directions)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.title.observe(this, Observer {
            saveDraft()
            viewModel.post.title = it
            create_button.isEnabled = viewModel.isPostReady
        })
        viewModel.message.observe(this, Observer {
            saveDraft()
            viewModel.post.message = it
            create_button.isEnabled = viewModel.isPostReady
            preview_button.visibility = if (it.isBlank())
                View.GONE
            else
                View.VISIBLE
        })
        viewModel.topics.observe(this, Observer {
            binding.topics = it
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Constants.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE -> {
                        viewModel.imageUri = data?.data!!
                        binding.viewmodel = viewModel
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                pickImageFromGallery()
            else
                Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_LONG).show()
        }
    }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        }
        startActivityForResult(intent, Constants.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE)
    }

    private fun requestStoragePermission() {
        if (activity is MainActivity) {
            val permission = ActivityCompat.shouldShowRequestPermissionRationale(
                activity as MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (permission) {
                AlertDialog.Builder(activity as MainActivity)
                    .setTitle(resources.getString(R.string.permission_needed))
                    .setMessage(resources.getString(R.string.permission_needed_explanation))
                    .setPositiveButton(resources.getString(R.string.confirm)) { _, _ ->
                        ActivityCompat.requestPermissions(
                            activity as MainActivity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.STORAGE_PERMISSION_CODE
                        )
                    }
                    .setNegativeButton(resources.getString(R.string.reject)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else
                ActivityCompat.requestPermissions(
                    activity as MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.STORAGE_PERMISSION_CODE
                )
        }
    }

    private fun saveDraft() {
        activity?.let {
            val postTitle = viewModel.post.title
            val postMessage = viewModel.post.message
            val topicId = viewModel.post.topic.id
            val topicTitle = viewModel.post.topic.title
            val sharedPreferences = it.getSharedPreferences(Constants.SharedPreferences.NAME, MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putString(Constants.SharedPreferences.POST_TITLE, postTitle)
                putString(Constants.SharedPreferences.POST_MESSAGE, postMessage)
                putString(Constants.SharedPreferences.TOPIC_ID, topicId)
                putString(Constants.SharedPreferences.TOPIC_TITLE, topicTitle)
                apply()
            }
        }
    }

    private fun loadDraft() {
        activity?.let { activity ->
            val sharedPreferences = activity.getSharedPreferences(Constants.SharedPreferences.NAME, MODE_PRIVATE)
            sharedPreferences.getString(Constants.SharedPreferences.TOPIC_ID, "")?.let {
                if (it.isNotBlank())
                    viewModel.post.topic.id = it
            }
            sharedPreferences.getString(Constants.SharedPreferences.TOPIC_TITLE, "")?.let {
                if (it.isNotBlank())
                    viewModel.post.topic.title = it
            }
            sharedPreferences.getString(Constants.SharedPreferences.POST_TITLE, "")?.let {
                if (it.isNotBlank())
                    viewModel.title.value = it
            }
            sharedPreferences.getString(Constants.SharedPreferences.POST_MESSAGE, "")?.let {
                if (it.isNotBlank())
                    viewModel.message.value = it
            }
        }
    }

}