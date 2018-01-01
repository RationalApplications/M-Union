package xyz.ratapp.chopino.ui.adapters

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import rm.com.audiowave.AudioWaveView
import xyz.ratapp.munion.R
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.InputStream


class AudiosAdapter(private val mValues: List<String>) :
        RecyclerView.Adapter<AudiosAdapter.AudioViewHolder>() {

    private var audioPlayer: MediaPlayer? = null
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val layout = R.layout.item_audio
        val view = LayoutInflater.from(parent.context).
                inflate(layout, parent, false)
        context = parent.context

        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holderAudio: AudioViewHolder, position: Int) {
        //setup data
        holderAudio.mItem = mValues[position]
        holderAudio.wave.setRawData(loadData(FileInputStream(mValues[position])))

        //setup delegates
        holderAudio.ivPlay.setOnClickListener {
            initPlayer(mValues[position])
            audioPlayer!!.start()
            audioPlayer!!.setOnCompletionListener {
                holderAudio.ivPlay.setImageResource(R.drawable.ic_media_play_dark)
            }
            holderAudio.ivPlay.setImageResource(R.drawable.ic_media_pause_dark)
        }
    }

    private fun loadData(input: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        try {
            var len = input.read(buffer)
            while (len != -1) {
                byteBuffer.write(buffer, 0, len)
                len = input.read(buffer)
            }
        }
        catch (e: Exception) {

        }

        return byteBuffer.toByteArray()
    }

    private fun initPlayer(url: String) {
        if(audioPlayer != null) {
            audioPlayer!!.stop()
        }

        audioPlayer = MediaPlayer.create(context, Uri.parse(url))
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class AudioViewHolder(val mView: View) :
            RecyclerView.ViewHolder(mView) {

        val ivPlay: ImageView = mView.findViewById(R.id.iv_play)
        var wave: AudioWaveView = mView.findViewById(R.id.awv_wave)
        var mItem: String? = null

    }
}
