package ru.shekhovtsov.carbase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import ru.shekhovtsov.carbase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //  Опять использование R класса, видимо я зря решил использовать Android navigation fragments, хотя мне кажется это очень удобным
        //  Здесь я добавляю Nav toolbar with nav controller, который добавляет в тулбар соответствующее название фрагмента и кнопку "назад"
        //  Почему-то активность не запускалась с ошибкой что не может найти айдишник фрагмент хоста, пришлось сплясать с бубнами :)
        val hostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = hostFragment.navController
        setupActionBarWithNavController(navController)

        setContentView(binding.root)
    }


    //  перегрузка метода для возможного способа возвращения через стрелочку в левом верхнем углу
    //  не боюсь ошибки неиницализированной переменной navController, так как работа этого метода после метода onCreate
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }
}