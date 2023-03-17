import org.evcs.android.model.Charge
import org.joda.time.DateTime
import java.io.Serializable

class Session : Charge(), Serializable {
    //duration
    val id = 0

    //    "kwh":"0.0",
    //    "status":"active",
    val updatedAt: DateTime? = null
    private val stationName = 0
}