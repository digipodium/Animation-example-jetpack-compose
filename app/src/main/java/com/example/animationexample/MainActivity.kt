package com.example.animationexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animationexample.ui.theme.AnimationExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimationExampleOne()
                }
            }
        }
    }
}

enum class BoxScale {
    Small,
    Large
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimationExampleOne() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        var boxScale by remember { mutableStateOf(BoxScale.Small) }
        var isOpen by remember { mutableStateOf(false) }
        val animateScale by animateFloatAsState(
            targetValue = when (boxScale) {
                BoxScale.Small -> .5f
                BoxScale.Large -> 2f
            },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy
            ),
            label = "scale box"
        )

        val angle by animateFloatAsState(
            targetValue = if (isOpen) 360f else 0f,
            label = "rotations",
            animationSpec = tween(
                durationMillis = 1000,
                easing = EaseInCubic
            )
        )
        Box(
            modifier = Modifier
                .padding(24.dp)
                .size(100.dp)
                .align(Alignment.TopCenter)
                .drawBehind {
                    drawArc(
                        color = Color.Green,
                        startAngle = -90f,
                        sweepAngle = angle,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx())
                    )
                }
        ) {
            Text(
                text = "${angle.toInt()}",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 24.sp,
            )
        }


        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
                .scale(animateScale)
                .background(Color.Red, shape = CircleShape)
                .clip(CircleShape)
                .clickable {
                    boxScale = when (boxScale) {
                        BoxScale.Small -> BoxScale.Large
                        BoxScale.Large -> BoxScale.Small
                    }
                }
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Button(
                onClick = { isOpen = !isOpen }
            ) {
                Text(
                    text = "Toggle Information",
                    fontFamily = FontFamily(Font(R.font.notosans_italic))
                )
            }
            AnimatedVisibility(
                visible = isOpen,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 1500)
                ) + expandIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                exit = shrinkHorizontally()
            ) {
                Text(text = "Use AnimatedVisibility to hide or show a Composable. Children inside AnimatedVisibility can use Modifier.animateEnterExit() for their own enter or exit transition.")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    AnimationExampleTheme {
        AnimationExampleOne()
    }
}