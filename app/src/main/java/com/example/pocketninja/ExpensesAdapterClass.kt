package com.example.pocketninja

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.exp

class ExpensesAdapterClass(private var expenses:List<Expense>,context: Context):
RecyclerView.Adapter<ExpensesAdapterClass.ExpenseViewHolder>(){

    class ExpenseViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val categoryDescription = itemView.findViewById<TextView>(R.id.CategoryDescriptionTextView)
        val amount = itemView.findViewById<TextView>(R.id.transactionAmountTxt)
        val imageview = itemView.findViewById<ImageView>(R.id.imageView2)
        val arrow = itemView.findViewById<ImageView>(R.id.transactionDetailsArrow)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val expenseView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout,parent,false)
        return ExpenseViewHolder(expenseView)

    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val transaction = expenses[position]
        holder.categoryDescription.text = transaction.category
        holder.amount.text = "R ${transaction.expenseAmount}"
        holder.imageview.setImageBitmap(byteArrayToBitmap(transaction.expensePicture))

        holder.arrow.setOnClickListener {
            val intent = Intent(holder.itemView.context,ExpenseDetails::class.java).apply {
                putExtra("expenseId",position)
            }
            holder.itemView.context.startActivity(intent)
        }

    }

    fun updateItems(newItems:List<Expense>){
        expenses = newItems
        notifyDataSetChanged()
    }
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}