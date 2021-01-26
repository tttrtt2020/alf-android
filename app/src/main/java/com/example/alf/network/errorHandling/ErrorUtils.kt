package com.example.alf.network.errorHandling;

import okhttp3.ResponseBody;
import retrofit2.Converter
import retrofit2.Response;
import java.io.IOException
import java.util.*

class ErrorUtils {

    companion object {
        fun parseError(response: Response<out Any>): ApiError {
            val converter: Converter<ResponseBody, ApiError> = ServiceGenerator.retrofit
                    .responseBodyConverter(ApiError::class.java, arrayOfNulls<Annotation>(0))

            val error: ApiError
            error = try {
                converter.convert(response.errorBody()!!)!!
            } catch (e: IOException) {
                return ApiError(
                        "Unknown status",
                        Date().toString(),
                        "Unknown error",
                        "Unknown error",
                        emptyList()
                )
            }
            return error
        }
    }

}