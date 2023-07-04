package com.nastirlex.superfit.presentation.imageList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nastirlex.superfit.R
import com.nastirlex.superfit.databinding.ItemImagesListDateBinding
import com.nastirlex.superfit.databinding.ItemImagesListImageBinding
import com.nastirlex.superfit.net.dto.PhotoDto
import javax.inject.Inject

class ImagesListAdapter @Inject constructor(
    private val imagesList: MutableList<Any>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ImagesListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewBinding = ItemImagesListImageBinding.bind(view)

        fun bind(image: PhotoDto) {
            viewBinding.imageView2.setImageBitmap(image.bitmap)
        }
    }


    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val viewBinding = ItemImagesListDateBinding.bind(view)

        fun bind(date: String) {
            val monthNumber =
                date.substring(5, 7)

            lateinit var monthName: String

            when (monthNumber) {
                "01" -> {
                    monthName = "January"
                }
                "02" -> {}
                "03" -> {}
                "04" -> {}
                "05" -> {monthName = "May"}
                "06" -> {monthName = "June"}
                "07" -> {monthName = "July"}
                "08" -> {monthName = "August"}
                "09" -> {}
                "10" -> {}
                "11" -> {}
                "12" -> {}
            }

            val year = date.substring(0, 4)

            val newDate = "$monthName, $year"

            viewBinding.textView4.text = newDate
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (imagesList[position] is String)
            return 2
        else if (imagesList[position] is PhotoDto)
            return 1

        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_images_list_image, parent, false)

                return ImagesListViewHolder(view)
            }

            2 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_images_list_date, parent, false)

                return DateViewHolder(view)
            }
        }

        throw IllegalArgumentException("Unknown view type")
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImagesListViewHolder -> {
                when (imagesList[position]) {
                    is PhotoDto -> {
                        holder.bind(imagesList[position] as PhotoDto)
                    }
                }
            }

            is DateViewHolder -> {
                when (imagesList[position]) {
                    is String -> {
                        holder.bind(imagesList[position] as String)
                    }
                }
            }
        }
    }
}