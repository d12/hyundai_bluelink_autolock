import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object CarConnectionViewModelSingleton {
    private var viewModel: CarConnectionViewModel? = null

    fun getInstance(context: Context): CarConnectionViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application).create(CarConnectionViewModel::class.java)
        }
        return viewModel!!
    }
}
