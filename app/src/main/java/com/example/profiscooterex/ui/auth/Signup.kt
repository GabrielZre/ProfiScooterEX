package com.example.profiscooterex.ui.auth

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profiscooterex.R
import com.example.profiscooterex.data.Resource
import com.example.profiscooterex.navigation.AuthNavGraph
import com.example.profiscooterex.ui.destinations.HomeScreenDestination
import com.example.profiscooterex.ui.destinations.LoginScreenDestination
import com.example.profiscooterex.ui.theme.AppTheme
import com.example.profiscooterex.ui.theme.DarkColor
import com.example.profiscooterex.ui.theme.DarkGradient
import com.example.profiscooterex.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@AuthNavGraph
@Destination
@Composable
fun SignupScreen(viewModel: AuthViewModel? = hiltViewModel(), navigator: DestinationsNavigator) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val signupFlow = viewModel?.signupFlow?.collectAsState()

    ConstraintLayout(modifier = Modifier.fillMaxSize().background(DarkGradient)) {
        val (
            refHeader,
            refName,
            refEmail,
            refPassword,
            refButtonSignup,
            refTextSignup,
            refLoader,
            refTextRegister) =
            createRefs()
        val spacing = MaterialTheme.spacing

        Box(
            modifier =
                Modifier.constrainAs(refHeader) {
                        top.linkTo(parent.top, spacing.extraLarge)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .wrapContentSize()
        ) {
            AuthHeader()
        }

        Text(
            text = "Register: ",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier =
                Modifier.constrainAs(refTextRegister) {
                    bottom.linkTo(refName.bottom, spacing.extraLarge)
                    start.linkTo(parent.start, spacing.large)
                    end.linkTo(parent.end, spacing.large)
                    width = Dimension.fillToConstraints
                },
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = stringResource(id = R.string.name)) },
            modifier =
                Modifier.constrainAs(refName) {
                    top.linkTo(refHeader.bottom, spacing.extraLarge)
                    start.linkTo(parent.start, spacing.large)
                    end.linkTo(parent.end, spacing.large)
                    width = Dimension.fillToConstraints
                },
            keyboardOptions =
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = stringResource(id = R.string.email)) },
            modifier =
                Modifier.constrainAs(refEmail) {
                    top.linkTo(refName.bottom, spacing.medium)
                    start.linkTo(parent.start, spacing.large)
                    end.linkTo(parent.end, spacing.large)
                    width = Dimension.fillToConstraints
                },
            keyboardOptions =
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = stringResource(id = R.string.password)) },
            modifier =
                Modifier.constrainAs(refPassword) {
                    top.linkTo(refEmail.bottom, spacing.medium)
                    start.linkTo(parent.start, spacing.large)
                    end.linkTo(parent.end, spacing.large)
                    width = Dimension.fillToConstraints
                },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions =
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
        )

        OutlinedButton(
            onClick = { viewModel?.signup(name, email, password) },
            modifier =
                Modifier.constrainAs(refButtonSignup) {
                    top.linkTo(refPassword.bottom, spacing.large)
                    start.linkTo(parent.start, spacing.extraLarge)
                    end.linkTo(parent.end, spacing.extraLarge)
                    width = Dimension.fillToConstraints
                },
            colors = ButtonDefaults.buttonColors(DarkColor)
        ) {
            Text(
                text = stringResource(id = R.string.signup),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            modifier =
                Modifier.constrainAs(refTextSignup) {
                        top.linkTo(refButtonSignup.bottom, spacing.medium)
                        start.linkTo(parent.start, spacing.extraLarge)
                        end.linkTo(parent.end, spacing.extraLarge)
                    }
                    .clickable {
                        navigator.navigate(LoginScreenDestination) {
                            popUpTo(LoginScreenDestination.route) { inclusive = true }
                        }
                    },
            text = stringResource(id = R.string.already_have_account),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )

        signupFlow?.value?.let {
            when (it) {
                is Resource.Failure -> {
                    val context = LocalContext.current
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                }
                Resource.Loading -> {
                    CircularProgressIndicator(
                        modifier =
                            Modifier.constrainAs(refLoader) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                }
                is Resource.Success -> {
                    LaunchedEffect(Unit) {
                        /*navController.navigate(ROUTE_HOME){
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }*/
                        navigator.popBackStack()
                        navigator.navigate(HomeScreenDestination) {
                            popUpTo(HomeScreenDestination.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun SignupScreenPreviewLight() {
    AppTheme { SignupScreen(null, navigator = EmptyDestinationsNavigator) }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SignupScreenPreviewDark() {
    AppTheme { SignupScreen(null, navigator = EmptyDestinationsNavigator) }
}
