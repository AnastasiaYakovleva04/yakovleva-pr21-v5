package com.example.yakovleva_pr21_v5

import androidx.fragment.app.Fragment

class ExpenseFragment: Fragment {
    private lateinit var expense: Expense
    private lateinit var name: EditText
    private lateinit var password: EditText
    private lateinit var enter: Button
    private lateinit var registration: Button
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name = findViewById(R.id.name_edittext)
        password = findViewById(R.id.password_edittext)
        enter = findViewById(R.id.enter_btn)
        registration = findViewById(R.id.registration_btn)

        prefs = getSharedPreferences("expense_app", Context.MODE_PRIVATE)
        val gson = Gson()

        registration.setOnClickListener{
            val username = name.text.toString()
            val pass = password.text.toString()
            if (username.isEmpty() || pass.isEmpty()) {
                showAlert("Ошибка", "Все поля должны быть заполнены")
            }
            else {
                if (pass.length < 8){
                    showAlert("Ошибка", "Пароль должен содержать минимум 8 символов")
                }
                val user = User(username, pass)
                val userJson = gson.toJson(user)
                prefs.edit().putString("user", userJson).apply()
                finish()
            }
        }
        enter.setOnClickListener {
            val username = name.text.toString()
            val pass = password.text.toString()
            if (username.isEmpty() || pass.isEmpty()) {
                showAlert("Ошибка", "Все поля должны быть заполнены")
            }
            else {
                val userJson = prefs.getString("user", null)
                if (userJson == null) {
                    showAlert("Ошибка", "Нет такого пользователя. Зарегистрируйтесь")
                }
                else {
                    val savedUser = gson.fromJson(userJson, User::class.java)
                    if (savedUser.username == username && savedUser.password == pass) {
                        prefs.edit().putBoolean("is_logged_in", true).apply()
                        //открыть фрагмент
                        finish()
                    } else {
                        showAlert("Ошибка", "Неверный логин или пароль")
                    }
                }
            }
        }
    }
    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}