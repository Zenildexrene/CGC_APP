package com.example

import com.example.data.model.UserRole
import com.example.data.repository.PruconRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testInitialVirginStateAndCreatorCredentials() {
    val repo = PruconRepository()
    // Verify only 1 registered user initially (virgin state)
    assertEquals(1, repo.allUsers.value.size)

    val creator = repo.profile.value
    assertEquals(PruconRepository.CREATOR_EMAIL, creator.email)
    assertEquals("zenildelutu@gmail.com", creator.email)
    assertEquals(PruconRepository.CREATOR_CODE, creator.secretCode)
    assertEquals("Firstgmg05", creator.secretCode)
    assertEquals(UserRole.CREATOR, creator.role)
    assertTrue(creator.isCreator)
    assertTrue(creator.isImmuneFromRemoval)
  }

  @Test
  fun testCreatorCanNominateOthers() {
    val repo = PruconRepository()
    // Register a new user
    val (regOk, _) = repo.registerNewPlayer("PlayerOne", "player1@test.com", "pass123")
    assertTrue(regOk)
    assertEquals(2, repo.allUsers.value.size)

    val player = repo.allUsers.value.first { it.email == "player1@test.com" }
    assertEquals(UserRole.PLAYER, player.role)

    // Switch back to creator to perform nomination
    repo.switchUser("usr_zenil_creator")
    val (nomOk, _) = repo.nominateRole(player.id, UserRole.ADMIN)
    assertTrue(nomOk)

    val updatedPlayer = repo.allUsers.value.first { it.id == player.id }
    assertEquals(UserRole.ADMIN, updatedPlayer.role)
  }

  @Test
  fun testCreatorCannotBeRemovedOrDemoted() {
    val repo = PruconRepository()
    // Attempting to demote or remove creator fails
    val (demoteOk, demoteMsg) = repo.nominateRole("usr_zenil_creator", UserRole.PLAYER)
    assertFalse(demoteOk)
    assertTrue(demoteMsg.contains("indéboulonnable") || demoteMsg.contains("interdite"))

    val (delOk, delMsg) = repo.deleteUser("usr_zenil_creator")
    assertFalse(delOk)
    assertTrue(delMsg.contains("Personne n'a le pouvoir"))

    // Verify creator is still intact
    val creator = repo.allUsers.value.first { it.email == "zenildelutu@gmail.com" }
    assertEquals(UserRole.CREATOR, creator.role)
  }

  @Test
  fun testTournamentRegistrationAndXp() {
    val repo = PruconRepository()
    val initialXp = repo.profile.value.xp
    val success = repo.registerTournament("tour_1")
    assertTrue(success)
    assertEquals(initialXp + 50, repo.profile.value.xp)
    val tournament = repo.tournaments.value.first { it.id == "tour_1" }
    assertTrue(tournament.isRegistered)
  }

  @Test
  fun testReactionToggle() {
    val repo = PruconRepository()
    val initialFire = repo.announcements.value.first { it.id == "ann_1" }.fireCount
    repo.toggleReaction("ann_1", "fire")
    val updatedFire = repo.announcements.value.first { it.id == "ann_1" }.fireCount
    assertEquals(initialFire + 1, updatedFire)
  }

  @Test
  fun testDiceRollInChat() {
    val repo = PruconRepository()
    val rollResult = repo.rollDiceInChat("grp_1")
    assertTrue(rollResult in 1..100)
    val messages = repo.chatMessages.value["grp_1"] ?: emptyList()
    assertTrue(messages.any { it.isDiceRoll && it.content.contains("$rollResult") })
  }
}
