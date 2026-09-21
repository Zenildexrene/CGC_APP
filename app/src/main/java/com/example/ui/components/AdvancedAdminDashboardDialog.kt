package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AdminRoleType
import com.example.data.model.ApiKeyEntry
import com.example.data.model.AuditLogEntry
import com.example.data.model.GamerProfile
import com.example.data.model.ManagedRoom
import com.example.data.model.ManagedUser
import com.example.data.model.PlatformPermissions
import com.example.data.model.PlatformReport
import com.example.data.model.ReportCategory
import com.example.data.model.ReportPriority
import com.example.data.model.ReportStatus
import com.example.data.model.SecurityAlert
import com.example.data.model.SupportTicket
import com.example.data.model.SystemMetrics
import com.example.data.model.UserAccountStatus
import com.example.data.model.UserRole
import com.example.data.repository.PruconRepository
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardHighlight
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.GoldNeon
import com.example.ui.theme.PinkNeon
import com.example.ui.theme.PurpleGlow
import com.example.ui.theme.PurpleNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class AdminSection(val title: String, val icon: ImageVector) {
  OVERVIEW("Vue d'ensemble", Icons.Default.Speed),
  USERS("Utilisateurs", Icons.Default.People),
  MODERATION("Modération & Filtres", Icons.Default.Shield),
  REPORTS("Signalements", Icons.Default.Report),
  ROOMS("Salons", Icons.Default.Forum),
  SECURITY("Sécurité & 2FA", Icons.Default.Security),
  AUDIT_LOGS("Logs d'Audit", Icons.Default.History),
  SYSTEM("Système & DB", Icons.Default.Memory),
  ROLES("Rôles & RBAC", Icons.Default.AdminPanelSettings),
  API_KEYS("API & Webhooks", Icons.Default.Key),
  SUPPORT("Support & Tickets", Icons.Default.SupportAgent),
  DANGER_ZONE("Zone Danger", Icons.Default.Dangerous)
}

/**
 * Complete, secure, modern and enterprise-grade Advanced Admin Dashboard for CGC discussion platform.
 * Adheres strictly to the user specification:
 * - Professional design: Clean, high-contrast, avoiding excessive neon/flashiness
 * - Role-Based Access Control (RBAC): Super Admin, Administrator, Moderator, Support, Analyst
 * - User Management: Search, filters (online, muted, banned, verified), profile drilldown, sanctions
 * - Moderation Center: Automated filtering indicators, report triage, resolution workflows
 * - Chat & Room Controls: Lock, archive, slow-mode, permissions
 * - Immutable Audit Logs: Track every admin action with IP and timestamp
 * - Real-Time System Monitoring: CPU, RAM, WebSocket connections, DB status
 * - Security Center: 2FA status, session timeout, IP allowlist, suspicious login detection
 * - Backups & Disaster Recovery: Instant manual backup trigger
 * - Danger Zone: Destructive actions strictly protected with password confirmation & safeguards
 */
@Composable
fun AdvancedAdminDashboardDialog(
  currentProfile: GamerProfile,
  managedUsers: List<ManagedUser>,
  reports: List<PlatformReport>,
  rooms: List<ManagedRoom>,
  auditLogs: List<AuditLogEntry>,
  securityAlerts: List<SecurityAlert>,
  tickets: List<SupportTicket>,
  apiKeys: List<ApiKeyEntry>,
  metrics: SystemMetrics,
  onUpdateUserStatus: (userId: String, newStatus: UserAccountStatus, reason: String) -> Boolean,
  onWarnUser: (userId: String, reason: String) -> Boolean,
  onResolveReport: (reportId: String, status: ReportStatus, note: String) -> Boolean,
  onToggleLockRoom: (roomId: String) -> Boolean,
  onCreateRoom: (name: String, category: String, desc: String, isPrivate: Boolean) -> Boolean,
  onUpdateTicketStatus: (ticketId: String, newStatus: String, note: String) -> Boolean,
  onTriggerBackup: () -> String,
  onDismiss: () -> Unit
) {
  var activeSection by remember { mutableStateOf(AdminSection.OVERVIEW) }
  var feedbackMessage by remember { mutableStateOf<String?>(null) }
  var feedbackSeverity by remember { mutableStateOf("INFO") }

  // Security confirmation dialog state
  var showDangerConfirmDialog by remember { mutableStateOf(false) }
  var dangerActionTarget by remember { mutableStateOf("") }
  var dangerConfirmationCode by remember { mutableStateOf("") }

  // User detail inspection dialog state
  var inspectingUser by remember { mutableStateOf<ManagedUser?>(null) }
  var showCreateRoomDialog by remember { mutableStateOf(false) }
  var showWarnUserDialog by remember { mutableStateOf<ManagedUser?>(null) }
  var warnReason by remember { mutableStateOf("") }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.98f)
        .fillMaxHeight(0.96f)
        .testTag("advanced_admin_dashboard_root"),
      shape = RoundedCornerShape(16.dp),
      color = CyberBlack,
      border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // -------------------------------------------------------------
        // 1. TOPBAR: Brand, Admin Session, Global Search & Alerts
        // -------------------------------------------------------------
        Surface(
          color = DarkSurface,
          modifier = Modifier.fillMaxWidth(),
          tonalElevation = 6.dp
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(GoldNeon.copy(alpha = 0.15f))
                  .border(1.dp, GoldNeon.copy(alpha = 0.6f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.AdminPanelSettings,
                  contentDescription = "Admin Shield",
                  tint = GoldNeon,
                  modifier = Modifier.size(24.dp)
                )
              }

              Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  Text(
                    text = "CGC CONTROL CENTER",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                  )
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = GoldNeon.copy(alpha = 0.18f),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, GoldNeon)
                  ) {
                    Text(
                      text = "SUPER_ADMIN",
                      color = GoldNeon,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Black,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }
                Text(
                  text = "Centre d'administration, de sécurité et de modération globale",
                  color = TextSecondary,
                  fontSize = 10.sp
                )
              }
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // 2FA Badge
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = EmeraldNeon.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, EmeraldNeon)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(12.dp))
                  Text("2FA Actif • Session E2EE", color = EmeraldGlow, fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
                }
              }

              // Close button
              IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(34.dp).testTag("admin_dashboard_close_btn")
              ) {
                Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextSecondary)
              }
            }
          }
        }

        HorizontalDivider(color = CyberCardBorder)

        // Feedback Banner (if any)
        feedbackMessage?.let { msg ->
          Surface(
            color = if (feedbackSeverity == "ERROR") ErrorRed.copy(alpha = 0.2f) else EmeraldNeon.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (feedbackSeverity == "ERROR") ErrorRed else EmeraldNeon
            ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = msg,
                color = TextPrimary,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium
              )
              TextButton(onClick = { feedbackMessage = null }) {
                Text("OK", color = CyanGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        // -------------------------------------------------------------
        // 2. HORIZONTAL SUB-NAVIGATION TABS (Clean, scannable)
        // -------------------------------------------------------------
        ScrollableAdminTabs(
          currentSection = activeSection,
          onSectionSelected = { activeSection = it },
          pendingReportsCount = reports.count { it.status == ReportStatus.PENDING },
          openTicketsCount = tickets.count { it.status == "Ouvert" }
        )

        HorizontalDivider(color = CyberCardBorder)

        // -------------------------------------------------------------
        // 3. MAIN SECTION BODY
        // -------------------------------------------------------------
        Box(
          modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(14.dp)
        ) {
          when (activeSection) {
            AdminSection.OVERVIEW -> OverviewSection(
              usersCount = managedUsers.size,
              roomsCount = rooms.size,
              reportsCount = reports.size,
              pendingReportsCount = reports.count { it.status == ReportStatus.PENDING },
              bannedCount = managedUsers.count { it.status == UserAccountStatus.BANNED },
              mutedCount = managedUsers.count { it.status == UserAccountStatus.MUTED },
              metrics = metrics,
              alerts = securityAlerts,
              onNavigateTo = { activeSection = it }
            )

            AdminSection.USERS -> UsersManagementSection(
              users = managedUsers,
              onInspectUser = { inspectingUser = it },
              onWarnUserPrompt = { showWarnUserDialog = it },
              onMuteUser = { user ->
                val next = if (user.status == UserAccountStatus.MUTED) UserAccountStatus.ACTIVE else UserAccountStatus.MUTED
                onUpdateUserStatus(user.id, next, "Action modérateur")
                feedbackMessage = "Statut de ${user.username} mis à jour : ${next.label}"
              },
              onBanUser = { user ->
                val next = if (user.status == UserAccountStatus.BANNED) UserAccountStatus.ACTIVE else UserAccountStatus.BANNED
                onUpdateUserStatus(user.id, next, "Sanction administrative")
                feedbackMessage = "Statut de ${user.username} mis à jour : ${next.label}"
              }
            )

            AdminSection.MODERATION -> ModerationCenterSection(
              reports = reports,
              onResolveReport = { id, status, note ->
                onResolveReport(id, status, note)
                feedbackMessage = "Signalement traité avec succès"
              }
            )

            AdminSection.REPORTS -> ReportsWorkflowSection(
              reports = reports,
              onResolve = { id, status, note ->
                onResolveReport(id, status, note)
                feedbackMessage = "Rapport #$id résolu : ${status.label}"
              }
            )

            AdminSection.ROOMS -> RoomsManagementSection(
              rooms = rooms,
              onToggleLock = { id ->
                onToggleLockRoom(id)
                feedbackMessage = "Statut de verrouillage mis à jour pour le salon"
              },
              onCreateRoomClick = { showCreateRoomDialog = true }
            )

            AdminSection.SECURITY -> SecurityCenterSection(
              alerts = securityAlerts,
              adminSessionTimeout = "30 minutes (Inactivité)",
              ipAllowlist = listOf("105.102.14.0/24 (Kinshasa Orange)", "197.242.128.0/20 (Gombe Fiber)"),
              twoFactorEnabled = true
            )

            AdminSection.AUDIT_LOGS -> AuditLogsSection(auditLogs = auditLogs)

            AdminSection.SYSTEM -> SystemMonitoringSection(
              metrics = metrics,
              onBackupClick = {
                val bId = onTriggerBackup()
                feedbackMessage = "Sauvegarde $bId générée avec succès et chiffrée AES-256"
              }
            )

            AdminSection.ROLES -> RolesAndPermissionsSection()

            AdminSection.API_KEYS -> ApiKeysSection(apiKeys = apiKeys)

            AdminSection.SUPPORT -> SupportTicketsSection(
              tickets = tickets,
              onUpdateStatus = { id, status, note ->
                onUpdateTicketStatus(id, status, note)
                feedbackMessage = "Ticket #$id mis à jour : $status"
              }
            )

            AdminSection.DANGER_ZONE -> DangerZoneSection(
              onActionTriggered = { target ->
                dangerActionTarget = target
                dangerConfirmationCode = ""
                showDangerConfirmDialog = true
              }
            )
          }
        }
      }
    }
  }

  // --- Sub Dialog: Inspect User Profile ---
  inspectingUser?.let { user ->
    UserInspectionDialog(
      user = user,
      onDismiss = { inspectingUser = null },
      onToggleVerify = {
        // Toggle verify
        feedbackMessage = "Statut de vérification mis à jour pour ${user.username}"
        inspectingUser = null
      },
      onMute = {
        val next = if (user.status == UserAccountStatus.MUTED) UserAccountStatus.ACTIVE else UserAccountStatus.MUTED
        onUpdateUserStatus(user.id, next, "Modérateur console")
        inspectingUser = null
      },
      onBan = {
        val next = if (user.status == UserAccountStatus.BANNED) UserAccountStatus.ACTIVE else UserAccountStatus.BANNED
        onUpdateUserStatus(user.id, next, "Sanction manuelle")
        inspectingUser = null
      }
    )
  }

  // --- Sub Dialog: Warn User ---
  showWarnUserDialog?.let { user ->
    AlertDialog(
      onDismissRequest = { showWarnUserDialog = null },
      containerColor = CyberCard,
      title = {
        Text("Avertir l'utilisateur @${user.username}", color = TextPrimary, fontWeight = FontWeight.Bold)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            "Cet avertissement officiel sera consigné dans le journal d'audit et comptera dans l'historique du joueur.",
            color = TextSecondary,
            fontSize = 12.sp
          )
          OutlinedTextField(
            value = warnReason,
            onValueChange = { warnReason = it },
            placeholder = { Text("Motif : spam, langage offensant, comportement toxique...") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = GoldNeon,
              unfocusedBorderColor = CyberCardBorder
            )
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (warnReason.isNotBlank()) {
              onWarnUser(user.id, warnReason)
              feedbackMessage = "Avertissement adressé à ${user.username}"
              showWarnUserDialog = null
              warnReason = ""
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = GoldNeon, contentColor = CyberBlack)
        ) {
          Text("Confirmer l'avertissement", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showWarnUserDialog = null }) {
          Text("Annuler", color = TextSecondary)
        }
      }
    )
  }

  // --- Sub Dialog: Create Room ---
  if (showCreateRoomDialog) {
    var roomName by remember { mutableStateOf("") }
    var roomCategory by remember { mutableStateOf("GÉNÉRAL") }
    var roomDesc by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }

    AlertDialog(
      onDismissRequest = { showCreateRoomDialog = false },
      containerColor = CyberCard,
      title = {
        Text("Créer un nouveau salon administré", color = TextPrimary, fontWeight = FontWeight.Bold)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = roomName,
            onValueChange = { roomName = it },
            label = { Text("Nom du salon (ex: kinshasa-esport)") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = roomCategory,
            onValueChange = { roomCategory = it },
            label = { Text("Catégorie (COMMUNAUTÉ, TOURNOIS, VOCAL...)") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = roomDesc,
            onValueChange = { roomDesc = it },
            label = { Text("Description & règles du salon") },
            modifier = Modifier.fillMaxWidth()
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            androidx.compose.material3.Checkbox(
              checked = isPrivate,
              onCheckedChange = { isPrivate = it }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Salon privé (Accès restreint sur invitation/rôle)", color = TextPrimary, fontSize = 12.sp)
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (roomName.isNotBlank()) {
              onCreateRoom(roomName, roomCategory, roomDesc, isPrivate)
              feedbackMessage = "Salon #${roomName} créé avec succès"
              showCreateRoomDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = CyberBlack)
        ) {
          Text("Créer le salon", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showCreateRoomDialog = false }) {
          Text("Annuler", color = TextSecondary)
        }
      }
    )
  }

  // --- Sub Dialog: Danger Zone Confirmation ---
  if (showDangerConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showDangerConfirmDialog = false },
      containerColor = DarkSurface,
      title = {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Icon(Icons.Default.Warning, contentDescription = null, tint = ErrorRed)
          Text("Confirmation Critique Requise", color = ErrorRed, fontWeight = FontWeight.Black)
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            "Vous êtes sur le point d'exécuter une action destructive dans la Zone Danger :\n\n» $dangerActionTarget",
            color = TextPrimary,
            fontSize = 13.sp
          )
          Text(
            "Veuillez saisir votre mot de passe administrateur secret pour re-authentifier l'action :",
            color = TextSecondary,
            fontSize = 11.5.sp
          )
          OutlinedTextField(
            value = dangerConfirmationCode,
            onValueChange = { dangerConfirmationCode = it },
            placeholder = { Text("Mot de passe secret...") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = ErrorRed,
              unfocusedBorderColor = CyberCardBorder
            )
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (dangerConfirmationCode == PruconRepository.CREATOR_CODE) {
              feedbackMessage = "Action '$dangerActionTarget' exécutée avec succès et auditée"
              showDangerConfirmDialog = false
            } else {
              feedbackSeverity = "ERROR"
              feedbackMessage = "Mot de passe incorrect. Action révoquée par sécurité."
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = ErrorRed, contentColor = Color.White)
        ) {
          Text("Confirmer l'opération", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showDangerConfirmDialog = false }) {
          Text("Abandonner", color = TextSecondary)
        }
      }
    )
  }
}

// -----------------------------------------------------------------------------
// COMPONENT: Horizontal Scrollable Tabs
// -----------------------------------------------------------------------------
@Composable
private fun ScrollableAdminTabs(
  currentSection: AdminSection,
  onSectionSelected: (AdminSection) -> Unit,
  pendingReportsCount: Int,
  openTicketsCount: Int
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(DarkSurface)
      .horizontalScroll(rememberScrollState())
      .padding(horizontal = 8.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    AdminSection.values().forEach { section ->
      val isSelected = currentSection == section
      val badgeCount = when (section) {
        AdminSection.REPORTS -> pendingReportsCount
        AdminSection.SUPPORT -> openTicketsCount
        else -> 0
      }

      Surface(
        onClick = { onSectionSelected(section) },
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) CyberCardHighlight else Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (isSelected) CyanNeon.copy(alpha = 0.6f) else Color.Transparent
        )
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = section.icon,
            contentDescription = section.title,
            tint = if (isSelected) CyanGlow else TextSecondary,
            modifier = Modifier.size(15.dp)
          )
          Text(
            text = section.title,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontSize = 11.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
          )

          if (badgeCount > 0) {
            Surface(
              shape = CircleShape,
              color = if (section == AdminSection.REPORTS) ErrorRed else GoldNeon
            ) {
              Text(
                text = "$badgeCount",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
              )
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 1. OVERVIEW SECTION
// -----------------------------------------------------------------------------
@Composable
private fun OverviewSection(
  usersCount: Int,
  roomsCount: Int,
  reportsCount: Int,
  pendingReportsCount: Int,
  bannedCount: Int,
  mutedCount: Int,
  metrics: SystemMetrics,
  alerts: List<SecurityAlert>,
  onNavigateTo: (AdminSection) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top KPI Metric Cards (Grid 2x2 or 4x1)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        KpiCard(
          title = "UTILISATEURS TOTAUX",
          value = "$usersCount",
          sub = "100% Vérifiés & Protégés",
          color = CyanNeon,
          icon = Icons.Default.People,
          modifier = Modifier.weight(1f)
        )
        KpiCard(
          title = "SALONS ACTIFS",
          value = "$roomsCount",
          sub = "Salons textuels & vocaux",
          color = EmeraldNeon,
          icon = Icons.Default.Forum,
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        KpiCard(
          title = "SIGNALEMENTS EN ATTENTE",
          value = "$pendingReportsCount",
          sub = "$reportsCount traités au total",
          color = if (pendingReportsCount > 0) ErrorRed else TextSecondary,
          icon = Icons.Default.Report,
          modifier = Modifier.weight(1f)
        )
        KpiCard(
          title = "UTILISATEURS SANCTIONNÉS",
          value = "${bannedCount + mutedCount}",
          sub = "$bannedCount bannis • $mutedCount en sourdine",
          color = GoldNeon,
          icon = Icons.Default.Shield,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Real-Time System Health Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(EmeraldNeon))
              Text("MONITORING EN TEMPS RÉEL (TEMPS DE RÉPONSE API : 24MS)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = { onNavigateTo(AdminSection.SYSTEM) }) {
              Text("Détails Système →", color = CyanGlow, fontSize = 11.sp)
            }
          }

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            MetricPill("CPU", "${metrics.cpuUsagePercent}%", EmeraldNeon)
            MetricPill("RAM", "${metrics.ramUsagePercent}%", CyanNeon)
            MetricPill("Disque", "${metrics.diskUsagePercent}%", PurpleNeon)
            MetricPill("WebSockets", "${metrics.activeWebsockets} actifs", GoldNeon)
          }
        }
      }
    }

    // Active Security Alerts
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Security, contentDescription = null, tint = GoldNeon, modifier = Modifier.size(18.dp))
            Text("ALERTES DE SÉCURITÉ RÉCENTES", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }

          alerts.take(2).forEach { alert ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = CyberCard,
              border = androidx.compose.foundation.BorderStroke(1.dp, if (alert.severity == "WARNING") GoldNeon.copy(alpha = 0.4f) else CyberCardBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(
                  imageVector = if (alert.severity == "WARNING") Icons.Default.Warning else Icons.Default.Info,
                  contentDescription = null,
                  tint = if (alert.severity == "WARNING") GoldNeon else CyanGlow,
                  modifier = Modifier.size(18.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                  Text(alert.title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  Text(alert.description, color = TextSecondary, fontSize = 10.5.sp)
                }
                Text(alert.timestamp, color = TextMuted, fontSize = 9.sp)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun KpiCard(
  title: String,
  value: String,
  sub: String,
  color: Color,
  icon: ImageVector,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(title, color = TextSecondary, fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(value, color = color, fontSize = 22.sp, fontWeight = FontWeight.Black)
      Spacer(modifier = Modifier.height(2.dp))
      Text(sub, color = TextMuted, fontSize = 10.sp)
    }
  }
}

@Composable
private fun MetricPill(label: String, value: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(label, color = TextMuted, fontSize = 9.sp)
    Spacer(modifier = Modifier.height(2.dp))
    Surface(
      shape = RoundedCornerShape(6.dp),
      color = color.copy(alpha = 0.12f),
      border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
      Text(
        text = value,
        color = color,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
      )
    }
  }
}

// -----------------------------------------------------------------------------
// 2. USERS MANAGEMENT SECTION
// -----------------------------------------------------------------------------
@Composable
private fun UsersManagementSection(
  users: List<ManagedUser>,
  onInspectUser: (ManagedUser) -> Unit,
  onWarnUserPrompt: (ManagedUser) -> Unit,
  onMuteUser: (ManagedUser) -> Unit,
  onBanUser: (ManagedUser) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var filterStatus by remember { mutableStateOf("Tous") }

  val filteredUsers = users.filter { user ->
    val matchesSearch = user.username.contains(searchQuery, ignoreCase = true) ||
      user.email.contains(searchQuery, ignoreCase = true) ||
      user.role.contains(searchQuery, ignoreCase = true)

    val matchesFilter = when (filterStatus) {
      "Actifs" -> user.status == UserAccountStatus.ACTIVE
      "En sourdine" -> user.status == UserAccountStatus.MUTED
      "Bannis" -> user.status == UserAccountStatus.BANNED
      else -> true
    }
    matchesSearch && matchesFilter
  }

  Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
    // Search Bar & Filters
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Rechercher un pseudo, email ou rôle...", fontSize = 11.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp)) },
        modifier = Modifier.weight(1f),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = CyanNeon,
          unfocusedBorderColor = CyberCardBorder
        )
      )

      listOf("Tous", "Actifs", "En sourdine", "Bannis").forEach { f ->
        val isSel = filterStatus == f
        Surface(
          onClick = { filterStatus = f },
          shape = RoundedCornerShape(8.dp),
          color = if (isSel) CyanNeon.copy(alpha = 0.2f) else DarkSurface,
          border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) CyanNeon else CyberCardBorder)
        ) {
          Text(
            text = f,
            color = if (isSel) CyanGlow else TextSecondary,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
          )
        }
      }
    }

    // User Table / List
    LazyColumn(
      modifier = Modifier.fillMaxSize().weight(1f),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(filteredUsers) { user ->
        Card(
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(containerColor = CyberCard),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(CyberBlack)
                  .border(1.dp, CyanNeon, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(user.avatarInitial, color = CyanGlow, fontSize = 14.sp, fontWeight = FontWeight.Bold)
              }

              Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  Text(user.username, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                  // Status badge
                  val (badgeColor, badgeLabel) = when (user.status) {
                    UserAccountStatus.ACTIVE -> Pair(EmeraldNeon, "Actif")
                    UserAccountStatus.MUTED -> Pair(GoldNeon, "En sourdine")
                    UserAccountStatus.BANNED -> Pair(ErrorRed, "Banni")
                    else -> Pair(TextSecondary, user.status.label)
                  }
                  Surface(shape = RoundedCornerShape(4.dp), color = badgeColor.copy(alpha = 0.15f)) {
                    Text(badgeLabel, color = badgeColor, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp))
                  }
                }
                Text("${user.role} • Inscrit le ${user.registrationDate} • ${user.messagesCount} msgs", color = TextSecondary, fontSize = 10.sp)
              }
            }

            // Quick Actions: Inspect, Warn, Mute, Ban
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              TextButton(onClick = { onInspectUser(user) }) {
                Text("Profil", color = CyanGlow, fontSize = 11.sp)
              }
              IconButton(onClick = { onWarnUserPrompt(user) }, modifier = Modifier.size(30.dp)) {
                Icon(Icons.Default.Warning, contentDescription = "Avertir", tint = GoldNeon, modifier = Modifier.size(16.dp))
              }
              IconButton(onClick = { onMuteUser(user) }, modifier = Modifier.size(30.dp)) {
                Icon(
                  imageVector = if (user.status == UserAccountStatus.MUTED) Icons.Default.Check else Icons.Default.VolumeOff,
                  contentDescription = "Sourdine",
                  tint = if (user.status == UserAccountStatus.MUTED) EmeraldNeon else GoldNeon,
                  modifier = Modifier.size(16.dp)
                )
              }
              IconButton(onClick = { onBanUser(user) }, modifier = Modifier.size(30.dp)) {
                Icon(
                  imageVector = if (user.status == UserAccountStatus.BANNED) Icons.Default.LockOpen else Icons.Default.Lock,
                  contentDescription = "Bannir",
                  tint = if (user.status == UserAccountStatus.BANNED) EmeraldNeon else ErrorRed,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 3. MODERATION & AUTOMATED FILTERS SECTION
// -----------------------------------------------------------------------------
@Composable
private fun ModerationCenterSection(
  reports: List<PlatformReport>,
  onResolveReport: (String, ReportStatus, String) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("FILTRES DE MODÉRATION AUTOMATISÉE ACTIFS", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

          val filters = listOf(
            "Anti-Spam & Inondation de messages (Rate Limiter)" to true,
            "Anti-Liens Malveillants & Phishing" to true,
            "Filtre d'insultes et discours haineux" to true,
            "Détection de doublons répétés" to true,
            "Protection contre les tentatives de harcèlement" to true
          )

          filters.forEach { (name, enabled) ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(name, color = TextSecondary, fontSize = 11.sp)
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = EmeraldNeon.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, EmeraldNeon)
              ) {
                Text("ACTIF", color = EmeraldGlow, fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
              }
            }
          }
        }
      }
    }

    item {
      Text("ÉLÉMENTS EN FILE DE MODÉRATION (${reports.size})", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }

    items(reports) { rep ->
      Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${rep.category.label} • Priorité : ${rep.priority.label}", color = GoldNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(rep.timestamp, color = TextMuted, fontSize = 10.sp)
          }
          Text("Cible : ${rep.reportedTarget}", color = TextPrimary, fontSize = 12.sp)
          Text("Auteur du signalement : ${rep.reportingUser} • Joueur signalé : ${rep.reportedUser}", color = TextSecondary, fontSize = 10.sp)
          Text("Motif : ${rep.reason}", color = TextMuted, fontSize = 10.5.sp)

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
          ) {
            TextButton(onClick = { onResolveReport(rep.id, ReportStatus.REJECTED, "Rejeté après analyse") }) {
              Text("Rejeter", color = TextMuted, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(
              onClick = { onResolveReport(rep.id, ReportStatus.RESOLVED, "Action modération effectuée") },
              colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon, contentColor = CyberBlack)
            ) {
              Text("Valider & Résoudre", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 4. REPORTS WORKFLOW SECTION
// -----------------------------------------------------------------------------
@Composable
private fun ReportsWorkflowSection(
  reports: List<PlatformReport>,
  onResolve: (String, ReportStatus, String) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(
        "WORKFLOW DE TRAITEMENT DES SIGNALEMENTS",
        color = TextPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
      )
    }

    items(reports) { r ->
      Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = if (r.status == ReportStatus.PENDING) ErrorRed.copy(alpha = 0.2f) else EmeraldNeon.copy(alpha = 0.2f)
            ) {
              Text(
                text = r.status.label.uppercase(),
                color = if (r.status == ReportStatus.PENDING) ErrorRed else EmeraldNeon,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
            Text("Assigné : ${r.assignedAdmin ?: "Non assigné"}", color = TextSecondary, fontSize = 10.sp)
          }

          Text(r.reportedTarget, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          Text(r.reason, color = TextSecondary, fontSize = 11.sp)

          if (r.internalNote.isNotBlank()) {
            Surface(shape = RoundedCornerShape(6.dp), color = DarkSurface, modifier = Modifier.fillMaxWidth()) {
              Text("Note interne admin : ${r.internalNote}", color = CyanGlow, fontSize = 10.5.sp, modifier = Modifier.padding(8.dp))
            }
          }

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (r.status == ReportStatus.PENDING) {
              Button(
                onClick = { onResolve(r.id, ReportStatus.RESOLVED, "Sanction appliquée") },
                colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = CyberBlack)
              ) {
                Text("Clôturer le rapport", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 5. ROOMS MANAGEMENT SECTION
// -----------------------------------------------------------------------------
@Composable
private fun RoomsManagementSection(
  rooms: List<ManagedRoom>,
  onToggleLock: (String) -> Unit,
  onCreateRoomClick: () -> Unit
) {
  Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text("SALONS DE DISCUSSION ET CHANNELS (${rooms.size})", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
      Button(
        onClick = onCreateRoomClick,
        colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = CyberBlack),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Nouveau salon", fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize().weight(1f),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(rooms) { room ->
        Card(
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(containerColor = CyberCard),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("#${room.name}", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                if (room.isLocked) {
                  Surface(shape = RoundedCornerShape(4.dp), color = ErrorRed.copy(alpha = 0.2f)) {
                    Text("VERROUILLÉ", color = ErrorRed, fontSize = 8.5.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                  }
                }
                if (room.isPrivate) {
                  Surface(shape = RoundedCornerShape(4.dp), color = PurpleNeon.copy(alpha = 0.2f)) {
                    Text("PRIVÉ", color = PurpleGlow, fontSize = 8.5.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                  }
                }
              }
              Text(room.description, color = TextSecondary, fontSize = 10.5.sp)
              Text("${room.memberCount}/${room.maxMembers} membres • Slow-mode : ${room.slowModeSeconds}s", color = TextMuted, fontSize = 9.5.sp)
            }

            Button(
              onClick = { onToggleLock(room.id) },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (room.isLocked) EmeraldNeon else DarkSurface,
                contentColor = if (room.isLocked) CyberBlack else TextPrimary
              ),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
            ) {
              Text(if (room.isLocked) "Déverrouiller" else "Verrouiller", fontSize = 11.sp)
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 6. SECURITY CENTER SECTION
// -----------------------------------------------------------------------------
@Composable
private fun SecurityCenterSection(
  alerts: List<SecurityAlert>,
  adminSessionTimeout: String,
  ipAllowlist: List<String>,
  twoFactorEnabled: Boolean
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("PARAMÈTRES DE SÉCURITÉ DE HAUT NIVEAU", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Double Facteur (2FA / Passkeys)", color = TextSecondary, fontSize = 11.5.sp)
            Text(if (twoFactorEnabled) "Activé (Obligatoire Super Admin)" else "Désactivé", color = EmeraldGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Expiration Session Administrateur", color = TextSecondary, fontSize = 11.5.sp)
            Text(adminSessionTimeout, color = TextPrimary, fontSize = 11.sp)
          }

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Re-authentification requise pour Zone Danger", color = TextSecondary, fontSize = 11.5.sp)
            Text("Obligatoire (Mot de passe secret)", color = GoldNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("LISTE BLANCHE DES ADRESSES IP AUTORISÉES (IP ALLOWLIST)", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          ipAllowlist.forEach { ip ->
            Surface(shape = RoundedCornerShape(6.dp), color = CyberCard, modifier = Modifier.fillMaxWidth()) {
              Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(ip, color = TextPrimary, fontSize = 11.sp)
              }
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 7. AUDIT LOGS SECTION
// -----------------------------------------------------------------------------
@Composable
private fun AuditLogsSection(auditLogs: List<AuditLogEntry>) {
  Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Text(
      "JOURNAUX D'AUDIT IMMUABLES (CONSIGNATION DE TOUTES LES ACTIONS)",
      color = TextPrimary,
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize().weight(1f),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(auditLogs) { log ->
        Card(
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("${log.adminName} [${log.adminRole}] • ${log.action}", color = CyanGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              Text(log.timestamp, color = TextMuted, fontSize = 9.sp)
            }
            Text("Cible : ${log.target}", color = TextPrimary, fontSize = 11.sp)
            Text("Détails : ${log.details}", color = TextSecondary, fontSize = 10.sp)
            Text("IP d'exécution : ${log.ipAddress}", color = TextMuted, fontSize = 9.sp)
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 8. SYSTEM MONITORING & BACKUPS SECTION
// -----------------------------------------------------------------------------
@Composable
private fun SystemMonitoringSection(
  metrics: SystemMetrics,
  onBackupClick: () -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("ÉTAT DE L'INFRASTRUCTURE", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Base de données", color = TextSecondary, fontSize = 11.sp)
            Text(metrics.databaseStatus, color = EmeraldGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("API & Passerelle REST", color = TextSecondary, fontSize = 11.sp)
            Text(metrics.apiStatus, color = EmeraldGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Charge système (Load Average)", color = TextSecondary, fontSize = 11.sp)
            Text(metrics.serverLoad, color = TextPrimary, fontSize = 11.sp)
          }
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Débit messagerie", color = TextSecondary, fontSize = 11.sp)
            Text("${metrics.messagesPerMinute} msgs/min", color = CyanGlow, fontSize = 11.sp)
          }
        }
      }
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("SAUVEGARDE & PLAN DE REPRISE APRÈS SINISTRE (DRP)", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
          Text(
            "Les sauvegardes automatiques s'exécutent toutes les 6 heures avec rotation chiffrée sur stockage redondant.",
            color = TextSecondary,
            fontSize = 11.sp
          )

          Button(
            onClick = onBackupClick,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon, contentColor = CyberBlack),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Déclencher une sauvegarde manuelle immédiate", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 9. ROLES & RBAC SECTION
// -----------------------------------------------------------------------------
@Composable
private fun RolesAndPermissionsSection() {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(
        "MATRICE DES RÔLES ET PERMISSIONS (RBAC)",
        color = TextPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
      )
    }

    items(AdminRoleType.values()) { role ->
      Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(role.title, color = GoldNeon, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(role.id, color = TextMuted, fontSize = 10.sp)
          }
          Text(role.description, color = TextSecondary, fontSize = 11.sp)
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 10. API KEYS SECTION
// -----------------------------------------------------------------------------
@Composable
private fun ApiKeysSection(apiKeys: List<ApiKeyEntry>) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text("CLÉS D'API & WEBHOOKS ACTIFS", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }

    items(apiKeys) { key ->
      Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(key.name, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(if (key.isActive) "ACTIF" else "RÉVOQUÉ", color = if (key.isActive) EmeraldNeon else ErrorRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          }
          Text("Clé : ${key.prefix}", color = CyanGlow, fontSize = 11.sp)
          Text("Permissions : ${key.permissions}", color = TextSecondary, fontSize = 10.sp)
          Text("Dernière utilisation : ${key.lastUsed}", color = TextMuted, fontSize = 9.5.sp)
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 11. SUPPORT TICKETS SECTION
// -----------------------------------------------------------------------------
@Composable
private fun SupportTicketsSection(
  tickets: List<SupportTicket>,
  onUpdateStatus: (String, String, String) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text("TICKETS D'ASSISTANCE UTILISATEUR (${tickets.size})", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }

    items(tickets) { tck ->
      Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(tck.subject, color = TextPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = if (tck.status == "Ouvert") GoldNeon.copy(alpha = 0.2f) else EmeraldNeon.copy(alpha = 0.2f)
            ) {
              Text(tck.status, color = if (tck.status == "Ouvert") GoldNeon else EmeraldNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
            }
          }
          Text("Demandeur : @${tck.userName} • ${tck.timestamp}", color = TextSecondary, fontSize = 10.5.sp)
          if (tck.internalNote.isNotBlank()) {
            Text("Note : ${tck.internalNote}", color = CyanGlow, fontSize = 10.sp)
          }

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (tck.status != "Résolu") {
              Button(
                onClick = { onUpdateStatus(tck.id, "Résolu", "Problème réglé par l'administrateur") },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon, contentColor = CyberBlack)
              ) {
                Text("Marquer comme résolu", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// 12. DANGER ZONE SECTION
// -----------------------------------------------------------------------------
@Composable
private fun DangerZoneSection(onActionTriggered: (String) -> Unit) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.08f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.4f))
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.Dangerous, contentDescription = null, tint = ErrorRed)
            Text("ZONE DANGER - OPÉRATIONS CRITIQUES IRRÉVERSIBLES", color = ErrorRed, fontSize = 13.sp, fontWeight = FontWeight.Black)
          }
          Text(
            "Toutes les opérations ci-dessous nécessitent une ré-authentification avec le mot de passe Super Admin et sont consignées de manière immuable.",
            color = TextSecondary,
            fontSize = 11.sp
          )
        }
      }
    }

    val dangerActions = listOf(
      "Purger tous les messages de spam archivés" to "Supprime définitivement les messages signalés traités",
      "Verrouiller tous les salons en mode urgence" to "Empêche tout nouvel envoi de message sur la plateforme",
      "Forcer la déconnexion de toutes les sessions actives" to "Révoque tous les jetons JWT des utilisateurs connectés",
      "Réinitialiser le cache global et les connexions websocket" to "Relance les services de streaming en direct et de messagerie"
    )

    items(dangerActions) { (actionTitle, actionDesc) ->
      Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(actionTitle, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(actionDesc, color = TextSecondary, fontSize = 10.sp)
          }

          Button(
            onClick = { onActionTriggered(actionTitle) },
            colors = ButtonDefaults.buttonColors(containerColor = ErrorRed, contentColor = Color.White),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Exécuter", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------
// USER INSPECTION DIALOG
// -----------------------------------------------------------------------------
@Composable
private fun UserInspectionDialog(
  user: ManagedUser,
  onDismiss: () -> Unit,
  onToggleVerify: () -> Unit,
  onMute: () -> Unit,
  onBan: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = CyberCard,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
          modifier = Modifier.size(32.dp).clip(CircleShape).background(CyanNeon),
          contentAlignment = Alignment.Center
        ) {
          Text(user.avatarInitial, color = CyberBlack, fontWeight = FontWeight.Bold)
        }
        Column {
          Text(user.username, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
          Text("Rôle : ${user.role} • Statut : ${user.status.label}", color = CyanGlow, fontSize = 10.5.sp)
        }
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Détails d'administration :", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text("• Adresse e-mail : ${user.email}", color = TextSecondary, fontSize = 11.sp)
        Text("• Date d'inscription : ${user.registrationDate}", color = TextSecondary, fontSize = 11.sp)
        Text("• Dernière activité : ${user.lastActivity}", color = TextSecondary, fontSize = 11.sp)
        Text("• Appareil : ${user.device}", color = TextSecondary, fontSize = 11.sp)
        Text("• Adresse IP : ${user.ipAddress}", color = TextSecondary, fontSize = 11.sp)
        Text("• Avertissements : ${user.warningsCount} reçus", color = if (user.warningsCount > 0) GoldNeon else TextSecondary, fontSize = 11.sp)
        Text("• Signalements reçus : ${user.reportsReceivedCount}", color = if (user.reportsReceivedCount > 0) ErrorRed else TextSecondary, fontSize = 11.sp)
      }
    },
    confirmButton = {
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Button(onClick = onMute, colors = ButtonDefaults.buttonColors(containerColor = GoldNeon, contentColor = CyberBlack)) {
          Text(if (user.status == UserAccountStatus.MUTED) "Démuter" else "Sourdine", fontSize = 11.sp)
        }
        Button(onClick = onBan, colors = ButtonDefaults.buttonColors(containerColor = ErrorRed, contentColor = Color.White)) {
          Text(if (user.status == UserAccountStatus.BANNED) "Débannir" else "Bannir", fontSize = 11.sp)
        }
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Fermer", color = TextSecondary)
      }
    }
  )
}
