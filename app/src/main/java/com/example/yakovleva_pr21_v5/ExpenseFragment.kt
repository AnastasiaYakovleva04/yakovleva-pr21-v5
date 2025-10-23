package com.example.yakovleva_pr21_v5

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date

class ExpenseFragment(private val userName: String): Fragment() {

    private lateinit var welcomeName: TextView
    private lateinit var checkAllBtn: Button
    private lateinit var addBtn: Button
    private lateinit var expenseEditText: EditText
    private lateinit var sumEditText: EditText
    private lateinit var gson: Gson
    private lateinit var prefs: SharedPreferences
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = inflater.inflate(R.layout.expense_fragment, container, false)
        welcomeName = view.findViewById(R.id.welcome_name)
        checkAllBtn = view.findViewById(R.id.checkAllBtn)
        addBtn = view.findViewById(R.id.addBtn)
        expenseEditText = view.findViewById(R.id.expense_edittext)
        sumEditText = view.findViewById(R.id.sum_edittext)

        prefs = requireContext().getSharedPreferences("expense_app", Context.MODE_PRIVATE)
        gson = Gson()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welcomeName.text = "Добро пожаловать, $userName"
    }

    override fun onStart() {
        super.onStart()

        //обработчик нажатия кнопки добавить
        addBtn.setOnClickListener{
            val expenseName = expenseEditText.text.toString()
            val sum = sumEditText.text.toString()
            if (expenseName.isEmpty() || sum.isEmpty()) {
                showSnackBar(view, "Все поля должны быть заполнены")
            }
            else {
                try {
                    val expense = Expense(sum.toDouble(), expenseName, Date())
                    val expenseJson = gson.toJson(expense)
                    val expCount = prefs.getInt("expenses_count", 0)
                    prefs.edit()
                        .putString("expense_$expCount", expenseJson)
                        .putInt("expenses_count", expCount + 1)
                        .apply()

                    expenseEditText.text.clear()
                    sumEditText.text.clear()

                    showSnackBar(view, "Расход добавлен")
                }
                catch (e: NumberFormatException){
                    showSnackBar(view, "Некорректный ввод суммы")
                }
            }
        }
        //обработчик нажатия кнопки посмотреть все расходы
        checkAllBtn.setOnClickListener {
            val newFragment = AllExpensesFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    //показ снекбара
    private fun showSnackBar(view:View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sky_blue))
        snackbar.setTextColor(ContextCompat.getColor(requireContext(), R.color.cream))
        snackbar.show()
    }
}