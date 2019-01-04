/*
 *    Copyright 2018 Horácio Flávio Comé Júnior
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

package io.github.horaciocome1.reeque.ui.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.github.horaciocome1.reeque.data.posts.Post
import io.github.horaciocome1.reeque.databinding.ItemUserPostBinding

class UserPostsAdapter(private val context: Context,
                       private val list: List<Post>)
    : RecyclerView.Adapter<UserPostsAdapter.ViewHolder>() {

    lateinit var binding: ItemUserPostBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemUserPostBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].run {
            binding.post = this
            Picasso.with(context).load(cover).into(binding.itemUserPostCoverImageview)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

}