package com.example.ui.components

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppNotification
import com.example.data.model.NotificationPreferences
import com.example.data.model.NotificationType
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardHighlight
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DiscordBlurple
import com.example.ui.theme.DiscordDark
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldNeon
import com.example.ui.theme.PinkNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun NotificationsCenterDialog(
  notifications: List<AppNotification>,
  preferences: NotificationPreferences,
  onDismiss: () -> Unit,
  onMarkAsRead: (String) -> Unit,
  onMarkAllAsRead: () -> Unit,
  onDeleteNotification: (String) -> Unit,
  onClearAll: () -> Unit,
  onUpdatePreferences: (NotificationPreferences) -> Unit,
  onNavigateToTarget: (String) -> Unit = {}
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Toutes, 1: Non lues, 2: Paramètres
  val unreadCount = notifications.count { !it.isRead }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .padding(top = 28.dp),
      color = CyberBlack,
      shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Top Header
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
                .background(DiscordBlurple.copy(alpha = 0.2f))
                .border(1.dp, DiscordBlurple, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = null,
                tint = DiscordBlurple,
                modifier = Modifier.size(20.dp)
              )
            }
            Column {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                  text = "Centre de Notifications",
                  color = TextPrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp
                )
                if (unreadCount > 0) {
                  Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PinkNeon
                  ) {
                    Text(
                      text = "$unreadCount non lue${if (unreadCount > 1) "s" else ""}",
                      color = Color.White,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }
              }
              Text(
                text = "Temps réel • In-app, push & mentions",
                color = TextSecondary,
                fontSize = 11.sp
              )
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextSecondary)
          }
        }

        HorizontalDivider(color = CyberCardBorder)

        // Sub-tabs: Toutes / Non lues / Réglages
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .padding(horizontal = 12.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilterTabChip(
              label = "Toutes (${notifications.size})",
              isSelected = selectedTab == 0,
              onClick = { selectedTab = 0 }
            )
            FilterTabChip(
              label = "Non lues ($unreadCount)",
              isSelected = selectedTab == 1,
              onClick = { selectedTab = 1 }
            )
            FilterTabChip(
              label = "⚙️ Réglages",
              isSelected = selectedTab == 2,
              onClick = { selectedTab = 2 }
            )
          }

          if (selectedTab != 2 && notifications.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              if (unreadCount > 0) {
                IconButton(onClick = onMarkAllAsRead, modifier = Modifier.size(28.dp)) {
                  Icon(Icons.Default.DoneAll, contentDescription = "Tout marquer lu", tint = EmeraldNeon, modifier = Modifier.size(16.dp))
                }
              }
              IconButton(onClick = onClearAll, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.ClearAll, contentDescription = "Tout effacer", tint = TextMuted, modifier = Modifier.size(16.dp))
              }
            }
          }
        }

        HorizontalDivider(color = CyberCardBorder)

        // Body Content
        Box(
          modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(14.dp)
        ) {
          when (selectedTab) {
            0 -> NotificationsList(
              items = notifications,
              onMarkAsRead = onMarkAsRead,
              onDelete = onDeleteNotification,
              onNavigate = {
                onNavigateToTarget(it)
                onDismiss()
              }
            )
            1 -> {
              val unreadList = notifications.filter { !it.isRead }
              NotificationsList(
                items = unreadList,
                onMarkAsRead = onMarkAsRead,
                onDelete = onDeleteNotification,
                onNavigate = {
                  onNavigateToTarget(it)
                  onDismiss()
                },
                emptyMessage = "Aucune notification non lue. Vous êtes à jour !"
              )
            }
            2 -> NotificationPreferencesTab(
              preferences = preferences,
              onUpdate = onUpdatePreferences
            )
          }
        }
      }
    }
  }
}

@Composable
private fun FilterTabChip(
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = if (isSelected) EmeraldNeon.copy(alpha = 0.2f) else CyberBlack,
    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) EmeraldNeon else CyberCardBorder),
    modifier = Modifier.clickable { onClick() }
  ) {
    Text(
      text = label,
      color = if (isSelected) EmeraldNeon else TextSecondary,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    )
  }
}

@Composable
private fun NotificationsList(
  items: List<AppNotification>,
  onMarkAsRead: (String) -> Unit,
  onDelete: (String) -> Unit,
  onNavigate: (String) -> Unit,
  emptyMessage: String = "Aucune notification pour le moment."
) {
  if (items.isEmpty()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.NotificationsOff,
          contentDescription = null,
          tint = TextMuted,
          modifier = Modifier.size(48.dp)
        )
        Text(
          text = emptyMessage,
          color = TextSecondary,
          fontSize = 13.sp
        )
      }
    }
  } else {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(items, key = { it.id }) { notif ->
        NotificationCardItem(
          item = notif,
          onMarkAsRead = { onMarkAsRead(notif.id) },
          onDelete = { onDelete(notif.id) },
          onClick = {
            onMarkAsRead(notif.id)
            if (notif.deepLinkDestination.isNotBlank()) {
              onNavigate(notif.deepLinkDestination)
            }
          }
        )
      }
    }
  }
}

@Composable
private fun NotificationCardItem(
  item: AppNotification,
  onMarkAsRead: () -> Unit,
  onDelete: () -> Unit,
  onClick: () -> Unit
) {
  val borderCol = if (!item.isRead) DiscordBlurple.copy(alpha = 0.7f) else CyberCardBorder
  val bgCol = if (!item.isRead) DiscordDark else CyberCard

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    colors = CardDefaults.cardColors(containerColor = bgCol),
    border = androidx.compose.foundation.BorderStroke(1.dp, borderCol),
    shape = RoundedCornerShape(10.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.Top
    ) {
      // Emoji Box Indicator
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(CyberBlack)
          .border(1.dp, if (!item.isRead) DiscordBlurple else CyberCardBorder, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = item.type.iconEmoji,
          fontSize = 18.sp
        )
      }

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = item.title,
              color = TextPrimary,
              fontWeight = if (!item.isRead) FontWeight.Bold else FontWeight.Medium,
              fontSize = 13.sp
            )
            if (!item.isRead) {
              Box(
                modifier = Modifier
                  .size(7.dp)
                  .clip(CircleShape)
                  .background(PinkNeon)
              )
            }
          }
          Text(
            text = item.timestamp,
            color = TextMuted,
            fontSize = 10.sp
          )
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
          text = item.message,
          color = if (!item.isRead) TextPrimary else TextSecondary,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = CyberBlack
          ) {
            Text(
              text = "${item.type.label} • ${item.senderName}",
              color = TextMuted,
              fontSize = 9.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            if (!item.isRead) {
              IconButton(onClick = onMarkAsRead, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Check, contentDescription = "Marquer lu", tint = EmeraldNeon, modifier = Modifier.size(14.dp))
              }
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
              Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = TextMuted, modifier = Modifier.size(14.dp))
            }
          }
        }
      }
    }
  }
}

@Composable
private fun NotificationPreferencesTab(
  preferences: NotificationPreferences,
  onUpdate: (NotificationPreferences) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text(
        text = "Canaux de Réception",
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
      )
      Text(
        text = "Choisissez comment recevoir les alertes de la communauté",
        color = TextSecondary,
        fontSize = 11.sp
      )
    }

    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          PrefSwitchRow(
            title = "Notifications In-App (Bannières)",
            subtitle = "Afficher les alertes animées pendant l'utilisation",
            checked = preferences.inAppNotifications,
            onCheckedChange = { onUpdate(preferences.copy(inAppNotifications = it)) }
          )
          HorizontalDivider(color = CyberCardBorder)
          PrefSwitchRow(
            title = "Notifications Push Téléphone",
            subtitle = "Recevoir les alertes même application fermée",
            checked = preferences.pushNotifications,
            onCheckedChange = { onUpdate(preferences.copy(pushNotifications = it)) }
          )
          HorizontalDivider(color = CyberCardBorder)
          PrefSwitchRow(
            title = "Récapitulatif E-mail",
            subtitle = "Résumé hebdomadaire des tournois et annonces majeures",
            checked = preferences.emailDigest,
            onCheckedChange = { onUpdate(preferences.copy(emailDigest = it)) }
          )
        }
      }
    }

    item {
      Text(
        text = "Catégories d'Alertes",
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
      )
      Text(
        text = "Filtrage granulaire pour votre confort et sérénité",
        color = TextSecondary,
        fontSize = 11.sp
      )
    }

    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          PrefSwitchRow(
            title = "💬 Nouveaux Messages Privés & Salons",
            subtitle = "Alertes de messages directs et canaux surveillés",
            checked = preferences.notifyNewMessages,
            onCheckedChange = { onUpdate(preferences.copy(notifyNewMessages = it)) }
          )
          HorizontalDivider(color = CyberCardBorder)
          PrefSwitchRow(
            title = "📣 Mentions & Réponses directes",
            subtitle = "Quand quelqu'un vous cite (@votre_pseudo)",
            checked = preferences.notifyMentions,
            onCheckedChange = { onUpdate(preferences.copy(notifyMentions = it)) }
          )
          HorizontalDivider(color = CyberCardBorder)
          PrefSwitchRow(
            title = "🔥 Réactions sur vos messages",
            subtitle = "Flammes, trophées et likes reçus",
            checked = preferences.notifyReactions,
            onCheckedChange = { onUpdate(preferences.copy(notifyReactions = it)) }
          )
          HorizontalDivider(color = CyberCardBorder)
          PrefSwitchRow(
            title = "👥 Demandes d'amis & Confirmations",
            subtitle = "Nouvelles demandes de connexion sociale",
            checked = preferences.notifyFriendRequests,
            onCheckedChange = { onUpdate(preferences.copy(notifyFriendRequests = it)) }
          )
          HorizontalDivider(color = CyberCardBorder)
          PrefSwitchRow(
            title = "🎮 Invitations Groupes & Tournois",
            subtitle = "Matchs, invitations d'équipe et salons vocaux",
            checked = preferences.notifyGroupInvites,
            onCheckedChange = { onUpdate(preferences.copy(notifyGroupInvites = it)) }
          )
          HorizontalDivider(color = CyberCardBorder)
          PrefSwitchRow(
            title = "🛡️ Alertes de Sécurité & Connexions",
            subtitle = "Nouvelle session, tentative de connexion ou 2FA",
            checked = preferences.notifySecurityAlerts,
            onCheckedChange = { onUpdate(preferences.copy(notifySecurityAlerts = it)) }
          )
        }
      }
    }

    item {
      Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          PrefSwitchRow(
            title = "🌙 Mode Heures Silencieuses (Ne Pas Déranger)",
            subtitle = "Désactive sons et vibrations entre 23:00 et 07:00",
            checked = preferences.quietHoursEnabled,
            onCheckedChange = { onUpdate(preferences.copy(quietHoursEnabled = it)) }
          )
        }
      }
    }
  }
}

@Composable
private fun PrefSwitchRow(
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
      Text(subtitle, color = TextSecondary, fontSize = 10.sp)
    }
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = EmeraldNeon,
        uncheckedTrackColor = DarkSurface
      )
    )
  }
}
