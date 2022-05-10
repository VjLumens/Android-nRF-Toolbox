package no.nordicsemi.android.cgms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import no.nordicsemi.android.cgms.data.CGMS_SERVICE_UUID
import no.nordicsemi.android.cgms.repository.CGMRepository
import no.nordicsemi.android.cgms.data.CGMServiceCommand
import no.nordicsemi.android.cgms.view.*
import no.nordicsemi.android.navigation.*
import no.nordicsemi.android.utils.exhaustive
import no.nordicsemi.android.utils.getDevice
import no.nordicsemi.ui.scanner.ScannerDestinationId
import javax.inject.Inject

@HiltViewModel
internal class CGMScreenViewModel @Inject constructor(
    private val repository: CGMRepository,
    private val navigationManager: NavigationManager
) : ViewModel() {

    private val _state = MutableStateFlow<BPSViewState>(NoDeviceState)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            if (repository.isRunning.firstOrNull() == false) {
                requestBluetoothDevice()
            }
        }

        repository.data.onEach {
            _state.value = WorkingState(it)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: CGMViewEvent) {
        when (event) {
            DisconnectEvent -> disconnect()
            is OnWorkingModeSelected -> onCommandReceived(event.workingMode)
            NavigateUp -> navigationManager.navigateUp()
            OpenLoggerEvent -> repository.openLogger()
        }.exhaustive
    }

    private fun requestBluetoothDevice() {
        navigationManager.navigateTo(ScannerDestinationId, UUIDArgument(CGMS_SERVICE_UUID))

        navigationManager.recentResult.onEach {
            if (it.destinationId == ScannerDestinationId) {
                handleArgs(it)
            }
        }.launchIn(viewModelScope)
    }

    private fun handleArgs(args: DestinationResult) {
        when (args) {
            is CancelDestinationResult -> navigationManager.navigateUp()
            is SuccessDestinationResult -> repository.launch(args.getDevice())
        }.exhaustive
    }

    private fun onCommandReceived(workingMode: CGMServiceCommand) {
        when (workingMode) {
            CGMServiceCommand.REQUEST_ALL_RECORDS -> repository.requestAllRecords()
            CGMServiceCommand.REQUEST_LAST_RECORD -> repository.requestLastRecord()
            CGMServiceCommand.REQUEST_FIRST_RECORD -> repository.requestFirstRecord()
            CGMServiceCommand.DISCONNECT -> disconnect()
        }.exhaustive
    }

    private fun disconnect() {
        repository.release()
        navigationManager.navigateUp()
    }
}
