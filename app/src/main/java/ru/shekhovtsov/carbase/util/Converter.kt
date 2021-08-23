package ru.shekhovtsov.carbase.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

//  Так как есть необходимость хранить изображения в БД
//  Так как RoomDB не может хранить в себе Bitmap
//  Написал 2 метода с аннотацией @TypeConverter, которые преобразуют Bitmap в ByteArray (в формат который можно пихнуть в БД)
//  Абстракт класс БД с аннотацией @TypeConverters, что говорит о том, что есть необходимость конвертировать входные значения
//  Это даёт нам указать в качестве столбца сущности Bitmap а хранить ByteArray
class Converter {

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

}