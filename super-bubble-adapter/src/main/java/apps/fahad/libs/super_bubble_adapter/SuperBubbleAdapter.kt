package apps.fahad.libs.super_bubble_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import apps.fahad.libs.super_bubble_adapter.databinding.LayoutRvBubbleItemBinding
import apps.fahad.libs.super_bubble_adapter.helpers.MeasureHelper
import apps.fahad.libs.super_bubble_adapter.interfaces.OptionIcon
import apps.fahad.libs.super_bubble_adapter.interfaces.OptionTitle
import apps.fahad.libs.super_bubble_adapter.layoutmanager.MultipleSpanGridLayoutManager
import com.bumptech.glide.Glide
import kotlin.properties.Delegates

class SuperBubbleAdapter<T>(
    private val list: ArrayList<T> = arrayListOf(),
    private var editableMode: Boolean = false
) : RecyclerView.Adapter<SuperBubbleAdapter<T>.BubbleView>()
        where T : OptionTitle, T : OptionIcon {

    private var onItemDeleteListener: ((Int, T) -> Unit)? = null

    fun setOnItemDeleteListener(onItemDeleteListener: ((Int, T) -> Unit)?) {
        this.onItemDeleteListener = onItemDeleteListener
    }

    fun clearAddData(arrayList: ArrayList<T>) {
        this.list.clear()
        this.list.addAll(arrayList)
        measureHelper = MeasureHelper(this, list.size)
        notifyDataSetChanged()
    }

    /***
     * Measuring Helper which is used to measure each row of the recyclerView
     */
    private var measureHelper = MeasureHelper(this, list.size)

    /***
     * Attached RecyclerView to the Adapter
     */
    private var recyclerView: RecyclerView? = null

    /***
     * First step is to get the width of the recyclerView then
     * Proceed to measuring the holders.
     *
     * Is RecyclerView done measuring.
     */
    private var ready = false

    /***
     * Determines when the measuring of all the cells is done.
     * If the newVal is true the adapter should be updated.
     */
    var measuringDone by Delegates.observable(false) { _, _, newVal ->
        if (newVal)
            update()
    }

    /***
     * Called to update the adapter with the new LayoutManager.
     */
    private fun update() {

        recyclerView ?: return

        recyclerView?.apply {

            visibility = View.VISIBLE

            // Get the list of sorted items from measureHelper
            list.clear()
            list.addAll(measureHelper.getItems())

            // Get the list of sorted spans from measureHelper
            layoutManager = MultipleSpanGridLayoutManager(
                context,
                MeasureHelper.SPAN_COUNT,
                measureHelper.getSpans()
            )
        }
    }

    /***
     * When recyclerView is attached measure the width and calculate the baseCell on measureHelper.
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView.apply {

            visibility = View.INVISIBLE

            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    // Prevent the multiple calls
                    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // Measure the BaseCell on MeasureHelper.
                    measureHelper.measureBaseCell(recyclerView.width)

                    ready = true

                    // NotifyDataSet because itemCount value needs to update.
                    notifyDataSetChanged()
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BubbleView(
            LayoutRvBubbleItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount() = if (ready) list.size else 0

    override fun onBindViewHolder(holder: BubbleView, position: Int) {

        val tag = list[position]

        // Determine if the MeasureHelper is done measuring if not holder should be measured.
        val shouldMeasure = measureHelper.shouldMeasure()

        holder.setData(position, tag, shouldMeasure)

        if (shouldMeasure)
            measureHelper.measure(holder, tag)
    }

    inner class BubbleView(val itemBinding: LayoutRvBubbleItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun setData(position: Int, tag: T, shouldMeasure: Boolean) {

            itemBinding.textViewBubbleTitle.apply {
                text = tag.optionTitle
            }

            if (tag.optionIcon.isNotEmpty()) {
                itemBinding.imageView.visibility = View.VISIBLE
                Glide.with(itemBinding.imageView.context)
                    .load(tag.optionIcon)
                    .into(itemBinding.imageView)
            } else {
                itemBinding.imageView.visibility = View.GONE
                Glide.with(itemBinding.imageView.context)
                    .clear(itemBinding.imageView)
            }

            if (editableMode) {
                itemBinding.imageViewCross.visibility = View.VISIBLE
            } else {
                itemBinding.imageViewCross.visibility = View.GONE
            }

            itemBinding.imageViewCross.setOnClickListener {
                onItemDeleteListener?.invoke(
                    position,
                    tag
                )
            }

            itemBinding.layoutParent.apply {
                /* set the height to normal, because in the measureHelper in order to fit
                  as much holders as possible we shrink the view to height of 1 */
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            }

            /* if the measuring is done set the width to fill the whole cell to avoid unwanted
                empty spaces between the cells */
            if (!shouldMeasure)
                itemBinding.layoutParent.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        }
    }
}