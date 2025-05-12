package com.example.pocketninja

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pocketninja.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityBinding:ActivityMainBinding
    private lateinit var expenseDatabase:ExpenseDatabaseImplementation
    private lateinit var categoriesSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        mainActivityBinding.ReceiptImageView.visibility = View.INVISIBLE
        val categories = mutableListOf("Food","Electricity","Mobile Data","Fashion")
        expenseDatabase = ExpenseDatabaseImplementation(this)
        categoriesSpinner = mainActivityBinding.categoryPicker


        val adapterArray= ArrayAdapter(this, android.R.layout.simple_spinner_item,categories)
        adapterArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoriesSpinner.adapter = adapterArray

        categoriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selection = parent?.getItemAtPosition(position).toString()
                mainActivityBinding.catResult.text = selection
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val calendarPicker = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            calendarPicker.set(Calendar.YEAR,year)
            calendarPicker.set(Calendar.MONTH,month)
            calendarPicker.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            ApplyDate(calendarPicker)
        }
        mainActivityBinding.AddDateImageView.setOnClickListener {
            DatePickerDialog(this,datePicker,calendarPicker.get(Calendar.YEAR),calendarPicker.get(Calendar.MONTH),calendarPicker.get(Calendar.DAY_OF_MONTH)).show()

        }

        mainActivityBinding.AddPictureImageView3.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        }

        mainActivityBinding.SaveButton.setOnClickListener {
            val description = mainActivityBinding.NotesEditText.text.toString()
            val category = mainActivityBinding.catResult.text.toString()
            val date = mainActivityBinding.DateEditText.text.toString()
            val amount = mainActivityBinding.AmountEditTextNumberDecimal.text.toString()
            val expensePicture = imageToByteArrayConverter(mainActivityBinding.ReceiptImageView)

            val expense = Expense(0,description,category,date,amount.toDouble(),expensePicture)
            expenseDatabase.insertExpense(expense)

            Toast.makeText(this,"Expense Added Successfully",Toast.LENGTH_LONG).show()

            val intent = Intent(this,ViewExpenses::class.java)
            startActivity(intent)
        }


    }
    private fun ApplyDate(calendar: Calendar){
        val dateFormat = "yyyy-MM-dd"
        val simple = SimpleDateFormat(dateFormat, Locale.UK)
        mainActivityBinding.DateEditText.text = simple.format(calendar.time)
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data != null) {
                    val receiptImage = result.data?.extras?.get("data") as Bitmap

                    mainActivityBinding.ReceiptImageView.visibility = View.VISIBLE
                    mainActivityBinding.ReceiptImageView.setImageBitmap(receiptImage)
                }
            }
        }
    fun imageToByteArrayConverter(image: ImageView): ByteArray {

        val InitialImage = image.drawable
        val bitmap = (InitialImage as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}