package apps.fahad.libs.super_bubble_adapter.helpers

import android.view.ViewGroup
import android.view.ViewTreeObserver
import apps.fahad.libs.super_bubble_adapter.SuperBubbleAdapter
import apps.fahad.libs.super_bubble_adapter.interfaces.OptionIcon
import apps.fahad.libs.super_bubble_adapter.interfaces.OptionTitle

class MeasureHelper<T>(
    private val adapter: SuperBubbleAdapter<T>,
    private val count: Int
) where T : OptionTitle, T : OptionIcon {

    companion object {

        /***
         * Number of the total spans that the layout manager is allowed to draw.
         */
        const val SPAN_COUNT = 52
    }

    /***
     * Holds the count of measured views to know when measuring is finished.
     */
    private var measuredCount = 0

    /***
     * TagRowManager is responsible for adding and getting of the tags.
     */
    private val rowManager = BubbleRowManager<T>()

    /***
     * baseCell is the value used for determining how much span will a holder require.
     */
    private var baseCell: Float = 0f

    fun measureBaseCell(width: Int) {
        baseCell = (width / SPAN_COUNT).toFloat()
    }

    /***
     * If measuring is finished or not.
     */
    fun shouldMeasure() = measuredCount != count

    fun getItems() = rowManager.getSortedTags()

    fun getSpans() = rowManager.getSortedSpans()

    /***
     * Calling this method every time a cell is measured and
     * if there is no more cell to measure it will notify the adapter
     * to update it self.
     */
    private fun cellMeasured() {

        if (!adapter.measuringDone && !shouldMeasure())
            adapter.measuringDone = true
    }

    fun measure(holder: SuperBubbleAdapter<T>.BubbleView, tag: T) {

        /* Get the ItemView and minimize it's height as small as possible
            to fit as many cells as it's possible in the screen. */
        val itemView = holder.itemBinding.root.apply {
            layoutParams.height = 1
        }

        val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                // Remove the observer to avoid multiple callbacks.
                itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Include also the horizontal margin of the layout.
                val marginTotal =
                    (holder.itemBinding.layoutParent.layoutParams as ViewGroup.MarginLayoutParams).marginStart * 2

                // Required span for the holder in Float/
                val span = (holder.itemBinding.layoutParent.width + marginTotal) / baseCell

                // Increase measured count.
                measuredCount++

                rowManager.add(span, tag)

                cellMeasured()
            }
        }

        // Observe for the view
        itemView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }
}