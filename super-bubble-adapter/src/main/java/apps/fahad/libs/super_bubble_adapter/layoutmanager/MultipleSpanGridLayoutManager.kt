package apps.fahad.libs.super_bubble_adapter.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class MultipleSpanGridLayoutManager(context: Context, spanCount: Int, spanList: MutableList<Int>) :
    GridLayoutManager(context, spanCount) {

    init {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int) = spanList[position]
        }
    }
}