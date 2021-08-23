package ru.shekhovtsov.carbase.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.shekhovtsov.carbase.model.Car
import ru.shekhovtsov.carbase.util.Converter

//  Основной класс Базы данных


@Database(entities = [Car::class], version = 3, exportSchema = false)
@TypeConverters(Converter::class)
abstract class CarDatabase: RoomDatabase() {

    abstract fun carDao(): CarDao

    companion object {
        @Volatile
        private var INSTANCE: CarDatabase? = null

        fun getDB(context: Context): CarDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            //  добавил здесь fallbackToDestructiveMigration так как требовалось добавить столбец
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarDatabase::class.java,
                    "car_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}