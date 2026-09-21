package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Enterprise & Platform Admin Models for the Advanced Admin Dashboard.
 * Covers full RBAC, Security & Sessions, Moderation, Reports, Room Management,
 * Audit Logs, System Monitoring, Backups, API Keys, and Support Tickets.
 */

enum class AdminRoleType(val id: String, val title: String, val description: String) {
  SUPER_ADMIN("super_admin", "Super Admin", "Accès complet et sans restriction à toute la plateforme"),
  ADMINISTRATOR("administrator", "Administrateur", "Gestion générale des utilisateurs, modération et salons"),
  MODERATOR("moderator", "Modérateur", "Modération des contenus, signalements et sanctions utilisateurs"),
  SUPPORT("support", "Support", "Traitement des tickets d'assistance et suivi utilisateur"),
  ANALYST("analyst", "Analyste", "Consultation des statistiques et métriques sans modification")
}

data class AdminPermission(
  val code: String,
  val category: String,
  val label: String
)

object PlatformPermissions {
  val ALL = listOf(
    AdminPermission("users.view", "Utilisateurs", "Voir la liste et profils des utilisateurs"),
    AdminPermission("users.manage", "Utilisateurs", "Modifier et gérer les comptes utilisateurs"),
    AdminPermission("users.warn", "Utilisateurs", "Avertir un utilisateur"),
    AdminPermission("users.mute", "Utilisateurs", "Mettre en sourdine un utilisateur"),
    AdminPermission("users.ban", "Utilisateurs", "Bannir temporairement ou définitivement"),
    AdminPermission("moderation.manage", "Modération", "Centre de modération et filtrage automatisé"),
    AdminPermission("reports.manage", "Signalements", "Attribuer, résoudre ou rejeter des signalements"),
    AdminPermission("reports.view", "Signalements", "Consulter les signalements utilisateurs"),
    AdminPermission("rooms.manage", "Salons", "Créer, verrouiller, éditer ou archiver des salons"),
    AdminPermission("messages.manage", "Messages", "Rechercher, supprimer ou restaurer des messages"),
    AdminPermission("announcements.manage", "Annonces", "Diffuser des annonces globales ou ciblées"),
    AdminPermission("statistics.view", "Statistiques", "Consulter les statistiques d'activité"),
    AdminPermission("analytics.view", "Analytique", "Accéder aux métriques détaillées et exports"),
    AdminPermission("security.manage", "Sécurité", "Gestion des sessions, IP, 2FA et alertes"),
    AdminPermission("audit.view", "Audit Logs", "Consulter et exporter les journaux d'audit immuables"),
    AdminPermission("system.monitor", "Système", "Surveillance serveur, base de données et websockets"),
    AdminPermission("backups.manage", "Sauvegardes", "Exécuter des sauvegardes manuelles ou restaurations"),
    AdminPermission("api.manage", "API & Webhooks", "Générer ou révoquer les clés d'API et webhooks"),
    AdminPermission("settings.view", "Paramètres", "Consulter la configuration globale de la plateforme"),
    AdminPermission("settings.edit", "Paramètres", "Modifier les paramètres de la plateforme"),
    AdminPermission("admins.manage", "Administrateurs", "Créer, suspendre ou révoquer des administrateurs"),
    AdminPermission("danger_zone.execute", "Zone Danger", "Actions destructives critiques (Super Admin uniquement)")
  )
}

enum class UserAccountStatus(val label: String) {
  ACTIVE("Actif"),
  MUTED("En sourdine"),
  SUSPENDED("Suspendu"),
  BANNED("Banni"),
  UNVERIFIED("Non vérifié")
}

data class ManagedUser(
  val id: String,
  val username: String,
  val email: String,
  val avatarInitial: String = username.take(1).uppercase(),
  val role: String = "Joueur",
  val status: UserAccountStatus = UserAccountStatus.ACTIVE,
  val isVerified: Boolean = true,
  val registrationDate: String = "15/08/2026",
  val lastLogin: String = "Aujourd'hui à 14:32",
  val lastActivity: String = "Il y a 3 min",
  val warningsCount: Int = 0,
  val reportsReceivedCount: Int = 0,
  val reportsSentCount: Int = 1,
  val messagesCount: Int = 42,
  val roomsJoinedCount: Int = 4,
  val ipAddress: String = "105.102.14.88",
  val device: String = "PlayStation 5 / Android App",
  val internalNote: String = ""
)

enum class ReportCategory(val label: String) {
  SPAM("Spam"),
  HARASSMENT("Harcèlement"),
  SCAM("Arnaque / Escroquerie"),
  IMPERSONATION("Usurpation d'identité"),
  INAPPROPRIATE_CONTENT("Contenu inapproprié"),
  HATE_SPEECH("Discours de haine"),
  THREATS("Menaces"),
  ILLEGAL_CONTENT("Contenu illégal"),
  OTHER("Autre")
}

enum class ReportStatus(val label: String) {
  PENDING("En attente"),
  UNDER_REVIEW("En examen"),
  RESOLVED("Résolu"),
  REJECTED("Rejeté")
}

enum class ReportPriority(val label: String) {
  LOW("Basse"),
  MEDIUM("Moyenne"),
  HIGH("Haute"),
  CRITICAL("Critique")
}

data class PlatformReport(
  val id: String,
  val category: ReportCategory,
  val reportedTarget: String,
  val reportedUser: String,
  val reportingUser: String,
  val reason: String,
  val priority: ReportPriority = ReportPriority.MEDIUM,
  var status: ReportStatus = ReportStatus.PENDING,
  val timestamp: String = "Il y a 10 min",
  var assignedAdmin: String? = null,
  var internalNote: String = ""
)

data class ManagedRoom(
  val id: String,
  val name: String,
  val category: String,
  val description: String,
  var isLocked: Boolean = false,
  var isPrivate: Boolean = false,
  var isArchived: Boolean = false,
  val memberCount: Int = 24,
  val maxMembers: Int = 200,
  val slowModeSeconds: Int = 0,
  val allowMedia: Boolean = true,
  val allowLinks: Boolean = true
)

data class AuditLogEntry(
  val id: String,
  val adminName: String,
  val adminRole: String,
  val action: String,
  val target: String,
  val details: String,
  val ipAddress: String = "197.242.128.4",
  val device: String = "Admin Console / Chrome",
  val timestamp: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
)

data class SecurityAlert(
  val id: String,
  val title: String,
  val description: String,
  val severity: String = "WARNING", // INFO, WARNING, CRITICAL
  val timestamp: String = "Aujourd'hui à 11:20",
  var isDismissed: Boolean = false
)

data class SupportTicket(
  val id: String,
  val subject: String,
  val userName: String,
  val priority: String = "Moyenne",
  var status: String = "Ouvert", // Ouvert, En cours, Résolu, Fermé
  val timestamp: String = "Il y a 1h",
  var internalNote: String = ""
)

data class ApiKeyEntry(
  val id: String,
  val name: String,
  val prefix: String,
  val permissions: String,
  val createdAt: String,
  val lastUsed: String,
  var isActive: Boolean = true
)

data class SystemMetrics(
  val cpuUsagePercent: Int = 28,
  val ramUsagePercent: Int = 42,
  val diskUsagePercent: Int = 31,
  val activeWebsockets: Int = 48,
  val databaseStatus: String = "Opérationnel (Latence 4ms)",
  val apiStatus: String = "Normal (99.98% uptime)",
  val serverLoad: String = "Faible (0.24, 0.31, 0.28)",
  val messagesPerMinute: Int = 14
)
