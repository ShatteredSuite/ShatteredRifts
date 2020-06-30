package com.github.shatteredsuite.shatteredrifts

import com.github.shatteredsuite.core.ShatteredPlugin
import com.github.shatteredsuite.shatteredrifts.data.RiftLocation
import com.github.shatteredsuite.shatteredrifts.data.RiftManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle

class ShatteredRifts : ShatteredPlugin() {
    var riftManager = RiftManager()
    var stoneTimings = mutableMapOf<String, Long>()

    init {
        this.createMessages = true
    }

    override fun onFirstTick() {
        riftManager.register(RiftLocation("test",
                Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0),
                Location(Bukkit.getWorld("world"), 50.0, 100.0, 0.0), 4.0, 4, 60,
                500, 5, 10, Particle.PORTAL, Particle.ENCHANTMENT_TABLE, true))
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, {
            this.updateStones()
        }, 20L, 20L)
    }

    fun updateStones() {
        for(stoneLocation in riftManager.getAll()) {
            // Add stones to timings if they're not in there already. Add one if they are.
            stoneTimings[stoneLocation.id] = stoneTimings[stoneLocation.id]?.plus(1) ?: 0
            // Play particles every 5 seconds.
            if(stoneTimings[stoneLocation.id]!! % stoneLocation.particleFrequency == 0L) {
                stoneLocation.playAmbientParticles(stoneTimings[stoneLocation.id]!!.toFloat() / (stoneLocation.timing.toFloat()))
            }
            // Teleport, play particles every second.
            if(stoneTimings[stoneLocation.id]!! > stoneLocation.timing) {
                stoneLocation.findPlayers().forEach { it.teleport(stoneLocation.destination) }
                stoneLocation.playActiveParticles()
            }
            // Reset timer.
            if(stoneTimings[stoneLocation.id]!! > stoneLocation.timing + stoneLocation.duration) {
                stoneTimings[stoneLocation.id] = 0
            }
        }
    }
}