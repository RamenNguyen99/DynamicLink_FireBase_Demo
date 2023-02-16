package com.example.dynamiclink_firebase_demo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamiclink_firebase_demo.databinding.ItemPostsBinding
import com.example.dynamiclink_firebase_demo.model.Posts
import com.squareup.picasso.Picasso

/**
 * @author mvn-lucnguyen2-dn
 */
class PostAdapter(private val items: List<Posts>, private val onItemClicked: (Int) -> Unit) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            ItemPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) {
            onItemClicked(it)
        }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size

    class PostViewHolder(
        private val binding: ItemPostsBinding,
        private val onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Posts) {
            binding.tvTitlePosts.text = item.title
            binding.tvDescriptionPosts.text = item.description
            Picasso.get().load(item.image).into(binding.imgPosts)
            itemView.setOnClickListener {
                onItemClicked(item.id)
            }
        }
    }
}
