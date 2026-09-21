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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.DynamicFeed
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Home
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
  TIMELINE("Accueil", Icons.Filled.Home, Icons.Outlined.Home, "tab_timeline"),
  TOURNAMENTS("Tournois", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents, "tab_tournaments"),
  GROUPS("Discord", Icons.Filled.Forum, Icons.Outlined.Forum, "tab_groups"),
  PROFILE("Profil", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle, "tab_profile"),
  LEADERBOARD("Classement", Icons.Filled.Leaderboard, Icons.Outlined.Leaderboard, "tab_leaderboard")
}

@Composable
fun PruconApp(
  repository: PruconRepository = remember { PruconRepository() },
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(PruconTab.TIMELINE) }
  var showCreatorConsole by remember { mutableStateOf(false) }
  var showAdminDashboard by remember { mutableStateOf(false) }
  var showSafetyDashboard by remember { mutableStateOf(false) }
  var showNotificationsDialog by remember { mutableStateOf(false) }
  var showAuthDialog by remember { mutableStateOf(false) }
  var showMenuDialog by remember { mutableStateOf(false) }

  val profile by repository.profile.collectAsState()
  val allUsers by repository.allUsers.collectAsState()
  val announcements by repository.announcements.collectAsState()
  val tournaments by repository.tournaments.collectAsState()
  val groups by repository.groups.collectAsState()
  val chatMessages by repository.chatMessages.collectAsState()
  val leaderboard by repository.leaderboard.collectAsState()
  val stories by repository.stories.collectAsState()
  val discordChannels by repository.discordChannels.collectAsState()
  val privacySettings by repository.privacySettings.collectAsState()
  val liveStreams by repository.liveStreams.collectAsState()
  val aboutCgcMessages by repository.aboutCgcMessages.collectAsState()

  val managedUsers by repository.managedUsers.collectAsState()
  val platformReports by repository.platformReports.collectAsState()
  val managedRooms by repository.managedRooms.collectAsState()
  val auditLogs by repository.auditLogs.collectAsState()
  val securityAlerts by repository.securityAlerts.collectAsState()
  val supportTickets by repository.supportTickets.collectAsState()
  val apiKeys by repository.apiKeys.collectAsState()
  val systemMetrics by repository.systemMetrics.collectAsState()

  // User Freedom, Comfort and Safety states
  val userPrivacySettings by repository.userPrivacySettings.collectAsState()
  val userCommunicationControls by repository.userCommunicationControls.collectAsState()
  val blockedUsers by repository.blockedUsers.collectAsState()
  val mutedUsers by repository.mutedUsers.collectAsState()
  val submittedReports by repository.submittedReports.collectAsState()
  val activeSessions by repository.activeSessions.collectAsState()
  val emergencyShield by repository.emergencyShield.collectAsState()
  val twoFactorEnabled by repository.twoFactorEnabled.collectAsState()
  val userAppeals by repository.userAppeals.collectAsState()
  val notifications by repository.notifications.collectAsState()
  val notificationPreferences by repository.notificationPreferences.collectAsState()
  val unreadNotificationsCount = notifications.count { !it.isRead }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = CyberBlack,
    topBar = {
      PruconHeader(
        profile = profile,
        onProfileClick = { selectedTab = PruconTab.PROFILE },
        onCreatorConsoleClick = { showCreatorConsole = true },
        onAdminDashboardClick = { showAdminDashboard = true },
        onSafetyDashboardClick = { showSafetyDashboard = true },
        onNotificationsClick = { showNotificationsDialog = true },
        unreadNotificationsCount = unreadNotificationsCount,
        onAuthClick = { showAuthDialog = true },
        onMenuClick = { showMenuDialog = true }
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
            stories = stories,
            liveStreams = liveStreams,
            leaderboard = leaderboard,
            currentUserName = profile.secretName,
            currentUserRole = profile.role,
            onAddAnnouncement = { title, content, tag ->
              repository.addAnnouncement(title, content, tag)
            },
            onToggleReaction = { annId, reaction ->
              repository.toggleReaction(annId, reaction)
            },
            onAddComment = { annId, text ->
              repository.addCommentToAnnouncement(annId, text)
            },
            onSharePost = { annId ->
              repository.shareAnnouncement(annId)
            },
            onCreateStory = { title, tag ->
              repository.createStory(title, tag)
            },
            onAddLiveStream = { title, platform, streamUrl, gameName ->
              repository.addLiveStream(title, platform, streamUrl, gameName)
            },
            onViewFullLeaderboard = {
              selectedTab = PruconTab.LEADERBOARD
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
          val blockedNames = blockedUsers.map { it.username.lowercase().trim() }.toSet()
          GroupsChatScreen(
            groups = groups,
            chatMessages = chatMessages,
            discordChannels = discordChannels,
            allUsers = allUsers,
            currentProfile = profile,
            blockedUsernames = blockedNames,
            onQuickBlockUser = { username ->
              repository.blockUser(username, username, "Bloqué depuis le chat communautaire")
            },
            onQuickReportMessage = { senderName, content, category ->
              val cat = when (category) {
                "harassment" -> com.example.data.model.SafetyReportCategory.HARASSMENT
                "hate_speech" -> com.example.data.model.SafetyReportCategory.HATE_OR_ABUSE
                "spam" -> com.example.data.model.SafetyReportCategory.SPAM
                "scam" -> com.example.data.model.SafetyReportCategory.SCAM
                "illegal_content" -> com.example.data.model.SafetyReportCategory.ILLEGAL_CONTENT
                else -> com.example.data.model.SafetyReportCategory.OTHER
              }
              repository.submitUserReport(
                category = cat,
                targetType = "MESSAGE",
                targetIdentifier = senderName,
                reason = "Signalement Discord : $content",
                isAnonymous = true
              )
            },
            onSendMessage = { groupId, content ->
              repository.sendMessage(groupId, content)
            },
            onRollDice = { groupId ->
              repository.rollDiceInChat(groupId)
            },
            onJoinGroup = { groupId ->
              repository.joinGroup(groupId)
            },
            onAddReaction = { groupId, msgId, reaction ->
              repository.addChatMessageReaction(groupId, msgId, reaction)
            }
          )
        }
        PruconTab.PROFILE -> {
          ProfileScreen(
            profile = profile,
            privacySettings = privacySettings,
            announcements = announcements,
            onConvertXp = { amount -> repository.convertXpToTokens(amount) },
            onGiveXp = { amount -> repository.giveXp(amount) },
            onDailySpin = { repository.playDailySpin() },
            onUpdateProfile = { name, title, clan ->
              repository.updateProfile(name, title, clan)
            },
            onUpdateExtendedProfile = { name, title, clan, bio, location, coverTheme ->
              repository.updateExtendedProfile(name, title, clan, bio, location, coverTheme)
            },
            onUpdateJournalCustomization = { bio, motto, location, coverTheme, platform, clan ->
              repository.updateJournalCustomization(bio, motto, location, coverTheme, platform, clan)
            },
            onToggleHideEmail = { repository.toggleHideEmail() },
            onToggleHideCode = { repository.toggleHideSecretCode() },
            onToggleE2EE = { repository.toggleE2EE() },
            onAddAnnouncement = { title, content, tag ->
              repository.addJournalPost(title, content, tag)
            },
            onDeletePost = { postId ->
              repository.deleteMyPost(postId)
            },
            onOpenCreatorConsole = { showCreatorConsole = true },
            onOpenAdminDashboard = { showAdminDashboard = true },
            onOpenSafetyDashboard = { showSafetyDashboard = true },
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
      onRegisterPlayer = { pseudo, uid, code -> repository.registerNewPlayer(pseudo, uid, code) },
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
      onLogin = { identifier, code -> repository.login(identifier, code) },
      onRegister = { pseudo, uid, code -> repository.registerNewPlayer(pseudo, uid, code) },
      onSwitchUser = { userId -> repository.switchUser(userId) },
      onDismiss = { showAuthDialog = false }
    )
  }

  // Menu & Settings with Admin About CGC modal
  if (showMenuDialog) {
    com.example.ui.screens.MenuDialog(
      profile = profile,
      privacySettings = privacySettings,
      aboutMessages = aboutCgcMessages,
      onDismiss = { showMenuDialog = false },
      onProfileClick = {
        showMenuDialog = false
        selectedTab = PruconTab.PROFILE
      },
      onCreatorConsoleClick = {
        showMenuDialog = false
        showCreatorConsole = true
      },
      onAdminDashboardClick = {
        showMenuDialog = false
        showAdminDashboard = true
      },
      onSafetyDashboardClick = {
        showMenuDialog = false
        showSafetyDashboard = true
      },
      onAuthClick = {
        showMenuDialog = false
        showAuthDialog = true
      },
      onToggleHideEmail = { repository.toggleHideEmail() },
      onToggleHideCode = { repository.toggleHideSecretCode() },
      onToggleE2EE = { repository.toggleE2EE() },
      onAddAboutMessage = { title, content -> repository.addAboutCgcMessage(title, content) },
      onDeleteAboutMessage = { id -> repository.deleteAboutCgcMessage(id) }
    )
  }

  // Advanced Admin Dashboard (Comprehensive Control Center)
  if (showAdminDashboard) {
    com.example.ui.components.AdvancedAdminDashboardDialog(
      currentProfile = profile,
      managedUsers = managedUsers,
      reports = platformReports,
      rooms = managedRooms,
      auditLogs = auditLogs,
      securityAlerts = securityAlerts,
      tickets = supportTickets,
      apiKeys = apiKeys,
      metrics = systemMetrics,
      onUpdateUserStatus = { userId, status, reason -> repository.updateUserStatus(userId, status, reason) },
      onWarnUser = { userId, reason -> repository.warnUser(userId, reason) },
      onResolveReport = { reportId, status, note -> repository.resolveReport(reportId, status, note) },
      onToggleLockRoom = { roomId -> repository.toggleLockRoom(roomId) },
      onCreateRoom = { name, cat, desc, isPriv -> repository.createManagedRoom(name, cat, desc, isPriv) },
      onUpdateTicketStatus = { ticketId, status, note -> repository.updateTicketStatus(ticketId, status, note) },
      onTriggerBackup = { repository.triggerManualBackup() },
      onDismiss = { showAdminDashboard = false }
    )
  }

  // User Freedom, Comfort and Safety System (Privacy Center & Personal Security)
  if (showSafetyDashboard) {
    com.example.ui.components.UserSafetyDashboardDialog(
      currentProfile = profile,
      privacySettings = userPrivacySettings,
      communicationControls = userCommunicationControls,
      blockedUsers = blockedUsers,
      mutedUsers = mutedUsers,
      reports = submittedReports,
      activeSessions = activeSessions,
      emergencyShield = emergencyShield,
      isTwoFactorEnabled = twoFactorEnabled,
      appeals = userAppeals,
      onUpdatePrivacySettings = { newSettings -> repository.updatePrivacySettings(newSettings) },
      onUpdateCommunicationControls = { newControls -> repository.updateCommunicationControls(newControls) },
      onUnblockUser = { userId -> repository.unblockUser(userId) },
      onBlockUser = { userId, name, reason -> repository.blockUser(userId, name, reason) },
      onUnmuteUser = { userId -> repository.unmuteUser(userId) },
      onMuteUser = { userId, name, duration -> repository.muteUser(userId, name, duration) },
      onSubmitReport = { cat, targetType, targetId, reason, note, isAnon ->
        repository.submitUserReport(cat, targetType, targetId, reason, note, isAnon)
      },
      onSubmitAppeal = { sanction, explanation -> repository.submitAppeal(sanction, explanation) },
      onToggleEmergencyShield = { enable -> repository.toggleEmergencyShield(enable) },
      onTerminateSession = { id -> repository.terminateSession(id) },
      onTerminateAllOtherSessions = { repository.terminateAllOtherSessions() },
      onToggleTwoFactor = { repository.toggleTwoFactor() },
      onExportData = { repository.exportUserDataJson() },
      onDismiss = { showSafetyDashboard = false }
    )
  }

  // Real-Time Notification Center Dialog
  if (showNotificationsDialog) {
    com.example.ui.components.NotificationsCenterDialog(
      notifications = notifications,
      preferences = notificationPreferences,
      onDismiss = { showNotificationsDialog = false },
      onMarkAsRead = { notifId -> repository.markNotificationAsRead(notifId) },
      onMarkAllAsRead = { repository.markAllNotificationsAsRead() },
      onDeleteNotification = { notifId -> repository.deleteNotification(notifId) },
      onClearAll = { repository.clearAllNotifications() },
      onUpdatePreferences = { prefs -> repository.updateNotificationPreferences(prefs) },
      onNavigateToTarget = { destination ->
        when (destination) {
          "chat", "groups", "discord" -> selectedTab = PruconTab.GROUPS
          "timeline" -> selectedTab = PruconTab.TIMELINE
          "profile" -> selectedTab = PruconTab.PROFILE
          "tournaments" -> selectedTab = PruconTab.TOURNAMENTS
          "leaderboard" -> selectedTab = PruconTab.LEADERBOARD
          else -> {}
        }
      }
    )
  }
}
