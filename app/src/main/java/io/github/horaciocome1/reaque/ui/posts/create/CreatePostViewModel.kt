package io.github.horaciocome1.reaque.ui.posts.create

import android.net.Uri
import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.storage.StorageRepository
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.util.ObservableViewModel

class CreatePostViewModel(
    topicsRepository: TopicsRepository,
    private val postsRepository: PostsRepository,
    private val storageRepository: StorageRepository
) : ObservableViewModel() {

    val post = Post("").apply {
        topic.title = "Selecione um tópico."
    }
    var imageUri: Uri = Uri.EMPTY

    @Bindable
    val title = MutableLiveData<String>()

    @Bindable
    val message = MutableLiveData<String>()

    val topics = topicsRepository.topics

    var isCreatingPost = false

    val isPostReady: Boolean
        get() {
            post.run {
                return title.isNotBlank() && message.isNotBlank() && topic.id.isNotBlank() && imageUri != Uri.EMPTY
            }
        }

    fun create(view: View): CreatePostViewModel {
        isCreatingPost = true
        storageRepository.uploadImage(imageUri, post.topic) {
            post.pic = it
            postsRepository.create(post) { navigateUp(view) }
        }
        return this
    }

    fun navigateUp(view: View) = view.findNavController().navigateUp()

}