package com.willfp.eco.spigot.drops

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.internal.drops.impl.EcoDropQueue
import com.willfp.eco.internal.drops.impl.EcoFastCollatedDropQueue

class CollatedRunnable(plugin: EcoPlugin) {
    init {
        plugin.scheduler.runTimer({
            for ((key, value) in EcoFastCollatedDropQueue.COLLATED_MAP) {
                val queue = EcoDropQueue(key)
                    .setLocation(value.location)
                    .addItems(value.drops)
                    .addXP(value.xp)

                if (value.telekinetic) {
                    queue.forceTelekinesis()
                }

                queue.push()

                EcoFastCollatedDropQueue.COLLATED_MAP.remove(key)
            }
            EcoFastCollatedDropQueue.COLLATED_MAP.clear()
        }, 0, 1)
    }
}