package sk.hyll.dominik.exercise11_shoppinglistdb

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.*
import java.util.*


class MainActivity : AppCompatActivity(), NewItem.NewItemListener {


    private var list: ArrayList<String>? = null
    private var listView: ListView? = null
    private var adapter: SimpleCursorAdapter? = null

    private var db: SQLiteDatabase? = null

    private var totalText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            val ni = NewItem()
            ni.show(fragmentManager, "new_item")
        }

        totalText = findViewById(R.id.total) as TextView
        listView = findViewById(R.id.listView) as ListView
        db = DatabaseOpenHelper(this).writableDatabase;

        queryData()

        this.listView!!.onItemLongClickListener = AdapterView.OnItemLongClickListener { adapterView, view, i, l ->
            val popup = PopupMenu(applicationContext, view)
            popup.setOnMenuItemClickListener {
                //  Toast.makeText(this@MainActivity,  adapterView.selectedItem.toString(), Toast.LENGTH_SHORT).show()


                val item = adapter!!.getItem(i) as SQLiteCursor
                val id = item.getString(0)
                db?.delete(DatabaseOpenHelper.DATABASE_TABLE, "_id=?", arrayOf(id))

                queryData()

                Toast.makeText(this@MainActivity, getString(R.string.remove_item) + item.getString(1), Toast.LENGTH_SHORT).show()
                true
            }
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.menu_main, popup.menu)
            popup.show()

            false
        }

    }

    fun queryData() {

        val resultColumns = arrayOf("_id", "name", "count", "price")
        val cursor = db?.query(DatabaseOpenHelper.DATABASE_TABLE, resultColumns, null, null, null, null, "price", null)

        countTotal(cursor)

        // add data to adapter
        adapter = SimpleCursorAdapter(this,
                R.layout.list_item, cursor,
                arrayOf("name", "count", "price"), // from
                intArrayOf(R.id.name, R.id.count, R.id.price)    // to
                , 0)  // flags

        // show data in listView
        listView?.adapter = adapter
    }

    fun countTotal(cursor: Cursor?) {
        var sum = 0.0

        if (cursor!!.moveToFirst()) {
            do {
                sum += cursor.getDouble(3)

            } while (cursor.moveToNext())
        }

        totalText?.text = getString(R.string.total) +  " %.2f".format(sum)

    }


    override fun onNewItemCreated(name: String, count: Int, price: Double) {

        val values = ContentValues()
        values.put(DatabaseOpenHelper.NAME, name)
        values.put(DatabaseOpenHelper.COUNT, count)
        values.put(DatabaseOpenHelper.PRICE, price)
        db?.insert(DatabaseOpenHelper.DATABASE_TABLE, null, values)

        queryData()
        Toast.makeText(this, getString(R.string.item_added) + name, Toast.LENGTH_SHORT).show()
    }


}