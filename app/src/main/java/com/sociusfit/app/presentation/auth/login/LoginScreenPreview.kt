package com.sociusfit.app.presentation.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.sociusfit.app.presentation.theme.SociusFitTheme

/**
 * Preview della LoginScreen
 */
@Preview(
    name = "Login Screen - Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    SociusFitTheme {
        LoginScreen(navController = rememberNavController())
    }
}

/**
 * Preview della LoginScreen in modalità scura
 */
@Preview(
    name = "Login Screen - Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LoginScreenDarkPreview() {
    SociusFitTheme {
        LoginScreen(navController = rememberNavController())
    }
}