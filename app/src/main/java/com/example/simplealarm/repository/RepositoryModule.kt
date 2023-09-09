package com.example.simplealarm.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

// ViewModel 에서 사용될 RepositoryInterface 구현체 제공 방법을 정의
// Interface 의 경우 의존성 주입시  @Module 과 @Binds 어노테이션을 사용
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(repository : LocalRepository) : RepositoryInterface
}