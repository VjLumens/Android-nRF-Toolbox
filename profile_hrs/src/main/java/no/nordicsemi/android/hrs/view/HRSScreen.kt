package no.nordicsemi.android.hrs.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.hrs.R
import no.nordicsemi.android.hrs.viewmodel.HRSViewModel
import no.nordicsemi.android.theme.view.BackIconAppBar
import no.nordicsemi.android.theme.view.DeviceConnectingView
import no.nordicsemi.android.utils.exhaustive

@Composable
fun HRSScreen() {
    val viewModel: HRSViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    Column {
        BackIconAppBar(stringResource(id = R.string.hrs_title)) {
            viewModel.onEvent(DisconnectEvent)
        }

        when (state) {
            is DisplayDataState -> HRSContentView(state.data) { viewModel.onEvent(it) }
            LoadingState -> DeviceConnectingView()
        }.exhaustive
    }
}
