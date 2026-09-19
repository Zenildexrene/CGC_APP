package com.example.data.repository

import com.example.data.model.Announcement
import com.example.data.model.ChatMessage
import com.example.data.model.GamerProfile
import com.example.data.model.GamingGroup
import com.example.data.model.LeaderboardGamer
import com.example.data.model.Tournament
import com.example.data.model.TournamentStatus
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class PruconRepository {

  companion object {
    const val CREATOR_EMAIL = "zenildelutu@gmail.com"
    const val CREATOR_CODE = "Firstgmg05"
  }

  // Initial pristine Creator / Owner Account
  private val creatorAccount = GamerProfile(
    id = "usr_zenil_creator",
    email = CREATOR_EMAIL,
    secretName = "Zenil",
    secretCode = CREATOR_CODE,
    role = UserRole.CREATOR,
    level = 50,
    xp = 2500,
    nextLevelXp = 10000,
    tokens = 500,
    title = "Créateur & Propriétaire Suprême",
    memberSince = "Septembre 2026",
    clan = "PRUCON Core Authority",
    tournamentsWon = 0,
    matchesPlayed = 0,
    winRate = "100%",
    status = "En ligne",
    isImmuneFromRemoval = true
  )

  // Virgin state: Only the project owner is registered initially
  private val _allUsers = MutableStateFlow<List<GamerProfile>>(listOf(creatorAccount))
  val allUsers: StateFlow<List<GamerProfile>> = _allUsers.asStateFlow()

  private val _profile = MutableStateFlow(creatorAccount)
  val profile: StateFlow<GamerProfile> = _profile.asStateFlow()

  // Clean state: 1 inaugural welcome post from the Creator
  private val _announcements = MutableStateFlow(
    listOf(
      Announcement(
        id = "ann_1",
        authorName = "Zenil",
        authorLevel = 50,
        authorTitle = "Créateur & Propriétaire",
        title = "Bienvenue sur Prucon Gaming RDC - Lancement Officiel",
        content = "L'application est officiellement déployée en direct. Le réseau est vierge et prêt à accueillir les gamers congolais. Tournois esport, salons de discussion et classements sont opérationnels !",
        tag = "PRUCON",
        timestamp = "À l'instant",
        fireCount = 1,
        gamepadCount = 0,
        trophyCount = 0,
        isPinned = true
      )
    )
  )
  val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

  // Inaugural tournament prepared for the launch
  private val _tournaments = MutableStateFlow(
    listOf(
      Tournament(
        id = "tour_1",
        title = "Tournoi Inaugural FC 25 - Kinshasa Open",
        gameTitle = "EA SPORTS FC 25",
        platform = "PS5 / Console",
        prizePool = "500 $ + 5 000 XP",
        entryFee = "Gratuit",
        startDate = "Inscriptions ouvertes",
        slotsFilled = 0,
        maxSlots = 32,
        status = TournamentStatus.OPEN,
        description = "Tournoi d'inauguration créé par la direction Prucon Gaming.",
        rules = listOf(
          "Matchs en mode Compétitif standard",
          "Fair-play strict et capture du score final obligatoire"
        )
      )
    )
  )
  val tournaments: StateFlow<List<Tournament>> = _tournaments.asStateFlow()

  // Clean initial gaming group: Central official lobby
  private val _groups = MutableStateFlow(
    listOf(
      GamingGroup(
        id = "grp_1",
        name = "Lobby Officiel PRUCON",
        gameCategory = "Hub Communautaire",
        description = "Salon officiel central créé par le propriétaire du site (zenildelutu@gmail.com).",
        memberCount = 1,
        isJoined = true,
        lastMessage = "Bienvenue dans le lobby officiel !",
        lastMessageTime = "Maintenant",
        unreadCount = 0
      )
    )
  )
  val groups: StateFlow<List<GamingGroup>> = _groups.asStateFlow()

  private val _chatMessages = MutableStateFlow<Map<String, List<ChatMessage>>>(
    mapOf(
      "grp_1" to listOf(
        ChatMessage(
          id = "msg_init",
          groupId = "grp_1",
          senderId = "usr_zenil_creator",
          senderName = "Zenil",
          senderLevel = 50,
          content = "Bienvenue à tous sur Prucon Gaming !",
          timestamp = "Maintenant",
          isMe = true
        )
      )
    )
  )
  val chatMessages: StateFlow<Map<String, List<ChatMessage>>> = _chatMessages.asStateFlow()

  private val _leaderboard = MutableStateFlow<List<LeaderboardGamer>>(emptyList())
  val leaderboard: StateFlow<List<LeaderboardGamer>> = _leaderboard.asStateFlow()

  init {
    updateLeaderboard()
  }

  private fun updateLeaderboard() {
    val currentUserId = _profile.value.id
    val sorted = _allUsers.value.sortedByDescending { it.xp }
    _leaderboard.value = sorted.mapIndexed { index, user ->
      LeaderboardGamer(
        rank = index + 1,
        secretName = user.secretName,
        title = user.title,
        level = user.level,
        xp = user.xp,
        tournamentsWon = user.tournamentsWon,
        winRate = user.winRate,
        clan = user.clan,
        isMe = user.id == currentUserId
      )
    }
  }

  // --- Authentication & User Management ---

  fun login(email: String, code: String): Pair<Boolean, String> {
    val user = _allUsers.value.find {
      it.email.equals(email.trim(), ignoreCase = true) && it.secretCode == code.trim()
    }
    return if (user != null) {
      _profile.value = user
      updateLeaderboard()
      true to "Connexion réussie en tant que ${user.secretName} (${user.role.label})"
    } else {
      false to "Identifiants invalides. Vérifiez l'adresse email et le code d'accès."
    }
  }

  fun registerNewPlayer(secretName: String, email: String, secretCode: String): Pair<Boolean, String> {
    val cleanEmail = email.trim()
    val cleanName = secretName.trim()
    val cleanCode = secretCode.trim()

    if (cleanEmail.isEmpty() || cleanName.isEmpty() || cleanCode.isEmpty()) {
      return false to "Tous les champs sont requis pour l'inscription."
    }

    if (_allUsers.value.any { it.email.equals(cleanEmail, ignoreCase = true) }) {
      return false to "Cet email est déjà enregistré."
    }

    val newGamer = GamerProfile(
      id = "usr_${System.currentTimeMillis()}",
      email = cleanEmail,
      secretName = cleanName,
      secretCode = cleanCode,
      role = UserRole.PLAYER,
      level = 1,
      xp = 100,
      nextLevelXp = 500,
      tokens = 20,
      title = "Recrue PRUCON",
      memberSince = "Septembre 2026",
      clan = "Indépendant",
      tournamentsWon = 0,
      matchesPlayed = 0,
      winRate = "0%",
      status = "En ligne",
      isImmuneFromRemoval = false
    )

    _allUsers.update { it + newGamer }
    _profile.value = newGamer
    updateLeaderboard()
    return true to "Compte créé avec succès ! Bienvenue, $cleanName."
  }

  fun switchUser(userId: String) {
    val target = _allUsers.value.find { it.id == userId }
    if (target != null) {
      _profile.value = target
      updateLeaderboard()
    }
  }

  // --- Creator & Role Nomination Powers ---

  fun nominateRole(targetUserId: String, newRole: UserRole): Pair<Boolean, String> {
    val currentUser = _profile.value
    if (!currentUser.isCreator) {
      return false to "Action refusée : Seul le Créateur du projet (zenildelutu@gmail.com) peut nommer d'autres personnes."
    }

    val target = _allUsers.value.find { it.id == targetUserId }
      ?: return false to "Utilisateur introuvable."

    // IMMUNITY ENFORCEMENT: Nobody can remove, demote, or modify the Creator!
    if (target.email.equals(CREATOR_EMAIL, ignoreCase = true) || target.isImmuneFromRemoval) {
      return false to "Action interdite : Le Créateur du projet ($CREATOR_EMAIL) est le propriétaire suprême et indéboulonnable. Personne n'a le pouvoir de le retirer ou modifier son rôle !"
    }

    _allUsers.update { list ->
      list.map { u ->
        if (u.id == targetUserId) {
          u.copy(
            role = newRole,
            title = when (newRole) {
              UserRole.ADMIN -> "Administrateur Officiel"
              UserRole.MODERATOR -> "Modérateur Communauté"
              UserRole.STAFF -> "Staff Technique PRUCON"
              UserRole.PLAYER -> "Joueur PRUCON"
              UserRole.CREATOR -> u.title
            }
          )
        } else u
      }
    }

    if (_profile.value.id == targetUserId) {
      _profile.value = _allUsers.value.first { it.id == targetUserId }
    }

    updateLeaderboard()
    return true to "${target.secretName} a été nommé au rôle de : ${newRole.label}."
  }

  fun deleteUser(targetUserId: String): Pair<Boolean, String> {
    val currentUser = _profile.value
    if (!currentUser.isCreator) {
      return false to "Action refusée : Seul le créateur peut supprimer un compte."
    }

    val target = _allUsers.value.find { it.id == targetUserId }
      ?: return false to "Utilisateur introuvable."

    // IMMUNITY ENFORCEMENT: Nobody can remove the Creator!
    if (target.email.equals(CREATOR_EMAIL, ignoreCase = true) || target.isImmuneFromRemoval) {
      return false to "Action strictement interdite : Personne n'a le pouvoir de retirer le créateur du projet ($CREATOR_EMAIL) !"
    }

    _allUsers.update { list -> list.filter { it.id != targetUserId } }
    updateLeaderboard()
    return true to "Le compte de ${target.secretName} a été retiré."
  }

  // --- Creator & Admin Content Management ---

  fun createTournament(
    title: String,
    gameTitle: String,
    platform: String,
    prizePool: String,
    entryFee: String,
    startDate: String,
    maxSlots: Int,
    description: String,
    rules: List<String>
  ): Boolean {
    val current = _profile.value
    if (!current.canManageContent) return false

    val newTourney = Tournament(
      id = "tour_${System.currentTimeMillis()}",
      title = title,
      gameTitle = gameTitle,
      platform = platform,
      prizePool = prizePool,
      entryFee = entryFee,
      startDate = startDate,
      slotsFilled = 0,
      maxSlots = maxSlots,
      status = TournamentStatus.OPEN,
      description = description,
      rules = rules
    )

    _tournaments.update { listOf(newTourney) + it }
    return true
  }

  fun deleteTournament(tournamentId: String): Boolean {
    val current = _profile.value
    if (!current.canManageContent) return false
    _tournaments.update { list -> list.filter { it.id != tournamentId } }
    return true
  }

  fun addAnnouncement(title: String, content: String, tag: String, isPinned: Boolean = false) {
    val current = _profile.value
    val newAnnouncement = Announcement(
      id = "ann_${System.currentTimeMillis()}",
      authorName = current.secretName,
      authorLevel = current.level,
      authorTitle = current.title,
      title = title,
      content = content,
      tag = tag,
      timestamp = "À l'instant",
      fireCount = 1,
      isPinned = isPinned && current.canManageContent
    )
    _announcements.update { listOf(newAnnouncement) + it }
    awardXp(25)
  }

  fun deleteAnnouncement(announcementId: String): Boolean {
    val current = _profile.value
    if (!current.canManageContent) return false
    _announcements.update { list -> list.filter { it.id != announcementId } }
    return true
  }

  fun createGroup(name: String, gameCategory: String, description: String): Boolean {
    val current = _profile.value
    if (!current.canManageContent) return false
    val newGroup = GamingGroup(
      id = "grp_${System.currentTimeMillis()}",
      name = name,
      gameCategory = gameCategory,
      description = description,
      memberCount = 1,
      isJoined = true,
      lastMessage = "Salon créé.",
      lastMessageTime = "Maintenant",
      unreadCount = 0
    )
    _groups.update { it + newGroup }
    return true
  }

  fun deleteGroup(groupId: String): Boolean {
    val current = _profile.value
    if (!current.canManageContent) return false
    _groups.update { list -> list.filter { it.id != groupId } }
    _chatMessages.update { map -> map - groupId }
    return true
  }

  // --- Normal Actions & Interactions ---

  fun toggleReaction(announcementId: String, reactionType: String) {
    _announcements.update { list ->
      list.map { ann ->
        if (ann.id == announcementId) {
          when (reactionType) {
            "fire" -> {
              val active = !ann.userReactedFire
              ann.copy(
                userReactedFire = active,
                fireCount = ann.fireCount + (if (active) 1 else -1)
              )
            }
            "gamepad" -> {
              val active = !ann.userReactedGamepad
              ann.copy(
                userReactedGamepad = active,
                gamepadCount = ann.gamepadCount + (if (active) 1 else -1)
              )
            }
            "trophy" -> {
              val active = !ann.userReactedTrophy
              ann.copy(
                userReactedTrophy = active,
                trophyCount = ann.trophyCount + (if (active) 1 else -1)
              )
            }
            else -> ann
          }
        } else {
          ann
        }
      }
    }
  }

  fun registerTournament(tournamentId: String): Boolean {
    var success = false
    _tournaments.update { list ->
      list.map { t ->
        if (t.id == tournamentId && !t.isRegistered && t.slotsFilled < t.maxSlots) {
          success = true
          t.copy(slotsFilled = t.slotsFilled + 1, isRegistered = true)
        } else {
          t
        }
      }
    }
    if (success) {
      awardXp(50)
    }
    return success
  }

  fun sendMessage(groupId: String, content: String) {
    val current = _profile.value
    val newMsg = ChatMessage(
      id = "msg_${System.currentTimeMillis()}",
      groupId = groupId,
      senderId = current.id,
      senderName = current.secretName,
      senderLevel = current.level,
      content = content,
      timestamp = "Maintenant",
      isMe = true
    )
    _chatMessages.update { map ->
      val currentList = map[groupId] ?: emptyList()
      map + (groupId to (currentList + newMsg))
    }
    awardXp(5)
  }

  fun rollDiceInChat(groupId: String): Int {
    val roll = Random.nextInt(1, 101)
    val current = _profile.value
    val diceMsg = ChatMessage(
      id = "msg_${System.currentTimeMillis()}",
      groupId = groupId,
      senderId = current.id,
      senderName = current.secretName,
      senderLevel = current.level,
      content = "🎲 a lancé un dé et a obtenu $roll !",
      timestamp = "Maintenant",
      isMe = true,
      isDiceRoll = true,
      diceValue = roll
    )
    _chatMessages.update { map ->
      val currentList = map[groupId] ?: emptyList()
      map + (groupId to (currentList + diceMsg))
    }
    return roll
  }

  fun convertXpToTokens(xpToConvert: Int = 50): Boolean {
    val cur = _profile.value
    if (cur.xp >= xpToConvert) {
      val tokensGained = (xpToConvert / 50) * 5
      val updated = cur.copy(
        xp = cur.xp - xpToConvert,
        tokens = cur.tokens + tokensGained
      )
      updateCurrentProfile(updated)
      return true
    }
    return false
  }

  fun giveXp(amount: Int): Boolean {
    val cur = _profile.value
    if (cur.xp >= amount) {
      updateCurrentProfile(cur.copy(xp = cur.xp - amount))
      return true
    }
    return false
  }

  fun playDailySpin(): Int {
    val wonXp = listOf(25, 50, 75, 100, 150, 200).random()
    awardXp(wonXp)
    return wonXp
  }

  fun joinGroup(groupId: String) {
    _groups.update { list ->
      list.map { g ->
        if (g.id == groupId) g.copy(isJoined = true, memberCount = g.memberCount + 1)
        else g
      }
    }
  }

  fun updateProfile(secretName: String, title: String, clan: String) {
    val cur = _profile.value
    val updated = cur.copy(secretName = secretName, title = title, clan = clan)
    updateCurrentProfile(updated)
  }

  private fun updateCurrentProfile(updated: GamerProfile) {
    _profile.value = updated
    _allUsers.update { list ->
      list.map { if (it.id == updated.id) updated else it }
    }
    updateLeaderboard()
  }

  private fun awardXp(amount: Int) {
    val current = _profile.value
    var newXp = current.xp + amount
    var newLevel = current.level
    var nextXp = current.nextLevelXp

    while (newXp >= nextXp) {
      newXp -= nextXp
      newLevel += 1
      nextXp = (nextXp * 1.2).toInt()
    }

    val updated = current.copy(xp = newXp, level = newLevel, nextLevelXp = nextXp)
    updateCurrentProfile(updated)
  }
}
