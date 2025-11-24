package com.example.currencyconverter1.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrencyKeypad(onKeyPress: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeypadButton(key = "1", onClick = { onKeyPress("1") }, modifier = Modifier.weight(1f))
            KeypadButton(key = "2", onClick = { onKeyPress("2") }, modifier = Modifier.weight(1f))
            KeypadButton(key = "3", onClick = { onKeyPress("3") }, modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeypadButton(key = "4", onClick = { onKeyPress("4") }, modifier = Modifier.weight(1f))
            KeypadButton(key = "5", onClick = { onKeyPress("5") }, modifier = Modifier.weight(1f))
            KeypadButton(key = "6", onClick = { onKeyPress("6") }, modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeypadButton(key = "7", onClick = { onKeyPress("7") }, modifier = Modifier.weight(1f))
            KeypadButton(key = "8", onClick = { onKeyPress("8") }, modifier = Modifier.weight(1f))
            KeypadButton(key = "9", onClick = { onKeyPress("9") }, modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeypadButton(key = ".", onClick = { onKeyPress(".") }, modifier = Modifier.weight(1f))
            KeypadButton(key = "0", onClick = { onKeyPress("0") }, modifier = Modifier.weight(1f))
            KeypadButton(key = "00", onClick = { onKeyPress("00") }, modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeypadButton(key = "Backspace", onClick = { onKeyPress("Backspace") }, modifier = Modifier.fillMaxWidth(), isFunctionKey = true)
        }
    }
}

@Composable
private fun KeypadButton(
    key: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFunctionKey: Boolean = false
) {
    VibratingButton(
        onClick = onClick,
        modifier = modifier
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFunctionKey) Color(0x9FFA0808) else Color(0x46C0AEAE)
        ),
    ) {
        if (key == "Backspace") {
            Icon(
                imageVector = Icons.Default.Backspace,
                contentDescription = "Backspace",
                tint = Color.White
            )
        } else {
            Text(
                text = key,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
