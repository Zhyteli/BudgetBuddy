package com.budget.buddy.data.database.getdata

import com.budget.buddy.domain.user.MainUserDataMouth

sealed class UserDataState {
    object Loading : UserDataState()
    data class Success(val data: MainUserDataMouth?) : UserDataState()
    data class Error(val message: String) : UserDataState()
}
