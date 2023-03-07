package com.motionProject.ble.di

import com.motionProject.ble.BleRepository
import com.motionProject.ble.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}

val repositoryModule = module{
    single{
        BleRepository()
    }
}