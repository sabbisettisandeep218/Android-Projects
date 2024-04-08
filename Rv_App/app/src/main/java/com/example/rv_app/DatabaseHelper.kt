package com.example.rv_app

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context):SQLiteOpenHelper(context,database_name,null,database_version){
    companion object{
        const val database_name="RvTable"
        const val database_version=1
        const val Desc="Description"
        const val delCheck="Deletion"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val dbquery = ("CREATE TABLE $database_name ($Desc TEXT)")
        db.execSQL(dbquery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO("Not yet implemented")
    }
    fun insertData(data:Rvdata):Long{
        val db=writableDatabase
        val datavalues=ContentValues().apply {
            put(Desc,data.desc)
        }
        return db.insert(database_name,null,datavalues)
    }


    @SuppressLint("Recycle", "Range")
    fun getData():List<Rvdata>{
        val db=readableDatabase
        var cursor: Cursor= db.rawQuery("select * from $database_name",null)
        val rvItems= mutableListOf<Rvdata>()
        while(cursor.moveToNext()){ //Move the cursor to the next row
            val getDesc=cursor.getString((cursor.getColumnIndexOrThrow(Desc)))
            val delIschecked=cursor.getInt(cursor.getColumnIndex(delCheck))!= 0
            rvItems.add(Rvdata(getDesc,delIschecked))

        }
        cursor.close()
        return rvItems
    }
    fun updateData(oldDesc:String,newDesc:String):Int{
        val db=writableDatabase
        val datavalues=ContentValues()
        datavalues.put(Desc,newDesc)
        val selection="$Desc = ?"
        return db.update(database_name,datavalues,selection, arrayOf(oldDesc))
    }

    fun deleteData(desc:String){
        val db=writableDatabase
        val selection="$Desc = ?"
        db.delete(database_name,selection, arrayOf(desc))
    }
    fun deleteAllData(){
        val db=writableDatabase
        val delQuery="delete from $database_name"
        db.execSQL(delQuery)

    }
}