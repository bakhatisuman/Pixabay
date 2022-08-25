package com.example.pixabaylibrary.module

import android.content.Context
import com.example.pixabaylibrary.repo.PixabayImageRepo
import com.example.pixabaylibrary.service.PixabayPictureService
import com.example.pixabaylibrary.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    val HTTP_DIR_CACHE = "pixabay"
    val CACHE_SIZE = 10 * 1024 * 1024


    @Provides
    @Singleton
    fun provideRetrofitService(@ApplicationContext appContext: Context, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://pixabay.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



    @Provides
    @Singleton
    fun providePixabayPictureServiceInstance( retrofit: Retrofit): PixabayPictureService {
        return retrofit.create(PixabayPictureService::class.java)
    }

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        return Cache(File(context.cacheDir, HTTP_DIR_CACHE), CACHE_SIZE.toLong())
    }


    @Provides
    @Singleton
    fun providePixabayRepositoryInstance(
        apiService: PixabayPictureService , @ApplicationContext context : Context): PixabayImageRepo {
        return PixabayImageRepo(apiService, context)
    }

    @Provides
    @Singleton
    fun provideHttpClient(@ApplicationContext context: Context, cache: Cache): OkHttpClient {

        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (NetworkUtils.hasInternetConnection(context))
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                chain.proceed(request)
            }
            .build()
    }

}