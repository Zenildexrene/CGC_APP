package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Announcement
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardHighlight
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldNeon
import com.example.ui.theme.PinkNeon
import com.example.ui.theme.PurpleNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TimelineScreen(
  announcements: List<Announcement>,
  onAddAnnouncement: (title: String, content: String, tag: String) -> Unit,
  onToggleReaction: (announcementId: String, reaction: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTag by remember { mutableStateOf("Tout") }
  var searchQuery by remember { mutableStateOf("") }
  var showAddDialog by remember { mutableStateOf(false) }

  val filterTags = listOf("Tout", "Tournoi", "Event", "Update", "Recrutement", "CGC")

  val filteredList = announcements.filter { ann ->
    val matchesTag = selectedTag == "Tout" || ann.tag.equals(selectedTag, ignoreCase = true)
    val matchesSearch = searchQuery.isBlank() ||
      ann.title.contains(searchQuery, ignoreCase = true) ||
      ann.content.contains(searchQuery, ignoreCase = true) ||
      ann.authorName.contains(searchQuery, ignoreCase = true)
    matchesTag && matchesSearch
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = CyberBlack,
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAddDialog = true },
        containerColor = EmeraldNeon,
        contentColor = CyberBlack,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.testTag("add_announcement_fab")
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Nouvelle Annonce",
          modifier = Modifier.size(24.dp)
        )
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      // Timeline Header Bar
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "CGC TIMELINE",
              color = TextPrimary,
              fontSize = 20.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 1.sp
            )
            Text(
              text = "Flux d'annonces communautaires",
              color = TextSecondary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Input
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = {
            Text(
              text = "Rechercher une annonce, un mot-clé...",
              color = TextMuted,
              fontSize = 13.sp
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Rechercher",
              tint = CyanNeon,
              modifier = Modifier.size(18.dp)
            )
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(
                  imageVector = Icons.Default.Clear,
                  contentDescription = "Effacer",
                  tint = TextMuted,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("timeline_search_input"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CyberCard,
            unfocusedContainerColor = CyberCard,
            focusedBorderColor = CyanNeon,
            unfocusedBorderColor = CyberCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tag Filter Horizontal Scroll
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          filterTags.forEach { tag ->
            val isSelected = selectedTag.equals(tag, ignoreCase = true)
            Surface(
              onClick = { selectedTag = tag },
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) EmeraldNeon else CyberCard,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) EmeraldGlow else CyberCardBorder
              ),
              modifier = Modifier.testTag("tag_filter_$tag")
            ) {
              Text(
                text = tag.uppercase(),
                color = if (isSelected) CyberBlack else TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
              )
            }
          }
        }
      }

      // Announcements Feed List
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .testTag("announcements_feed_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        if (filteredList.isEmpty()) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                  imageVector = Icons.Default.SportsEsports,
                  contentDescription = null,
                  tint = TextMuted,
                  modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "Aucune annonce trouvée",
                  color = TextSecondary,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "Essayez un autre mot-clé ou filtre",
                  color = TextMuted,
                  fontSize = 12.sp
                )
              }
            }
          }
        } else {
          items(filteredList, key = { it.id }) { ann ->
            AnnouncementCard(
              announcement = ann,
              onToggleReaction = onToggleReaction
            )
          }
        }
      }
    }
  }

  // Dialog to post new announcement
  if (showAddDialog) {
    NewAnnouncementDialog(
      onDismiss = { showAddDialog = false },
      onSubmit = { title, content, tag ->
        onAddAnnouncement(title, content, tag)
        showAddDialog = false
      }
    )
  }
}

@Composable
private fun AnnouncementCard(
  announcement: Announcement,
  onToggleReaction: (announcementId: String, reaction: String) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("announcement_card_${announcement.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(
      width = if (announcement.isPinned) 1.5.dp else 1.dp,
      color = if (announcement.isPinned) GoldNeon.copy(alpha = 0.6f) else CyberCardBorder
    )
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Top row: Author avatar badge, secret name, level chip, pinned tag, timestamp
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Author icon box
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(getTagColor(announcement.tag).copy(alpha = 0.2f))
              .border(1.dp, getTagColor(announcement.tag), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = announcement.authorName.take(1).uppercase(),
              color = getTagColor(announcement.tag),
              fontSize = 14.sp,
              fontWeight = FontWeight.Black
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = announcement.authorName,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .background(CyberBlack, RoundedCornerShape(4.dp))
                  .border(0.5.dp, CyberCardBorder, RoundedCornerShape(4.dp))
                  .padding(horizontal = 4.dp, vertical = 1.dp)
              ) {
                Text(
                  text = "LVL ${announcement.authorLevel}",
                  color = CyanNeon,
                  fontSize = 8.5.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            Text(
              text = announcement.authorTitle,
              color = TextMuted,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        // Tag badge or Pinned indicator
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (announcement.isPinned) {
            Icon(
              imageVector = Icons.Default.PushPin,
              contentDescription = "Épinglé",
              tint = GoldNeon,
              modifier = Modifier
                .size(14.dp)
                .padding(end = 4.dp)
            )
          }
          TagChip(tag = announcement.tag)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Announcement Title
      Text(
        text = announcement.title,
        color = TextPrimary,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Content
      Text(
        text = announcement.content,
        color = TextSecondary,
        fontSize = 13.sp,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Footer: Timestamp and Reaction Pills (Fire, Gamepad, Trophy)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = announcement.timestamp,
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.Medium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          ReactionButton(
            emoji = "🔥",
            count = announcement.fireCount,
            isSelected = announcement.userReactedFire,
            onClick = { onToggleReaction(announcement.id, "fire") },
            testTag = "reaction_fire_${announcement.id}"
          )
          ReactionButton(
            emoji = "🎮",
            count = announcement.gamepadCount,
            isSelected = announcement.userReactedGamepad,
            onClick = { onToggleReaction(announcement.id, "gamepad") },
            testTag = "reaction_gamepad_${announcement.id}"
          )
          ReactionButton(
            emoji = "🏆",
            count = announcement.trophyCount,
            isSelected = announcement.userReactedTrophy,
            onClick = { onToggleReaction(announcement.id, "trophy") },
            testTag = "reaction_trophy_${announcement.id}"
          )
        }
      }
    }
  }
}

@Composable
private fun ReactionButton(
  emoji: String,
  count: Int,
  isSelected: Boolean,
  onClick: () -> Unit,
  testTag: String
) {
  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(12.dp),
    color = if (isSelected) CyberCardHighlight else CyberBlack,
    border = androidx.compose.foundation.BorderStroke(
      width = 1.dp,
      color = if (isSelected) EmeraldNeon else CyberCardBorder
    ),
    modifier = Modifier.testTag(testTag)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = emoji, fontSize = 12.sp)
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = "$count",
        color = if (isSelected) EmeraldGlow else TextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
private fun TagChip(tag: String) {
  val color = getTagColor(tag)
  Box(
    modifier = Modifier
      .background(color.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
      .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
      .padding(horizontal = 8.dp, vertical = 3.dp)
  ) {
    Text(
      text = tag.uppercase(),
      color = color,
      fontSize = 9.sp,
      fontWeight = FontWeight.Black,
      letterSpacing = 0.5.sp
    )
  }
}

private fun getTagColor(tag: String): Color {
  return when (tag.lowercase()) {
    "tournoi" -> GoldNeon
    "event" -> PinkNeon
    "update" -> CyanNeon
    "recrutement" -> PurpleNeon
    "cgc", "prucon" -> EmeraldNeon
    else -> TextSecondary
  }
}

@Composable
private fun NewAnnouncementDialog(
  onDismiss: () -> Unit,
  onSubmit: (title: String, content: String, tag: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var selectedTag by remember { mutableStateOf("Tournoi") }
  val tags = listOf("Tournoi", "Event", "Update", "Recrutement", "CGC")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Publier une Annonce CGC",
        color = TextPrimary,
        fontWeight = FontWeight.Black,
        fontSize = 18.sp
      )
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Partagez une opportunité, recrutez pour votre clan ou annoncez un match.",
          color = TextSecondary,
          fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Tag Selector
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          tags.forEach { tag ->
            val isSelected = selectedTag == tag
            Surface(
              onClick = { selectedTag = tag },
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) getTagColor(tag) else CyberCard,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) Color.White else CyberCardBorder
              )
            ) {
              Text(
                text = tag,
                color = if (isSelected) CyberBlack else TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Titre de l'annonce", color = TextMuted) },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("new_announcement_title_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CyberBlack,
            unfocusedContainerColor = CyberBlack,
            focusedBorderColor = EmeraldNeon,
            unfocusedBorderColor = CyberCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("Message / Détails", color = TextMuted) },
          minLines = 3,
          maxLines = 5,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("new_announcement_content_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CyberBlack,
            unfocusedContainerColor = CyberBlack,
            focusedBorderColor = EmeraldNeon,
            unfocusedBorderColor = CyberCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank() && content.isNotBlank()) {
            onSubmit(title.trim(), content.trim(), selectedTag)
          }
        },
        enabled = title.isNotBlank() && content.isNotBlank(),
        colors = ButtonDefaults.buttonColors(
          containerColor = EmeraldNeon,
          contentColor = CyberBlack
        ),
        modifier = Modifier.testTag("submit_announcement_button")
      ) {
        Text("Publier (+25 XP)", fontWeight = FontWeight.Black)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Annuler", color = TextSecondary)
      }
    },
    containerColor = CyberCard,
    shape = RoundedCornerShape(18.dp)
  )
}
