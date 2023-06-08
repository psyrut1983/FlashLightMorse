package com.example.flashlight

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {

    private lateinit var mySwitch: Switch
    private lateinit var mCameraManager: CameraManager
    private lateinit var mCameraId: String
    private lateinit var vibrator: Vibrator
    private lateinit var editText: EditText
    private lateinit var btnMorse: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mySwitch=findViewById(R.id.my_switch)
        editText=findViewById(R.id.edittext)
        btnMorse=findViewById(R.id.btn_morse)

        var text = editText.text.toString()



        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            mCameraId = mCameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        mySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                turnOnFlashLight(mCameraManager, mCameraId)
                vibratePhone(vibrator)
            }
            else{
                turnOfFlashLight(mCameraManager, mCameraId)
                vibratePhone(vibrator)
            }

        }
btnMorse.setOnClickListener {
    GlobalScope.launch {

            morse(editText.text.toString(), mCameraManager, mCameraId)

    }
}


        }

    }



    fun turnOnFlashLight(mCameraManager:CameraManager, mCameraId:String) {

        mCameraManager.setTorchMode(mCameraId, true)

    }



    fun turnOfFlashLight(mCameraManager:CameraManager, mCameraId:String){

        mCameraManager.setTorchMode(mCameraId, false)

    }

    fun vibratePhone(vibrator:Vibrator){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,
                VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

suspend fun shortSignal(mCameraManager:CameraManager, mCameraId:String) {
    try {
        mCameraManager.setTorchMode(mCameraId, true)
        Log.d("morse","long signal ")
        delay(50)
        mCameraManager.setTorchMode(mCameraId, false)
        delay(50)
    }

    catch (e: CameraAccessException) {
        e.printStackTrace()
    }
}
suspend fun longSignal(mCameraManager:CameraManager, mCameraId:String) {


    try {
        mCameraManager.setTorchMode(mCameraId, true)
        Log.d("morse","long signal ")
        delay(150)
        mCameraManager.setTorchMode(mCameraId, false)
        delay(50)
    }

    catch (e: CameraAccessException) {
        e.printStackTrace()
    }

}
suspend fun pause(mCameraManager:CameraManager, mCameraId:String) {
    try {
        mCameraManager.setTorchMode(mCameraId, true)
        Log.d("morse","long signal ")
        delay(450)
        mCameraManager.setTorchMode(mCameraId, false)
        delay(50)
    }

    catch (e: CameraAccessException) {
        e.printStackTrace()
    }
}

suspend fun morse(text: String, mCameraManager:CameraManager, mCameraId:String) {
    val textList = text.split("")



    GlobalScope.launch {
runBlocking {
    for (i in 1..text.length){
        if (textList[i] == " ") {
            pause(mCameraManager,mCameraId) }

        for (l in letters.indices) {
            if (textList[i] == letters[l].toString()) {
            var signal = signals[l].toString().split("")
                for (s in signal.indices){
                    when{
                        signal[s] == "." -> shortSignal(mCameraManager,mCameraId)
                        signal[s] == "-" -> longSignal(mCameraManager,mCameraId)

                    }
                }

            }
        }
    }



}
    }
}



//    for (i in textList){

//        Log.d ("morse","$i")
//        Log.d ("morse","$textList")
//        Log.d ("morse","$text")

       // when {
          // i == "a" ->
//        shortSignal(mCameraManager,mCameraId)
//        shortSignal(mCameraManager,mCameraId)



        //else -> break




        //}











