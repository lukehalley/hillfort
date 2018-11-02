package org.wit.hillfort.activities
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel

interface HillfortListener {
    fun onHillfortClick(hillfort: HillfortModel)
    fun onOptionsItemSelected(item: MenuItem?): Boolean
}

class HillfortAdapter(private var hillforts: List<HillfortModel>,
                                  private val listener: HillfortListener) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_hillfort, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView), AnkoLogger {

        fun bind(hillfort: HillfortModel, listener: HillfortListener) {
            itemView.cardHillfortTitle.text = hillfort.title
            itemView.cardHillfortDescription.text = hillfort.description
            itemView.cardHillfortLocation.text = "Address: " + hillfort.address
            itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, hillfort.firstImage))
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }

            if (hillfort.visited){
                itemView.visitedIndicator.setBackgroundColor(Color.parseColor("#5db761"))
                itemView.visitedIndicator.setText(R.string.isVisited)
            } else {
                itemView.visitedIndicator.setBackgroundColor(Color.parseColor("#FF9E9E9E"))
                itemView.visitedIndicator.setText(R.string.notVisited)
            }

        }
    }
}