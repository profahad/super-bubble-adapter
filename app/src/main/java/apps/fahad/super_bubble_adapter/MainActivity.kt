package apps.fahad.super_bubble_adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.fahad.libs.super_bubble_adapter.SuperBubbleAdapter
import apps.fahad.libs.super_bubble_adapter.helpers.SuperBubbleInitializer
import apps.fahad.super_bubble_adapter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBubblesAdapter(DataProvider.tagList)
    }

    private fun initBubblesAdapter(array: ArrayList<Tag>) {
        SuperBubbleInitializer<Tag>()
            .setRecyclerView(binding.recyclerView)
            .setData(array)
            .setEnabledEditMode(true)
            .setDeletionCallback { i, tag ->
                print(i)
            }
            .initAdapter()

    }
}