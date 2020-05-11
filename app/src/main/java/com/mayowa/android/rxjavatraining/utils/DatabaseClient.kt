package com.mayowa.android.rxjavatraining.utils

class DatabaseClient {

    fun saveUserName(name: String?) {
        if (name != null) {
            println("User name is saved as $name")
        } else {
            throw NullPointerException()
        }
    }

    fun getUser(userId: Int): ProxyUser? = if (userId != -1) {
        ProxyUser(
            email = "will@gmail.com",
            fullname = "William Shelor"
        )
    } else {
        null
    }
}
