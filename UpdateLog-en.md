## Update Log

### 0.1.7 (not released yet)

Updated following content:

- Added more types of signs, such as wooden signs, ice signs.
- Added handrails. Handrails have multiple styles.
- For force placing tool and fast building tool, in non-water-including mode, if using on waterlogged blocks, then whether the placed block is waterlogged is decided on whether the block at that position before the placement is waterlogged. For example, duplicating a waterlogged block to a non-waterlogged place, the duplicated block is not waterlogged.
- In bi-hand mode, when using force placing tool and fast building tool, the block placed may be influenced by tags `BlockStateTag` and `BlockEntityTag`, to specify the block state and block entity of the block after placing.
- Renamed most road blocks, to solve the problem that names are too long. As old names are no longer recognized, **if you used road blocks previously, after updating this mod, previous road blocks will disappear!** After updating the mod before entering the world, remember to back up the world.
- Added yellow and cyan light blocks, and added stripped light blocks with a background.
- Adjusted the models of light blocks in the inventory.
- Optimized the process of runtime data generation (including resource pack and data pack). Now more data is generated on runtime. Some block states, due to limitations of ARRP mod, still stores json files in mod file.
- Added more block tags.
- Fixed the issue that data tag tool has no tooltip.
- As the heavenly Mojang annotates Block.getName as `@Environment(EnvType.CLIENT)`, some tools were switched to client-side usage.
- Now sign blocks support json texts. You can type `-json <a json>` in a text box, e.g., `-json {color:red,text:Hi}`.
- Added text copy tool, which can be used to copy and paste texts, and allows copy and paste between vanilla signs and mod signs.

Fixed following issues:

- Force placing tool and fast building tool may not correctly handle waterlogging, and may not see item tags on the offhand item.
- Force placing tool can remove entity even if you are not in Creative Mode.

### 0.1.6

Updated following content:

- Invisible glowing signs.
- For Minecraft version Minecraft 1.17 and above, you can set text outlines.
- Improved sign edit screen. You can adjust the order of lines, and cancel the edit.
- Adjusted the logic of rearranging texts. Now each line keeps a margin above and below, the size of which equals to 1/8 of the line.
- Magma creams can be used to quickly rearrange texts.
- Added zh-tw and zh-hk.
- Adjusted the default text size of wall sign blocks.
- Fast building tools allows quickly switching matching mode.
- No items will drop when using force placing tool to break a chest.

Fixed following issues:

- In game modes except Creative, the fast-building tool and force-placing tool are still available.
- In Survival mode, holding some tools and left-click may execute multiple times.
- Rearrangement multiple of texts is sometimes incorrect.
- Even in Survival mode, breaking slabs with slab tools does not drop stacks.
- Sign edit screen is too crowded in some languages, for example, English.
- Hung sign bars, when placed in water, are not by default waterlogged.
- Incorrect models of some light decoration blocks.

### 0.1.5

Updated following content:

- ID checker tool can now be used to see biome iD.
- Optimized the NBT display when triggered.

Fixed following issues:

- Some tools are executed on both client and server side.
- Game instance starts normally when ARRP is not installed.

### 0.1.4

First publish.