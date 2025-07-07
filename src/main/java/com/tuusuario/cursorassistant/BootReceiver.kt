package com.tuusuario.cursorassistant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            Intent.ACTION_PACKAGE_REPLACED -> {
                
                // Check if Ami should auto-start (user preference)
                val prefs = context.getSharedPreferences("ami_settings", Context.MODE_PRIVATE)
                val autoStart = prefs.getBoolean("auto_start", true)
                
                if (autoStart) {
                    // Delay startup to ensure system is ready
                    GlobalScope.launch {
                        delay(10000) // Wait 10 seconds after boot
                        
                        try {
                            // Start background service
                            val serviceIntent = Intent(context, AmiBackgroundService::class.java)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(serviceIntent)
                            } else {
                                context.startService(serviceIntent)
                            }
                            
                            // Start floating overlay (with additional delay)
                            delay(5000)
                            FloatingAmiService.start(context)
                            
                        } catch (e: Exception) {
                            // Log error but don't crash
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}