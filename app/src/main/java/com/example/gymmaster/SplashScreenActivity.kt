package com.example.gymmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gymmaster.activity.LoginActivity
import android.os.Handler
import com.example.gymmaster.databinding.ActivityMainBinding
import com.example.gymmaster.global.DB
import com.example.gymmaster.manager.SessionManager
import android.util.Log
import com.example.gymmaster.activity.HomeActivity


class SplashScreenActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val splash_delay: Long = 3000 //3 seconds
    var db: DB? = null
    var session: SessionManager? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DB(this)
        session = SessionManager(this)

        insertAdminData()
        mDelayHandler = Handler()
        mDelayHandler?.postDelayed(mRunnable, splash_delay)
    }

    private val mRunnable: Runnable = Runnable {
        if(session?.isLoggedIn == true){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun insertAdminData() {
        try {
            val sqlCheck = "SELECT * FROM ADMIN"
            db?.fireQuery(sqlCheck)?.use {
                if (it.count > 0) {
                    Log.d("SplashActivity", "data available")
                } else {
                    val sqlQuery =
                        "INSERT OR REPLACE INTO ADMIN(ID,USER_NAME,PASSWORD,MOBILE)VALUES('1','ADMIN','123456','0771239871')"
                    db?.executeQuery(sqlQuery)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDelayHandler?.removeCallbacks(mRunnable)
    }

}