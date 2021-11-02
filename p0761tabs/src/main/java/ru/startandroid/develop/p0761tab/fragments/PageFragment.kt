package ru.startandroid.develop.p0761tab.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.startandroid.develop.p0761tab.R

const val ARG_NUM = "num"

//класс фрагмента
class PageFragment : Fragment() {
    //указывает на номер текущей страницы
    private var pageNumber = 0

    /*
        Номер страницы будет передаваться извне через фабричный метод newInstance(). Передача
            номера происходит путем добавления значения в аргумент "num"
     */
    companion object {
        fun newInstance(page: Int) : PageFragment {
            val fragment = PageFragment()
            return fragment.also {
                val args = Bundle()
                args.putInt(ARG_NUM, page)
                it.arguments = args
            }
        }
    }

    /*
        Затем при создании фрагмента в методе onCreate() этот номер будет извлекаться из
            аргумента "num" (если аргументы определены)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = arguments?.getInt(ARG_NUM) ?: 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val result = inflater.inflate(R.layout.fragment_page, container, false)
        return result.also {
            val pageHeader = (it.findViewById<TextView>(R.id.displayText)) as TextView
            val header = "Фрагмент ${pageNumber + 1}"
            pageHeader.text = header
        }
    }
}