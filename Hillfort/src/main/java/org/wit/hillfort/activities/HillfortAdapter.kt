package org.wit.hillfort.activities
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import kotlinx.android.synthetic.main.card_list_hillfort.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel

interface HillfortListener {
    fun onHillfortClick(hillfort: HillfortModel)
    fun onOptionsItemSelected(item: MenuItem?): Boolean
}

class HillfortAdapter(private var hillforts: List<HillfortModel>, private val listener: HillfortListener) : RecyclerView.Adapter<HillfortAdapter.MainHolder>(), Filterable, AnkoLogger {

    val hillfortsFull: List<HillfortModel>? = hillforts
    private var recycleFilter : RecycleFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_list_hillfort, parent, false))
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
            if (hillfort.rating > 0) {
                itemView.starRatingIcon.visibility = View.VISIBLE
                itemView.cardRatingNumber.visibility = View.VISIBLE
                itemView.cardRatingNumber.text = hillfort.rating.toString()
            } else {
                itemView.starRatingIcon.visibility = View.INVISIBLE
                itemView.cardRatingNumber.visibility = View.INVISIBLE
            }
            if (hillfort.visited){
                itemView.visitedIndicator.setBackgroundColor(Color.parseColor("#5db761"))
                itemView.visitedIndicator.setText(R.string.isVisited)
            } else {
                itemView.visitedIndicator.setBackgroundColor(Color.parseColor("#FF9E9E9E"))
                itemView.visitedIndicator.setText(R.string.notVisited)
            }
            if (hillfort.favourited){
                itemView.favouritedIcon.setImageResource(R.drawable.filled_green_heart)
            } else {
                itemView.favouritedIcon.setImageResource(R.drawable.outline_green_heart)
            }
        }
    }

    override fun getFilter(): Filter {
        if (recycleFilter == null){
            recycleFilter = RecycleFilter()
        }
        return recycleFilter as RecycleFilter
    }

    inner class RecycleFilter: Filter(){

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var results = FilterResults()
            info { "HILLFORTS THE 2ND ARRAY " + hillfortsFull}
            if (constraint != null && constraint.isNotEmpty()) {
                var localList : ArrayList<HillfortModel> = ArrayList()
                for (i : Int in 0..hillfortsFull?.size?.minus(1) as Int){
                    if (hillfortsFull?.get(i)?.title?.toLowerCase()?.contains(constraint.toString().toLowerCase()) as Boolean){
                        localList.add(hillfortsFull?.get(i))
                    }
                }
                results.values = localList
                info { "RESULT: " + localList }
                results.count = localList.size
            } else {
                results.values = hillfortsFull
                info { "RESULT: " + hillfortsFull }
                results.count = hillfortsFull?.size as Int
            }
            info { "RETURN RESULTS" +  results}
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            info { "HILLFORTS THE RESULTS !!! " + results?.values }
            info { "HILLFORTS THE BEFORE " + hillforts }
            hillforts = results?.values as List<HillfortModel>
            notifyDataSetChanged()
        }


    }

}