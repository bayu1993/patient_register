package com.example.dell.registerapps.ui.update

import android.app.DatePickerDialog
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.RadioButton
import com.example.dell.registerapps.R
import com.example.dell.registerapps.data.PatientData
import com.example.dell.registerapps.data.PatientData.Companion.ADDRESS
import com.example.dell.registerapps.data.PatientData.Companion.DATE
import com.example.dell.registerapps.data.PatientData.Companion.GENDER
import com.example.dell.registerapps.data.PatientData.Companion.NAME
import com.example.dell.registerapps.data.PatientData.Companion.TBL_PATIENT
import com.example.dell.registerapps.data.PatientData.Companion.TELP
import com.example.dell.registerapps.data.database
import com.example.dell.registerapps.ui.main.MainActivity.Companion.DATA_PATIENT
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.db.update
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    private lateinit var data : PatientData
    private lateinit var name:String
    private lateinit var gender:String
    private lateinit var date:String
    private lateinit var telp:String
    private lateinit var address:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupBackButton()
        initData()
        getDate()
        fab_save.setOnClickListener {
            formValidate()
        }

    }

    private fun setupBackButton() {
        supportActionBar?.title = getString(R.string.update_data)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initData() {
        data = intent.getParcelableExtra(DATA_PATIENT)
        name = data.name
        gender = data.gender
        date = data.birthDate
        telp = data.telp
        address = data.address

        tv_title_form.text = getString(R.string.update_data)
        edt_name.setText(name)
        edt_birthDate.setText(date)
        edt_telp.setText(telp)
        edt_address.setText(address)
        when(gender.toLowerCase().trim()){
            "male" ->{
                rb_male.isChecked = true
                rb_female.isChecked = false
            }
            "female" ->{
                rb_female.isChecked = true
                rb_male.isChecked = false
            }
        }
    }

    private fun getDate() {
        val date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setDate()
        }

        edt_birthDate.setOnClickListener {
            DatePickerDialog(this@UpdateActivity, date,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setDate() {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        edt_birthDate.setText(sdf.format(calendar.time))
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
            else -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Update Confirmation")
                builder.setMessage("Are you sure want to save this data?")
                        .setPositiveButton("Yes") { _, _ ->
                            updateData()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                val dialog = builder.create()
                dialog.show()
            }
        }

    }

    private fun updateData() {
        try {
            database.use{
                update(TBL_PATIENT,
                        NAME to name,
                        GENDER to gender,
                        DATE to date,
                        TELP to telp,
                        ADDRESS to address)
                        .whereArgs("ID_ = {id}","id" to data.id)
                        .exec()
            }
            toast("Update successful")
            this.finish()
        }catch (error : SQLiteConstraintException){
            fab_save.snackbar("error : ${error.localizedMessage}")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return true
    }
}
