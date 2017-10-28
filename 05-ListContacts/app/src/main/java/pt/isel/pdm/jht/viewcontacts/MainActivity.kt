package pt.isel.pdm.jht.viewcontacts

import android.Manifest
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.TextView
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat



class MainActivity : AppCompatActivity() {

    private val txtBox by lazy { findViewById(R.id.txtBox) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (hasPermissionToReadContacts())
            loadContacts()
        else
            txtBox.text = "[NO PERMISSION TO READ CONTACTS]"
    }

    private fun hasPermissionToReadContacts() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED

    private fun loadContacts() {
        var contactsCursor: Cursor = readContacts()
        showContacts(contactsCursor)
    }

    private fun readContacts(): Cursor =
        contentResolver.query(
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