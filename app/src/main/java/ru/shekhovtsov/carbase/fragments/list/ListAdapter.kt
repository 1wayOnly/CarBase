package ru.shekhovtsov.carbase.fragments.list

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.shekhovtsov.carbase.databinding.CarRowBinding
import ru.shekhovtsov.carbase.databinding.PicViewerBinding
import ru.shekhovtsov.carbase.model.Car

//  Адаптер для RecyclerView в ListFragment

class ListAdapter: RecyclerView.Adapter<ListAdapter.MViewHolder>() {

    //  список машин для отображения
    private var carList = emptyList<Car>()

    inner class MViewHolder(val binding: CarRowBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Car) {
            binding.txtBrand.text = car.brand
            binding.txtHorsepower.text = car.horsepower.toString() + " horsepower"
            binding.txtCountry.text = car.country
            binding.imgCar.setImageBitmap(car.carPic)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.MViewHolder {
        return MViewHolder(CarRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListAdapter.MViewHolder, position: Int) {
        val currentItem = carList[position]
        holder.bind(currentItem)

        //  Я выбрал способ просмотра миниаюты через alertDialog написав кастомный лэйаут для него
        val bindingAlert = PicViewerBinding.inflate(LayoutInflater.from(holder.itemView.context), null, false)
        //  Важно строить диалог вне слушателя, через переменную, так как аттач в паренту может быть только один
        val alertDialog = AlertDialog.Builder(holder.itemView.context).setView(bindingAlert.root).create()
        holder.binding.imgCar.setOnClickListener {
            alertDialog.show()
            bindingAlert.carBigPic.setImageBitmap(currentItem.carPic)
        }
    }

    override fun getItemCount() = carList.size

    //  этот метод используется в классе фрагмента, для обновления информации содержимого (LiveData)
    fun setData(cars: List<Car>) {
        this.carList = cars
        notifyDataSetChanged()
    }


}
