package com.example.simplealarm.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier

// ViewModel 에서 사용될 RepositoryInterface 구현체 제공 방법을 정의
// Interface 의 경우 의존성 주입시  @Module 과 @Binds 어노테이션을 사용
// 현재는 Local Repository(Room) 만 사용중이지만 만약 Remote Repository(Retrofit) 도 사용해야 하면 다음과 같이 사용한다
// 1. annotation class 생성하여 고유한 이름 부여하기 + @Qualifier + @Retention 어노테이션 사용
// 2. @Bind 어노테이션과 더해 @LocalRepositry 어노테이션 붙이기
// 3. 해당 Repository가 사용되는 부분에 @LocalRepository 어노테이션을 사용해 구분하기

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @LocalRepository
    @Binds
    abstract fun bindRepository(repository : RoomRepository) : RepositoryInterface
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalRepository