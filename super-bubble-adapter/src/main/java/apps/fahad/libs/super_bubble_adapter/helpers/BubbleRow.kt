package apps.fahad.libs.super_bubble_adapter.helpers

import apps.fahad.libs.super_bubble_adapter.interfaces.OptionIcon
import apps.fahad.libs.super_bubble_adapter.interfaces.OptionTitle
import kotlin.math.ceil

class BubbleRow<T> where T : OptionTitle, T : OptionIcon {

    /***
     * The available spans that the current row has.
     */
    var freeSpans = MeasureHelper.SPAN_COUNT

    /***
     * List of the holders hosted in the current row.
     */
    val tagList = mutableListOf<T>()

    /***
     * List of the spans required by each holder hosted in the current row.
     */
    val spanList = mutableListOf<Int>()

    fun addTag(spanRequired: Float, tag: T): Boolean {

        // if the current row has enough available span
        if (spanRequired < freeSpans)
            if (tagList.add(tag)) {

                // Round the required span to Int
                val spanRequiredInt = ceil(spanRequired).toInt()

                // Add the required span to spanList
                spanList.add(spanRequiredInt)

                // Minus the required span from the available span.
                freeSpans -= spanRequiredInt

                return true
            }

        return false
    }
}