package com.example.travelmemories.memories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "memories_table")
data class Memory(
    @ColumnInfo(name = "place_name") var placeName: String,
    @ColumnInfo(name = "place_location") var placeLocation: String,
    @ColumnInfo(name = "place_coordinate_lat") var placeLatitude: Double?,
    @ColumnInfo(name = "place_coordinate_long") var placeLongitude: Double?,
    @ColumnInfo(name = "travel_type") var travelType: String?,
    @ColumnInfo(name = "travel_time") var travelTime: String,
    @ColumnInfo(name = "mood_level") var moodLevel: Float,
    @ColumnInfo(name = "memory_notes") var memoryNotes: String?,
    @ColumnInfo(name = "memory_image") var memoryImage: String?
    ) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0
}
