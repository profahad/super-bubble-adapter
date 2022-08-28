package apps.fahad.libs.super_bubble_adapter.helpers

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apps.fahad.libs.super_bubble_adapter.SuperBubbleAdapter
import apps.fahad.libs.super_bubble_adapter.interfaces.OptionIcon
import apps.fahad.libs.super_bubble_adapter.interfaces.OptionTitle

class SuperBubbleInitializer<T> where  T : OptionTitle, T : OptionIcon {
    private var recyclerView: RecyclerView? = null
    private var arrayList: ArrayList<T>? = null
    private var deletionCallback: ((Int, T) -> Unit)? = null
    private var editableMode: Boolean = false

    fun setRecyclerView(recyclerView: RecyclerView) = apply {
        this.recyclerView = recyclerView
    }

    fun setData(arrayList: ArrayList<T>) = apply {
        this.arrayList = arrayList
    }

    fun setDeletionCallback(deletionCallback: ((Int, T) -> Unit)) = apply {
        this.deletionCallback = deletionCallback
    }

    fun setEnabledEditMode(enabled: Boolean) = apply {
        this.editableMode = enabled
    }

    private fun initBubblesAdapter() {
        arrayList?.let { arrayList ->
            val superAdapter = SuperBubbleAdapter(arrayList, editableMode = editableMode)
            superAdapter.setOnItemDeleteListener { i, tag ->
                deletionCallback?.invoke(i, tag)
                arrayList.apply { removeAt(i) }.let {
                    initBubblesAdapter()
                }
            }
            recyclerView?.apply {
                adapter = superAdapter
                layoutManager = LinearLayoutManager(recyclerView?.context)
            }
        }
    }

    fun initAdapter() {
        arrayList?.let { arrayList ->
            val superAdapter = SuperBubbleAdapter(arrayList, editableMode = editableMode)
            superAdapter.setOnItemDeleteListener { i, tag ->
                deletionCallback?.invoke(i, tag)
            }
            recyclerView?.apply {
                adapter = superAdapter
                layoutManager = LinearLayoutManager(recyclerView?.context)
            }
        }
    }

    fun initialize() = apply {
        initBubblesAdapter()
    }
}