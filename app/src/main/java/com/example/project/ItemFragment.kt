package com.example.project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONObject

private const val API_KEY = "7709E4D011EF4661BF0E5C4FAF5340E7"

/*
 * The class for the only fragment in the app, which contains the progress bar,
 * recyclerView, and performs the network calls to the NY Times API.
 */
class ItemFragmentFragment : Fragment(), OnListFragmentInteractionListener {

    /*
     * Constructing the view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        val context = view.context

        updateAdapter(progressBar,recyclerView)
        return view
    }

    /*
     * Updates the RecyclerView adapter with new data.  This is where the
     * networking magic happens!
     */
    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()
        // Create and set up an AsyncHTTPClient() here
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api-key"] = API_KEY

        // Using the client, perform the HTTP request
        client[
            "https://api.redcircleapi.com/request",
            params,
            object : JsonHttpResponseHandler()
            { //connect these callbacks to your API call

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    // The wait for a response is over
                    progressBar.hide()

                    //TODO - Parse JSON into Models
                    val resultsJSON : JSONObject = json.jsonObject.get("results") as JSONObject
                    val itemsRawJSON : String = resultsJSON.get("items").toString()
                    val gson = Gson()
                    val arrayItemType = object : TypeToken<List<Item>>() {}.type
                    val models : List<Item>? = gson.fromJson(itemsRawJSON, arrayItemType)
                    recyclerView.adapter =
                        ItemAdapter(models!!, this@ItemFragmentFragment)


                    // Look for this in Logcat:
                    Log.d("ItemFragment", "response successful")
                }

                /*
                 * The onFailure function gets called when
                 * HTTP response status is "4XX" (eg. 401, 403, 404)
                 */
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    // The wait for a response is over
                    progressBar.hide()

                    // If the error is not null, log it!
                    t?.message?.let {
                        Log.e("ItemFragemnt", errorResponse)
                    }
                }


            }]

    }

    /*
     * What happens when a particular book is clicked.p
     */
    override fun onItemClick(item: Item) {
        Toast.makeText(context, "test: " + item.name, Toast.LENGTH_LONG).show()
    }

}