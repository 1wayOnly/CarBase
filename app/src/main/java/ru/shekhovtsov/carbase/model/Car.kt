package ru.shekhovtsov.carbase.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


//  Дата класс - сущность БД Room
//  Всего 5 полей, 4 из которых видимые для юзера, 5-ый - ключ, автогенерируемый БД Room
//  Использую kotlinx.parcelize.Parcelize и android.os.Parcelable для того, чтобы запихнуть объект класса в safe args
@Parcelize
@Entity(tableName = "car_table")
data class Car (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val brand: String,
    val country: String,
    val horsepower: Int,
    val carPic: Bitmap
): Parcelable