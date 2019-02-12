/*
 *    Copyright 2019 Horácio Flávio Comé Júnior
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and limitations under the License.
 */

package io.github.horaciocome1.reaque.ui.users

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.horaciocome1.reaque.databinding.FragmentProfileBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_profile.*

class MyProfileFragment: Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUsers().observe(this, Observer { users ->
            when {
                users.isEmpty() -> {
                    fragment_profile_cover_imageview.visibility = View.GONE
                    fragment_profile_scrollview.visibility = View.GONE
                    fragment_profile_profile_pic_imageview.visibility = View.GONE
                }
                else -> {
                    fragment_profile_cover_imageview.visibility = View.VISIBLE
                    fragment_profile_profile_pic_imageview.visibility = View.VISIBLE
                    fragment_profile_scrollview.visibility = View.VISIBLE
                    fragment_profile_progressbar.visibility = View.GONE

                    users[0].run {
                        binding.user = this
                        Glide.with(this@MyProfileFragment).load(pic).run {
                            apply(RequestOptions.bitmapTransform(BlurTransformation(7, 14)))
                                .into(fragment_profile_cover_imageview)
                            apply(RequestOptions.circleCropTransform())
                                .into(binding.fragmentProfileProfilePicImageview)
                        }

                        fragment_profile_more_button.setOnClickListener {
                            val openUserPosts = MyProfileFragmentDirections.actionOpenUserPosts(id, name)
                            Navigation.findNavController(it).navigate(openUserPosts)
                        }

                        fragment_profile_profile_pic_imageview.setOnClickListener {
                            val openImage = MyProfileFragmentDirections.actionOpenImage(pic)
                            Navigation.findNavController(it).navigate(openImage)
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.show()
    }

}