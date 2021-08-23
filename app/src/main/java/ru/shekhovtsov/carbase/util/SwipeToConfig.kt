package ru.shekhovtsov.carbase.util

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

//  Абстракт класс наследованый от ItemTouchHelper.SimpleCallback, для свайпа элементов
abstract class SwipeToConfig(context: Context, dragDir: Int, swipeDir: Int): ItemTouchHelper.SimpleCallback(dragDir, swipeDir) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

}