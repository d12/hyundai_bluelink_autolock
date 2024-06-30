import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hyundai_bluelink_autolock_android.CarConnectionState

class CarConnectionViewModel : ViewModel() {
    private val _state = MutableLiveData(CarConnectionState.NOT_CONNECTED)
    val state: LiveData<CarConnectionState> get() = _state

    fun setState(newState: CarConnectionState) {
        _state.value = newState
    }
}