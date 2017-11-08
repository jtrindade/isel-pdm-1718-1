package pt.isel.pdm.jht.profslist.provider

import android.provider.BaseColumns

object DbSchema {

    val DB_NAME = "profs.db"
    val DB_VERSION = 1

    val COL_ID = BaseColumns._ID

    object Profs {
        val TBL_NAME = "profs"

        val COL_CODE = ProfsContract.Profs.CODE
        val COL_NAME = ProfsContract.Profs.NAME
        val COL_EMAIL = ProfsContract.Profs.EMAIL

        val DDL_CREATE_TABLE =
                "CREATE TABLE " + TBL_NAME + "(" +
                        COL_ID + " INTEGER PRIMARY KEY, " +
                        COL_CODE + " INTEGER UNIQUE, " +
                        COL_NAME + " TEXT, " +
                        COL_EMAIL + " TEXT UNIQUE)"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }
}