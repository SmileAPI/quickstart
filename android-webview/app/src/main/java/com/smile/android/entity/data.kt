package com.smile.android.entity

import com.smile.android.base.BaseModel
import java.util.*


data class SmileJsModel(
    var apiHost: String? = null,
    var providers: List<String> = emptyList(),
    var userToken: String,
    var enableUpload: Boolean = false,
    var topProviders: List<String> = emptyList(),
) : BaseModel()

data class  AccountModel(
    var providerId: String,
    var accountId: String,
): BaseModel()

data class  ArchiveModel(
    var fileType: String
): BaseModel()

data class ResultModel(
    var expiresAt: String,
    var mode: String,
    var accessToken: String,
    var apiHost: String? = null,
    var providers: List<String> = emptyList(),
    var enableUpload: Boolean = false,
    var topProviders: List<String> = emptyList()
) : BaseModel()