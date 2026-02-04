import android.content.Context
import android.widget.Toast
import com.hye.shared.util.CommonUiEffect

object UiEffectHelper {
    fun handle(
        context: Context,
        effect: CommonUiEffect,
        onNavigateBack: () -> Unit = {}
    ) {
        when(effect) {
            is CommonUiEffect.ShowToast -> {
                if (effect.message.isNotBlank()) {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
            is CommonUiEffect.NavigateBack -> {
                onNavigateBack()
            }
        }
    }
}