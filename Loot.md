Chance =
- COMMON_CHANCE = beeRarity * 10
- RARE_CHANCE = beeRarity * 1

Multiplier =
- ZERO_MULTIPLIER = beeRarity * 1
- COMMON_MULTIPLIER = beeRarity * 2
- RARE_MULTIPLIER = beeRarity * 5
- LEGENDARY_MULTIPLIER = beeRarity * 10

Formula: 
```
if(random(100) <= Chance){
  itemCount = floor(beeRarity * Multiplier)
}
```



| Type        | Drop             | Chance        | Multiplier           |
|-------------|------------------|---------------|----------------------|
| Chorus      | CHORUS_FRUIT     | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Obsidian    | OBSIDIAN         | COMMON_CHANCE | RARE_MULTIPLIER      |
| Shulker     | SHULKER_SHELL    | COMMON_CHANCE | COMMON_MULTIPLIER    |
| Quartz      | QUARTZ           | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Gold        | GOLD_INGOT       | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Glowstone   | GLOWSTONE        | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Netherite   | NETHERITE_SCRAP  | RARE_CHANCE   | COMMON_MULTIPLIER    |
| SoulSand    | SOUL_SAND        | COMMON_CHANCE | RARE_MULTIPLIER      |
| Magma       | MAGMA_CREAM      | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Dirt        | DIRT             | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Cobblestone | COBBLESTONE      | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Wood        | OAK_WOOD         | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Stone       | STONE            | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Coal        | COAL             | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Iron        | RAW_IRON         | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Diamond     | DIAMOND          | COMMON_CHANCE | ZERO_MULTIPLIER      |
| Emerald     | EMERALD          | COMMON_CHANCE | RARE_MULTIPLIER      |
| Clay        | CLAY_BALL        | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Sand        | GRAVEL           | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Glass       | GLASS            | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Lapis       | CLAY_BALL        | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Water       | HEART_OF_THE_SEA | RARE_CHANCE   | ZERO_MULTIPLIER      |
| Copper      | RAW_COPPER       | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Gravel      | GRAVEL           | COMMON_CHANCE | LEGENDARY_MULTIPLIER |
| Redstone    | REDSTONE         | COMMON_CHANCE | LEGENDARY_MULTIPLIER |