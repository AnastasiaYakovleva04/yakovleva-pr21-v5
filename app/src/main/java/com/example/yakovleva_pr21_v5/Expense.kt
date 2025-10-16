package com.example.yakovleva_pr21_v5

import com.android.identity.util.UUID

data class Expense(val id: String = UUID.randomUUID().toString(),
                   val sum: Double,
                   val description: String,
                   val date: String)
