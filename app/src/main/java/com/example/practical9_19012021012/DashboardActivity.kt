package com.example.practical9_19012021012

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import src.LoginInfo

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        var logoutBtn = findViewById<Button>(R.id.tbtn_logout)


        val name = LoginInfo.full_name
        val email = LoginInfo.email
        val phone = LoginInfo.phone_number
        val city = LoginInfo.city


        val tvName = findViewById<TextView>(R.id.dash_name)
        val tvEmail = findViewById<TextView>(R.id.dash_email)
        val tvFullName = findViewById<TextView>(R.id.tv_full_name)
        val tvEmailInfo = findViewById<TextView>(R.id.tv_email)
        val tvPhone = findViewById<TextView>(R.id.tv_phn_no)
        val tvCity = findViewById<TextView>(R.id.tv_city)

        tvName.text = name
        tvFullName.text = name
        tvEmail.text = email
        tvEmailInfo.text = email
        tvCity.text = city
        tvPhone.text = phone




        logoutBtn.setOnClickListener {

            Toast.makeText(this, "Logged Out!", Toast.LENGTH_SHORT).show()
            LoginInfo.logout()
            Intent(this, LoginActivity :: class.java).apply {
                startActivity(this)
            }
        }
    }


}