package com.galemu00.acronyms.util

sealed class Resource<T>(
    val data: T? = null,
    var message: String? = null
) {
    class Idel<T>:Resource<T>()
    class Loading<T> : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data = data)

    class Error<T>(data: T? = null, message: String) :
        Resource<T>(
            data = data,
            message = message
        )
}