package io.github.horaciocome1.reaque.data.ratings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.addSimpleSnapshotListener
import io.github.horaciocome1.reaque.util.map
import io.github.horaciocome1.reaque.util.user

class RatingsService : RatingsServiceIntarface {

    private val tag = "RatingsService"

    private val ref = FirebaseFirestore.getInstance().collection("ratings")

    private val auth = FirebaseAuth.getInstance()

    private val rate = MutableLiveData<Float>().apply { value = 1f }

    override fun rate(post: Post, value: Int, onSuccessListener: (Void) -> Unit) {
        auth.addSimpleAuthStateListener {
            val rating = Rating("").apply {
                this.post = post
                user = it.user
                this.value = value
            }
            ref.document("${it.uid}_${post.id}").set(rating.map).addOnSuccessListener(onSuccessListener)
        }
    }

    override fun get(post: Post): LiveData<Float> {
        auth.addSimpleAuthStateListener { user ->
            ref.document("${user.uid}_${post.id}").addSimpleSnapshotListener(tag) {
                rate.value = it["value"].toString().toFloat()
            }
        }
        return rate
    }

}