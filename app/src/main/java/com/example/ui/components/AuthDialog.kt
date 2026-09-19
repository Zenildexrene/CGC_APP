package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.GamerProfile
import com.example.data.repository.PruconRepository
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

@Composable
fun AuthDialog(
  currentProfile: GamerProfile,
  allUsers: List<GamerProfile>,
  onLogin: (email: String, code: String) -> Pair<Boolean, String>,
  onRegister: (name: String, email: String, code: String) -> Pair<Boolean, String>,
  onSwitchUser: (userId: String) -> Unit,
  onDismiss: () -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var emailInput by remember { mutableStateOf("") }
  var codeInput by remember { mutableStateOf("") }
  var registerName by remember { mutableStateOf("") }
  var registerEmail by remember { mutableStateOf("") }
  var registerCode by remember { mutableStateOf("") }
  var statusMessage by remember { mutableStateOf<String?>(null) }
  var isError by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("auth_dialog"),
      color = CyberBlack,
      shape = RoundedCornerShape(20.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "ESPACE JOUEURS & AUTHENTIFICATION",
              color = TextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 0.5.sp
            )
            Text(
              text = "Connectez-vous ou inscrivez votre Gamer Tag",
              color = TextSecondary,
              fontSize = 10.sp
            )
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = DarkSurface,
          contentColor = EmeraldNeon,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = EmeraldNeon
            )
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = { Text("Connexion", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = { Text("Inscription", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 2,
            onClick = { selectedTab = 2 },
            text = { Text("Changer (${allUsers.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Creator login shortcut
        if (selectedTab == 0) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(GoldNeon.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
              .border(1.dp, GoldNeon.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
              .clickable {
                emailInput = PruconRepository.CREATOR_EMAIL
                codeInput = PruconRepository.CREATOR_CODE
                val (ok, msg) = onLogin(PruconRepository.CREATOR_EMAIL, PruconRepository.CREATOR_CODE)
                statusMessage = msg
                isError = !ok
                if (ok) onDismiss()
              }
              .padding(10.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = GoldNeon, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = "Connexion Créateur (zenildelutu@gmail.com)",
                    color = GoldNeon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                  )
                  Text(
                    text = "Code: Firstgmg05 • Accès Suprême",
                    color = TextSecondary,
                    fontSize = 9.sp
                  )
                }
              }
              Text("Se connecter", color = EmeraldGlow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
        }

        // Status Feedback
        statusMessage?.let { msg ->
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(if (isError) PinkNeon.copy(alpha = 0.15f) else EmeraldNeon.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
              .border(1.dp, if (isError) PinkNeon else EmeraldNeon, RoundedCornerShape(8.dp))
              .padding(8.dp)
          ) {
            Text(text = msg, color = if (isError) PinkNeon else EmeraldGlow, fontSize = 11.sp)
          }
          Spacer(modifier = Modifier.height(10.dp))
        }

        when (selectedTab) {
          0 -> {
            // Login Form
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              OutlinedTextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                label = { Text("Adresse Email") },
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedBorderColor = EmeraldNeon,
                  unfocusedBorderColor = CyberCardBorder
                ),
                modifier = Modifier.fillMaxWidth()
              )

              OutlinedTextField(
                value = codeInput,
                onValueChange = { codeInput = it },
                label = { Text("Code d'accès secret") },
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedBorderColor = EmeraldNeon,
                  unfocusedBorderColor = CyberCardBorder
                ),
                modifier = Modifier.fillMaxWidth()
              )

              Button(
                onClick = {
                  val (ok, msg) = onLogin(emailInput, codeInput)
                  statusMessage = msg
                  isError = !ok
                  if (ok) onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon),
                shape = RoundedCornerShape(10.dp)
              ) {
                Text("Se connecter", color = CyberBlack, fontWeight = FontWeight.Black)
              }
            }
          }
          1 -> {
            // Registration Form
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              OutlinedTextField(
                value = registerName,
                onValueChange = { registerName = it },
                label = { Text("Pseudo / Gamer Tag") },
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedBorderColor = CyanNeon,
                  unfocusedBorderColor = CyberCardBorder
                ),
                modifier = Modifier.fillMaxWidth()
              )

              OutlinedTextField(
                value = registerEmail,
                onValueChange = { registerEmail = it },
                label = { Text("Adresse Email") },
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedBorderColor = CyanNeon,
                  unfocusedBorderColor = CyberCardBorder
                ),
                modifier = Modifier.fillMaxWidth()
              )

              OutlinedTextField(
                value = registerCode,
                onValueChange = { registerCode = it },
                label = { Text("Code d'accès secret") },
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedBorderColor = CyanNeon,
                  unfocusedBorderColor = CyberCardBorder
                ),
                modifier = Modifier.fillMaxWidth()
              )

              Button(
                onClick = {
                  val (ok, msg) = onRegister(registerName, registerEmail, registerCode)
                  statusMessage = msg
                  isError = !ok
                  if (ok) onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
                shape = RoundedCornerShape(10.dp)
              ) {
                Text("Créer mon compte", color = CyberBlack, fontWeight = FontWeight.Black)
              }
            }
          }
          2 -> {
            // Quick Switcher among registered users
            LazyColumn(
              verticalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.height(240.dp)
            ) {
              items(allUsers, key = { it.id }) { user ->
                val isActive = user.id == currentProfile.id
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                      onSwitchUser(user.id)
                      onDismiss()
                    },
                  shape = RoundedCornerShape(10.dp),
                  colors = CardDefaults.cardColors(
                    containerColor = if (isActive) CyberCardHighlight else CyberCard
                  ),
                  border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isActive) EmeraldNeon else CyberCardBorder
                  )
                ) {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                          text = user.secretName,
                          color = TextPrimary,
                          fontSize = 12.sp,
                          fontWeight = FontWeight.Bold
                        )
                        if (user.isCreator) {
                          Spacer(modifier = Modifier.width(4.dp))
                          Text("👑 Créateur", color = GoldNeon, fontSize = 9.sp, fontWeight = FontWeight.Black)
                        }
                      }
                      Text(text = user.email, color = TextSecondary, fontSize = 10.sp)
                    }

                    if (isActive) {
                      Text("Actif", color = EmeraldNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    } else {
                      Text("Basculer", color = CyanNeon, fontSize = 10.sp)
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
