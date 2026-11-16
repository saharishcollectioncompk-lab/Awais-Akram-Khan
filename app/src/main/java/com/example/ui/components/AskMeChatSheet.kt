package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AddComment
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.LocalPortfolioColors
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.viewmodel.ChatUiState

@Composable
fun FloatingRightActionStack(
    onOpenAskMe: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .navigationBarsPadding()
            .padding(end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // WhatsApp Floating Button
        Surface(
            onClick = { openWhatsAppBusiness(context) },
            shape = CircleShape,
            color = WhatsAppGreen,
            shadowElevation = 8.dp,
            modifier = Modifier
                .size(54.dp)
                .testTag("floating_whatsapp_button")
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(26.dp)) {
                    val w = size.width
                    val h = size.height
                    drawCircle(
                        color = Color.White,
                        radius = w * 0.42f,
                        style = Stroke(width = w * 0.11f)
                    )
                    val phonePath = Path().apply {
                        moveTo(w * 0.34f, h * 0.36f)
                        quadraticTo(w * 0.42f, h * 0.62f, w * 0.66f, h * 0.66f)
                    }
                    drawPath(
                        path = phonePath,
                        color = Color.White,
                        style = Stroke(
                            width = w * 0.12f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        }

        // ASK ME Floating Button positioned directly below WhatsApp
        Surface(
            onClick = onOpenAskMe,
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 10.dp,
            modifier = Modifier
                .height(52.dp)
                .testTag("floating_ask_me_button")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.SmartToy,
                    contentDescription = "ASK ME AI Assistant",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "ASK ME",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun AskMeChatDialog(
    chatState: ChatUiState,
    onInputChange: (String) -> Unit,
    onSend: (String?) -> Unit,
    onNewChat: () -> Unit,
    onDismiss: () -> Unit
) {
    val ext = LocalPortfolioColors.current
    val listState = rememberLazyListState()

    LaunchedEffect(chatState.messages.size, chatState.streamingText) {
        if (chatState.messages.isNotEmpty() || chatState.streamingText.isNotEmpty()) {
            listState.animateScrollToItem(listState.layoutInfo.totalItemsCount.coerceAtLeast(1) - 1)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .widthIn(max = 540.dp)
                .heightIn(min = 420.dp, max = 640.dp)
                .imePadding()
                .testTag("ask_me_chat_window"),
            shape = RoundedCornerShape(24.dp),
            color = ext.cardBackground,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.20f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SmartToy,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ASK ME",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "English • اردو • Roman Urdu",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(
                            onClick = onNewChat,
                            modifier = Modifier.testTag("ask_me_new_chat_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AddComment,
                                contentDescription = "New Chat",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New Chat", color = Color.White)
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("ask_me_close_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close ASK ME",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Conversation Body
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Exact Welcome Screen Structure
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = ext.elevatedSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, ext.borderColor, RoundedCornerShape(18.dp))
                        ) {
                            Text(
                                text = ChatUiState.WELCOME_MESSAGE,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .testTag("ask_me_welcome_message")
                            )
                        }
                    }

                    // Four Clickable Suggested Questions directly below Welcome Message
                    if (chatState.messages.isEmpty()) {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                ChatUiState.SUGGESTED_QUESTIONS.forEachIndexed { idx, question ->
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .border(
                                                1.dp,
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                                                RoundedCornerShape(14.dp)
                                            )
                                            .clickable(enabled = !chatState.isGenerating) {
                                                onSend(question)
                                            }
                                            .testTag("suggested_question_$idx")
                                    ) {
                                        Text(
                                            text = question,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    items(chatState.messages, key = { it.id }) { msg ->
                        ChatBubble(message = msg)
                    }

                    if (chatState.isGenerating) {
                        item {
                            if (chatState.streamingText.isNotEmpty()) {
                                ChatBubble(
                                    message = ChatMessage(
                                        id = "streaming",
                                        text = chatState.streamingText,
                                        isUser = false
                                    )
                                )
                            } else {
                                TypingIndicatorBubble()
                            }
                        }
                    }
                }

                HorizontalDivider(color = ext.borderColor)

                chatState.errorMessage?.let { err ->
                    Text(
                        text = err,
                        style = MaterialTheme.typography.bodySmall,
                        color = ErrorCrimson,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ErrorCrimson.copy(alpha = 0.10f))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("ask_me_error_banner")
                    )
                }

                // Input Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = chatState.inputText,
                        onValueChange = onInputChange,
                        placeholder = { Text("Ask about Awais's work...") },
                        maxLines = 3,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ask_me_input")
                    )
                    Surface(
                        onClick = { onSend(null) },
                        enabled = chatState.inputText.isNotBlank() && !chatState.isGenerating,
                        shape = CircleShape,
                        color = if (chatState.inputText.isNotBlank() && !chatState.isGenerating) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            ext.elevatedSurface
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("ask_me_send_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Send,
                                contentDescription = "Send message",
                                tint = if (chatState.inputText.isNotBlank() && !chatState.isGenerating) {
                                    Color.White
                                } else {
                                    ext.secondaryText
                                },
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val ext = LocalPortfolioColors.current
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val bgColor = when {
        message.isUser -> MaterialTheme.colorScheme.primary
        message.isError -> ErrorCrimson.copy(alpha = 0.14f)
        else -> ext.elevatedSurface
    }
    val textColor = when {
        message.isUser -> Color.White
        message.isError -> ErrorCrimson
        else -> MaterialTheme.colorScheme.onBackground
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 16.dp
            ),
            color = bgColor,
            modifier = Modifier.widthIn(max = 360.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
private fun TypingIndicatorBubble() {
    val ext = LocalPortfolioColors.current
    val infiniteTransition = rememberInfiniteTransition(label = "typingDots")
    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "dot1"
    )
    val alpha2 by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 180), RepeatMode.Reverse),
        label = "dot2"
    )
    val alpha3 by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 360), RepeatMode.Reverse),
        label = "dot3"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = ext.elevatedSurface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(alpha1, alpha2, alpha3).forEach { alpha ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha))
                )
            }
        }
    }
}
