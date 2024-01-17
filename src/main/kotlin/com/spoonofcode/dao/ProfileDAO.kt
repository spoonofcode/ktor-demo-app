package com.spoonofcode.dao

import com.spoonofcode.data.model.Profile
import com.spoonofcode.data.model.Profiles
import com.spoonofcode.data.model.User
import com.spoonofcode.data.model.Users
import com.spoonofcode.plugins.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

interface ProfileDAO {
    suspend fun createProfile(profile: Profile): Profile?
    suspend fun readProfile(id: Int): Profile?
    suspend fun updateProfile(profile: Profile): Boolean
    suspend fun deleteProfile(id: Int): Boolean
    suspend fun readAllProfiles(): List<Profile>
}

class ProfileDAOImpl : ProfileDAO {
    override suspend fun createProfile(profile: Profile): Profile? = dbQuery {
        val newUserId = Users.insertAndGetId {
            it[firstName] = profile.user.firstName
            it[lastName] = profile.user.lastName
        }

        val insertStatement = Profiles.insert {
            it[description] = profile.description
            it[userId] = EntityID(newUserId.value, Users)
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToProfile)
    }

    override suspend fun readProfile(id: Int): Profile? = dbQuery {
        Profiles
            .select { Profiles.id eq id }
            .map(::resultRowToProfile)
            .singleOrNull()
    }

    override suspend fun updateProfile(profile: Profile): Boolean = dbQuery {
        Profiles.update({ Profiles.id eq profile.id }) {
            it[description] = profile.description
        } > 0
    }

    override suspend fun deleteProfile(id: Int): Boolean = dbQuery {
        Profiles.deleteWhere { Profiles.id eq id } > 0
    }

    override suspend fun readAllProfiles(): List<Profile> = dbQuery {
        Profiles.selectAll().map(::resultRowToProfile)
    }

    private fun resultRowToProfile(row: ResultRow): Profile {
        println("row = $row")
        val user = Users.select( Users.id eq row[Profiles.userId]).singleOrNull()?.let(::resultRowToUser)
        return Profile(
            id = row[Profiles.id].value,
            description = row[Profiles.description],
            user = requireNotNull(user)
        )
    }

    fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id].value,
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
    )
}