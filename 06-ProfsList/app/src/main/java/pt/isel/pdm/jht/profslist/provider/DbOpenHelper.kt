package pt.isel.pdm.jht.profslist.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbOpenHelper(context: Context?) : SQLiteOpenHelper(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createDb(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        deleteDb(db)
        createDb(db)
    }

    private fun createDb(db: SQLiteDatabase) {
        db.execSQL(DbSchema.Profs.DDL_CREATE_TABLE)
        db.execSQL(
            "INSERT INTO " + DbSchema.Profs.TBL_NAME + " VALUES (" +
                1 + ", " + 1647 + ", \"Carlos Guedes\", \"cguedes@cc.isel.ipl.pt\"" +
                ");"
        )
        db.execSQL(
            "INSERT INTO " + DbSchema.Profs.TBL_NAME + " VALUES (" +
                2 + ", " + 1754 + ", \"João Trindade\", \"jtrindade@cc.isel.ipl.pt\"" +
                ");"
        )
    }

    private fun deleteDb(db: SQLiteDatabase) {
        db.execSQL(DbSchema.Profs.DDL_DROP_TABLE)
    }

}