package pt.isel.pdm.jht.simpleproflist

import android.app.ListActivity
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ListActivity() {

    data class Prof(val number: Int, val name: String) {
        override fun toString(): String = name
    }

    class ProfViewHolder(val numberTextView: TextView, val nameTextView: TextView) {

    }

    private val TEACHERS_URI = "https://adeetc.thothapp.com/api/v1/teachers"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        application.requestQueue.add(JsonObjectRequest(
                TEACHERS_URI,
                null,
                {
                    val jsonTeachers = it.get("teachers") as JSONArray

                    val teachers = jsonTeachers
                            .asSequence()
                            .map {
                                Prof(
                                        it["number"] as Int,
                                        it["shortName"] as String
                                )
                            }
                            .toList()
                            .toTypedArray()

                    val itemLayoutId = android.R.layout.simple_list_item_2
                    val inflater = LayoutInflater.from(this)
                    val adapter = object : ArrayAdapter<Prof>(this, itemLayoutId, teachers) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                            val itemView =
                                    convertView ?:
                                    inflater.inflate(itemLayoutId, parent, false)
                            if (convertView == null) {
                                val text1 = itemView.findViewById<TextView>(android.R.id.text1)
                                val text2 = itemView.findViewById<TextView>(android.R.id.text2)
                                itemView.tag = ProfViewHolder(text2, text1)
                            }
                            val profViewHolder = itemView.tag as ProfViewHolder
                            profViewHolder.nameTextView.setText(teachers[position].name)
                            profViewHolder.numberTextView.setText(teachers[position].number.toString())

                            profViewHolder.nameTextView.setTextColor(
                                if (teachers[position].name.contains("Jo"))
                                    Color.RED
                                else
                                    Color.BLACK
                            )
                            return itemView
                        }
                    }

                    listView.adapter = adapter

                    listView.setOnItemClickListener { parent, view, position, id ->
                        Toast.makeText(this, teachers[position].number.toString(), Toast.LENGTH_SHORT).show()
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