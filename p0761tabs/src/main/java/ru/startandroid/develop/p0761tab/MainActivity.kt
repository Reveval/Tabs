package ru.startandroid.develop.p0761tab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.startandroid.develop.p0761tab.databinding.ActivityMainBinding
import ru.startandroid.develop.p0761tab.fragments.MyAdapter

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //val pager = findViewById<ViewPager2>(R.id.pager)
        val pageAdapter = MyAdapter(this)
        binding.pager.adapter = pageAdapter

        //val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        /*
            TabLayoutMediator позволяет связать ViewPager2 и TabLayout.
            Конструктор TabLayoutMediator принимает три параметра: объекты ViewPager2 и TabLayout
                и реализацию интерфейса TabConfigurationStrategy, которая с помощью метода
                onConfigureTab() получает отдельную вкладку в виде объекта Tab и номер страницы и
                позволяет настроить вид вкладки, например, установить заголовок вкладки.
         */
        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.pager) {
                tab, position -> tab.text = "Страница ${position + 1}"
        }
        tabLayoutMediator.attach()
    }
}