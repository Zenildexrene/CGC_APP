package com.example.ui.components

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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldNeon
import com.example.ui.theme.PurpleNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GamerCardView(
  profile: GamerProfile,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("gamer_card_view"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    border = androidx.compose.foundation.BorderStroke(
      width = 1.5.dp,
      brush = Brush.linearGradient(
        colors = listOf(EmeraldNeon, CyanNeon, PurpleNeon)
      )
    )
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = Brush.radialGradient(
            colors = listOf(
              EmeraldNeon.copy(alpha = 0.08f),
              CyberBlack.copy(alpha = 0.95f)
            ),
            radius = 600f
          )
        )
        .padding(18.dp)
    ) {
      Column {
        // Card top line: Brand logo, Gamer Card title, Secret Code chip
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = "Shield",
              tint = EmeraldNeon,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "CARTE GAMER CGC",
              color = EmeraldGlow,
              fontSize = 11.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 1.2.sp
            )
          }

          // Secret code badge
          Box(
            modifier = Modifier
              .background(CyberBlack, RoundedCornerShape(8.dp))
              .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = profile.secretCode,
              color = CyanNeon,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Role & Creator Banner
        if (profile.isCreator) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(GoldNeon.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
              .border(1.dp, GoldNeon.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
              .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "👑 PROPRIÉTAIRE & CRÉATEUR",
                color = GoldNeon,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.8.sp
              )
            }
            Text(
              text = "INVIOLABLE / NON RÉVOCABLE",
              color = EmeraldGlow,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
        } else {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(CyanNeon.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
              .border(1.dp, CyanNeon.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
              .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "RÔLE : ${profile.role.label.uppercase()}",
              color = CyanGlow,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
        }

        // Gamer Avatar, Secret Name, Title, and Clan
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Cyber Avatar Box
          Box(
            modifier = Modifier
              .size(62.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(
                Brush.linearGradient(
                  colors = if (profile.isCreator) {
                    listOf(GoldNeon.copy(alpha = 0.3f), EmeraldNeon.copy(alpha = 0.4f))
                  } else {
                    listOf(CyanNeon.copy(alpha = 0.2f), EmeraldNeon.copy(alpha = 0.3f))
                  }
                )
              )
              .border(
                2.dp,
                Brush.linearGradient(
                  colors = if (profile.isCreator) listOf(GoldNeon, EmeraldNeon) else listOf(EmeraldNeon, CyanNeon)
                ),
                RoundedCornerShape(16.dp)
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (profile.isCreator) Icons.Default.Shield else Icons.Default.SportsEsports,
              contentDescription = "Avatar",
              tint = if (profile.isCreator) GoldNeon else EmeraldGlow,
              modifier = Modifier.size(34.dp)
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = profile.secretName,
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
              )
              Spacer(modifier = Modifier.width(6.dp))
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = "Vérifié",
                tint = if (profile.isCreator) GoldNeon else CyanNeon,
                modifier = Modifier.size(16.dp)
              )
            }

            Text(
              text = profile.email,
              color = TextSecondary,
              fontSize = 10.sp,
              fontWeight = FontWeight.Normal
            )

            Text(
              text = profile.title,
              color = if (profile.isCreator) GoldNeon else CyanGlow,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )

            Text(
              text = "Clan: ${profile.clan}",
              color = TextSecondary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Level & XP Bar Section
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(CyberBlack.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
            .border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "EXPÉRIENCE CGC",
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
            }
            Text(
              text = "LVL ${profile.level}",
              color = EmeraldNeon,
              fontSize = 12.sp,
              fontWeight = FontWeight.Black
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          val progress = (profile.xp.toFloat() / profile.nextLevelXp.toFloat()).coerceIn(0f, 1f)
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(CircleShape),
            color = EmeraldNeon,
            trackColor = CyberCardBorder
          )

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "${profile.xp} / ${profile.nextLevelXp} XP",
              color = TextMuted,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.ElectricBolt,
                contentDescription = "Jetons",
                tint = GoldNeon,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${profile.tokens} Jetons",
                color = GoldNeon,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Stats Triad: Tournois Gagnés, Matchs Joués, Win Rate
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          GamerStatPill(
            label = "TOURNOIS",
            value = "${profile.tournamentsWon}",
            iconColor = GoldNeon,
            modifier = Modifier.weight(1f)
          )
          GamerStatPill(
            label = "MATCHS",
            value = "${profile.matchesPlayed}",
            iconColor = CyanNeon,
            modifier = Modifier.weight(1f)
          )
          GamerStatPill(
            label = "VICTOIRES",
            value = profile.winRate,
            iconColor = EmeraldNeon,
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Card footer: Member since
        Text(
          text = "Membre de la CGC depuis ${profile.memberSince}",
          color = TextMuted,
          fontSize = 9.5.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}

@Composable
private fun GamerStatPill(
  label: String,
  value: String,
  iconColor: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .background(CyberBlack.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
      .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
      .padding(vertical = 8.dp, horizontal = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = value,
        color = iconColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.Black
      )
      Text(
        text = label,
        color = TextSecondary,
        fontSize = 8.5.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp
      )
    }
  }
}
