package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.GamingGroup
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardHighlight
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldNeon
import com.example.ui.theme.PurpleNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GroupsChatScreen(
  groups: List<GamingGroup>,
  chatMessages: Map<String, List<ChatMessage>>,
  onSendMessage: (groupId: String, content: String) -> Unit,
  onRollDice: (groupId: String) -> Int,
  onJoinGroup: (groupId: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var activeGroupId by remember { mutableStateOf<String?>(null) }

  if (activeGroupId == null) {
    // Show Groups List View
    GroupsListView(
      groups = groups,
      onSelectGroup = { activeGroupId = it },
      onJoinGroup = onJoinGroup,
      modifier = modifier
    )
  } else {
    // Show Chat Room View
    val group = groups.find { it.id == activeGroupId }
    val messages = chatMessages[activeGroupId] ?: emptyList()

    if (group != null) {
      ChatRoomView(
        group = group,
        messages = messages,
        onBack = { activeGroupId = null },
        onSendMessage = { text -> onSendMessage(group.id, text) },
        onRollDice = { onRollDice(group.id) },
        modifier = modifier
      )
    }
  }
}

@Composable
private fun GroupsListView(
  groups: List<GamingGroup>,
  onSelectGroup: (String) -> Unit,
  onJoinGroup: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .testTag("groups_list_view"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Column {
        Text(
          text = "SALONS & CLANS GAMING",
          color = TextPrimary,
          fontSize = 20.sp,
          fontWeight = FontWeight.Black,
          letterSpacing = 1.sp
        )
        Text(
          text = "Rejoignez une communauté par jeu, trouvez des coéquipiers et discutez en direct.",
          color = TextSecondary,
          fontSize = 12.sp
        )
      }
    }

    items(groups, key = { it.id }) { group ->
      GroupCard(
        group = group,
        onOpenChat = { onSelectGroup(group.id) },
        onJoin = { onJoinGroup(group.id) }
      )
    }
  }
}

@Composable
private fun GroupCard(
  group: GamingGroup,
  onOpenChat: () -> Unit,
  onJoin: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onOpenChat() }
      .testTag("group_card_${group.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(EmeraldNeon.copy(alpha = 0.15f))
              .border(1.dp, EmeraldNeon.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Groups,
              contentDescription = null,
              tint = EmeraldNeon,
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = group.name,
              color = TextPrimary,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = group.gameCategory,
              color = CyanNeon,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }

        // Member count pill
        Box(
          modifier = Modifier
            .background(CyberBlack, RoundedCornerShape(6.dp))
            .border(1.dp, CyberCardBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "${group.memberCount} membres",
            color = TextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = group.description,
        color = TextSecondary,
        fontSize = 12.sp,
        lineHeight = 16.sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Last message preview & Chat button
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(CyberBlack, RoundedCornerShape(10.dp))
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.ChatBubbleOutline,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = group.lastMessage,
            color = TextMuted,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
          text = group.lastMessageTime,
          color = TextMuted,
          fontSize = 10.sp
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        if (!group.isJoined) {
          Button(
            onClick = onJoin,
            colors = ButtonDefaults.buttonColors(
              containerColor = CyberCardHighlight,
              contentColor = CyanNeon
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("join_group_${group.id}")
          ) {
            Text("Rejoindre le Clan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.width(8.dp))
        }

        Button(
          onClick = onOpenChat,
          colors = ButtonDefaults.buttonColors(
            containerColor = EmeraldNeon,
            contentColor = CyberBlack
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("open_chat_${group.id}")
        ) {
          Text("Ouvrir le Salon", fontSize = 11.sp, fontWeight = FontWeight.Black)
        }
      }
    }
  }
}

@Composable
private fun ChatRoomView(
  group: GamingGroup,
  messages: List<ChatMessage>,
  onBack: () -> Unit,
  onSendMessage: (String) -> Unit,
  onRollDice: () -> Unit,
  modifier: Modifier = Modifier
) {
  var inputText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .testTag("chatroom_view")
  ) {
    // Chat Header
    Surface(
      color = DarkSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("back_to_groups_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Retour",
            tint = TextPrimary
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = group.name,
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${group.memberCount} membres en ligne • Serveur Kinshasa",
            color = EmeraldGlow,
            fontSize = 10.sp
          )
        }

        // Dice roll action in header
        IconButton(
          onClick = onRollDice,
          modifier = Modifier.testTag("dice_roll_header_button")
        ) {
          Icon(
            imageVector = Icons.Default.Casino,
            contentDescription = "Lancer Dé",
            tint = GoldNeon
          )
        }
      }
    }

    // Messages List
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 12.dp),
      contentPadding = PaddingValues(vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(messages, key = { it.id }) { msg ->
        ChatMessageBubble(message = msg)
      }
    }

    // Chat Input Bar
    Surface(
      color = DarkSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(8.dp)) {
        // Quick Actions Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            onClick = onRollDice,
            shape = RoundedCornerShape(12.dp),
            color = CyberBlack,
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldNeon.copy(alpha = 0.5f)),
            modifier = Modifier.testTag("quick_roll_dice_chip")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Casino,
                contentDescription = null,
                tint = GoldNeon,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Lancer un Dé (/roll)",
                color = GoldNeon,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Surface(
            onClick = { inputText = "GG les gars ! Bien joué !" },
            shape = RoundedCornerShape(12.dp),
            color = CyberBlack,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
          ) {
            Text(
              text = "GG les gars !",
              color = TextSecondary,
              fontSize = 10.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        // Main input field and send button
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            placeholder = { Text("Message...", color = TextMuted, fontSize = 13.sp) },
            singleLine = true,
            modifier = Modifier
              .weight(1f)
              .testTag("chat_input_field"),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = CyberBlack,
              unfocusedContainerColor = CyberBlack,
              focusedBorderColor = EmeraldNeon,
              unfocusedBorderColor = CyberCardBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )

          Spacer(modifier = Modifier.width(8.dp))

          IconButton(
            onClick = {
              if (inputText.isNotBlank()) {
                if (inputText.trim().equals("/roll", ignoreCase = true)) {
                  onRollDice()
                } else {
                  onSendMessage(inputText.trim())
                }
                inputText = ""
              }
            },
            enabled = inputText.isNotBlank(),
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(if (inputText.isNotBlank()) EmeraldNeon else CyberCardBorder)
              .testTag("chat_send_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Envoyer",
              tint = if (inputText.isNotBlank()) CyberBlack else TextMuted,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ChatMessageBubble(message: ChatMessage) {
  val isMe = message.isMe

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
  ) {
    Column(
      modifier = Modifier
        .widthIn(max = 280.dp)
        .background(
          color = when {
            message.isDiceRoll -> GoldNeon.copy(alpha = 0.15f)
            isMe -> EmeraldNeon.copy(alpha = 0.15f)
            else -> CyberCard
          },
          shape = RoundedCornerShape(12.dp)
        )
        .border(
          width = 1.dp,
          color = when {
            message.isDiceRoll -> GoldNeon.copy(alpha = 0.5f)
            isMe -> EmeraldNeon.copy(alpha = 0.4f)
            else -> CyberCardBorder
          },
          shape = RoundedCornerShape(12.dp)
        )
        .padding(10.dp)
    ) {
      if (!isMe) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(bottom = 2.dp)
        ) {
          Text(
            text = message.senderName,
            color = CyanNeon,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(4.dp))
          Box(
            modifier = Modifier
              .background(CyberBlack, RoundedCornerShape(4.dp))
              .padding(horizontal = 4.dp, vertical = 1.dp)
          ) {
            Text(
              text = "LVL ${message.senderLevel}",
              color = TextSecondary,
              fontSize = 8.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      if (message.isDiceRoll) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = "🎲", fontSize = 16.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = message.content,
            color = GoldNeon,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black
          )
        }
      } else {
        Text(
          text = message.content,
          color = TextPrimary,
          fontSize = 13.sp,
          lineHeight = 17.sp
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = message.timestamp,
        color = TextMuted,
        fontSize = 9.sp,
        modifier = Modifier.align(Alignment.End)
      )
    }
  }
}
