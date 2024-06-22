package com.example.multicalculator.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalcView()
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Welcome to the UI Calculator.")
    }
}

@Composable
fun CalcView() {
    // State variables using rememberSaveable
    var leftNumber by rememberSaveable { mutableStateOf(0) }
    var rightNumber by rememberSaveable { mutableStateOf(0) }
    var operation by rememberSaveable { mutableStateOf("") }
    var complete by rememberSaveable { mutableStateOf(false) }
    var displayText by rememberSaveable { mutableStateOf("0") }

    // If statement to check if complete is true and operation is not empty
    if (complete && operation.isNotEmpty()) {
        var answer = 0
        when (operation) {
            "+" -> answer = leftNumber + rightNumber
            "-" -> answer = leftNumber - rightNumber
            "*" -> answer = leftNumber * rightNumber
            "/" -> if (rightNumber != 0) answer = leftNumber / rightNumber
        }
        displayText = answer.toString()
    } else if (operation.isNotEmpty() && !complete) {
        // If operation exists but not complete, show rightNumber
        displayText = rightNumber.toString()
    } else {
        // Otherwise, show leftNumber
        displayText = leftNumber.toString()
    }

    // Function to handle number button presses
    fun numberPress(btnNum: Int) {
        if (complete) {
            // Reset state if calculation is complete
            leftNumber = 0
            rightNumber = 0
            operation = ""
            complete = false
        }
        if (operation.isNotEmpty() && !complete) {
            rightNumber = rightNumber * 10 + btnNum
        } else if (operation.isEmpty() && !complete) {
            leftNumber = leftNumber * 10 + btnNum
        }
    }

    // Function to handle operation button presses
    fun operationPress(op: String) {
        if (!complete) {
            operation = op
        }
    }

    // Function to handle equals button press
    fun equalsPress() {
        complete = true
    }

    // UI Layout
    Column(
        modifier = Modifier
            .background(Color.Yellow)
            .fillMaxSize()
    ) {
        Row {
            CalcDisplay(display = displayText)
        }
        Row {
            Column {
                for (i in 7 downTo 1 step 3) {
                    CalcRow(onPress = { numberPress(it) }, startNum = i, numButtons = 3)
                }
                Row {
                    CalcNumericButton(onPress = { numberPress(0) })
                    CalcEqualsButton(onPress = { equalsPress() })
                }
            }
            Column {
                CalcOperationButton(onPress = { operationPress("/") })
                CalcOperationButton(onPress = { operationPress("*") })
                CalcOperationButton(onPress = { operationPress("+") })
                CalcOperationButton(onPress = { operationPress("-") })
            }
        }
    }
}

@Composable
fun CalcDisplay(display: String) {
    Text(
        text = display,
        modifier = Modifier
            .height(45.dp)
            .padding(5.dp)
            .fillMaxWidth()
    )
}

@Composable
fun CalcRow(onPress: (Int) -> Unit, startNum: Int, numButtons: Int) {
    val endNum = startNum + numButtons
    Row(modifier = Modifier.padding(0.dp)) {
        for (i in startNum until endNum) {
            CalcNumericButton(onPress = { onPress(i) })
        }
    }
}

@Composable
fun CalcNumericButton(onPress: (Int) -> Unit) {
    Button(
        onClick = { onPress(0) },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = "0")
    }
}

@Composable
fun CalcOperationButton(onPress: (String) -> Unit) {
    Button(
        onClick = { onPress("/") },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = "/")
    }
}

@Composable
fun CalcEqualsButton(onPress: () -> Unit) {
    Button(
        onClick = { onPress() },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = "=")
    }
}
