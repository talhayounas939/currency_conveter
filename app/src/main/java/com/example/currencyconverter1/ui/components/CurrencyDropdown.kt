package com.example.currencyconverter1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.currencyconverter1.data.model.CountryWithRate

@Composable
fun CurrencyDropdown(
    countries: List<CountryWithRate>,
    onDismiss: () -> Unit,
    onSelect: (CountryWithRate) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF2D0F48))
                .padding(16.dp)
        ) {
            LazyColumn {
                items(countries) { country ->

                    val interactionSource = remember { MutableInteractionSource() }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = androidx.compose.material3.ripple(),
                                onClick = { onSelect(country) }
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = getFlagEmojiForCountry(country.country.alpha2),
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${country.country.ccode} - ${country.country.name_en}",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

private fun getFlagEmojiForCountry(countryCode: String): String {
    if (countryCode.length != 2) return "🏳️"
    val firstLetter = Character.codePointAt(countryCode.uppercase(), 0) - 'A'.code + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCode.uppercase(), 1) - 'A'.code + 0x1F1E6
    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}
