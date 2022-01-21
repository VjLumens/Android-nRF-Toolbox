package no.nordicsemi.android.cgms.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.cgms.R
import no.nordicsemi.android.cgms.viewmodel.CGMScreenViewModel
import no.nordicsemi.android.theme.view.BackIconAppBar
import no.nordicsemi.android.theme.view.DeviceConnectingView
import no.nordicsemi.android.utils.exhaustive

@Composable
fun CGMScreen() {
    val viewModel: CGMScreenViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    Column {
        BackIconAppBar(stringResource(id = R.string.cgms_title)) {
            viewModel.onEvent(DisconnectEvent)
        }

        when (state) {
            is DisplayDataState -> CGMContentView(state.data) { viewModel.onEvent(it) }
            LoadingState -> DeviceConnectingView()
        }.exhaustive
    }
}
