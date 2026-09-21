package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ChatMessage
import com.example.data.model.DiscordChannel
import com.example.data.model.GamerProfile
import com.example.data.model.GamingGroup
import com.example.data.model.UserRole
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardHighlight
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DiscordBlurple
import com.example.ui.theme.DiscordChannelBar
import com.example.ui.theme.DiscordChatBackground
import com.example.ui.theme.DiscordDark
import com.example.ui.theme.DiscordMessageHover
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldNeon
import com.example.ui.theme.PinkNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun GroupsChatScreen(
  groups: List<GamingGroup>,
  chatMessages: Map<String, List<ChatMessage>>,
  discordChannels: List<DiscordChannel> = emptyList(),
  allUsers: List<GamerProfile> = emptyList(),
  currentProfile: GamerProfile? = null,
  blockedUsernames: Set<String> = emptySet(),
  onQuickBlockUser: (username: String) -> Unit = {},
  onQuickReportMessage: (senderName: String, content: String, category: String) -> Unit = { _, _, _ -> },
  onSendMessage: (groupId: String, content: String) -> Unit,
  onRollDice: (groupId: String) -> Int,
  onJoinGroup: (groupId: String) -> Unit,
  onAddReaction: (groupId: String, messageId: String, reaction: String) -> Unit = { _, _, _ -> },
  modifier: Modifier = Modifier
) {
  // Default channels if none provided
  val defaultChannels = listOf(
    DiscordChannel("grp_1", "général-rdc", "SALONS TEXTUELS", "Discussion gaming libre entre tous les gamers congolais"),
    DiscordChannel("ch_annonces", "annonces-cgc", "ANNONCES & INFOS", "Annonces officielles de la communauté"),
    DiscordChannel("ch_regles", "règles-et-sécurité", "ANNONCES & INFOS", "Règles communautaires et garantie zéro fuite"),
    DiscordChannel("ch_team", "recherche-de-team", "SALONS TEXTUELS", "Recrutement et création de teams esport"),
    DiscordChannel("ch_fc25", "fc25-tournois", "SALONS TEXTUELS", "Matchs amicaux et organisation FC 25"),
    DiscordChannel("ch_voice_kinshasa", "Kinshasa Lounge", "SALONS VOCAUX", "Canal vocal communautaire 64kbps Opus", isVoice = true),
    DiscordChannel("ch_dm_support", "Support Direct Créateur", "MESSAGES DIRECTS", "Canal privé chiffré de bout en bout (E2EE)", isVoice = false, isEncrypted = true)
  )

  val channels = if (discordChannels.isNotEmpty()) discordChannels else defaultChannels

  var activeChannelId by remember { mutableStateOf("grp_1") }
  var showChannelSidebar by remember { mutableStateOf(false) }
  var showMembersListModal by remember { mutableStateOf(false) }
  var isMicMuted by remember { mutableStateOf(false) }
  var isHeadsetDeafened by remember { mutableStateOf(false) }
  var quickSafetyMessage by remember { mutableStateOf<ChatMessage?>(null) }

  val activeChannel = channels.find { it.id == activeChannelId } ?: channels.first()
  val messages = chatMessages[activeChannelId] ?: emptyList()

  val activeUsers = if (allUsers.isNotEmpty()) allUsers else listOf(
    GamerProfile(
      id = "usr_zenil_creator",
      email = "zenildelutu@gmail.com",
      secretName = "Zenil",
      secretCode = "Firstgmg05",
      role = UserRole.CREATOR,
      level = 50,
      xp = 2500,
      nextLevelXp = 10000,
      tokens = 500,
      title = "Créateur & Propriétaire Suprême",
      memberSince = "Septembre 2026",
      clan = "CGC Core Authority",
      tournamentsWon = 0,
      matchesPlayed = 0,
      winRate = "100%",
      status = "En ligne",
      isImmuneFromRemoval = true
    )
  )

  Row(
    modifier = modifier
      .fillMaxSize()
      .background(DiscordChatBackground)
      .testTag("discord_chat_screen")
  ) {
    // ---------------------------------------------------------
    // 1. Left Narrow Server Bar (Discord Server Rail)
    // ---------------------------------------------------------
    Column(
      modifier = Modifier
        .width(62.dp)
        .fillMaxHeight()
        .background(DiscordDark)
        .padding(vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Direct Messages Icon
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(DarkSurface)
          .clickable {
            activeChannelId = "ch_dm_support"
          },
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Forum,
          contentDescription = "Messages Directs",
          tint = if (activeChannel.category == "MESSAGES DIRECTS") DiscordBlurple else TextSecondary,
          modifier = Modifier.size(22.dp)
        )
      }

      HorizontalDivider(
        color = CyberCardBorder,
        modifier = Modifier
          .width(32.dp)
          .padding(vertical = 2.dp)
      )

      // Main CGC Server Icon (Rounded Square style Discord)
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(DiscordBlurple)
          .border(2.dp, EmeraldNeon, RoundedCornerShape(16.dp))
          .clickable {
            activeChannelId = "grp_1"
          }
          .testTag("discord_cgc_server_icon"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "CGC",
          color = Color.White,
          fontWeight = FontWeight.Black,
          fontSize = 14.sp
        )
      }

      // Add Server / Explore Icon
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(DarkSurface)
          .clickable {},
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Explorer",
          tint = EmeraldNeon,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.weight(1f))

      // Anti-Leak Shield Indicator
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(DarkSurface),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Security,
          contentDescription = "Protection Anti-Fuite",
          tint = EmeraldNeon,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    // ---------------------------------------------------------
    // 2. Discord Channels Sidebar (Collapsible on mobile)
    // ---------------------------------------------------------
    // We display the channels sidebar if showChannelSidebar is true OR on wide layouts
    AnimatedVisibility(visible = showChannelSidebar) {
      Column(
        modifier = Modifier
          .width(200.dp)
          .fillMaxHeight()
          .background(DiscordChannelBar)
      ) {
        // Server Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "CGC • RDC 🇨🇩",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextSecondary)
        }

        HorizontalDivider(color = CyberCardBorder)

        // Channels List grouped by Category
        LazyColumn(
          modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp, vertical = 6.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          val categories = channels.map { it.category }.distinct()

          categories.forEach { category ->
            item {
              Text(
                text = "▼ $category",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 6.dp, top = 6.dp, bottom = 4.dp)
              )
            }

            items(channels.filter { it.category == category }) { channel ->
              val isSelected = channel.id == activeChannelId

              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(6.dp))
                  .clickable {
                    activeChannelId = channel.id
                    showChannelSidebar = false
                  }
                  .testTag("discord_channel_${channel.name}"),
                color = if (isSelected) DiscordMessageHover else Color.Transparent
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Icon(
                    imageVector = when {
                      channel.isVoice -> Icons.Default.VolumeUp
                      channel.isEncrypted -> Icons.Default.Lock
                      else -> Icons.Default.Tag
                    },
                    contentDescription = null,
                    tint = if (isSelected) TextPrimary else TextMuted,
                    modifier = Modifier.size(16.dp)
                  )
                  Text(
                    text = channel.name,
                    color = if (isSelected) TextPrimary else TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                  )
                }
              }
            }
          }
        }

        // Bottom User Strip (Discord Style)
        Surface(
          modifier = Modifier.fillMaxWidth(),
          color = DiscordDark
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(CyberBlack)
                  .border(2.dp, GoldNeon, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = (currentProfile?.secretName ?: "Z").take(1).uppercase(),
                  color = GoldNeon,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }

              Column {
                Text(
                  text = currentProfile?.secretName ?: "Zenil",
                  color = TextPrimary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "#0001",
                  color = TextMuted,
                  fontSize = 9.sp
                )
              }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
              IconButton(
                onClick = { isMicMuted = !isMicMuted },
                modifier = Modifier.size(26.dp)
              ) {
                Icon(
                  imageVector = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                  contentDescription = "Micro",
                  tint = if (isMicMuted) PinkNeon else TextSecondary,
                  modifier = Modifier.size(16.dp)
                )
              }
              IconButton(
                onClick = { isHeadsetDeafened = !isHeadsetDeafened },
                modifier = Modifier.size(26.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Headphones,
                  contentDescription = "Casque",
                  tint = if (isHeadsetDeafened) PinkNeon else TextSecondary,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }
    }

    // ---------------------------------------------------------
    // 3. Main Discord Chat Area
    // ---------------------------------------------------------
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .background(DiscordChatBackground)
    ) {
      // Discord Channel Top Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .background(DiscordChatBackground)
          .border(0.5.dp, CyberCardBorder.copy(alpha = 0.5f))
          .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Toggle channels list button
          IconButton(
            onClick = { showChannelSidebar = !showChannelSidebar },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Menu,
              contentDescription = "Salons",
              tint = if (showChannelSidebar) DiscordBlurple else TextSecondary
            )
          }

          Icon(
            imageVector = when {
              activeChannel.isVoice -> Icons.Default.VolumeUp
              activeChannel.isEncrypted -> Icons.Default.Lock
              else -> Icons.Default.Tag
            },
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
          )

          Text(
            text = activeChannel.name,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )

          if (activeChannel.isEncrypted) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = EmeraldNeon.copy(alpha = 0.2f)
            ) {
              Text(
                "🔒 E2EE",
                color = EmeraldNeon,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
              )
            }
          }
        }

        // Action buttons (Members list & Dice)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          IconButton(
            onClick = { onRollDice(activeChannelId) },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(Icons.Default.Casino, contentDescription = "Lancer un dé", tint = CyanNeon, modifier = Modifier.size(18.dp))
          }

          IconButton(
            onClick = { showMembersListModal = true },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(Icons.Default.People, contentDescription = "Membres", tint = TextSecondary, modifier = Modifier.size(20.dp))
          }
        }
      }

      // Voice Channel Banner if active channel is voice
      if (activeChannel.isVoice) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          colors = CardDefaults.cardColors(containerColor = DiscordDark),
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon),
          shape = RoundedCornerShape(10.dp)
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Icon(Icons.Default.VolumeUp, contentDescription = null, tint = EmeraldNeon)
              Column {
                Text("Connecté au salon vocal Kinshasa", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("Opus Codec • Faible latence • RTC Actif", color = EmeraldGlow, fontSize = 10.sp)
              }
            }
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = PinkNeon.copy(alpha = 0.2f),
              border = androidx.compose.foundation.BorderStroke(1.dp, PinkNeon),
              modifier = Modifier.clickable { activeChannelId = "grp_1" }
            ) {
              Text("Quitter", color = PinkNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
          }
        }
      }

      // Messages Feed (Discord style)
      val listState = rememberLazyListState()
      LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
          listState.animateScrollToItem(messages.size - 1)
        }
      }

      LazyColumn(
        state = listState,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Welcome Channel Header
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(DiscordDark),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Tag,
                contentDescription = null,
                tint = TextPrimary,
                modifier = Modifier.size(28.dp)
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Bienvenue dans #${activeChannel.name} !",
              color = TextPrimary,
              fontSize = 18.sp,
              fontWeight = FontWeight.Black
            )
            Text(
              text = activeChannel.topic.ifBlank { "C'est le début de ce salon." },
              color = TextSecondary,
              fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.5f))
          }
        }

        // Messages list
        val visibleMessages = messages.filter { msg ->
          !blockedUsernames.contains(msg.senderName.lowercase().trim())
        }

        items(visibleMessages, key = { it.id }) { msg ->
          val isOwn = currentProfile?.secretName?.equals(msg.senderName, ignoreCase = true) == true
          DiscordMessageRow(
            message = msg,
            isOwnMessage = isOwn,
            onAddReaction = { reaction ->
              onAddReaction(activeChannelId, msg.id, reaction)
            },
            onSafetyClick = {
              quickSafetyMessage = msg
            }
          )
        }
      }

      // Discord Message Composer
      var messageInput by remember { mutableStateOf("") }

      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp, vertical = 8.dp),
        color = DiscordDark,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Plus / Attachment Button
          IconButton(
            onClick = { onRollDice(activeChannelId) },
            modifier = Modifier.size(34.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Ajouter",
              tint = TextSecondary,
              modifier = Modifier.size(20.dp)
            )
          }

          // Input field
          OutlinedTextField(
            value = messageInput,
            onValueChange = { messageInput = it },
            placeholder = {
              Text(
                text = "Envoyer un message dans #${activeChannel.name}...",
                color = TextMuted,
                fontSize = 13.sp
              )
            },
            modifier = Modifier
              .weight(1f)
              .testTag("discord_message_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color.Transparent,
              unfocusedContainerColor = Color.Transparent,
              focusedBorderColor = Color.Transparent,
              unfocusedBorderColor = Color.Transparent,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            ),
            singleLine = false,
            maxLines = 3
          )

          // Dice Button
          IconButton(
            onClick = { onRollDice(activeChannelId) },
            modifier = Modifier.size(34.dp)
          ) {
            Text("🎲", fontSize = 16.sp)
          }

          // Send Button
          IconButton(
            onClick = {
              if (messageInput.isNotBlank()) {
                onSendMessage(activeChannelId, messageInput.trim())
                messageInput = ""
              }
            },
            enabled = messageInput.isNotBlank(),
            modifier = Modifier.size(34.dp)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Envoyer",
              tint = if (messageInput.isNotBlank()) DiscordBlurple else TextMuted,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }

  // ---------------------------------------------------------
  // 4. Discord Members List Modal
  // ---------------------------------------------------------
  if (showMembersListModal) {
    Dialog(onDismissRequest = { showMembersListModal = false }) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = DiscordDark),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Membres du salon", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            IconButton(onClick = { showMembersListModal = false }, modifier = Modifier.size(24.dp)) {
              Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
            }
          }

          HorizontalDivider(color = CyberCardBorder)

          // Creator Section
          Text(
            text = "👑 CRÉATEUR DU PROJET — 1",
            color = GoldNeon,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black
          )

          val creator = activeUsers.find { it.isCreator } ?: activeUsers.first()
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(CyberBlack)
                .border(2.dp, GoldNeon, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text("Z", color = GoldNeon, fontWeight = FontWeight.Bold)
            }
            Column {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(creator.secretName, color = GoldNeon, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = GoldNeon.copy(alpha = 0.2f)
                ) {
                  Text("CRÉATEUR", color = GoldNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
                }
              }
              Text("Immunité totale • Fondateur", color = TextSecondary, fontSize = 10.sp)
            }
          }

          // Anti-leak data protection reminder
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CyberBlack),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(8.dp)
          ) {
            Row(
              modifier = Modifier.padding(8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(16.dp))
              Text(
                text = "Sécurité garantie : les e-mails et mots de passe des membres sont invisibles pour éviter toute fuite.",
                color = TextSecondary,
                fontSize = 10.sp
              )
            }
          }
        }
      }
    }
  }

  // ---------------------------------------------------------
  // Quick Safety Dialog (2-click block, 3-click report)
  // ---------------------------------------------------------
  if (quickSafetyMessage != null) {
    val targetMsg = quickSafetyMessage!!
    var reportStep by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("harassment") }
    var actionDoneMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
      onDismissRequest = { quickSafetyMessage = null },
      containerColor = DarkSurface,
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = EmeraldNeon,
            modifier = Modifier.size(20.dp)
          )
          Text(
            if (!reportStep) "Protection & Sécurité" else "Signaler un message",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          if (actionDoneMessage != null) {
            Text(actionDoneMessage!!, color = EmeraldGlow, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          } else if (!reportStep) {
            Text("Message de @${targetMsg.senderName} :", color = TextSecondary, fontSize = 12.sp)
            Surface(
              color = CyberBlack,
              shape = RoundedCornerShape(8.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "\"${targetMsg.content.take(120)}\"",
                color = TextPrimary,
                fontSize = 12.sp,
                modifier = Modifier.padding(10.dp)
              )
            }

            Text(
              "Agissez en toute liberté : bloquez les messages indésirables en 2 clics ou signalez anonymement.",
              color = TextMuted,
              fontSize = 11.sp
            )

            // Button 1: Quick Block (2 clicks total)
            Button(
              onClick = {
                onQuickBlockUser(targetMsg.senderName)
                actionDoneMessage = "Utilisateur @${targetMsg.senderName} bloqué avec succès. Ses messages sont désormais masqués."
              },
              colors = ButtonDefaults.buttonColors(containerColor = CyberBlack),
              border = androidx.compose.foundation.BorderStroke(1.dp, PinkNeon),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("🛡️ Bloquer @${targetMsg.senderName} (2 clics)", color = PinkNeon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            // Button 2: Report Message
            Button(
              onClick = { reportStep = true },
              colors = ButtonDefaults.buttonColors(containerColor = CyberBlack),
              border = androidx.compose.foundation.BorderStroke(1.dp, GoldNeon),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("⚠️ Signaler ce message", color = GoldNeon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          } else {
            Text("Motif du signalement (100% anonyme & garanti sans fuite) :", color = TextSecondary, fontSize = 12.sp)
            val categories = listOf(
              "harassment" to "Harcèlement ou intimidation",
              "hate_speech" to "Discours de haine ou discrimination",
              "spam" to "Spam ou publicité abusive",
              "scam" to "Arnaque financière / Scam",
              "illegal_content" to "Contenu illégal ou dangereux"
            )
            categories.forEach { (catId, label) ->
              val isSel = selectedCategory == catId
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSel) CyanNeon.copy(alpha = 0.15f) else CyberBlack,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) CyanNeon else CyberCardBorder),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { selectedCategory = catId }
              ) {
                Text(
                  text = label,
                  color = if (isSel) CyanGlow else TextPrimary,
                  fontSize = 12.sp,
                  fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
              }
            }
          }
        }
      },
      confirmButton = {
        if (actionDoneMessage != null) {
          TextButton(onClick = { quickSafetyMessage = null }) {
            Text("Fermer", color = EmeraldNeon, fontWeight = FontWeight.Bold)
          }
        } else if (reportStep) {
          Button(
            onClick = {
              onQuickReportMessage(targetMsg.senderName, targetMsg.content, selectedCategory)
              actionDoneMessage = "Merci. Votre signalement a été transmis à l'équipe de modération dans le respect de votre vie privée."
            },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon)
          ) {
            Text("Envoyer le signalement (Étape 3/3)", color = CyberBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
          }
        }
      },
      dismissButton = {
        if (actionDoneMessage == null) {
          TextButton(onClick = { quickSafetyMessage = null }) {
            Text("Annuler", color = TextSecondary)
          }
        }
      }
    )
  }
}

// -------------------------------------------------------------
// Component: Discord Message Item
// -------------------------------------------------------------
@Composable
fun DiscordMessageRow(
  message: ChatMessage,
  isOwnMessage: Boolean = false,
  onAddReaction: (String) -> Unit,
  onSafetyClick: () -> Unit = {}
) {
  val isCreator = message.senderName.contains("Zenil", ignoreCase = true) || message.senderRole == UserRole.CREATOR
  val authorColor = when {
    isCreator -> GoldNeon
    message.senderRole == UserRole.ADMIN -> DiscordBlurple
    else -> EmeraldNeon
  }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(6.dp))
      .padding(horizontal = 6.dp, vertical = 4.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // Discord Rounded Square Avatar
    Box(
      modifier = Modifier
        .size(38.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(CyberBlack)
        .border(1.5.dp, authorColor, RoundedCornerShape(10.dp)),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = message.senderName.take(1).uppercase(),
        color = authorColor,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
      )
    }

    Column(modifier = Modifier.weight(1f)) {
      // Header: Username + Role pill + Timestamp + Safety Shield for other users
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = message.senderName,
          color = authorColor,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )

        if (isCreator) {
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = GoldNeon.copy(alpha = 0.2f)
          ) {
            Text(
              text = "👑 CRÉATEUR",
              color = GoldNeon,
              fontSize = 8.sp,
              fontWeight = FontWeight.Black,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
            )
          }
        }

        Text(
          text = message.timestamp,
          color = TextMuted,
          fontSize = 10.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        if (!isOwnMessage) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = "Options de protection et sécurité",
            tint = TextMuted.copy(alpha = 0.6f),
            modifier = Modifier
              .size(16.dp)
              .clickable { onSafetyClick() }
          )
        }
      }

      Spacer(modifier = Modifier.height(2.dp))

      // Dice roll embed card (Discord embed style)
      if (message.isDiceRoll) {
        Card(
          colors = CardDefaults.cardColors(containerColor = DiscordDark),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.padding(vertical = 4.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text("🎲", fontSize = 20.sp)
            Column {
              Text("Jet de dé officiel CGC", color = CyanGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              Text("Résultat : ${message.diceValue ?: 50} sur 100", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Black)
            }
          }
        }
      } else {
        // Normal text message
        Text(
          text = message.content,
          color = TextPrimary,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
      }

      // Quick Reactions Row (Discord reactions chips)
      Row(
        modifier = Modifier.padding(top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        val fireCount = message.reactions["🔥"] ?: 0
        val gameCount = message.reactions["🎮"] ?: 0
        val trophyCount = message.reactions["🏆"] ?: 0

        if (fireCount > 0) {
          DiscordReactionChip(emoji = "🔥", count = fireCount, onClick = { onAddReaction("🔥") })
        }
        if (gameCount > 0) {
          DiscordReactionChip(emoji = "🎮", count = gameCount, onClick = { onAddReaction("🎮") })
        }
        if (trophyCount > 0) {
          DiscordReactionChip(emoji = "🏆", count = trophyCount, onClick = { onAddReaction("🏆") })
        }

        // Quick add reaction button (+)
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = DiscordDark,
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
          modifier = Modifier.clickable { onAddReaction("🔥") }
        ) {
          Text("+🔥", fontSize = 10.sp, color = TextMuted, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
        }
      }
    }
  }
}

@Composable
private fun DiscordReactionChip(emoji: String, count: Int, onClick: () -> Unit) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = DiscordDark,
    border = androidx.compose.foundation.BorderStroke(1.dp, DiscordBlurple),
    modifier = Modifier.clickable { onClick() }
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(emoji, fontSize = 11.sp)
      Text("$count", color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
  }
}
