package ru.shekhovtsov.carbase.fragments.add

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.shekhovtsov.carbase.R
import ru.shekhovtsov.carbase.databinding.FragmentAddBinding
import ru.shekhovtsov.carbase.model.Car
import ru.shekhovtsov.carbase.viewmodel.CarViewModel


//  Фрагмент добавления новой машины

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var mCarViewModel: CarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(layoutInflater)

        //  Установка дефолтных значений
        setDefaults()

        //  Я использую компонент андроид разработки View Model - здесь я привязываю модель к фрагменту
        mCarViewModel = ViewModelProvider(this).get(CarViewModel::class.java)

        //  Применение вбитых пользователем параметров и добавление новой машины в БД
        binding.btnAddNewCar.setOnClickListener {
            applyNewCar()
        }

        //  Загрузка изображения из галереи (проверки на вес нету) ((сжатия картинки тоже))
        binding.btnUpload.setOnClickListener {
            openGalleryForImage()
        }

        return binding.root
    }


    //  Использую view binding вместе с R классом.......
    //  А вообще этот метод отвечает за дефолтные параметры новой машины
    private fun setDefaults() {
        binding.carImgView.setImageResource(R.mipmap.def_car_img)
    }


    //  Метод для добавления новой машины
    private fun applyNewCar() {
        val brand = binding.editBrandName.text.toString()
        val country = binding.editCounty.text.toString()
        val horsepower = binding.editHorsepower.text
        //  проверка корректного ввода полей
        if (correctInputCheck(brand, country, horsepower)) {
            val car = Car(0, brand, country, Integer.parseInt(horsepower.toString()), (binding.carImgView.drawable as BitmapDrawable).bitmap)
            mCarViewModel.addCar(car)
            Toast.makeText(requireContext(), "Successfully added new car", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        } else {
            Toast.makeText(requireContext(), "Incorrect params entered", Toast.LENGTH_SHORT).show()
        }
    }


    //  проверка правильности ввода данных
    private fun correctInputCheck(brand: String, country: String, horsepower: Editable): Boolean {
        return !(TextUtils.isEmpty(brand) && TextUtils.isEmpty(country) && horsepower.isEmpty())
    }


    //  намерение выбрать фотку пользователем
    //  тут депрекейтед метод, аналогов на просторах интернета не нашёл, все советы про startActivityForResult
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }


    //  пикнув фотку, загружаем её в ImageView в перегруженом методе
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            binding.carImgView.setImageURI(data?.data)
        }
    }
}