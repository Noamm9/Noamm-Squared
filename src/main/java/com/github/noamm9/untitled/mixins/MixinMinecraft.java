package com.github.noamm9.untitled.mixins;

import com.github.noamm9.NoammAddons;
import com.github.noamm9.ui.clickgui.CategoryType;
import kotlin.enums.EnumEntriesKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "setLevel", at = @At("HEAD"))
    public void onSetLevel(ClientLevel clientLevel, CallbackInfo ci) {
        NoammAddons.logger.info("Hi From Example Mixins");
    }
}