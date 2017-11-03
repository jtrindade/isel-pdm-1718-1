package pt.isel.pdm.jht.viewcontacts

import android.Manifest
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
import android.widget.Toast


class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private val CONTACTS_LOADER = 88

    private val txtBox by lazy { findViewById(R.id.txtBox) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "Create", Toast.LENGTH_LONG).show()
        if (hasPermissionToReadContacts())
            loaderManager.initLoader(CONTACTS_LOADER, null, this)
        else
            txtBox.text = "[NO PERMISSION TO READ CONTACTS]"
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
        if (data != null) {
            showContacts(data)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        Toast.makeText(this, "Loader Reset", Toast.LENGTH_LONG).show();
        txtBox.text = "[NO CONTACTS]"
    }

    private fun showContacts(contactsCursor: Cursor) {
        if (contactsCursor.count > 0) {
            val idxName = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val idxKey = contactsCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)
            txtBox.text = ""
            contactsCursor.moveToFirst()
            do {
                val name = contactsCursor.getString(idxName)
                val key = contactsCursor.getString(idxKey)
                txtBox.append("$name   [$key]\n")
            } while (contactsCursor.moveToNext())
        } else {
            txtBox.text = "[NO CONTACTS]"
        }
    }
}