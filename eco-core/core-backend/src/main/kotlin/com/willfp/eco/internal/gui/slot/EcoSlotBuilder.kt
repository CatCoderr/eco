package com.willfp.eco.internal.gui.slot

import com.willfp.eco.core.gui.slot.Slot
import com.willfp.eco.core.gui.slot.SlotBuilder
import com.willfp.eco.core.gui.slot.SlotHandler
import com.willfp.eco.core.gui.slot.SlotProvider

class EcoSlotBuilder(private val provider: SlotProvider) : SlotBuilder {
    private var captive = false;

    private var onLeftClick = SlotHandler { _, _, _ -> run { } }
    private var onRightClick = SlotHandler { _, _, _ -> run { } }
    private var onShiftLeftClick = SlotHandler { _, _, _ -> run { } }
    private var onShiftRightClick = SlotHandler { _, _, _ -> run { } }
    private var onMiddleClick = SlotHandler { _, _, _ -> run { } }

    override fun onLeftClick(action: SlotHandler): SlotBuilder {
        onLeftClick = action
        return this
    }

    override fun onRightClick(action: SlotHandler): SlotBuilder {
        onRightClick = action
        return this
    }

    override fun onShiftLeftClick(action: SlotHandler): SlotBuilder {
        onShiftLeftClick = action
        return this
    }

    override fun onShiftRightClick(action: SlotHandler): SlotBuilder {
        onShiftRightClick = action
        return this
    }

    override fun onMiddleClick(action: SlotHandler): SlotBuilder {
        onMiddleClick = action
        return this
    }

    override fun setCaptive(): SlotBuilder {
        captive = true
        return this
    }

    override fun build(): Slot {
        return if (captive) {
            EcoCaptivatorSlot()
        } else {
            EcoSlot(provider, onLeftClick, onRightClick, onShiftLeftClick, onShiftRightClick, onMiddleClick)
        }
    }
}