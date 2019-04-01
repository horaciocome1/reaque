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

package io.github.horaciocome1.reaque.ui.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.databinding.ItemTopicBinding

class TopicsAdapter(private val list: List<Topic>) : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

    private lateinit var binding: ItemTopicBinding
    private lateinit var context: Context
    private var viewHolder: ViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        binding = ItemTopicBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.indicator.visibility = View.INVISIBLE
        list[position].run {
            Glide.with(context)
                .load(cover)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageview)
            binding.topic = this
        }
        holder.itemView.setOnClickListener {
            viewHolder?.indicator?.visibility = View.INVISIBLE
            holder.indicator.visibility = View.VISIBLE
            viewHolder = holder
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val indicator = view.findViewById<View>(R.id.indicator)!!

    }

}