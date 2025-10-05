package com.example.weather.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.presentation.weather.WeatherComponent
import com.example.weather.ui.theme.BorderColorDefault
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
fun WeatherContent(
    component: WeatherComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        model.currentRequest?.let { request ->
            WeatherCard(
                city = request.city,
                temperature = request.temperature,
                showDescription = true
            )
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
                value = model.city,
                onValueChange = component::onCityChanged,
                label = stringResource(R.string.city)
            )

            InputField(
                value = model.temperature,
                onValueChange = component::onTemperatureChanged,
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
            val (label, enabled) = when (model.buttonState) {
                WeatherComponent.WeatherButtonState.EvaluateDisabled -> stringResource(R.string.evaluate) to false
                WeatherComponent.WeatherButtonState.EvaluateEnabled -> stringResource(R.string.evaluate) to true
                WeatherComponent.WeatherButtonState.NewRequest -> stringResource(R.string.make_a_new_request) to true
            }

            Button(
                onClick = {
                    when (model.buttonState) {
                        WeatherComponent.WeatherButtonState.EvaluateDisabled,
                        WeatherComponent.WeatherButtonState.EvaluateEnabled -> component.onEvaluateClicked()

                        WeatherComponent.WeatherButtonState.NewRequest -> component.onNewRequestClicked()
                    }
                },
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (enabled) ButtonBackgroundEnabled else ButtonBackgroundDisabled,
                    contentColor = if (enabled) ButtonTextEnabled else ButtonTextDisabled
                ),
                shape = RoundedCornerShape(4.dp),
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

        if (model.previousRequests.isEmpty()) {
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
            model.previousRequests.forEach { request ->
                WeatherCard(
                    city = request.city,
                    temperature = request.temperature,
                    showDescription = false
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun WeatherCard(
    city: String,
    temperature: String,
    showDescription: Boolean = false
) {
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
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = BorderColorDefault,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(city.uppercase(), color = WeatherCardText)
            }
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
            Divider(
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