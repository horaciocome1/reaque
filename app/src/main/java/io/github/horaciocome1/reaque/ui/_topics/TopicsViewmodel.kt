package io.github.horaciocome1.reaque.ui._topics

import androidx.lifecycle.ViewModel
import io.github.horaciocome1.reaque.data.topics.TopicsRepository

class TopicsViewmodel(topicsRepository: TopicsRepository) : ViewModel() {


    val notEmptyTopics = topicsRepository.notEmptyTopics

}