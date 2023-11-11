package com.example.a2125777

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.a2125777.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import org.json.JSONArray

data class CartoonItem(
    val title: String,
    val year: Int,
    val creator: List<String>,
    val rating: String,
    val genre: List<String>,
    val episodes: Int,
    val id: Int,
    val imageUrl: String
)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var spinner: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var textViewYear: TextView
    private lateinit var textViewCreator: TextView
    private lateinit var textViewRating: TextView
    private lateinit var textViewGenre: TextView
    private lateinit var textViewEpisodes: TextView
    private lateinit var textViewId: TextView
    private lateinit var imageView: ImageView
    private val data = mutableListOf<CartoonItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.spinner
        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ArrayList())
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        textViewYear = binding.textViewYear
        textViewCreator = binding.textViewCreator
        textViewRating = binding.textViewRating
        textViewGenre = binding.textViewGenre
        textViewEpisodes = binding.textViewEpisodes
        textViewId = binding.textViewId
        imageView = binding.imageView

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                // wait for button click
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // does not do anything here
            }
        })

        binding.btnQuote.setOnClickListener {
            // check data
            if (data.isNotEmpty()) {
                val selectedTitle = spinnerAdapter.getItem(spinner.selectedItemPosition)
                val selectedItem = data.find { it.title == selectedTitle }
                if (selectedItem != null) {
                    textViewYear.text = "Year: ${selectedItem.year}"
                    textViewCreator.text = "Creator: ${selectedItem.creator.joinToString(", ")}"
                    textViewRating.text = "Rating: ${selectedItem.rating}"
                    textViewGenre.text = "Genre: ${selectedItem.genre.joinToString(", ")}"
                    textViewEpisodes.text = "Episodes: ${selectedItem.episodes.toString()}"
                    textViewId.text = "ID: ${selectedItem.id.toString()}"

                    // to load and display image
                    Picasso.get().load(selectedItem.imageUrl).into(imageView)
                }
            }
        }

        // retrieve data and fill in spinner options
        handleRetrieveQuoteWithVolley()
    }

    private fun jsonArrayToStringList(jsonArray: JSONArray): List<String> {
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        return list
    }

    private fun parseJson(jsonArray: JSONArray) {
        data.clear()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val title = jsonObject.getString("title")
            val year = jsonObject.getInt("year")
            val creator = jsonArrayToStringList(jsonObject.getJSONArray("creator"))
            val rating = jsonObject.getString("rating")
            val genre = jsonArrayToStringList(jsonObject.getJSONArray("genre"))
            val episodes = jsonObject.getInt("episodes")
            val id = jsonObject.getInt("id")
            val imageUrl = jsonObject.getString("image")

            val cartoonItem =
                CartoonItem(title, year, creator, rating, genre, episodes, id, imageUrl)
            data.add(cartoonItem)
            spinnerAdapter.add(title)
        }
        spinnerAdapter.notifyDataSetChanged()
    }

    private fun handleRetrieveQuoteWithVolley() {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, "https://api.sampleapis.com/cartoons/cartoons2D", null,
            Response.Listener { response ->
                parseJson(response)
            },
            Response.ErrorListener { error ->
                binding.textView.text = "That didn't work! Error: ${error.message}"
            }
        )
        queue.add(jsonArrayRequest)
    }
}