package ru.bees.data;

import net.minecraft.block.Block;
import org.jetbrains.annotations.Nullable;
import ru.bees.util.Enums.*;

public record BreedVariant(BeeTypes parent1Type, BeeTypes parent2Type, BeeTypes resultType, @Nullable Block checkedBlock, int chance){}
