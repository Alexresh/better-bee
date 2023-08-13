Better-bees (Fabric, Fabric api, Minecraft 1.20.1)

На русском: [README.md](https://github.com/Alexresh/better-bee/blob/master/README_RU.md)

This mod adds some mechanics to vanilla bees:
1. Bees gain genes, type and rarity
2. Some bees bring special honey to the hive, which is spent to exchange blocks or for other things
3. some bees bring an item of their type when entering the hive (drops in the hive as an item, the hopper under the hive will pick it up).
   
  ![Drop example](https://github.com/Alexresh/better-bee/blob/master/images/example2.png?raw=true)

4. Bees can interbreed in the following pattern (better-bees.vsdx visio scheme):
   
  ![Dropping and crossbreeding scheme](https://github.com/Alexresh/better-bee/blob/master/images/scheme.png?raw=true)

5. Clicking on a bee with the lectern displays information about it in the chat
6. Clicking on a bee with a hive will make it settle in it
7. Clicking on the hive with any item with silk touch enchantment will make the hive drop with all the honey and bees inside it
8. (If the player permission level is 2) Clicking on a small bee with a torch will make it grow.

How do I get special honey in the hive? 
- A bee with basic genes (OverworldBasic, NetherBasic, EndBasic, Thunder, Rain) when pollinating a flower and entering a hive will add honey to that hive.

How do I spend special honey? 
- Each special honey is spent to exchange items or to trigger certain events (lightning if the honey was brought by a thunder bee)

  ![Example of a lightning call](https://github.com/Alexresh/better-bee/blob/master/images/example4.png?raw=true)

Here are some examples from the diagram in text form: 
- (NetherBascic bee) clicking on a hive with a bucket will fill that bucket with lava and spend 5 Nether honey.
- (OverworldBasic) clicking on  hive with netherrack will drop a stone and spend 1 Overworld honey.

How do you propagate bees according to the scheme?
- Initially you need to get basic bees (Nether, End). To do this, they must be born in the corresponding world.
- Next, they must be crossed with each other so that both genes are passed on to the new bee, and it has a different gene (Overworld). Crossing Overworld and End will result in EndBasic. Overworld+Nether=NetherBasic (the world doesn't matter anymore).

  ![How to get an Overworld bee](https://github.com/Alexresh/better-bee/blob/master/images/example.png?raw=true)

How to get a bee that will bring items?
- Take parents with the same genes (e.g. OverworldBasic (Overworld+Overworld)) and make them love over a block of stone, if both parents were over a block of stone, then with a 10% chance you will get a stone bee (It will have no genes, but the type Stone will appear).

  ![Example of obtaining an obsidian bee](https://github.com/Alexresh/better-bee/blob/master/images/example3.png?raw=true)

- Depending on her rarity she will bring a certain amount of stone to the hive with a certain chance from the scheme.
For example a bee with rarity 1 will bring 1 stone with 10% chance, and a bee with rarity 10 will bring 10 stones with 100% chance. 

How to get a rarer bee?
- Cross 2 bees with the same type of bee