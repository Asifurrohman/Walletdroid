package com.example.dompetdigital

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.*

// Model data DompetDigital
class DompetDigital(private val pinBenar: String) {
    var saldo by mutableStateOf(150000.0)
        private set

    fun topUp(jumlah: Double): String {
        return if (jumlah < 10000) {
            "*Gagal: Minimal Top Up Rp 10.000"
        } else {
            saldo += jumlah
            "Top up berhasil!"
        }
    }

    fun tarikTunai(jumlah: Double, pinInput: String): String {
        return if (pinInput != pinBenar) {
            "*Gagal: PIN Salah!"
        } else if (jumlah > saldo) {
            "*Gagal: Saldo tidak cukup!"
        } else {
            saldo -= jumlah
            "Tarik tunai berhasil!"
        }
    }
}

// Fungsi pembantu format rupiah
fun formatRupiah(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount).replace(",00", "").replace("Rp", "Rp ")
}

@Composable
fun WalletScreen() {
    val dompet = remember { DompetDigital("1234") }
    var statusText by remember { mutableStateOf("") }
    var inputVal by remember { mutableStateOf("") }
    
    // Warna tema sesuai gambar
    val blueColor = Color(0xFF2E86C1)
    val orangeColor = Color(0xFFEB812A)
    val cardBgColor = Color(0xFFF8F9F9)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(blueColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "MyWallet v1.0",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // --- CARD SALDO ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardBgColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 30.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TOTAL SALDO",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatRupiah(dompet.saldo),
                    color = Color(0xFF2C3E50),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // --- INPUT FIELD ---
        OutlinedTextField(
            value = inputVal,
            onValueChange = { inputVal = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 45.dp),
            placeholder = { Text("Masukkan Jumlah / PIN", color = Color.LightGray) },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        // --- TOMBOL TOP UP ---
        Button(
            onClick = {
                val jumlah = inputVal.toDoubleOrNull() ?: 0.0
                statusText = dompet.topUp(jumlah)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(horizontal = 30.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = blueColor)
        ) {
            Text("Top Up", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- TOMBOL TARIK TUNAI ---
        Button(
            onClick = {
                // Untuk simulasi, kita pakai inputVal sebagai PIN dan nominal fix 20rb
                statusText = dompet.tarikTunai(20000.0, inputVal)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(horizontal = 30.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = orangeColor)
        ) {
            Text("Tarik Tunai", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(25.dp))

        // --- STATUS / ERROR MESSAGE ---
        if (statusText.isNotEmpty()) {
            Text(
                text = statusText,
                color = if (statusText.contains("berhasil")) Color(0xFF388E3C) else Color.Red,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}
