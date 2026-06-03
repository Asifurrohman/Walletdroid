package com.example.dompetdigital

abstract class MetodePembayaran(val namaMetode: String){
    abstract fun prosesBayar(jumlah: Double): String
}

class pembayaranQris : MetodePembayaran("QRIS"){
    override fun prosesBayar(jumlah: Double): String {
        return "Token QRIS Berhasil Dibuat. Silakan scan untuk membayar Rp $jumlah"
    }
}

class TransferBank(val namaBank: String) : MetodePembayaran("Transfer Bank $namaBank"){
    override fun prosesBayar(jumlah: Double): String {
        val nomorVA = (100000..999999).random()
        return "Transfer Rp $jumlah ke Virtual Account $namaBank: 8800$nomorVA"
    }
}