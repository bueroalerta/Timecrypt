package co.timecrypt.android.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import co.timecrypt.android.R
import kotlinx.android.synthetic.main.activity_link_display.*


class LinkDisplayActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    private val TAG = LinkDisplayActivity::class.simpleName!!

    companion object {
        val KEY_URL = "MESSAGE_URL"
        val KEY_DATE = "MESSAGE_DESTRUCT_DATE"
        val KEY_VIEWS = "MESSAGE_ALLOWED_VIEWS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // check preconditions
        val extras = intent.extras
        if (extras == null) {
            Log.e(TAG, "No bundle provided")
            finish()
            return
        }
        val url = extras.getString(KEY_URL, null)
        val date = extras.getString(KEY_DATE, null)
        val views = extras.getInt(KEY_VIEWS, -1)
        if (url == null || date == null || views < 1) {
            Log.e(TAG, "Missing bundle date")
            finish()
            return
        }

        // all is fine, load the data now
        setContentView(R.layout.activity_link_display)
        messageUrlDisplay.text = url
        messageUrlInfo.text = prepareMessageInfo(date, views)

        listOf(messageUrlDisplay, messageUrlCopy, messageUrlShare, buttonNewMessage).forEach {
            it.setOnClickListener(this@LinkDisplayActivity)
            it.setOnLongClickListener(this@LinkDisplayActivity)
        }
    }

    private fun prepareMessageInfo(date: String, views: Int): String {
        val replacementText = resources.getQuantityString(R.plurals.plural_how_many_times, views, views)
        return getString(R.string.link_more_info, date, replacementText)
    }

    override fun onClick(view: View) {
        when (view.id) {
            messageUrlCopy.id -> {
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(getString(R.string.link_copy_label), messageUrlDisplay.text)
                clipboard.primaryClip = clip
                Toast.makeText(this, R.string.link_copy_success, Toast.LENGTH_LONG).show()
            }
            messageUrlShare.id -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.link_share_content_title))
                i.putExtra(Intent.EXTRA_TEXT, messageUrlDisplay.text)
                startActivity(Intent.createChooser(i, getString(R.string.link_share_chooser_title)))
            }
            buttonNewMessage.id -> {
                startActivity(Intent(this, CreateMessageActivity::class.java))
                finish()
            }
            messageUrlDisplay.id -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(messageUrlDisplay.text.toString())))
                finish()
            }
        }
    }

    override fun onLongClick(view: View): Boolean {
        // show hints only for copy and share actions
        when (view.id) {
            messageUrlCopy.id, messageUrlShare.id -> {
                Toast.makeText(this@LinkDisplayActivity, view.contentDescription, Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return false
        }
    }

}