package pt.isel.pdm.jht.profslist.provider

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

object ProfsContract {

    val AUTHORITY = "pt.isel.pdm.profs.provider"

    val CONTENT_URI = Uri.parse("content://" + AUTHORITY)

    val MEDIA_BASE_SUBTYPE = "/vnd.profs."

    object Profs : BaseColumns {
        val RESOURCE = "profs"

        val CONTENT_URI = Uri.withAppendedPath(
                ProfsContract.CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CODE = "code"
        val NAME = "name"
        val EMAIL = "email"

        val PROJECT_ALL = arrayOf(BaseColumns._ID, CODE, NAME, EMAIL)

        val DEFAULT_SORT_ORDER = NAME + " ASC"
    }
}