@file:OptIn(ExperimentalAnimationApi::class)

package com.example.currencyconverter1.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter1.R
import com.example.currencyconverter1.data.model.CountryWithRate
import com.example.currencyconverter1.ui.theme.JameelNooriNastaleeq
import com.example.currencyconverter1.ui.viewmodel.CurrencyViewModel
import java.util.*

@Composable
fun FirstSelectionScreen(
    vm: CurrencyViewModel,
    onNext: () -> Unit
) {
    val fromCurrency by vm.fromCurrency.collectAsState()
    val toCurrency by vm.toCurrency.collectAsState()
    val allCountries by vm.countries.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val currentLanguage = Locale.getDefault().language

    val filteredCountries = remember(searchQuery, allCountries, currentLanguage) {
        if (searchQuery.isBlank()) {
            allCountries
        } else {
            allCountries.filter {
                val countryName = if (currentLanguage == "ur") {
                    it.country.name_ur
                } else {
                    it.country.name_en
                }

                countryName.contains(searchQuery, ignoreCase = true) ||
                        it.country.ccode.contains(searchQuery, ignoreCase = true)
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF351059),
                        Color(0xFF2D0F48),
                        Color(0xFF131138)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 19.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                RotatingEarthIcon(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.1f), shape = CircleShape)
                        .align(Alignment.TopEnd)
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(160.dp)
                        .align(Alignment.Center)
                        .alpha(0.95f)
                )
            }

            Text(
                text = stringResource(R.string.to_start_currency_conversion),
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xD300EFC5), Color(0xFF91A2E7))
                    ),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            val titleText = when {
                fromCurrency == null -> stringResource(id = R.string.select_from_currency)
                toCurrency == null -> stringResource(id = R.string.select_to_currency)
                else -> stringResource(id = R.string.currencies_selected)
            }

            AnimatedSearchBox(
                text = titleText,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (vm.isLoadingRates.collectAsState().value) {
                CircularProgressIndicator(color = Color.White)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredCountries, key = { it.country.id }) { currency ->
                        val countryName: String
                        val displayFontFamily: FontFamily?

                        if (currentLanguage == "ur") {
                            countryName = currency.country.name_ur
                            displayFontFamily = JameelNooriNastaleeq
                        } else {
                            countryName = currency.country.name_en
                            displayFontFamily = null
                        }

                        CurrencyCard(
                            countryName = countryName,
                            fontFamily = displayFontFamily,
                            currency = currency,
                            isSelected = fromCurrency?.country?.id == currency.country.id,
                            isToCurrency = toCurrency?.country?.id == currency.country.id,
                            onClick = {
                                val localFrom = fromCurrency
                                val localTo = toCurrency

                                if (localFrom == null) {
                                    vm.setFromCurrency(currency)
                                } else if (localTo == null) {
                                    if (currency.country.id == localFrom.country.id) {
                                        vm.setFromCurrency(null)
                                    } else {
                                        vm.setToCurrency(currency)
                                    }
                                } else {
                                    when (currency.country.id) {
                                        localTo.country.id -> vm.setToCurrency(null)
                                        localFrom.country.id -> {
                                            vm.setFromCurrency(localTo)
                                            vm.setToCurrency(null)
                                        }
                                        else -> vm.setFromCurrency(currency)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = fromCurrency != null && toCurrency != null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Button(
                onClick = onNext,
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFA9B1717)
                )
            ) {
                Text(
                    stringResource(R.string.next),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun RotatingEarthIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    Icon(
        imageVector = Icons.Filled.Public,
        contentDescription = "Earth Icon",
        tint = Color(0xFF48E9CC),
        modifier = modifier
            .graphicsLayer(rotationZ = rotation)
    )
}

@Composable
private fun AnimatedSearchBox(
    text: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    var isSearchVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        AnimatedVisibility(
            visible = !isSearchVisible,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        AnimatedVisibility(
            visible = isSearchVisible,
            enter = slideInHorizontally(
                initialOffsetX = { it / 2 },
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400)),
            exit = slideOutHorizontally(
                targetOffsetX = { it / 5 },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300)),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(stringResource(R.string.search_country), color = Color.White.copy(alpha = 0.6f))
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.White,
                    unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                    focusedContainerColor = Color.White.copy(alpha = 0.1f)
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AnimatedContent(
            targetState = isSearchVisible,
            transitionSpec = {
                fadeIn(animationSpec = tween(200, delayMillis = 200)) togetherWith
                        fadeOut(animationSpec = tween(200))
            }, label = "Search Icon Animation"
        ) { targetState ->
            if (!targetState) {
                IconButton(onClick = { isSearchVisible = true }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            } else {
                IconButton(onClick = {
                    if (searchQuery.isNotEmpty()) onSearchQueryChange("")
                    else isSearchVisible = false
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
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

@Composable
fun CurrencyCard(
    countryName: String,
    fontFamily: FontFamily?,
    currency: CountryWithRate,
    isSelected: Boolean,
    isToCurrency: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val backgroundColor = Color.White.copy(alpha = 0.05f)
    val borderColor = when {
        isSelected -> Color(0xFF4DCDE4)
        isToCurrency -> Color(0xFF2FEEC5)
        else -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (borderColor != Color.Transparent) Modifier.border(1.5.dp, borderColor, shape) else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = getFlagEmojiForCountry(currency.country.alpha2),
            fontSize = 24.sp,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = countryName,
            fontFamily = fontFamily,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = currency.country.ccode,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}
