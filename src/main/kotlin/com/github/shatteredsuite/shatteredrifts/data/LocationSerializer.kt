package com.github.shatteredsuite.shatteredrifts.data

import com.github.shatteredsuite.shatteredrifts.ShatteredRifts
import com.google.gson.*
import org.bukkit.Bukkit
import org.bukkit.Location
import java.lang.reflect.Type

class LocationSerializer(private val instance: ShatteredRifts) : JsonSerializer<Location> {
    override fun serialize(location: Location?, p1: Type?, p2: JsonSerializationContext?): JsonElement {
        val element = JsonObject()
        if (location == null) {
            return element
        }
        element.add("x", instance.gson.toJsonTree(location.x))
        element.add("y", instance.gson.toJsonTree(location.y))
        element.add("z", instance.gson.toJsonTree(location.z))
        element.add("pitch", instance.gson.toJsonTree(location.pitch))
        element.add("yaw", instance.gson.toJsonTree(location.yaw))
        element.add("world", instance.gson.toJsonTree(location.world?.name))
        return element
    }
}

class LocationDeserializer : JsonDeserializer<Location> {
    override fun deserialize(element: JsonElement, type: Type?, p2: JsonDeserializationContext?): Location {
        val obj = element.asJsonObject
        val x = obj.get("x").asDouble
        val y = obj.get("y").asDouble
        val z = obj.get("z").asDouble
        val pitch = obj.get("pitch").asFloat
        val yaw = obj.get("yaw").asFloat
        val worldName = obj.get("world")
        val world = if (worldName == null) {
            Bukkit.getWorlds()[0]
        } else Bukkit.getWorld(worldName.asString)
        return Location(world, x, y, z, yaw, pitch)
    }
}