package xyz.ratapp.munion.ui.activities

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import kotlinx.android.synthetic.main.activity_cabinet.*
import xyz.ratapp.munion.R
import xyz.ratapp.munion.controllers.interfaces.DataCallback
import xyz.ratapp.munion.data.DataController
import xyz.ratapp.munion.data.pojo.Lead
import xyz.ratapp.munion.helpers.ChatSDKHelper
import java.text.SimpleDateFormat
import java.util.*


class CabinetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cabinet)

        setupUI()
        setupData()
        setupDelegates()
    }

    private fun setupDelegates() {
        DataController.getInstance(this).
                refreshUser(object: DataCallback<Lead> {
                    override fun onSuccess(user: Lead) {
                        btn_copy_link.setOnClickListener {
                            val formMask = getString(R.string.form_mask, user.id)
                            val message = getString(R.string.friendMessageMask, formMask)

                            val clipboard = this@CabinetActivity.
                                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("message", message)
                            clipboard.primaryClip = clip

                            Toast.makeText(this@CabinetActivity,
                                    "Сообщение скопировано, \nотправьте его своему другу :)",
                                    Toast.LENGTH_LONG).show()
                        }

                        btn_address.setOnClickListener {
                            val address = "0,0?q=Санкт-Петербург,${user.title}"
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("geo:" + address)

                            try {
                                this@CabinetActivity.startActivity(intent)
                            } catch (e: SecurityException) {
                                Log.e("ContactsTag", "Ошибка открытия карты")
                            }
                        }

                        if(user.money.toFloat() <= 0) {
                            btn_out_money.setBackgroundResource(R.drawable.radius_button_disabled)
                            btn_out_money.isEnabled = false
                        }

                        btn_out_money.setOnClickListener {
                            //go to money out activity
                            if(user.money.toFloat() > 0) {
                                val intent = Intent(this@CabinetActivity,
                                        MoneyOutActivity::class.java)
                                startActivityForResult(intent, 10)
                            }
                            else {
                                Toast.makeText(this@CabinetActivity,
                                        getString(R.string.you_have_no_money),
                                        Toast.LENGTH_LONG).show()
                            }
                        }

                        iv_user_photo.setOnClickListener {
                            //load photo
                            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                            startActivityForResult(intent, 100)
                        }
                    }

                    override fun onFailed(thr: Throwable?) {

                    }
                })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK &&
                requestCode == 100 && data != null &&
                data.data != null) {
            val uri = data.data
            setupImage(uri)
        }
        else if (requestCode == 10) {
            if(resultCode == Activity.RESULT_OK &&
                    data != null &&
                    data.data != null) {
                //change money value
            }
            else {
                Toast.makeText(this@CabinetActivity,
                        getString(R.string.cant_send_money),
                        Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupImage(uri: Uri) {
        setImage(uri.toString())
        val dataController = DataController.getInstance(this)
        dataController.setUserPhotoUri(uri)
        dataController.getUser(object: DataCallback<Lead> {
            override fun onSuccess(user: Lead) {
                ChatSDKHelper.initChatUser(user)
            }

            override fun onFailed(thr: Throwable?) {

            }
        })
    }

    private fun setImage(uri: String) {
        Glide.with(this).
                load(uri).
                asBitmap().centerCrop().
                error(R.drawable.icon_me).
                into(object : BitmapImageViewTarget(iv_user_photo) {
                    override fun setResource(resource: Bitmap) {
                        val circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(resources, resource)
                        circularBitmapDrawable.isCircular = true
                        iv_user_photo.setBackgroundDrawable(circularBitmapDrawable)
                        iv_user_photo.setImageResource(R.drawable.mask_photo)
                    }
                })
    }

    private fun setupData() {
        DataController.getInstance(this).
                getUser(object: DataCallback<Lead> {
                    override fun onSuccess(user: Lead) {
                        tv_name.text = "${user.name} ${user.lastName}"
                        btn_address.text = user.title
                        btn_user_id.text = "ID: " + user.id.toString()
                        btn_invited_count.text = getString(R.string.invited_count,
                                user.countOfInvitedFriends.toString())
                        btn_balance.text = getString(R.string.balance, user.money)
                        val dateString = user.dateCreate.substring(0, 10)
                        var sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val date = sdf.parse(dateString)
                        sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        tv_date_of_publish.text = "Работаем вместе с: ${sdf.format(date)}"
                        setImage(
                                if(user.photoUri != null)
                                    user.photoUri
                                else
                                    "android.resource://xyz.ratapp.munion/drawable/ic_account_large")
                    }

                    override fun onFailed(thr: Throwable?) {

                    }
                })
    }

    private fun setupUI() {
        setSupportActionBar(toolbar_cabinet)
        supportActionBar?.title = getString(R.string.title_personal_cabinet)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
