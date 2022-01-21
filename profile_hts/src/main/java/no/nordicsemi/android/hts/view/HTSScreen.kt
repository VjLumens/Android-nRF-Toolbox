package no.nordicsemi.android.hts.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.hts.R
import no.nordicsemi.android.hts.viewmodel.HTSViewModel
import no.nordicsemi.android.theme.view.BackIconAppBar
import no.nordicsemi.android.theme.view.DeviceConnectingView
import no.nordicsemi.android.utils.exhaustive

@Composable
fun HTSScreen() {
    val viewModel: HTSViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    Column {
        BackIconAppBar(stringResource(id = R.string.hts_title)) {
            viewModel.onEvent(DisconnectEvent)
        }

        when (state) {
            is DisplayDataState -> HTSContentView(state.data) { viewModel.onEvent(it) }
            LoadingState -> DeviceConnectingView()
        }.exhaustive
    }
}
