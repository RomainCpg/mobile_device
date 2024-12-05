package com.esilv.hiphiphip.ui.extensions

import android.app.Activity
import android.content.Intent
import kotlin.reflect.KClass

// Fonction d'extension générique pour démarrer une activité
fun <T : Activity> Activity.openActivity(targetActivity: KClass<T>) {
    val intent = Intent(this, targetActivity.java)  // Utilisation de targetActivity.java pour obtenir la classe
    startActivity(intent)
    overridePendingTransition(0,0)
}
