package com.esilv.hiphiphip.levels

import android.widget.TextView



abstract class Level {
    abstract fun getWordStyle(textView: TextView)
    abstract fun animateWord(view: TextView)
    abstract fun spawnWordBehaviour(): Pair<Int, Int>
    abstract fun onWordClickEffect(view: TextView, isCorrect: Boolean)
}