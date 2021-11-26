package src.receivers

import AlarmServices
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.example.practical9_19012021012.R

class AlarmBroadcastReceiver : BroadcastReceiver() {
    var mp: MediaPlayer? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            mp = MediaPlayer.create(context, R.raw.alarm);
            mp?.start()
        }
    }
}


//
//class AlarmBroadcastReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
////        var note:Notes? = null
//        if (intent != null && context != null) {
//            val str1 = intent.getStringExtra("Service1")
//            if (str1 == null) {
//            } else if (str1 == "Start" || str1 == "Stop") {
//                val intentService = Intent(context, AlarmServices::class.java)
//                intentService.putExtra("Service1", intent!!.getStringExtra("Service1"))
//
//                if (str1 == "Start")
//                    context.startService(intentService)
//                else if (str1 == "Stop")
//                    context.stopService(intentService)
//            }
//        }
//    }
//}