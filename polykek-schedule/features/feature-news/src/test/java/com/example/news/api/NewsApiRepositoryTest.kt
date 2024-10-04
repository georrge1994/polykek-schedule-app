package com.example.news.api

import com.android.common.models.api.Resource
import com.android.test.support.unitTest.base.BASE_TEST_URL
import com.android.test.support.unitTest.base.BaseApiRepositoryTest
import com.android.test.support.unitTest.checkNegativeResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

/**
 * Test for [NewsApiRepository].
 */
class NewsApiRepositoryTest : BaseApiRepositoryTest() {
    private val newsApiRepository = NewsApiRepository(
        newsApiService = getApi(NewsApiService::class.java)
    )

    override fun <T> getApi(apiServiceClass: Class<T>): T = Retrofit.Builder()
        .baseUrl(mockWebServer.url(BASE_TEST_URL))
        .client(defaultClient)
        // JaxbConverterFactory is not supported in Android.
        // We can migrate to the JacksonConverterFactory, but it's an another big deal.
        .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
        .build()
        .create(apiServiceClass)

    /**
     * Get news 404.
     */
    @Test
    fun getNews_404() = runTest {
        enqueueResponse(code = 404)
        newsApiRepository.getMediaFromRss().checkNegativeResult()
    }

    /**
     * Get news 200.
     */
    @Test
    fun getNews_200() = runTest {
        enqueueResponse(fileName = "news-200.xml", code = 200)
        newsApiRepository.getMediaFromRss().apply {
            assert(this is Resource.Success)
            assertEquals("Новости", data?.channel?.title)
            assertEquals(20, data?.channel?.item?.size)
            data?.channel?.item?.get(9)?.apply {
                assertEquals("Высшая инженерно-экономическая школа Политеха — победитель конкурса на получение мегагранта БРИКС", title)
                assertEquals("https://www.spbstu.ru/media/news/achievements/vysshaya-inzhenerno-ekonomicheskaya-shkola-politekha-pobeditel-konkursa-na-poluchenie-megagranta-bri/", link)
                assertEquals("https://www.spbstu.ru/upload/iblock/d92/1.jpg", enclosure?.url)
                assertEquals("Достижения/", category)
                assertEquals("Команда исследователей из&nbsp;Высшей инженерно-экономической школы (ВИЭШ) Института промышленного...", description)
                assertEquals("Wed, 25 Sep 2024 13:57:00 +0300", pubDate)
            }
        }
    }
}