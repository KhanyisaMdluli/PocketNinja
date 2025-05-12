package com.example.pocketninja

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract.CommonDataKinds.Note
import kotlin.math.exp

class ExpenseDatabaseImplementation(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null,
    DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "Expensedb"
        private const val DATABASE_VERSION = 3
        private const val NAME_OF_TABLE = "Expenses"
        private const val ID = "Id"
        private const val DESCRIPTION = "Description"
        private const val CATEGORY = "Category"
        private const val TransactionDate = "Date"
        private const val Amount = "Amount"
        private const val ExpensePhoto = "Photo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createExpenseTable = "CREATE TABLE $NAME_OF_TABLE(" +
                "$ID INT PRIMARY KEY," +
                "$DESCRIPTION TEXT," +
                "$CATEGORY TEXT," +
                "$TransactionDate TEXT," +
                "$Amount DOUBLE," +
                "$ExpensePhoto BLOB)"
        db?.execSQL(createExpenseTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val removeTableQuery = "DROP TABLE IF EXISTS $NAME_OF_TABLE"
        db?.execSQL(removeTableQuery)
        onCreate(db)
    }

    fun insertExpense(expense: Expense){
        val expenseDb = writableDatabase
        val values = ContentValues().apply {
            put(DESCRIPTION,expense.expenseDescription)
            put(CATEGORY, expense.category)
            put(TransactionDate, expense.expenseDate)
            put(Amount, expense.expenseAmount)
            put(ExpensePhoto,expense.expensePicture)
        }
        expenseDb.insert(NAME_OF_TABLE,null,values)
        expenseDb.close()
    }

    fun getTransactionById(expenseId:Int):Expense{
        val expenseDb = readableDatabase
        val query = "SELECT * FROM $NAME_OF_TABLE " +
                "WHERE $ID = $expenseId"
        val cursor = expenseDb.rawQuery(query,null)
        cursor.moveToLast()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
        val description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
        val category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(TransactionDate))
        val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(Amount))
        val image = cursor.getBlob(cursor.getColumnIndexOrThrow(ExpensePhoto))

        cursor.close()
        expenseDb.close()
        val expense = Expense(id,description,category,date,amount,image)
        return expense
    }

    fun returnAllExpenses():List<Expense>{
        val expenseDb = readableDatabase
        val expenses = mutableListOf<Expense>()
        val query = "SELECT * FROM $NAME_OF_TABLE"
        val expenseCursor = expenseDb.rawQuery(query,null)

        while (expenseCursor.moveToNext()){
            val expenseId = expenseCursor.getInt(expenseCursor.getColumnIndexOrThrow(ID))
            val description = expenseCursor.getString(expenseCursor.getColumnIndexOrThrow(DESCRIPTION))
            val category = expenseCursor.getString(expenseCursor.getColumnIndexOrThrow(CATEGORY))
            val date = expenseCursor.getString(expenseCursor.getColumnIndexOrThrow(TransactionDate))
            val amount = expenseCursor.getDouble(expenseCursor.getColumnIndexOrThrow(Amount))
            val expensePicture = expenseCursor.getBlob(expenseCursor.getColumnIndexOrThrow(ExpensePhoto))

            val expense = Expense(expenseId,description,category,date,amount,expensePicture)
            expenses.add(expense)
        }
        expenseCursor.close()
        expenseDb.close()
        return expenses
    }
}