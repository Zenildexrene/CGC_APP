package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AboutCgcMessage
import com.example.data.model.GamerProfile
import com.example.data.model.PrivacySettings
import com.example.data.model.UserRole
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MenuDialog(
  profile: GamerProfile,
  privacySettings: PrivacySettings,
  aboutMessages: List<AboutCgcMessage>,
  onDismiss: () -> Unit,
  onProfileClick: () -> Unit,
  onCreatorConsoleClick: () -> Unit,
  onAuthClick: () -> Unit,
  onToggleHideEmail: () -> Unit,
  onToggleHideCode: () -> Unit,
  onToggleE2EE: () -> Unit,
  onAddAboutMessage: (title: String, content: String) -> Unit,
  onDeleteAboutMessage: (String) -> Unit
) {
  var showAddAboutDialog by remember { mutableStateOf(false) }

  // Local settings toggles
  var tournamentAlerts by remember { mutableStateOf(true) }
  var liveStreamAlerts by remember { mutableStateOf(true) }
  var discordAlerts by remember { mutableStateOf(true) }
  var dataSaverMode by remember { mutableStateOf(false) }
  var selectedPlatform by remember { mutableStateOf("PlayStation 5") }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .background(CyberBlack)
        .testTag("cgc_menu_screen"),
      color = CyberBlack
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Menu Top Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(DarkSurface)
            .padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(EmeraldNeon),
              contentAlignment = Alignment.Center
            ) {
              Text("CGC", color = CyberBlack, fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
            Text(
              text = "Menu & Paramètres",
              color = TextPrimary,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold
            )
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextPrimary)
          }
        }

        HorizontalDivider(color = CyberCardBorder)

        LazyColumn(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
          contentPadding = PaddingValues(vertical = 14.dp)
        ) {
          // 1. Profile Shortcut Header Card
          item {
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onDismiss()
                  onProfileClick()
                },
              colors = CardDefaults.cardColors(containerColor = CyberCard),
              border = androidx.compose.foundation.BorderStroke(1.dp, if (profile.isCreator) GoldNeon else FacebookBlue),
              shape = RoundedCornerShape(14.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(DarkSurface)
                    .border(2.dp, if (profile.isCreator) GoldNeon else EmeraldNeon, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = profile.secretName.take(1).uppercase(),
                    color = if (profile.isCreator) GoldNeon else EmeraldGlow,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                  )
                }

                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                      text = profile.secretName,
                      color = TextPrimary,
                      fontSize = 16.sp,
                      fontWeight = FontWeight.Bold
                    )
                    if (profile.isCreator) {
                      Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = GoldNeon.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldNeon)
                      ) {
                        Text(
                          "👑 CRÉATEUR",
                          color = GoldNeon,
                          fontSize = 9.sp,
                          fontWeight = FontWeight.Bold,
                          modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                      }
                    }
                  }
                  Text(
                    text = "Voir et modifier votre profil de joueur",
                    color = TextSecondary,
                    fontSize = 11.sp
                  )
                }

                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = DarkSurface,
                  border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
                ) {
                  Text(
                    "Profil",
                    color = FacebookBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                  )
                }
              }
            }
          }

          // 2. Section: Administration (if Admin / Creator)
          if (profile.canManageContent) {
            item {
              SettingsGroupCard(title = "👑 Privilèges Administrateur CGC") {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                      onDismiss()
                      onCreatorConsoleClick()
                    }
                    .padding(vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = GoldNeon, modifier = Modifier.size(20.dp))
                    Column {
                      Text("Console Créateur & Gestion", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                      Text("Gérer rôles, bannir tricheurs, créer tournois", color = TextSecondary, fontSize = 11.sp)
                    }
                  }
                  Text("Ouvrir ➔", color = GoldNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }

          // 3. Section: Sécurité des Données & Anti-Fuite
          item {
            SettingsGroupCard(title = "🔒 Confidentialité & Sécurité Anti-Fuite") {
              // Email privacy toggle
              SettingsToggleRow(
                icon = Icons.Default.Lock,
                title = "Masquer l'adresse email publique",
                subtitle = "Affichage masqué : ${profile.maskedEmail}",
                isChecked = privacySettings.hideEmailFromPublic,
                onCheckedChange = { onToggleHideEmail() }
              )

              HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.5f))

              // Secret code privacy toggle
              SettingsToggleRow(
                icon = Icons.Default.VpnKey,
                title = "Protection du mot de passe / code d'accès",
                subtitle = "Chiffré en mémoire et invisible sur les écrans",
                isChecked = privacySettings.hideSecretCode,
                onCheckedChange = { onToggleHideCode() }
              )

              HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.5f))

              // E2EE toggle
              SettingsToggleRow(
                icon = Icons.Default.Shield,
                title = "Chiffrement de bout en bout (E2EE)",
                subtitle = "Sécurise les messages directs et salons privés",
                isChecked = privacySettings.e2eeChatActive,
                onCheckedChange = { onToggleE2EE() }
              )

              HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.5f))

              // Switch Account button
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    onDismiss()
                    onAuthClick()
                  }
                  .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Changer d'utilisateur ou se reconnecter", color = CyanGlow, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Text("Connexion ➔", color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }

          // 4. Section: Notifications
          item {
            SettingsGroupCard(title = "🔔 Notifications & Alertes Gaming") {
              SettingsToggleRow(
                icon = Icons.Default.SportsEsports,
                title = "Alertes de nouveaux tournois",
                subtitle = "Recevez une notification lors de l'ouverture d'un tournoi",
                isChecked = tournamentAlerts,
                onCheckedChange = { tournamentAlerts = it }
              )

              HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.5f))

              SettingsToggleRow(
                icon = Icons.Default.Videocam,
                title = "Notifications de lives (TikTok & YouTube)",
                subtitle = "Soyez prévenu dès qu'un streamer CGC lance un live",
                isChecked = liveStreamAlerts,
                onCheckedChange = { liveStreamAlerts = it }
              )

              HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.5f))

              SettingsToggleRow(
                icon = Icons.Default.Headphones,
                title = "Notifications vocales Discord",
                subtitle = "Alertes de salons vocaux et mentions",
                isChecked = discordAlerts,
                onCheckedChange = { discordAlerts = it }
              )
            }
          }

          // 5. Section: Préférences Gaming & Réseau
          item {
            SettingsGroupCard(title = "🎮 Préférences de Jeu & Réseau") {
              Text("Plateforme de jeu principale :", color = TextSecondary, fontSize = 11.sp)
              Spacer(modifier = Modifier.height(6.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                listOf("PlayStation 5", "Xbox", "PC", "Mobile").forEach { platform ->
                  val isSelected = selectedPlatform == platform
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) EmeraldNeon else DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) EmeraldGlow else CyberCardBorder),
                    modifier = Modifier
                      .weight(1f)
                      .clickable { selectedPlatform = platform }
                  ) {
                    Text(
                      text = platform,
                      color = if (isSelected) CyberBlack else TextSecondary,
                      fontSize = 10.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                      modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                      textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(10.dp))
              HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.5f))

              SettingsToggleRow(
                icon = Icons.Default.Wifi,
                title = "Mode Économie de Données RDC",
                subtitle = "Optimise la consommation de mégaoctets pour les connexions mobiles",
                isChecked = dataSaverMode,
                onCheckedChange = { dataSaverMode = it }
              )
            }
          }

          // 6. Section: À PROPOS DU CGC (Demandé par l'utilisateur avec ajout de messages pour administrateur)
          item {
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .testTag("about_cgc_section"),
              colors = CardDefaults.cardColors(containerColor = CyberCard),
              border = androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldNeon.copy(alpha = 0.7f)),
              shape = RoundedCornerShape(14.dp)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                // Header of À propos
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(20.dp))
                    Column {
                      Text(
                        text = "À PROPOS DU CGC",
                        color = TextPrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                      )
                      Text(
                        text = "COMMUNAUTÉ GAMING CONGOLAISE 🇨🇩",
                        color = EmeraldGlow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }

                  // Admin button to add messages from the inside
                  if (profile.canManageContent) {
                    Button(
                      onClick = { showAddAboutDialog = true },
                      colors = ButtonDefaults.buttonColors(containerColor = GoldNeon, contentColor = CyberBlack),
                      shape = RoundedCornerShape(8.dp),
                      contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                      modifier = Modifier.testTag("add_about_message_button")
                    ) {
                      Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Ajouter", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                  }
                }

                HorizontalDivider(color = CyberCardBorder)

                // List of About Messages
                aboutMessages.forEach { msg ->
                  Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
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
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                          Icon(Icons.Default.PushPin, contentDescription = null, tint = GoldNeon, modifier = Modifier.size(14.dp))
                          Text(
                            text = "${msg.authorName} (${msg.authorRole.label})",
                            color = GoldNeon,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                          )
                        }

                        if (profile.canManageContent) {
                          IconButton(
                            onClick = { onDeleteAboutMessage(msg.id) },
                            modifier = Modifier.size(24.dp)
                          ) {
                            Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = PinkNeon, modifier = Modifier.size(14.dp))
                          }
                        }
                      }

                      Text(
                        text = msg.title,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                      )

                      Text(
                        text = msg.content,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                      )

                      Text(
                        text = "Publié en ${msg.timestamp}",
                        color = TextMuted,
                        fontSize = 9.sp
                      )
                    }
                  }
                }

                // Footer legal and technical notes
                Column(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Text(
                    text = "CGC v2.4.0 • Édition Officielle RDC",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                  )
                  Text(
                    text = "Développé pour les passionnés de jeux vidéo en République Démocratique du Congo",
                    color = TextMuted,
                    fontSize = 9.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                  )
                }
              }
            }
          }

          item {
            Spacer(modifier = Modifier.height(20.dp))
          }
        }
      }
    }
  }

  // --- Dialog: Admin Add About CGC Message ---
  if (showAddAboutDialog) {
    var titleInput by remember { mutableStateOf("") }
    var contentInput by remember { mutableStateOf("") }

    AlertDialog(
      onDismissRequest = { showAddAboutDialog = false },
      containerColor = CyberCard,
      title = {
        Text("Publier un message officiel 'À propos'", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            "Ce message sera affiché dans la section 'À propos du CGC' pour tous les membres.",
            color = TextSecondary,
            fontSize = 11.sp
          )
          OutlinedTextField(
            value = titleInput,
            onValueChange = { titleInput = it },
            placeholder = { Text("Titre du message (ex: Nouveau partenariat esport)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = EmeraldNeon,
              unfocusedBorderColor = CyberCardBorder,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary
            )
          )
          OutlinedTextField(
            value = contentInput,
            onValueChange = { contentInput = it },
            placeholder = { Text("Contenu officiel du message...") },
            minLines = 4,
            maxLines = 6,
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
            if (titleInput.isNotBlank() && contentInput.isNotBlank()) {
              onAddAboutMessage(titleInput.trim(), contentInput.trim())
              showAddAboutDialog = false
            }
          },
          enabled = titleInput.isNotBlank() && contentInput.isNotBlank(),
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon, contentColor = CyberBlack)
        ) {
          Text("Publier sur la CGC", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddAboutDialog = false }) {
          Text("Annuler", color = TextSecondary)
        }
      }
    )
  }
}

@Composable
private fun SettingsGroupCard(
  title: String,
  content: @Composable () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
    shape = RoundedCornerShape(14.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text(
        text = title,
        color = TextPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
      )
      HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.5f))
      content()
    }
  }
}

@Composable
private fun SettingsToggleRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  subtitle: String,
  isChecked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      modifier = Modifier.weight(1f),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(20.dp))
      Column {
        Text(text = title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(text = subtitle, color = TextMuted, fontSize = 10.sp)
      }
    }

    Switch(
      checked = isChecked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = EmeraldNeon,
        checkedTrackColor = EmeraldGlow.copy(alpha = 0.3f)
      )
    )
  }
}
