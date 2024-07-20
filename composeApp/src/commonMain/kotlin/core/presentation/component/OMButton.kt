package io.ikutsu.osumusic.core.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ikutsu.osumusic.core.presentation.theme.OM_Container
import io.ikutsu.osumusic.core.presentation.util.OM_SemiBold

@Composable
fun OMButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(100),
        colors = ButtonDefaults.buttonColors(
            containerColor = OM_Container
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            fontFamily = OM_SemiBold,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}