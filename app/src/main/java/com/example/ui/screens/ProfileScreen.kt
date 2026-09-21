package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Announcement
import com.example.data.model.GamerProfile
import com.example.data.model.PrivacySettings
import com.example.data.model.UserRole
import com.example.ui.components.GamerCardView
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardHighlight
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.GoldNeon
import com.example.ui.theme.PinkNeon
import com.example.ui.theme.PurpleNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
  profile: GamerProfile,
  privacySettings: PrivacySettings = PrivacySettings(),
  announcements: List<Announcement> = emptyList(),
  onConvertXp: (Int) -> Boolean,
  onGiveXp: (Int) -> Boolean,
  onDailySpin: () -> Int,
  onUpdateProfile: (name: String, title: String, clan: String) -> Unit,
  onUpdateExtendedProfile: (name: String, title: String, clan: String, bio: String, location: String, coverTheme: String) -> Unit = { _, _, _, _, _, _ -> },
  onUpdateJournalCustomization: (bio: String, motto: String, location: String, coverTheme: String, platform: String, clan: String) -> Unit = { _, _, _, _, _, _ -> },
  onToggleHideEmail: () -> Unit = {},
  onToggleHideCode: () -> Unit = {},
  onToggleE2EE: () -> Unit = {},
  onAddAnnouncement: (title: String, content: String, tag: String) -> Unit = { _, _, _ -> },
  onDeletePost: (String) -> Unit = {},
  onOpenCreatorConsole: () -> Unit = {},
  onOpenAdminDashboard: () -> Unit = {},
  onOpenSafetyDashboard: () -> Unit = {},
  onOpenAuth: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  var showCustomizeJournalDialog by remember { mutableStateOf(false) }
  var showCoverThemeDialog by remember { mutableStateOf(false) }
  var showWritePostDialog by remember { mutableStateOf(false) }
  
  var isSpinning by remember { mutableStateOf(false) }
  var lastSpinReward by remember { mutableStateOf<Int?>(null) }
  val spinRotation = remember { Animatable(0f) }
  val coroutineScope = rememberCoroutineScope()

  val tabs = listOf("Mon Journal", "À propos", "Carte Gamer & XP", "Sécurité Zéro-Fuite")

  val coverGradient = when (profile.coverTheme) {
    "cyber_gold" -> listOf(Color(0xFF8A6508), Color(0xFF2E2002), CyberBlack)
    "fire_leopard" -> listOf(Color(0xFFB71C1C), Color(0xFF4A0E17), CyberBlack)
    "blue_voltage" -> listOf(Color(0xFF0D47A1), Color(0xFF051B3B), CyberBlack)
    "dark_stealth" -> listOf(Color(0xFF263238), Color(0xFF101416), CyberBlack)
    else -> listOf(Color(0xFF004D40), Color(0xFF00241B), CyberBlack) // cyber_emerald
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .testTag("gamer_journal_screen")
  ) {
    // ---------------------------------------------------------
    // 1. Facebook Cover Photo & Profile Header
    // ---------------------------------------------------------
    item {
      Box(modifier = Modifier.fillMaxWidth()) {
        // Cover Photo Container
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Brush.verticalGradient(coverGradient))
            .clickable { showCoverThemeDialog = true }
        ) {
          // Cover tag & Badge
          Row(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = CyberBlack.copy(alpha = 0.7f),
              border = androidx.compose.foundation.BorderStroke(1.dp, GoldNeon.copy(alpha = 0.6f))
            ) {
              Text(
                text = "JOURNAL CGC • RDC 🇨🇩",
                color = GoldNeon,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }

          // Customization icon button
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = CyberBlack.copy(alpha = 0.75f),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .padding(12.dp)
              .clickable { showCoverThemeDialog = true }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(Icons.Default.Palette, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(14.dp))
              Text("Changer thème", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        // Circular Avatar overlapping cover
        Box(
          modifier = Modifier
            .padding(start = 16.dp)
            .offset(y = 125.dp)
        ) {
          Box(
            modifier = Modifier
              .size(96.dp)
              .clip(CircleShape)
              .background(DarkSurface)
              .border(
                3.dp,
                if (profile.isCreator) GoldNeon else FacebookBlue,
                CircleShape
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = profile.secretName.take(1).uppercase(),
              color = if (profile.isCreator) GoldNeon else EmeraldGlow,
              fontSize = 38.sp,
              fontWeight = FontWeight.Black
            )

            // Online status indicator
            Box(
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
                .size(16.dp)
                .clip(CircleShape)
                .background(EmeraldNeon)
                .border(2.dp, CyberBlack, CircleShape)
            )
          }
        }
      }

      // Spacing for avatar overlap
      Spacer(modifier = Modifier.height(52.dp))
    }

    // ---------------------------------------------------------
    // 2. Profile Details & Gamer Motto
    // ---------------------------------------------------------
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        // Name & Badges (Pseudo de gamer, PAS de vrai nom)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = profile.secretName,
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black
          )
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Gamer Vérifié",
            tint = FacebookBlue,
            modifier = Modifier.size(20.dp)
          )

          if (profile.isCreator) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = GoldNeon.copy(alpha = 0.2f),
              border = androidx.compose.foundation.BorderStroke(1.dp, GoldNeon)
            ) {
              Text(
                text = "👑 FONDATEUR",
                color = GoldNeon,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          } else if (profile.role == UserRole.ADMIN) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = CyanNeon.copy(alpha = 0.2f),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon)
            ) {
              Text(
                text = "🛡️ ADMIN",
                color = CyanNeon,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        // Subtitle Title & Clan
        Text(
          text = "${profile.title} • Clan : ${profile.clan}",
          color = if (profile.isCreator) GoldNeon else CyanGlow,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Gamer Motto (Devise de gamer)
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = CyberCardHighlight,
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon.copy(alpha = 0.3f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.FormatQuote, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = profile.gamingMotto,
              color = EmeraldGlow,
              fontSize = 12.sp,
              fontStyle = FontStyle.Italic,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bio
        Text(
          text = profile.bio,
          color = TextSecondary,
          fontSize = 13.sp,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Gamer Meta Tags
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
          ) {
            Text(
              text = "🎮 ${profile.favoritePlatform}",
              color = TextPrimary,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
          ) {
            Text(
              text = "📍 ${profile.location}",
              color = TextSecondary,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
          ) {
            Text(
              text = "⭐ LVL ${profile.level}",
              color = GoldNeon,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // 1. Publier dans mon journal
          Button(
            onClick = { showWritePostDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = FacebookBlue, contentColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Écrire", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }

          // 2. Personnaliser journal
          Button(
            onClick = { showCustomizeJournalDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = CyberCard, contentColor = TextPrimary),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1.3f)
          ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = EmeraldNeon)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Personnaliser", fontSize = 12.sp)
          }

          // 3. Espace Administration ou Switcher
          if (profile.isCreator || profile.role == com.example.data.model.UserRole.ADMIN) {
            Button(
              onClick = onOpenAdminDashboard,
              colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = CyberBlack),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1.1f)
            ) {
              Text("Admin 🛡️", fontSize = 11.sp, fontWeight = FontWeight.Black)
            }
            Button(
              onClick = onOpenCreatorConsole,
              colors = ButtonDefaults.buttonColors(containerColor = GoldNeon, contentColor = CyberBlack),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1.1f)
            ) {
              Text("Console 👑", fontSize = 11.sp, fontWeight = FontWeight.Black)
            }
          } else {
            Button(
              onClick = onOpenAuth,
              colors = ButtonDefaults.buttonColors(containerColor = DarkSurface, contentColor = CyanNeon),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon.copy(alpha = 0.4f)),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("Compte", fontSize = 11.sp)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider(color = CyberCardBorder)
    }

    // ---------------------------------------------------------
    // 3. Navigation Tabs
    // ---------------------------------------------------------
    item {
      TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = CyberBlack,
        contentColor = EmeraldNeon,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
            color = if (selectedTabIndex == 3) EmeraldNeon else FacebookBlue
          )
        },
        divider = {}
      ) {
        tabs.forEachIndexed { index, title ->
          val isSelected = selectedTabIndex == index
          Tab(
            selected = isSelected,
            onClick = { selectedTabIndex = index },
            text = {
              Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) TextPrimary else TextMuted
              )
            }
          )
        }
      }
      HorizontalDivider(color = CyberCardBorder)
      Spacer(modifier = Modifier.height(10.dp))
    }

    // ---------------------------------------------------------
    // 4. Tab Content
    // ---------------------------------------------------------
    when (selectedTabIndex) {
      0 -> {
        // === TAB 0 : MON JOURNAL (Facebook Style Feed personnel) ===
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // Quick Post Box right in the journal
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = CyberCard),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(32.dp)
                      .clip(CircleShape)
                      .background(DarkSurface),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = profile.secretName.take(1),
                      color = EmeraldGlow,
                      fontWeight = FontWeight.Bold,
                      fontSize = 14.sp
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = "Quoi de neuf sur ton journal, ${profile.secretName} ?",
                    color = TextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier
                      .weight(1f)
                      .clickable { showWritePostDialog = true }
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                  onClick = { showWritePostDialog = true },
                  colors = ButtonDefaults.buttonColors(containerColor = FacebookBlue),
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Icon(Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Publier une actualité sur mon journal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            // User's journal publications
            val myPosts = announcements.filter {
              it.authorId == profile.id || it.authorName.equals(profile.secretName, ignoreCase = true)
            }

            if (myPosts.isEmpty()) {
              Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CyberCard),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
              ) {
                Column(
                  modifier = Modifier.padding(28.dp),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Icon(Icons.Default.PostAdd, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                  Text(
                    text = "Votre journal est encore vierge",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "Aucune publication pour le moment. Cliquez sur 'Écrire' ci-dessus pour partager votre premier post, un clip ou un défi gaming !",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                  )
                }
              }
            } else {
              myPosts.forEach { post ->
                Card(
                  modifier = Modifier.fillMaxWidth(),
                  colors = CardDefaults.cardColors(containerColor = CyberCard),
                  border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(post.authorName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                          color = CyanNeon.copy(alpha = 0.15f),
                          shape = RoundedCornerShape(4.dp)
                        ) {
                          Text(
                            text = post.tag,
                            color = CyanGlow,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                          )
                        }
                      }

                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(post.timestamp, color = TextMuted, fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                          onClick = { onDeletePost(post.id) },
                          modifier = Modifier.size(24.dp)
                        ) {
                          Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = PinkNeon, modifier = Modifier.size(16.dp))
                        }
                      }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(post.title, color = EmeraldGlow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(post.content, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                      Text("🔥 ${post.fireCount}", color = TextSecondary, fontSize = 11.sp)
                      Text("🎮 ${post.gamepadCount}", color = TextSecondary, fontSize = 11.sp)
                      Text("💬 ${post.comments.size} commentaires", color = TextSecondary, fontSize = 11.sp)
                      Text("↗️ ${post.shareCount} partages", color = TextSecondary, fontSize = 11.sp)
                    }
                  }
                }
              }
            }
          }
        }
      }

      1 -> {
        // === TAB 1 : À PROPOS ===
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = CyberCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
            shape = RoundedCornerShape(12.dp)
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Text("Informations du Profil Gamer", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
              HorizontalDivider(color = CyberCardBorder)

              AboutItem(label = "Pseudo de joueur (Public)", value = profile.secretName)
              AboutItem(label = "Devise personnelle", value = profile.gamingMotto)
              AboutItem(label = "Titre / Rang", value = profile.title)
              AboutItem(label = "Clan Esport", value = profile.clan)
              AboutItem(label = "Plateforme principale", value = profile.favoritePlatform)
              AboutItem(label = "Ville & Résidence", value = profile.location)
              AboutItem(label = "Date d'inscription", value = profile.memberSince)
              AboutItem(label = "Niveau de joueur", value = "Niveau ${profile.level} (${profile.xp} XP)")
              
              // Confidentialité UID : Strictement masqué sur le site, visible uniquement par admin/modo
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text("UID de Gamer (Confidentiel)", color = TextSecondary, fontSize = 12.sp)
                  Text("Non visible par le public", color = TextMuted, fontSize = 10.sp)
                }
                Text(
                  text = profile.getDisplayUid(profile.canManageContent),
                  color = if (profile.canManageContent) GoldNeon else TextMuted,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }

      2 -> {
        // === TAB 2 : CARTE GAMER & XP ===
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            GamerCardView(
              profile = profile,
              modifier = Modifier.fillMaxWidth()
            )

            // XP converter and Daily Lucky Wheel
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = CyberCard),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Text("Banque de Tokens & Récompenses", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Button(
                    onClick = { onConvertXp(100) },
                    enabled = profile.xp >= 100,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanNeon, contentColor = CyberBlack),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                  ) {
                    Text("100 XP -> 10 Tokens", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                  }

                  Button(
                    onClick = {
                      coroutineScope.launch {
                        isSpinning = true
                        spinRotation.animateTo(
                          targetValue = spinRotation.value + 1080f + (0..360).random(),
                          animationSpec = tween(1200)
                        )
                        val reward = onDailySpin()
                        lastSpinReward = reward
                        isSpinning = false
                      }
                    },
                    enabled = !isSpinning,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldNeon, contentColor = CyberBlack),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Casino,
                      contentDescription = null,
                      modifier = Modifier
                        .size(14.dp)
                        .rotate(spinRotation.value)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isSpinning) "Tirage..." else "Roue Chance", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                  }
                }

                if (lastSpinReward != null) {
                  Text(
                    "Félicitations ! +$lastSpinReward XP remportés !",
                    color = EmeraldNeon,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }

      3 -> {
        // === TAB 3 : SÉCURITÉ ZÉRO-FUITE (Protection Absolue) ===
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            // Anti-Leak Guard Header Banner
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = DarkSurface),
              border = androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldNeon),
              shape = RoundedCornerShape(14.dp)
            ) {
              Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(24.dp))
                  Column {
                    Text("BOUCLIER ANTI-FUITE SUPRÊME", color = EmeraldGlow, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    Text("Zéro fuite • Données et identifiants sécurisés", color = TextSecondary, fontSize = 10.sp)
                  }
                }
                Text(
                  "Sécurité garantie : Votre adresse email et votre mot de passe ne sont JAMAIS visibles publiquement sur le site pour éviter que d'autres personnes ne se connectent à votre compte.",
                  color = TextPrimary,
                  fontSize = 12.sp,
                  lineHeight = 16.sp
                )
              }
            }

            // Sensitive Info Safeguard Card
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = CyberCard),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
              ) {
                Text("État des Identifiants Privés", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                // Email display (Masked)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                  Text("Adresse E-mail privée", color = TextSecondary, fontSize = 11.sp)
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .background(DarkSurface, RoundedCornerShape(8.dp))
                      .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                      Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(16.dp))
                      Text(
                        text = profile.maskedEmail,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                    Surface(
                      shape = RoundedCornerShape(4.dp),
                      color = EmeraldNeon.copy(alpha = 0.2f)
                    ) {
                      Text(
                        "🔒 Masqué au public",
                        color = EmeraldNeon,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                }

                // Password status (NEVER displayed in plain text to prevent leaks)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                  Text("Mot de passe du compte", color = TextSecondary, fontSize = 11.sp)
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .background(DarkSurface, RoundedCornerShape(8.dp))
                      .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                      Icon(Icons.Default.Shield, contentDescription = null, tint = GoldNeon, modifier = Modifier.size(16.dp))
                      Text(
                        text = "••••••••••••••••",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                    Surface(
                      shape = RoundedCornerShape(4.dp),
                      color = GoldNeon.copy(alpha = 0.2f)
                    ) {
                      Text(
                        "Chiffré & Protégé",
                        color = GoldNeon,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                }

                // UID Status
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                  Text("UID de Gamer", color = TextSecondary, fontSize = 11.sp)
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .background(DarkSurface, RoundedCornerShape(8.dp))
                      .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                      Icon(Icons.Default.Lock, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(16.dp))
                      Text(
                        text = profile.getDisplayUid(profile.canManageContent),
                        color = if (profile.canManageContent) CyanGlow else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                      )
                    }
                    Surface(
                      shape = RoundedCornerShape(4.dp),
                      color = CyanNeon.copy(alpha = 0.15f)
                    ) {
                      Text(
                        "Visible Admin/Modo",
                        color = CyanNeon,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                }

                HorizontalDivider(color = CyberCardBorder)

                // Toggles
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text("Chiffrement des messages directs (E2EE)", color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 12.sp)
                    Text("Chiffre les communications privées de bout en bout", color = TextMuted, fontSize = 10.sp)
                  }
                  Switch(
                    checked = privacySettings.e2eeChatActive,
                    onCheckedChange = { onToggleE2EE() },
                    colors = SwitchDefaults.colors(checkedThumbColor = EmeraldNeon, checkedTrackColor = EmeraldGlow.copy(alpha = 0.3f))
                  )
                }

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text("Masquage anti-phishing renforcé", color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 12.sp)
                    Text("Bloque tout affichage de coordonnées sur le web", color = TextMuted, fontSize = 10.sp)
                  }
                  Switch(
                    checked = privacySettings.hideEmailFromPublic,
                    onCheckedChange = { onToggleHideEmail() },
                    colors = SwitchDefaults.colors(checkedThumbColor = EmeraldNeon, checkedTrackColor = EmeraldGlow.copy(alpha = 0.3f))
                  )
                }

                HorizontalDivider(color = CyberCardBorder)

                // Button to open complete User Freedom, Comfort & Safety Dashboard
                Button(
                  onClick = onOpenSafetyDashboard,
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("open_safety_dashboard_from_profile_button"),
                  colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = CyberBlack, modifier = Modifier.size(18.dp))
                    Text(
                      "Centre de Liberté, Confort & Sécurité Complet",
                      color = CyberBlack,
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }

  // ---------------------------------------------------------
  // Dialog: Personnaliser mon Journal (Devise, Bio, Clan, Ville, Plateforme)
  // ---------------------------------------------------------
  if (showCustomizeJournalDialog) {
    var editMotto by remember { mutableStateOf(profile.gamingMotto) }
    var editBio by remember { mutableStateOf(profile.bio) }
    var editClan by remember { mutableStateOf(profile.clan) }
    var editLocation by remember { mutableStateOf(profile.location) }
    var editPlatform by remember { mutableStateOf(profile.favoritePlatform) }
    var editTheme by remember { mutableStateOf(profile.coverTheme) }

    AlertDialog(
      onDismissRequest = { showCustomizeJournalDialog = false },
      containerColor = CyberCard,
      title = {
        Text("Personnaliser mon Journal CGC", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = editMotto,
            onValueChange = { editMotto = it },
            label = { Text("Devise de Gamer") },
            placeholder = { Text("Ex: Force & Honneur sur la CGC 🇨🇩", color = TextMuted) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editBio,
            onValueChange = { editBio = it },
            label = { Text("Bio personnelle du Journal") },
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editPlatform,
            onValueChange = { editPlatform = it },
            label = { Text("Plateforme favorite (PS5, PC, Xbox, Mobile)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editClan,
            onValueChange = { editClan = it },
            label = { Text("Clan Esport / Équipe") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = editLocation,
            onValueChange = { editLocation = it },
            label = { Text("Ville / Province") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onUpdateJournalCustomization(
              editBio,
              editMotto,
              editLocation,
              editTheme,
              editPlatform,
              editClan
            )
            onUpdateExtendedProfile(
              profile.secretName,
              profile.title,
              editClan,
              editBio,
              editLocation,
              editTheme
            )
            showCustomizeJournalDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = FacebookBlue)
        ) {
          Text("Sauvegarder mon journal", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showCustomizeJournalDialog = false }) {
          Text("Annuler", color = TextSecondary)
        }
      }
    )
  }

  // ---------------------------------------------------------
  // Dialog: Écrire une publication dans le Journal
  // ---------------------------------------------------------
  if (showWritePostDialog) {
    var postContent by remember { mutableStateOf("") }
    var postTag by remember { mutableStateOf("Journal") }

    AlertDialog(
      onDismissRequest = { showWritePostDialog = false },
      containerColor = CyberCard,
      title = {
        Text("Publier dans votre journal CGC", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Auteur : ${profile.secretName} (${profile.title})", color = TextSecondary, fontSize = 11.sp)

          // Tag selection
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            listOf("Journal", "Clip", "Victoire", "Défi 1v1", "CGC").forEach { tag ->
              val isSel = postTag == tag
              Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isSel) FacebookBlue else DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) FacebookBlue else CyberCardBorder),
                modifier = Modifier.clickable { postTag = tag }
              ) {
                Text(
                  text = tag,
                  color = if (isSel) Color.White else TextSecondary,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }

          OutlinedTextField(
            value = postContent,
            onValueChange = { postContent = it },
            label = { Text("Quoi de neuf ?") },
            placeholder = { Text("Partagez vos victoires, clips, entraînements ou défis...", color = TextMuted) },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (postContent.isNotBlank()) {
              onAddAnnouncement(
                "Journal de ${profile.secretName}",
                postContent.trim(),
                postTag
              )
              showWritePostDialog = false
            }
          },
          enabled = postContent.isNotBlank(),
          colors = ButtonDefaults.buttonColors(containerColor = FacebookBlue)
        ) {
          Text("Publier dans mon journal", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showWritePostDialog = false }) {
          Text("Annuler", color = TextSecondary)
        }
      }
    )
  }

  // ---------------------------------------------------------
  // Dialog: Cover Theme Selector
  // ---------------------------------------------------------
  if (showCoverThemeDialog) {
    val themes = listOf(
      "cyber_emerald" to "Vert Émeraude Congo 🇨🇩",
      "fire_leopard" to "Léopard Rouge Feu 🔥",
      "cyber_gold" to "Cyber Or Fondateur 👑",
      "blue_voltage" to "Bleu Cobalt Gaming ⚡",
      "dark_stealth" to "Dark Stealth Cyberpunk 🛡️"
    )

    AlertDialog(
      onDismissRequest = { showCoverThemeDialog = false },
      containerColor = CyberCard,
      title = { Text("Personnaliser la couverture du Journal", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          themes.forEach { (themeKey, themeLabel) ->
            val isSelected = profile.coverTheme == themeKey
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) DarkSurface else CyberCard,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) EmeraldNeon else CyberCardBorder
              ),
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onUpdateExtendedProfile(
                    profile.secretName,
                    profile.title,
                    profile.clan,
                    profile.bio,
                    profile.location,
                    themeKey
                  )
                  showCoverThemeDialog = false
                }
            ) {
              Text(
                text = themeLabel,
                color = if (isSelected) EmeraldGlow else TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(12.dp)
              )
            }
          }
        }
      },
      confirmButton = {},
      dismissButton = {
        TextButton(onClick = { showCoverThemeDialog = false }) {
          Text("Fermer", color = TextSecondary)
        }
      }
    )
  }
}

@Composable
private fun AboutItem(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, color = TextSecondary, fontSize = 12.sp, modifier = Modifier.weight(1f))
    Text(value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
  }
}
