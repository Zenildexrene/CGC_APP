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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Announcement
import com.example.data.model.GamerProfile
import com.example.data.model.GamingGroup
import com.example.data.model.Tournament
import com.example.data.model.UserRole
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
import com.example.ui.theme.PurpleNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CreatorConsoleDialog(
  currentProfile: GamerProfile,
  allUsers: List<GamerProfile>,
  tournaments: List<Tournament>,
  announcements: List<Announcement>,
  groups: List<GamingGroup>,
  onNominateRole: (userId: String, role: UserRole) -> Pair<Boolean, String>,
  onDeleteUser: (userId: String) -> Pair<Boolean, String>,
  onRegisterPlayer: (name: String, email: String, code: String) -> Pair<Boolean, String>,
  onCreateTournament: (title: String, game: String, platform: String, prize: String, fee: String, date: String, slots: Int, desc: String, rules: List<String>) -> Boolean,
  onDeleteTournament: (String) -> Boolean,
  onCreateAnnouncement: (title: String, content: String, tag: String, isPinned: Boolean) -> Unit,
  onDeleteAnnouncement: (String) -> Boolean,
  onCreateGroup: (name: String, category: String, desc: String) -> Boolean,
  onDeleteGroup: (String) -> Boolean,
  onDismiss: () -> Unit
) {
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  var feedbackMessage by remember { mutableStateOf<String?>(null) }

  // Sub-dialogs
  var showCreateUserDialog by remember { mutableStateOf(false) }
  var showCreateTournamentDialog by remember { mutableStateOf(false) }
  var showCreateAnnouncementDialog by remember { mutableStateOf(false) }
  var showCreateGroupDialog by remember { mutableStateOf(false) }

  val tabs = listOf("Membres & Rôles", "Tournois", "Annonces", "Salons")

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.92f)
        .testTag("creator_console_dialog"),
      color = CyberBlack,
      shape = RoundedCornerShape(20.dp),
      border = androidx.compose.foundation.BorderStroke(
        1.5.dp,
        Brush.linearGradient(listOf(GoldNeon, EmeraldNeon, CyanNeon))
      )
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = "Console",
              tint = GoldNeon,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "CONSOLE DU CRÉATEUR",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
              )
              Text(
                text = "Propriétaire: ${PruconRepository.CREATOR_EMAIL}",
                color = GoldNeon,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Fermer",
              tint = TextMuted
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Immutable Protection Banner
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(GoldNeon.copy(alpha = 0.12f), RoundedCornerShape(10.dp))
            .border(1.dp, GoldNeon.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(10.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = "Shield",
              tint = GoldNeon,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Protection Suprême : Vous êtes le créateur permanent du projet. Vous seul pouvez nommer des personnes et personne n'a le pouvoir de vous révoquer ou de vous retirer.",
              color = EmeraldGlow,
              fontSize = 11.sp,
              lineHeight = 15.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Feedback alert
        feedbackMessage?.let { msg ->
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(CyberCardHighlight, RoundedCornerShape(8.dp))
              .border(1.dp, CyanNeon, RoundedCornerShape(8.dp))
              .padding(8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = msg, color = TextPrimary, fontSize = 11.sp)
              IconButton(
                onClick = { feedbackMessage = null },
                modifier = Modifier.size(20.dp)
              ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = CyanNeon, modifier = Modifier.size(14.dp))
              }
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
        }

        // Tabs
        TabRow(
          selectedTabIndex = selectedTabIndex,
          containerColor = DarkSurface,
          contentColor = EmeraldNeon,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
              color = GoldNeon
            )
          }
        ) {
          tabs.forEachIndexed { index, title ->
            Tab(
              selected = selectedTabIndex == index,
              onClick = { selectedTabIndex = index },
              text = {
                Text(
                  text = title,
                  fontSize = 11.sp,
                  fontWeight = if (selectedTabIndex == index) FontWeight.Black else FontWeight.Medium,
                  color = if (selectedTabIndex == index) GoldNeon else TextMuted
                )
              }
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Content
        Box(modifier = Modifier.weight(1f)) {
          when (selectedTabIndex) {
            0 -> {
              // Users & Roles
              UsersManagementTab(
                allUsers = allUsers,
                onAddUser = { showCreateUserDialog = true },
                onNominate = { userId, role ->
                  val (ok, msg) = onNominateRole(userId, role)
                  feedbackMessage = msg
                },
                onDelete = { userId ->
                  val (ok, msg) = onDeleteUser(userId)
                  feedbackMessage = msg
                }
              )
            }
            1 -> {
              // Tournaments Management
              TournamentsManagementTab(
                tournaments = tournaments,
                onCreateTournament = { showCreateTournamentDialog = true },
                onDeleteTournament = {
                  onDeleteTournament(it)
                  feedbackMessage = "Tournoi supprimé avec succès."
                }
              )
            }
            2 -> {
              // Announcements Management
              AnnouncementsManagementTab(
                announcements = announcements,
                onCreateAnnouncement = { showCreateAnnouncementDialog = true },
                onDeleteAnnouncement = {
                  onDeleteAnnouncement(it)
                  feedbackMessage = "Annonce supprimée avec succès."
                }
              )
            }
            3 -> {
              // Groups Management
              GroupsManagementTab(
                groups = groups,
                onCreateGroup = { showCreateGroupDialog = true },
                onDeleteGroup = {
                  onDeleteGroup(it)
                  feedbackMessage = "Salon supprimé avec succès."
                }
              )
            }
          }
        }
      }
    }
  }

  // Dialog: Create User
  if (showCreateUserDialog) {
    CreateUserModal(
      onDismiss = { showCreateUserDialog = false },
      onCreate = { name, email, code ->
        val (ok, msg) = onRegisterPlayer(name, email, code)
        feedbackMessage = msg
        showCreateUserDialog = false
      }
    )
  }

  // Dialog: Create Tournament
  if (showCreateTournamentDialog) {
    CreateTournamentModal(
      onDismiss = { showCreateTournamentDialog = false },
      onCreate = { title, game, platform, prize, fee, date, slots, desc, rules ->
        onCreateTournament(title, game, platform, prize, fee, date, slots, desc, rules)
        feedbackMessage = "Nouveau tournoi '$title' créé avec succès !"
        showCreateTournamentDialog = false
      }
    )
  }

  // Dialog: Create Announcement
  if (showCreateAnnouncementDialog) {
    CreateAnnouncementModal(
      onDismiss = { showCreateAnnouncementDialog = false },
      onCreate = { title, content, tag, isPinned ->
        onCreateAnnouncement(title, content, tag, isPinned)
        feedbackMessage = "Annonce publiée avec succès !"
        showCreateAnnouncementDialog = false
      }
    )
  }

  // Dialog: Create Group
  if (showCreateGroupDialog) {
    CreateGroupModal(
      onDismiss = { showCreateGroupDialog = false },
      onCreate = { name, category, desc ->
        onCreateGroup(name, category, desc)
        feedbackMessage = "Salon '$name' créé avec succès !"
        showCreateGroupDialog = false
      }
    )
  }
}

@Composable
private fun UsersManagementTab(
  allUsers: List<GamerProfile>,
  onAddUser: () -> Unit,
  onNominate: (String, UserRole) -> Unit,
  onDelete: (String) -> Unit
) {
  Column(modifier = Modifier.fillMaxSize()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "MEMBRES INSCRITS (${allUsers.size})",
        color = TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )

      Button(
        onClick = onAddUser,
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Icon(Icons.Default.PersonAdd, contentDescription = null, tint = CyberBlack, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Inscrire un joueur", color = CyberBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(allUsers, key = { it.id }) { user ->
        UserManagementRow(
          user = user,
          onNominate = { role -> onNominate(user.id, role) },
          onDelete = { onDelete(user.id) }
        )
      }
    }
  }
}

@Composable
private fun UserManagementRow(
  user: GamerProfile,
  onNominate: (UserRole) -> Unit,
  onDelete: () -> Unit
) {
  var showRoleMenu by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (user.isCreator) CyberCardHighlight else CyberCard
    ),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (user.isCreator) GoldNeon else CyberCardBorder
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = user.secretName,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(6.dp))
          if (user.isCreator) {
            Box(
              modifier = Modifier
                .background(GoldNeon.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                .border(1.dp, GoldNeon, RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "👑 CRÉATEUR INTOUCHABLE",
                color = GoldNeon,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black
              )
            }
          } else {
            Box(
              modifier = Modifier
                .background(CyanNeon.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = user.role.label,
                color = CyanGlow,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Text(
          text = user.email,
          color = TextSecondary,
          fontSize = 10.sp
        )
        Text(
          text = "Code: ${user.secretCode} • LVL ${user.level} • ${user.xp} XP",
          color = TextMuted,
          fontSize = 9.sp
        )
      }

      // Action buttons
      if (user.isCreator) {
        // Creator is immune - show gold lock badge
        Box(
          modifier = Modifier
            .background(GoldNeon.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .border(1.dp, GoldNeon.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = "Non révocable",
              tint = GoldNeon,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Non révocable",
              color = GoldNeon,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
          // Nominate Role Button
          Box {
            Button(
              onClick = { showRoleMenu = true },
              colors = ButtonDefaults.buttonColors(containerColor = CyanNeon.copy(alpha = 0.2f)),
              shape = RoundedCornerShape(6.dp),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon.copy(alpha = 0.4f))
            ) {
              Text("Nommer", color = CyanNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            DropdownMenu(
              expanded = showRoleMenu,
              onDismissRequest = { showRoleMenu = false },
              modifier = Modifier.background(CyberCard)
            ) {
              DropdownMenuItem(
                text = { Text("🛡️ Administrateur", color = TextPrimary, fontSize = 11.sp) },
                onClick = {
                  onNominate(UserRole.ADMIN)
                  showRoleMenu = false
                }
              )
              DropdownMenuItem(
                text = { Text("⚔️ Modérateur", color = TextPrimary, fontSize = 11.sp) },
                onClick = {
                  onNominate(UserRole.MODERATOR)
                  showRoleMenu = false
                }
              )
              DropdownMenuItem(
                text = { Text("📋 Staff PRUCON", color = TextPrimary, fontSize = 11.sp) },
                onClick = {
                  onNominate(UserRole.STAFF)
                  showRoleMenu = false
                }
              )
              DropdownMenuItem(
                text = { Text("🎮 Joueur Simple", color = TextPrimary, fontSize = 11.sp) },
                onClick = {
                  onNominate(UserRole.PLAYER)
                  showRoleMenu = false
                }
              )
            }
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Delete user
          IconButton(
            onClick = onDelete,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Supprimer",
              tint = PinkNeon,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun TournamentsManagementTab(
  tournaments: List<Tournament>,
  onCreateTournament: () -> Unit,
  onDeleteTournament: (String) -> Unit
) {
  Column(modifier = Modifier.fillMaxSize()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "TOURNOIS (${tournaments.size})",
        color = TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )

      Button(
        onClick = onCreateTournament,
        colors = ButtonDefaults.buttonColors(containerColor = GoldNeon),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = CyberBlack, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Créer Tournoi", color = CyberBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    if (tournaments.isEmpty()) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Aucun tournoi enregistré.", color = TextMuted, fontSize = 12.sp)
      }
    } else {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(tournaments, key = { it.id }) { t ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = CyberCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(text = t.title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(text = "${t.gameTitle} • ${t.platform} • ${t.prizePool}", color = GoldNeon, fontSize = 10.sp)
                Text(text = "Slots: ${t.slotsFilled}/${t.maxSlots} • ${t.status.label}", color = TextMuted, fontSize = 9.sp)
              }

              IconButton(
                onClick = { onDeleteTournament(t.id) },
                modifier = Modifier.size(28.dp)
              ) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = PinkNeon, modifier = Modifier.size(16.dp))
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun AnnouncementsManagementTab(
  announcements: List<Announcement>,
  onCreateAnnouncement: () -> Unit,
  onDeleteAnnouncement: (String) -> Unit
) {
  Column(modifier = Modifier.fillMaxSize()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "ANNONCES (${announcements.size})",
        color = TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )

      Button(
        onClick = onCreateAnnouncement,
        colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = CyberBlack, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Publier", color = CyberBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(announcements, key = { it.id }) { ann ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(containerColor = CyberCard),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(text = ann.title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              Text(text = "Tag: ${ann.tag} • Par ${ann.authorName}", color = CyanGlow, fontSize = 10.sp)
              Text(
                text = ann.content.take(60) + if (ann.content.length > 60) "..." else "",
                color = TextSecondary,
                fontSize = 9.sp
              )
            }

            IconButton(
              onClick = { onDeleteAnnouncement(ann.id) },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = PinkNeon, modifier = Modifier.size(16.dp))
            }
          }
        }
      }
    }
  }
}

@Composable
private fun GroupsManagementTab(
  groups: List<GamingGroup>,
  onCreateGroup: () -> Unit,
  onDeleteGroup: (String) -> Unit
) {
  Column(modifier = Modifier.fillMaxSize()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "SALONS (${groups.size})",
        color = TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )

      Button(
        onClick = onCreateGroup,
        colors = ButtonDefaults.buttonColors(containerColor = PurpleNeon),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Créer Salon", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(groups, key = { it.id }) { g ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = CardDefaults.cardColors(containerColor = CyberCard),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(text = g.name, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              Text(text = "${g.gameCategory} • ${g.memberCount} membres", color = PurpleNeon, fontSize = 10.sp)
            }

            IconButton(
              onClick = { onDeleteGroup(g.id) },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = PinkNeon, modifier = Modifier.size(16.dp))
            }
          }
        }
      }
    }
  }
}

// --- Modals ---

@Composable
private fun CreateUserModal(
  onDismiss: () -> Unit,
  onCreate: (name: String, email: String, code: String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var code by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Inscrire un nouveau gamer", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Pseudo / Gamer Tag") },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedBorderColor = EmeraldNeon,
            unfocusedBorderColor = CyberCardBorder
          ),
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
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
          value = code,
          onValueChange = { code = it },
          label = { Text("Code d'accès secret") },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedBorderColor = EmeraldNeon,
            unfocusedBorderColor = CyberCardBorder
          ),
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onCreate(name, email, code) },
        enabled = name.isNotBlank() && email.isNotBlank() && code.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldNeon)
      ) {
        Text("Inscrire", color = CyberBlack, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Annuler", color = TextMuted)
      }
    },
    containerColor = CyberCard
  )
}

@Composable
private fun CreateTournamentModal(
  onDismiss: () -> Unit,
  onCreate: (title: String, game: String, platform: String, prize: String, fee: String, date: String, slots: Int, desc: String, rules: List<String>) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var game by remember { mutableStateOf("EA SPORTS FC 25") }
  var platform by remember { mutableStateOf("PS5 / Console") }
  var prize by remember { mutableStateOf("500 $ + 5 000 XP") }
  var fee by remember { mutableStateOf("Gratuit") }
  var date by remember { mutableStateOf("Samedi à 15:00") }
  var slotsText by remember { mutableStateOf("32") }
  var desc by remember { mutableStateOf("Tournoi officiel Prucon Gaming.") }
  var ruleText by remember { mutableStateOf("Fair-play strict, pas de triche, capture d'écran du score.") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Créer un tournoi officiel", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
    text = {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Titre du tournoi") },
            modifier = Modifier.fillMaxWidth()
          )
        }
        item {
          OutlinedTextField(
            value = game,
            onValueChange = { game = it },
            label = { Text("Jeu (ex: FC 25, Warzone, Tekken 8)") },
            modifier = Modifier.fillMaxWidth()
          )
        }
        item {
          OutlinedTextField(
            value = platform,
            onValueChange = { platform = it },
            label = { Text("Plateforme (PS5, PC, Mobile)") },
            modifier = Modifier.fillMaxWidth()
          )
        }
        item {
          OutlinedTextField(
            value = prize,
            onValueChange = { prize = it },
            label = { Text("Cash Prize / Récompense") },
            modifier = Modifier.fillMaxWidth()
          )
        }
        item {
          OutlinedTextField(
            value = fee,
            onValueChange = { fee = it },
            label = { Text("Frais d'inscription (ex: Gratuit)") },
            modifier = Modifier.fillMaxWidth()
          )
        }
        item {
          OutlinedTextField(
            value = slotsText,
            onValueChange = { slotsText = it },
            label = { Text("Nombre de places (Slots)") },
            modifier = Modifier.fillMaxWidth()
          )
        }
        item {
          OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date et Heure") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val slots = slotsText.toIntOrNull() ?: 16
          onCreate(
            title, game, platform, prize, fee, date, slots, desc,
            ruleText.split(",").map { it.trim() }
          )
        },
        enabled = title.isNotBlank() && game.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = GoldNeon)
      ) {
        Text("Créer Tournoi", color = CyberBlack, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Annuler", color = TextMuted) }
    },
    containerColor = CyberCard
  )
}

@Composable
private fun CreateAnnouncementModal(
  onDismiss: () -> Unit,
  onCreate: (title: String, content: String, tag: String, isPinned: Boolean) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var tag by remember { mutableStateOf("PRUCON") }
  var isPinned by remember { mutableStateOf(true) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Publier une annonce officielle", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Titre de l'annonce") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("Message / Contenu") },
          minLines = 3,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = tag,
          onValueChange = { tag = it },
          label = { Text("Tag (PRUCON, Tournoi, Event, Update)") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onCreate(title, content, tag, isPinned) },
        enabled = title.isNotBlank() && content.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = CyanNeon)
      ) {
        Text("Publier", color = CyberBlack, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Annuler", color = TextMuted) }
    },
    containerColor = CyberCard
  )
}

@Composable
private fun CreateGroupModal(
  onDismiss: () -> Unit,
  onCreate: (name: String, category: String, desc: String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Gaming Général") }
  var desc by remember { mutableStateOf("Salon officiel de discussion.") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Créer un salon de discussion", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Nom du salon") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = category,
          onValueChange = { category = it },
          label = { Text("Catégorie de jeu") },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = desc,
          onValueChange = { desc = it },
          label = { Text("Description") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onCreate(name, category, desc) },
        enabled = name.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = PurpleNeon)
      ) {
        Text("Créer", color = TextPrimary, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Annuler", color = TextMuted) }
    },
    containerColor = CyberCard
  )
}
