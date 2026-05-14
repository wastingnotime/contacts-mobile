package org.wastingnotime.contactsmobile.interfaces.theme

import android.os.Build
import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext

private val ContactsLightColorScheme = lightColorScheme(
    primary = Color(0xFF22577A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCAE9F5),
    onPrimaryContainer = Color(0xFF0B1D27),
    secondary = Color(0xFF4A6A6A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCDE4E0),
    onSecondaryContainer = Color(0xFF102020),
    tertiary = Color(0xFF7B4F27),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF8D8BD),
    onTertiaryContainer = Color(0xFF2B1707),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF7FBFC),
    onBackground = Color(0xFF181C1D),
    surface = Color(0xFFF7FBFC),
    onSurface = Color(0xFF181C1D),
    surfaceVariant = Color(0xFFDDE3E3),
    onSurfaceVariant = Color(0xFF414848),
    outline = Color(0xFF727878),
    outlineVariant = Color(0xFFC1C7C7),
)

private val ContactsDarkColorScheme = darkColorScheme(
    primary = Color(0xFF8CCBE6),
    onPrimary = Color(0xFF003547),
    primaryContainer = Color(0xFF004D66),
    onPrimaryContainer = Color(0xFFCDE9F6),
    secondary = Color(0xFFB1CDC8),
    onSecondary = Color(0xFF1B3532),
    secondaryContainer = Color(0xFF324B48),
    onSecondaryContainer = Color(0xFFCDE4E0),
    tertiary = Color(0xFFF5BA86),
    onTertiary = Color(0xFF472700),
    tertiaryContainer = Color(0xFF5F3713),
    onTertiaryContainer = Color(0xFFF8D8BD),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF101415),
    onBackground = Color(0xFFE0E3E4),
    surface = Color(0xFF101415),
    onSurface = Color(0xFFE0E3E4),
    surfaceVariant = Color(0xFF414848),
    onSurfaceVariant = Color(0xFFC1C7C7),
    outline = Color(0xFF8B9191),
    outlineVariant = Color(0xFF414848),
)

@Composable
fun ContactsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = resolveContactsColorScheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        context = context,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content,
    )
}

internal fun resolveContactsColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    context: Context? = null,
): ColorScheme {
    return when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val resolvedContext = requireNotNull(context) {
                "Context is required when dynamicColor is enabled on supported Android versions."
            }
            if (darkTheme) dynamicDarkColorScheme(resolvedContext) else dynamicLightColorScheme(resolvedContext)
        }
        darkTheme -> ContactsDarkColorScheme
        else -> ContactsLightColorScheme
    }
}
