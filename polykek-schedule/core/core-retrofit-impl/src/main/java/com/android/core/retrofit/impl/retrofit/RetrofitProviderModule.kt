package com.android.core.retrofit.impl.retrofit

import android.app.Application
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "https://ruz.spbstu.ru/api/v1/ruz/"
private const val TIME_OUT_IN_SECONDS = 30

@Module
internal class RetrofitProviderModule {
    @Provides
    @Singleton
    internal fun provideDefaultOkHttpClient(
        application: Application,
        polytechOnlineCacheInterceptor: PolytechOnlineCacheInterceptor,
        polytechOfflineCacheInterceptor: PolytechOfflineCacheInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIME_OUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
        .readTimeout(TIME_OUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
        .addNetworkInterceptor(polytechOnlineCacheInterceptor)
        .addInterceptor(polytechOfflineCacheInterceptor)
        .cache(Cache(application.cacheDir, 32 * 1024 * 1024)) // 32 MB.
        .build()

    /**
     * Get retrofit.
     *
     * @param okHttpClient Ok http client
     * @return [Retrofit]
     */
    @Provides
    @Singleton
    internal fun getRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}