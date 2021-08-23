package ru.shekhovtsov.carbase.fragments.configure

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.shekhovtsov.carbase.R
import ru.shekhovtsov.carbase.databinding.FragmentConfigureBinding
import ru.shekhovtsov.carbase.model.Car
import ru.shekhovtsov.carbase.viewmodel.CarViewModel

//  Фрагмент изменения мощности или фото авто
//  Можно было реализовать через один фрагмент на создание и удаление
//  Я хотел продемонстрировать работу с несколькими фрагментами

class ConfigureFragment : Fragment() {

    private lateinit var binding: FragmentConfigureBinding
    private val args by navArgs<ConfigureFragmentArgs>()
    private lateinit var mCarViewModel: CarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfigureBinding.inflate(layoutInflater)

        //  Сетап полученых args во вью
        //  Так как я использую 'androidx.navigation.safeargs' мне нет необходимости лишний раз дёргать Bundle
        setupView()


        //  Я использую компонент андроид разработки View Model - здесь я привязываю модель к фрагменту
        mCarViewModel = ViewModelProvider(this).get(CarViewModel::class.java)


        //  Обработка нажатия на кнопку обновления и изменение в БД
        binding.btnReconfCar.setOnClickListener {
            confCar()
        }


        //  Интент на открытие фотки из галереи
        binding.btnUpload.setOnClickListener {
            openGalleryForImage()
        }

        //  Подключения меню для возможности удаления элемента
        setHasOptionsMenu(true)


        return binding.root
    }


    //  метод заполнения полученными значениями нашего вью
    private fun setupView() {
        binding.editHorsepower.setText(args.currentcar.horsepower.toString())
        binding.txtBrand.text = args.currentcar.brand
        binding.txtCountry.text = args.currentcar.country
        binding.carImgView.setImageBitmap(args.currentcar.carPic)
    }


    //  Метод для выполнения Update запроса в БД
    private fun confCar() {
        val horsepower = Integer.parseInt(binding.editHorsepower.text.toString())
        if (correctInputCheck(binding.editHorsepower.text)) {
            val configuredCar = Car(args.currentcar.id, args.currentcar.brand, args.currentcar.country, horsepower, (binding.carImgView.drawable as BitmapDrawable).bitmap)
            mCarViewModel.updateCar(configuredCar)
            Toast.makeText(requireContext(), "Successfully reconfigured car", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        } else {
            Toast.makeText(requireContext(), "Incorrect params entered", Toast.LENGTH_SHORT).show()
        }
    }


    //  Метод для удаления машины из БД (вызывается в OptionsMenu)
    private fun deleteCar() {
        AlertDialog.Builder(requireContext())
            .setPositiveButton("Yes") { _, _ ->
                mCarViewModel.deleteCar(args.currentcar)
                Toast.makeText(requireContext(), "Successfully deleted " + args.currentcar.brand, Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .setNegativeButton("No")  { _, _ ->

            }
            .setTitle("Delete " + args.currentcar.brand + " ?")
            .setMessage("Confirm to delete the " + args.currentcar.brand)
            .create().show()
    }


    //  проверка правильности ввода данных
    private fun correctInputCheck(horsepower: Editable): Boolean {
        return horsepower.isNotEmpty()
    }


    //  Перегрузка метода меню, установка моего лэйаута
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }


    //  Указываю, что сделать при нажатии на элемент меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete) {
            deleteCar()
        }
        return super.onOptionsItemSelected(item)
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