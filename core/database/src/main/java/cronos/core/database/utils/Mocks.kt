package cronos.core.database.utils

import android.content.Context
import cronos.core.database.R
import cronos.core.database.models.ChronometerEntity

object Mocks {

    fun getFirstChronometer(context: Context): ChronometerEntity {
        val title = context.resources.getString(R.string.first_chronometer_title)
        return ChronometerEntity(
            title = title
        )
    }

}