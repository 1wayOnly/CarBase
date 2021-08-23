package ru.shekhovtsov.carbase.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import ru.shekhovtsov.carbase.data.CarDao
import ru.shekhovtsov.carbase.model.Car

//  В данном проекте я использую паттерн Repository
//  Данный паттерн позволяет иметь доступ к элементам БД, без каких-либо взаимодействий с технической частью БД
//  Исколючительно методы для добавления / удаления и получения сущностей


class CarRepo(private val carDao: CarDao): ViewModel() {

    val getAllCars: LiveData<List<Car>> = carDao.getAllCars()

    //  Добавление машины
    fun addCar(car: Car) {
        carDao.addCar(car)
    }

    //  Изменение машины
    suspend fun updateCar(car: Car) {
        carDao.updateCar(car)
    }

    //  Удаление машины
    suspend fun deleteCar(car: Car) {
        carDao.deleteCar(car)
    }

    //  Сортировка машин по алфавиту
    fun sortAllCars(): Flow<List<Car>> {
        return carDao.sortAllCars()
    }

    //  Поиск по базе по бренду и стране производителе
    fun searchDB(searchQuery: String): Flow<List<Car>> {
        return carDao.searchDB(searchQuery)
    }

}