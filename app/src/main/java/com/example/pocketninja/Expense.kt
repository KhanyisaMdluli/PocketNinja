package com.example.pocketninja

data class Expense(val id:Int,val expenseDescription:String,
    val category:String,val expenseDate:String,val expenseAmount:Double,val expensePicture:ByteArray)
