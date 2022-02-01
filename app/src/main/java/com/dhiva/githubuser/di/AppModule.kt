package com.dhiva.githubuser.di

import com.dhiva.githubuser.core.domain.usecase.UserInteractor
import com.dhiva.githubuser.core.domain.usecase.UserUseCase
import com.dhiva.githubuser.home.MainViewModel
import com.dhiva.githubuser.userdetail.UserDetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<UserUseCase> { UserInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { UserDetailViewModel(get()) }
}