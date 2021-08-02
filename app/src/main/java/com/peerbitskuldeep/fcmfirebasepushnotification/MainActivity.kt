package com.peerbitskuldeep.fcmfirebasepushnotification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        button.setOnClickListener {

            val title = edtTitle.text.toString()
            val message = edtMessage.text.toString()

            if (title.isNotEmpty() && message.isNotEmpty())
            {
                PushNotification(
                    NotificationData(title, message),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            }
        }

    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {

            try {

                val response = RetrofitInstance.api.postNotification(notification)

                if (response.isSuccessful) {
                    d("TAG", "RESPONSE: ${Gson().toJson(response)}")
                } else {
                    d("TAG", "ERROR! ${response.errorBody().toString()}")
                }

            } catch (e: Exception) {
                d("TAG", e.toString())
            }

        }


}