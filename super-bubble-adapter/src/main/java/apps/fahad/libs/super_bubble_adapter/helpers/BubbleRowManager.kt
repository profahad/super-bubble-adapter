package apps.fahad.libs.super_bubble_adapter.helpers

import apps.fahad.libs.super_bubble_adapter.interfaces.OptionIcon
import apps.fahad.libs.super_bubble_adapter.interfaces.OptionTitle

class BubbleRowManager<T> where T : OptionTitle, T : OptionIcon {

    /***
     * List of all the rows inside the row manager.
     */
    private val rowList = mutableListOf<BubbleRow<T>>()
        .apply {

            // Manually add the first empty row.
            add(BubbleRow())
        }

    /***
     * Loop through the rowList to fit the required span
     */
    fun add(spanRequired: Float, tag: T) {

        for (i in 0..rowList.size) {

            val tagRow = rowList[i]

            // If the title was added and was fit to the list in dreamTitleRow
            if (tagRow.addTag(spanRequired, tag))
                break

            // If the model did not fit in any of current cells
            if (i == rowList.lastIndex)
                rowList.add(BubbleRow())
        }
    }

    fun getSortedSpans() =
        mutableListOf<Int>().apply {
            rowList.forEach {
                addAll(it.spanList)
            }
        }

    fun getSortedTags() =
        mutableListOf<T>().apply {
            rowList.forEach {
                addAll(it.tagList)
            }
        }
}