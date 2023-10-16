package com.example.a2125777

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.a2125777.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

private lateinit var binding : ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnQuote.setOnClickListener(){
       handleRetrieveQuoteWithVolley()
        }
    }

//    fun getQuote(view: View) {
//        val thread = Thread {
//
//            val url = URL("https://ron-swanson-quotes.herokuapp.com/v2/quotes ")
//
//            try {
//                val connection = url.openConnection()
//                if (connection is HttpURLConnection) {
//                    connection.connectTimeout = 10000
//                    connection.readTimeout = 10000
//                    connection.requestMethod = "GET"
//                    connection.connect()
//
//                    val responseCode = connection.responseCode
//
//                    if (responseCode == HttpURLConnection.HTTP_OK) {
//                        val scanner = Scanner(connection.inputStream)
//                        scanner.useDelimiter("\\A")
//                        val jsonData = if (scanner.hasNext()) scanner.next() else ""
//                        Log.d("jsondata", jsonData)
//                        val jsonArray = JSONArray(jsonData)
//                        val quote = jsonArray[0].toString() // only grab one item the 0
//                        updateTextBoxFromThread(quote)
//                        Log.d("jsonArray[0]", quote)
//
//
//                    } else {
//                        updateTextBoxFromThread("Sorry, there's a problem with the server berchandyaaaa")
//                    }
//
//                } else {
//                    Log.wtf("MAIN_ACTIVITY", "Someone changed the API protocol")
//                }
//            } catch (e: IOException) {
//                updateTextBoxFromThread("Sorry, there was an error processing the data")
//            }
//        }
//        thread.start()
//
//    }
    private fun parseJson(jsonData: String?): String {
        val jsonObject = JSONObject(jsonData)
        val quote = jsonObject.getString("value")
        return quote
    }

//    private fun updateTextBoxFromThread(text: String) {
//        runOnUiThread {
//            binding.textView.text = text
//        }
//
//    }

    private fun handleRetrieveQuoteWithVolley() {
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, "https://api.sampleapis.com/cartoons/cartoons2D", null,{
                response -> binding.textView.text = parseJson(response.toString()) },
            { binding.textView.text = "That didn't work!" + ": $it"}
        )
        queue.add(jsonObjectRequest)
    }


}






