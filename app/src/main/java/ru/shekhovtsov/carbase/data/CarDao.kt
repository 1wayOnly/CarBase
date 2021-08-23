package ru.shekhovtsov.carbase.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.shekhovtsov.carbase.model.Car

//  Интерфейс Data Access Object, показывающий, какие функции работы с БД доступны

@Dao
interface CarDao {

    //  запрос добавления новой машины
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCar(car: Car)

    //  запрос изменения машины
    @Update
    suspend fun updateCar(car: Car)

    //  запрос удаления машины
    @Delete
    suspend fun deleteCar(car: Car)

    //  запрос на получение всех машин сортированных по индексу
    @Query("SELECT * FROM car_table ORDER BY id ASC")
    fun getAllCars() : LiveData<List<Car>>

    //  запрос на получение всех машин сортированных по бренду
    @Query("SELECT * FROM car_table ORDER BY brand ASC")
    fun sortAllCars() : Flow<List<Car>>

    //  запрос на поиск по бренду или стране
    @Query("SELECT * FROM car_table WHERE brand LIKE :searchQuery OR country LIKE :searchQuery")
    fun searchDB(searchQuery: String): Flow<List<Car>>
}