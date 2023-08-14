package ru.bees.data;

import net.minecraft.item.Item;

public record BeeDrop(Item droppedItem, int chance, float multiplier) {

}
