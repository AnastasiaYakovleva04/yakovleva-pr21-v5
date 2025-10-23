package com.example.yakovleva_pr21_v5

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import java.text.SimpleDateFormat
import kotlin.math.exp

class AllExpensesFragment:Fragment() {

    private lateinit var backBtn: Button
    private lateinit var expensesContainer: LinearLayout
    private lateinit var totalSum: TextView

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

        view = inflater.inflate(R.layout.all_expenses_fragment, container, false)
        backBtn = view.findViewById(R.id.backBtn)
        expensesContainer = view.findViewById(R.id.expensesContainer)
        totalSum = view.findViewById(R.id.totalSum)

        prefs = requireContext().getSharedPreferences("expense_app", Context.MODE_PRIVATE)
        gson = Gson()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expensesContainer.removeAllViews()
        val expenses = getExpenses()
        var sum = 0.0
        if (expenses.isEmpty()){
            val textview = TextView(requireContext())
            textview.text = "Нет расходов"
            textview.textSize=16f
            textview.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            expensesContainer.addView(textview)
        }
        else{
            for (expense in expenses) {
                val textview = TextView(requireContext())
                textview.text = "Расход ${expense.name} на ${expense.sum} от ${SimpleDateFormat("dd/MM/yyyy").format(expense.date)}"
                textview.textSize=16f
                textview.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                expensesContainer.addView(textview)
                sum+=expense.sum
            }
        }
        totalSum.text = "Сумма: $sum"

    }

    override fun onStart() {
        super.onStart()

        backBtn.setOnClickListener{
            parentFragmentManager.popBackStack()
        }
    }

    //получение списка расходов
    private fun getExpenses(): List<Expense>{
        val expCount = prefs.getInt("expenses_count", 0)
        val expenses = mutableListOf<Expense>()

        for(i in 0 until expCount){
            val expenseJson = prefs.getString("expense_$i", null)
            if (expenseJson != null){
                val expense = gson.fromJson(expenseJson, Expense::class.java)
                expenses.add(expense)
            }
        }
        return expenses
    }

}