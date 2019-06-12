package io.github.horaciocome1.reaque.data.posts

import com.google.firebase.firestore.DocumentReference
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User

class PostsRepository private constructor(private val service: PostsService) : PostsServiceInterface {

    override fun create(post: Post, onSuccessListener: (DocumentReference) -> Unit) =
        service.create(post, onSuccessListener)

    override fun get(post: Post) = service.get(post)

    override fun get(user: User) = service.get(user)

    override fun get(topic: Topic) = service.get(topic)

    companion object {

        @Volatile
        private var instance: PostsRepository? = null

        fun getInstance(service: PostsService) = instance
            ?: synchronized(this) {
                instance ?: PostsRepository(service).also { instance = it }
            }

    }

}