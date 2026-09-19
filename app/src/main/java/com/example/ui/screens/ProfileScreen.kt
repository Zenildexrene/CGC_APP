package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GamerProfile
import com.example.ui.components.GamerCardView
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardHighlight
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldGlow
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldNeon
import com.example.ui.theme.PurpleNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
  profile: GamerProfile,
  onConvertXp: (Int) -> Boolean,
  onGiveXp: (Int) -> Boolean,
  onDailySpin: () -> Int,
  onUpdateProfile: (name: String, title: String, clan: String) -> Unit,
  onOpenCreatorConsole: () -> Unit = {},
  onOpenAuth: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var showEditProfileDialog by remember { mutableStateOf(false) }
  var showGiveXpDialog by remember { mutableStateOf(false) }
  var alertMessage by remember { mutableStateOf<String?>(null) }

  // Spin wheel state
  val coroutineScope = rememberCoroutineScope()
  val spinRotation = remember { Animatable(0f) }
  var isSpinning by remember { mutableStateOf(false) }
  var lastSpinReward by remember { mutableStateOf<Int?>(null) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .testTag("profile_screen_list"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Screen Title
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "CARTE GAMER PRUCON",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
          )
          Text(
            text = if (profile.isCreator) "Connecté en tant que Propriétaire Suprême" else "Identité esport, progression XP et portefeuille de jetons",
            color = if (profile.isCreator) GoldNeon else TextSecondary,
            fontSize = 11.sp
          )
        }

        Row {
          IconButton(
            onClick = onOpenAuth,
            modifier = Modifier.testTag("open_auth_button")
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = "Comptes",
              tint = CyanNeon
            )
          }

          IconButton(
            onClick = { showEditProfileDialog = true },
            modifier = Modifier.testTag("edit_profile_button")
          ) {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = "Modifier Profil",
              tint = EmeraldNeon
            )
          }
        }
      }
    }

    // Interactive Gamer Card
    item {
      GamerCardView(profile = profile)
    }

    // Creator & Project Management Card (Special dedicated section for the Owner)
    if (profile.isCreator) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("creator_management_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = CyberCardHighlight),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldNeon)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Shield,
                  contentDescription = null,
                  tint = GoldNeon,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "PANEL DU CRÉATEUR DU PROJET",
                  color = GoldNeon,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Black,
                  letterSpacing = 0.8.sp
                )
              }

              Box(
                modifier = Modifier
                  .background(GoldNeon.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                  .border(1.dp, GoldNeon, RoundedCornerShape(6.dp))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "SUPER ADMIN",
                  color = GoldNeon,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Black
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = "Propriétaire: zenildelutu@gmail.com\nCode d'accès: Firstgmg05",
              color = TextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "Statut : Indéboulonnable / Non révocable. Vous avez tous les pouvoirs sur le site et personne ne peut vous retirer.",
              color = EmeraldGlow,
              fontSize = 10.sp,
              lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
              onClick = onOpenCreatorConsole,
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.buttonColors(containerColor = GoldNeon, contentColor = CyberBlack),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Ouvrir la Console du Créateur (Nommer / Gérer)", fontWeight = FontWeight.Black, fontSize = 12.sp)
            }
          }
        }
      }
    }

    // Economy & XP Hub
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("economy_hub_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "ÉCONOMIE & GESTION DE L'XP",
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Convert XP to Tokens action
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(CyberBlack, RoundedCornerShape(12.dp))
              .border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp))
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Convertir 50 XP en 5 Jetons",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Les jetons permettent de s'inscrire aux tournois payants",
                color = TextMuted,
                fontSize = 10.sp
              )
            }

            Button(
              onClick = {
                val success = onConvertXp(50)
                if (success) {
                  alertMessage = "Succès : 50 XP convertis en 5 Jetons !"
                } else {
                  alertMessage = "Erreur : Vous avez besoin d'au moins 50 XP pour convertir."
                }
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = CyanNeon,
                contentColor = CyberBlack
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("convert_xp_button")
            ) {
              Icon(
                imageVector = Icons.Default.CurrencyExchange,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Convertir", fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Give XP Action
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(CyberBlack, RoundedCornerShape(12.dp))
              .border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp))
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Donner de l'XP à un ami",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Aidez vos coéquipiers de clan à monter en niveau",
                color = TextMuted,
                fontSize = 10.sp
              )
            }

            Button(
              onClick = { showGiveXpDialog = true },
              colors = ButtonDefaults.buttonColors(
                containerColor = PurpleNeon,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("give_xp_button")
            ) {
              Icon(
                imageVector = Icons.Default.Redeem,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Donner XP", fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
          }
        }
      }
    }

    // Daily Spin Wheel mini-game
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("daily_spin_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldNeon.copy(alpha = 0.4f))
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Casino,
                contentDescription = null,
                tint = GoldNeon,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "ROUE DE LA FORTUNE GAMER",
                color = GoldNeon,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
              )
            }

            Box(
              modifier = Modifier
                .background(GoldNeon.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "BONUS DU JOUR",
                color = GoldNeon,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Spinning Graphic Box
          Box(
            modifier = Modifier
              .size(110.dp)
              .clip(CircleShape)
              .background(
                Brush.sweepGradient(
                  listOf(EmeraldNeon, CyanNeon, PurpleNeon, GoldNeon, EmeraldNeon)
                )
              )
              .rotate(spinRotation.value),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(CyberBlack)
                .border(2.dp, GoldNeon, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                  imageVector = Icons.Default.ElectricBolt,
                  contentDescription = null,
                  tint = GoldNeon,
                  modifier = Modifier.size(24.dp)
                )
                Text(
                  text = "XP",
                  color = TextPrimary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Black
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          lastSpinReward?.let { reward ->
            Text(
              text = "Félicitations ! +$reward XP obtenus !",
              color = EmeraldGlow,
              fontSize = 13.sp,
              fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(6.dp))
          }

          Button(
            onClick = {
              if (!isSpinning) {
                isSpinning = true
                coroutineScope.launch {
                  val target = spinRotation.value + 1440f + (0..360).random()
                  spinRotation.animateTo(
                    targetValue = target,
                    animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
                  )
                  val reward = onDailySpin()
                  lastSpinReward = reward
                  isSpinning = false
                }
              }
            },
            enabled = !isSpinning,
            colors = ButtonDefaults.buttonColors(
              containerColor = GoldNeon,
              contentColor = CyberBlack
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("spin_wheel_button")
          ) {
            Text(
              text = if (isSpinning) "Tirage en cours..." else "Tourner la Roue (+XP)",
              fontWeight = FontWeight.Black
            )
          }
        }
      }
    }

    // Security & Network Status Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberBlack),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = EmeraldNeon,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "SÉCURITÉ & RÉSEAU PRUCON",
              color = EmeraldNeon,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "• Protocole de chiffrement TLS 1.3 activé\n• Anti-triche matériel vérifié\n• Serveur CDN : Kinshasa Gombe Node 01\n• Synchronisation Cloud : En temps réel",
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        }
      }
    }
  }

  // Edit Profile Dialog
  if (showEditProfileDialog) {
    var editName by remember { mutableStateOf(profile.secretName) }
    var editTitle by remember { mutableStateOf(profile.title) }
    var editClan by remember { mutableStateOf(profile.clan) }

    AlertDialog(
      onDismissRequest = { showEditProfileDialog = false },
      title = {
        Text("Personnaliser votre Profil", color = TextPrimary, fontWeight = FontWeight.Bold)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = editName,
            onValueChange = { editName = it },
            label = { Text("Nom Secret (Gamer Tag)", color = TextMuted) },
            singleLine = true,
            modifier = Modifier.testTag("edit_name_input")
          )
          OutlinedTextField(
            value = editTitle,
            onValueChange = { editTitle = it },
            label = { Text("Titre Gamer", color = TextMuted) },
            singleLine = true,
            modifier = Modifier.testTag("edit_title_input")
          )
          OutlinedTextField(
            value = editClan,
            onValueChange = { editClan = it },
            label = { Text("Nom du Clan", color = TextMuted) },
            singleLine = true,
            modifier = Modifier.testTag("edit_clan_input")
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (editName.isNotBlank()) {
              onUpdateProfile(editName.trim(), editTitle.trim(), editClan.trim())
              showEditProfileDialog = false
              alertMessage = "Profil mis à jour avec succès !"
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon, contentColor = CyberBlack)
        ) {
          Text("Enregistrer", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditProfileDialog = false }) {
          Text("Annuler", color = TextSecondary)
        }
      },
      containerColor = CyberCard,
      shape = RoundedCornerShape(16.dp)
    )
  }

  // Give XP Dialog
  if (showGiveXpDialog) {
    var targetFriend by remember { mutableStateOf("Kinshasa_Ghost") }
    var xpAmountText by remember { mutableStateOf("50") }

    AlertDialog(
      onDismissRequest = { showGiveXpDialog = false },
      title = {
        Text("Combien d'XP voulez-vous donner ?", color = TextPrimary, fontWeight = FontWeight.Bold)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "XP Disponible : ${profile.xp} XP",
            color = EmeraldGlow,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
          OutlinedTextField(
            value = targetFriend,
            onValueChange = { targetFriend = it },
            label = { Text("Nom du Joueur destinataire", color = TextMuted) },
            singleLine = true
          )
          OutlinedTextField(
            value = xpAmountText,
            onValueChange = { xpAmountText = it },
            label = { Text("Montant d'XP", color = TextMuted) },
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val amt = xpAmountText.toIntOrNull() ?: 50
            val ok = onGiveXp(amt)
            showGiveXpDialog = false
            if (ok) {
              alertMessage = "Vous avez donné $amt XP à $targetFriend !"
            } else {
              alertMessage = "Erreur : Solde d'XP insuffisant."
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = PurpleNeon, contentColor = Color.White)
        ) {
          Text("Confirmer le don", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showGiveXpDialog = false }) {
          Text("Annuler", color = TextSecondary)
        }
      },
      containerColor = CyberCard,
      shape = RoundedCornerShape(16.dp)
    )
  }

  // General Notification Alert
  alertMessage?.let { msg ->
    AlertDialog(
      onDismissRequest = { alertMessage = null },
      text = { Text(text = msg, color = TextPrimary, fontSize = 14.sp) },
      confirmButton = {
        Button(
          onClick = { alertMessage = null },
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon, contentColor = CyberBlack)
        ) {
          Text("OK", fontWeight = FontWeight.Bold)
        }
      },
      containerColor = CyberCard,
      shape = RoundedCornerShape(16.dp)
    )
  }
}
