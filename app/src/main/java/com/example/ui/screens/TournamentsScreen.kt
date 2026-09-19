package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Tournament
import com.example.data.model.TournamentStatus
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
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
fun TournamentsScreen(
  tournaments: List<Tournament>,
  onRegister: (String) -> Boolean,
  modifier: Modifier = Modifier
) {
  var selectedStatus by remember { mutableStateOf("Tous") }
  var registeredTournamentModal by remember { mutableStateOf<Tournament?>(null) }
  var showRulesDialog by remember { mutableStateOf(false) }

  val filteredTournaments = tournaments.filter { t ->
    when (selectedStatus) {
      "Ouverts" -> t.status == TournamentStatus.OPEN
      "En cours" -> t.status == TournamentStatus.LIVE
      "Terminés" -> t.status == TournamentStatus.COMPLETED
      else -> true
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .testTag("tournaments_screen_list"),
    contentPadding = PaddingValues(bottom = 24.dp)
  ) {
    // Hero Banner with esports arena
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_hero_banner),
          contentDescription = "Esports Arena",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Transparent,
                  CyberBlack.copy(alpha = 0.7f),
                  CyberBlack
                )
              )
            )
        )

        // Content overlay
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.Bottom
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .background(GoldNeon.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
              .border(1.dp, GoldNeon.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Icon(
              imageVector = Icons.Default.EmojiEvents,
              contentDescription = null,
              tint = GoldNeon,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "CIRCUITS ESPORT RDC 2024-2025",
              color = GoldNeon,
              fontSize = 9.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 1.sp
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "TOURNOIS & MATCHS OFFICIELS",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
          )

          Text(
            text = "Participez aux ligues congolaises, gagnez des cash prizes et montez au classement.",
            color = TextSecondary,
            fontSize = 11.sp
          )
        }
      }
    }

    // Filter Buttons & Rules Action Bar
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Status filters
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Tous", "Ouverts", "En cours", "Terminés").forEach { status ->
              val isSelected = selectedStatus == status
              Surface(
                onClick = { selectedStatus = status },
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) CyanNeon else CyberCard,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isSelected) CyanGlow else CyberCardBorder
                ),
                modifier = Modifier.testTag("tournament_filter_$status")
              ) {
                Text(
                  text = status,
                  color = if (isSelected) CyberBlack else TextSecondary,
                  fontSize = 10.5.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
              }
            }
          }

          // Rules Button
          Surface(
            onClick = { showRulesDialog = true },
            shape = RoundedCornerShape(16.dp),
            color = CyberCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, PurpleNeon.copy(alpha = 0.5f)),
            modifier = Modifier.testTag("tournament_rules_button")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Gavel,
                contentDescription = "Règles",
                tint = PurpleNeon,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "RÈGLES",
                color = PurpleNeon,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // Tournaments List
    items(filteredTournaments, key = { it.id }) { tournament ->
      TournamentCard(
        tournament = tournament,
        onRegister = {
          val success = onRegister(tournament.id)
          if (success) {
            registeredTournamentModal = tournament
          }
        },
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
      )
    }
  }

  // Registration Confirmation Dialog
  registeredTournamentModal?.let { tournament ->
    AlertDialog(
      onDismissRequest = { registeredTournamentModal = null },
      icon = {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = null,
          tint = EmeraldNeon,
          modifier = Modifier.size(48.dp)
        )
      },
      title = {
        Text(
          text = "Inscription Confirmée !",
          color = TextPrimary,
          fontWeight = FontWeight.Black,
          fontSize = 18.sp
        )
      },
      text = {
        Column {
          Text(
            text = "Votre place pour le tournoi ${tournament.title} est validée.",
            color = TextSecondary,
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(CyberBlack, RoundedCornerShape(10.dp))
              .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
              .padding(12.dp)
          ) {
            Column {
              Text(
                text = "Jeu: ${tournament.gameTitle}",
                color = CyanNeon,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Début: ${tournament.startDate}",
                color = TextPrimary,
                fontSize = 12.sp
              )
              Text(
                text = "Bonus d'inscription: +50 PRUCON XP accordés !",
                color = EmeraldGlow,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { registeredTournamentModal = null },
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon, contentColor = CyberBlack)
        ) {
          Text("Prêt pour la victoire", fontWeight = FontWeight.Black)
        }
      },
      containerColor = CyberCard,
      shape = RoundedCornerShape(18.dp)
    )
  }

  // Community Rules Dialog
  if (showRulesDialog) {
    AlertDialog(
      onDismissRequest = { showRulesDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Gavel,
            contentDescription = null,
            tint = PurpleNeon,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Règles & Fair-play PRUCON",
            color = TextPrimary,
            fontWeight = FontWeight.Black,
            fontSize = 16.sp
          )
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "1. Respect mutuel : Aucun comportement toxique ou insulte n'est toléré dans les salons de match.",
            color = TextSecondary,
            fontSize = 12.sp
          )
          Text(
            text = "2. Connexion stable : Les joueurs doivent s'assurer d'un ping décent (serveur Kinshasa/RDC).",
            color = TextSecondary,
            fontSize = 12.sp
          )
          Text(
            text = "3. Preuve de score : Une capture d'écran du tableau d'affichage final doit être envoyée en cas de litige.",
            color = TextSecondary,
            fontSize = 12.sp
          )
          Text(
            text = "4. Anti-Cheat : L'usage de scripts, glitchs non officiels ou triches entraîne le bannissement à vie du réseau.",
            color = TextSecondary,
            fontSize = 12.sp
          )
        }
      },
      confirmButton = {
        Button(
          onClick = { showRulesDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = PurpleNeon, contentColor = Color.White)
        ) {
          Text("Compris", fontWeight = FontWeight.Bold)
        }
      },
      containerColor = CyberCard,
      shape = RoundedCornerShape(18.dp)
    )
  }
}

@Composable
private fun TournamentCard(
  tournament: Tournament,
  onRegister: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("tournament_card_${tournament.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = CyberCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Top row: Game title chip, platform badge, status badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .background(CyanNeon.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
              .border(1.dp, CyanNeon.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = tournament.gameTitle,
              color = CyanGlow,
              fontSize = 10.sp,
              fontWeight = FontWeight.Black
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "• ${tournament.platform}",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
          )
        }

        // Status badge
        StatusBadge(status = tournament.status)
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Tournament Name
      Text(
        text = tournament.title,
        color = TextPrimary,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = tournament.description,
        color = TextSecondary,
        fontSize = 12.sp,
        lineHeight = 16.sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Prize Pool & Details Grid
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(CyberBlack, RoundedCornerShape(10.dp))
          .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
          .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "CASH PRIZE & XP",
            color = TextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
          )
          Text(
            text = tournament.prizePool,
            color = GoldNeon,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "ENTRÉE",
            color = TextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = tournament.entryFee,
            color = EmeraldNeon,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Slots indicator & Progress
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.People,
              contentDescription = null,
              tint = TextMuted,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Places: ${tournament.slotsFilled} / ${tournament.maxSlots}",
              color = TextSecondary,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Schedule,
              contentDescription = null,
              tint = TextMuted,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = tournament.startDate,
              color = TextMuted,
              fontSize = 11.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        val progress = (tournament.slotsFilled.toFloat() / tournament.maxSlots.toFloat()).coerceIn(0f, 1f)
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(CircleShape),
          color = if (progress >= 0.9f) GoldNeon else CyanNeon,
          trackColor = CyberBlack
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Action Button
      if (tournament.isRegistered) {
        Button(
          onClick = { },
          enabled = false,
          colors = ButtonDefaults.buttonColors(
            disabledContainerColor = EmeraldNeon.copy(alpha = 0.2f),
            disabledContentColor = EmeraldGlow
          ),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "INSCRIT AU TOURNOI",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      } else if (tournament.status == TournamentStatus.OPEN) {
        Button(
          onClick = onRegister,
          enabled = tournament.slotsFilled < tournament.maxSlots,
          colors = ButtonDefaults.buttonColors(
            containerColor = EmeraldNeon,
            contentColor = CyberBlack
          ),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("register_button_${tournament.id}")
        ) {
          Icon(
            imageVector = Icons.Default.MilitaryTech,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "S'INSCRIRE (+50 XP)",
            fontWeight = FontWeight.Black,
            fontSize = 12.sp,
            letterSpacing = 0.5.sp
          )
        }
      } else if (tournament.status == TournamentStatus.LIVE) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = PinkNeon.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, PinkNeon.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "MATCHS EN COURS • SUIVRE LE BRACKET",
            color = PinkNeon,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(vertical = 10.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun StatusBadge(status: TournamentStatus) {
  val (color, label) = when (status) {
    TournamentStatus.OPEN -> EmeraldNeon to "INSCRIPTIONS"
    TournamentStatus.LIVE -> PinkNeon to "EN DIRECT"
    TournamentStatus.COMPLETED -> TextMuted to "TERMINÉ"
  }

  Box(
    modifier = Modifier
      .background(color.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
      .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
      .padding(horizontal = 6.dp, vertical = 2.dp)
  ) {
    Text(
      text = label,
      color = color,
      fontSize = 9.sp,
      fontWeight = FontWeight.Black,
      letterSpacing = 0.5.sp
    )
  }
}
