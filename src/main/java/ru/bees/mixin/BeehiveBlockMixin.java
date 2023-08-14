package ru.bees.mixin;

import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bees.util.Enums.*;
import ru.bees.util.BeehiveData;
import ru.bees.util.INbtSaver;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin extends BlockWithEntity{


    protected BeehiveBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onUse", at = @At(value = "HEAD"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> ci){
        ItemStack itemInHand = player.getStackInHand(hand);
        INbtSaver beehive = (INbtSaver) world.getBlockEntity(pos);
        //rain
        checkRainHoney(itemInHand, world, ci, beehive, player);

        //thunder
        checkThunderHoney(itemInHand, world, ci, beehive, player, hand, pos);

        //overworld
        checkOverworldHoney(itemInHand, world, ci, beehive, player);

        //nether
        checkNetherHoney(itemInHand, world, ci, beehive, player);

        //end
        checkEndHoney(itemInHand, world, ci, beehive, player);
    }

    private void checkRainHoney(ItemStack itemInHand, World world, CallbackInfoReturnable<ActionResult> ci, INbtSaver beehive, PlayerEntity player) {
        //BUCKET -> WATER_BUCKET
        checkItem(Items.BUCKET, Items.WATER_BUCKET, Honeys.RainHoney,5, SoundEvents.ITEM_BUCKET_FILL, player, itemInHand, beehive, world ,ci);
    }
    private void checkThunderHoney(ItemStack itemInHand, World world, CallbackInfoReturnable<ActionResult> ci, INbtSaver beehive, PlayerEntity player, Hand hand, BlockPos pos) {
        if(itemInHand.isOf(Items.TRIDENT) && itemInHand.getEnchantments().toString().contains("minecraft:channeling")){
            if(world.isClient){
                ci.setReturnValue(ActionResult.success(true));
                return;
            }
            if(BeehiveData.tryRemoveHoney(beehive, Honeys.ThunderHoney, 5)){
                itemInHand.damage(100, player, playerx -> playerx.sendToolBreakStatus(hand));
                LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                lightningEntity.setPosition(pos.getX(), world.getTopY(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ()), pos.getZ());
                world.spawnEntity(lightningEntity);
            }
            ci.setReturnValue(ActionResult.SUCCESS);
        }
    }
    private void checkOverworldHoney(ItemStack itemInHand, World world, CallbackInfoReturnable<ActionResult> ci, INbtSaver beehive, PlayerEntity player) {
        //NETHERRACK || END_STONE -> STONE
        checkItem(Items.NETHERRACK, Items.STONE, Honeys.OverworldHoney,1, SoundEvents.BLOCK_STONE_PLACE, player, itemInHand, beehive, world ,ci);
        checkItem(Items.END_STONE, Items.STONE, Honeys.OverworldHoney,1, SoundEvents.BLOCK_STONE_PLACE, player, itemInHand, beehive, world ,ci);

        //BLACK_STONE -> COBBLESTONE
        checkItem(Items.BLACKSTONE, Items.COBBLESTONE, Honeys.OverworldHoney,1, SoundEvents.BLOCK_STONE_BREAK, player, itemInHand, beehive, world ,ci);
    }
    private void checkNetherHoney(ItemStack itemInHand, World world, CallbackInfoReturnable<ActionResult> ci, INbtSaver beehive, PlayerEntity player) {
        //STONE->NETHERRACK
        checkItem(Items.STONE, Items.NETHERRACK, Honeys.NetherHoney,1, SoundEvents.BLOCK_STONE_BREAK, player, itemInHand, beehive, world ,ci);

        //COBBLESTONE->BLACKSTONE
        checkItem(Items.COBBLESTONE, Items.BLACKSTONE, Honeys.NetherHoney,1, SoundEvents.BLOCK_STONE_BREAK, player, itemInHand, beehive, world ,ci);

        //BUCKET->LAVABUCKET
        checkItem(Items.BUCKET, Items.LAVA_BUCKET, Honeys.NetherHoney,5, SoundEvents.BLOCK_LAVA_POP, player, itemInHand, beehive, world ,ci);

        //STONE_BRICKS->NETHER_BRICKS
        checkItem(Items.STONE_BRICKS, Items.NETHER_BRICKS, Honeys.NetherHoney,1, SoundEvents.BLOCK_STONE_PLACE, player, itemInHand, beehive, world ,ci);

    }

    private void checkEndHoney(ItemStack itemInHand, World world, CallbackInfoReturnable<ActionResult> ci, INbtSaver beehive, PlayerEntity player) {
        //COBBLESTONE->ENDSTONE
        checkItem(Items.COBBLESTONE, Items.END_STONE, Honeys.EndHoney,1, SoundEvents.BLOCK_STONE_PLACE, player, itemInHand, beehive, world ,ci);

        //GLASS->PURPUR_GLASS
        checkItem(Items.GLASS, Items.PURPLE_STAINED_GLASS, Honeys.EndHoney,1, SoundEvents.BLOCK_GLASS_PLACE, player, itemInHand, beehive, world ,ci);

    }

    private void checkItem(Item checkedItem, Item droppedItem, Honeys removedHoney, int amount, SoundEvent soundEvent, PlayerEntity player, ItemStack itemInHand, INbtSaver beehive, World world, CallbackInfoReturnable<ActionResult> ci ){
        if(itemInHand.isOf(checkedItem)){
            if(world.isClient){
                ci.setReturnValue(ActionResult.success(true));
                return;
            }
            if(BeehiveData.tryRemoveHoney(beehive, removedHoney, amount)){
                player.playSound(soundEvent, SoundCategory.BLOCKS, 1.0f, 1.0f);
                itemInHand.decrement(1);
                player.dropItem(new ItemStack(droppedItem), true);
            }
            ci.setReturnValue(ActionResult.SUCCESS);
        }
    }
}
