package com.snugzy.trulytreasures.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Mixin(TradeOffers.EnchantBookFactory.class)
public class EnchantBookFactoryMixin {
    @Shadow
    @Final
    private int experience;

    @Inject(at = @At("HEAD"), method = "create", cancellable = true)
    private void removeTreasureEnchantments(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> callback) {
        List<Enchantment> list = Registry.ENCHANTMENT.stream().filter((e) -> {
            return !e.isTreasure() && e.isAvailableForEnchantedBookOffer();
        }).collect(Collectors.toList());
        Enchantment enchantment = list.get(random.nextInt(list.size()));
        int i = MathHelper.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
        ItemStack itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, i));
        int j = 2 + random.nextInt(5 + i * 10) + 3 * i;

        if (j > 64) {
            j = 64;
        }

        callback.setReturnValue(new TradeOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemStack, 12, this.experience, 0.2F));
    }
}