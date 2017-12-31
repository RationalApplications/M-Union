package xyz.ratapp.munionagent.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import xyz.ratapp.munionagent.R
import xyz.ratapp.munionagent.ui.activities.auth.AuthActivity
import xyz.ratapp.munionagent.ui.activities.main.MainActivity

/**
 * Created by timtim on 28/12/2017.
 */

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setTestData()

        auth_submit.setOnClickListener {
            auth()
        }
    }

    private fun setTestData() {
        edit_email.setText("commercial@ratapp.xyz")
        edit_pass.setText("pK5zC9")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK &&
                requestCode == AuthActivity.REQUEST_AUTH_CODE) {
            finish()
            startChatListActivity()
        }
    }

    private fun startChatListActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun auth() {
        val email = edit_email.text.toString()
        val password = edit_pass.text.toString()

        if(dataIsValid(email, password)) {
            val intent = AuthActivity.getAuthIntent(email, password)
            startActivityForResult(intent, AuthActivity.REQUEST_AUTH_CODE)
        }
        else {
            Toast.makeText(this,
                    R.string.login_fill_correct_data,
                    Toast.LENGTH_LONG).show()
        }
    }

    private fun dataIsValid(email: String, password: String): Boolean {
        return password.isNotBlank() &&
                email.contains('@') && email.contains('.')
    }

}