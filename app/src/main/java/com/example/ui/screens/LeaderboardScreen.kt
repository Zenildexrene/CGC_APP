package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import com.example.data.model.LeaderboardGamer
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardHighlight
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldNeon
import com.example.ui.theme.PinkNeon
import com.example.ui.theme.PurpleNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LeaderboardScreen(
  leaderboard: List<LeaderboardGamer>,
  modifier: Modifier = Modifier
) {
  val topThree = leaderboard.take(3)
  val restOfGamers = leaderboard.drop(3)
  val myEntry = leaderboard.find { it.isMe }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .testTag("leaderboard_screen")
  ) {
    // Top Bar Header
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "CLASSEMENT CGC",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
          )
          Text(
            text = "Top Gamers de la République Démocratique du Congo",
            color = TextSecondary,
            fontSize = 11.sp
          )
        }

        Box(
          modifier = Modifier
            .background(GoldNeon.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .border(1.dp, GoldNeon.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "SAISON 4",
            color = GoldNeon,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black
          )
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(bottom = 16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Podium Section (Top 3)
      item {
        PodiumSection(topThree = topThree)
      }

      item {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "TABLEAU DES JOUEURS",
          color = TextSecondary,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }

      // Rest of gamers
      items(restOfGamers, key = { it.secretName }) { gamer ->
        LeaderboardRow(gamer = gamer)
      }
    }

    // Pinned My Rank Bar
    myEntry?.let { me ->
      Surface(
        color = CyberCardHighlight,
        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("my_rank_bar")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(EmeraldNeon),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "#${me.rank}",
                color = CyberBlack,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = me.secretName,
                  color = TextPrimary,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "(Vous)",
                  color = EmeraldGlow,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Text(
                text = "${me.clan} • LVL ${me.level}",
                color = TextSecondary,
                fontSize = 10.sp
              )
            }
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "${me.xp} XP",
              color = EmeraldNeon,
              fontSize = 13.sp,
              fontWeight = FontWeight.Black
            )
            Text(
              text = "${me.tournamentsWon} Trophées",
              color = GoldNeon,
              fontSize = 10.sp
            )
          }
        }
      }
    }
  }
}

@Composable
private fun PodiumSection(topThree: List<LeaderboardGamer>) {
  if (topThree.size < 3) return

  val first = topThree[0]
  val second = topThree[1]
  val third = topThree[2]

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("podium_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
      ) {
        // Second Place
        PodiumStep(
          gamer = second,
          rank = 2,
          color = CyanNeon,
          height = 100.dp
        )

        // First Place
        PodiumStep(
          gamer = first,
          rank = 1,
          color = GoldNeon,
          height = 130.dp
        )

        // Third Place
        PodiumStep(
          gamer = third,
          rank = 3,
          color = PurpleNeon,
          height = 85.dp
        )
      }
    }
  }
}

@Composable
private fun PodiumStep(
  gamer: LeaderboardGamer,
  rank: Int,
  color: Color,
  height: androidx.compose.ui.unit.Dp
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.width(90.dp)
  ) {
    // Medal Icon / Crown
    Icon(
      imageVector = if (rank == 1) Icons.Default.EmojiEvents else Icons.Default.MilitaryTech,
      contentDescription = null,
      tint = color,
      modifier = Modifier.size(if (rank == 1) 28.dp else 22.dp)
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
      text = gamer.secretName,
      color = TextPrimary,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      maxLines = 1
    )

    Text(
      text = "${gamer.xp} XP",
      color = color,
      fontSize = 10.sp,
      fontWeight = FontWeight.Black
    )

    Spacer(modifier = Modifier.height(6.dp))

    // Podium Block
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
        .background(
          Brush.verticalGradient(
            colors = listOf(color.copy(alpha = 0.35f), color.copy(alpha = 0.1f))
          )
        )
        .border(
          width = 1.dp,
          color = color.copy(alpha = 0.6f),
          shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
        ),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "#$rank",
        color = color,
        fontSize = 22.sp,
        fontWeight = FontWeight.Black
      )
    }
  }
}

@Composable
private fun LeaderboardRow(gamer: LeaderboardGamer) {
  val isMe = gamer.isMe

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("leaderboard_row_${gamer.rank}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isMe) CyberCardHighlight else CyberCard
    ),
    border = androidx.compose.foundation.BorderStroke(
      width = 1.dp,
      color = if (isMe) EmeraldNeon else CyberCardBorder
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = "#${gamer.rank}",
          color = TextSecondary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Black,
          modifier = Modifier.width(32.dp)
        )

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = gamer.secretName,
              color = TextPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
            if (isMe) {
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "(Vous)",
                color = EmeraldGlow,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
          Text(
            text = "${gamer.clan} • LVL ${gamer.level}",
            color = TextMuted,
            fontSize = 10.sp
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "${gamer.xp} XP",
          color = CyanNeon,
          fontSize = 12.sp,
          fontWeight = FontWeight.Black
        )
        Text(
          text = "Victoires: ${gamer.winRate}",
          color = TextMuted,
          fontSize = 10.sp
        )
      }
    }
  }
}
