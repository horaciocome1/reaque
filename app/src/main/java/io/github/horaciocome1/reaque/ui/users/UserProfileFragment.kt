package io.github.horaciocome1.reaque.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.databinding.FragmentUserProfileBinding
import io.github.horaciocome1.reaque.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_user_profile.*

class UserProfileFragment : Fragment() {

    lateinit var binding: FragmentUserProfileBinding

    private val viewModel: UsersViewModel by lazy {
        val factory = InjectorUtils.usersViewModelFactory
        ViewModelProviders.of(this, factory)[UsersViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { bundle ->
            val user = User(
                UserProfileFragmentArgs.fromBundle(bundle).userId
            )
            viewModel.get(user).observe(this, Observer {
                binding.user = it
            })
            viewModel.amSubscribedTo(user).observe(this, Observer {
                subscribe_button.visibility = visibility(!it)
                unsubscribe_button.visibility = visibility(it)
            })
        }
    }

    private fun visibility(state: Boolean): Int {
        return if (state)
            View.VISIBLE
        else
            View.GONE
    }

}