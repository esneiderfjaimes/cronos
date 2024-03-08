package cronos.core.database.views

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation
import cronos.core.database.models.SectionEntity

@DatabaseView(
    viewName = "sections_view",
    value = "SELECT * FROM sections AS s " +
            "LEFT JOIN chronometer_with_last_event AS c ON s.id = c.c_section_id"
)
data class SectionView(
    @Embedded
    val section: SectionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "c_section_id",
    )
    val chronometers: List<ChronometerWithLastEventView> = emptyList()
)

data class SectionWithChronometers(
    @Embedded
    val section: SectionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "c_section_id",
    )
    val chronometers: List<ChronometerWithLastEventView>
)
