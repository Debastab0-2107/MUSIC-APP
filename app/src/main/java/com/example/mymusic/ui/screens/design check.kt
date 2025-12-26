package com.example.mymusic.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class) // Needed for custom Thumb customization in M3
@Composable
fun CleanProfessionalSlider() {
    // 1. State for the slider value (0 to 100)
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    // 2. Define your professional colors
    val primaryColor = Color(0xFF0A5EB0) // Corporate Blue
    val trackColor = Color(0xFFE0E0E0)   // Light Gray for the inactive part

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        // Label to see the value
        Text(
            text = "Progress: ${sliderPosition.toInt()}%",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 3. The Custom Slider
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..100f,

            // Custom Colors for the Track
            colors = SliderDefaults.colors(
                thumbColor = Color.White,             // We handle thumb color manually below, but this is backup
                activeTrackColor = primaryColor,      // The "Progress" part
                inactiveTrackColor = trackColor,      // The "Remaining" part
                activeTickColor = Color.Transparent,  // Hide ticks
                inactiveTickColor = Color.Transparent
            ),

            // Customizing the Thumb (The "Knot")
            thumb = {
                // This Box creates the clean white circle with a border
                Box(
                    modifier = Modifier
                        .size(24.dp) // Control the size of the knot here
                        .shadow(4.dp, shape = CircleShape) // Add subtle shadow for depth
                        .background(Color.White, CircleShape)
                        .border(2.dp, primaryColor, CircleShape) // Blue ring around white circle
                )
            },

            // Customizing the Track (Making it a thin straight line)
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(4.dp), // Make the line thinner (standard is usually thicker)
                    colors = SliderDefaults.colors(
                        activeTrackColor = primaryColor,
                        inactiveTrackColor = trackColor
                    ),
                    thumbTrackGapSize = 0.dp // Removes the gap between thumb and track
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSlider() {
    CleanProfessionalSlider()
}

/*
ANIMATIONS FOR ICONS LIKE INBETWEEN

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AnimationGallery() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Icon Animation Gallery", style = MaterialTheme.typography.headlineMedium)

        // 1. The "Spotify Style" Vertical Slide
        AnimationRow(label = "1. Vertical Slide (Play/Pause)") {
            SlideAnimationBtn()
        }

        // 2. The "Instagram Style" Pop/Scale
        AnimationRow(label = "2. Scale Pop (Favorite)") {
            ScaleAnimationBtn()
        }

        // 3. The 360 Spin
        AnimationRow(label = "3. 360 Spin (Refresh)") {
            RotateAnimationBtn()
        }

        // 4. The Elastic Shake (Notification)
        AnimationRow(label = "4. Elastic Shake (Notify)") {
            ShakeAnimationBtn()
        }
    }
}

// ---------------------------------------------------------
// 1. SLIDE ANIMATION (Vertical)
// Great for: Play/Pause, Up/Down arrows
// ---------------------------------------------------------
@Composable
fun SlideAnimationBtn() {
    var isPlaying by remember { mutableStateOf(false) }

    IconButton(onClick = { isPlaying = !isPlaying }) {
        AnimatedContent(
            targetState = isPlaying,
            label = "Slide",
            transitionSpec = {
                // If switching to playing: Slide UP in, Slide UP out
                if (targetState) {
                    (slideInVertically { height -> height } + fadeIn()) togetherWith
                    (slideOutVertically { height -> -height } + fadeOut())
                } else {
                    // If switching to pause: Slide DOWN in, Slide DOWN out
                    (slideInVertically { height -> -height } + fadeIn()) togetherWith
                    (slideOutVertically { height -> height } + fadeOut())
                }
            }
        ) { playing ->
            Icon(
                imageVector = if (playing) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color.Black
            )
        }
    }
}

// ---------------------------------------------------------
// 2. SCALE / POP ANIMATION
// Great for: Likes, Favorites, Add to Cart
// ---------------------------------------------------------
@Composable
fun ScaleAnimationBtn() {
    var isFav by remember { mutableStateOf(false) }

    IconButton(onClick = { isFav = !isFav }) {
        AnimatedContent(
            targetState = isFav,
            label = "Scale",
            transitionSpec = {
                // Grow from 0% to 100% with a spring bounce
                (scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn()) togetherWith
                (scaleOut(targetScale = 0f) + fadeOut())
            }
        ) { fav ->
            Icon(
                imageVector = if (fav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = if (fav) Color.Red else Color.Gray
            )
        }
    }
}

// ---------------------------------------------------------
// 3. ROTATE ANIMATION (360 Spin)
// Great for: Refresh, Sync, Settings
// ---------------------------------------------------------
@Composable
fun RotateAnimationBtn() {
    var isRotated by remember { mutableStateOf(false) }

    // animateFloatAsState handles the rotation value
    val angle by animateFloatAsState(
        targetValue = if (isRotated) 360f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow), // Slow spin
        label = "Spin"
    )

    IconButton(onClick = { isRotated = !isRotated }) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .rotate(angle), // Apply the rotation here
            tint = Color(0xFF009688)
        )
    }
}

// ---------------------------------------------------------
// 4. SHAKE ANIMATION (Elastic)
// Great for: Errors, Bells, Alerts
// ---------------------------------------------------------
@Composable
fun ShakeAnimationBtn() {
    var isShaking by remember { mutableStateOf(false) }

    // We use an Animatable for complex keyframes
    val offsetX = remember { Animatable(0f) }

    // Trigger the shake when isShaking becomes true
    LaunchedEffect(isShaking) {
        if (isShaking) {
            // Keyframes define the path of the shake
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 400
                    0f at 0
                    (-10f) at 50 // Move left
                    10f at 100  // Move right
                    (-10f) at 150
                    5f at 200
                    (-5f) at 250
                    0f at 400
                }
            )
            isShaking = false // Reset state
        }
    }

    IconButton(onClick = { isShaking = true }) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .offset(x = offsetX.value.dp), // Apply the offset here
            tint = Color(0xFFFF9800)
        )
    }
}

// Helper for Layout
@Composable
fun AnimationRow(label: String, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), CircleShape)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryPreview() {
    MaterialTheme {
        AnimationGallery()
    }
}


 */