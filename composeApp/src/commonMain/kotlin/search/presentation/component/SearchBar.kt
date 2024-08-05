package io.ikutsu.osumusic.search.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import core.presentation.res.OMIcon
import core.presentation.res.omicon.Close
import core.presentation.res.omicon.Search
import io.ikutsu.osumusic.core.presentation.component.OMIconButton
import io.ikutsu.osumusic.core.presentation.theme.OM_Primary
import io.ikutsu.osumusic.core.presentation.theme.OM_ShapeMedium
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold
import io.ikutsu.osumusic.core.presentation.util.noRippleClickable
import io.ikutsu.osumusic.core.presentation.util.sp

@Composable
fun SearchBar(
    textFieldValue: String,
    onTextFieldValueChange: (String) -> Unit,
    hintText: String,
    enableClearButton: Boolean = false,
    onClearClick: () -> Unit = {},
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(OM_ShapeMedium)
            .background(OM_Primary)
            .fillMaxWidth()
            .height(48.dp)
            .padding(start = 12.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = { onTextFieldValueChange(it) },
            modifier = Modifier.weight(1f),
            maxLines = 1,
            cursorBrush = SolidColor(Color.White),
            textStyle = TextStyle(
                color = Color.White,
                fontFamily = OM_SemiBold,
                fontSize = 16.dp.sp
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClick()
                }
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (textFieldValue.isEmpty()) {
                        Text(
                            text = hintText,
                            modifier = Modifier
                                .alpha(0.5f),
                            fontFamily = OM_SemiBold,
                            fontSize = 16.dp.sp,
                            style = TextStyle(
                                lineHeightStyle = LineHeightStyle(
                                    trim = LineHeightStyle.Trim.Both,
                                    alignment = LineHeightStyle.Alignment.Center
                                )
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
        if (enableClearButton) {
            Icon(
                imageVector = OMIcon.Close,
                contentDescription = "Clear",
                modifier = Modifier
                    .alpha(if (textFieldValue.isNotBlank()) 1f else 0f)
                    .clip(OM_ShapeMedium)
                    .background(OM_Primary)
                    .padding(12.dp)
                    .size(24.dp)
                    .noRippleClickable {
                        onClearClick()
                    }
            )
        }
        OMIconButton(
            onClick = { onSearchClick() },
            vector = OMIcon.Search,
            contentDescription = "Search",
            containerSize = 40.dp
        )
    }
}