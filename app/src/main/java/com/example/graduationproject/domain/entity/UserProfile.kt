package com.example.graduationproject.domain.entity

import android.os.Parcel
import android.os.Parcelable

data class UserProfile(
    val userId: String,
    val username: String,
    val userEmail: String,
    val userAvatarPath: String?
)
//    : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.readString()!!
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(userId)
//        parcel.writeString(username)
//        parcel.writeString(userEmail)
//        parcel.writeString(userAvatarPath)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<UserProfile> {
//        override fun createFromParcel(parcel: Parcel): UserProfile {
//            return UserProfile(parcel)
//        }
//
//        override fun newArray(size: Int): Array<UserProfile?> {
//            return arrayOfNulls(size)
//        }
//    }
//}