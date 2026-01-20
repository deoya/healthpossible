import android.content.Context
import android.widget.Toast

object ToastHelper {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun show(message: String) {
        appContext?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }
}