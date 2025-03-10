package com.willfp.eco.internal.gui.slot

import com.willfp.eco.core.Eco
import com.willfp.eco.core.gui.slot.functional.SlotHandler
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class EcoCaptivatorSlot : EcoSlot(
    { _, _ -> ItemStack(Material.AIR) },
    allowMovingItem,
    allowMovingItem,
    allowMovingItem,
    allowMovingItem,
    allowMovingItem,
    { _, _, _ -> }
) {
    companion object {
        val plugin = Eco.getHandler().ecoPlugin

        val allowMovingItem = SlotHandler { event, _, _ ->
            event.isCancelled = false
        }
    }

    override fun isCaptive(): Boolean {
        return true
    }
}