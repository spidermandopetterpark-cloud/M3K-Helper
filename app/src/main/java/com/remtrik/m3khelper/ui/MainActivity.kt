package com.remtrik.m3khelper.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rikka.shizuku.Shizuku

class MainActivity : ComponentActivity() {

    companion object {
        private const val SHIZUKU_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                M3KHelperScreen(
                    onRequestShizuku = {
                        requestShizukuPermission()
                    },
                    onStartWindows = {
                        startWindows11()
                    },
                    onReboot = {
                        rebootAndroid()
                    }
                )
            }
        }
    }

    private fun requestShizukuPermission() {
        try {
            if (!Shizuku.pingBinder()) {
                Toast.makeText(
                    this,
                    "Shizuku não está executando.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            if (Shizuku.checkSelfPermission() ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    this,
                    "Shizuku já está autorizado.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            Shizuku.requestPermission(SHIZUKU_REQUEST_CODE)

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Erro no Shizuku: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startWindows11() {
        Toast.makeText(
            this,
            "Windows 11: iniciar máquina virtual",
            Toast.LENGTH_SHORT
        ).show()

        /*
         * Coloque aqui o comando real do seu backend/QEMU.
         *
         * Exemplo:
         * startService(Intent(this, WindowsService::class.java))
         *
         * Não é possível transformar o Android fisicamente
         * em Snapdragon 8 Elite Gen 5 ou criar RAM física.
         */
    }

    private fun rebootAndroid() {
        if (!hasShizukuPermission()) {
            Toast.makeText(
                this,
                "Autorize o Shizuku primeiro.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        try {
            val process = Shizuku.newProcess(
                arrayOf("reboot"),
                null,
                null
            )

            process.start()

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Falha ao reiniciar: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun hasShizukuPermission(): Boolean {
        return try {
            Shizuku.pingBinder() &&
                Shizuku.checkSelfPermission() ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
        } catch (_: Exception) {
            false
        }
    }
}

@androidx.compose.runtime.Composable
private fun M3KHelperScreen(
    onRequestShizuku: () -> Unit,
    onStartWindows: () -> Unit,
    onReboot: () -> Unit
) {
    var ram by remember { mutableStateOf("4 GB") }
    var cpu by remember { mutableStateOf("4 vCPU") }

    var ramMenu by remember { mutableStateOf(false) }
    var cpuMenu by remember { mutableStateOf(false) }

    val ramOptions = listOf(
        "2 GB",
        "4 GB",
        "6 GB",
        "8 GB",
        "12 GB",
        "16 GB",
        "20 GB",
        "24 GB",
        "26 GB"
    )

    val cpuOptions = listOf(
        "1 vCPU",
        "2 vCPU",
        "4 vCPU",
        "6 vCPU",
        "8 vCPU",
        "12 vCPU"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            text = "M3K Helper",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Windows 11 / Shizuku",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "Memória RAM da VM",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = ram,
                        modifier = Modifier.padding(12.dp)
                    )

                    OutlinedButton(
                        onClick = {
                            ramMenu = true
                        }
                    ) {
                        Text("Selecionar")
                    }

                    DropdownMenu(
                        expanded = ramMenu,
                        onDismissRequest = {
                            ramMenu = false
                        }
                    ) {
                        ramOptions.forEach { option ->

                            DropdownMenuItem(
                                text = {
                                    Text(option)
                                },
                                onClick = {
                                    ram = option
                                    ramMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "Processador da VM",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = cpu,
                        modifier = Modifier.padding(12.dp)
                    )

                    OutlinedButton(
                        onClick = {
                            cpuMenu = true
                        }
                    ) {
                        Text("Selecionar")
                    }

                    DropdownMenu(
                        expanded = cpuMenu,
                        onDismissRequest = {
                            cpuMenu = false
                        }
                    ) {
                        cpuOptions.forEach { option ->

                            DropdownMenuItem(
                                text = {
                                    Text(option)
                                },
                                onClick = {
                                    cpu = option
                                    cpuMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onRequestShizuku
        ) {
            Text("Autorizar Shizuku")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onStartWindows
        ) {
            Text("Iniciar Windows 11")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onReboot
        ) {
            Text("Reiniciar Android")
        }
    }
}
