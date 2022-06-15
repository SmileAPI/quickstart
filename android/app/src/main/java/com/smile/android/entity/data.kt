package com.smile.android.entity

import com.smile.android.base.BaseModel


data class SmileJsModel(
    var apiHost: String? = "",
    var providers: List<String>? = emptyList(),
    var userToken: String? = null,
    var enableUpload: Boolean? = false,
    var topProviders: List<String> = emptyList(),
    var providerTypes: List<String> = emptyList()
) : BaseModel()

data class ResultModel(
    var expiresAt: String,
    var mode: String,
    var accessToken: String,
    var apiHost: String
)