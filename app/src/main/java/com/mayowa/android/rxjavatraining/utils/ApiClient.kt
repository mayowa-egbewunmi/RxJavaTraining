package com.mayowa.android.rxjavatraining.utils

import com.mayowa.android.rxjavatraining.operators.transform_n_combine.User

class ApiClient {

    fun getSurvey(taskId: Long): String = "Demographics Survey"

    /**
     * Mock API client register user to complete successfully
     * and return success status 1
     */
    fun registerUser(user: User): Int = 1
}
