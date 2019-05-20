package com.example.dell.registerapps.ui.main

import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.View.GONE
import android.view.View.VISIBLE
import com.example.dell.registerapps.R
import com.example.dell.registerapps.data.PatientData
import com.example.dell.registerapps.data.PatientData.Companion.TBL_PATIENT
import com.example.dell.registerapps.data.database
import com.example.dell.registerapps.ui.register.RegisterActivity
import com.example.dell.registerapps.ui.update.UpdateActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    companion object{
        const val DATA_PATIENT = "DATA_PATIENT"
    }

    private val data = mutableListOf<PatientData>()
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        getAllData()
        fab_add.setOnClickListener {
            startActivity<RegisterActivity>()
        }
    }

    private fun getAllData() {
        try {
            database.use {
                val result = select(TBL_PATIENT)
                val parser = result.parseList(classParser<PatientData>())
                data.clear()
                data.addAll(parser)
                adapter.notifyDataSetChanged()
                if (data.isEmpty()) tv_empty.visibility = VISIBLE else tv_empty.visibility = GONE
            }
        } catch (e: SQLiteConstraintException) {
            fab_add.snackbar("error : ${e.localizedMessage}")
        }
    }

    private fun initView() {
        adapter = MainAdapter(data) {data,view ->
            val popupMenu = PopupMenu(this@MainActivity,view)
            popupMenu.inflate(R.menu.popup_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menu_update -> {
                        showDialog(data,"Update confirmation", "Are you sure want to update this data"){
                            startActivity<UpdateActivity>(DATA_PATIENT to data)
                        }
                        true
                    }
                    R.id.menu_delete -> {
                        showDialog(data, "Deleted Confirmation","Are you sure want to delete this data"){
                            deleteData(data.id)
                        }
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
        rv_patients.adapter = adapter
        rv_patients.layoutManager = LinearLayoutManager(this)
        rv_patients.setHasFixedSize(true)
    }

    private fun showDialog(data:PatientData, title:String, message:String, listenerDelete : (PatientData) -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
                .setPositiveButton("Yes") { _, _ ->
                    listenerDelete(data)
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteData(id: Int) {
        try {
            database.use {
                delete(TBL_PATIENT, "ID_ = {id}", "id" to id)
                fab_add.snackbar("Deleted data successful")
                getAllData()
            }
        } catch (error: SQLiteConstraintException) {
            fab_add.snackbar("error : ${error.localizedMessage}")
        }
    }

    override fun onResume() {
        super.onResume()
        getAllData()
    }
}
