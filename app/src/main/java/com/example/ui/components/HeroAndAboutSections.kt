package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LocalPortfolioColors
import kotlinx.coroutines.delay

@Composable
fun HeroSection(
    onExplorePortfolio: () -> Unit,
    onApplyTeam: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = LocalPortfolioColors.current
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(80)
        visible = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "heroGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.14f,
        targetValue = 0.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        ext.sectionBackground,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = 760.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(550)) + slideInVertically(tween(550)) { 36 }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        modifier = Modifier.border(
                            1.dp,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.28f),
                            RoundedCornerShape(50)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Verified,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PERSONAL PORTFOLIO & DIGITAL EDUCATION",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Awais Akram Khan",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("hero_name")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Founder & Teacher",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("hero_title")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Exact institute name placed directly underneath "Founder & Teacher"
                    Text(
                        text = "AL AWAIS ENTERPRISES & INSTITUTE OF COLLEGE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 1.4.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = ext.secondaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .testTag("hero_institute_name")
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = "Teaching practical digital skills and crafting modern graphic design, brand identities, websites, and UI/UX experiences.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ext.secondaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(max = 580.dp)
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onExplorePortfolio,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .height(50.dp)
                                .testTag("hero_explore_button")
                        ) {
                            Text("Explore Portfolio", style = MaterialTheme.typography.labelLarge)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = onApplyTeam,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .height(50.dp)
                                .testTag("hero_apply_button")
                        ) {
                            Text(
                                text = "Join Our Team",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Hero Personal Portrait Frame (Read-only, zero upload/edit controls)
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(750, delayMillis = 160)) + slideInVertically(tween(750, delayMillis = 160)) { 48 }
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(max = 360.dp)
                        .fillMaxWidth()
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(28.dp),
                            ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha),
                            spotColor = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha)
                        )
                        .clip(RoundedCornerShape(28.dp))
                        .background(ext.cardBackground)
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.65f),
                                    ext.borderColor,
                                    ext.secondaryAccent.copy(alpha = 0.45f)
                                )
                            ),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .padding(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.awais_profile),
                        contentDescription = "Portrait photograph of Awais Akram Khan",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.74f)
                            .clip(RoundedCornerShape(20.dp))
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutAndJourneySections(modifier: Modifier = Modifier) {
    val ext = LocalPortfolioColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ext.sectionBackground)
            .padding(horizontal = 20.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.widthIn(max = 760.dp)) {
            // Section 2: Personal Introduction & About Me (Fixed, Read-Only)
            SectionHeader(
                eyebrow = "ABOUT ME",
                title = "Personal Introduction & Mission",
                subtitle = "Dedicated to digital education, creative craft, and empowering learners."
            )

            Spacer(modifier = Modifier.height(22.dp))

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = ext.cardBackground,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ext.borderColor, RoundedCornerShape(24.dp))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "I am Awais Akram Khan, Founder & Teacher at AL AWAIS ENTERPRISES & INSTITUTE OF COLLEGE. My primary focus is teaching different digital skills and mentoring students to build real-world creative and technical confidence.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Across graphic designing, brand identity systems, web design, UI/UX interfaces, and modern AI workflows, I believe great education bridges clear visual thinking with practical execution.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ext.secondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Section 3: My Digital Teaching Journey & Creative Focus
            SectionHeader(
                eyebrow = "TEACHING & CREATIVE FOCUS",
                title = "My Digital Teaching Journey",
                subtitle = "Structured hands-on guidance across visual design and modern digital tools."
            )

            Spacer(modifier = Modifier.height(22.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 2
            ) {
                JourneyPillarCard(
                    icon = Icons.Rounded.Brush,
                    title = "Graphic & Brand Design",
                    description = "Teaching logo construction, visual brand systems, packaging, and social media campaign layout.",
                    modifier = Modifier.weight(1f, fill = true).widthIn(min = 260.dp)
                )
                JourneyPillarCard(
                    icon = Icons.Rounded.Devices,
                    title = "Web & UI/UX Design",
                    description = "Crafting responsive e-commerce websites, clean mobile app interfaces, and user-centered prototypes.",
                    modifier = Modifier.weight(1f, fill = true).widthIn(min = 260.dp)
                )
                JourneyPillarCard(
                    icon = Icons.Rounded.AutoAwesome,
                    title = "AI & Modern Productivity",
                    description = "Helping students adopt Artificial Intelligence, Microsoft Office, and Database fundamentals effectively.",
                    modifier = Modifier.weight(1f, fill = true).widthIn(min = 260.dp)
                )
                JourneyPillarCard(
                    icon = Icons.Rounded.School,
                    title = "Mentorship & Leadership",
                    description = "Leading AL AWAIS ENTERPRISES & INSTITUTE OF COLLEGE with practical studio-style mentorship.",
                    modifier = Modifier.weight(1f, fill = true).widthIn(min = 260.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Section 4: Personal Profile & Photograph
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = ext.cardBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ext.borderColor, RoundedCornerShape(24.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_photo),
                        contentDescription = "Awais Akram Khan personal profile photograph",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Awais Akram Khan",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Founder & Teacher",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "AL AWAIS ENTERPRISES & INSTITUTE OF COLLEGE",
                            style = MaterialTheme.typography.bodySmall,
                            color = ext.secondaryText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JourneyPillarCard(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    val ext = LocalPortfolioColors.current
    Surface(
        modifier = modifier.border(1.dp, ext.borderColor, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = ext.cardBackground,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = ext.secondaryText
            )
        }
    }
}

@Composable
fun SectionHeader(
    eyebrow: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val ext = LocalPortfolioColors.current
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = eyebrow,
            style = MaterialTheme.typography.labelMedium.copy(
                letterSpacing = 1.4.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = ext.secondaryText
        )
    }
}
