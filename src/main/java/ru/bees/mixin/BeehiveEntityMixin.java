package ru.bees.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.bees.util.BeeData;
import ru.bees.util.BeehiveData;
import ru.bees.util.Enums.*;
import ru.bees.util.INbtSaver;

@Mixin(BeehiveBlockEntity.class)
public abstract class BeehiveEntityMixin extends BlockEntity {

    private static final int COMMON_CHANCE = 10;
    private static final int RARE_CHANCE = 5;
    private static final float COMMON_MULTIPLIER = 0.99f;
    private static final float RARE_MULTIPLIER = 0.49f;
    private static final float ZERO_MULTIPLIER = 0;
    private static final int TENTH = 10;


    public BeehiveEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "tryEnterHive(Lnet/minecraft/entity/Entity;ZI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;removeAllPassengers()V", shift = At.Shift.AFTER))
    private void addBee(Entity entity, boolean hasNectar, int ticksInHive, CallbackInfo ci){
        if(entity instanceof BeeEntity bee && hasNectar){
            INbtSaver BBbee = (INbtSaver) bee;
            INbtSaver BBBeehive = (INbtSaver) this;
            //add honeys
            NbtList genes = BeeData.getGenes(BBbee);
            if(genes != null){
                for (NbtElement gene:genes) {
                    Genes swGene = Genes.valueOf(gene.asString());
                    switch (swGene){
                        case Rain -> BeehiveData.addHoney(BBBeehive, Honeys.RainHoney, 1);
                        case Thunder -> BeehiveData.addHoney(BBBeehive, Honeys.ThunderHoney, 1);
                        case End -> BeehiveData.addHoney(BBBeehive, Honeys.EndHoney, 5);
                        case Nether -> BeehiveData.addHoney(BBBeehive, Honeys.NetherHoney, 5);
                        case Overworld -> BeehiveData.addHoney(BBBeehive, Honeys.OverworldHoney, 5);
                        default -> {}
                    }
                }
            }
            //drop items
            BeeTypes type = BeeData.getBeeType(BBbee);
            int beeRarity = BeeData.getBeeRarity(BBbee);
            if(type == null) return;

            switch (type){
                case Chorus -> dropItem(Items.CHORUS_FRUIT, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Obsidian -> dropItem(Items.OBSIDIAN, beeRarity * COMMON_CHANCE * TENTH, beeRarity, RARE_MULTIPLIER);
                case Shulker -> dropItem(Items.SHULKER_SHELL, beeRarity * COMMON_CHANCE * TENTH, beeRarity, ZERO_MULTIPLIER);
                case Quartz -> dropItem(Items.QUARTZ, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Gold -> dropItem(Items.GOLD_INGOT, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Glowstone -> dropItem(Items.GLOWSTONE, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Netherite -> dropItem(Items.NETHERITE_SCRAP, beeRarity * RARE_CHANCE, beeRarity, ZERO_MULTIPLIER);
                case SoulSand -> dropItem(Items.SOUL_SAND, beeRarity * COMMON_CHANCE * TENTH, beeRarity, RARE_MULTIPLIER);
                case Magma -> dropItem(Items.MAGMA_CREAM, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Dirt -> dropItem(Items.DIRT, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Cobblestone -> dropItem(Items.COBBLESTONE, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Wood -> dropItem(Items.OAK_WOOD, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Stone -> dropItem(Items.STONE, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Coal -> dropItem(Items.COAL, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Iron -> dropItem(Items.RAW_IRON, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Diamond -> dropItem(Items.DIAMOND, beeRarity * COMMON_CHANCE, beeRarity, ZERO_MULTIPLIER);
                case Emerald -> dropItem(Items.EMERALD, beeRarity * COMMON_CHANCE * 5, beeRarity, RARE_MULTIPLIER);

                case Clay -> dropItem(Items.CLAY_BALL, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Sand -> dropItem(Items.GRAVEL, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Glass -> dropItem(Items.GLASS, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Lapis -> dropItem(Items.CLAY_BALL, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Water -> dropItem(Items.HEART_OF_THE_SEA, beeRarity * COMMON_CHANCE, beeRarity, ZERO_MULTIPLIER);
                case Copper -> dropItem(Items.RAW_COPPER, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Gravel -> dropItem(Items.GRAVEL, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                case Redstone -> dropItem(Items.REDSTONE, beeRarity * COMMON_CHANCE * TENTH, beeRarity, COMMON_MULTIPLIER);
                default -> {}
            }
        }
    }

    private void dropItem(Item item, int chance, float beeRarity, float multiplier){
        if(world.random.nextInt(1000) <= chance){
            ItemEntity itemEntity = new ItemEntity(this.world, this.getPos().getX(),this.getPos().getY(),this.getPos().getZ(), new ItemStack(item));
            itemEntity.getStack().setCount((int) Math.floor(beeRarity * multiplier) + 1);
            this.getWorld().spawnEntity(itemEntity);
        }
    }
}