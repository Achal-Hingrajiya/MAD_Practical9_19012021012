package com.example.practical9_19012021012

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import src.ListBaseAdapter
import src.Notes
import src.Notes.Companion.notesArray
import src.receivers.AlarmBroadcastReceiver
import src.receivers.NotificationBroadcastReceiver
import java.util.*

class NotesActivity : AppCompatActivity() {


    val channelId = "notesNotificationChannel"
    val channelName = "Notes Notification Channel"

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        setContentView(R.layout.activity_notes)

        val bNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val fabAddNote = findViewById<FloatingActionButton>(R.id.fab_add_note)
        val lvNotes = findViewById<ListView>(R.id.lv_notes)

        bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard_page -> {
                    Intent(this, DashboardActivity::class.java).apply {
                        startActivity(this)
                    }
                    return@setOnItemSelectedListener true
                }
                else ->
                    return@setOnItemSelectedListener true
            }
        }

        val lvAdapter = ListBaseAdapter(this, Notes.notesArray)

        lvNotes.adapter =lvAdapter

        fabAddNote.setOnClickListener {
            showAddNoteDialog(lvAdapter)
        }
    }


    private fun createNotificationChannel() {
        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val description = "Notification channel for notes notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = description
            channel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getMillis(hour:Int, min:Int):Long
    {
        val setcalendar = Calendar.getInstance()
        setcalendar[Calendar.HOUR_OF_DAY] = hour
        setcalendar[Calendar.MINUTE] = min
        setcalendar[Calendar.SECOND] = 0
        return setcalendar.timeInMillis
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentDateTime(remindertime : Long): String {

        val df: SimpleDateFormat = SimpleDateFormat("MMM, dd yyyy hh:mm:ss a")
        return df.format(Date(remindertime))
    }



    @RequiresApi(Build.VERSION_CODES.N)
    private fun showAddNoteDialog(lvAdapter: ListBaseAdapter){
        val dialogTitle = "Add Note"
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)

        val customLayout: View = LayoutInflater.from(this).inflate(R.layout.dialog_note, null)

        builder.setView(customLayout)
        builder.setPositiveButton(
                "Save",
                DialogInterface.OnClickListener {
                    dialog, which ->

                    val title = customLayout.findViewById<TextInputEditText>(R.id.tiet_note_title).text.toString()
                    val subtitle = customLayout.findViewById<TextInputEditText>(R.id.tiet_note_subtitle).text.toString()
                    val desc = customLayout.findViewById<TextInputEditText>(R.id.tiet_note_desc).text.toString()
                    val reminderSwitch = customLayout . findViewById <SwitchMaterial>(R.id.reminderSwitch)
                    val timePicker = customLayout.findViewById<TimePicker>(R.id.reminder_time_picker)
                    val isReminder = reminderSwitch.isChecked
                    val hour = timePicker.hour
                    val minute = timePicker.minute
                    val remTimeInMillis = getMillis(hour, minute)
                    val modifiedTime = getCurrentDateTime(remTimeInMillis)

                    val note = Notes(
                            title= title,
                            subTitle= subtitle,
                            Description = desc,
                            isReminder = isReminder,
                            modifiedTime =modifiedTime,
                    )


                    Notes.addNote(note)
                    lvAdapter.notifyDataSetChanged()
                    setAlarm(this, note, remTimeInMillis)

                })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }



    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setAlarm(context: Context, notes: Notes, timeInMillis :Long)
    {
        val index = notesArray.indexOf(notes)
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        intent.putExtra("index", index)
        val pendingIntent = PendingIntent.getBroadcast(
                context,
                index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        if (notes.isReminder) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
            )
//            Toast.makeText(this,"Reminder set $hour:$minute", Toast.LENGTH_SHORT).show()
        }
        else
            alarmManager.cancel(pendingIntent)
    }

}