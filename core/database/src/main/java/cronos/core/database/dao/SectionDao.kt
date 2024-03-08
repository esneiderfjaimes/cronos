package cronos.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import cronos.core.database.models.ChronometerEntity
import cronos.core.database.models.SectionEntity
import cronos.core.database.views.ChronometerWithLastEventView
import cronos.core.database.views.SectionView
import cronos.core.database.views.SectionWithChronometers
import kotlinx.coroutines.flow.Flow

@Dao
interface SectionDao {
    @Insert
    fun insert(section: SectionEntity)

    @Query("SELECT * FROM sections")
    fun sections(): Flow<List<SectionEntity>>

    @Transaction
    @Query(
        "SELECT * FROM sections AS s " +
                "LEFT JOIN chronometer_with_last_event AS c ON s.id = c.c_section_id"
    )
    fun sectionsVie2w(): Flow<List<SectionView>>

    @Transaction
    @Query(
        "SELECT * FROM sections AS s " +
                "LEFT JOIN chronometer_with_last_event AS c ON s.id = c.c_section_id WHERE c.c_is_archived = false"
    )
    fun sectEasions22Vie2w(): Flow<List<SectionWithChronometers>>


    @Transaction
    @Query(
        "SELECT * FROM sections AS s " +
                "LEFT JOIN chronometer_with_last_event AS c ON s.id = c.c_section_id WHERE c.c_is_archived = false"
    )
    fun sectEasionsVie2w(): Flow<Map<SectionEntity, List<ChronometerWithLastEventView>>>

    @Transaction
    @Query("select * from sections_view")
    fun sectionsView(): Flow<List<SectionView>>

    companion object {
        const val KEY_IS_ARCHIVED = "c_is_archived"
    }
}