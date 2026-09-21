package com.example.data.model

enum class TournamentStatus(val label: String) {
  OPEN("Inscriptions ouvertes"),
  LIVE("Tournoi en cours"),
  COMPLETED("Terminé")
}

enum class UserRole(val label: String, val levelPriority: Int) {
  CREATOR("Propriétaire & Créateur", 100),
  ADMIN("Administrateur", 80),
  MODERATOR("Modérateur", 60),
  STAFF("Staff CGC", 40),
  PLAYER("Joueur", 10)
}

data class GamerProfile(
  val id: String,
  val uid: String = "CGC-ADMIN-01",
  val email: String,
  val secretName: String,
  val secretCode: String,
  val role: UserRole = UserRole.PLAYER,
  val level: Int = 1,
  val xp: Int = 100,
  val nextLevelXp: Int = 500,
  val tokens: Int = 20,
  val title: String = "Gamer CGC",
  val memberSince: String = "Septembre 2026",
  val clan: String = "Indépendant",
  val tournamentsWon: Int = 0,
  val matchesPlayed: Int = 0,
  val winRate: String = "0%",
  val status: String = "En ligne",
  val gamingMotto: String = "Force & Honneur sur la CGC 🇨🇩",
  val bio: String = "Gamer passionné CGC • Esport & Fair-play • Kinshasa 🇨🇩",
  val location: String = "Kinshasa, RDC",
  val coverTheme: String = "cyber_emerald",
  val favoritePlatform: String = "PlayStation 5",
  val favoriteGames: List<String> = listOf("EA SPORTS FC 25", "Warzone", "Tekken 8"),
  val friendsCount: Int = 0,
  val isImmuneFromRemoval: Boolean = false
) {
  val isCreator: Boolean
    get() = role == UserRole.CREATOR || email.equals("zenildelutu@gmail.com", ignoreCase = true)

  val canManageContent: Boolean
    get() = isCreator || role == UserRole.ADMIN

  val canNominate: Boolean
    get() = isCreator

  /**
   * Anti-fuite : Masque l'email pour protéger les données personnelles des joueurs.
   * Exemple : zenildelutu@gmail.com -> zen***u@gmail.com
   */
  val maskedEmail: String
    get() {
      val atIndex = email.indexOf('@')
      if (atIndex <= 3) return "***@cgc.cd"
      val userPart = email.substring(0, atIndex)
      val domainPart = email.substring(atIndex)
      val firstThree = userPart.take(3)
      val lastOne = userPart.takeLast(1)
      return "$firstThree***$lastOne$domainPart"
    }

  /**
   * Anti-fuite : Code secret masqué par défaut pour éviter qu'une capture d'écran ne le dévoile.
   */
  val maskedSecretCode: String
    get() = "••••••••"

  /**
   * Confidentialité UID : Strictement masqué sur le site, visible uniquement par l'administrateur et les modérateurs.
   */
  fun getDisplayUid(isViewerAdminOrMod: Boolean): String {
    return if (isViewerAdminOrMod) uid else "•••••••• (Confidentiel Admin/Modo)"
  }
}

data class PostComment(
  val id: String,
  val authorName: String,
  val authorRole: UserRole,
  val content: String,
  val timestamp: String,
  val likesCount: Int = 0
)

data class UserStory(
  val id: String,
  val authorName: String,
  val title: String,
  val tag: String,
  val isCreator: Boolean = false,
  val gradientStart: Long,
  val gradientEnd: Long,
  val timestamp: String = "Il y a 2h"
)

data class PrivacySettings(
  val hideEmailFromPublic: Boolean = true,
  val hideSecretCode: Boolean = true,
  val e2eeChatActive: Boolean = true,
  val allowDirectMessages: Boolean = true,
  val memoryLeakGuardActive: Boolean = true,
  val lastAuditStatus: String = "Bouclier Zéro-Fuite Actif • Chiffrement TLS 1.3 • Intégrité 100%"
)

data class DiscordChannel(
  val id: String,
  val name: String,
  val category: String, // "ANNONCES & INFOS", "SALONS TEXTUELS", "SALONS VOCAUX", "MESSAGES DIRECTS"
  val topic: String,
  val isVoice: Boolean = false,
  val isEncrypted: Boolean = false,
  val unreadCount: Int = 0
)

data class Announcement(
  val id: String,
  val authorId: String = "",
  val authorName: String,
  val authorRole: UserRole = UserRole.PLAYER,
  val authorLevel: Int,
  val authorTitle: String,
  val title: String,
  val content: String,
  val tag: String, // "Journal", "Tournoi", "Clip", "Défi", "CGC"
  val timestamp: String,
  val fireCount: Int = 0,
  val gamepadCount: Int = 0,
  val trophyCount: Int = 0,
  val heartCount: Int = 0,
  val userReactedFire: Boolean = false,
  val userReactedGamepad: Boolean = false,
  val userReactedTrophy: Boolean = false,
  val userReactedHeart: Boolean = false,
  val isPinned: Boolean = false,
  val isJournalPost: Boolean = false,
  val comments: List<PostComment> = emptyList(),
  val shareCount: Int = 0,
  val privacyLevel: String = "Public"
)

data class Tournament(
  val id: String,
  val title: String,
  val gameTitle: String,
  val platform: String,
  val prizePool: String,
  val entryFee: String,
  val startDate: String,
  val slotsFilled: Int,
  val maxSlots: Int,
  val status: TournamentStatus,
  val description: String,
  val rules: List<String>,
  val isRegistered: Boolean = false
)

data class GamingGroup(
  val id: String,
  val name: String,
  val gameCategory: String,
  val description: String,
  val memberCount: Int,
  val isJoined: Boolean,
  val lastMessage: String,
  val lastMessageTime: String,
  val unreadCount: Int = 0
)

data class ChatMessage(
  val id: String,
  val groupId: String,
  val senderId: String,
  val senderName: String,
  val senderLevel: Int,
  val senderRole: UserRole = UserRole.PLAYER,
  val content: String,
  val timestamp: String,
  val isMe: Boolean,
  val isDiceRoll: Boolean = false,
  val diceValue: Int? = null,
  val hasAudioAttachment: Boolean = false,
  val hasImageAttachment: Boolean = false,
  val isEncrypted: Boolean = false,
  val reactions: Map<String, Int> = emptyMap()
)

data class LeaderboardGamer(
  val rank: Int,
  val secretName: String,
  val title: String,
  val level: Int,
  val xp: Int,
  val tournamentsWon: Int,
  val winRate: String,
  val clan: String,
  val isMe: Boolean = false
)

data class LiveStream(
  val id: String,
  val streamerName: String,
  val streamerRole: UserRole = UserRole.PLAYER,
  val title: String,
  val platform: String, // "TikTok Live" or "YouTube Live"
  val streamUrl: String,
  val gameName: String,
  val viewerCount: Int = 1,
  val isLive: Boolean = true,
  val timestamp: String = "En direct",
  val authorId: String = ""
)

data class AboutCgcMessage(
  val id: String,
  val title: String,
  val content: String,
  val authorName: String,
  val authorRole: UserRole = UserRole.CREATOR,
  val timestamp: String,
  val isPinned: Boolean = true
)
