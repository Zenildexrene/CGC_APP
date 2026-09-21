package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * User Freedom, Comfort and Safety System Models
 * Implements privacy by default, granular communication controls, blocking,
 * silent muting, anonymous and respectful reporting, emergency safety freeze,
 * session governance, user data export, and transparency.
 */

enum class PrivacyVisibilityOption(val label: String, val description: String) {
  EVERYONE("Tout le monde", "Visible par l'ensemble des membres et visiteurs"),
  REGISTERED_USERS("Membres inscrits", "Visible uniquement par les membres connectés"),
  FRIENDS("Mes amis uniquement", "Visible seulement par vos amis confirmés"),
  ONLY_ME("Moi uniquement", "Strictement privé, invisible pour les autres")
}

enum class OnlineStatusVisibility(val label: String, val description: String) {
  EVERYONE("Tout le monde", "Votre présence en ligne est visible de tous"),
  FRIENDS("Mes amis uniquement", "Seuls vos amis voient quand vous êtes en ligne"),
  NOBODY("Personne (Mode Fantôme)", "Vous apparaissez hors ligne pour tout le monde")
}

enum class LastSeenVisibility(val label: String) {
  EVERYONE("Tout le monde"),
  FRIENDS("Mes amis uniquement"),
  NOBODY("Personne")
}

enum class CommunicationAudience(val label: String, val description: String) {
  EVERYONE("Tout le monde", "N'importe quel joueur peut initier le contact"),
  FRIENDS("Mes amis uniquement", "Réservé aux membres de votre liste d'amis"),
  VERIFIED_USERS("Utilisateurs vérifiés", "Membres avec badge d'authenticité CGC uniquement"),
  NOBODY("Personne", "Aucune interaction entrante autorisée")
}

enum class MuteDuration(val label: String, val durationMillis: Long) {
  ONE_HOUR("1 heure", 3600_000L),
  EIGHT_HOURS("8 heures", 28800_000L),
  ONE_DAY("24 heures", 86400_000L),
  SEVEN_DAYS("7 jours", 604800_000L),
  UNTIL_UNMUTED("Jusqu'à réactivation manuelle", -1L)
}

enum class SafetyReportCategory(val label: String, val description: String) {
  HARASSMENT("Harcèlement", "Messages répétés, insultes ou pression non sollicitée"),
  BULLYING("Intimidation / Racket", "Menaces d'exclusion, moqueries ciblées ou cyberharcèlement"),
  SPAM("Spam / Inondation", "Publicité non autorisée, liens répétitifs, messages automatisés"),
  SCAM("Arnaque / Escroquerie", "Tentative de vol de compte, vente illicite de crédits"),
  IMPERSONATION("Usurpation d'identité", "Prétend être un admin, modérateur ou autre joueur"),
  THREAT("Menaces de violence", "Menaces physiques, violences verbales graves"),
  HATE_OR_ABUSE("Discours haineux", "Propos discriminatoires, racistes ou haineux"),
  SEXUAL_CONTENT("Contenu sexuel ou obscène", "Partage de contenu non sollicité et inapproprié"),
  ILLEGAL_CONTENT("Contenu illégal", "Piratage, vente illicite, violation des lois"),
  PRIVACY_VIOLATION("Atteinte à la vie privée", "Divulgation de nom réel, téléphone ou localisation (Doxxing)"),
  OTHER("Autre préoccupation", "Autre comportement contraire à la sérénité du réseau")
}

data class UserPrivacyCenterSettings(
  val profileVisibility: PrivacyVisibilityOption = PrivacyVisibilityOption.EVERYONE,
  val onlineStatus: OnlineStatusVisibility = OnlineStatusVisibility.FRIENDS,
  val lastSeen: LastSeenVisibility = LastSeenVisibility.FRIENDS,
  val readReceiptsEnabled: Boolean = true,
  val typingIndicatorEnabled: Boolean = true,
  val profilePhotoVisibility: PrivacyVisibilityOption = PrivacyVisibilityOption.EVERYONE,
  val activityVisibility: PrivacyVisibilityOption = PrivacyVisibilityOption.FRIENDS,
  val hideEmail: Boolean = true,
  val hidePhone: Boolean = true,
  val hideExactLocation: Boolean = true,
  val filterSensitiveMedia: Boolean = true,
  val blurSensitiveContent: Boolean = true
)

data class UserCommunicationControls(
  val whoCanMessageMe: CommunicationAudience = CommunicationAudience.FRIENDS,
  val whoCanAddMeToRooms: CommunicationAudience = CommunicationAudience.FRIENDS,
  val whoCanCallMe: CommunicationAudience = CommunicationAudience.FRIENDS,
  val whoCanMentionMe: CommunicationAudience = CommunicationAudience.FRIENDS,
  val whoCanReplyToMyMessages: CommunicationAudience = CommunicationAudience.EVERYONE,
  val filterUnknownMessageRequests: Boolean = true,
  val limitUnknownMessages: Boolean = true,
  val blockRoomInvitesFromStrangers: Boolean = true
)

data class BlockedUserItem(
  val id: String,
  val username: String,
  val avatarInitial: String = username.take(1).uppercase(),
  val blockedAt: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
  val reason: String = "Bloqué par l'utilisateur",
  val effectsDescription: String = "Messages masqués, mentions et appels désactivés, invitations interdites"
)

data class MutedUserItem(
  val id: String,
  val username: String,
  val mutedAt: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
  val duration: MuteDuration = MuteDuration.ONE_DAY,
  val silentNotice: String = "L'utilisateur n'est pas informé. Notifications coupées sans perte des messages."
)

data class UserSubmittedReport(
  val id: String,
  val category: SafetyReportCategory,
  val targetType: String, // "Message", "Joueur", "Salon", "Profil"
  val targetIdentifier: String,
  val reason: String,
  val evidenceNote: String = "",
  val isAnonymous: Boolean = true,
  val status: String = "En attente d'examen", // "En attente", "En cours", "Résolu", "Classé sans suite"
  val submittedAt: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
  val adminFeedback: String? = null
)

data class UserActiveSession(
  val id: String,
  val device: String,
  val browserOrApp: String,
  val approximateLocation: String,
  val ipAddress: String,
  val lastActivity: String,
  val isCurrent: Boolean = false,
  val loginDate: String = "15/09/2026"
)

data class EmergencySafetyShieldState(
  val isActive: Boolean = false,
  val blockAllUnknownIncoming: Boolean = true,
  val disableCallsTemporarily: Boolean = true,
  val hideProfileTemporarily: Boolean = true,
  val logoutAllOtherDevices: Boolean = false,
  val activatedAt: String? = null
)

data class UserAppeal(
  val id: String,
  val sanctionType: String,
  val explanation: String,
  val status: String = "En cours d'examen",
  val submittedAt: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
  val responseNote: String? = null
)

data class PersonalizationPreferences(
  val language: String = "Français (RDC)",
  val theme: String = "Cyber Discord Dark",
  val fontSize: String = "Normal (14sp)",
  val reducedMotion: Boolean = false,
  val highContrast: Boolean = false,
  val autoplayMedia: Boolean = false,
  val soundEffects: Boolean = true
)
