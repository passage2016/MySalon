package com.example.mysalon.viewModel

import java.util.*

fun getWeekOfDate(day: String): Int {
    val weekDays = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    val intDay = weekDays.indexOf(day)
    val cal = Calendar.getInstance()
    cal.add(Calendar.MINUTE, 30)
    var w = cal[Calendar.DAY_OF_WEEK] - 1
    println((intDay + 7 - w) % 7)
    if (w < 0) {
        w = 0
    }
    return (intDay + 7 - w) % 7
}
fun main(){
    getWeekOfDate("Monday")
}