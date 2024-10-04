package com.android.core.retrofit.impl.retrofit

import android.app.Application
import com.android.core.retrofit.api.JSON_RETROFIT
import com.android.core.retrofit.api.RSS_MEDIA_RETROFIT
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL = "https://ruz.spbstu.ru/api/v1/ruz/"
private const val BASE_MEDIA_URL = "https://www.spbstu.ru/"
private const val BASE_ENGLISH_MEDIA_URL = "https://english.spbstu.ru/"
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
    @Named(JSON_RETROFIT)
    internal fun getRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    /**
     * Get RSS retrofit client for media news.
     *
     * @param okHttpClient Ok http client
     * @return [Retrofit]
     */
    @Provides
    @Singleton
    @Named(RSS_MEDIA_RETROFIT)
    internal fun getMediaRssRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val baseUrl = if (Locale.getDefault().displayLanguage.contains("русский", ignoreCase = true)) {
            BASE_MEDIA_URL
        } else {
            BASE_ENGLISH_MEDIA_URL
        }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            // JaxbConverterFactory is not supported in Android.
            // We can migrate to the JacksonConverterFactory, but it's an another big deal.
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
            .client(okHttpClient)
            .build()
    }
}