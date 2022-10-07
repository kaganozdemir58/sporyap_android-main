package com.sporyap.sporyap.data.rp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sporyap.sporyap.data.entity.MediaObject

class MainMediaObjectDaoRepository {
    var mediaObjectList: MutableLiveData<List<MediaObject>>

    init {
        mediaObjectList = MutableLiveData()
    }

    fun getMutableList(): MutableLiveData<List<MediaObject>> {
        return mediaObjectList
    }

    fun getAllObjects() {

        val mList = ArrayList<MediaObject>()

        val i1 = MediaObject(1,"https://sporyaproot@sporyap.app/photos/video/buz_pateni_video.mp4","","Koşu", "Eğitim","Jogging","Bakırköy Sahil / İstanbul",4,6,false,"")
        val i2 = MediaObject(2,"https://sporyaproot@sporyap.app/photos/video/golf_video.mp4","","Yoga","Eğitim", "Yoga","Avcılar / İstanbul",3,23,true,"")
        val i3 = MediaObject(3,"https://sporyaproot@sporyap.app/photos/video/kurek_video.mp4","","Kürek", "Eğitim","Kürek","Tuzla / İstanbul",4,8,false,"")
        val i4 = MediaObject(4,"https://sporyaproot@sporyap.app/photos/video/yuzme_video.mp4","","Yüzme", "Eğitim","Yüzme","Gürün / Sivas",5,12,true,"")
        val i5 = MediaObject(5,"https://sporyaproot@sporyap.app/photos/video/parasut_video.mp4","","Paraşüt", "Eğitim","Paraşüt","Etimesgut / Ankara",2,23,false,"")
        val i6 = MediaObject(6,"https://sporyaproot@sporyap.app/photos/video/ruzgar_sorfu_video.mp4","","Rüzgar", "Eğitim","Rüzgar","Ataköy / İstanbul",3,8,true,"")
        val i7 = MediaObject(7,"https://sporyaproot@sporyap.app/photos/video/tenis_video.mp4","","Tenis", "Eğitim","Tenis","Merter / İstanbul",1,6,false,"")

        mList.add(i1)
        mList.add(i2)
        mList.add(i3)
        mList.add(i4)
        mList.add(i5)
        mList.add(i6)
        mList.add(i7)

        Log.e("deneme","3 çalıştı")

        mediaObjectList.value = mList
    }
}