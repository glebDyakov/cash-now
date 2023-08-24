package cash.now.cshnw.di

import android.content.Context
import androidx.room.Room
import cash.now.cshnw.database.RecipesDataBase
import cash.now.cshnw.utils.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context.applicationContext,
        RecipesDataBase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideDao(dataBase: RecipesDataBase) = dataBase.RecipesDao()

}