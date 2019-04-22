package io.github.horaciocome1.reaque.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.github.horaciocome1.reaque.databinding.FragmentEditProfileBinding
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.let {
            it.lifecycleOwner = this
            it.viewmodel = viewModel
        }
        viewModel.me.observe(this, Observer {
            viewModel.run {
                bio.value = it.bio
                address.value = it.address
                binding.viewmodel = this
            }
        })
        viewModel.bio.observe(this, Observer {
            viewModel.user.bio = it
            submit_button.isEnabled = isProfileReady
        })
        viewModel.address.observe(this, Observer {
            viewModel.user.address = it
            submit_button.isEnabled = isProfileReady
        })
    }

    private val isProfileReady: Boolean
        get() {
            viewModel.user.run {
                return name.isNotBlank() && bio.isNotBlank() && address.isNotBlank()
            }
        }

}