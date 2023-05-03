package com.acme.sk8

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.acme.sk8.databinding.FragmentItemDetailBinding
import com.acme.sk8.placeholder.PlaceholderContent
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ItemDetailFragment : Fragment(), MConnect.sensorListener {

    private val TAG = javaClass.simpleName

    private fun log(s: String) {
        Log.i(TAG, s)
    }
    /**
     * The placeholder content this fragment is presenting.
     */
    private var item: PlaceholderContent.PlaceholderItem? = null

    lateinit var itemDetailTextView: TextView
    private var fab: FloatingActionButton? = null
    private var webView: WebView? = null
    private var toolbarLayout: CollapsingToolbarLayout? = null
    private lateinit var sensor :MConnect

    private var _binding: FragmentItemDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
            item = PlaceholderContent.ITEM_MAP[dragData]
            updateContent()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the placeholder content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = PlaceholderContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]


            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        toolbarLayout = binding.toolbarLayout
        itemDetailTextView = binding.itemDetail
        webView = binding.web
        fab = binding.fab
        updateContent()
        rootView.setOnDragListener(dragListener)

        return rootView
    }


    public override fun updateData(dat: String){

        itemDetailTextView.text = dat
    }
    private fun updateContent() {
        toolbarLayout?.title = item?.content

        // Show the placeholder content as text in a TextView.
        item?.let {
            itemDetailTextView.text = it.details
        }
        webView?.getSettings()?.setJavaScriptEnabled(true);
//        webView?.loadUrl("10.0.0.72:4242")
        //next
        sensor = MConnect()
        sensor.mlistener = this
        fab?.setOnClickListener(View.OnClickListener {
            log("Fab 1 Clicked: next")

//            webView?.loadUrl("10.0.0.72:4242/android")

            sensor.Message = "Update Sensor."
            sensor.requestSensorData()
        })

        fab?.setOnLongClickListener(OnLongClickListener {
            log( "Fab 1 Long Clicked: next")
            val calendar: Calendar = Calendar.getInstance()
            val now: Date = calendar.getTime()
             log(now.toString())
//            val current: TimeZone = calendar.getTimeZone()
//            webView?.loadUrl("10.0.0.72:4242/Time:1234567")

            sensor.Message = now.toString()
            sensor.requestSensorData()

            true
        })
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}