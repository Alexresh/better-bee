package ru.bees;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bees.util.BeeData;
import ru.bees.util.BeehiveData;
import ru.bees.util.Enums;
import ru.bees.util.INbtSaver;

import static net.minecraft.block.BeehiveBlock.HONEY_LEVEL;

public class ServerMain implements ModInitializer {
	public static final String MOD_ID = "better-bees";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) ->{
			if(world.isClient) return ActionResult.PASS;
			if(!(entity instanceof BeeEntity bee)) return ActionResult.PASS;
			if(hitResult == null) return ActionResult.PASS;

			ItemStack playerHandItemStack = player.getStackInHand(hand);
			INbtSaver beeNBT = (INbtSaver) bee;
			if(playerHandItemStack.getItem() == Items.BOOK){
				NbtList beeGenes = BeeData.getGenes(beeNBT);
				int generation = BeeData.getGeneration(beeNBT);

				player.sendMessage(Text.literal("Generation: ").formatted(Formatting.GRAY).append(Text.literal("" + generation).formatted(Formatting.AQUA)));

				if(beeGenes != null){
					String genesOutput = "Genes:\n" + beeGenes.asString()
							.replace(',', '\n')
							.replace("{","")
							.replace("}","")
							.replace("[", "[\n")
							.replace("]","\n]");
					player.sendMessage(Text.literal("Genes: ").formatted(Formatting.GRAY).append(Text.literal(genesOutput).formatted(Formatting.AQUA)));
				}
				Enums.BeeTypes type = BeeData.getBeeType(beeNBT);
				if(type != null){
					player.sendMessage(Text.literal("Type: ").formatted(Formatting.GRAY).append(Text.literal(type.name()).formatted(Formatting.AQUA)));
				}
				player.sendMessage(Text.literal("Rarity: ").formatted(Formatting.GRAY).append(Text.literal("" + BeeData.getBeeRarity(beeNBT))));

				player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 1,1);
				return ActionResult.SUCCESS;
			}

			if(playerHandItemStack.getItem() == Items.BEEHIVE){
				if(playerHandItemStack.getCount() > 1){
					player.sendMessage(Text.literal("Only one hive!").formatted(Formatting.RED), true);
					return ActionResult.SUCCESS;
				}
				NbtCompound beeNbt = bee.writeNbt(new NbtCompound());
				beeNbt.remove("UUID");
				beeNbt.putString("id", "minecraft:bee");

				NbtCompound smallBeeData = new NbtCompound();
				smallBeeData.putInt("MinOccupationTicks", bee.hasNectar() ? 2400 : 600);
				smallBeeData.put("EntityData", beeNbt);
				NbtCompound beehiveNbt = playerHandItemStack.getNbt();
				NbtCompound blockEntityTag;
				if(beehiveNbt != null){
					blockEntityTag = beehiveNbt.getCompound("BlockEntityTag");
				}else{
					blockEntityTag = new NbtCompound();
				}
				NbtList beesArray = blockEntityTag.getList("Bees", 10);
				if(beesArray.size() < 3){
					beesArray.add(smallBeeData);
					player.playSound(SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1, 1);
					bee.teleport(0,-1000,0);
				}else {
					player.sendMessage(Text.literal("Beehive is full!").formatted(Formatting.RED), true);
				}
				blockEntityTag.put("Bees", beesArray);
				BlockItem.setBlockEntityNbt(playerHandItemStack,BlockEntityType.BEEHIVE, blockEntityTag);
			}

			if(playerHandItemStack.getItem() == Items.TORCH && player.hasPermissionLevel(2)){
				bee.age = 0;
				bee.setBaby(false);
				player.playSound(SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.NEUTRAL, 1 ,1);
				return ActionResult.SUCCESS;
			}
			return ActionResult.PASS;

		});
		//get beehive block with data
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if(!world.isClient){
				//right-click on beehive
				if(world.getBlockEntity(hitResult.getBlockPos()) instanceof BeehiveBlockEntity beehiveBlockEntity){
					//click silk touch
					if(player.getStackInHand(hand).getEnchantments().toString().contains("silk_touch")){
						ItemStack itemStack = new ItemStack(Items.BEEHIVE);
						NbtCompound compound = new NbtCompound();
						//insert a BB data in compound
						compound.put(BeehiveData.HONEY_KEY, BeehiveData.getCompound(((INbtSaver) beehiveBlockEntity)));
						boolean hasBees = !beehiveBlockEntity.hasNoBees();
						//insert bees
						if (hasBees) {
							compound.put("Bees", beehiveBlockEntity.getBees());
						}
						//insertBlockEntity
						BlockItem.setBlockEntityNbt(itemStack, BlockEntityType.BEEHIVE, compound);
						NbtCompound blockStateCompound = new NbtCompound();
						//insert honeyLevel
						int honeyLevel = world.getBlockState(hitResult.getBlockPos()).get(HONEY_LEVEL);
						blockStateCompound.putInt("honey_level", honeyLevel);
						//insert blockStateTag
						itemStack.setSubNbt("BlockStateTag", blockStateCompound);
						//spawn item in world
						ItemEntity itemEntity = new ItemEntity(world, hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY() + 1, hitResult.getBlockPos().getZ(), itemStack);
						itemEntity.setToDefaultPickupDelay();
						world.spawnEntity(itemEntity);
						//remove block from world
						world.removeBlock(hitResult.getBlockPos(),false);
						return ActionResult.SUCCESS;
					}
					//click book
					if(player.getStackInHand(hand).isOf(Items.BOOK)){
						//send beehive data
						NbtCompound beehiveHoneyData = BeehiveData.getCompound(((INbtSaver) beehiveBlockEntity));
						if(!beehiveHoneyData.isEmpty()){
							player.sendMessage(Text.literal(beehiveHoneyData.asString()
									.replace("{", "").replace("}", "").replace(',', '\n')).formatted(Formatting.GOLD));
						}
						return ActionResult.SUCCESS;
					}
				}
			}
			return ActionResult.PASS;
		});


	}
}