package cronos.core.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import cronos.core.database.converter.ChronometerFormatConverter
import cronos.core.database.converter.EventTypeConverter
import cronos.core.database.converter.IdsConverter
import cronos.core.database.converter.ZonedDateTimeConverter
import cronos.core.database.dao.ChronometerDao
import cronos.core.database.dao.EventDao
import cronos.core.database.dao.SectionDao
import cronos.core.database.models.ChronometerEntity
import cronos.core.database.models.EventEntity
import cronos.core.database.models.SectionEntity
import cronos.core.database.utils.Mocks
import cronos.core.database.views.ChronometerWithLastEventView
import cronos.core.database.views.LastEventRefView
import cronos.core.database.views.SectionView

@Database(
    version = 2,
    entities = [SectionEntity::class, ChronometerEntity::class, EventEntity::class],
    views = [
        SectionView::class,
        ChronometerWithLastEventView::class,
        LastEventRefView::class,
    ],
    exportSchema = true,
)
@TypeConverters(
    IdsConverter::class,
    ZonedDateTimeConverter::class,
    ChronometerFormatConverter::class,
    EventTypeConverter::class
)
abstract class CronosDatabase : RoomDatabase() {

    abstract fun sectionDao(): SectionDao

    abstract fun chronometerDao(): ChronometerDao

    abstract fun lapDao(): EventDao

    companion object {

        private const val DATABASE_NAME = "cronos_database"

        @Synchronized
        fun getDatabase(context: Context): CronosDatabase {
            return Room.databaseBuilder(
                context, CronosDatabase::class.java, DATABASE_NAME
            ).run {
                addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.d(TAG, "callback onCreate called")

                        // insert default values
                        SectionEntity.onCreate(db)
                        ChronometerEntity.onDbCreate(db, Mocks.getFirstChronometer(context))
                    }

                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                        Log.d(TAG, "callback onDestructiveMigration called")
                    }
                })
                fallbackToDestructiveMigrationOnDowngrade()
                addMigrations(DatabaseMigrations.Schema1to2)
                build()
            }
        }

        private const val TAG = "CronosDatabase"
    }
}