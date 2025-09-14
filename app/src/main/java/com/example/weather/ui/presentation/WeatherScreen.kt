package com.example.weather.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.R
import com.example.weather.ui.navigation.NavigationItems
import com.example.weather.ui.theme.BorderColorDefault
import com.example.weather.ui.theme.BottomNavItemSelectedBackground
import com.example.weather.ui.theme.ButtonBackgroundDisabled
import com.example.weather.ui.theme.ButtonBackgroundEnabled
import com.example.weather.ui.theme.ButtonTextDisabled
import com.example.weather.ui.theme.ButtonTextEnabled
import com.example.weather.ui.theme.CursorColorPrimary
import com.example.weather.ui.theme.SnackBarBackground
import com.example.weather.ui.theme.TextColorPrimary
import com.example.weather.ui.theme.TextFieldBackground
import com.example.weather.ui.theme.TextFieldIndicator
import com.example.weather.ui.theme.TextFieldLabel
import com.example.weather.ui.theme.WeatherCardBackground
import com.example.weather.ui.theme.WeatherCardText
import com.example.weather.utils.cityToPrepositional
import com.example.weather.utils.toWeatherDescription

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.Black,
        bottomBar = { BottomNavigationBar(viewModel) },
        snackbarHost = {
            SnackbarHost(hostState = viewModel.snackBarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            uiState.currentRequest?.let { (city, temp) ->
                WeatherCard(city = city, temperature = temp, showDescription = true)
                Spacer(modifier = Modifier.height(12.dp))
            } ?: run {
                Text(
                    text = stringResource(R.string.navigatiom_item_weather),
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 12.dp, start = 17.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                InputField(
                    value = uiState.city,
                    onValueChange = { viewModel.updateCity(it) },
                    label = stringResource(R.string.city)
                )

                InputField(
                    value = uiState.temperature,
                    onValueChange = { viewModel.updateTemperature(it) },
                    label = stringResource(R.string.temperature)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                val (label, enabled) = when (uiState.buttonState) {
                    WeatherButtonState.EvaluateDisabled -> stringResource(R.string.evaluate) to false
                    WeatherButtonState.EvaluateEnabled -> stringResource(R.string.evaluate) to true
                    WeatherButtonState.NewRequest -> stringResource(R.string.make_a_new_request) to true
                }
                Button(
                    onClick = {
                        when (uiState.buttonState) {
                             WeatherButtonState.EvaluateDisabled,
                             WeatherButtonState.EvaluateEnabled -> viewModel.addCurrentRequest()
                             WeatherButtonState.NewRequest -> viewModel.resetRequest()
                        }

                    },
                    enabled = enabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (enabled) ButtonBackgroundEnabled else ButtonBackgroundDisabled,
                        contentColor = if (enabled) ButtonTextEnabled else ButtonTextDisabled
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier,
                ) {
                    Text(
                        text = label,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                modifier = Modifier.padding(start = 17.dp),
                text = stringResource(R.string.recently_searched),
                fontSize = 16.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (uiState.previousRequests?.isEmpty() == true) {
                Snackbar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 7.dp, end = 7.dp)
                        .height(33.dp),
                    shape = RoundedCornerShape(6.dp),
                    containerColor = SnackBarBackground,
                    contentColor = Color.White
                ) {
                    Text(text = stringResource(R.string.requests), color = Color.White)
                }
            } else {
                uiState.previousRequests?.forEach { (city, temp) ->
                    WeatherCard(city = city, temperature = temp, showDescription = false)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

        }
    }
}

@Composable
fun WeatherCard(city: String, temperature: String, showDescription: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(WeatherCardBackground)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(city.uppercase(), color = WeatherCardText)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(R.drawable.icon_temp),
                    contentDescription = null,
                    tint = WeatherCardText
                )
                Spacer(Modifier.width(2.dp))
                Text("$temperature°C", color = WeatherCardText)
            }
        }

        if (showDescription) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = BorderColorDefault
            )
            Text(
                "Сейчас в ${city.cityToPrepositional()} ${
                    temperature.toInt().toWeatherDescription()
                }",
                color = Color.White
            )
        }
    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedTextColor = TextColorPrimary,
            unfocusedTextColor = TextColorPrimary,
            cursorColor = CursorColorPrimary,
            unfocusedLabelColor = TextFieldLabel,
            focusedLabelColor = TextFieldLabel,
            focusedIndicatorColor = TextFieldIndicator,
            unfocusedIndicatorColor = TextFieldIndicator,
            focusedContainerColor = TextFieldBackground,
            unfocusedContainerColor = TextFieldBackground
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
    )
}


@Preview
@Composable
fun PreviewWeather() {
    WeatherScreen()
}

@Composable
fun BottomNavigationBar(viewModel: WeatherViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val items = listOf(
        NavigationItems.Weather,
        NavigationItems.News,
        NavigationItems.Gallery
    )

    NavigationBar(
        modifier = Modifier.padding(6.dp),
        containerColor = Color.Black
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    when (item) {
                        NavigationItems.Weather -> Icon(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = null
                        )

                        NavigationItems.News -> Icon(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = null
                        )

                        NavigationItems.Gallery -> Icon(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = null
                        )
                    }
                },
                label = { Text(stringResource(item.titleResId)) },
                selected = uiState.value.selectedTabIndex == index,
                onClick = { viewModel.updateSelectedTab(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .height(53.dp)
                    .border(
                        width = 0.6.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(3.6.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (uiState.value.selectedTabIndex == index) BottomNavItemSelectedBackground else Color.Transparent)
            )
        }
    }
}
