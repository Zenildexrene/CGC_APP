package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Announcement
import com.example.data.model.LiveStream
import com.example.data.model.UserRole
import com.example.data.model.UserStory
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardHighlight
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DiscordBlurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.GoldNeon
import com.example.ui.theme.PinkNeon
import com.example.ui.theme.PurpleNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TimelineScreen(
  announcements: List<Announcement>,
  stories: List<UserStory> = emptyList(),
  liveStreams: List<LiveStream> = emptyList(),
  currentUserName: String = "Zenil",
  currentUserRole: UserRole = UserRole.CREATOR,
  onAddAnnouncement: (title: String, content: String, tag: String) -> Unit,
  onToggleReaction: (announcementId: String, reaction: String) -> Unit,
  onAddComment: (announcementId: String, commentText: String) -> Unit = { _, _ -> },
  onSharePost: (announcementId: String) -> Unit = {},
  onCreateStory: (title: String, tag: String) -> Unit = { _, _ -> },
  onAddLiveStream: (title: String, platform: String, streamUrl: String, gameName: String) -> Unit = { _, _, _, _ -> },
  modifier: Modifier = Modifier
) {
  var selectedTag by remember { mutableStateOf("Tout") }
  var searchQuery by remember { mutableStateOf("") }
  var showAddDialog by remember { mutableStateOf(false) }
  var showCreateStoryDialog by remember { mutableStateOf(false) }
  var showLiveImportDialog by remember { mutableStateOf(false) }
  var activeStoryViewing by remember { mutableStateOf<UserStory?>(null) }
  var activeLiveViewing by remember { mutableStateOf<LiveStream?>(null) }
  var expandedCommentsMap by remember { mutableStateOf(mapOf<String, Boolean>()) }
  var shareNotificationText by remember { mutableStateOf<String?>(null) }

  val filterTags = listOf("Tout", "CGC", "Tournoi", "Event", "Recrutement", "Update")

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
    containerColor = CyberBlack
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .testTag("facebook_style_feed"),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // 1. Facebook Stories Carousel
      item {
        FacebookStoriesBar(
          currentUserName = currentUserName,
          stories = stories,
          onAddStoryClick = { showCreateStoryDialog = true },
          onStoryClick = { activeStoryViewing = it }
        )
      }

      // 2. Facebook Post Composer Card ("Que voulez-vous dire ?")
      item {
        FacebookPostComposerCard(
          currentUserName = currentUserName,
          onOpenComposer = { showAddDialog = true },
          onOpenLiveImport = { showLiveImportDialog = true }
        )
      }

      // 3. Live Streams Showcase Section (TikTok & YouTube Lives CGC)
      item {
        FacebookLiveStreamsSection(
          liveStreams = liveStreams,
          onImportLiveClick = { showLiveImportDialog = true },
          onWatchLiveClick = { activeLiveViewing = it }
        )
      }

      // 4. Search & Tag Filters
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
              Text(
                text = "Rechercher sur le fil d'actualité CGC...",
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

          Spacer(modifier = Modifier.height(10.dp))

          // Filter tags row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            filterTags.forEach { tag ->
              val isSelected = selectedTag == tag
              Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) EmeraldNeon else CyberCard,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isSelected) EmeraldGlow else CyberCardBorder
                ),
                modifier = Modifier
                  .clickable { selectedTag = tag }
                  .testTag("filter_tag_$tag")
              ) {
                Text(
                  text = when (tag) {
                    "Tout" -> "Tous les posts"
                    "CGC" -> "📢 Officiel CGC"
                    "Tournoi" -> "🏆 Tournois"
                    "Event" -> "🎉 Événements"
                    "Recrutement" -> "⚔️ Recrutement"
                    "Update" -> "🚀 Mises à jour"
                    else -> tag
                  },
                  color = if (isSelected) CyberBlack else TextSecondary,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
              }
            }
          }
        }
      }

      // Share Notification Feedback
      if (shareNotificationText != null) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = CyberCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon),
            shape = RoundedCornerShape(10.dp)
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.Share, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(18.dp))
              Text(shareNotificationText ?: "", color = TextPrimary, fontSize = 12.sp, modifier = Modifier.weight(1f))
              IconButton(onClick = { shareNotificationText = null }, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted, modifier = Modifier.size(16.dp))
              }
            }
          }
        }
      }

      // 5. Facebook Feed Posts
      if (filteredList.isEmpty()) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = CyberCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
            shape = RoundedCornerShape(16.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(
                imageVector = Icons.Default.SportsEsports,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(48.dp)
              )
              Text(
                text = "Aucune publication trouvée",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Soyez le premier à publier sur le fil d'actualité CGC !",
                color = TextSecondary,
                fontSize = 12.sp
              )
              Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon, contentColor = CyberBlack),
                shape = RoundedCornerShape(10.dp)
              ) {
                Text("Publier maintenant", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      } else {
        items(filteredList, key = { it.id }) { announcement ->
          val isCommentsExpanded = expandedCommentsMap[announcement.id] == true
          FacebookPostCard(
            announcement = announcement,
            isCommentsExpanded = isCommentsExpanded,
            onToggleComments = {
              expandedCommentsMap = expandedCommentsMap + (announcement.id to !isCommentsExpanded)
            },
            onToggleReaction = onToggleReaction,
            onAddComment = { text ->
              onAddComment(announcement.id, text)
            },
            onShare = {
              onSharePost(announcement.id)
              shareNotificationText = "Publication partagée sur votre profil CGC ! (+15 XP)"
            }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }

  // --- Modals & Dialogs ---

  // 1. Facebook Post Creation Dialog
  if (showAddDialog) {
    FacebookCreatePostDialog(
      currentUserName = currentUserName,
      currentUserRole = currentUserRole,
      onDismiss = { showAddDialog = false },
      onSubmit = { title, content, tag ->
        onAddAnnouncement(title, content, tag)
        showAddDialog = false
      }
    )
  }

  // 2. Facebook Story Creation Dialog
  if (showCreateStoryDialog) {
    CreateStoryDialog(
      onDismiss = { showCreateStoryDialog = false },
      onSubmit = { title, tag ->
        onCreateStory(title, tag)
        showCreateStoryDialog = false
      }
    )
  }

  // 3. Live Stream Import Dialog (TikTok or YouTube)
  if (showLiveImportDialog) {
    ImportLiveStreamDialog(
      currentUserName = currentUserName,
      onDismiss = { showLiveImportDialog = false },
      onSubmit = { title, platform, streamUrl, gameName ->
        onAddLiveStream(title, platform, streamUrl, gameName)
        showLiveImportDialog = false
        shareNotificationText = "🔴 Votre direct ($platform) est désormais diffusé sur la CGC !"
      }
    )
  }

  // 4. Facebook Story Viewer Modal
  activeStoryViewing?.let { story ->
    StoryViewerDialog(
      story = story,
      onDismiss = { activeStoryViewing = null }
    )
  }

  // 5. Live Stream Player / Preview Modal
  activeLiveViewing?.let { liveStream ->
    LiveStreamPlayerModal(
      liveStream = liveStream,
      onDismiss = { activeLiveViewing = null }
    )
  }
}

// -------------------------------------------------------------
// Component: Live Streams Showcase Section (TikTok & YouTube)
// -------------------------------------------------------------
@Composable
fun FacebookLiveStreamsSection(
  liveStreams: List<LiveStream>,
  onImportLiveClick: () -> Unit,
  onWatchLiveClick: (LiveStream) -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_live")
  val pulseLiveAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseLiveAlpha"
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .testTag("live_streams_section"),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, PinkNeon.copy(alpha = 0.6f)),
    shape = RoundedCornerShape(14.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      // Header of Live Section
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(PinkNeon.copy(alpha = pulseLiveAlpha))
          )
          Text(
            text = "LIVES EN COURS (TIKTOK & YOUTUBE)",
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
          )
        }

        Surface(
          shape = RoundedCornerShape(14.dp),
          color = PinkNeon.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, PinkNeon),
          modifier = Modifier.clickable { onImportLiveClick() }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = PinkNeon, modifier = Modifier.size(12.dp))
            Text("Diffuser mon live", color = PinkNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      if (liveStreams.isEmpty()) {
        Text(
          text = "Aucun streamer en direct pour le moment. Cliquez sur 'Diffuser mon live' pour partager votre TikTok ou YouTube !",
          color = TextMuted,
          fontSize = 11.sp,
          modifier = Modifier.padding(vertical = 6.dp)
        )
      } else {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          liveStreams.forEach { stream ->
            LiveStreamCard(
              stream = stream,
              pulseAlpha = pulseLiveAlpha,
              onWatchClick = { onWatchLiveClick(stream) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun LiveStreamCard(
  stream: LiveStream,
  pulseAlpha: Float,
  onWatchClick: () -> Unit
) {
  val platformColor = when {
    stream.platform.contains("TikTok", ignoreCase = true) -> PinkNeon
    stream.platform.contains("YouTube", ignoreCase = true) -> Color(0xFFFF0000)
    else -> PurpleNeon
  }

  Card(
    modifier = Modifier
      .width(220.dp)
      .clickable { onWatchClick() }
      .testTag("live_card_${stream.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = DarkSurface),
    border = androidx.compose.foundation.BorderStroke(1.dp, platformColor.copy(alpha = 0.6f))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      // Top row: Pulsing Live badge & Platform chip
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = Color.Red.copy(alpha = 0.8f)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = pulseAlpha))
            )
            Text("EN DIRECT", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black)
          }
        }

        Surface(
          shape = RoundedCornerShape(4.dp),
          color = platformColor.copy(alpha = 0.2f),
          border = androidx.compose.foundation.BorderStroke(1.dp, platformColor)
        ) {
          Text(
            text = stream.platform,
            color = platformColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      // Title
      Text(
        text = stream.title,
        color = TextPrimary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        lineHeight = 16.sp
      )

      // Game & Streamer row
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Box(
          modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(platformColor),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = stream.streamerName.take(1).uppercase(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
          )
        }
        Text(
          text = stream.streamerName,
          color = TextSecondary,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "🎮 ${stream.gameName}",
          color = CyanGlow,
          fontSize = 10.sp,
          maxLines = 1,
          modifier = Modifier.weight(1f)
        )

        Button(
          onClick = onWatchClick,
          colors = ButtonDefaults.buttonColors(containerColor = platformColor, contentColor = Color.White),
          shape = RoundedCornerShape(6.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text("Regarder", fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// Component: Facebook Stories Bar
// -------------------------------------------------------------
@Composable
fun FacebookStoriesBar(
  currentUserName: String,
  stories: List<UserStory>,
  onAddStoryClick: () -> Unit,
  onStoryClick: (UserStory) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // "Créer une story" Card
    Card(
      modifier = Modifier
        .width(110.dp)
        .height(165.dp)
        .clickable { onAddStoryClick() }
        .testTag("create_story_card"),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = CyberCard),
      border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(
              Brush.verticalGradient(
                listOf(CyanNeon.copy(alpha = 0.4f), CyberCardHighlight)
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.SportsEsports,
            contentDescription = null,
            tint = CyanGlow,
            modifier = Modifier.size(36.dp)
          )
        }
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .padding(vertical = 8.dp, horizontal = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(FacebookBlue),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Ajouter",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Créer une story",
              color = TextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // User Stories
    stories.forEach { story ->
      Card(
        modifier = Modifier
          .width(110.dp)
          .height(165.dp)
          .clickable { onStoryClick(story) }
          .testTag("story_card_${story.id}"),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
          1.5.dp,
          if (story.isCreator) GoldNeon else FacebookBlue
        )
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(
                  Color(story.gradientStart),
                  Color(story.gradientEnd).copy(alpha = 0.8f),
                  CyberBlack
                )
              )
            )
            .padding(8.dp)
        ) {
          Box(
            modifier = Modifier
              .align(Alignment.TopStart)
              .size(34.dp)
              .clip(CircleShape)
              .background(CyberBlack)
              .border(
                2.dp,
                if (story.isCreator) GoldNeon else EmeraldNeon,
                CircleShape
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = story.authorName.take(1).uppercase(),
              color = if (story.isCreator) GoldNeon else TextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.Black
            )
          }

          Surface(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(top = 2.dp),
            shape = RoundedCornerShape(4.dp),
            color = CyberBlack.copy(alpha = 0.7f)
          ) {
            Text(
              text = story.tag,
              color = if (story.isCreator) GoldNeon else CyanGlow,
              fontSize = 8.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
          }

          Column(modifier = Modifier.align(Alignment.BottomStart)) {
            Text(
              text = story.authorName,
              color = TextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1
            )
            Text(
              text = story.title,
              color = Color.White,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium,
              maxLines = 2,
              lineHeight = 12.sp
            )
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// Component: Facebook Post Composer Card
// -------------------------------------------------------------
@Composable
fun FacebookPostComposerCard(
  currentUserName: String,
  onOpenComposer: () -> Unit,
  onOpenLiveImport: () -> Unit = {}
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .testTag("facebook_post_composer_card"),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
    shape = RoundedCornerShape(14.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(CyberCardHighlight)
            .border(2.dp, EmeraldNeon, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = currentUserName.take(1).uppercase(),
            color = EmeraldGlow,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
        }

        Surface(
          modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onOpenComposer() },
          color = DarkSurface,
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Quoi de neuf, $currentUserName ? Exprimez-vous...",
              color = TextMuted,
              fontSize = 12.sp
            )
          }
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = CyberCardBorder.copy(alpha = 0.5f)
      )

      // 3 Quick Action Buttons: En direct (Live TikTok/YouTube), Photo/Clip, Tournoi
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        // En direct: opens TikTok / YouTube Live Import
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onOpenLiveImport() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("live_composer_button"),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Videocam,
            contentDescription = null,
            tint = PinkNeon,
            modifier = Modifier.size(18.dp)
          )
          Text(text = "🔴 En direct", color = PinkNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onOpenComposer() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Image,
            contentDescription = null,
            tint = EmeraldNeon,
            modifier = Modifier.size(18.dp)
          )
          Text(text = "Photo/Clip", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }

        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onOpenComposer() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = GoldNeon,
            modifier = Modifier.size(18.dp)
          )
          Text(text = "Tournoi", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// Component: Facebook Post Card (Rich with reactions & comments)
// -------------------------------------------------------------
@Composable
fun FacebookPostCard(
  announcement: Announcement,
  isCommentsExpanded: Boolean,
  onToggleComments: () -> Unit,
  onToggleReaction: (String, String) -> Unit,
  onAddComment: (String) -> Unit,
  onShare: () -> Unit
) {
  var showOptionsMenu by remember { mutableStateOf(false) }
  var newCommentInput by remember { mutableStateOf("") }

  val totalReactions = announcement.fireCount + announcement.gamepadCount +
    announcement.trophyCount + announcement.heartCount

  val tagColor = when (announcement.tag) {
    "CGC" -> GoldNeon
    "Tournoi" -> CyanNeon
    "Event" -> PinkNeon
    "Recrutement" -> EmeraldNeon
    else -> PurpleNeon
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .testTag("facebook_post_${announcement.id}"),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
    shape = RoundedCornerShape(14.dp)
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(CyberCardHighlight)
              .border(
                2.dp,
                if (announcement.authorTitle.contains("Créateur", ignoreCase = true)) GoldNeon else EmeraldNeon,
                CircleShape
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = announcement.authorName.take(1).uppercase(),
              color = if (announcement.authorTitle.contains("Créateur", ignoreCase = true)) GoldNeon else TextPrimary,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Column {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(
                text = announcement.authorName,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              if (announcement.authorTitle.contains("Créateur", ignoreCase = true)) {
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = GoldNeon.copy(alpha = 0.2f),
                  border = androidx.compose.foundation.BorderStroke(1.dp, GoldNeon)
                ) {
                  Text(
                    text = "👑 CRÉATEUR",
                    color = GoldNeon,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                text = announcement.timestamp,
                color = TextMuted,
                fontSize = 11.sp
              )
              Text(text = "•", color = TextMuted, fontSize = 11.sp)
              Icon(
                imageVector = Icons.Default.Public,
                contentDescription = "Public",
                tint = TextMuted,
                modifier = Modifier.size(12.dp)
              )
              Text(
                text = announcement.privacyLevel,
                color = TextMuted,
                fontSize = 10.sp
              )
            }
          }
        }

        Box {
          IconButton(onClick = { showOptionsMenu = true }) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "Options",
              tint = TextMuted
            )
          }

          DropdownMenu(
            expanded = showOptionsMenu,
            onDismissRequest = { showOptionsMenu = false },
            modifier = Modifier.background(CyberCard)
          ) {
            DropdownMenuItem(
              text = { Text("Partager cette annonce", color = TextPrimary) },
              onClick = {
                showOptionsMenu = false
                onShare()
              }
            )
            DropdownMenuItem(
              text = { Text("🔒 Vérification Zéro-Fuite : Données protégées", color = EmeraldNeon) },
              onClick = { showOptionsMenu = false }
            )
          }
        }
      }

      // Post Body
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 4.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = tagColor.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, tagColor.copy(alpha = 0.5f))
        ) {
          Text(
            text = "#${announcement.tag.uppercase()}",
            color = tagColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = announcement.title,
          color = TextPrimary,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = announcement.content,
          color = TextSecondary,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Social Counts Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          if (totalReactions > 0) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              if (announcement.fireCount > 0) Text("🔥", fontSize = 12.sp)
              if (announcement.gamepadCount > 0) Text("🎮", fontSize = 12.sp)
              if (announcement.trophyCount > 0) Text("🏆", fontSize = 12.sp)
              if (announcement.heartCount > 0) Text("❤️", fontSize = 12.sp)
            }
            Text(
              text = "$totalReactions",
              color = TextSecondary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          } else {
            Text(
              text = "Soyez le premier à réagir",
              color = TextMuted,
              fontSize = 11.sp
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "${announcement.comments.size} commentaires",
            color = TextMuted,
            fontSize = 11.sp,
            modifier = Modifier.clickable { onToggleComments() }
          )
          Text(text = "•", color = TextMuted, fontSize = 11.sp)
          Text(
            text = "${announcement.shareCount} partages",
            color = TextMuted,
            fontSize = 11.sp
          )
        }
      }

      HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.5f))

      // Facebook Action Bar (J'aime, Commenter, Partager)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onToggleReaction(announcement.id, "fire") }
            .padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = if (announcement.userReactedFire) Icons.Default.Whatshot else Icons.Default.ThumbUp,
            contentDescription = "Réagir",
            tint = if (announcement.userReactedFire) GoldNeon else TextSecondary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = if (announcement.userReactedFire) "J'adore" else "J'aime",
            color = if (announcement.userReactedFire) GoldNeon else TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onToggleComments() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.ChatBubbleOutline,
            contentDescription = "Commenter",
            tint = if (isCommentsExpanded) CyanNeon else TextSecondary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "Commenter",
            color = if (isCommentsExpanded) CyanNeon else TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onShare() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Partager",
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "Partager",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Quick Reaction Bar (Emojis)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(DarkSurface)
          .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Réactions rapides :", color = TextMuted, fontSize = 10.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "🔥 ${announcement.fireCount}",
            fontSize = 12.sp,
            modifier = Modifier.clickable { onToggleReaction(announcement.id, "fire") }
          )
          Text(
            text = "🎮 ${announcement.gamepadCount}",
            fontSize = 12.sp,
            modifier = Modifier.clickable { onToggleReaction(announcement.id, "gamepad") }
          )
          Text(
            text = "🏆 ${announcement.trophyCount}",
            fontSize = 12.sp,
            modifier = Modifier.clickable { onToggleReaction(announcement.id, "trophy") }
          )
          Text(
            text = "❤️ ${announcement.heartCount}",
            fontSize = 12.sp,
            modifier = Modifier.clickable { onToggleReaction(announcement.id, "heart") }
          )
        }
      }

      // Expandable Comments Section
      AnimatedVisibility(visible = isCommentsExpanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface.copy(alpha = 0.5f))
            .padding(12.dp)
        ) {
          if (announcement.comments.isEmpty()) {
            Text(
              text = "Aucun commentaire pour le moment. Écrivez le premier !",
              color = TextMuted,
              fontSize = 11.sp,
              modifier = Modifier.padding(vertical = 8.dp)
            )
          } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              announcement.comments.forEach { comment ->
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(30.dp)
                      .clip(CircleShape)
                      .background(CyberCardHighlight),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = comment.authorName.take(1).uppercase(),
                      color = if (comment.authorRole == UserRole.CREATOR) GoldNeon else TextPrimary,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }

                  Column(modifier = Modifier.weight(1f)) {
                    Surface(
                      shape = RoundedCornerShape(12.dp),
                      color = CyberCard,
                      border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
                    ) {
                      Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                          verticalAlignment = Alignment.CenterVertically,
                          horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                          Text(
                            text = comment.authorName,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                          )
                          if (comment.authorRole == UserRole.CREATOR) {
                            Text(
                              text = "👑 Créateur",
                              color = GoldNeon,
                              fontSize = 9.sp,
                              fontWeight = FontWeight.Bold
                            )
                          }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                          text = comment.content,
                          color = TextSecondary,
                          fontSize = 12.sp
                        )
                      }
                    }
                    Text(
                      text = "${comment.timestamp} • J'aime",
                      color = TextMuted,
                      fontSize = 10.sp,
                      modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                    )
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Comment Input field
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = newCommentInput,
              onValueChange = { newCommentInput = it },
              placeholder = {
                Text(
                  text = "Écrivez un commentaire public...",
                  color = TextMuted,
                  fontSize = 12.sp
                )
              },
              modifier = Modifier
                .weight(1f)
                .height(48.dp),
              shape = RoundedCornerShape(24.dp),
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CyberCard,
                unfocusedContainerColor = CyberCard,
                focusedBorderColor = FacebookBlue,
                unfocusedBorderColor = CyberCardBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
              )
            )

            IconButton(
              onClick = {
                if (newCommentInput.isNotBlank()) {
                  onAddComment(newCommentInput)
                  newCommentInput = ""
                }
              },
              enabled = newCommentInput.isNotBlank(),
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (newCommentInput.isNotBlank()) FacebookBlue else CyberCardBorder)
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Envoyer",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// Dialog: Import Live Stream (TikTok or YouTube)
// -------------------------------------------------------------
@Composable
fun ImportLiveStreamDialog(
  currentUserName: String,
  onDismiss: () -> Unit,
  onSubmit: (title: String, platform: String, streamUrl: String, gameName: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var streamUrl by remember { mutableStateOf("") }
  var selectedPlatform by remember { mutableStateOf("TikTok Live") }
  var selectedGame by remember { mutableStateOf("EA SPORTS FC 25") }

  val platforms = listOf("TikTok Live", "YouTube Live", "Twitch")
  val games = listOf("EA SPORTS FC 25", "Call of Duty: Warzone", "Tekken 8", "PUBG Mobile", "Mobile Legends")

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("import_live_stream_dialog"),
      colors = CardDefaults.cardColors(containerColor = CyberCard),
      shape = RoundedCornerShape(16.dp),
      border = androidx.compose.foundation.BorderStroke(1.5.dp, PinkNeon)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Dialog Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.Videocam, contentDescription = null, tint = PinkNeon, modifier = Modifier.size(20.dp))
            Text("Diffuser mon Live Stream", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
          }
        }

        Text(
          text = "Importez le lien de votre direct TikTok ou YouTube pour le diffuser en temps réel sur la communauté CGC 🇨🇩.",
          color = TextSecondary,
          fontSize = 11.sp,
          lineHeight = 15.sp
        )

        HorizontalDivider(color = CyberCardBorder)

        // Platform Selector Pills
        Text("Plateforme de streaming :", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          platforms.forEach { platform ->
            val isSelected = selectedPlatform == platform
            val activeColor = when (platform) {
              "TikTok Live" -> PinkNeon
              "YouTube Live" -> Color(0xFFFF0000)
              else -> PurpleNeon
            }
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) activeColor else DarkSurface,
              border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) activeColor else CyberCardBorder),
              modifier = Modifier
                .weight(1f)
                .clickable { selectedPlatform = platform }
            ) {
              Text(
                text = platform,
                color = if (isSelected) Color.White else TextSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
            }
          }
        }

        // Live Title Input
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          placeholder = { Text("Titre de votre live (ex: Finale Tournoi Kinshasa)", color = TextMuted, fontSize = 12.sp) },
          label = { Text("Titre du Stream") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PinkNeon,
            unfocusedBorderColor = CyberCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        // Stream URL Input (TikTok or YouTube)
        OutlinedTextField(
          value = streamUrl,
          onValueChange = { streamUrl = it },
          placeholder = {
            Text(
              if (selectedPlatform == "TikTok Live") "https://www.tiktok.com/@votre_pseudo/live"
              else "https://www.youtube.com/live/..."
            )
          },
          label = { Text("Lien du Live ($selectedPlatform)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PinkNeon,
            unfocusedBorderColor = CyberCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        // Game selector
        Text("Jeu diffusé :", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          games.forEach { game ->
            val isSelected = selectedGame == game
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) CyanNeon else DarkSurface,
              border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CyanGlow else CyberCardBorder),
              modifier = Modifier.clickable { selectedGame = game }
            ) {
              Text(
                text = game,
                color = if (isSelected) CyberBlack else TextSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }

        // Security Anti-Leak Notice
        Card(
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon.copy(alpha = 0.4f)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(14.dp))
            Text(
              text = "Zéro Fuite : Seul le lien public est diffusé. Vos identifiants privés restent 100% protégés.",
              color = TextSecondary,
              fontSize = 10.sp
            )
          }
        }

        // Submit Button
        Button(
          onClick = {
            if (title.isNotBlank() && streamUrl.isNotBlank()) {
              onSubmit(title.trim(), selectedPlatform, streamUrl.trim(), selectedGame)
            }
          },
          enabled = title.isNotBlank() && streamUrl.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = PinkNeon,
            contentColor = Color.White
          )
        ) {
          Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Diffuser en direct sur la CGC", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// Dialog: Live Stream Player / Preview Modal
// -------------------------------------------------------------
@Composable
fun LiveStreamPlayerModal(
  liveStream: LiveStream,
  onDismiss: () -> Unit
) {
  val uriHandler = LocalUriHandler.current

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      colors = CardDefaults.cardColors(containerColor = CyberBlack),
      shape = RoundedCornerShape(16.dp),
      border = androidx.compose.foundation.BorderStroke(2.dp, PinkNeon)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Player Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color.Red
            ) {
              Text(
                text = "🔴 EN DIRECT",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
            Text(liveStream.platform, color = PinkNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
          }
        }

        // Fake live screen video canvas
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
              Brush.verticalGradient(
                listOf(Color(0xFF1E1B4B), Color(0xFF0F172A), CyberBlack)
              )
            )
            .border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(PinkNeon.copy(alpha = 0.8f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(36.dp))
            }
            Text(liveStream.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("Diffusé par ${liveStream.streamerName} • 🎮 ${liveStream.gameName}", color = TextSecondary, fontSize = 11.sp)
          }
        }

        // Stream URL & Direct Open Button
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = DarkSurface,
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Lien officiel du live :", color = TextMuted, fontSize = 10.sp)
              Text(liveStream.streamUrl, color = CyanGlow, fontSize = 11.sp, maxLines = 1)
            }
            Button(
              onClick = {
                try {
                  uriHandler.openUri(liveStream.streamUrl)
                } catch (e: Exception) {
                  // Fallback
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = PinkNeon),
              shape = RoundedCornerShape(6.dp),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Ouvrir", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        // Live Chat Simulation
        Text("Tchat en direct de la communauté CGC :", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text("🎮 Patrick K. : Force à toi pour la finale !", color = TextPrimary, fontSize = 11.sp)
          Text("🔥 Sarah M. : Superbe action à la 80e minute !", color = TextPrimary, fontSize = 11.sp)
          Text("🇨🇩 Jonathan B. : Kinshasa est derrière toi !", color = TextPrimary, fontSize = 11.sp)
        }

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = DarkSurface, contentColor = TextPrimary),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Fermer le lecteur")
        }
      }
    }
  }
}

// -------------------------------------------------------------
// Dialog: Facebook Style Create Post
// -------------------------------------------------------------
@Composable
fun FacebookCreatePostDialog(
  currentUserName: String,
  currentUserRole: UserRole,
  onDismiss: () -> Unit,
  onSubmit: (title: String, content: String, tag: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var selectedTag by remember { mutableStateOf("CGC") }
  var privacyMode by remember { mutableStateOf("Public (Tous les gamers)") }

  val tagOptions = listOf("CGC", "Tournoi", "Event", "Recrutement", "Update")

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .testTag("facebook_create_post_dialog"),
      colors = CardDefaults.cardColors(containerColor = CyberCard),
      shape = RoundedCornerShape(16.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, FacebookBlue)
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
          Text(
            text = "Créer une publication",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
          }
        }

        HorizontalDivider(color = CyberCardBorder)

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(FacebookBlue),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = currentUserName.take(1).uppercase(),
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
          }

          Column {
            Text(text = currentUserName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = DarkSurface,
              border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(Icons.Default.Public, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(11.dp))
                Text(privacyMode, color = CyanGlow, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }
          }
        }

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          placeholder = { Text("Titre de la publication...", color = TextMuted, fontSize = 13.sp) },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DarkSurface,
            unfocusedContainerColor = DarkSurface,
            focusedBorderColor = FacebookBlue,
            unfocusedBorderColor = CyberCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          placeholder = {
            Text(
              "Qu'avez-vous en tête ? Partagez une annonce, un clip ou défiez un joueur...",
              color = TextMuted,
              fontSize = 13.sp
            )
          },
          minLines = 4,
          maxLines = 6,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DarkSurface,
            unfocusedContainerColor = DarkSurface,
            focusedBorderColor = FacebookBlue,
            unfocusedBorderColor = CyberCardBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          )
        )

        Text("Catégorie :", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          tagOptions.forEach { tag ->
            val isSelected = selectedTag == tag
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) EmeraldNeon else DarkSurface,
              border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) EmeraldGlow else CyberCardBorder),
              modifier = Modifier.clickable { selectedTag = tag }
            ) {
              Text(
                text = "#$tag",
                color = if (isSelected) CyberBlack else TextSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }
          }
        }

        Card(
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon.copy(alpha = 0.4f)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(14.dp))
            Text(
              text = "Protection anti-fuite : vos identifiants (email & code) restent 100% privés.",
              color = TextSecondary,
              fontSize = 10.sp
            )
          }
        }

        Button(
          onClick = {
            if (title.isNotBlank() && content.isNotBlank()) {
              onSubmit(title.trim(), content.trim(), selectedTag)
            }
          },
          enabled = title.isNotBlank() && content.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = FacebookBlue,
            contentColor = Color.White
          )
        ) {
          Text("Publier sur la CGC", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// Dialog: Create User Story
// -------------------------------------------------------------
@Composable
fun CreateStoryDialog(
  onDismiss: () -> Unit,
  onSubmit: (title: String, tag: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var tag by remember { mutableStateOf("CLIPS") }

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = CyberCard,
    title = {
      Text("Ajouter à votre Story CGC", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Partagez un instant gaming éphémère (24h) visible par tous les membres.", color = TextSecondary, fontSize = 12.sp)
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          placeholder = { Text("Ex: Victoire 5-0 sur FC 25 ce soir !", color = TextMuted, fontSize = 12.sp) },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
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
          if (title.isNotBlank()) {
            onSubmit(title.trim(), tag)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon, contentColor = CyberBlack)
      ) {
        Text("Partager dans la story", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Annuler", color = TextSecondary)
      }
    }
  )
}

// -------------------------------------------------------------
// Dialog: Story Viewer Modal
// -------------------------------------------------------------
@Composable
fun StoryViewerDialog(
  story: UserStory,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .height(480.dp),
      shape = RoundedCornerShape(16.dp),
      border = androidx.compose.foundation.BorderStroke(2.dp, if (story.isCreator) GoldNeon else FacebookBlue)
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              listOf(
                Color(story.gradientStart),
                Color(story.gradientEnd),
                CyberBlack
              )
            )
          )
          .padding(16.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White.copy(alpha = 0.5f))
            .align(Alignment.TopCenter)
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .align(Alignment.TopCenter),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(CyberBlack)
                .border(2.dp, Color.White, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = story.authorName.take(1).uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold
              )
            }
            Column {
              Text(text = story.authorName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text(text = story.timestamp, color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = Color.White)
          }
        }

        Column(
          modifier = Modifier.align(Alignment.Center),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = CyberBlack.copy(alpha = 0.6f)
          ) {
            Text(
              text = story.tag,
              color = if (story.isCreator) GoldNeon else CyanGlow,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = story.title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            lineHeight = 26.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
          )
        }

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(bottom = 8.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = CyberBlack.copy(alpha = 0.7f)),
            shape = RoundedCornerShape(20.dp)
          ) {
            Text("Fermer la story", color = Color.White, fontSize = 12.sp)
          }
        }
      }
    }
  }
}
