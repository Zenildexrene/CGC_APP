package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.DynamicFeed
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.PruconRepository
import com.example.ui.components.PruconHeader
import com.example.ui.screens.GroupsChatScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TimelineScreen
import com.example.ui.screens.TournamentsScreen
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardHighlight
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

enum class PruconTab(
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String
) {
  TIMELINE("Timeline", Icons.Filled.DynamicFeed, Icons.Outlined.DynamicFeed, "tab_timeline"),
  TOURNAMENTS("Tournois", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents, "tab_tournaments"),
  GROUPS("Salons", Icons.Filled.Forum, Icons.Outlined.Forum, "tab_groups"),
  PROFILE("Gamer Card", Icons.Filled.Badge, Icons.Outlined.Badge, "tab_profile"),
  LEADERBOARD("Classement", Icons.Filled.Leaderboard, Icons.Outlined.Leaderboard, "tab_leaderboard")
}

@Composable
fun PruconApp(
  repository: PruconRepository = remember { PruconRepository() },
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(PruconTab.TIMELINE) }
  var showCreatorConsole by remember { mutableStateOf(false) }
  var showAuthDialog by remember { mutableStateOf(false) }

  val profile by repository.profile.collectAsState()
  val allUsers by repository.allUsers.collectAsState()
  val announcements by repository.announcements.collectAsState()
  val tournaments by repository.tournaments.collectAsState()
  val groups by repository.groups.collectAsState()
  val chatMessages by repository.chatMessages.collectAsState()
  val leaderboard by repository.leaderboard.collectAsState()

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = CyberBlack,
    topBar = {
      PruconHeader(
        profile = profile,
        onProfileClick = { selectedTab = PruconTab.PROFILE },
        onCreatorConsoleClick = { showCreatorConsole = true },
        onAuthClick = { showAuthDialog = true }
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = DarkSurface,
        tonalElevation = 8.dp,
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .testTag("prucon_bottom_navigation")
      ) {
        PruconTab.values().forEach { tab ->
          val isSelected = selectedTab == tab
          NavigationBarItem(
            selected = isSelected,
            onClick = { selectedTab = tab },
            icon = {
              Icon(
                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                contentDescription = tab.label,
                modifier = Modifier.size(22.dp)
              )
            },
            label = {
              Text(
                text = tab.label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = CyberBlack,
              selectedTextColor = EmeraldGlow,
              indicatorColor = EmeraldNeon,
              unselectedIconColor = TextMuted,
              unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag(tab.testTag)
          )
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(CyberBlack)
    ) {
      when (selectedTab) {
        PruconTab.TIMELINE -> {
          TimelineScreen(
            announcements = announcements,
            onAddAnnouncement = { title, content, tag ->
              repository.addAnnouncement(title, content, tag)
            },
            onToggleReaction = { annId, reaction ->
              repository.toggleReaction(annId, reaction)
            }
          )
        }
        PruconTab.TOURNAMENTS -> {
          TournamentsScreen(
            tournaments = tournaments,
            onRegister = { tournamentId ->
              repository.registerTournament(tournamentId)
            }
          )
        }
        PruconTab.GROUPS -> {
          GroupsChatScreen(
            groups = groups,
            chatMessages = chatMessages,
            onSendMessage = { groupId, content ->
              repository.sendMessage(groupId, content)
            },
            onRollDice = { groupId ->
              repository.rollDiceInChat(groupId)
            },
            onJoinGroup = { groupId ->
              repository.joinGroup(groupId)
            }
          )
        }
        PruconTab.PROFILE -> {
          ProfileScreen(
            profile = profile,
            onConvertXp = { amount -> repository.convertXpToTokens(amount) },
            onGiveXp = { amount -> repository.giveXp(amount) },
            onDailySpin = { repository.playDailySpin() },
            onUpdateProfile = { name, title, clan ->
              repository.updateProfile(name, title, clan)
            },
            onOpenCreatorConsole = { showCreatorConsole = true },
            onOpenAuth = { showAuthDialog = true }
          )
        }
        PruconTab.LEADERBOARD -> {
          LeaderboardScreen(leaderboard = leaderboard)
        }
      }
    }
  }

  // Creator Console Modal
  if (showCreatorConsole) {
    com.example.ui.components.CreatorConsoleDialog(
      currentProfile = profile,
      allUsers = allUsers,
      tournaments = tournaments,
      announcements = announcements,
      groups = groups,
      onNominateRole = { userId, role -> repository.nominateRole(userId, role) },
      onDeleteUser = { userId -> repository.deleteUser(userId) },
      onRegisterPlayer = { name, email, code -> repository.registerNewPlayer(name, email, code) },
      onCreateTournament = { title, game, platform, prize, fee, date, slots, desc, rules ->
        repository.createTournament(title, game, platform, prize, fee, date, slots, desc, rules)
      },
      onDeleteTournament = { id -> repository.deleteTournament(id) },
      onCreateAnnouncement = { title, content, tag, isPinned ->
        repository.addAnnouncement(title, content, tag, isPinned)
      },
      onDeleteAnnouncement = { id -> repository.deleteAnnouncement(id) },
      onCreateGroup = { name, cat, desc -> repository.createGroup(name, cat, desc) },
      onDeleteGroup = { id -> repository.deleteGroup(id) },
      onDismiss = { showCreatorConsole = false }
    )
  }

  // Auth & Account switcher modal
  if (showAuthDialog) {
    com.example.ui.components.AuthDialog(
      currentProfile = profile,
      allUsers = allUsers,
      onLogin = { email, code -> repository.login(email, code) },
      onRegister = { name, email, code -> repository.registerNewPlayer(name, email, code) },
      onSwitchUser = { userId -> repository.switchUser(userId) },
      onDismiss = { showAuthDialog = false }
    )
  }
}
