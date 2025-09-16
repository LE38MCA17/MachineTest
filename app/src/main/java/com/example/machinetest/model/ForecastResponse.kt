
import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val city: City,
    val list: List<ForecastItem>
)

data class City(
    val id: Long,
    val name: String,
    val country: String
)

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    @SerializedName("dt_txt")
    val dtTxt: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
