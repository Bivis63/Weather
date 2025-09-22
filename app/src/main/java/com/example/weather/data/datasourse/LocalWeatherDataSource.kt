package com.example.weather.data.datasourse

import com.example.weather.domain.model.WeatherRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalWeatherDataSource {

    private val _weatherRequest = MutableStateFlow<List<WeatherRequest>>(emptyList())
    val weatherRequest:Flow<List<WeatherRequest>> = _weatherRequest.asStateFlow()

    fun addWeatherRequest(request: WeatherRequest){
        val currentList = _weatherRequest.value.toMutableList()
        currentList.add(request)
        _weatherRequest.value = currentList
    }
    fun clearRequests(){
        _weatherRequest.value = emptyList()
    }
}