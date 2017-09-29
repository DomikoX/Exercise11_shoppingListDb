package sk.hyll.dominik.exercise11_shoppinglistdb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by DomikoX on 28-Sep-17.
 */

class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {


    companion object {
        val DATABASE_NAME = "ShoppingList_database"
        val DATABASE_TABLE = "items"
        val NAME = "name"
        val COUNT = "count"
        val PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $DATABASE_TABLE (_id INTEGER PRIMARY KEY AUTOINCREMENT, $NAME TEXT, $COUNT INTEGER, $PRICE REAL);")
        // create sample data
        val values = ContentValues()
        val names = arrayOf("Eggs","Milk","Yogurt","Mineral water")
        val counts = intArrayOf(15,2,4,6)
        val prices = doubleArrayOf(6.5,1.32,0.89,17.42)

        for (i in  0..names.size-1){
            values.put(NAME,names[i])
            values.put(COUNT,counts[i])
            values.put(PRICE,prices[i])
            db.insert(DATABASE_TABLE, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE)
        onCreate(db)
    }

}
