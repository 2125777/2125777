package com.example.a2125777

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.a2125777.databinding.ActivityMainBinding
import org.json.JSONArray

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnQuote.setOnClickListener {
            handleRetrieveQuoteWithVolley()
        }
    }

    private fun parseJson(jsonArray: JSONArray): List<String> {
        val titles = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val title = jsonObject.getString("title")
            titles.add(title)
        }

        return titles
    }

    private fun handleRetrieveQuoteWithVolley() {
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, "https://api.sampleapis.com/cartoons/cartoons2D", null,
            { response ->
                val titles = parseJson(response)
                val titleText = titles.joinToString("\n") // Join the titles into a single string with line breaks
                binding.textView.text = titleText
            },
            { error ->
                binding.textView.text = "That didn't work! Error: ${error.message}"
            }
        )
        queue.add(jsonArrayRequest)
    }
}







