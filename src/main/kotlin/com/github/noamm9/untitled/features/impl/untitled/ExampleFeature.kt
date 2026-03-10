package com.github.noamm9.untitled.features.impl.untitled

import com.github.noamm9.event.impl.TickEvent
import com.github.noamm9.features.Feature
import com.github.noamm9.ui.clickgui.components.getValue
import com.github.noamm9.ui.clickgui.components.impl.*
import com.github.noamm9.ui.clickgui.components.provideDelegate
import com.github.noamm9.ui.clickgui.components.section
import com.github.noamm9.ui.clickgui.components.showIf
import com.github.noamm9.ui.clickgui.components.withDescription
import com.github.noamm9.utils.ChatUtils
import com.github.noamm9.utils.render.Render2D
import com.github.noamm9.utils.render.Render2D.width
import com.github.noamm9.utils.render.Render2D.height
import net.minecraft.client.gui.GuiGraphics
import java.awt.Color

/**
 * Example Feature: Auto Sprint
 *
 * Category: Automatically set as 'UNTITLED' from the package name 'impl.untitled'.
 * Name: Automatically set as "Example Feature" from the object name 'ExampleFeature'.
 */
object ExampleFeature : Feature(
    description = "Automatically holds the sprint key and displays your speed.",
    name = "Automatic Sprint",
    toggled = false
) {
    private val legitMode by ToggleSetting("Legit Mode", true)
        .section("Sprint Rules") // sets the inner category for the setting. all the settings under it will follow the category
        .withDescription("Only sprints when walking forward to prevent anti-cheat flags.")

    // Dropdown (0 = Vanilla, 1 = Omni)
    private val sprintDirection by DropdownSetting("Direction", 0, listOf("Vanilla", "Omni-Sprint"))
        .withDescription("Vanilla: Sprints forward only. Omni: Sprints in all directions.")
        .showIf { !legitMode.value }


    private val textColor by ColorSetting("HUD Color", Color.GREEN).section("HUD Settings")
        .withDescription("The color of the sprint status text on your screen.")

    private val printInfoButton by ButtonSetting("Print Debug Info") {
        ChatUtils.modMessage("§aLegit Mode is: §e${legitMode.value}")
        ChatUtils.modMessage("§aSprint Direction is: §e${sprintDirection.value}")
    }.withDescription("Prints your current settings into the chat.")


    // Creates a draggable HUD element. It will only render if this feature is enabled.
    private val sprintHud = hudElement(
        name = "Sprint Status",
        centered = true
    ) { context: GuiGraphics, isExample: Boolean ->
        val text = if (isExample) "[Sprinting]"
        else if (mc.player?.isSprinting == true) "[Sprinting]" else "[Walking]"

        Render2D.drawCenteredString(context, text, 0, 0, textColor.value)
        return@hudElement text.width().toFloat() to text.height().toFloat()
    }

    override fun init() {
        // register you events here. They will automatically unregister when the feature is toggled off

        register<TickEvent.Start> {
            val player = mc.player ?: return@register
            val options = mc.options
            
            if (legitMode.value || sprintDirection.value == 0) {
                if (options.keyUp.isDown && !player.isCrouching && !player.horizontalCollision) {
                    player.isSprinting = true
                }
            }
            else {
                if ((options.keyUp.isDown || options.keyDown.isDown || options.keyLeft.isDown || options.keyRight.isDown)
                    && !player.isCrouching && !player.horizontalCollision) {
                    player.isSprinting = true
                }
            }
        }
    }

    // optional overrides
    override fun onEnable() {
        super.onEnable()
        ChatUtils.modMessage("§aAutoSprint Enabled!")
    }

    override fun onDisable() {
        super.onDisable()
        mc.player?.isSprinting = false
        ChatUtils.modMessage("§cAutoSprint Disabled!")
    }
}