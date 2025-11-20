package com.sociusfit.app.presentation.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.sociusfit.app.presentation.auth.register.RegisterScreen
import com.sociusfit.app.presentation.theme.SociusFitTheme

/**
 * Preview della RegisterScreen
 */
@Preview(
    name = "Register Screen - Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RegisterScreenPreview() {
    SociusFitTheme {
        RegisterScreen(navController = rememberNavController())
    }
}

/**
 * Preview della RegisterScreen in modalità scura
 */
@Preview(
    name = "Register Screen - Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RegisterScreenDarkPreview() {
    SociusFitTheme {
        RegisterScreen(navController = rememberNavController())
    }
}