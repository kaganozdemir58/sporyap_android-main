package com.sporyap.sporyap.data.entity

import java.io.Serializable

data class MediaObject (
    var Id:Int,
    var media_url:String,
    var ProfileImage:String,
    var SportName:String,
    var EventTypeName:String,
    var EventName:String,
    var LocationName:String,
    var Rating:Int,
    var TotalMemberCount:Int,
    var IsLiked:Boolean,
    var EventCoverImage:String,
) : Serializable {
}