package com.example.dell.registerapps.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.dell.registerapps.data.PatientData.Companion.ADDRESS
import com.example.dell.registerapps.data.PatientData.Companion.DATE
import com.example.dell.registerapps.data.PatientData.Companion.GENDER
import com.example.dell.registerapps.data.PatientData.Companion.ID
import com.example.dell.registerapps.data.PatientData.Companion.NAME
import com.example.dell.registerapps.data.PatientData.Companion.TBL_PATIENT
import com.example.dell.registerapps.data.PatientData.Companion.TELP
import org.jetbrains.anko.db.*

class DatabaseHelper (context:Context) : ManagedSQLiteOpenHelper(context, "db_pasient.db",null,1){

    companion object{
        private var instance : DatabaseHelper? = null

        @Synchronized
        fun getInstance(context: Context) : DatabaseHelper{
            if (null == instance){
                instance = DatabaseHelper(context.applicationContext)
            }
            return instance as DatabaseHelper
        }
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TBL_PATIENT,true,
                ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                NAME to TEXT,
                GENDER to TEXT,
                DATE to TEXT,
                TELP to TEXT,
                ADDRESS to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(TBL_PATIENT,true)
    }

}

val Context.database : DatabaseHelper get() = DatabaseHelper.getInstance(applicationContext)