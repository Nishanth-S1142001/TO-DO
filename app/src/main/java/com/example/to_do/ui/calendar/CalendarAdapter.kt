package com.example.to_do.ui.calendar

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do.R
import com.example.to_do.databinding.DateItemBinding



class CalendarAdapter (private val calendarInterface: CalendarInterface,
     private val list:ArrayList<CalendarData>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    var pos: Int = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  ViewHolder {
        val binding = DateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
      holder.bind(list[position],position)
    }
    inner class ViewHolder(private val binding: DateItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(calendarDataModel: CalendarData, position: Int) {
                val calendar_day= binding.calendarDay
                var calendar_date= binding.calendarDate
                val cardView= binding.cardCalendar
                if (pos == position) {
                    calendarDataModel.isSelected = true
                }

                if (calendarDataModel.isSelected)
                {
                    pos = -1
                    calendar_day.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.orange)
                    )
                    calendar_date.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.blue)
                    )
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.red)
                    )
                }
                else
                {
                    calendar_day.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.red)
                    )
                    calendar_date.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.white)
                    )
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.red)
                    )
                }
                calendar_day.text = calendarDataModel.calendarDay
                calendar_date.text = calendarDataModel.calendarDate()
                println("Calendar : ${calendar_date.text}")
                cardView.setOnClickListener{
                    calendarInterface.onSelect(calendarDataModel,adapterPosition,calendar_date)

                }
            }
        }


        interface CalendarInterface {
            fun onSelect(calendarData: CalendarData, position: Int, day: TextView)

        }

        fun setPosition(pos: Int) {
            this.pos = pos
        }

        fun updateList(calendarList:ArrayList<CalendarData>)
        {
            list.clear()
            list.addAll(calendarList)
            notifyDataSetChanged()
        }


}