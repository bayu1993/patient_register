package com.example.dell.registerapps.ui.register

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.RadioButton
import com.example.dell.registerapps.R
import com.example.dell.registerapps.data.PatientData.Companion.ADDRESS
import com.example.dell.registerapps.data.PatientData.Companion.DATE
import com.example.dell.registerapps.data.PatientData.Companion.GENDER
import com.example.dell.registerapps.data.PatientData.Companion.NAME
import com.example.dell.registerapps.data.PatientData.Companion.TBL_PATIENT
import com.example.dell.registerapps.data.PatientData.Companion.TELP
import com.example.dell.registerapps.data.database
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    private lateinit var name: String
    private lateinit var gender: String
    private lateinit var date: String
    private lateinit var telp: String
    private lateinit var address: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupBackButton()
        getDate()
        fab_save.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Register Confirmation")
            builder.setMessage("Are you sure want to save this data?")
                    .setPositiveButton("Yes") { _, _ ->
                        formValidate()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun setupBackButton() {
        supportActionBar?.title = getString(R.string.register_new)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getDate() {
        val date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setDate()
        }

        edt_birthDate.setOnClickListener {
            DatePickerDialog(this@RegisterActivity, date,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setDate() {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        edt_birthDate.setText(sdf.format(calendar.time))
    }

    private fun insertData(){
        try {
            database.use{
                insert(TBL_PATIENT,
                        NAME to name,
                        GENDER to gender,
                        DATE to date,
                        TELP to telp,
                        ADDRESS to address)
            }
            toast("Register successful")
            this.finish()
        }catch (e:SQLiteConstraintException){
            fab_save.snackbar("error : ${e.localizedMessage}")
        }
    }

    private fun formValidate(){
        name = edt_name.text.toString().trim()
        date = edt_birthDate.text.toString().trim()
        telp = edt_telp.text.toString().trim()
        address = edt_address.text.toString().trim()

        when(val id = rg_gender.checkedRadioButtonId){
            R.id.rb_female -> gender = findViewById<RadioButton>(id).text.toString().trim()
            R.id.rb_male -> gender = findViewById<RadioButton>(id).text.toString().trim()
            else -> {
                gender = ""
                fab_save.snackbar("please choose your gender")
            }
        }

        when {
            name.isEmpty() -> edt_name.error = "Name cannot be empty, please fill out"
            gender.isEmpty() -> rb_male.error = "please choose your gender"
            date.isEmpty() -> edt_birthDate.error = "Birth date cannot be empty, please fill out"
            telp.isEmpty() -> edt_telp.error = "Telephone number cannot be empty, please fill out"
            address.isEmpty() -> edt_address.error = "Address cannot be empty, please fill out"
            else -> insertData()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
