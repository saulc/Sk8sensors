package com.acme.sk8

import android.content.ClipData
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContentResolverCompat
import androidx.fragment.app.Fragment
import com.acme.sk8.databinding.FragmentItemDetailBinding
import com.acme.sk8.placeholder.PlaceholderContent
import com.acme.sk8.stl.Model
import com.acme.sk8.stl.ModelSurfaceView
import com.acme.sk8.stl.StlModel
import com.acme.sk8.stl.util.Util
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayInputStream
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ItemDetailFragment : Fragment(), Mudp.sensorListener {

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
//    private var webView: WebView? = null
    private var toolbarLayout: CollapsingToolbarLayout? = null
    private lateinit var sensor :MConnect
    private lateinit var sconn :Mudp

    private var _binding: FragmentItemDetailBinding? = null

    private lateinit var sampleModels: List<String>
    private var sampleModelIndex = 0
    private  var modelView: ModelSurfaceView? = null
    private val disposables = CompositeDisposable()

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

        sampleModels = activity?.assets?.list("")!!.filter { it.endsWith(".stl") }
//        modelView = binding.surfaceView
//        modelView = ModelSurfaceView(this, model)

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
//        webView = binding.web
        fab = binding.fab

        loadSampleModel()
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
//        webView?.getSettings()?.setJavaScriptEnabled(true);
//        webView?.loadUrl("10.0.0.72:4242")
        //next
//        sensor = MConnect()
//        sensor.mlistener = this
        sconn = Mudp()
        sconn.setListener(this)
        fab?.setOnClickListener(View.OnClickListener {
            log("Fab 1 Clicked: next")

//            webView?.loadUrl("10.0.0.72:4242/android")
            val calendar: Calendar = Calendar.getInstance()
            val now: Date = calendar.getTime()
            sconn.Message = "\n" +
                    now.toString() + " :12345 12345 12345 12345 12345 12345 12345"+
                    now.toString() + " :12345 12345 12345 12345 12345 12345 12345"+
                    now.toString() + " :12345 12345 12345 12345 12345 12345 12345"+
                    now.toString() + " :12345 12345 12345 12345 12345 12345 12345"+
                    now.toString() + " :12345 12345 12345 12345 12345 12345 12345"+
                    now.toString() + " :12345 12345 12345 12345 12345 12345 12345"+
                    now.toString() + " :12345 12345 12345 12345 12345 12345 12345"+
                    now.toString() + " :12345 12345 12345 12345 12345 12345 12345"+
                    now.toString() + " :12345 12345 12345 12345 12345 12345 12345"+
                    now.toString() + " :12345 12345 12345 12345 12345 12345 12345"
            sconn.requestSensorData()
        })

        fab?.setOnLongClickListener(OnLongClickListener {
            log( "Fab 1 Long Clicked: next")
            val calendar: Calendar = Calendar.getInstance()
            val now: Date = calendar.getTime()
             log(now.toString())
//            val current: TimeZone = calendar.getTimeZone()
//            webView?.loadUrl("10.0.0.72:4242/Time:1234567")

            sconn.Message = now.toString()
            sconn.closeSocket()

            true
        })
    }

    private fun createNewModelView(model: Model?) {
        if (modelView != null) {
            binding.containerView?.removeView(modelView)
        }
        modelView = activity?.applicationContext?.let { ModelSurfaceView(it, model) }
        binding.containerView?.addView(modelView, 0)
    }

    private fun beginLoadModel(uri: Uri) {

            var model: Model? = null
            var stream: InputStream? = null
            try {
                val cr = activity?.applicationContext?.contentResolver
                val fileName = cr?.let { getFileName(it, uri) }

                if (stream != null) {
                    if (!fileName.isNullOrEmpty()) {
                        // assume it's STL.
                        model = StlModel(stream)
                    }
                }
            } finally {
                Util.closeSilently(stream)
            }

    }


    private fun getFileName(cr: ContentResolver, uri: Uri): String? {
        if ("content" == uri.scheme) {
            val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
            ContentResolverCompat.query(cr, uri, projection, null, null, null, null)?.use { metaCursor ->
                if (metaCursor.moveToFirst()) {
                    return metaCursor.getString(0)
                }
            }
        }
        return uri.lastPathSegment
    }
    private fun setCurrentModel(model: Model) {
        createNewModelView(model)
        ModelViewerApplication.currentModel = model
        model!!
        Toast.makeText(activity?.applicationContext, "Model opened!", Toast.LENGTH_SHORT).show()
//        title = model.title
//        binding.progressBar.visibility = View.GONE
    }
    private fun loadSampleModel() {
        try {

            val stream = activity?.assets!!.open(sampleModels[0])
//            val stream = assets.
            setCurrentModel(StlModel(stream))
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
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