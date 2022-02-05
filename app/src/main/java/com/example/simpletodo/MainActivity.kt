package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object: TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //remove item from list
                listOfTasks.removeAt(position)
                //notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                showToast("Item was removed")
                saveItems()
            }
        }

//        listOfTasks.add("Do laundry")
//        listOfTasks.add("Go for walk")
//        listOfTasks.add("feed the dog")

        loadItems()

        //when single click on task item and launch 2nd activity
        val onClickListener = object : TaskItemAdapter.onSingleClickListener{
            override fun onItemClicked(position: Int) {
                launchEditItemActivity(position)
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        adapter = TaskItemAdapter(listOfTasks, onLongClickListener, onClickListener)

        // attach the adapter to the recycleview to polulate items
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        val addBtn = findViewById<Button>(R.id.button).setOnClickListener {
            //1. Grab the text
            val userInputtedTask = findViewById<EditText>(R.id.addTaskField)
            listOfTasks.add(userInputtedTask.text.toString())

            //notify the data adapter that the data is updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //reset the text field.
            userInputtedTask.setText("")

            showToast("Item was saved")

            // save item to file
            saveItems()
        }
    }

    //save the data that the user has inputted
    // Save data By writing and reading from a file

    // Create a method to get the file we need
    fun getDataFile(): File {

        // Every line is going to represent a specfic task in our list of task
        return File(filesDir, "data.txt")
    }


    // Load the items by reading every line in the data file
    fun loadItems(){
        try{
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    // Save items by writing them into our data file
    fun saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    fun showToast(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun launchEditItemActivity(position: Int){

        val LaunchEditItem = Intent(this@MainActivity, EditItemActivity::class.java)
            .apply {
                putExtra("item", listOfTasks[position])
                putExtra("pos", position)
            }
        startActivityForResult(LaunchEditItem, 7)
    }

    //get result from edit-item activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 7 && resultCode == RESULT_OK) {
            val TaskName = data?.getExtras()?.getString("text")
            val TaskPos = data?.getExtras()?.getInt("pos", 0)

            if (TaskName != null && TaskPos != null) {
                    updateTask(TaskName, TaskPos)
            }
            showToast(getString(R.string.updateSuccessful))
        } else {
            showToast(getString(R.string.taskNotUpdated))
        }
    }

    fun updateTask(result: String, pos: Int) {
        listOfTasks[pos] = result
        adapter.notifyItemChanged(pos)
        saveItems()
    }
}