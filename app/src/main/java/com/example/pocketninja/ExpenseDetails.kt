package com.example.pocketninja

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pocketninja.databinding.ActivityExpenseDetailsBinding

class ExpenseDetails : AppCompatActivity() {

    private lateinit var expenseDetailsBinding: ActivityExpenseDetailsBinding
    private lateinit var expenseDatabase:ExpenseDatabaseImplementation
    private var expense_id:Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        expenseDetailsBinding = ActivityExpenseDetailsBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_expense_details)
        expenseDatabase = ExpenseDatabaseImplementation(this)

        expense_id = intent.getIntExtra("expense_id",-1)
        if(expense_id == -1){
            finish()
            return
        }
        //getting the selected expense
        val expense = expenseDatabase.getTransactionById(expense_id)

        expenseDetailsBinding.DescriptionTextView.text = expense.expenseDescription
        expenseDetailsBinding.CategoryTextView.text = expense.category
        expenseDetailsBinding.DateTextView.text = expense.expenseDate
        expenseDetailsBinding.AmountTextView.text = expense.expenseAmount.toString()
        expenseDetailsBinding.RecieipimageView.setImageBitmap(byteArrayToBitmap(expense.expensePicture))
    }
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}