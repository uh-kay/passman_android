package com.example.passman

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val data: List<DataViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = data[position]
        holder.setupView(dataModel.title, dataModel.username, dataModel.image)
    }

    override fun getItemCount() = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title : TextView = view.findViewById<TextView>(R.id.title_text)
        var username : TextView = view.findViewById<TextView>(R.id.username_text)
        var image : ImageView = view.findViewById<ImageView>(R.id.imageview)

        fun setupView(titleData: String, usernameData: String, imageData: Int) {
            title.text = titleData
            username.text = usernameData
            image.setImageResource(imageData)

            itemView.setOnClickListener {
                val intent = Intent(this, PasswordDetail::class.java)
            }
        }
    }
}