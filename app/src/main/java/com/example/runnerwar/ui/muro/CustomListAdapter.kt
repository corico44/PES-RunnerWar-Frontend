package com.example.runnerwar.ui.muro

import com.example.runnerwar.R
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomListAdapter(private val mContext: Context, private val itemsItems: List<Items>) :

    BaseAdapter() {
    private var inflater: LayoutInflater? = null
    override fun getCount(): Int {
        return itemsItems.size
    }

    override fun getItem(location: Int): Any {
        return itemsItems[location]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ResourceType")
    override fun getView(position: Int, scoreView: View, parent: ViewGroup): View {
        Log.d("1 ", "entra get view")
        var scoreView = scoreView
        val holder: ViewHolder
        if (inflater == null) {
            Log.d("1 ", "entra primer if")
            inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (scoreView == null) {
            Log.d("1 ", "entra segundo if")
            scoreView = inflater!!.inflate(R.layout.list_row, parent, false)
            holder = ViewHolder()
            holder.name = scoreView.findViewById<View>(R.id.name) as TextView
            holder.score = scoreView.findViewById<View>(R.id.score) as TextView
            scoreView.tag = holder
        } else {
            Log.d("1 ", "entra else")
            holder = scoreView.tag as ViewHolder
        }
        val m = itemsItems[position]
        holder.name!!.text = m.name
        holder.score!!.text = m.score
        return scoreView
    }

    internal class ViewHolder {
        var name: TextView? = null
        var score: TextView? = null
    }
}