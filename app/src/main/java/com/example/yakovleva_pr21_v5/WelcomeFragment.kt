package com.example.yakovleva_pr21_v5

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class WelcomeFragment: Fragment() {
    private lateinit var user: User
    private lateinit var name: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var registration: Button
    private lateinit var prefs: SharedPreferences
    private lateinit var gson: Gson
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = inflater.inflate(R.layout.welcome_fragment, container, false)

        name = view.findViewById(R.id.name_edittext)
        password = view.findViewById(R.id.password_edittext)
        login = view.findViewById(R.id.login_btn)
        registration = view.findViewById(R.id.registration_btn)

        prefs = requireContext().getSharedPreferences("expense_app", Context.MODE_PRIVATE)
        gson = Gson()

        return view
    }

    override fun onStart() {
        super.onStart()

        //обработчик нажатия кнопки регистрация
        registration.setOnClickListener{
            val username = name.text.toString()
            val pass = password.text.toString()
            if (username.isEmpty() || pass.isEmpty()) {
                showSnackBar(view, "Все поля должны быть заполнены")
            }
            else {
                if (pass.length < 8){
                    showSnackBar(view, "Пароль должен содержать минимум 8 символов")
                }
                else {
                    val user = User(username, pass)
                    val userJson = gson.toJson(user) //превращения объекта user в json строку
                    prefs.edit().putString("user", userJson).apply() //сохранение
                    showSnackBar(view, "Вы успешно зарегистрированы!")
                }
            }
        }
        //обработчик нажатия кнопки вход
        login.setOnClickListener {
            val username = name.text.toString()
            val pass = password.text.toString()
            if (username.isEmpty() || pass.isEmpty()) {
                showSnackBar(view, "Все поля должны быть заполнены")
            }
            else {
                val userJson = prefs.getString("user", null) //превращение json строки из prefs в объект
                if (userJson == null) {
                    showSnackBar(view, "Нет такого пользователя. Зарегистрируйтесь")
                }
                else {
                    val savedUser = gson.fromJson(userJson, User::class.java) //превращение json обратно в объект
                    if (savedUser.username == username && savedUser.password == pass) {
                        prefs.edit().putBoolean("is_logged_in", true).apply()

                        val newFragment = ExpenseFragment(username)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, newFragment)
                            .commit()
                    }
                    else {
                        showSnackBar(view, "Неверный логин или пароль")
                    }
                }
            }
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