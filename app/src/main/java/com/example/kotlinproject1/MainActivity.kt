package com.example.kotlinproject1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread(Runnable {
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url("https://api.github.com/users/square/repos")
                    .build()
            val response = client.newCall(request).execute()
            val responseText = response.body()!!.string()
            val repos = Gson().fromJson(responseText, GitHubRepositoryInfo.List::class.java)
            android.util.Log.d("Repos", repos.joinToString { it.name })
        }).start()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(generateFakeValues())
    }

    private fun generateFakeValues(): List<String> {
        val values = mutableListOf<String>()
        for (i in 0..100) {
            values.add("$i element")
        }
        return values
    }

    class Adapter(private val values: List<String>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun getItemCount() = values.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_view, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder?.textView?.text = values[position]
        }

        class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var textView: TextView? = null

            init {
                textView = itemView?.findViewById(R.id.text_list_item)
            }
        }
    }

    data class GitHubRepositoryInfo(val name: String) {

        class List : ArrayList<GitHubRepositoryInfo>()
    }

}

