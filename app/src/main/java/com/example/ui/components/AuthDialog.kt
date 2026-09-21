package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.GamerProfile
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
  onLogin: (identifier: String, code: String) -> Pair<Boolean, String>,
  onRegister: (pseudo: String, uid: String, code: String) -> Pair<Boolean, String>,
  onSwitchUser: (userId: String) -> Unit,
  onDismiss: () -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  
  // Login states
  var loginIdentifier by remember { mutableStateOf("") }
  var loginPassword by remember { mutableStateOf("") }
  var showLoginPassword by remember { mutableStateOf(false) }

  // Registration states (Pseudo, UID, Password - Pas de vrai nom !)
  var registerPseudo by remember { mutableStateOf("") }
  var registerUid by remember { mutableStateOf("") }
  var registerPassword by remember { mutableStateOf("") }
  var registerConfirmPassword by remember { mutableStateOf("") }
  var showRegisterPassword by remember { mutableStateOf(false) }

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
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = EmeraldGlow,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "ESPACE JOUEURS CGC",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
              )
            }
            Text(
              text = "Plateforme 100% sécurisée • Données protégées",
              color = TextSecondary,
              fontSize = 10.sp
            )
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Fermer", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Navigation Tabs
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
            onClick = {
              selectedTab = 0
              statusMessage = null
            },
            text = { Text("Connexion", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = {
              selectedTab = 1
              statusMessage = null
            },
            text = { Text("Inscription", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 2,
            onClick = {
              selectedTab = 2
              statusMessage = null
            },
            text = { Text("Comptes (${allUsers.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Status Feedback
        statusMessage?.let { msg ->
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                if (isError) PinkNeon.copy(alpha = 0.15f) else EmeraldNeon.copy(alpha = 0.15f),
                RoundedCornerShape(8.dp)
              )
              .border(
                1.dp,
                if (isError) PinkNeon else EmeraldNeon,
                RoundedCornerShape(8.dp)
              )
              .padding(10.dp)
          ) {
            Text(
              text = msg,
              color = if (isError) PinkNeon else EmeraldGlow,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
          Spacer(modifier = Modifier.height(12.dp))
        }

        when (selectedTab) {
          0 -> {
            // === CONNEXION SÉCURISÉE ===
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              OutlinedTextField(
                value = loginIdentifier,
                onValueChange = { loginIdentifier = it },
                label = { Text("Pseudo de Gamer ou UID") },
                placeholder = { Text("Ex: ShadowKin, Zenil ou CGC-00001", color = TextMuted) },
                leadingIcon = {
                  Icon(Icons.Default.Person, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(18.dp))
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedBorderColor = EmeraldNeon,
                  unfocusedBorderColor = CyberCardBorder
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("login_identifier_input")
              )

              OutlinedTextField(
                value = loginPassword,
                onValueChange = { loginPassword = it },
                label = { Text("Mot de passe") },
                singleLine = true,
                leadingIcon = {
                  Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldNeon, modifier = Modifier.size(18.dp))
                },
                trailingIcon = {
                  IconButton(onClick = { showLoginPassword = !showLoginPassword }) {
                    Icon(
                      imageVector = if (showLoginPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                      contentDescription = if (showLoginPassword) "Masquer" else "Afficher",
                      tint = TextMuted,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                },
                visualTransformation = if (showLoginPassword) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedBorderColor = EmeraldNeon,
                  unfocusedBorderColor = CyberCardBorder
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("login_password_input")
              )

              Button(
                onClick = {
                  val (ok, msg) = onLogin(loginIdentifier, loginPassword)
                  statusMessage = msg
                  isError = !ok
                  if (ok) onDismiss()
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("login_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon),
                shape = RoundedCornerShape(10.dp)
              ) {
                Text("Se connecter à mon Journal", color = CyberBlack, fontWeight = FontWeight.Black)
              }
            }
          }

          1 -> {
            // === INSCRIPTION GAMER (Pseudo + UID + Mot de passe) ===
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              // Confidentiality notice banner
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .background(CyanNeon.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                  .border(1.dp, CyanNeon.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                  .padding(8.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Shield, contentDescription = null, tint = CyanGlow, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Protection de la vie privée : Entrez votre Pseudo et votre UID de gamer (PAS de vrai nom). Votre UID ne sera jamais visible publiquement sur le site, seuls l'administrateur et les modérateurs y ont accès.",
                    color = TextSecondary,
                    fontSize = 9.sp,
                    lineHeight = 12.sp
                  )
                }
              }

              // 1. Pseudo de gamer
              OutlinedTextField(
                value = registerPseudo,
                onValueChange = { registerPseudo = it },
                label = { Text("Pseudo de Gamer (Public)") },
                placeholder = { Text("Ex: CongoSniper, Leopard_FC", color = TextMuted) },
                singleLine = true,
                leadingIcon = {
                  Icon(Icons.Default.Person, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(18.dp))
                },
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedBorderColor = CyanNeon,
                  unfocusedBorderColor = CyberCardBorder
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("register_pseudo_input")
              )

              // 2. UID de gamer (Confidentiel)
              OutlinedTextField(
                value = registerUid,
                onValueChange = { registerUid = it.uppercase() },
                label = { Text("UID de Gamer (Confidentiel)") },
                placeholder = { Text("Ex: CGC-94821 ou UID-7701", color = TextMuted) },
                singleLine = true,
                leadingIcon = {
                  Icon(Icons.Default.Badge, contentDescription = null, tint = GoldNeon, modifier = Modifier.size(18.dp))
                },
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedBorderColor = GoldNeon,
                  unfocusedBorderColor = CyberCardBorder
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("register_uid_input")
              )

              // 3. Mot de passe
              OutlinedTextField(
                value = registerPassword,
                onValueChange = { registerPassword = it },
                label = { Text("Mot de passe sécurisé") },
                singleLine = true,
                leadingIcon = {
                  Icon(Icons.Default.Lock, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(18.dp))
                },
                trailingIcon = {
                  IconButton(onClick = { showRegisterPassword = !showRegisterPassword }) {
                    Icon(
                      imageVector = if (showRegisterPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                      contentDescription = if (showRegisterPassword) "Masquer" else "Afficher",
                      tint = TextMuted,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                },
                visualTransformation = if (showRegisterPassword) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedBorderColor = CyanNeon,
                  unfocusedBorderColor = CyberCardBorder
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("register_password_input")
              )

              // 4. Confirmer mot de passe
              OutlinedTextField(
                value = registerConfirmPassword,
                onValueChange = { registerConfirmPassword = it },
                label = { Text("Confirmer le mot de passe") },
                singleLine = true,
                leadingIcon = {
                  Icon(Icons.Default.VpnKey, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(18.dp))
                },
                visualTransformation = if (showRegisterPassword) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = TextPrimary,
                  unfocusedTextColor = TextPrimary,
                  focusedBorderColor = CyanNeon,
                  unfocusedBorderColor = CyberCardBorder
                ),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("register_confirm_password_input")
              )

              Button(
                onClick = {
                  if (registerPassword != registerConfirmPassword) {
                    statusMessage = "Les mots de passe ne correspondent pas."
                    isError = true
                    return@Button
                  }
                  val (ok, msg) = onRegister(registerPseudo, registerUid, registerPassword)
                  statusMessage = msg
                  isError = !ok
                  if (ok) onDismiss()
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("register_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
                shape = RoundedCornerShape(10.dp)
              ) {
                Text("Créer mon Journal Gamer CGC", color = CyberBlack, fontWeight = FontWeight.Black)
              }
            }
          }

          2 -> {
            // === LISTE DES COMPTES (ZÉRO FUITE) ===
            Column {
              Text(
                text = "Sélectionnez un profil pour basculer :",
                color = TextSecondary,
                fontSize = 11.sp,
                modifier = Modifier.padding(bottom = 8.dp)
              )

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
                        .padding(12.dp),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                          Text(
                            text = user.secretName,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                          )
                          if (user.isCreator) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("👑 Créateur", color = GoldNeon, fontSize = 10.sp, fontWeight = FontWeight.Black)
                          } else if (user.role == com.example.data.model.UserRole.ADMIN) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("🛡️ Admin", color = CyanNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                          }
                        }
                        
                        Text(
                          text = user.title,
                          color = TextSecondary,
                          fontSize = 10.sp
                        )

                        // UID affiché SEULEMENT pour l'admin ou les modérateurs
                        Text(
                          text = "UID : ${user.getDisplayUid(currentProfile.canManageContent)}",
                          color = if (currentProfile.canManageContent) GoldNeon else TextMuted,
                          fontSize = 9.sp
                        )
                      }

                      if (isActive) {
                        Surface(
                          color = EmeraldNeon.copy(alpha = 0.2f),
                          shape = RoundedCornerShape(12.dp),
                          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldNeon)
                        ) {
                          Text(
                            "Actif",
                            color = EmeraldNeon,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                          )
                        }
                      } else {
                        Text("Basculer", color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
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
}
