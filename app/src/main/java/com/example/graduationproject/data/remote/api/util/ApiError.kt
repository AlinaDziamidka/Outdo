import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("code")
    val errorCode: Int,
    @SerializedName("message")
    val message: String,
)

fun String.toApiError(): ApiError = Gson().fromJson(this, ApiError::class.java)

enum class ApiErrorCode {

}