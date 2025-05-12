package com.example.pocketninja

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketninja.databinding.ActivityViewExpensesBinding

class ViewExpenses : AppCompatActivity() {
    private lateinit var viewExpenseBinding:ActivityViewExpensesBinding
    private lateinit var expenseAdapter:ExpensesAdapterClass
    private lateinit var expenseDatabase:ExpenseDatabaseImplementation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewExpenseBinding = ActivityViewExpensesBinding.inflate(layoutInflater)
        setContentView(viewExpenseBinding.root)
        expenseDatabase = ExpenseDatabaseImplementation(this)
        expenseAdapter = ExpensesAdapterClass(expenseDatabase.returnAllExpenses(),this)

        viewExpenseBinding.expensesRecyclerView.layoutManager = LinearLayoutManager(this)
        viewExpenseBinding.expensesRecyclerView.adapter = expenseAdapter

        viewExpenseBinding.AddExpenseButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

}