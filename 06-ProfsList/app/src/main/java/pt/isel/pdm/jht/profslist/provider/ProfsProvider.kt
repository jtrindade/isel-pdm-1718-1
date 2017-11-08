package pt.isel.pdm.jht.profslist.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils

/*
 * Original ideas taken from:
 * https://www.grokkingandroid.com/android-tutorial-writing-your-own-content-provider/
 */

class ProfsProvider : ContentProvider() {

    private val PROFS_LST = 1
    private val PROFS_OBJ = 2

    private val URI_MATCHER : UriMatcher

    init {
        URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        URI_MATCHER.addURI(
                ProfsContract.AUTHORITY,
                ProfsContract.Profs.RESOURCE,
                PROFS_LST
        )
        URI_MATCHER.addURI(
                ProfsContract.AUTHORITY,
                ProfsContract.Profs.RESOURCE + "/#",
                PROFS_OBJ
        )
    }

    private var dbHelper: DbOpenHelper? = null

    override fun onCreate(): Boolean {
        dbHelper = DbOpenHelper(context)
        return true
    }

    override fun getType(uri: Uri?): String {
        when (URI_MATCHER.match(uri)) {
            PROFS_LST -> return ProfsContract.Profs.CONTENT_TYPE
            PROFS_OBJ -> return ProfsContract.Profs.CONTENT_ITEM_TYPE
            else -> throw badUri(uri)
        }
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        var sortOrder = sortOrder

        val qbuilder = SQLiteQueryBuilder()
        when (URI_MATCHER.match(uri)) {
            PROFS_LST -> {
                qbuilder.tables = DbSchema.Profs.TBL_NAME
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = ProfsContract.Profs.DEFAULT_SORT_ORDER
                }
            }
            PROFS_OBJ -> {
                qbuilder.tables = DbSchema.Profs.TBL_NAME
                qbuilder.appendWhere(DbSchema.COL_ID + "=" + uri!!.lastPathSegment)
            }
            else -> throw badUri(uri)
        }

        val db = dbHelper!!.readableDatabase
        val cursor = qbuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        return cursor
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        val table : String
        when (URI_MATCHER.match(uri)) {
            PROFS_LST -> table = DbSchema.Profs.TBL_NAME
            else -> throw badUri(uri)
        }

        val db = dbHelper!!.writableDatabase
        val newId = db.insert(table, null, values)

        return ContentUris.withAppendedId(uri, newId)
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        val table : String
        when (URI_MATCHER.match(uri)) {
            PROFS_LST -> {
                table = DbSchema.Profs.TBL_NAME
                if (selection != null) {
                    throw IllegalArgumentException("selection not supported")
                }
            }
            else -> throw badUri(uri)
        }

        val db = dbHelper!!.writableDatabase
        var ndel = db.delete(table, null, null)

        return ndel
    }

    private fun badUri(uri: Uri?) =
        IllegalArgumentException("unknown uri: $uri");
}