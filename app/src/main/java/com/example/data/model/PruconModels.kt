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
  STAFF("Staff PRUCON", 40),
  PLAYER("Joueur", 10)
}

data class GamerProfile(
  val id: String,
  val email: String,
  val secretName: String,
  val secretCode: String,
  val role: UserRole = UserRole.PLAYER,
  val level: Int,
  val xp: Int,
  val nextLevelXp: Int,
  val tokens: Int,
  val title: String,
  val memberSince: String,
  val clan: String,
  val tournamentsWon: Int,
  val matchesPlayed: Int,
  val winRate: String,
  val status: String = "En ligne",
  val isImmuneFromRemoval: Boolean = false
) {
  val isCreator: Boolean
    get() = role == UserRole.CREATOR || email.equals("zenildelutu@gmail.com", ignoreCase = true)

  val canManageContent: Boolean
    get() = isCreator || role == UserRole.ADMIN

  val canNominate: Boolean
    get() = isCreator
}

data class Announcement(
  val id: String,
  val authorName: String,
  val authorLevel: Int,
  val authorTitle: String,
  val title: String,
  val content: String,
  val tag: String, // "Tournoi", "Event", "Update", "Recrutement", "PRUCON"
  val timestamp: String,
  val fireCount: Int = 0,
  val gamepadCount: Int = 0,
  val trophyCount: Int = 0,
  val userReactedFire: Boolean = false,
  val userReactedGamepad: Boolean = false,
  val userReactedTrophy: Boolean = false,
  val isPinned: Boolean = false
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
  val content: String,
  val timestamp: String,
  val isMe: Boolean,
  val isDiceRoll: Boolean = false,
  val diceValue: Int? = null,
  val hasAudioAttachment: Boolean = false,
  val hasImageAttachment: Boolean = false
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
