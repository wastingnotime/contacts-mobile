package org.wastingnotime.contactsmobile.interfaces.theme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class ContactsThemeTest {
    @Test
    fun `resolves the light static palette when dark mode is off`() {
        val scheme = resolveContactsColorScheme(
            darkTheme = false,
            dynamicColor = false,
        )

        assertEquals(Color(0xFF22577A), scheme.primary)
        assertEquals(Color(0xFFF7FBFC), scheme.background)
        assertEquals(Color(0xFF181C1D), scheme.onBackground)
        assertEquals(Color(0xFFF7FBFC), scheme.surface)
    }

    @Test
    fun `resolves the dark static palette when dark mode is on`() {
        val scheme = resolveContactsColorScheme(
            darkTheme = true,
            dynamicColor = false,
        )

        assertEquals(Color(0xFF8CCBE6), scheme.primary)
        assertEquals(Color(0xFF101415), scheme.background)
        assertEquals(Color(0xFFE0E3E4), scheme.onBackground)
        assertEquals(Color(0xFF101415), scheme.surface)
    }
}
