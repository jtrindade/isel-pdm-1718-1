package pt.isel.pdm.jht.profslist

import android.app.ListActivity
import android.app.LoaderManager
import android.content.ContentValues
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import pt.isel.pdm.jht.profslist.provider.ProfsContract

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private val PROFS_LOADER = 1

    val adapter by lazy {
        val from = arrayOf(ProfsContract.Profs.NAME, ProfsContract.Profs.EMAIL)
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)

        SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, from, to, 0)
    }

    val listView by lazy { findViewById(R.id.listView) as ListView }

    val editIndex by lazy { findViewById(R.id.editIndex) as EditText }

    val index: Int
        get() = editIndex.text.trim().toString().toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView.adapter = adapter
        loaderManager.initLoader(PROFS_LOADER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
        when (id) {
            PROFS_LOADER ->
                CursorLoader(
                    this,
                    ProfsContract.Profs.CONTENT_URI,
                    ProfsContract.Profs.PROJECT_ALL,
                    null,
                    null,
                    null
                )
            else ->
                throw IllegalArgumentException("unknown id: $id")
        }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        adapter.changeCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        adapter.changeCursor(null)
    }

    fun onInsert(view: View) {
        if (index > 1 && index < Profs.profs.size) {
            Toast.makeText(this, "INSERTING: $index", Toast.LENGTH_LONG).show()

            val prof = Profs.profs[index]
            val newProf = ContentValues()
            newProf.put(ProfsContract.Profs.CODE, prof.code)
            newProf.put(ProfsContract.Profs.NAME, prof.name)
            newProf.put(ProfsContract.Profs.EMAIL, prof.email)
            contentResolver.insert(
                    ProfsContract.Profs.CONTENT_URI,
                    newProf
            )
        } else {
            Toast.makeText(this, "INVALID: $index", Toast.LENGTH_LONG).show()
        }
    }
}
