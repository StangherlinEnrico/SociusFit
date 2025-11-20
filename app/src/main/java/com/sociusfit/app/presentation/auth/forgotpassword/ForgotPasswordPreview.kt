package com.sociusfit.app.presentation.auth.forgotpassword

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.sociusfit.app.presentation.theme.SociusFitTheme

/**
 * Preview della LoginScreen
 */
@Preview(
    name = "Forgot Password Screen - Light Mode",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ForgotPasswordScreenPreview() {
    SociusFitTheme {
        ForgotPasswordScreen(navController = rememberNavController())
    }
}

/**
 * Preview della LoginScreen in modalità scura
 */
@Preview(
    name = "Forgot Password Screen - Dark Mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ForgotPasswordScreenDarkPreview() {
    SociusFitTheme {
        ForgotPasswordScreen(navController = rememberNavController())
    }
}