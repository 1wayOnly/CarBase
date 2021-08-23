package ru.shekhovtsov.carbase.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.shekhovtsov.carbase.data.CarDatabase
import ru.shekhovtsov.carbase.model.Car
import ru.shekhovtsov.carbase.repos.CarRepo
import kotlinx.coroutines.flow.Flow

//  В качестве демонстрации своих знаний, использую связку LiveData + ViewModel
//  ViewModel позволяет хранить в себе изменения в БД и отображать актуальную информацию во View
//  Использую AndroidViewModel

class CarViewModel(app: Application): AndroidViewModel(app) {

    val getAllCars: LiveData<List<Car>>
    private val carRepo: CarRepo

    init {
        val carDao = CarDatabase.getDB(app).carDao()
        carRepo = CarRepo(carDao)
        getAllCars = carRepo.getAllCars
    }


    //  Добавление машины
    fun addCar(car: Car) {
        viewModelScope.launch(Dispatchers.IO) {
            carRepo.addCar(car)
        }
    }


    //  Изменение машины
    fun updateCar(car: Car) {
        viewModelScope.launch(Dispatchers.IO) {
            carRepo.updateCar(car)
        }
    }


    //  Удаление машины
    fun deleteCar(car: Car) {
        viewModelScope.launch(Dispatchers.IO) {
            carRepo.deleteCar(car)
        }
    }


    //  Сортировка машин по алфавиту
    fun sortAllCars(): LiveData<List<Car>> {
        return carRepo.sortAllCars().asLiveData()
    }


    //  Поиск по базе по бренду и стране производителе
    fun searchDB(searchQuery: String): LiveData<List<Car>> {
        return carRepo.searchDB(searchQuery).asLiveData()
    }

}