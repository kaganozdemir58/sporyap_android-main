package com.sporyap.sporyap.use_case.sport

import com.sporyap.sporyap.data.network.response.sport.GetSports
import com.sporyap.sporyap.data.rp.SportRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SportUseCase @Inject constructor(private val sportRepository: SportRepository){

    suspend operator fun invoke(): GetSports?{
        return sportRepository.getSports().body()
    }
}