## Update Log

Note: Not all versions in the update log are definitely published. Please refer to relevant pages in CurseForge and Modrinth, or the "releases" section in the GitHub.

### 0.2.0

Updated following content:

- Licence switched to LGPLv3.
- Use BRRP as the dependency, instead of former ARRP.
- Added custom colored sign blocks, but as it is not stable yet, it is not formally added.
- Added crafting recipe for some content.
- Adjusted the texture of road block.
- No longer allow snow placed on road blocks (blocks with `mishanguc:roads` tag), to prevent the situation roads can't be seen in snowy days.
- Added `-rect` and `-pattern` feature or sign blocks. Clicking a sign holding slime ball can replace arrow characters with `-pattern` form.
- Walking on road blocks (blocks with `mishanguc:roads` tag) can accelerate, which does not affect FOV.
- Added yellow straight line and angle line roads (previously there is only white), yellow cross line roads (previously there is only white), road with white thick T-shaped yellow line (previously there is only road with white normal line with yellow line, and white thick line with yellow double line). Auto-connecting road blocks are adjusted accordingly.
- Changed Chinese names of some road blocks (see the update log in Chinese).
- Changed `RoadConnectionState` into a record. For versions for 1.16.5, though record is not supported in Java 8, relevant changed are made following the relevant formats.
- Adjusted the outline drawing of fast building tool and force placing tool, to make them consistent.
- Some items have durability now.
- When mirroring tool is used on the up or down surface of blocks, the mirror will be determined by player's horizontal facing.
- Added explosion tool and omnipotent tool.
- All items are not stackable now.
- When operating on blocks with rotating tools or mirroring tools, if the operated block is the same as that before the operation, the operation is regarded fail.
- The model of fast building tool can be affected by its properties. If the matching range is larger, the texture is deeper.
- Adjusted the display of mod info. It displays only when player logs into the world, and will not display again when the player respawns or switches dimension.

Fixed following issues:

- When displaying values of enum properties using block state tool, the result is not the result of `StringIdentifiable#asString`, but directly the name of enum value in the code.
- When placing a sign bar above a hung sign, the sign bars above it do not update.
- When running on a dedicated server, switching the matching rule of fast building tool causes crash.
- The height of display range of full wall sign block is not correct.
- Fast building tool does not correctly draws the outline of fluid.
- Slab tool may drop abnormally when mining.
- (For versions above 1.19) Added hung sign, sign bar, wall sing and handrail for made of mangrove.

### 0.1.7

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
- As the heavenly Mojang annotates Block.getName as `@Environment(EnvType.CLIENT)` (only versions before 1.16.5), some tools were switched to client-side usage.
    - Data tag tool still uses server data, but server sends the NBT now, instead of the prettified text.
- Now sign blocks support json texts. You can type `-json <a json>` in a text box, e.g., `-json {color:red,text:Hi}`.
- Added text copy tool, which can be used to copy and paste texts, and allows copy and paste between vanilla signs and mod signs.
- Added the button to flip texts in a sign edit screen, which can be used to flip one line or all lines of text.
- Improved the logic of road connection state debugging tool. Now can view line types.
- Improved some translations according to word-usage and format of vanilla language files.

Fixed following issues:

- Force placing tool and fast building tool may not correctly handle waterlogging, and may not see item tags on the offhand item.
- Force placing tool can remove entity even if you are not in Creative Mode.
- When setting formats for sign text lines, formats may be applied to text itself when rendering.
- In multiplayer mode, some NBT prettified text may lose clicking actions when transmitted to client side, as these clicking action is defined by the mod and cannot be serialized.
- Some data of signs may be stored even if there is no text.

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