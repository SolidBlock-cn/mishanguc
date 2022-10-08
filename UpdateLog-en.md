## Update Log

Note: Not all versions in this update log are already published. Please refer to relevant pages in CurseForge and Modrinth, or the "releases" section in the GitHub.

### 1.0.1

Fixed following issues:

- When fetching NBT with a data tag tool, clicking the "expand" in the result does not work.
- Typo in Chinese translation.
- Incorrect text height in hung sign blocks.
- Wrong display name of full wall signs.
- Wall signs cause the server to error.

### 1.0.0

Updated following content:

- Adjusted the texture of carrying tool.
- Added game rules to specify which players are those only who can use force placing tool, explosion tool or carrying tool. When the player has no permission to use, the player will get informed.
- Attacking a player with force placing tool will directly kill, instead of causing strange behaviours. Carrying tools cannot be used on players.
- The message that the mod is not stable will no longer be sent. When using 1.19 or 1.19.1, a notice will be sent that the version will be no longer supported later; you can set game rules to suppress the notice.
- Non-OPs cannot handle operator blocks with most items in this mod.
- Growth tool no longer works as bone meal.

Fixed following issues:

- In 1.16.5, when placing force placing tools and fast building tools, block entities are not correctly updated.

### 0.2.4

Updated following content:

- Added introduction (tooltip) for road blocks, and fixed some too ambiguous names.
- Modified the notice on player entering world.
- Added more road blocks, such as road with different straight and bevel lines, and half double line.
    - Added a "bevel_top" property for road with straight and bevel lines of different colors.
- Slab tools can handle road blocks of this mod.
- Added text describing the lines for road blocks, as well as a tooltip of speed-up on the road.
- Resorted roads and their slab blocks.
- Renamed the Chinese name of some road blocks.
- Added growth tool, carrying tool and road tool.
- When breaking a handrail block composed of two ones, no longer leave one.
- Added more colored blocks, including colored leaves, colored andesite.
- Adjusted the durability of tp tool, and the durability worn on each tp depends on the distance moved.
- Adjusted and optimized the generation logic of automatic line on road, in order to handle some blocks newly added.
- Added framed glass handrail blocks, allowing custom tinting. Adjusted the culling between handrail blocks.
- Adjusted the display of handrail blocks in inventory.
- Changed item id: black_stone_hung_sign → blackstone_hung_sign.
- Added some vanilla colored blocks to vanilla tags. For example, colored wool can be sheared and can dampen vibrations like vanilla wools.

Fixed following issues:

- Tools needed when breaking block are not well configured.
- Force-placing tool and fast-building tool do not sync data when replacing block entities with the same block state.
- The item model of color tool is not based on handheld item model.

### 0.2.3

Updated following content:

- Added colored blocks. Colored blocks can be tinted any color; the color on placed depends on where placed. If picked with pressing your mouse wheel, the color of that item will be fixed.
    - Colored signs and sign bars are included.
    - Colored glasses do not tint beacon beams.
    - Colored ice becomes simple water after smelting.
    - Relevant block and item tag `mishanguc:colored` is also added.
    - Colored block displays its nearest `MapColor` when displaying on map.
- Some block items can be burnt in furnace.
- Changed the id of hung sign block entity type, with the limited compatibility for the old ID.
- When adding a text in the text edit screen, if any text is selected, the added text format is the same as the selected one, instead of default format.
- The `-pattern` option has more pattern names, including `ulb`, `urb`, `ult`, `urt`.
- Slightly enlarged the display size of `-pattern circle` and `-pattern ban`.
- You may type `-texture_beta <textureLocation>` in text edit screen, which is just temporary.
- Updates some language files.
- Added tooltips for some buttons in the text edit screen.
- In the text edit screen, the text guidance when no texts are added is optimized.
- Slightly enlarged the hung sign block to keep consistent with wall sign block.
- When holding text copy tool, outlines will not be drawn for blocks that do not support text copying.
- Added color tool, which can be used to pick color from block and apply the color on the block.

Fixed following issues:

- In the text edit screen, the display may differ when moving selection.
- Cannot edit the custom color of texts.
- Potential error when adding or removing text.
- When the text color is dark, the text color tooltip in the text-editing screen may look unclear.
- When copying text from back, if there is no text on the back, crashes might be caused.
- Text fields on the text edit screen are not correctly rendered.
- The Tab order in the text edit screen is not correct.

### 0.2.1

Updated following content:

- Text copy tools now displays its copied content in the item name.
- Adjusted the outline shape of hung signs and hung sign bars, which is slightly wider than their collision box, but narrower than former outline shape when holding hung signs or hung sign bars. Now the outline shape will not be wider when you hold a hung sign or bar than when not.

Fixed following issues:

- Hung sign and hung sign bar blocks are not mirrored correctly.
- When holding some tools, the ShapeContext is ignored when drawing outline.

### 0.2.0

Updated following content:

- Licence switched to LGPLv3.
- Use BRRP as the dependency, instead of former ARRP.
- Added custom colored sign blocks, but as it is not stable yet, it is not formally added.
- Added crafting recipe for some content.
- Adjusted the texture of road block.
- No longer allow snow placed on road blocks (blocks with `mishanguc:roads` tag), to prevent the situation roads can't be seen in snowy days.
- Added `-rect` and `-pattern` feature or sign blocks. Clicking a sign holding slime ball can replace arrow characters with `-pattern` form. Clicking holding a slime ball can replace the block entities of the same type in a whole chunk.
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
- No longer allow ID checker tools and data tag tools have effect when in Spectator Mode. Now when you hit entities in Spectator Mode holding this item, you will spectate it as in other cases.
- Optimized the rendering of text.
- (For versions above 1.19) Added hung sign, sign bar, wall sing and handrail for made of mangrove.

Fixed following issues:

- When displaying values of enum properties using block state tool, the result is not the result of `StringIdentifiable#asString`, but directly the name of enum value in the code.
- When placing a sign bar above a hung sign, the sign bars above it do not update.
- When running on a dedicated server, switching the matching rule of fast building tool causes crash.
- The height of display range of full wall sign block is not correct.
- Fast building tool does not correctly draw the outline of fluid.
- Slab tool may drop abnormally when mining.
- Adventure Mode players can edit hung signs.
- The outline rendered does not match the real situation when holding items in offhand. (Note: When holding items in offhand and main hand is empty, you can trigger "use" but cannot trigger "attack" or "break".)

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