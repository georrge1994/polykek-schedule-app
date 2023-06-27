package argument.twins.com.polykekschedule.activity.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import argument.twins.com.polykekschedule.App
import argument.twins.com.polykekschedule.R
import argument.twins.com.polykekschedule.activity.mvi.ActivityIntent
import argument.twins.com.polykekschedule.activity.viewModels.MainActivityViewModel
import argument.twins.com.polykekschedule.databinding.FragmentNotificationDialogBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.core.ui.fragments.BaseDialogFragment
import com.android.shared.code.utils.syntaxSugar.createViewModel

/**
 * Notification dialog fragment explains to the user why app is needed notification permission.
 *
 * @constructor Create empty constructor for notification dialog fragment
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class NotificationDialogFragment : BaseDialogFragment() {
    private val viewBinding by viewBinding(FragmentNotificationDialogBinding::bind)
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var mainActivityViewModel: MainActivityViewModel

    private val yesClickListener = View.OnClickListener {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private val noClickListener = View.OnClickListener {
        mainActivityViewModel.dispatchIntentAsync(
            ActivityIntent.ShowMessage(getString(R.string.fcm_notification_spirit_leaves))
        )
        dismissAllowingStateLoss()
    }

    override fun injectToComponent() = App.appComponent.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogThemeTransparent)
        requestPermissionLauncher = getCallBackAndPostRequest()
        mainActivityViewModel = (activity as AppCompatActivity).createViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_notification_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogFragmentAnimation
        viewBinding.yesBtn.setOnClickListener(yesClickListener)
        viewBinding.noBtn.setOnClickListener(noClickListener)
    }

    /**
     * Init call back and post request.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getCallBackAndPostRequest() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getString(R.string.fcm_notification_spirit_time_to_time)
            } else {
                getString(R.string.fcm_notification_spirit_leaves)
            }.let { message ->
                mainActivityViewModel.dispatchIntentAsync(ActivityIntent.ShowMessage(message))
            }
            dismissAllowingStateLoss()
        }
}