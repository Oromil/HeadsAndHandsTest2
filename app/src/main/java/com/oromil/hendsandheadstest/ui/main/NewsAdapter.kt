package com.oromil.hendsandheadstest.ui.main

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.oromil.hendsandheadstest.R
import com.oromil.hendsandheadstest.data.entities.StoryEntity
import com.oromil.hendsandheadstest.ui.base.BaseAdapter
import kotlinx.android.synthetic.main.item_storie.view.*

class NewsAdapter : BaseAdapter<NewsAdapter.NewsViewHolder, StoryEntity>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(getView(R.layout.item_storie, parent))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.init(dataList[position])
    }

    class NewsViewHolder(itemView: View) : BaseAdapter.BaseViewHolder(itemView) {
        override fun init(item: Any) {
            item as StoryEntity
            itemView.tvTitle.text = item.title
            itemView.tvDescription.text = item.abstract
            itemView.tvDate.text = item.createdDate.take(10)
            Glide.with(itemView).setDefaultRequestOptions(RequestOptions()
                    .placeholder(R.drawable.ic_default2)
                    .centerCrop().error(R.drawable.ic_default2))
                    .load(item.multimedia[item.multimedia.size - 1].url)
                    .into(itemView.imageView)
        }
    }
}