package com.spoonofcode.routes

import com.spoonofcode.data.model.Profile
import com.spoonofcode.data.model.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val profiles = mutableListOf(
    Profile(1, User("1", "Tom", "Hawk")),
    Profile(2, User("2", "Harry", "Potter")),
    Profile(3, User("3", "Cristiano", "Ronaldo")),
)

fun Route.profiles() {
    route("/profiles") {
        get("/") {
            call.respond(profiles)
        }

        get("/{id}") {
            val profileId = call.parameters["id"]?.toInt()

            if (profileId != null) {
                try {
                    val item = getProfileById(profileId)
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
            val newProfile = call.receive<Profile>() // TODO #1 Change to uuid and auto id creation
//            val newProfileId = profiles.size + 1
//            newProfile.id = newProfileId
            profiles.add(newProfile)
            call.respond(HttpStatusCode.Created, newProfile)
        }

        put("/{id}") {
            val profileId = call.parameters["id"]?.toInt()
            if (profileId != null) {
                try {
                    val existingProfile = getProfileById(profileId)
                    if (existingProfile != null) {
                        val updatedProfile = call.receive<Profile>()
                        profiles[profileId-1] = updatedProfile.copy(id = profileId) // TODO #1 Set proper element index from list
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
                    val deletedProfile = profiles.removeAt(profileId-1) // TODO #1 Set proper element index from list
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

fun getProfileById(id: Int): Profile? {
    return profiles.find { profile: Profile -> profile.id == id }
}