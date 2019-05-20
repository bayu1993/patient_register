package com.example.dell.registerapps.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PatientData(
        val id : Int,
        val name : String,
        val gender : String,
        val birthDate : String,
        val telp : String,
        val address : String
) : Parcelable {
    companion object{
        const val TBL_PATIENT = "TBL_PATIENT"
        const val ID = "ID_"
        const val NAME = "NAME"
        const val GENDER = "GENDER"
        const val DATE = "DATE"
        const val TELP = "TELP"
        const val ADDRESS = "ADDRESS"
    }
}