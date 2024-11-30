package com.example.arimagame

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val TRAP_SQUARES = listOf(
    Pair(2, 2), Pair(2, 5),
    Pair(5, 2), Pair(5, 5)
)

val INITIAL_PIECES = listOf(
    "Rabbit", "Rabbit", "Rabbit", "Rabbit", "Rabbit", "Rabbit", "Rabbit", "Rabbit",
    "Cat", "Dog", "Horse", "Camel", "Elephant", "Horse", "Dog", "Cat"
)


