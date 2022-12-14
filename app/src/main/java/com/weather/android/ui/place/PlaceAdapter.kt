package com.weather.android.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.weather.android.R
import com.weather.android.logic.model.Place
import com.weather.android.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_weather.*

class PlaceAdapter (private  val fragment: PlaceFragment,private val placeList: List<Place>):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val placeName: TextView=view.findViewById(R.id.placeName)
        val placeAddress: TextView=view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        //从搜索城市页面跳转到天气界面
        val holder=ViewHolder(view)
        holder.itemView.setOnClickListener{
            val position=holder.layoutPosition
            val place=placeList[position]
            val activity=fragment.activity
            if(activity is WeatherActivity){
                //如果当前fragment所处的activity是WeatherActivity，刷新页面
                activity.drawerLayout.closeDrawers()
                activity.viewModel.locationLng=place.location.lng
                activity.viewModel.locationLat=place.location.lat
                activity.viewModel.placeName=place.name
                activity.refreshWeather()
            }else {
                //否则跳转到WeatherActivity
                val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                }
                fragment.startActivity(intent)
                activity?.finish()
            }
            fragment.viewModel.savePlace(place)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place=placeList[position]
        holder.placeName.text=place.name
        holder.placeAddress.text=place.address
    }

    override fun getItemCount()=placeList.size


    }
