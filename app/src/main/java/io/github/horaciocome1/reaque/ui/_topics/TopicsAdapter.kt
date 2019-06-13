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

package io.github.horaciocome1.reaque.ui._topics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.databinding.ItemTopicBinding
import io.github.horaciocome1.reaque.databinding.ItemTopicPostingBinding

class TopicsAdapter(private val list: List<Topic>) : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

    private lateinit var binding: ItemTopicBinding

    //    the indicator of last clicked topic
    private var indicator: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.run {
            topic = list[position]
            indicator.visibility = View.INVISIBLE
            imageview.setOnClickListener {
                this@TopicsAdapter.indicator?.visibility = View.INVISIBLE
                indicator.visibility = View.VISIBLE
                this@TopicsAdapter.indicator = indicator
            }
        }
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class Simple(private val list: List<Topic>) : RecyclerView.Adapter<Simple.ViewHolder>() {

        lateinit var binding: ItemTopicPostingBinding

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            binding = ItemTopicPostingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding.root)
        }

        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            binding.topic = list[position]
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    }

}