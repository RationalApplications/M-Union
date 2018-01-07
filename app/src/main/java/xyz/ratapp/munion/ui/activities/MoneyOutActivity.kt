package xyz.ratapp.munion.ui.activities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_money_out.*
import xyz.ratapp.munion.R
import xyz.ratapp.munion.controllers.interfaces.DataCallback
import xyz.ratapp.munion.data.DataController
import xyz.ratapp.munion.data.pojo.Lead
import xyz.ratapp.munion.helpers.email.Sender
import java.lang.Exception


class MoneyOutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money_out)
        setupViews()
    }

    private fun setupViews() {
        setupToolbar()

        card_form.cardRequired(true).setup(this)
        card_form.setOnCardFormSubmitListener {
            et_money.requestFocus()
        }
        DataController.getInstance(this).getUser(object: DataCallback<Lead> {
            override fun onSuccess(data: Lead) {
                val moneyAmount = getString(R.string.balance, data.money)
                tv_money.text = moneyAmount
            }

            override fun onFailed(thr: Throwable?) {
                setResult(2)
            }
        })
        btn_sent_money.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val cardNumber = card_form.cardNumber
        val money = et_money.text.toString()

        val instance = DataController.getInstance(this@MoneyOutActivity)

        if (card_form.isValid && money.isNotBlank()) {
            instance.getUser(object : DataCallback<Lead> {
                override fun onSuccess(data: Lead) {
                    val userMoney = data.money.toFloat()
                    val activityMoney = money.toFloat()

                    if(userMoney < activityMoney) {
                        Toast.makeText(this@MoneyOutActivity,
                                getString(R.string.have_no_money),
                                Toast.LENGTH_LONG).show()
                        setResult(2)
                    }
                    else {
                        val sender = Sender.getInstance()
                        try {
                            sender.sendMoneyMessage(this@MoneyOutActivity,
                                    data.name + " " + data.lastName, cardNumber, money)

                            Toast.makeText(this@MoneyOutActivity,
                                    getString(R.string.succes_money_request),
                                    Toast.LENGTH_LONG).show()
                            setResult(Activity.RESULT_OK)
                        } catch (e: Exception) {
                            Toast.makeText(this@MoneyOutActivity,
                                    getString(R.string.error_when_load_data),
                                    Toast.LENGTH_LONG).show()
                            setResult(2)
                        }
                    }
                }

                override fun onFailed(thr: Throwable?) {
                    Toast.makeText(this@MoneyOutActivity,
                            getString(R.string.error_when_load_data),
                            Toast.LENGTH_LONG).show()
                    setResult(2)
                }
            })
        } else {
            Toast.makeText(this@MoneyOutActivity,
                    getString(R.string.fill_fields),
                    Toast.LENGTH_LONG).show()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_money)
        supportActionBar?.title = getString(R.string.title_money)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
