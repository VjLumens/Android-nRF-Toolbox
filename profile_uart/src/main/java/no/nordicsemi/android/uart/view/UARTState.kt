/*
 * Copyright (c) 2022, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.uart.view

import no.nordicsemi.android.service.BleManagerResult
import no.nordicsemi.android.uart.data.UARTConfiguration
import no.nordicsemi.android.uart.data.UARTData
import no.nordicsemi.android.uart.data.UARTMacro

internal data class UARTViewState(
    val editedPosition: Int? = null,
    val selectedConfigurationName: String? = null,
    val isConfigurationEdited: Boolean = false,
    val configurations: List<UARTConfiguration> = emptyList(),
    val uartManagerState: HTSManagerState = NoDeviceState,
    val isInputVisible: Boolean = true
) {
    val showEditDialog: Boolean = editedPosition != null

    val selectedConfiguration: UARTConfiguration? = configurations.find { selectedConfigurationName == it.name }

    val selectedMacro: UARTMacro? = selectedConfiguration?.let { configuration ->
        editedPosition?.let {
            configuration.macros.getOrNull(it)
        }
    }
}

internal sealed class HTSManagerState

internal data class WorkingState(
    val result: BleManagerResult<UARTData>
) : HTSManagerState()

internal object NoDeviceState : HTSManagerState()
