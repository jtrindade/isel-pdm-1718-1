package pt.isel.pdm.jht.viewcontacts

import android.Manifest
import android.app.ListActivity
import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.TextView
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.widget.SimpleCursorAdapter
import android.widget.Toast


class MainActivity : ListActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private val CONTACTS_LOADER = 88

    val adapter by lazy {
        val from = arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.LOOKUP_KEY)
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)

        SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, from, to, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listView.adapter = adapter

        Toast.makeText(this, "Create", Toast.LENGTH_LONG).show()

        if (hasPermissionToReadContacts())
            loaderManager.initLoader(CONTACTS_LOADER, null, this)
        else
            Toast.makeText(this, "[NO PERMISSION TO READ CONTACTS]", Toast.LENGTH_LONG).show()
    }

    private fun hasPermissionToReadContacts() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
            when (id) {
                CONTACTS_LOADER ->
                    CursorLoader(
                        this,
                        ContactsContract.Contacts.CONTENT_URI,
                        arrayOf(
                                ContactsContract.Contacts._ID,
                                ContactsContract.Contacts.DISPLAY_NAME,
                                ContactsContract.Contacts.LOOKUP_KEY
                        ),
                        ContactsContract.Contacts.IN_VISIBLE_GROUP + "=1",
                        null,
                        ContactsContract.Contacts.DISPLAY_NAME + " ASC"
                    )
                else ->
                    throw IllegalArgumentException("unknown id: $id")
            }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        Toast.makeText(this, "Load Finished", Toast.LENGTH_LONG).show();
        adapter.changeCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        Toast.makeText(this, "Loader Reset", Toast.LENGTH_LONG).show();
        adapter.changeCursor(null)
    }
}