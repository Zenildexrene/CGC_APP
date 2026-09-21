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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LeaderboardGamer
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Simple, elegant leaderboard UI component displaying the top players in the CGC community
 * to foster competitive spirit, recognition, and engagement.
 */
@Composable
fun TopPlayersLeaderboardCard(
  topGamers: List<LeaderboardGamer>,
  onViewFullLeaderboard: () -> Unit,
  modifier: Modifier = Modifier,
  maxDisplayCount: Int = 4
) {
  val topPlayers = topGamers.take(maxDisplayCount)
  val myEntry = topGamers.find { it.isMe }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("top_players_leaderboard_component"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // 1. Header: Title, Season Badge & Competitive Indicator
      Row(
        modifier = Modifier.fillMaxWidth(),
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
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.linearGradient(listOf(GoldNeon.copy(alpha = 0.3f), Color.Transparent))
              )
              .border(1.dp, GoldNeon.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.EmojiEvents,
              contentDescription = "Leaderboard",
              tint = GoldNeon,
              modifier = Modifier.size(20.dp)
            )
          }

          Column {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(
                text = "TOP JOUEURS CGC",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
              )
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = GoldNeon.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldNeon.copy(alpha = 0.4f))
              ) {
                Text(
                  text = "SAISON 4",
                  color = GoldNeon,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                )
              }
            }
            Text(
              text = "Les maîtres du jeu en République Démocratique du Congo",
              color = TextSecondary,
              fontSize = 10.sp
            )
          }
        }

        // Live contest indicator pill
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = EmeraldNeon.copy(alpha = 0.12f),
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon.copy(alpha = 0.3f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(EmeraldNeon)
            )
            Text(
              text = "Live",
              color = EmeraldGlow,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 2. List of Top Players
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        topPlayers.forEachIndexed { index, gamer ->
          TopPlayerRowItem(
            gamer = gamer,
            rank = index + 1
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider(color = CyberCardBorder.copy(alpha = 0.6f))
      Spacer(modifier = Modifier.height(12.dp))

      // 3. Competitive Engagement Footer & CTA
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(
            imageVector = Icons.Default.TrendingUp,
            contentDescription = null,
            tint = EmeraldNeon,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = if (myEntry != null && myEntry.rank > 3) {
              "Vous êtes #${myEntry.rank} • +50 XP par victoire pour grimper !"
            } else {
              "Participez aux tournois pour entrer dans le Top !"
            },
            color = TextSecondary,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        TextButton(
          onClick = onViewFullLeaderboard,
          modifier = Modifier.testTag("view_full_leaderboard_btn")
        ) {
          Text(
            text = "Voir tout",
            color = CyanGlow,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(2.dp))
          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = CyanGlow,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun TopPlayerRowItem(
  gamer: LeaderboardGamer,
  rank: Int
) {
  val (rankColor, rankBadgeBg, rankLabel) = when (rank) {
    1 -> Triple(GoldNeon, GoldNeon.copy(alpha = 0.2f), "🥇 1er")
    2 -> Triple(CyanNeon, CyanNeon.copy(alpha = 0.2f), "🥈 2e")
    3 -> Triple(Color(0xFFCD7F32), Color(0xFFCD7F32).copy(alpha = 0.2f), "🥉 3e")
    else -> Triple(TextSecondary, DarkSurface, "#$rank")
  }

  val isUser = gamer.isMe

  Surface(
    shape = RoundedCornerShape(10.dp),
    color = if (isUser) CyberCardHighlight else DarkSurface,
    border = androidx.compose.foundation.BorderStroke(
      width = if (isUser || rank == 1) 1.dp else 0.5.dp,
      color = if (isUser) EmeraldNeon else if (rank == 1) GoldNeon.copy(alpha = 0.5f) else CyberCardBorder
    ),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("top_player_item_$rank")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 9.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Left: Rank badge, Avatar initial & Gamer details
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.weight(1f)
      ) {
        // Rank pill
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = rankBadgeBg,
          border = androidx.compose.foundation.BorderStroke(0.5.dp, rankColor.copy(alpha = 0.4f)),
          modifier = Modifier.width(42.dp)
        ) {
          Text(
            text = rankLabel,
            color = rankColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(vertical = 3.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }

        // Avatar Initial
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(CyberBlack)
            .border(1.5.dp, rankColor, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = gamer.secretName.take(1).uppercase(),
            color = rankColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black
          )
        }

        // Names & Clan
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = gamer.secretName,
              color = TextPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            if (isUser) {
              Spacer(modifier = Modifier.width(4.dp))
              Surface(
                shape = RoundedCornerShape(3.dp),
                color = EmeraldNeon.copy(alpha = 0.2f)
              ) {
                Text(
                  text = "VOUS",
                  color = EmeraldGlow,
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Black,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
              }
            }
          }
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = gamer.clan,
              color = CyanGlow,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "•",
              color = TextMuted,
              fontSize = 10.sp
            )
            Text(
              text = "LVL ${gamer.level}",
              color = TextSecondary,
              fontSize = 10.sp
            )
          }
        }
      }

      // Right: Win rate & XP badge
      Column(
        horizontalAlignment = Alignment.End
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.FlashOn,
            contentDescription = null,
            tint = GoldNeon,
            modifier = Modifier.size(12.dp)
          )
          Text(
            text = "${gamer.xp} XP",
            color = GoldNeon,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black
          )
        }
        Text(
          text = "${gamer.tournamentsWon} trophées • ${gamer.winRate}",
          color = TextSecondary,
          fontSize = 10.sp
        )
      }
    }
  }
}
