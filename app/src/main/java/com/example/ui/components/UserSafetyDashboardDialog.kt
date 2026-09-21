package com.example.ui.components

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BlockedUserItem
import com.example.data.model.CommunicationAudience
import com.example.data.model.EmergencySafetyShieldState
import com.example.data.model.GamerProfile
import com.example.data.model.LastSeenVisibility
import com.example.data.model.MuteDuration
import com.example.data.model.MutedUserItem
import com.example.data.model.OnlineStatusVisibility
import com.example.data.model.PrivacyVisibilityOption
import com.example.data.model.SafetyReportCategory
import com.example.data.model.UserActiveSession
import com.example.data.model.UserAppeal
import com.example.data.model.UserCommunicationControls
import com.example.data.model.UserPrivacyCenterSettings
import com.example.data.model.UserSubmittedReport
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
import com.example.ui.theme.PinkNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class SafetyDashboardTab(val label: String, val icon: ImageVector) {
  OVERVIEW("Aperçu & Confort", Icons.Default.Shield),
  PRIVACY("Confidentialité", Icons.Default.VisibilityOff),
  COMMUNICATION("Communications", Icons.Default.Lock),
  BLOCKS("Blocages & Sourdines", Icons.Default.Block),
  REPORTS("Signalements", Icons.Default.Report),
  SESSIONS("Sécurité & Sessions", Icons.Default.Fingerprint),
  DATA("Mes Données", Icons.Default.Download)
}

@Composable
fun UserSafetyDashboardDialog(
  currentProfile: GamerProfile,
  privacySettings: UserPrivacyCenterSettings,
  communicationControls: UserCommunicationControls,
  blockedUsers: List<BlockedUserItem>,
  mutedUsers: List<MutedUserItem>,
  reports: List<UserSubmittedReport>,
  activeSessions: List<UserActiveSession>,
  emergencyShield: EmergencySafetyShieldState,
  isTwoFactorEnabled: Boolean,
  appeals: List<UserAppeal>,
  onUpdatePrivacySettings: (UserPrivacyCenterSettings) -> Unit,
  onUpdateCommunicationControls: (UserCommunicationControls) -> Unit,
  onUnblockUser: (String) -> Unit,
  onBlockUser: (String, String, String) -> Unit,
  onUnmuteUser: (String) -> Unit,
  onMuteUser: (String, String, MuteDuration) -> Unit,
  onSubmitReport: (SafetyReportCategory, String, String, String, String, Boolean) -> Unit,
  onSubmitAppeal: (String, String) -> Unit,
  onToggleEmergencyShield: (Boolean) -> Unit,
  onTerminateSession: (String) -> Unit,
  onTerminateAllOtherSessions: () -> Unit,
  onToggleTwoFactor: () -> Unit,
  onExportData: () -> String,
  onDismiss: () -> Unit
) {
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  val context = LocalContext.current
  val tabs = SafetyDashboardTab.entries

  // Calculate informational Comfort & Security Score (Informational only, never penalizing)
  val securityScore = remember(privacySettings, communicationControls, isTwoFactorEnabled, emergencyShield) {
    var score = 70
    if (isTwoFactorEnabled) score += 10
    if (privacySettings.onlineStatus != OnlineStatusVisibility.EVERYONE) score += 5
    if (communicationControls.whoCanCallMe != CommunicationAudience.EVERYONE) score += 5
    if (privacySettings.hideEmail) score += 5
    if (emergencyShield.isActive) score += 5
    minOf(100, score)
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .padding(top = 24.dp),
      color = CyberBlack,
      shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // -------------------------------------------------------------
        // Header
        // -------------------------------------------------------------
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(EmeraldGlow.copy(alpha = 0.2f))
                .border(1.dp, EmeraldNeon, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = EmeraldNeon,
                modifier = Modifier.size(20.dp)
              )
            }
            Column {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                  text = "Espace Liberté, Confort & Sécurité",
                  color = TextPrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                )
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = EmeraldNeon.copy(alpha = 0.15f)
                ) {
                  Text(
                    text = "Score : $securityScore%",
                    color = EmeraldNeon,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
              Text(
                text = "Maître de vos données, protégé sans surveillance inutile",
                color = TextSecondary,
                fontSize = 11.sp
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(32.dp)
              .testTag("close_safety_dashboard_button")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextSecondary)
          }
        }

        HorizontalDivider(color = CyberCardBorder)

        // -------------------------------------------------------------
        // Tabs
        // -------------------------------------------------------------
        ScrollableTabRow(
          selectedTabIndex = selectedTabIndex,
          containerColor = DarkSurface,
          contentColor = EmeraldNeon,
          edgePadding = 12.dp,
          divider = {}
        ) {
          tabs.forEachIndexed { index, tab ->
            Tab(
              selected = selectedTabIndex == index,
              onClick = { selectedTabIndex = index },
              text = {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(tab.icon, contentDescription = null, modifier = Modifier.size(16.dp))
                  Text(tab.label, fontSize = 12.sp, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal)
                }
              },
              selectedContentColor = EmeraldNeon,
              unselectedContentColor = TextMuted
            )
          }
        }

        HorizontalDivider(color = CyberCardBorder)

        // -------------------------------------------------------------
        // Tab Contents
        // -------------------------------------------------------------
        Box(
          modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(14.dp)
        ) {
          when (tabs[selectedTabIndex]) {
            SafetyDashboardTab.OVERVIEW -> OverviewTabContent(
              securityScore = securityScore,
              emergencyShield = emergencyShield,
              onToggleEmergencyShield = onToggleEmergencyShield,
              onNavigateToTab = { selectedTabIndex = it },
              currentProfile = currentProfile,
              onTerminateAllOtherSessions = onTerminateAllOtherSessions
            )

            SafetyDashboardTab.PRIVACY -> PrivacyCenterTabContent(
              settings = privacySettings,
              onUpdate = onUpdatePrivacySettings
            )

            SafetyDashboardTab.COMMUNICATION -> CommunicationControlsTabContent(
              controls = communicationControls,
              onUpdate = onUpdateCommunicationControls
            )

            SafetyDashboardTab.BLOCKS -> BlockingAndMutingTabContent(
              blockedUsers = blockedUsers,
              mutedUsers = mutedUsers,
              onUnblockUser = onUnblockUser,
              onBlockUser = onBlockUser,
              onUnmuteUser = onUnmuteUser,
              onMuteUser = onMuteUser
            )

            SafetyDashboardTab.REPORTS -> ReportsAndAppealsTabContent(
              reports = reports,
              appeals = appeals,
              onSubmitReport = onSubmitReport,
              onSubmitAppeal = onSubmitAppeal
            )

            SafetyDashboardTab.SESSIONS -> SessionsAndSecurityTabContent(
              isTwoFactorEnabled = isTwoFactorEnabled,
              activeSessions = activeSessions,
              onToggleTwoFactor = onToggleTwoFactor,
              onTerminateSession = onTerminateSession,
              onTerminateAllOtherSessions = onTerminateAllOtherSessions
            )

            SafetyDashboardTab.DATA -> UserDataAndTransparencyTabContent(
              currentProfile = currentProfile,
              onExportData = onExportData
            )
          }
        }
      }
    }
  }
}

// =============================================================================
// 1. OVERVIEW TAB: Score, Recommendations & Emergency Shield
// =============================================================================
@Composable
private fun OverviewTabContent(
  securityScore: Int,
  emergencyShield: EmergencySafetyShieldState,
  onToggleEmergencyShield: (Boolean) -> Unit,
  onNavigateToTab: (Int) -> Unit,
  currentProfile: GamerProfile,
  onTerminateAllOtherSessions: () -> Unit
) {
  val context = LocalContext.current
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Emergency Shield Card (Bouclier de Sécurité Express)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("emergency_shield_card"),
        colors = CardDefaults.cardColors(
          containerColor = if (emergencyShield.isActive) PinkNeon.copy(alpha = 0.12f) else CyberCard
        ),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (emergencyShield.isActive) PinkNeon else EmeraldNeon.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(
                imageVector = Icons.Default.HealthAndSafety,
                contentDescription = null,
                tint = if (emergencyShield.isActive) PinkNeon else EmeraldNeon,
                modifier = Modifier.size(24.dp)
              )
              Column {
                Text(
                  text = "Bouclier d'Urgence (Sérénité Immédiate)",
                  color = TextPrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
                Text(
                  text = if (emergencyShield.isActive) "ACTIF : Interactions inconnues bloquées" else "Prêt à être activé en 1 clic en cas de pression",
                  color = if (emergencyShield.isActive) PinkNeon else TextSecondary,
                  fontSize = 11.sp
                )
              }
            }

            Switch(
              checked = emergencyShield.isActive,
              onCheckedChange = { active ->
                onToggleEmergencyShield(active)
                Toast.makeText(
                  context,
                  if (active) "🛡️ Bouclier d'urgence activé : Vous êtes temporairement isolé des inconnus."
                  else "Bouclier d'urgence désactivé. Vos paramètres habituels sont restaurés.",
                  Toast.LENGTH_SHORT
                ).show()
              },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PinkNeon,
                uncheckedTrackColor = DarkSurface
              )
            )
          }

          Text(
            text = "En cas de harcèlement, de spam ou si vous désirez une pause totale, le bouclier d'urgence bloque instantanément toutes les invitations, appels et messages des personnes extérieures à vos amis, tout en masquant temporairement votre profil.",
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )

          if (emergencyShield.isActive) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface, RoundedCornerShape(8.dp))
                .padding(10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text("Appareils secondaires connectés", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("Déconnectez immédiatement toute autre session suspecte", color = TextMuted, fontSize = 10.sp)
              }
              Button(
                onClick = {
                  onTerminateAllOtherSessions()
                  Toast.makeText(context, "Tous les autres appareils ont été déconnectés.", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = PinkNeon),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text("Déconnecter les autres", fontSize = 10.sp, color = Color.White)
              }
            }
          }
        }
      }
    }

    // Score & Philosophy Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Indice de Sérénité & Liberté", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Text("Indicateur informatif pour votre confort personnel (non punitif)", color = TextMuted, fontSize = 11.sp)
            }
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = EmeraldNeon.copy(alpha = 0.2f),
              border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon)
            ) {
              Text(
                text = "$securityScore / 100",
                color = EmeraldNeon,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }

          HorizontalDivider(color = CyberCardBorder)

          // 8 User Principles Checklist
          Text("Nos 8 Engagements Fondamentaux :", color = CyanGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          val principles = listOf(
            "Liberté d'expression & respect mutuel sans censure arbitraire",
            "Confidentialité par défaut (e-mail, téléphone & GPS masqués)",
            "Contrôle absolu : vous choisissez qui peut vous contacter ou vous mentionner",
            "Transparence complète : aucun algorithme caché ou revente de données",
            "Modération juste, respectueuse et motivée avec droit de recours",
            "Sécurité par conception (Chiffrement TLS 1.3 & E2EE)",
            "Zéro surveillance superflue : nous ne lisons pas vos messages privés",
            "Simplicité d'action : bloquer en 2 clics, signaler en 3 clics"
          )

          principles.forEach { principle ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(14.dp))
              Text(text = principle, color = TextSecondary, fontSize = 11.sp)
            }
          }
        }
      }
    }

    // Quick Recommendations
    item {
      Text("Recommandations de Confort :", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }

    item {
      RecommendationActionCard(
        title = "Restreindre qui peut vous appeler",
        subtitle = "Par défaut seuls vos amis confirmés peuvent initier un appel vocal",
        actionText = "Ajuster ➔",
        onClick = { onNavigateToTab(2) } // Navigate to Communication
      )
    }

    item {
      RecommendationActionCard(
        title = "Gérer les accusés de lecture & présence",
        subtitle = "Activez le mode fantôme pour naviguer sans que personne ne voie votre statut",
        actionText = "Confidentialité ➔",
        onClick = { onNavigateToTab(1) } // Navigate to Privacy
      )
    }

    item {
      RecommendationActionCard(
        title = "Télécharger l'archive de vos données",
        subtitle = "Exportez en 1 clic l'intégralité de vos informations (droit d'accès RGPD)",
        actionText = "Mes Données ➔",
        onClick = { onNavigateToTab(6) } // Navigate to Data
      )
    }
  }
}

// =============================================================================
// 2. PRIVACY CENTER TAB: Profile, Online Status, Last Seen, Read Receipts
// =============================================================================
@Composable
private fun PrivacyCenterTabContent(
  settings: UserPrivacyCenterSettings,
  onUpdate: (UserPrivacyCenterSettings) -> Unit
) {
  val context = LocalContext.current
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(
        text = "Centre de Confidentialité",
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
      )
      Text(
        text = "Configurez avec précision votre visibilité et vos traces de navigation.",
        color = TextMuted,
        fontSize = 11.sp
      )
    }

    // Profile Visibility Selector
    item {
      SettingsSelectorCard(
        title = "Visibilité du Profil",
        subtitle = "Qui peut consulter l'ensemble de votre fiche de joueur",
        currentValue = settings.profileVisibility.label,
        options = PrivacyVisibilityOption.entries.map { it.label },
        onSelect = { label ->
          val opt = PrivacyVisibilityOption.entries.first { it.label == label }
          onUpdate(settings.copy(profileVisibility = opt))
        }
      )
    }

    // Online Status Selector
    item {
      SettingsSelectorCard(
        title = "Statut en Ligne",
        subtitle = "Contrôlez qui peut voir quand vous êtes connecté à la CGC",
        currentValue = settings.onlineStatus.label,
        options = OnlineStatusVisibility.entries.map { it.label },
        onSelect = { label ->
          val opt = OnlineStatusVisibility.entries.first { it.label == label }
          onUpdate(settings.copy(onlineStatus = opt))
        }
      )
    }

    // Last Seen Selector
    item {
      SettingsSelectorCard(
        title = "Dernière Connexion (Vu pour la dernière fois)",
        subtitle = "Affichage de l'horodatage de votre dernière activité",
        currentValue = settings.lastSeen.label,
        options = LastSeenVisibility.entries.map { it.label },
        onSelect = { label ->
          val opt = LastSeenVisibility.entries.first { it.label == label }
          onUpdate(settings.copy(lastSeen = opt))
        }
      )
    }

    // Toggles Group: Read Receipts, Typing Indicator, Sensitive Content
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text("Indicateurs & Confort Visuel", color = CyanGlow, fontSize = 12.sp, fontWeight = FontWeight.Bold)

          // Read Receipts
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Accusés de lecture (Vu)", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Text("Si désactivé, vos interlocuteurs ne savent pas si vous avez lu leur message", color = TextMuted, fontSize = 10.sp)
            }
            Switch(
              checked = settings.readReceiptsEnabled,
              onCheckedChange = { onUpdate(settings.copy(readReceiptsEnabled = it)) }
            )
          }

          HorizontalDivider(color = CyberCardBorder)

          // Typing Indicator
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Indicateur de frappe (En train d'écrire...)", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Text("Afficher lorsque vous êtes en train de composer un message", color = TextMuted, fontSize = 10.sp)
            }
            Switch(
              checked = settings.typingIndicatorEnabled,
              onCheckedChange = { onUpdate(settings.copy(typingIndicatorEnabled = it)) }
            )
          }

          HorizontalDivider(color = CyberCardBorder)

          // Filter Sensitive Media
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Flouter le contenu sensible", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Text("Applique un flou de protection sur les images potentiellement choquantes", color = TextMuted, fontSize = 10.sp)
            }
            Switch(
              checked = settings.blurSensitiveContent,
              onCheckedChange = { onUpdate(settings.copy(blurSensitiveContent = it)) }
            )
          }

          HorizontalDivider(color = CyberCardBorder)

          // Location Privacy
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Masquer la localisation exacte", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Text("Seule la ville générale (Kinshasa) est visible, sans coordonnées GPS précises", color = TextMuted, fontSize = 10.sp)
            }
            Switch(
              checked = settings.hideExactLocation,
              onCheckedChange = { onUpdate(settings.copy(hideExactLocation = it)) }
            )
          }
        }
      }
    }
  }
}

// =============================================================================
// 3. COMMUNICATION CONTROLS TAB: Who can message, add, call, mention, reply
// =============================================================================
@Composable
private fun CommunicationControlsTabContent(
  controls: UserCommunicationControls,
  onUpdate: (UserCommunicationControls) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(
        text = "Contrôle des Communications",
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
      )
      Text(
        text = "Vous définissez souverainement qui a le droit d'entrer en contact avec vous.",
        color = TextMuted,
        fontSize = 11.sp
      )
    }

    // Who can message me
    item {
      SettingsSelectorCard(
        title = "Qui peut m'envoyer un message direct ?",
        subtitle = "Filtre les demandes de conversation privée",
        currentValue = controls.whoCanMessageMe.label,
        options = CommunicationAudience.entries.map { it.label },
        onSelect = { label ->
          val aud = CommunicationAudience.entries.first { it.label == label }
          onUpdate(controls.copy(whoCanMessageMe = aud))
        }
      )
    }

    // Who can add me to rooms
    item {
      SettingsSelectorCard(
        title = "Qui peut m'ajouter à un salon ou un groupe ?",
        subtitle = "Évite les ajouts abusifs à des groupes de spam",
        currentValue = controls.whoCanAddMeToRooms.label,
        options = listOf(
          CommunicationAudience.EVERYONE.label,
          CommunicationAudience.FRIENDS.label,
          CommunicationAudience.NOBODY.label
        ),
        onSelect = { label ->
          val aud = CommunicationAudience.entries.first { it.label == label }
          onUpdate(controls.copy(whoCanAddMeToRooms = aud))
        }
      )
    }

    // Who can call me
    item {
      SettingsSelectorCard(
        title = "Qui peut m'appeler en vocal ?",
        subtitle = "Restreindre les appels vocaux entrants",
        currentValue = controls.whoCanCallMe.label,
        options = listOf(
          CommunicationAudience.EVERYONE.label,
          CommunicationAudience.FRIENDS.label,
          CommunicationAudience.NOBODY.label
        ),
        onSelect = { label ->
          val aud = CommunicationAudience.entries.first { it.label == label }
          onUpdate(controls.copy(whoCanCallMe = aud))
        }
      )
    }

    // Who can mention me
    item {
      SettingsSelectorCard(
        title = "Qui peut me mentionner (@mon_nom) ?",
        subtitle = "Empêche les notifications de mentions répétitives de la part d'inconnus",
        currentValue = controls.whoCanMentionMe.label,
        options = listOf(
          CommunicationAudience.EVERYONE.label,
          CommunicationAudience.FRIENDS.label,
          CommunicationAudience.NOBODY.label
        ),
        onSelect = { label ->
          val aud = CommunicationAudience.entries.first { it.label == label }
          onUpdate(controls.copy(whoCanMentionMe = aud))
        }
      )
    }

    // Protection anti-harcèlement et anti-spam
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text("Filtres Avancés Anti-Harcèlement", color = GoldNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Filtrer les demandes de messages inconnus", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Text("Isole les premiers messages d'inconnus dans un dossier séparé sans sonnerie", color = TextMuted, fontSize = 10.sp)
            }
            Switch(
              checked = controls.filterUnknownMessageRequests,
              onCheckedChange = { onUpdate(controls.copy(filterUnknownMessageRequests = it)) }
            )
          }

          HorizontalDivider(color = CyberCardBorder)

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Limiter les rafales de messages (Anti-Flood)", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
              Text("Protège contre l'envoi rapide et répété de messages indésirables", color = TextMuted, fontSize = 10.sp)
            }
            Switch(
              checked = controls.limitUnknownMessages,
              onCheckedChange = { onUpdate(controls.copy(limitUnknownMessages = it)) }
            )
          }
        }
      }
    }
  }
}

// =============================================================================
// 4. BLOCKING & MUTING TAB: Instant 2-click block, search, mute timer
// =============================================================================
@Composable
private fun BlockingAndMutingTabContent(
  blockedUsers: List<BlockedUserItem>,
  mutedUsers: List<MutedUserItem>,
  onUnblockUser: (String) -> Unit,
  onBlockUser: (String, String, String) -> Unit,
  onUnmuteUser: (String) -> Unit,
  onMuteUser: (String, String, MuteDuration) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var showQuickBlockDialog by remember { mutableStateOf(false) }
  var showQuickMuteDialog by remember { mutableStateOf(false) }
  val context = LocalContext.current

  val filteredBlocked = remember(blockedUsers, searchQuery) {
    if (searchQuery.isBlank()) blockedUsers
    else blockedUsers.filter { it.username.contains(searchQuery, ignoreCase = true) }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header & Actions
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("Gestion des Blocages & Sourdines", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
          Text("Contrôlez vos interactions en silence et sans friction", color = TextMuted, fontSize = 11.sp)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Button(
            onClick = { showQuickBlockDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = PinkNeon),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("quick_block_button")
          ) {
            Text("+ Bloquer", fontSize = 11.sp, color = Color.White)
          }
          OutlinedButton(
            onClick = { showQuickMuteDialog = true },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("quick_mute_button")
          ) {
            Text("+ Sourdine", fontSize = 11.sp, color = CyanNeon)
          }
        }
      }
    }

    // Search bar
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Rechercher un membre bloqué...", color = TextMuted, fontSize = 12.sp) },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("search_blocked_users_input"),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = DarkSurface,
          unfocusedContainerColor = DarkSurface,
          focusedBorderColor = CyanNeon,
          unfocusedBorderColor = CyberCardBorder,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        ),
        singleLine = true
      )
    }

    // Blocked Users Section
    item {
      Text(
        text = "Utilisateurs Bloqués (${filteredBlocked.size})",
        color = PinkNeon,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
      )
    }

    if (filteredBlocked.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CyberCard),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
        ) {
          Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
            Text("Aucun utilisateur bloqué dans cette liste.", color = TextMuted, fontSize = 12.sp)
          }
        }
      }
    } else {
      items(filteredBlocked, key = { it.id }) { item ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CyberCard),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
          shape = RoundedCornerShape(10.dp)
        ) {
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
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(PinkNeon.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Text(item.avatarInitial, color = PinkNeon, fontWeight = FontWeight.Bold)
              }
              Column {
                Text(item.username, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Motif : ${item.reason}", color = TextSecondary, fontSize = 11.sp)
                Text(item.effectsDescription, color = TextMuted, fontSize = 10.sp)
              }
            }

            OutlinedButton(
              onClick = {
                onUnblockUser(item.id)
                Toast.makeText(context, "${item.username} a été débloqué.", Toast.LENGTH_SHORT).show()
              },
              colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldNeon),
              border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("unblock_button_${item.id}")
            ) {
              Text("Débloquer", fontSize = 11.sp)
            }
          }
        }
      }
    }

    // Muted Users Section
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = "Membres en Sourdine Discrète (${mutedUsers.size})",
        color = CyanGlow,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp
      )
      Text(
        text = "La sourdine coupe les notifications sans alerter l'utilisateur et sans supprimer les messages.",
        color = TextMuted,
        fontSize = 11.sp
      )
    }

    if (mutedUsers.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CyberCard),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
        ) {
          Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
            Text("Aucun membre en sourdine.", color = TextMuted, fontSize = 12.sp)
          }
        }
      }
    } else {
      items(mutedUsers, key = { it.id }) { item ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CyberCard),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
          shape = RoundedCornerShape(10.dp)
        ) {
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
              Icon(Icons.Default.NotificationsOff, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(22.dp))
              Column {
                Text(item.username, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Durée : ${item.duration.label} (Depuis ${item.mutedAt})", color = TextSecondary, fontSize = 11.sp)
                Text("Sourdine 100% silencieuse pour votre tranquillité", color = TextMuted, fontSize = 10.sp)
              }
            }

            OutlinedButton(
              onClick = {
                onUnmuteUser(item.id)
                Toast.makeText(context, "Sourdine retirée pour ${item.username}.", Toast.LENGTH_SHORT).show()
              },
              shape = RoundedCornerShape(6.dp)
            ) {
              Text("Réactiver son", fontSize = 11.sp, color = TextSecondary)
            }
          }
        }
      }
    }
  }

  // Quick Block Dialog (2 clicks: Enter Name -> Block)
  if (showQuickBlockDialog) {
    var targetName by remember { mutableStateOf("") }
    var blockReason by remember { mutableStateOf("Harcèlement ou propos non souhaités") }

    Dialog(onDismissRequest = { showQuickBlockDialog = false }) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PinkNeon)
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Text("Bloquer un Utilisateur (2 clics)", color = PinkNeon, fontWeight = FontWeight.Bold, fontSize = 15.sp)
          Text("Effet immédiat : masquage mutuel, appels et mentions bloqués.", color = TextSecondary, fontSize = 11.sp)

          OutlinedTextField(
            value = targetName,
            onValueChange = { targetName = it },
            label = { Text("Pseudo du joueur à bloquer") },
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = blockReason,
            onValueChange = { blockReason = it },
            label = { Text("Motif (Optionnel)") },
            modifier = Modifier.fillMaxWidth()
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedButton(onClick = { showQuickBlockDialog = false }) {
              Text("Annuler", color = TextMuted)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                if (targetName.isNotBlank()) {
                  val id = "usr_${targetName.lowercase().trim()}"
                  onBlockUser(id, targetName.trim(), blockReason)
                  Toast.makeText(context, "$targetName a été bloqué.", Toast.LENGTH_SHORT).show()
                  showQuickBlockDialog = false
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = PinkNeon),
              enabled = targetName.isNotBlank()
            ) {
              Text("Bloquer immédiatement", color = Color.White)
            }
          }
        }
      }
    }
  }

  // Quick Mute Dialog
  if (showQuickMuteDialog) {
    var targetName by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableStateOf(MuteDuration.ONE_DAY) }

    Dialog(onDismissRequest = { showQuickMuteDialog = false }) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon)
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Text("Mettre en Sourdine Discrète", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 15.sp)
          Text("L'utilisateur n'en sera jamais informé.", color = TextSecondary, fontSize = 11.sp)

          OutlinedTextField(
            value = targetName,
            onValueChange = { targetName = it },
            label = { Text("Pseudo du joueur") },
            modifier = Modifier.fillMaxWidth()
          )

          Text("Choisir la durée de sourdine :", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          MuteDuration.entries.forEach { duration ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(if (selectedDuration == duration) CyanNeon.copy(alpha = 0.15f) else Color.Transparent)
                .clickable { selectedDuration = duration }
                .padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(duration.label, color = if (selectedDuration == duration) CyanNeon else TextPrimary, fontSize = 12.sp)
              if (selectedDuration == duration) {
                Icon(Icons.Default.Check, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(16.dp))
              }
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            OutlinedButton(onClick = { showQuickMuteDialog = false }) {
              Text("Annuler", color = TextMuted)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                if (targetName.isNotBlank()) {
                  val id = "usr_${targetName.lowercase().trim()}"
                  onMuteUser(id, targetName.trim(), selectedDuration)
                  Toast.makeText(context, "$targetName est en sourdine pour ${selectedDuration.label}.", Toast.LENGTH_SHORT).show()
                  showQuickMuteDialog = false
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
              enabled = targetName.isNotBlank()
            ) {
              Text("Confirmer sourdine", color = CyberBlack, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

// =============================================================================
// 5. REPORTS & APPEALS TAB: Anonymous report, 3 clicks, tracking & appeal
// =============================================================================
@Composable
private fun ReportsAndAppealsTabContent(
  reports: List<UserSubmittedReport>,
  appeals: List<UserAppeal>,
  onSubmitReport: (SafetyReportCategory, String, String, String, String, Boolean) -> Unit,
  onSubmitAppeal: (String, String) -> Unit
) {
  var showReportDialog by remember { mutableStateOf(false) }
  var showAppealDialog by remember { mutableStateOf(false) }
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("Signalements & Recours Équitables", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
          Text("Signalement anonyme garanti • Droit au recours transparent", color = TextMuted, fontSize = 11.sp)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Button(
            onClick = { showReportDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = GoldNeon),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("open_submit_report_button")
          ) {
            Text("🚨 Signaler (3 clics)", color = CyberBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
          OutlinedButton(
            onClick = { showAppealDialog = true },
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Faire un recours", color = CyanGlow, fontSize = 11.sp)
          }
        }
      }
    }

    // Protection Guarantee Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(20.dp))
          Text(
            text = "Anonymat Garanti : La personne signalée ne saura JAMAIS qui a déposé le signalement. Le système anti-abus empêche également les signalements de masse malveillants.",
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 15.sp
          )
        }
      }
    }

    // Reports Tracking
    item {
      Text("Historique & Suivi de vos Signalements (${reports.size})", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }

    if (reports.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CyberCard)
        ) {
          Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
            Text("Vous n'avez déposé aucun signalement pour le moment.", color = TextMuted, fontSize = 12.sp)
          }
        }
      }
    } else {
      items(reports, key = { it.id }) { rep ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CyberCard),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
          shape = RoundedCornerShape(10.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${rep.category.label} • ${rep.targetType}",
                color = GoldNeon,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (rep.status.contains("Résolu")) EmeraldNeon.copy(alpha = 0.2f) else DarkSurface
              ) {
                Text(
                  text = rep.status,
                  color = if (rep.status.contains("Résolu")) EmeraldNeon else TextSecondary,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Text("Cible : ${rep.targetIdentifier}", color = TextPrimary, fontSize = 12.sp)
            Text("Motif : ${rep.reason}", color = TextSecondary, fontSize = 11.sp)
            if (rep.evidenceNote.isNotBlank()) {
              Text("Éléments transmis : ${rep.evidenceNote}", color = TextMuted, fontSize = 10.sp)
            }

            if (rep.adminFeedback != null) {
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = DarkSurface
              ) {
                Row(
                  modifier = Modifier.padding(8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(14.dp))
                  Text(text = "Réponse Équipe CGC : ${rep.adminFeedback}", color = TextPrimary, fontSize = 10.sp)
                }
              }
            }
          }
        }
      }
    }

    // Appeals Section
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Text("Recours en cas de sanction contestée (${appeals.size})", color = CyanGlow, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }

    if (appeals.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CyberCard)
        ) {
          Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
            Text("Aucun recours en cours. Votre compte est en parfaite conformité.", color = TextMuted, fontSize = 12.sp)
          }
        }
      }
    } else {
      items(appeals, key = { it.id }) { appeal ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = CyberCard),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon.copy(alpha = 0.3f)),
          shape = RoundedCornerShape(10.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Recours : ${appeal.sanctionType}", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 12.sp)
              Text(appeal.status, color = GoldNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Text("Explication transmise : ${appeal.explanation}", color = TextSecondary, fontSize = 11.sp)
          }
        }
      }
    }
  }

  // 3-Clicks Report Dialog
  if (showReportDialog) {
    var selectedCategory by remember { mutableStateOf(SafetyReportCategory.HARASSMENT) }
    var targetIdentifier by remember { mutableStateOf("") }
    var targetType by remember { mutableStateOf("Joueur") }
    var reasonDetails by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = { showReportDialog = false }) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldNeon)
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Signaler un problème (3 clics)", color = GoldNeon, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            IconButton(onClick = { showReportDialog = false }, modifier = Modifier.size(24.dp)) {
              Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
            }
          }

          // Clic 1: Target
          Text("1. Éléments à signaler :", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Joueur", "Message", "Salon", "Profil").forEach { type ->
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (targetType == type) GoldNeon.copy(alpha = 0.2f) else CyberCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (targetType == type) GoldNeon else CyberCardBorder),
                modifier = Modifier.clickable { targetType = type }
              ) {
                Text(
                  text = type,
                  color = if (targetType == type) GoldNeon else TextSecondary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }

          OutlinedTextField(
            value = targetIdentifier,
            onValueChange = { targetIdentifier = it },
            placeholder = { Text("Pseudo ou identifiant (ex: @Joueur)", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          // Clic 2: Category
          Text("2. Motif principal :", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          LazyColumn(
            modifier = Modifier.height(130.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            items(SafetyReportCategory.entries) { cat ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (selectedCategory == cat) GoldNeon.copy(alpha = 0.15f) else Color.Transparent)
                  .clickable { selectedCategory = cat }
                  .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(cat.label, color = if (selectedCategory == cat) GoldNeon else TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                  Text(cat.description, color = TextMuted, fontSize = 9.sp)
                }
                if (selectedCategory == cat) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = GoldNeon, modifier = Modifier.size(16.dp))
                }
              }
            }
          }

          // Clic 3: Submit with Anonymous Toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Switch(checked = isAnonymous, onCheckedChange = { isAnonymous = it })
              Text("Signalement 100% Anonyme", color = TextSecondary, fontSize = 11.sp)
            }
          }

          Button(
            onClick = {
              if (targetIdentifier.isNotBlank()) {
                onSubmitReport(
                  selectedCategory,
                  targetType,
                  targetIdentifier.trim(),
                  selectedCategory.label,
                  reasonDetails,
                  isAnonymous
                )
                Toast.makeText(context, "Signalement chiffré transmis à l'équipe de modération.", Toast.LENGTH_SHORT).show()
                showReportDialog = false
              }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = GoldNeon),
            enabled = targetIdentifier.isNotBlank()
          ) {
            Text("Transmettre le Signalement ➔", color = CyberBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }
    }
  }

  // Appeal Dialog
  if (showAppealDialog) {
    var sanctionType by remember { mutableStateOf("Avertissement") }
    var explanation by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { showAppealDialog = false }) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon)
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Déposer un Recours Respectueux", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Text("Vous avez le droit d'expliquer votre situation en toute sérénité.", color = TextSecondary, fontSize = 11.sp)

          OutlinedTextField(
            value = explanation,
            onValueChange = { explanation = it },
            placeholder = { Text("Détaillez vos explications et le contexte du malentendu...") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
          )

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(onClick = { showAppealDialog = false }) {
              Text("Annuler", color = TextMuted)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                if (explanation.isNotBlank()) {
                  onSubmitAppeal(sanctionType, explanation.trim())
                  Toast.makeText(context, "Votre recours a été transmis pour révision indépendante.", Toast.LENGTH_SHORT).show()
                  showAppealDialog = false
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
              enabled = explanation.isNotBlank()
            ) {
              Text("Soumettre le recours", color = CyberBlack, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

// =============================================================================
// 6. SESSIONS & ACCOUNT SECURITY TAB: 2FA, Devices, Terminate Sessions
// =============================================================================
@Composable
private fun SessionsAndSecurityTabContent(
  isTwoFactorEnabled: Boolean,
  activeSessions: List<UserActiveSession>,
  onToggleTwoFactor: () -> Unit,
  onTerminateSession: (String) -> Unit,
  onTerminateAllOtherSessions: () -> Unit
) {
  val context = LocalContext.current
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text("Sécurité du Compte & Sessions Actives", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
      Text("Gérez vos appareils connectés et renforcez vos clés d'accès.", color = TextMuted, fontSize = 11.sp)
    }

    // 2FA Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isTwoFactorEnabled) EmeraldNeon else GoldNeon),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(
              Icons.Default.Fingerprint,
              contentDescription = null,
              tint = if (isTwoFactorEnabled) EmeraldNeon else GoldNeon,
              modifier = Modifier.size(28.dp)
            )
            Column {
              Text("Authentification à Deux Facteurs (2FA)", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text(
                if (isTwoFactorEnabled) "Actif • Validation requise lors de chaque nouvelle connexion"
                else "Recommandé pour sécuriser votre compte contre toute usurpation",
                color = TextSecondary,
                fontSize = 11.sp
              )
            }
          }

          Switch(
            checked = isTwoFactorEnabled,
            onCheckedChange = {
              onToggleTwoFactor()
              Toast.makeText(
                context,
                if (!isTwoFactorEnabled) "2FA activé avec succès !" else "2FA désactivé.",
                Toast.LENGTH_SHORT
              ).show()
            }
          )
        }
      }
    }

    // Active Sessions Header with "Déconnecter tous les autres"
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("Appareils & Sessions Actives (${activeSessions.size})", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text("Consultez et révoquez l'accès à distance", color = TextMuted, fontSize = 11.sp)
        }

        OutlinedButton(
          onClick = {
            onTerminateAllOtherSessions()
            Toast.makeText(context, "Toutes les autres sessions ont été fermées.", Toast.LENGTH_SHORT).show()
          },
          shape = RoundedCornerShape(6.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, PinkNeon)
        ) {
          Text("Déconnecter les autres", color = PinkNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    // Sessions list
    items(activeSessions, key = { it.id }) { sess ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
          containerColor = if (sess.isCurrent) EmeraldGlow.copy(alpha = 0.08f) else CyberCard
        ),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (sess.isCurrent) EmeraldNeon else CyberCardBorder
        ),
        shape = RoundedCornerShape(10.dp)
      ) {
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
            Icon(
              imageVector = Icons.Default.PhoneAndroid,
              contentDescription = null,
              tint = if (sess.isCurrent) EmeraldNeon else TextSecondary,
              modifier = Modifier.size(24.dp)
            )
            Column {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(sess.device, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                if (sess.isCurrent) {
                  Surface(shape = RoundedCornerShape(4.dp), color = EmeraldNeon.copy(alpha = 0.2f)) {
                    Text("Cet appareil", color = EmeraldNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                  }
                }
              }
              Text("${sess.browserOrApp} • ${sess.approximateLocation}", color = TextSecondary, fontSize = 11.sp)
              Text("Dernière activité : ${sess.lastActivity} (${sess.ipAddress})", color = TextMuted, fontSize = 10.sp)
            }
          }

          if (!sess.isCurrent) {
            Button(
              onClick = {
                onTerminateSession(sess.id)
                Toast.makeText(context, "Session révoquée.", Toast.LENGTH_SHORT).show()
              },
              colors = ButtonDefaults.buttonColors(containerColor = PinkNeon.copy(alpha = 0.2f)),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text("Déconnecter", color = PinkNeon, fontSize = 10.sp)
            }
          }
        }
      }
    }
  }
}

// =============================================================================
// 7. USER DATA & TRANSPARENCY TAB: View, Export JSON, Account Deletion
// =============================================================================
@Composable
private fun UserDataAndTransparencyTabContent(
  currentProfile: GamerProfile,
  onExportData: () -> String
) {
  var exportedJson by remember { mutableStateOf<String?>(null) }
  var showDeleteConfirmation by remember { mutableStateOf(false) }
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text("Maîtrise de vos Données Personnelles", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
      Text("Vos données vous appartiennent. Aucune revente commerciale, transparence 100%.", color = TextMuted, fontSize = 11.sp)
    }

    // Export Data Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Icon(Icons.Default.Download, contentDescription = null, tint = CyanNeon)
              Text("Télécharger / Exporter mon compte", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Button(
              onClick = {
                exportedJson = onExportData()
                Toast.makeText(context, "Archive des données générée avec succès.", Toast.LENGTH_SHORT).show()
              },
              colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("export_data_button")
            ) {
              Text("Générer l'export JSON", color = CyberBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
          }
          Text(
            text = "Conformément au principe de portabilité, vous pouvez exporter une archive lisible de votre profil, historique de jeux, préférences et paramètres de sécurité.",
            color = TextSecondary,
            fontSize = 11.sp
          )

          if (exportedJson != null) {
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
              color = DarkSurface,
              shape = RoundedCornerShape(8.dp)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text("Export JSON sécurisé :", color = EmeraldNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(
                  text = exportedJson ?: "",
                  color = TextPrimary,
                  fontSize = 10.sp,
                  lineHeight = 14.sp
                )
              }
            }
          }
        }
      }
    }

    // Data Transparency Explanations
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Transparence de Collecte CGC :", color = EmeraldNeon, fontWeight = FontWeight.Bold, fontSize = 13.sp)

          val items = listOf(
            "Données conservées" to "Pseudonyme, mot de passe chiffré (Argon2), participations aux tournois et messages échangés.",
            "Pourquoi ces données ?" to "Permettre le matchmaking esport, l'attribution des récompenses et la sécurité contre les tricheurs.",
            "Partage avec des tiers" to "AUCUN partage ou revente à des régies publicitaires ou courtiers de données.",
            "Durée de conservation" to "Vos données sont conservées tant que votre compte est actif et purgées sous 30 jours en cas de suppression."
          )

          items.forEach { (title, desc) ->
            Column {
              Text(title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
              Text(desc, color = TextMuted, fontSize = 11.sp)
            }
          }
        }
      }
    }

    // Account Deactivation & Deletion
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, PinkNeon.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Désactivation ou Suppression Définitive", color = PinkNeon, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text(
            text = "Vous pouvez désactiver temporairement votre compte ou demander sa suppression irréversible sans aucune pénalité ni obstacle.",
            color = TextSecondary,
            fontSize = 11.sp
          )

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
              onClick = {
                Toast.makeText(context, "Compte mis en pause temporaire.", Toast.LENGTH_SHORT).show()
              },
              shape = RoundedCornerShape(6.dp)
            ) {
              Text("Désactiver temporairement", fontSize = 11.sp, color = TextSecondary)
            }

            Button(
              onClick = { showDeleteConfirmation = true },
              colors = ButtonDefaults.buttonColors(containerColor = PinkNeon),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text("Supprimer mon compte", fontSize = 11.sp, color = Color.White)
            }
          }
        }
      }
    }
  }

  // Delete Confirmation Dialog
  if (showDeleteConfirmation) {
    Dialog(onDismissRequest = { showDeleteConfirmation = false }) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PinkNeon)
      ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Confirmation de suppression", color = PinkNeon, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Text(
            "Êtes-vous certain de vouloir supprimer votre compte CGC ? Toutes vos statistiques, tournois et trophées seront effacés de manière permanente.",
            color = TextSecondary,
            fontSize = 11.sp
          )
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(onClick = { showDeleteConfirmation = false }) {
              Text("Conserver mon compte", color = TextMuted)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                showDeleteConfirmation = false
                Toast.makeText(context, "Demande de suppression enregistrée avec période de grâce de 30 jours.", Toast.LENGTH_LONG).show()
              },
              colors = ButtonDefaults.buttonColors(containerColor = PinkNeon)
            ) {
              Text("Confirmer la suppression", color = Color.White)
            }
          }
        }
      }
    }
  }
}

// =============================================================================
// Helper Reusable Components
// =============================================================================
@Composable
private fun RecommendationActionCard(
  title: String,
  subtitle: String,
  actionText: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
    shape = RoundedCornerShape(10.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(subtitle, color = TextMuted, fontSize = 10.sp)
      }
      Text(actionText, color = CyanGlow, fontWeight = FontWeight.Bold, fontSize = 11.sp)
    }
  }
}

@Composable
private fun SettingsSelectorCard(
  title: String,
  subtitle: String,
  currentValue: String,
  options: List<String>,
  onSelect: (String) -> Unit
) {
  var expanded by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text(subtitle, color = TextMuted, fontSize = 10.sp)
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = EmeraldNeon.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon),
          modifier = Modifier.clickable { expanded = !expanded }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(currentValue, color = EmeraldNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(if (expanded) "▲" else "▼", color = EmeraldNeon, fontSize = 9.sp)
          }
        }
      }

      AnimatedVisibility(visible = expanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface, RoundedCornerShape(6.dp))
            .padding(6.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          options.forEach { option ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(if (option == currentValue) EmeraldNeon.copy(alpha = 0.2f) else Color.Transparent)
                .clickable {
                  onSelect(option)
                  expanded = false
                }
                .padding(horizontal = 8.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = option,
                color = if (option == currentValue) EmeraldNeon else TextPrimary,
                fontSize = 11.sp,
                fontWeight = if (option == currentValue) FontWeight.Bold else FontWeight.Normal
              )
              if (option == currentValue) {
                Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(14.dp))
              }
            }
          }
        }
      }
    }
  }
}
