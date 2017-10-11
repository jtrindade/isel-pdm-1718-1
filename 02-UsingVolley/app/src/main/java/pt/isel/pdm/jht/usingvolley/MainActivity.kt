package pt.isel.pdm.jht.usingvolley

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val TEACHERS_URI = "https://adeetc.thothapp.com/api/v1/teachers"

    private val requestQueue by lazy { Volley.newRequestQueue(this) }

    private val txtBox by lazy { findViewById(R.id.txtBox) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue.add(JsonObjectRequest(
                TEACHERS_URI,
                null,
                {
                    // txtBox.text = it.toString()

                    val jsonTeachers = it.get("teachers") as JSONArray

                    val teachers = jsonTeachers.asSequence().joinToString(
                            separator = "\n",
                            transform = { it.getString("shortName") }
                    )

                    txtBox.text = teachers
                },
                {
                    txtBox.text = "::ERROR::\n${ it.toString()}"
                }
        ))
    }
}

fun JSONArray.asSequence() =
        (0 until length()).asSequence().map { get(it) as JSONObject }