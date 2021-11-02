package ru.startandroid.develop.p0761tab.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/*
    Сам по себе фрагмент еще не создает функциональность постраничной навигации. Для этого нам
        нужен один из классов PagerAdapter. Android SDK содержит ряд встроенных реализаций
        PagerAdapter, в частности, класс FragmentStateAdapter. Этот класс являются абстрактным,
        поэтому напрямую мы его использовать не можем, и нам нужно создать класс-наследник.
        Для этого добавим в проект новый класс, который назовем MyAdapter.
 */
class MyAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    //возвращает количество страниц, которые будут в ViewPager2 (в нашем случае 10)
    override fun getItemCount(): Int {
        return 3
    }

    //по номеру страницы, передаваемому в качестве параметра position, возвращает объект фрагмента
    override fun createFragment(position: Int): Fragment {
        return (PageFragment.newInstance(position))
    }
}