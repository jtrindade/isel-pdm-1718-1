package pt.isel.pdm.jht.usingvolley

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val TEACHERS_URI = "https://adeetc.thothapp.com/api/v1/teachers"

    private val lstProfs by lazy { findViewById(R.id.lstProfs) as ListView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        application.requestQueue.add(JsonObjectRequest(
                TEACHERS_URI,
                null,
                {
                    val jsonTeachers = it.get("teachers") as JSONArray

                    val teachers = jsonTeachers
                                        .asSequence()
                                        .map { it.getString("shortName") ?: "---" }
                                        .toList()
                                        .toTypedArray()

                    var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, teachers)

                    lstProfs.adapter = adapter

                    lstProfs.setOnItemClickListener { parent, view, position, id ->
                        Toast.makeText(this, teachers[position], Toast.LENGTH_SHORT).show()
                    }
                },
                {
                    Log.e("pdm/profsList", it.toString())
                    Toast.makeText(this, "::ERROR::", Toast.LENGTH_SHORT).show()
                }
        ))
    }
}

fun JSONArray.asSequence() =
        (0 until length()).asSequence().map { get(it) as JSONObject }