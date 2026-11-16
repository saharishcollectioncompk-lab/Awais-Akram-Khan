package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.data.model.JobApplication
import com.example.data.model.PortfolioCatalog
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.LocalPortfolioColors
import com.example.ui.theme.SuccessEmerald
import com.example.ui.viewmodel.JobApplicationFormState

fun openWhatsAppBusiness(context: Context) {
    val waUrl = "https://wa.me/923097309810"
    val businessIntent = Intent(Intent.ACTION_VIEW, Uri.parse(waUrl)).apply {
        setPackage("com.whatsapp.w4b")
    }
    try {
        context.startActivity(businessIntent)
    } catch (_: Exception) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(waUrl)))
        } catch (_: Exception) {
        }
    }
}

fun openEmailClient(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:awaisakramkhan77@gmail.com")
    }
    try {
        context.startActivity(intent)
    } catch (_: Exception) {
    }
}

fun openSocialUrl(context: Context, url: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (_: Exception) {
    }
}

@Composable
fun JobApplicationSection(
    formState: JobApplicationFormState,
    submittedApplications: List<JobApplication>,
    isUserSignedIn: Boolean,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onMobileChange: (String) -> Unit,
    onCnicChange: (String) -> Unit,
    onSelectPosition: (String) -> Unit,
    onSelectCourse: (String) -> Unit,
    onCustomCourseChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onOpenSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = LocalPortfolioColors.current
    var positionMenuExpanded by remember { mutableStateOf(false) }
    var courseMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ext.sectionBackground)
            .padding(horizontal = 20.dp, vertical = 52.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.widthIn(max = 760.dp)) {
            Text(
                text = "CAREERS & TEACHING TEAM",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Job Application – Awais Akram Khan",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.testTag("job_application_heading")
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Apply to Join Our Institute Team",
                style = MaterialTheme.typography.titleLarge,
                color = ext.secondaryText
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = ext.cardBackground,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ext.borderColor, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (!isUserSignedIn) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                                    RoundedCornerShape(14.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Sign in with Google to submit & track your private application.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                OutlinedButton(
                                    onClick = onOpenSignIn,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("job_sign_in_prompt_button")
                                ) {
                                    Text("Sign In")
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = formState.fullName,
                        onValueChange = onFullNameChange,
                        label = { Text("Full Name") },
                        placeholder = { Text("Enter your full name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("job_full_name_input")
                    )

                    OutlinedTextField(
                        value = formState.email,
                        onValueChange = onEmailChange,
                        label = { Text("Gmail / Email Address") },
                        placeholder = { Text("example@gmail.com") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("job_email_input")
                    )

                    OutlinedTextField(
                        value = formState.mobileNumber,
                        onValueChange = onMobileChange,
                        label = { Text("Mobile Number") },
                        placeholder = { Text("03XX-XXXXXXX") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("job_mobile_input")
                    )

                    OutlinedTextField(
                        value = formState.cnicNumber,
                        onValueChange = onCnicChange,
                        label = { Text("CNIC Number") },
                        placeholder = { Text("35202-1234567-1") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("job_cnic_input")
                    )

                    // Privacy Notice for sensitive CNIC handling
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ext.elevatedSurface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Privacy Notice: Your personal details and CNIC are collected strictly for recruitment review at AL AWAIS ENTERPRISES & INSTITUTE OF COLLEGE, protected securely, and never displayed publicly.",
                                style = MaterialTheme.typography.bodySmall,
                                color = ext.secondaryText
                            )
                        }
                    }

                    // Dropdown 1: Select Your Job Position (Teacher, Manager)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = formState.selectedPosition,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Your Job Position") },
                            placeholder = { Text("Choose Teacher or Manager") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = "Select job position"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("job_position_dropdown")
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { positionMenuExpanded = true }
                        )
                        DropdownMenu(
                            expanded = positionMenuExpanded,
                            onDismissRequest = { positionMenuExpanded = false }
                        ) {
                            PortfolioCatalog.jobPositions.forEach { position ->
                                DropdownMenuItem(
                                    text = { Text(position) },
                                    onClick = {
                                        onSelectPosition(position)
                                        positionMenuExpanded = false
                                    },
                                    modifier = Modifier.testTag("position_option_${position.lowercase()}")
                                )
                            }
                        }
                    }

                    // Dropdown 2: Select Your Course / Subject
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = formState.selectedCourse,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Your Course / Subject") },
                            placeholder = { Text("Choose the subject you can teach") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = "Select course or subject"
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("job_course_dropdown")
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { courseMenuExpanded = true }
                        )
                        DropdownMenu(
                            expanded = courseMenuExpanded,
                            onDismissRequest = { courseMenuExpanded = false }
                        ) {
                            PortfolioCatalog.jobCourses.forEach { course ->
                                DropdownMenuItem(
                                    text = { Text(course) },
                                    onClick = {
                                        onSelectCourse(course)
                                        courseMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = formState.selectedCourse == "Other") {
                        OutlinedTextField(
                            value = formState.customCourse,
                            onValueChange = onCustomCourseChange,
                            label = { Text("Enter Course / Subject Name") },
                            placeholder = { Text("Specify the course you learned") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("job_custom_course_input")
                        )
                    }

                    if (formState.errorMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ErrorCrimson.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ErrorOutline,
                                    contentDescription = null,
                                    tint = ErrorCrimson,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = formState.errorMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ErrorCrimson
                                )
                            }
                        }
                    }

                    if (formState.successMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SuccessEmerald.copy(alpha = 0.14f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = SuccessEmerald,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = formState.successMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }

                    Button(
                        onClick = onSubmit,
                        enabled = !formState.isSubmitting,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("apply_now_button")
                    ) {
                        if (formState.isSubmitting) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Send,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Apply Now", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            if (isUserSignedIn && submittedApplications.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ext.cardBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ext.borderColor, RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Your Submitted Applications",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        submittedApplications.forEach { app ->
                            val courseDisplay = if (app.course == "Other" && !app.customCourse.isNullOrBlank()) {
                                "Other (${app.customCourse})"
                            } else {
                                app.course
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ext.elevatedSurface)
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${app.position} — $courseDisplay",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = "CNIC: ${app.cnicMasked} • Status: ${app.status}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ext.secondaryText
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = "Submitted",
                                    tint = SuccessEmerald,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContactInformationSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val ext = LocalPortfolioColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 52.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.widthIn(max = 760.dp)) {
            SectionHeader(
                eyebrow = "CONTACT INFORMATION",
                title = "Get in Touch Directly",
                subtitle = "Tap the phone number to message on WhatsApp Business or tap Gmail to compose an email."
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = ext.cardBackground,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ext.borderColor, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Clickable Phone Number -> opens WhatsApp Business directly (no copy icon, no extra button)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { openWhatsAppBusiness(context) }
                            .padding(vertical = 10.dp, horizontal = 8.dp)
                            .testTag("contact_phone_clickable"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Phone,
                                contentDescription = "Phone number",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Phone Number",
                                style = MaterialTheme.typography.labelMedium,
                                color = ext.secondaryText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "+309-730-9810",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    textDecoration = TextDecoration.Underline
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    HorizontalDivider(color = ext.borderColor)

                    // Clickable Gmail -> opens Email client directly (no copy icon)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { openEmailClient(context) }
                            .padding(vertical = 10.dp, horizontal = 8.dp)
                            .testTag("contact_gmail_clickable"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Email,
                                contentDescription = "Gmail address",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Gmail Address",
                                style = MaterialTheme.typography.labelMedium,
                                color = ext.secondaryText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "awaisakramkhan77@gmail.com",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    textDecoration = TextDecoration.Underline
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PortfolioFooter(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val ext = LocalPortfolioColors.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = ext.sectionBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(color = ext.borderColor)
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 760.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Awais Akram Khan",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Founder & Teacher",
                        style = MaterialTheme.typography.bodySmall,
                        color = ext.secondaryText
                    )
                    Text(
                        text = "AL AWAIS ENTERPRISES & INSTITUTE OF COLLEGE",
                        style = MaterialTheme.typography.bodySmall,
                        color = ext.secondaryText
                    )
                }

                // All Social Media Icons grouped together at the bottom-right corner
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.testTag("footer_social_row")
                ) {
                    SocialBrandButton(
                        contentDesc = "TikTok @neekha11",
                        backgroundColor = if (ext.isDark) Color(0xFF0F172A) else Color(0xFF111827),
                        onClick = { openSocialUrl(context, "https://www.tiktok.com/@neekha11") },
                        testTag = "social_tiktok_button"
                    ) {
                        // Clean TikTok musical-note glyph
                        Canvas(modifier = Modifier.size(20.dp)) {
                            val w = size.width
                            val h = size.height
                            val path = Path().apply {
                                moveTo(w * 0.62f, h * 0.14f)
                                lineTo(w * 0.62f, h * 0.66f)
                                cubicTo(w * 0.62f, h * 0.80f, w * 0.50f, h * 0.88f, w * 0.36f, h * 0.86f)
                                cubicTo(w * 0.24f, h * 0.84f, w * 0.18f, h * 0.72f, w * 0.24f, h * 0.61f)
                                cubicTo(w * 0.29f, h * 0.52f, w * 0.40f, h * 0.50f, w * 0.48f, h * 0.54f)
                            }
                            drawPath(
                                path = path,
                                color = Color.White,
                                style = Stroke(width = w * 0.14f, cap = StrokeCap.Round)
                            )
                            val wing = Path().apply {
                                moveTo(w * 0.62f, h * 0.20f)
                                cubicTo(w * 0.67f, h * 0.33f, w * 0.76f, h * 0.38f, w * 0.86f, h * 0.39f)
                            }
                            drawPath(
                                path = wing,
                                color = Color(0xFF25F4EE),
                                style = Stroke(width = w * 0.13f, cap = StrokeCap.Round)
                            )
                        }
                    }

                    SocialBrandButton(
                        contentDesc = "Instagram @awais.ii_",
                        backgroundColor = Color(0xFFE1306C),
                        onClick = { openSocialUrl(context, "https://www.instagram.com/awais.ii_") },
                        testTag = "social_instagram_button"
                    ) {
                        Canvas(modifier = Modifier.size(20.dp)) {
                            val w = size.width
                            val h = size.height
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset(w * 0.12f, h * 0.12f),
                                size = Size(w * 0.76f, h * 0.76f),
                                cornerRadius = CornerRadius(w * 0.22f, w * 0.22f),
                                style = Stroke(width = w * 0.11f)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = w * 0.19f,
                                style = Stroke(width = w * 0.11f)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = w * 0.06f,
                                center = Offset(w * 0.72f, h * 0.28f)
                            )
                        }
                    }

                    SocialBrandButton(
                        contentDesc = "YouTube @neekha-khan",
                        backgroundColor = Color(0xFFFF0000),
                        onClick = { openSocialUrl(context, "https://www.youtube.com/@neekha-khan") },
                        testTag = "social_youtube_button"
                    ) {
                        Canvas(modifier = Modifier.size(20.dp)) {
                            val w = size.width
                            val h = size.height
                            val play = Path().apply {
                                moveTo(w * 0.34f, h * 0.24f)
                                lineTo(w * 0.78f, h * 0.50f)
                                lineTo(w * 0.34f, h * 0.76f)
                                close()
                            }
                            drawPath(path = play, color = Color.White)
                        }
                    }
                }
            }
            // Extra bottom clearance so floating right-side buttons never obscure footer icons
            Spacer(modifier = Modifier.height(104.dp))
        }
    }
}

@Composable
private fun SocialBrandButton(
    contentDesc: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    testTag: String,
    iconContent: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = backgroundColor,
        shadowElevation = 4.dp,
        modifier = Modifier
            .size(48.dp)
            .semantics { contentDescription = contentDesc }
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            iconContent()
        }
    }
}
