package pt.isel.pdm.beginpdm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var textBox : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textBox = findViewById(R.id.textBox) as TextView?
    }

    public fun onDoSomething(view: View?) {
        val msg = "Message: " + (textBox?.getText() ?: "NONE")
        Log.i("pdm/begin/main", msg)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
