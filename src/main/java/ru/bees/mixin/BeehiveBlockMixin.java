package ru.bees.mixin;

import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bees.ServerMain;
import ru.bees.util.BeehiveData;
import ru.bees.util.INbtSaver;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin extends BlockWithEntity{


    protected BeehiveBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onUse", at = @At(value = "HEAD"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable ci){
        ItemStack itemInHand = player.getStackInHand(hand);
        INbtSaver beehive = (INbtSaver) world.getBlockEntity(pos);
        //rain
        if(itemInHand.isOf(Items.BUCKET)){
            if(world.isClient){
                ci.setReturnValue(ActionResult.success(true));
                return;
            }
            removeRainHoney(world, pos, player, hand, beehive, itemInHand);
            ci.setReturnValue(ActionResult.SUCCESS);
        }
        //thunder
        if(itemInHand.isOf(Items.TRIDENT) && itemInHand.getEnchantments().toString().contains("minecraft:channeling")){
            if(world.isClient){
                ci.setReturnValue(ActionResult.success(true));
                return;
            }
            removeThunderHoney(itemInHand, player, hand, world, pos, beehive);
            ci.setReturnValue(ActionResult.SUCCESS);
        }
        //nether
        if(itemInHand.isOf(Items.STONE)){
            if(world.isClient){
                ci.setReturnValue(ActionResult.success(true));
                return;
            }
            removeNetherHoney(beehive, world, player, hand, itemInHand);
            ci.setReturnValue(ActionResult.SUCCESS);
        }
        //overworld
        if(itemInHand.isEmpty()){
            if(world.isClient){
                ci.setReturnValue(ActionResult.success(true));
                return;
            }
            removeOverworldHoney(beehive, player, world, pos);
        }
        if(itemInHand.isOf(Items.COBBLESTONE)){
            if(world.isClient){
                ci.setReturnValue(ActionResult.success(true));
                return;
            }
            removeEndHoney(beehive, world, player, hand, itemInHand);
            ci.setReturnValue(ActionResult.SUCCESS);
        }

    }

    private void removeEndHoney(INbtSaver beehive, World world, PlayerEntity player, Hand hand, ItemStack itemInHand) {
        if(BeehiveData.tryRemoveHoney(beehive, BeehiveData.Honeys.EndHoney, 1)){
            player.playSound(SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            Item endstone = Items.END_STONE;
            itemInHand.decrement(1);
            player.dropItem(new ItemStack(endstone), true);
        }
    }

    private void removeOverworldHoney(INbtSaver beehive, PlayerEntity player, World world, BlockPos pos) {
        if(BeehiveData.tryRemoveHoney(beehive, BeehiveData.Honeys.OverworldHoney, 1)){
            player.playSound(SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.BLOCKS, 1, 1);
            ServerMain.LOGGER.info(Boolean.toString(BoneMealItem.useOnGround(new ItemStack(Items.BONE_MEAL), world, pos.add(0,2,0), Direction.DOWN)));
        }
    }

    private void removeNetherHoney(INbtSaver beehive, World world, PlayerEntity player, Hand hand, ItemStack itemInHand){
        if(BeehiveData.tryRemoveHoney(beehive, BeehiveData.Honeys.NetherHoney, 1)){
            player.playSound(SoundEvents.BLOCK_NETHERRACK_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            Item netherrack = Items.NETHERRACK;
            itemInHand.decrement(1);
            player.dropItem(new ItemStack(netherrack), true);
        }
    }

    private void removeRainHoney(World world, BlockPos pos, PlayerEntity player, Hand hand, INbtSaver beehive, ItemStack itemInHand){
        if(BeehiveData.tryRemoveHoney(beehive, BeehiveData.Honeys.RainHoney, 1)){
            player.playSound(SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            Item waterBucket = Items.WATER_BUCKET;
            itemInHand.decrement(1);
            player.dropItem(new ItemStack(waterBucket), true);
        }
    }
    private void removeThunderHoney(ItemStack itemInHand, PlayerEntity player, Hand hand, World world, BlockPos pos, INbtSaver beehive){
        if(BeehiveData.tryRemoveHoney(beehive, BeehiveData.Honeys.ThunderHoney, 5)){
            itemInHand.damage(100, player, playerx -> playerx.sendToolBreakStatus(hand));
            LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
            lightningEntity.setPosition(pos.getX(), world.getTopY(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ()), pos.getZ());
            world.spawnEntity(lightningEntity);
        }
    }
}
