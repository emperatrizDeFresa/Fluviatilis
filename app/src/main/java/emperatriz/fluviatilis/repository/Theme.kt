package emperatriz.fluviatilis.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "theme")
data class Theme (
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "fluvHeight") val fluvHeight:Int,
        @ColumnInfo(name = "fluvNumber") val fluvNumber: Int,
        @ColumnInfo(name = "fluvWeight") val fluvWeight: Int,
        @ColumnInfo(name = "speed") val speed:Int,
        @ColumnInfo(name = "wideness") val wideness:Int,
        @ColumnInfo(name = "heightness") val heightness:Int,
        @ColumnInfo(name = "dimAlpha") val dimAlpha:Int,
        @ColumnInfo(name = "dimHeight") val dimHeight:Int,
        @ColumnInfo(name = "rotation") val rotation:Int,
        @ColumnInfo(name = "horizontalOffset") val horizontalOffset:Int,
        @ColumnInfo(name = "verticalOffset") val verticalOffset:Int,
        @ColumnInfo(name = "color") val color:Int,
        @ColumnInfo(name = "colorLeft") val colorLeft:Int,
        @ColumnInfo(name = "colorRight") val colorRight:Int,
        @ColumnInfo(name = "widgetSelected") val widgetSelected:Int,
        @ColumnInfo(name = "widgetX") val widgetX:Int?,
        @ColumnInfo(name = "widgetY") val widgetY:Int?,
        @ColumnInfo(name = "widgetXsize") val widgetXsize:Int?,
        @ColumnInfo(name = "style") val style:Int?,
        @ColumnInfo(name = "discreto") val discreto:Boolean?,
        @ColumnInfo(name = "halo") val halo:Boolean?,
        @ColumnInfo(name = "colorW1") val colorW1:Int?,
        @ColumnInfo(name = "colorW2") val colorW2:Int?,
        @ColumnInfo(name = "colorW3") val colorW3:Int?,
        @ColumnInfo(name = "colorW4") val colorW4:Int?,
        )