package com.example.data.repository

import com.example.data.model.AboutCgcMessage
import com.example.data.model.Announcement
import com.example.data.model.ChatMessage
import com.example.data.model.DiscordChannel
import com.example.data.model.GamerProfile
import com.example.data.model.GamingGroup
import com.example.data.model.LeaderboardGamer
import com.example.data.model.LiveStream
import com.example.data.model.PostComment
import com.example.data.model.PrivacySettings
import com.example.data.model.Tournament
import com.example.data.model.TournamentStatus
import com.example.data.model.UserRole
import com.example.data.model.UserStory
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
    uid = "CGC-ADMIN-01",
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
    clan = "CGC Core Authority",
    tournamentsWon = 0,
    matchesPlayed = 0,
    winRate = "100%",
    status = "En ligne",
    gamingMotto = "Fondateur de la Communauté Gaming Congolaise 🇨🇩",
    bio = "Créateur & Propriétaire officiel de la plateforme CGC 🇨🇩 • Bienvenue sur le réseau social des gamers congolais !",
    location = "Kinshasa, Gombe",
    coverTheme = "cyber_gold",
    favoritePlatform = "PlayStation 5",
    favoriteGames = listOf("EA SPORTS FC 25", "Warzone", "Tekken 8", "Valorant"),
    friendsCount = 0,
    isImmuneFromRemoval = true
  )

  // Virgin state: Only the project owner is registered initially
  private val _allUsers = MutableStateFlow<List<GamerProfile>>(listOf(creatorAccount))
  val allUsers: StateFlow<List<GamerProfile>> = _allUsers.asStateFlow()

  private val _profile = MutableStateFlow(creatorAccount)
  val profile: StateFlow<GamerProfile> = _profile.asStateFlow()

  // Privacy & Anti-Data-Leak settings
  private val _privacySettings = MutableStateFlow(PrivacySettings())
  val privacySettings: StateFlow<PrivacySettings> = _privacySettings.asStateFlow()

  // Stories (Facebook Style) - Empty initial state for a pristine launch
  private val _stories = MutableStateFlow<List<UserStory>>(emptyList())
  val stories: StateFlow<List<UserStory>> = _stories.asStateFlow()

  // Discord channels structure
  private val _discordChannels = MutableStateFlow(
    listOf(
      DiscordChannel("ch_annonces", "annonces-cgc", "ANNONCES & INFOS", "Canal officiel des annonces de la communauté CGC", isVoice = false),
      DiscordChannel("ch_regles", "règles-et-sécurité", "ANNONCES & INFOS", "Règlement communautaire, fair-play et protection zéro-fuite", isVoice = false),
      DiscordChannel("grp_1", "général-rdc", "SALONS TEXTUELS", "Discussion gaming libre entre tous les joueurs congolais", isVoice = false),
      DiscordChannel("ch_team", "recherche-de-team", "SALONS TEXTUELS", "Recrutement et création d'équipes pour les tournois", isVoice = false),
      DiscordChannel("ch_fc25", "fc25-tournois", "SALONS TEXTUELS", "Matchs amicaux, entraînements et tirages au sort FC 25", isVoice = false),
      DiscordChannel("ch_clips", "clips-et-fails", "SALONS TEXTUELS", "Partage de clips vidéo et d'actions spectaculaires", isVoice = false),
      DiscordChannel("ch_voice_kinshasa", "Kinshasa Lounge", "SALONS VOCAUX", "Salon vocal communautaire haute fidélité", isVoice = true),
      DiscordChannel("ch_voice_match", "Match Tournoi 1v1", "SALONS VOCAUX", "Canal vocal réservé aux phases de match", isVoice = true),
      DiscordChannel("ch_dm_support", "Support Direct Créateur", "MESSAGES DIRECTS", "Canal privé chiffré de bout en bout (E2EE)", isVoice = false, isEncrypted = true)
    )
  )
  val discordChannels: StateFlow<List<DiscordChannel>> = _discordChannels.asStateFlow()

  // Live Streams (TikTok Live & YouTube Live) - Empty initial state
  private val _liveStreams = MutableStateFlow<List<LiveStream>>(emptyList())
  val liveStreams: StateFlow<List<LiveStream>> = _liveStreams.asStateFlow()

  // About CGC Official Messages (Admin editable)
  private val _aboutCgcMessages = MutableStateFlow(
    listOf(
      AboutCgcMessage(
        id = "about_1",
        title = "Manifeste de la Communauté Gaming Congolaise (CGC)",
        content = "La CGC (COMMUNAUTÉ GAMING CONGOLAISE) est la plateforme centrale d'esport, de cohésion et de divertissement numérique en République Démocratique du Congo. Fondée à Kinshasa, elle rassemble tous les gamers passionnés (PlayStation, Xbox, PC et Mobile) autour de compétitions équitables, de récompenses concrètes et d'un esprit communautaire d'entraide.",
        authorName = "Zenil",
        authorRole = UserRole.CREATOR,
        timestamp = "Septembre 2026",
        isPinned = true
      ),
      AboutCgcMessage(
        id = "about_2",
        title = "Sécurité & Protection des Données Zéro-Fuite",
        content = "Notre engagement formel : aucune donnée personnelle (adresse e-mail privée, mot de passe secret, UID des joueurs) n'est jamais exposée publiquement. Les communications privées bénéficient d'un chiffrement de pointe et le noyau CGC protège rigoureusement l'intégrité de chaque compte membre.",
        authorName = "Zenil",
        authorRole = UserRole.CREATOR,
        timestamp = "Septembre 2026",
        isPinned = true
      )
    )
  )
  val aboutCgcMessages: StateFlow<List<AboutCgcMessage>> = _aboutCgcMessages.asStateFlow()

  // Pristine virgin state: No publications yet
  private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
  val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

  // Pristine tournaments: Empty initially
  private val _tournaments = MutableStateFlow<List<Tournament>>(emptyList())
  val tournaments: StateFlow<List<Tournament>> = _tournaments.asStateFlow()

  // Clean initial gaming group: Central official lobby
  private val _groups = MutableStateFlow(
    listOf(
      GamingGroup(
        id = "grp_1",
        name = "Lobby Officiel CGC",
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
          content = "Bienvenue à tous sur la Communauté Gaming Congolaise (CGC) !",
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

  fun login(identifier: String, code: String): Pair<Boolean, String> {
    val cleanId = identifier.trim()
    val cleanCode = code.trim()

    if (cleanId.isEmpty() || cleanCode.isEmpty()) {
      return false to "Veuillez saisir votre pseudo (ou UID) et votre mot de passe."
    }

    val user = _allUsers.value.find {
      (it.secretName.equals(cleanId, ignoreCase = true) ||
       it.uid.equals(cleanId, ignoreCase = true) ||
       it.email.equals(cleanId, ignoreCase = true)) &&
      it.secretCode == cleanCode
    }

    return if (user != null) {
      _profile.value = user
      updateLeaderboard()
      true to "Connexion réussie ! Bon retour sur votre journal, ${user.secretName}."
    } else {
      false to "Identifiants incorrects. Vérifiez votre pseudo et mot de passe."
    }
  }

  fun registerNewPlayer(pseudo: String, uid: String, secretCode: String): Pair<Boolean, String> {
    val cleanPseudo = pseudo.trim()
    val cleanUid = uid.trim().uppercase()
    val cleanCode = secretCode.trim()

    if (cleanPseudo.isEmpty() || cleanUid.isEmpty() || cleanCode.isEmpty()) {
      return false to "Le Pseudo, l'UID et le mot de passe sont obligatoires."
    }

    if (cleanPseudo.length < 3) {
      return false to "Le pseudo doit faire au moins 3 caractères."
    }

    if (cleanCode.length < 4) {
      return false to "Le mot de passe doit faire au moins 4 caractères."
    }

    if (_allUsers.value.any { it.secretName.equals(cleanPseudo, ignoreCase = true) }) {
      return false to "Ce pseudo de gamer est déjà utilisé. Choisissez-en un autre."
    }

    if (_allUsers.value.any { it.uid.equals(cleanUid, ignoreCase = true) }) {
      return false to "Cet UID de gamer est déjà enregistré. Veuillez en choisir un autre."
    }

    val newGamer = GamerProfile(
      id = "usr_${System.currentTimeMillis()}",
      uid = cleanUid,
      email = "${cleanPseudo.lowercase().replace(" ", "")}@cgc.cd",
      secretName = cleanPseudo,
      secretCode = cleanCode,
      role = UserRole.PLAYER,
      level = 1,
      xp = 100,
      nextLevelXp = 500,
      tokens = 20,
      title = "Gamer CGC",
      memberSince = "Septembre 2026",
      clan = "CGC Indépendant",
      tournamentsWon = 0,
      matchesPlayed = 0,
      winRate = "0%",
      status = "En ligne",
      gamingMotto = "Force & Honneur sur la CGC 🇨🇩",
      bio = "Gamer passionné CGC • Journal de $cleanPseudo 🇨🇩",
      location = "Kinshasa, RDC",
      coverTheme = "cyber_emerald",
      favoritePlatform = "PlayStation 5",
      favoriteGames = listOf("EA SPORTS FC 25", "Warzone", "Tekken 8"),
      friendsCount = 0,
      isImmuneFromRemoval = false
    )

    _allUsers.update { it + newGamer }
    _profile.value = newGamer
    updateLeaderboard()
    return true to "Compte créé avec succès ! Bienvenue sur ton journal, $cleanPseudo."
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
              UserRole.STAFF -> "Staff Technique CGC"
              UserRole.PLAYER -> "Joueur CGC"
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

  fun addAnnouncement(
    title: String,
    content: String,
    tag: String,
    isPinned: Boolean = false,
    isJournalPost: Boolean = false
  ) {
    val current = _profile.value
    val newAnnouncement = Announcement(
      id = "ann_${System.currentTimeMillis()}",
      authorId = current.id,
      authorName = current.secretName,
      authorRole = current.role,
      authorLevel = current.level,
      authorTitle = current.title,
      title = title.ifBlank { "Journal de ${current.secretName}" },
      content = content,
      tag = tag.ifBlank { "Journal" },
      timestamp = "À l'instant",
      fireCount = 0,
      isPinned = isPinned && current.canManageContent,
      isJournalPost = isJournalPost
    )
    _announcements.update { listOf(newAnnouncement) + it }
    awardXp(25)
  }

  fun addJournalPost(title: String = "", content: String, tag: String = "Journal"): Boolean {
    if (content.isBlank()) return false
    val current = _profile.value
    addAnnouncement(
      title = title.ifBlank { "Journal de ${current.secretName}" },
      content = content.trim(),
      tag = tag,
      isPinned = false,
      isJournalPost = true
    )
    return true
  }

  fun deleteMyPost(announcementId: String): Boolean {
    val current = _profile.value
    _announcements.update { list ->
      list.filterNot { it.id == announcementId && (it.authorId == current.id || it.authorName == current.secretName || current.canManageContent) }
    }
    return true
  }

  fun updateJournalCustomization(
    bio: String,
    gamingMotto: String,
    location: String,
    coverTheme: String,
    favoritePlatform: String,
    clan: String
  ) {
    val cur = _profile.value
    val updated = cur.copy(
      bio = bio.trim(),
      gamingMotto = gamingMotto.trim(),
      location = location.trim(),
      coverTheme = coverTheme,
      favoritePlatform = favoritePlatform,
      clan = clan.trim()
    )
    updateCurrentProfile(updated)
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
            "heart" -> {
              val active = !ann.userReactedHeart
              ann.copy(
                userReactedHeart = active,
                heartCount = ann.heartCount + (if (active) 1 else -1)
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

  fun addCommentToAnnouncement(announcementId: String, content: String): Boolean {
    if (content.isBlank()) return false
    val current = _profile.value
    val comment = PostComment(
      id = "comment_${System.currentTimeMillis()}",
      authorName = current.secretName,
      authorRole = current.role,
      content = content.trim(),
      timestamp = "À l'instant",
      likesCount = 0
    )
    _announcements.update { list ->
      list.map { ann ->
        if (ann.id == announcementId) {
          ann.copy(comments = ann.comments + comment)
        } else ann
      }
    }
    awardXp(10)
    return true
  }

  fun shareAnnouncement(announcementId: String) {
    _announcements.update { list ->
      list.map { ann ->
        if (ann.id == announcementId) {
          ann.copy(shareCount = ann.shareCount + 1)
        } else ann
      }
    }
    awardXp(15)
  }

  fun createStory(title: String, tag: String) {
    val current = _profile.value
    val newStory = UserStory(
      id = "story_${System.currentTimeMillis()}",
      authorName = current.secretName,
      title = title,
      tag = tag.ifBlank { "GAMING" },
      isCreator = current.isCreator,
      gradientStart = if (current.isCreator) 0xFFFFD700 else 0xFF00E676,
      gradientEnd = if (current.isCreator) 0xFFFF9100 else 0xFF00B0FF,
      timestamp = "À l'instant"
    )
    _stories.update { listOf(newStory) + it }
    awardXp(30)
  }

  // --- Discord Messaging & Reactions ---

  fun addChatMessageReaction(groupId: String, messageId: String, reaction: String) {
    _chatMessages.update { map ->
      val list = map[groupId] ?: emptyList()
      val updatedList = list.map { msg ->
        if (msg.id == messageId) {
          val currentCount = msg.reactions[reaction] ?: 0
          val updatedReactions = msg.reactions + (reaction to (currentCount + 1))
          msg.copy(reactions = updatedReactions)
        } else msg
      }
      map + (groupId to updatedList)
    }
  }

  // --- Privacy & Anti-Data-Leak Controls ---

  fun toggleHideEmail() {
    _privacySettings.update {
      it.copy(hideEmailFromPublic = !it.hideEmailFromPublic)
    }
  }

  fun toggleHideSecretCode() {
    _privacySettings.update {
      it.copy(hideSecretCode = !it.hideSecretCode)
    }
  }

  fun toggleE2EE() {
    _privacySettings.update {
      it.copy(e2eeChatActive = !it.e2eeChatActive)
    }
  }

  fun updateExtendedProfile(
    secretName: String,
    title: String,
    clan: String,
    bio: String,
    location: String,
    coverTheme: String
  ) {
    val cur = _profile.value
    val updated = cur.copy(
      secretName = secretName.trim(),
      title = title.trim(),
      clan = clan.trim(),
      bio = bio.trim(),
      location = location.trim(),
      coverTheme = coverTheme
    )
    updateCurrentProfile(updated)
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

  // --- Live Stream Management ---
  fun addLiveStream(title: String, platform: String, streamUrl: String, gameName: String) {
    val cur = _profile.value
    val newStream = LiveStream(
      id = "live_${System.currentTimeMillis()}",
      streamerName = cur.secretName,
      streamerRole = cur.role,
      title = title,
      platform = platform,
      streamUrl = streamUrl,
      gameName = gameName,
      viewerCount = 1,
      isLive = true,
      timestamp = "En direct",
      authorId = cur.id
    )
    _liveStreams.update { listOf(newStream) + it }

    // Also notify on the Facebook feed as an announcement
    addAnnouncement(
      title = "🔴 [LIVE STREAM] $title",
      content = "Je suis en direct sur $platform pour jouer à $gameName ! Rejoignez mon live ici : $streamUrl",
      tag = "Event"
    )
  }

  fun stopLiveStream(streamId: String) {
    _liveStreams.update { list -> list.filter { it.id != streamId } }
  }

  // --- About CGC Messages (Admin / Creator editable) ---
  fun addAboutCgcMessage(title: String, content: String): Boolean {
    val cur = _profile.value
    if (!cur.canManageContent) return false

    val newMessage = AboutCgcMessage(
      id = "about_${System.currentTimeMillis()}",
      title = title,
      content = content,
      authorName = cur.secretName,
      authorRole = cur.role,
      timestamp = "À l'instant",
      isPinned = true
    )
    _aboutCgcMessages.update { listOf(newMessage) + it }
    return true
  }

  fun deleteAboutCgcMessage(messageId: String): Boolean {
    val cur = _profile.value
    if (!cur.canManageContent) return false
    _aboutCgcMessages.update { list -> list.filter { it.id != messageId } }
    return true
  }

  // =========================================================================
  // ADVANCED ADMIN DASHBOARD MANAGEMENT
  // =========================================================================

  private val _managedUsers = MutableStateFlow<List<com.example.data.model.ManagedUser>>(
    listOf(
      com.example.data.model.ManagedUser(
        id = "usr_creator",
        username = "Zenil",
        email = CREATOR_EMAIL,
        role = "Super Admin",
        status = com.example.data.model.UserAccountStatus.ACTIVE,
        isVerified = true,
        registrationDate = "01/09/2026",
        lastLogin = "Aujourd'hui à 15:45",
        lastActivity = "À l'instant",
        warningsCount = 0,
        reportsReceivedCount = 0,
        reportsSentCount = 0,
        messagesCount = 184,
        roomsJoinedCount = 5,
        ipAddress = "105.102.14.22 (Kinshasa)",
        device = "MacBook Pro / Chrome • Kinshasa RDC"
      ),
      com.example.data.model.ManagedUser(
        id = "usr_kabila_gaming",
        username = "KinshasaSniper",
        email = "sniper.kin@gmail.com",
        role = "Modérateur",
        status = com.example.data.model.UserAccountStatus.ACTIVE,
        isVerified = true,
        registrationDate = "04/09/2026",
        lastLogin = "Aujourd'hui à 14:10",
        lastActivity = "Il y a 12 min",
        warningsCount = 0,
        reportsReceivedCount = 0,
        reportsSentCount = 4,
        messagesCount = 92,
        roomsJoinedCount = 3,
        ipAddress = "197.242.128.84 (Gombe)",
        device = "PlayStation 5 / App CGC"
      ),
      com.example.data.model.ManagedUser(
        id = "usr_goma_pro",
        username = "GomaStriker",
        email = "goma.warrior@yahoo.fr",
        role = "Joueur",
        status = com.example.data.model.UserAccountStatus.ACTIVE,
        isVerified = true,
        registrationDate = "10/09/2026",
        lastLogin = "Hier à 22:15",
        lastActivity = "Il y a 1h",
        warningsCount = 1,
        reportsReceivedCount = 1,
        reportsSentCount = 2,
        messagesCount = 64,
        roomsJoinedCount = 4,
        ipAddress = "41.243.19.102 (Goma)",
        device = "PC Windows / Discord Web"
      ),
      com.example.data.model.ManagedUser(
        id = "usr_spammer_bot",
        username = "FreeCoinsBot",
        email = "bot992@tempmail.org",
        role = "Joueur",
        status = com.example.data.model.UserAccountStatus.MUTED,
        isVerified = false,
        registrationDate = "18/09/2026",
        lastLogin = "Il y a 3h",
        lastActivity = "Il y a 3h",
        warningsCount = 3,
        reportsReceivedCount = 6,
        reportsSentCount = 0,
        messagesCount = 28,
        roomsJoinedCount = 2,
        ipAddress = "185.220.101.5 (Proxy)",
        device = "Inconnu / Headless Script"
      )
    )
  )
  val managedUsers: StateFlow<List<com.example.data.model.ManagedUser>> = _managedUsers.asStateFlow()

  private val _platformReports = MutableStateFlow<List<com.example.data.model.PlatformReport>>(
    listOf(
      com.example.data.model.PlatformReport(
        id = "rep_101",
        category = com.example.data.model.ReportCategory.SPAM,
        reportedTarget = "Message dans #général: 'Gagnez 500k crédits FC 25 gratuit sur bit.ly/...'",
        reportedUser = "FreeCoinsBot",
        reportingUser = "KinshasaSniper",
        reason = "Lien suspect d'arnaque et phishing détecté dans le salon général",
        priority = com.example.data.model.ReportPriority.HIGH,
        status = com.example.data.model.ReportStatus.PENDING,
        timestamp = "Il y a 15 min"
      ),
      com.example.data.model.PlatformReport(
        id = "rep_102",
        category = com.example.data.model.ReportCategory.HARASSMENT,
        reportedTarget = "Salon vocal Tournoi 1v1",
        reportedUser = "TrollKing243",
        reportingUser = "GomaStriker",
        reason = "Insultes répétées après une défaite en quart de finale",
        priority = com.example.data.model.ReportPriority.MEDIUM,
        status = com.example.data.model.ReportStatus.UNDER_REVIEW,
        timestamp = "Il y a 1h",
        assignedAdmin = "Zenil"
      )
    )
  )
  val platformReports: StateFlow<List<com.example.data.model.PlatformReport>> = _platformReports.asStateFlow()

  private val _managedRooms = MutableStateFlow<List<com.example.data.model.ManagedRoom>>(
    listOf(
      com.example.data.model.ManagedRoom(
        id = "room_gen",
        name = "lobby-general",
        category = "COMMUNAUTÉ",
        description = "Discussion générale de tous les gamers de la CGC en RDC",
        isLocked = false,
        isPrivate = false,
        memberCount = 142,
        maxMembers = 500,
        slowModeSeconds = 0,
        allowMedia = true,
        allowLinks = true
      ),
      com.example.data.model.ManagedRoom(
        id = "room_fc25",
        name = "fc25-competitions",
        category = "TOURNOIS",
        description = "Salon dédié aux compétitions EA SPORTS FC 25 et défis 1v1",
        isLocked = false,
        isPrivate = false,
        memberCount = 88,
        maxMembers = 250,
        slowModeSeconds = 5,
        allowMedia = true,
        allowLinks = false
      ),
      com.example.data.model.ManagedRoom(
        id = "room_vip",
        name = "capitaines-de-clans",
        category = "RESTREINT",
        description = "Salon réservé aux capitaines de clans et staffs certifiés CGC",
        isLocked = false,
        isPrivate = true,
        memberCount = 18,
        maxMembers = 50,
        slowModeSeconds = 0,
        allowMedia = true,
        allowLinks = true
      )
    )
  )
  val managedRooms: StateFlow<List<com.example.data.model.ManagedRoom>> = _managedRooms.asStateFlow()

  private val _auditLogs = MutableStateFlow<List<com.example.data.model.AuditLogEntry>>(
    listOf(
      com.example.data.model.AuditLogEntry(
        id = "log_01",
        adminName = "Zenil",
        adminRole = "Super Admin",
        action = "MODERATION_MUTE",
        target = "FreeCoinsBot",
        details = "Mise en sourdine automatique pour détection de message de spam",
        ipAddress = "105.102.14.22"
      ),
      com.example.data.model.AuditLogEntry(
        id = "log_02",
        adminName = "Zenil",
        adminRole = "Super Admin",
        action = "ROLE_ASSIGNMENT",
        target = "KinshasaSniper",
        details = "Nomination au rôle de Modérateur certifié",
        ipAddress = "105.102.14.22"
      ),
      com.example.data.model.AuditLogEntry(
        id = "log_03",
        adminName = "Système CGC",
        adminRole = "Automated Security Engine",
        action = "SECURITY_BACKUP",
        target = "PostgreSQL DB / Cluster",
        details = "Sauvegarde instantanée chiffrée avec succès (Taille : 48.2 MB)",
        ipAddress = "127.0.0.1"
      )
    )
  )
  val auditLogs: StateFlow<List<com.example.data.model.AuditLogEntry>> = _auditLogs.asStateFlow()

  private val _securityAlerts = MutableStateFlow<List<com.example.data.model.SecurityAlert>>(
    listOf(
      com.example.data.model.SecurityAlert(
        id = "sec_1",
        title = "Tentative de connexion inhabituelle bloquée",
        description = "IP 185.220.101.5 a tenté 5 connexions erronées sur l'UID admin. Blocage temporaire 1h.",
        severity = "WARNING"
      ),
      com.example.data.model.SecurityAlert(
        id = "sec_2",
        title = "Chiffrement et intégrité base de données : 100% Vérifié",
        description = "Audit d'intégrité automatique réussi. Aucun mot de passe exposé, aucune fuite d'UID.",
        severity = "INFO"
      )
    )
  )
  val securityAlerts: StateFlow<List<com.example.data.model.SecurityAlert>> = _securityAlerts.asStateFlow()

  private val _supportTickets = MutableStateFlow<List<com.example.data.model.SupportTicket>>(
    listOf(
      com.example.data.model.SupportTicket(
        id = "tck_201",
        subject = "Problème d'attribution des récompenses FC 25",
        userName = "GomaStriker",
        priority = "Moyenne",
        status = "Ouvert",
        timestamp = "Il y a 45 min",
        internalNote = "Vérifier le tableau du tournoi du 15 Septembre"
      ),
      com.example.data.model.SupportTicket(
        id = "tck_202",
        subject = "Demande de vérification de badge de clan esport",
        userName = "KinshasaSniper",
        priority = "Basse",
        status = "En cours",
        timestamp = "Hier à 18:20",
        internalNote = "Clan vérifié : Kinshasa Esports Federation"
      )
    )
  )
  val supportTickets: StateFlow<List<com.example.data.model.SupportTicket>> = _supportTickets.asStateFlow()

  private val _apiKeys = MutableStateFlow<List<com.example.data.model.ApiKeyEntry>>(
    listOf(
      com.example.data.model.ApiKeyEntry(
        id = "key_live_01",
        name = "CGC Discord Webhook Relay",
        prefix = "cgc_live_9f82...",
        permissions = "messages.read, webhooks.post",
        createdAt = "01/09/2026",
        lastUsed = "Il y a 4 min",
        isActive = true
      ),
      com.example.data.model.ApiKeyEntry(
        id = "key_live_02",
        name = "Tournaments Bracket Sync API",
        prefix = "cgc_live_a441...",
        permissions = "tournaments.manage",
        createdAt = "10/09/2026",
        lastUsed = "Hier à 21:00",
        isActive = true
      )
    )
  )
  val apiKeys: StateFlow<List<com.example.data.model.ApiKeyEntry>> = _apiKeys.asStateFlow()

  private val _systemMetrics = MutableStateFlow(com.example.data.model.SystemMetrics())
  val systemMetrics: StateFlow<com.example.data.model.SystemMetrics> = _systemMetrics.asStateFlow()

  // --- Admin Action Dispatchers ---

  fun updateUserStatus(userId: String, newStatus: com.example.data.model.UserAccountStatus, reason: String = ""): Boolean {
    val cur = _profile.value
    if (!cur.isCreator && cur.role != UserRole.ADMIN && cur.role != UserRole.MODERATOR) return false

    _managedUsers.update { list ->
      list.map { user ->
        if (user.id == userId) user.copy(status = newStatus) else user
      }
    }

    addAuditLog(
      action = "USER_STATUS_CHANGE ($newStatus)",
      target = userId,
      details = "Statut utilisateur modifié vers $newStatus. Motif : $reason"
    )
    return true
  }

  fun warnUser(userId: String, reason: String): Boolean {
    _managedUsers.update { list ->
      list.map { user ->
        if (user.id == userId) user.copy(warningsCount = user.warningsCount + 1) else user
      }
    }
    addAuditLog("USER_WARN", userId, "Avertissement adressé. Motif : $reason")
    return true
  }

  fun resolveReport(reportId: String, resolution: com.example.data.model.ReportStatus, adminNote: String): Boolean {
    _platformReports.update { list ->
      list.map { rep ->
        if (rep.id == reportId) rep.copy(status = resolution, internalNote = adminNote, assignedAdmin = _profile.value.secretName)
        else rep
      }
    }
    addAuditLog("REPORT_RESOLUTION", reportId, "Signalement passé au statut : $resolution. Note : $adminNote")
    return true
  }

  fun toggleLockRoom(roomId: String): Boolean {
    _managedRooms.update { list ->
      list.map { room ->
        if (room.id == roomId) {
          val next = !room.isLocked
          addAuditLog(if (next) "ROOM_LOCKED" else "ROOM_UNLOCKED", room.name, "Verrouillage du salon")
          room.copy(isLocked = next)
        } else room
      }
    }
    return true
  }

  fun createManagedRoom(name: String, category: String, desc: String, isPrivate: Boolean): Boolean {
    val newRoom = com.example.data.model.ManagedRoom(
      id = "room_${System.currentTimeMillis()}",
      name = name.lowercase().replace(" ", "-"),
      category = category.uppercase(),
      description = desc,
      isPrivate = isPrivate
    )
    _managedRooms.update { listOf(newRoom) + it }
    addAuditLog("ROOM_CREATED", newRoom.name, "Création d'un nouveau salon administré")
    return true
  }

  fun updateTicketStatus(ticketId: String, newStatus: String, note: String = ""): Boolean {
    _supportTickets.update { list ->
      list.map { t ->
        if (t.id == ticketId) t.copy(status = newStatus, internalNote = note.ifBlank { t.internalNote })
        else t
      }
    }
    addAuditLog("TICKET_UPDATED", ticketId, "Ticket d'assistance passé à : $newStatus")
    return true
  }

  fun triggerManualBackup(): String {
    val backupId = "CGC_BACKUP_${System.currentTimeMillis()}"
    addAuditLog("DATABASE_BACKUP", backupId, "Sauvegarde complète manuelle déclenchée par l'administrateur (Taille estimée : 52.4 MB)")
    return backupId
  }

  // =========================================================================
  // USER FREEDOM, COMFORT AND SAFETY SYSTEM
  // =========================================================================

  private val _userPrivacySettings = MutableStateFlow(com.example.data.model.UserPrivacyCenterSettings())
  val userPrivacySettings: StateFlow<com.example.data.model.UserPrivacyCenterSettings> = _userPrivacySettings.asStateFlow()

  private val _userCommunicationControls = MutableStateFlow(com.example.data.model.UserCommunicationControls())
  val userCommunicationControls: StateFlow<com.example.data.model.UserCommunicationControls> = _userCommunicationControls.asStateFlow()

  private val _blockedUsers = MutableStateFlow<List<com.example.data.model.BlockedUserItem>>(
    listOf(
      com.example.data.model.BlockedUserItem(
        id = "usr_spammer_bot",
        username = "FreeCoinsBot",
        blockedAt = "Hier à 16:30",
        reason = "Messages de spam répétitifs et liens suspects"
      )
    )
  )
  val blockedUsers: StateFlow<List<com.example.data.model.BlockedUserItem>> = _blockedUsers.asStateFlow()

  private val _mutedUsers = MutableStateFlow<List<com.example.data.model.MutedUserItem>>(
    listOf(
      com.example.data.model.MutedUserItem(
        id = "usr_toxic_player",
        username = "TrollKing243",
        mutedAt = "Aujourd'hui à 11:15",
        duration = com.example.data.model.MuteDuration.ONE_DAY
      )
    )
  )
  val mutedUsers: StateFlow<List<com.example.data.model.MutedUserItem>> = _mutedUsers.asStateFlow()

  private val _submittedReports = MutableStateFlow<List<com.example.data.model.UserSubmittedReport>>(
    listOf(
      com.example.data.model.UserSubmittedReport(
        id = "rep_user_01",
        category = com.example.data.model.SafetyReportCategory.SPAM,
        targetType = "Message",
        targetIdentifier = "#général-rdc (FreeCoinsBot)",
        reason = "Lien suspect d'arnaque de crédits",
        evidenceNote = "Message contenant lien phishing bit.ly/fc25-free-coins",
        isAnonymous = true,
        status = "Résolu - Mesures prises",
        submittedAt = "Hier à 16:35",
        adminFeedback = "L'utilisateur a été mis en sourdine et les messages nettoyés. Merci pour votre vigilance !"
      )
    )
  )
  val submittedReports: StateFlow<List<com.example.data.model.UserSubmittedReport>> = _submittedReports.asStateFlow()

  private val _activeSessions = MutableStateFlow<List<com.example.data.model.UserActiveSession>>(
    listOf(
      com.example.data.model.UserActiveSession(
        id = "sess_current",
        device = "Android App CGC (Cet appareil)",
        browserOrApp = "Application Native Kotlin",
        approximateLocation = "Kinshasa, Gombe • RDC",
        ipAddress = "105.102.14.*** (Protégé)",
        lastActivity = "Actif à l'instant",
        isCurrent = true,
        loginDate = "Aujourd'hui à 14:20"
      ),
      com.example.data.model.UserActiveSession(
        id = "sess_mac",
        device = "MacBook Pro / Chrome Web",
        browserOrApp = "Chrome 128 / macOS",
        approximateLocation = "Kinshasa, Lingwala • RDC",
        ipAddress = "197.242.128.*** (Protégé)",
        lastActivity = "Il y a 3 heures",
        isCurrent = false,
        loginDate = "Hier à 20:15"
      ),
      com.example.data.model.UserActiveSession(
        id = "sess_ps5",
        device = "PlayStation 5 Console",
        browserOrApp = "PlayStation Network Client",
        approximateLocation = "Kinshasa, RDC",
        ipAddress = "41.243.19.*** (Protégé)",
        lastActivity = "Il y a 2 jours",
        isCurrent = false,
        loginDate = "18/09/2026"
      )
    )
  )
  val activeSessions: StateFlow<List<com.example.data.model.UserActiveSession>> = _activeSessions.asStateFlow()

  private val _emergencyShield = MutableStateFlow(com.example.data.model.EmergencySafetyShieldState())
  val emergencyShield: StateFlow<com.example.data.model.EmergencySafetyShieldState> = _emergencyShield.asStateFlow()

  private val _twoFactorEnabled = MutableStateFlow(true)
  val twoFactorEnabled: StateFlow<Boolean> = _twoFactorEnabled.asStateFlow()

  private val _userAppeals = MutableStateFlow<List<com.example.data.model.UserAppeal>>(emptyList())
  val userAppeals: StateFlow<List<com.example.data.model.UserAppeal>> = _userAppeals.asStateFlow()

  private val _personalizationPreferences = MutableStateFlow(com.example.data.model.PersonalizationPreferences())
  val personalizationPreferences: StateFlow<com.example.data.model.PersonalizationPreferences> = _personalizationPreferences.asStateFlow()

  fun updatePrivacySettings(newSettings: com.example.data.model.UserPrivacyCenterSettings) {
    _userPrivacySettings.value = newSettings
  }

  fun updateCommunicationControls(newControls: com.example.data.model.UserCommunicationControls) {
    _userCommunicationControls.value = newControls
  }

  fun blockUser(userId: String, username: String, reason: String = "Bloqué par l'utilisateur"): Boolean {
    if (_blockedUsers.value.any { it.id == userId }) return false
    val item = com.example.data.model.BlockedUserItem(
      id = userId,
      username = username,
      reason = reason
    )
    _blockedUsers.update { listOf(item) + it }
    return true
  }

  fun unblockUser(userId: String): Boolean {
    val before = _blockedUsers.value.size
    _blockedUsers.update { list -> list.filter { it.id != userId } }
    return _blockedUsers.value.size < before
  }

  fun muteUser(userId: String, username: String, duration: com.example.data.model.MuteDuration): Boolean {
    _mutedUsers.update { list ->
      list.filter { it.id != userId } + com.example.data.model.MutedUserItem(
        id = userId,
        username = username,
        duration = duration
      )
    }
    return true
  }

  fun unmuteUser(userId: String): Boolean {
    val before = _mutedUsers.value.size
    _mutedUsers.update { list -> list.filter { it.id != userId } }
    return _mutedUsers.value.size < before
  }

  fun submitUserReport(
    category: com.example.data.model.SafetyReportCategory,
    targetType: String,
    targetIdentifier: String,
    reason: String,
    evidenceNote: String = "",
    isAnonymous: Boolean = true
  ): String {
    val reportId = "rep_${System.currentTimeMillis().toString().takeLast(6)}"
    val report = com.example.data.model.UserSubmittedReport(
      id = reportId,
      category = category,
      targetType = targetType,
      targetIdentifier = targetIdentifier,
      reason = reason,
      evidenceNote = evidenceNote,
      isAnonymous = isAnonymous
    )
    _submittedReports.update { listOf(report) + it }

    // Also link into Platform Reports for Admins
    _platformReports.update { list ->
      listOf(
        com.example.data.model.PlatformReport(
          id = reportId,
          category = when (category) {
            com.example.data.model.SafetyReportCategory.SPAM -> com.example.data.model.ReportCategory.SPAM
            com.example.data.model.SafetyReportCategory.HARASSMENT -> com.example.data.model.ReportCategory.HARASSMENT
            com.example.data.model.SafetyReportCategory.SCAM -> com.example.data.model.ReportCategory.SCAM
            com.example.data.model.SafetyReportCategory.IMPERSONATION -> com.example.data.model.ReportCategory.IMPERSONATION
            com.example.data.model.SafetyReportCategory.THREAT -> com.example.data.model.ReportCategory.THREATS
            com.example.data.model.SafetyReportCategory.HATE_OR_ABUSE -> com.example.data.model.ReportCategory.HATE_SPEECH
            com.example.data.model.SafetyReportCategory.ILLEGAL_CONTENT -> com.example.data.model.ReportCategory.ILLEGAL_CONTENT
            else -> com.example.data.model.ReportCategory.OTHER
          },
          reportedTarget = "$targetType : $targetIdentifier",
          reportedUser = targetIdentifier,
          reportingUser = if (isAnonymous) "Utilisateur Anonyme Protégé" else _profile.value.secretName,
          reason = reason,
          priority = if (category == com.example.data.model.SafetyReportCategory.THREAT || category == com.example.data.model.SafetyReportCategory.ILLEGAL_CONTENT) {
            com.example.data.model.ReportPriority.CRITICAL
          } else {
            com.example.data.model.ReportPriority.MEDIUM
          }
        )
      ) + list
    }

    return reportId
  }

  fun toggleEmergencyShield(enable: Boolean) {
    _emergencyShield.update { current ->
      if (enable) {
        current.copy(
          isActive = true,
          activatedAt = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
        )
      } else {
        current.copy(isActive = false, activatedAt = null)
      }
    }
  }

  fun terminateSession(sessionId: String) {
    _activeSessions.update { list -> list.filter { it.id != sessionId } }
  }

  fun terminateAllOtherSessions() {
    _activeSessions.update { list -> list.filter { it.isCurrent } }
  }

  fun toggleTwoFactor(): Boolean {
    _twoFactorEnabled.update { !it }
    return _twoFactorEnabled.value
  }

  fun submitAppeal(sanctionType: String, explanation: String): String {
    val appealId = "apl_${System.currentTimeMillis().toString().takeLast(5)}"
    val appeal = com.example.data.model.UserAppeal(
      id = appealId,
      sanctionType = sanctionType,
      explanation = explanation
    )
    _userAppeals.update { listOf(appeal) + it }
    return appealId
  }

  fun exportUserDataJson(): String {
    val prof = _profile.value
    val priv = _userPrivacySettings.value
    val comm = _userCommunicationControls.value
    return """
    {
      "export_version": "1.0",
      "platform": "CGC - Communauté Gaming Congolaise",
      "export_date": "${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}",
      "user_profile": {
        "id": "${prof.id}",
        "username": "${prof.secretName}",
        "email": "${prof.email}",
        "member_since": "${prof.memberSince}",
        "role": "${prof.role.label}",
        "level": ${prof.level},
        "xp": ${prof.xp},
        "clan": "${prof.clan}",
        "platform": "${prof.favoritePlatform}"
      },
      "privacy_settings": {
        "profile_visibility": "${priv.profileVisibility.label}",
        "online_status": "${priv.onlineStatus.label}",
        "last_seen": "${priv.lastSeen.label}",
        "read_receipts": ${priv.readReceiptsEnabled},
        "typing_indicators": ${priv.typingIndicatorEnabled},
        "hide_email": ${priv.hideEmail},
        "hide_location": ${priv.hideExactLocation}
      },
      "communication_controls": {
        "who_can_message": "${comm.whoCanMessageMe.label}",
        "who_can_add_to_rooms": "${comm.whoCanAddMeToRooms.label}",
        "who_can_call": "${comm.whoCanCallMe.label}",
        "who_can_mention": "${comm.whoCanMentionMe.label}"
      },
      "blocked_users_count": ${_blockedUsers.value.size},
      "muted_users_count": ${_mutedUsers.value.size},
      "data_retention_policy": "Vos données vous appartiennent. Aucune revente commerciale, chiffrement E2EE garanti."
    }
    """.trimIndent()
  }

  fun addAuditLog(action: String, target: String, details: String) {
    val cur = _profile.value
    val newEntry = com.example.data.model.AuditLogEntry(
      id = "log_${System.currentTimeMillis()}",
      adminName = cur.secretName,
      adminRole = cur.role.label,
      action = action,
      target = target,
      details = details,
      ipAddress = "105.102.14.22"
    )
    _auditLogs.update { listOf(newEntry) + it }
  }
}
