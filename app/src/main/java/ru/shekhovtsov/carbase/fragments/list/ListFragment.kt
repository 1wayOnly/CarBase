package ru.shekhovtsov.carbase.fragments.list

import android.os.Bundle
import android.text.Layout
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.shekhovtsov.carbase.R
import ru.shekhovtsov.carbase.databinding.FragmentListBinding
import ru.shekhovtsov.carbase.model.Car
import ru.shekhovtsov.carbase.util.SwipeToConfig
import ru.shekhovtsov.carbase.viewmodel.CarViewModel

//  Основной фрагмент приложения
//  Здесь отображается список всех машин

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentListBinding
    private lateinit var mViewModel: CarViewModel
    //  Использую инициализацию lazy
    private val adapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)

        // Отображение списка автомобилей
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        //  Я использую компонент андроид разработки View Model - здесь я привязываю модель к фрагменту
        mViewModel = ViewModelProvider(this).get(CarViewModel::class.java)
        //  наблюдаем за LiveData и изменяем содержимое списка
        mViewModel.getAllCars.observe(viewLifecycleOwner, Observer { cars ->
            adapter.setData(cars)
        })

        //  Открытие меню
        setHasOptionsMenu(true)

        //  Открытие фрагмента добавления новой машины
        binding.floatingActionButton.setOnClickListener {
            //  ВАЖНО!!!
            //  Как избежать использования R, используя ViewBinding?!?!?!?
            findNavController().navigate(R.id.action_list_to_add)
        }

        //  Реализую открытие изменения элемента по свайпу, для этого был создан абстрактный класс SwipeToConfig
        val item = object: SwipeToConfig(requireContext(), 0, ItemTouchHelper.LEFT) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //  В случае свайпа открываем фрагмент и передаём в него открываемый элемент
                //  КОСЯК!!!
                //  В случае сортированного списка отправляет не на ту тачку
                //  Проблема в определении текущего элемента
                val action = ListFragmentDirections.actionListToConfigureFragment(mViewModel.getAllCars.value!!.get(viewHolder.adapterPosition))
                findNavController().navigate(action)
            }
        }
        val itemTouchHelper = ItemTouchHelper(item)
        itemTouchHelper.attachToRecyclerView(binding.recycler)


        return binding.root
    }


    //  Привязка сёрчвью к меню
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
        val search = menu?.findItem(R.id.search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }


    //  Сортируем по алфавиту и даем адаптеру понять что дата изменилась
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort) {
            mViewModel.sortAllCars().observe(this, { list ->
                list.let {
                    adapter.setData(it)
                }
            })
        }
        return super.onOptionsItemSelected(item)
    }


    //  Функция поиска с подстановкой значения в запрос
    private fun searchDB(query: String) {
        val searchQuery = "%$query%"
        mViewModel.searchDB(searchQuery).observe(requireActivity(), { list ->
            list.let {
                adapter.setData(it)
            }
        })
    }


    //  Вызываем метод поиска при нажатии на кнопку поиска
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDB(query)
        }
        return true
    }


    //  Вызываем метод поиска при изменении текста поиска, дабы список подгружался сразу, после нажатия одной буквы
    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDB(query)
        }
        return true
    }


}