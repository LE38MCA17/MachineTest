
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("data/2.5/forecast")
    suspend fun getFiveDayForecast(
        @Query("q") location: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): ForecastResponse
}
