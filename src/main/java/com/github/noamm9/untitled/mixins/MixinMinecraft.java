package com.github.noamm9.untitled.mixins;

import com.github.noamm9.NoammAddons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "setLevel", at = @At("HEAD"))
    public void onSetLevel(ClientLevel clientLevel, CallbackInfo ci) {
        NoammAddons.INSTANCE.getLogger().info("Hi From Example Mixins");
    }
}