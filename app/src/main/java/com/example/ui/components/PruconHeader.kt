package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GamerProfile
import com.example.data.model.UserRole
import com.example.data.repository.PruconRepository
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DiscordBlurple
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldNeon
import com.example.ui.theme.PinkNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PruconHeader(
  profile: GamerProfile,
  modifier: Modifier = Modifier,
  onProfileClick: () -> Unit = {},
  onCreatorConsoleClick: () -> Unit = {},
  onAdminDashboardClick: () -> Unit = {},
  onSafetyDashboardClick: () -> Unit = {},
  onNotificationsClick: () -> Unit = {},
  unreadNotificationsCount: Int = 0,
  onAuthClick: () -> Unit = {},
  onMenuClick: () -> Unit = {}
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseAlpha"
  )

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = CyberBlack,
    tonalElevation = 4.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      // Top status line: Core active, network ping, system label
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Pulse status
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .background(
              color = EmeraldNeon.copy(alpha = 0.12f),
              shape = RoundedCornerShape(12.dp)
            )
            .border(
              width = 1.dp,
              color = EmeraldNeon.copy(alpha = 0.3f),
              shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(EmeraldNeon.copy(alpha = pulseAlpha))
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "CGC RÉSEAU ACTIF",
            color = EmeraldGlow,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        // Account and Ping
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Auth / Switch button
          Surface(
            onClick = onAuthClick,
            shape = RoundedCornerShape(12.dp),
            color = CyberCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
            modifier = Modifier.testTag("header_auth_button")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = profile.secretName,
                color = if (profile.isCreator) GoldNeon else CyanGlow,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Ping indicator
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .background(
                color = CyanNeon.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
              )
              .border(
                width = 1.dp,
                color = CyanNeon.copy(alpha = 0.25f),
                shape = RoundedCornerShape(12.dp)
              )
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Wifi,
              contentDescription = "Ping",
              tint = CyanGlow,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "24MS",
              color = CyanGlow,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Main branding bar & Quick stats pills
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Logo & Tagline
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.testTag("app_branding_header")
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(
                brush = Brush.linearGradient(
                  colors = if (profile.isCreator) listOf(GoldNeon, EmeraldNeon) else listOf(EmeraldNeon, CyanNeon)
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (profile.isCreator) Icons.Default.Shield else Icons.Default.SportsEsports,
              contentDescription = "CGC Logo",
              tint = CyberBlack,
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = "CGC",
              color = TextPrimary,
              fontSize = 20.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 2.sp
            )
            Text(
              text = "COMMUNAUTÉ GAMING CONGOLAISE",
              color = EmeraldGlow,
              fontSize = 8.5.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.8.sp
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // If Creator or Admin, show direct Admin Control Center button
          if (profile.isCreator || profile.role == UserRole.ADMIN) {
            Surface(
              onClick = onAdminDashboardClick,
              shape = RoundedCornerShape(16.dp),
              color = CyanNeon.copy(alpha = 0.2f),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon),
              modifier = Modifier.testTag("admin_dashboard_header_button")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "🛡️ ADMIN",
                  color = CyanGlow,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Black
                )
              }
            }
            Spacer(modifier = Modifier.width(6.dp))

            Surface(
              onClick = onCreatorConsoleClick,
              shape = RoundedCornerShape(16.dp),
              color = GoldNeon.copy(alpha = 0.2f),
              border = androidx.compose.foundation.BorderStroke(1.dp, GoldNeon),
              modifier = Modifier.testTag("creator_console_header_button")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "👑 CONSOLE",
                  color = GoldNeon,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Black
                )
              }
            }
            Spacer(modifier = Modifier.width(6.dp))
          }

          // Quick profile chip (LVL + Tokens)
          Surface(
            onClick = onProfileClick,
            shape = RoundedCornerShape(20.dp),
            color = CyberCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
            modifier = Modifier.testTag("quick_profile_chip")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "LVL ${profile.level}",
                color = CyanNeon,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .width(1.dp)
                  .height(12.dp)
                  .background(CyberCardBorder)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Icon(
                imageVector = Icons.Default.ElectricBolt,
                contentDescription = "Jetons",
                tint = GoldNeon,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(2.dp))
              Text(
                text = "${profile.tokens}",
                color = GoldNeon,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Quick Notifications Bell button
          Surface(
            onClick = onNotificationsClick,
            shape = RoundedCornerShape(20.dp),
            color = if (unreadNotificationsCount > 0) DiscordBlurple.copy(alpha = 0.2f) else DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, if (unreadNotificationsCount > 0) DiscordBlurple else CyberCardBorder),
            modifier = Modifier.testTag("header_notifications_button")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = if (unreadNotificationsCount > 0) DiscordBlurple else TextSecondary,
                modifier = Modifier.size(15.dp)
              )
              if (unreadNotificationsCount > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                  modifier = Modifier
                    .clip(CircleShape)
                    .background(PinkNeon)
                    .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                  Text(
                    text = "$unreadNotificationsCount",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Quick Freedom, Comfort & Safety Center button
          Surface(
            onClick = onSafetyDashboardClick,
            shape = RoundedCornerShape(20.dp),
            color = EmeraldNeon.copy(alpha = 0.15f),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon),
            modifier = Modifier.testTag("header_safety_button")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "🛡️ SÉCURITÉ",
                color = EmeraldNeon,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black
              )
            }
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Menu Button (Settings & À Propos CGC)
          Surface(
            onClick = onMenuClick,
            shape = RoundedCornerShape(20.dp),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon.copy(alpha = 0.6f)),
            modifier = Modifier.testTag("header_menu_button")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu et Paramètres",
                tint = EmeraldGlow,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "MENU",
                color = EmeraldGlow,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // System Marquee ticker bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(6.dp))
          .background(Color(0xFF0F172A))
          .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "SYSTEM:",
            color = EmeraldNeon,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (profile.isCreator) {
              "Console Suprême Active • Propriétaire: ${PruconRepository.CREATOR_EMAIL} • Statut Protégé"
            } else {
              "CGC Core v2.4.0 Online • Communauté Gaming Congolaise • Gamer Card Sync: OK"
            },
            color = if (profile.isCreator) GoldNeon else TextMuted,
            fontSize = 9.sp,
            maxLines = 1,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}
