package com.spoonofcode.routes

import com.spoonofcode.dao.ProfileDAO
import com.spoonofcode.data.model.Profile
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.koin.ktor.ext.get

fun Route.profiles(profileDAO: ProfileDAO = get()) {
    route("/profiles") {
        get("/") {
            val profiles = profileDAO.readAllProfiles()
            call.respond(profiles)
        }

        get("/{id}") {
            val profileId = call.parameters["id"]?.toInt()

            if (profileId != null) {
                try {
                    val item = profileDAO.readProfile(id = profileId)
                    if (item != null) {
                        call.respond(item)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Profile not found")
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid Id format")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }

        post("/") {
            val newProfile = call.receive<Profile>()
            try {
                val result = profileDAO.createProfile(profile = newProfile)
                result?.let {
                    call.respond(HttpStatusCode.Created, it)
                } ?: call.respond(HttpStatusCode.NotImplemented, "Error adding profile")
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "SQL Exception!!")
            }
        }

        put("/{id}") {
            val profileId = call.parameters["id"]?.toInt()
            if (profileId != null) {
                try {
                    val existingProfile = profileDAO.readProfile(profileId)
                    if (existingProfile != null) {
                        val updatedProfile = call.receive<Profile>()
                        profileDAO.updateProfile(updatedProfile)
                        call.respond(HttpStatusCode.OK, updatedProfile)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Profile not found")
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid Id format")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }

        delete("/{id}") {
            val profileId = call.parameters["id"]?.toInt()
            if (profileId != null) {
                try {
                    val deletedProfile = profileDAO.deleteProfile(profileId)
                    if (deletedProfile != null) {
                        call.respond(HttpStatusCode.OK, "Profile deleted")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Profile not found")
                    }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid Id format")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing 'id' parameter")
            }
        }
    }
}