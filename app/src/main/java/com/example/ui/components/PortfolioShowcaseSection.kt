package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ZoomIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.MockupFrameType
import com.example.data.model.PortfolioCatalog
import com.example.data.model.PortfolioCategory
import com.example.data.model.PortfolioProject
import com.example.ui.theme.LocalPortfolioColors

@Composable
fun PortfolioShowcaseSection(
    selectedCategory: PortfolioCategory,
    onSelectCategory: (PortfolioCategory) -> Unit,
    onProjectClick: (PortfolioProject) -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = LocalPortfolioColors.current
    val filteredProjects = if (selectedCategory == PortfolioCategory.ALL) {
        PortfolioCatalog.projects
    } else {
        PortfolioCatalog.projects.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 52.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.widthIn(max = 760.dp)) {
            SectionHeader(
                eyebrow = "PORTFOLIO SHOWCASE",
                title = "Selected Original Works",
                subtitle = "26 original logos, brand identity boards, websites, mobile UI/UX, and social media designs presented in clean 2D mockups."
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Category Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PortfolioCategory.entries.forEach { category ->
                    val count = if (category == PortfolioCategory.ALL) {
                        PortfolioCatalog.projects.size
                    } else {
                        PortfolioCatalog.projects.count { it.category == category }
                    }
                    val isSelected = category == selectedCategory
                    val bg by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else ext.elevatedSurface,
                        animationSpec = tween(200),
                        label = "catBg"
                    )
                    val fg by animateColorAsState(
                        targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground,
                        animationSpec = tween(200),
                        label = "catFg"
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(bg)
                            .border(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else ext.borderColor,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectCategory(category) }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .testTag("filter_${category.name.lowercase()}")
                    ) {
                        Text(
                            text = "${category.label} ($count)",
                            style = MaterialTheme.typography.labelLarge,
                            color = fg
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2D Mockup Cards List (2-column responsive layout)
            val rows = filteredProjects.chunked(2)
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                rows.forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        pair.forEach { project ->
                            Portfolio2DMockupCard(
                                project = project,
                                onClick = { onProjectClick(project) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Portfolio2DMockupCard(
    project: PortfolioProject,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = LocalPortfolioColors.current
    Surface(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, ext.borderColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("project_card_${project.id}"),
        color = ext.cardBackground,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Pure 2D Mockup Presentation Container (no 3D transforms)
            Portfolio2DMockupFrame(
                project = project,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.95f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = project.categoryLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.ZoomIn,
                    contentDescription = "View larger mockup",
                    tint = ext.secondaryText,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = project.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = project.clientOrBrand,
                style = MaterialTheme.typography.bodySmall,
                color = ext.secondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun Portfolio2DMockupFrame(
    project: PortfolioProject,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false
) {
    val ext = LocalPortfolioColors.current
    val frameBg = if (ext.isDark) Color(0xFF0E182B) else Color(0xFFEDF1F7)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(frameBg)
            .padding(if (isExpanded) 14.dp else 10.dp),
        contentAlignment = Alignment.Center
    ) {
        when (project.mockupType) {
            MockupFrameType.WEBSITE_BROWSER -> {
                // Clean 2D Browser Chrome Frame
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(4.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (ext.isDark) Color(0xFF17253D) else Color.White)
                        .border(1.dp, ext.borderColor, RoundedCornerShape(10.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (ext.isDark) Color(0xFF1E2F4D) else Color(0xFFF1F5F9))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                        Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                        Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF10B981)))
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(ext.borderColor.copy(alpha = 0.6f))
                        )
                    }
                    Image(
                        painter = painterResource(id = project.drawableRes),
                        contentDescription = project.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.03f))
                    )
                }
            }

            MockupFrameType.MOBILE_UI -> {
                // Clean 2D Mobile Device Mat Frame
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (ext.isDark) Color(0xFF1B2B45) else Color(0xFF1E293B))
                        .padding(6.dp)
                ) {
                    Image(
                        painter = painterResource(id = project.drawableRes),
                        contentDescription = project.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                    )
                }
            }

            MockupFrameType.LOGO_STUDIO,
            MockupFrameType.BRAND_BOARD,
            MockupFrameType.POSTER_FRAME -> {
                // Clean 2D Gallery Mat & Shadow Frame preserving original colors/logos
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(4.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (ext.isDark) Color(0xFF142138) else Color.White)
                        .border(1.dp, ext.borderColor, RoundedCornerShape(10.dp))
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = project.drawableRes),
                        contentDescription = project.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(6.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectMockupDetailDialog(
    project: PortfolioProject,
    onDismiss: () -> Unit
) {
    val ext = LocalPortfolioColors.current
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .widthIn(max = 680.dp),
            shape = RoundedCornerShape(24.dp),
            color = ext.cardBackground,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                    ) {
                        Text(
                            text = project.mockupType.badge,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close project preview",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Portfolio2DMockupFrame(
                    project = project,
                    isExpanded = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = project.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${project.categoryLabel} • ${project.clientOrBrand}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = project.summary,
                    style = MaterialTheme.typography.bodyLarge,
                    color = ext.secondaryText
                )
            }
        }
    }
}
