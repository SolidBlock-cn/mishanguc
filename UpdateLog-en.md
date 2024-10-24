## Update Log

Note: Not all versions in this update log are already published. Please refer to relevant pages in CurseForge and Modrinth, or the "releases" section in the GitHub.

### 1.4.1

- Fixed the issue that road painting recipes are lost.

### 1.4.0

- No longer depends on Better Runtime Resource Pack mod.
- Fixed the issue of crash when on-stair handrail blocks are above handrail blocks.
- Added full light blocks, light stairs, etc., into block and item tag `#mishanguc:lights`.
- Added all stairs into block and item tag `#minecraft:stairs`.
- When determining shape upon placing handrail blocks, only stairs of bottom half (regular direction) will affect the handrail's shape, position and direction, and stairs of top half (upside-down direction) will not affect anymore.
- Added more handrail blocks.
    - Added glowing variants for glass handrails of iron, gold, emerald, diamond, netherite and lapiz, which requires a colored light block when dyeing. (At present, there are no such blocks without custom coloring. You can color them in white.)
    - Added glass bamboo handrails with custom coloring, and various handrails of stripped wood and stripped bamboo. These handrails support custom coloring.
    - Added packed ice and blue ice handrails with custom coloring, which requires colored snow blocks when crafting. Non-custom-colored blocks require ordinary snow blocks when crafting.
- The Color of custom-colored blocks can be changed through dyes.
- When modifying block colors with color tools or dye, sounds will be played.
- Fixed the issue that when a wall sign block is next to a full wall sign block, some sides are accidentally invisible.
- (For 1.20.5 and above) Added more color tools, which have different opacity or mixture type.
    - Opacity is controlled by component `mishanguc:opacity`, where the type is float. Provided in the creative inventory are tools with opacity 0.5, 0.25 and 0.1.
    - Mixture type is controlled by component `mishanguc:color_mixture_type`, and supports the following values:
        - `normal` (default): Normally changes block color.
        - `random`: Randomly sets block color.
        - `invert`: Sets block color to an opposite color.
        - `hue`: Set the hue of block color to the hue of a specified color, and saturation and luminosity remains.
        - `hue_and_saturation`: Set the hue and saturation of block color to hue and saturation of a specified color, and luminosity remains.
        - `hue_rotate`: Rotates the hue of block color. When using while holding Shift, the rotation will be in an opposite direction.
        - `saturation_change`: Increases the saturation of block color. When using while holding Shift, the saturation will be decreased.
        - `brightness_change`: Increases the luminosity of block color. When using while holding Shift, the luminosity will be decreased.
        - In the inventory, except for `normal` which provides multiple tools with different opacity, each of the above provides one tool.
    - Amount of modification applied to color is controlled by component `mishanguc:color_change_amount`, where the type is float, and is used to specify the amount of modification to hue, saturation or luminosity. In the items in creative inventory, hue rotation amount is 1/24 by default, and saturation and luminosity's modification amount is 0.1 by default.
    - Note: Non-custom-colored blocks which can be converted to custom-colored blocks, when converting custom-colored blocks, takes the *map color* of the original block as an initial color, so it is normal its color differs from the color presented in custom-colored blocks.
    - Note: If you decrease the saturation to 0 (gray) with color tool that modifies saturation, or change the luminosity to 0 (black) or 1 (white) with color tool that modifies luminosity, then the color's hue will be 0 or 1 (red), and saturation will be 0. For example, if you decrease the saturation to 0, and then increase it, it will only be red. If you change luminosity to 0 or 1 and then increase or decrease it, it will be only gray.
- Now you can select multiple lines when editing signs.
    - Select text box while holding Ctrl to multi-select. Select selected text box while holding Ctrl to deselect it.
    - Select text box while holding Shift to select continuous multiple text boxes at once.
    - It is supported to operate multiple lines at ones, including adding, deleting, modifying styles or inputting text. If you increase or decrease their size, position coordinate, rotation, the modification is applied relatively to multiple lines. For properties such as color, or setting custom values, multiple lines will be set the same value.
    - The Tab-key behavior when selecting multiple lines may be unstable.
- Fixed the issue that players in Adventure Mode using slab tools with `CanDestroy` NBT tag (or component) cannot destroy blocks correctly.

### 1.3.5-beta.2

- Fixed the issue that modification caused by color tool modifying a block color may not be saved.

### 1.3.5-beta.1

- Fixed the potential block entity issue that results from colored glass handrail blocks.

### 1.3.4

- Fixed the issue that tp tools may fail to teleport.
- Fixed the issue that some tools cannot operate blocks at a far distance after modifying attributes.

### 1.3.3

- Fixed the issue that the tooltip of 'rearrange' button and 'clear' button in the sign edit screen is incorrect.
- Fixed the issue that text cannot be correctly selected in the sign edit screen.
- When holding invisible signs, the outlines of placed ordinary invisible signs will be drawn in cyan, while those of glowing invisible signs are still yellow.
- Removed the usage of deprecated `-texture_beta`.
- Fixed potential `NullPointerException` on editing standing signs.
- Fixed the issue that the Tab key cannot correctly select buttons in the sign edit screen.

### 1.3.2

- Fixed the issue that road blocks cannot be used normally under circumstances other than water cauldron.
- Fixed the issue that some tools can be used normally in Spectator Mode.

### 1.3.1

- Fixed the issue that the colors displayed on maps of some road blocks are incorrect.
- Fixed the issue that invisible signs cannot be broken in Survival Mode while they can be crafted and placed.
- Adjusted recipes.
    - The output count of invisible signs has been changed from 6 to 9.
    - The output count of light cover block has been changed from 6 to 8.
    - The output count of small light tube has been changed from 16 to 64.
    - The output count of medium light tube is 32.
    - The output count of large light tube has been changed from 12 to 16.
    - The output count of thin strip light tube has been changed from 12 to 16.
    - The output count of double strip light tube has been changed from 10 to 18.
    - The output count of thick strip light tube has been changed from 8 to 12.
    - The output count of thin column light tube is 32.
    - The output count of medium light tube is 16.
    - The output count of thin light tube is 8.
    - The output count of light decoration block has been adjusted to: 18 for points, 15 for simple, 12 for rhombus of hash shape, 9 for round.
    - Now you can craft road blocks and road slab blocks with line painted into road blocks and road slab blocks without line painted via a stonecutter.
    - Now you can craft hun sign bars in a stonecutter, one base block for 20 pieces.
    - Now you can craft road blocks and light blocks into slabs in a stonecutter.
    - All road blocks and slabs with marks can be crafted in a crafting table from base road blocks or slabs and corresponding dyes (supporting dye tags in Fabric Conventional Tags).
    - In the crafting recipe of slab tool, stone has been adjusted to item tag `#c:stones` from Fabric Conventional Tags.
    - Added recipes for handrail blocks. Simple handrails can be crafted from the base block in the stonecutter (including non-stone blocks). Glass handrails need to be crafted on the crafting table.
    - Now you can craft a road mark block from a white dye in a stonecutter.
- Adjusted and fixed tags.
    - Fixed the issue that block tag `#mishanguc:lights` lacks some light blocks.
    - Added `mishanguc:colored_light` into `#mishanguc:lights`.
    - Fixed the issue that tags such as `#mishanguc:simple_handrails` lack some blocks.
    - Fixed some missing mineable block tags.
    - Added some blocks to tags of Fabric Conventional Tags, such as some tags for dyed blocks.
- Now you can wash out the lines of a road block or slab in a cauldron (consuming one level water).
- Fixed the issue that when breaking with an omnipotent tool, the block may not be dropped.
- Now some of the items of this mod can be enchanted directly.

### 1.3.1-beta.1

Updated to fit 1.20.5 and 1.20.6.

### 1.3.1-alpha.1

This update is limited to 1.20.5. Changed the data structure to fit into item component forms. There are many contents undone in this version.

### 1.3.0

- Honeycomb can be used to wax signs (those in this mod only). In Creative Mode, honeycomb can remove wax on sign. Wax cannot be removed under non-Creative Mode. Waxed signs cannot be edited, nor be made glow of removed the glow.
- Using glowing inc sac can make texts on signs glow. Inc sac can remove the glow on the texts. Glowing texts will be displayed on max luminance in any dim places, but the outline of texts is not affected. When the texts are black or the sign is in bright places, glowing texts may not make an obvious difference. Glowing texts also do not affect the brightness level of blocks.
- For hung sign and standing sign, text glowing and waxing are handled respectively on the two sides. Under non-Creative Mode, each success operation will consume one honeycomb, inc sac or glowing inc sac.
- Adjusted some language files, especially Traditional Chinese (Hong Kong SAR).
- Fixed the compatibility issue with Sinytra Connector.

### 1.2.7

- Fixed the issue of exceptions on dedicated server with Fabric Loader above 0.15.
- Swapped the buttons on the sign edit screen of rearranging and clear.

### 1.2.6

- Fixed the issue that column building tool may crash the dedicated server.

### 1.2.5

- Fixed the issue that text properties may be lost if they are copied from a wall sign with a text copy tool.
- Fixed the issue that other players do not see changes after editing a sign.
- (For 1.20.2) Removed the feature of walking accelerated on road blocks as well as relevant game rules.

### 1.2.4

- Fixed the issue that after deleting a row of text using Backspace in the sign edit screen, the no remaining text is selected.
- Fixed the issue of impossibility in the sign edit screen to set custom values for properties of texts except the last line.
- Modified the display name of glass handrail blocks.
- Added three types of signs of various wood and stripped wood, and sign bars of stripped wood.
- Fixed the issue that standing signs of nether woods are flammable.
- Adjusted the sequence of some standing signs in the inventory to conform to the sequence of difference wood types in vanilla.
- Added simple plank handrails.
- Added nether wood framed glass handrails (including those with plank texture decoration and those with customizable colored decoration).
- Added the obsidian framed and crying obsidian framed handrails with decorations in different textures.
- Added glass handrails with frame of stone, cobblestone, mossy cobblestone, lapis, packed ice and blue ice.
- In the sign edit screen, when there are no texts added, press Enter to add texts.
- Adjusted the display in the sign edit screen, avoiding scroll bars occupying the place of text fields, and fixing the issue that user cannot scroll by dragging the scrollbar.

### 1.2.3

- Fixed the issue that handrail blocks on upside-down stairs are also displayed in the form of stairs.
- Adjusted the edit screen of signs. Optimized the content and display of tooltips of buttons.
    - The numbers now support custom values, including text color and outline color.
    - Adjusted the display of hex color values. When an alpha channel exists, the hex color displays 8 digits instead of 6.
    - When editing the text color and outline color, similar to the custom text color in previous versions, color names and hex colors are supported. Besides, for outline colors, now special keywords `auto` and `none` are supported. Text color names and the special keywords above now support displaying suggestions.
    - The tooltip of buttons contains shortcuts.
    - Supports hiding the GUI to preview changes on the sign.
    - When using mouse wheels to adjust text size, X scale or Y scale, mouse wheel scrolling up leads to sizing up or scaling up now, which conforms to the user convention.
    - Within the tooltip of the buttons, numbers that equal to integer values do not show decimal parts.
- Adjusted the display of some items in the creative inventory. Some adjustable parameters are not added repetitively, saving more room.
- Fast building tools now display its size of range in the name.
- Growth tools affect the size of slimes (and magma cubes) now.
- Growth tools can be used in dispensers.
- Force placing tools now suppress the `onBlockAdded` method. For example, you can directly place fluids without concerning it may flow.
- Force placing tools and fast building tools can identify in off-hands flint and steel (to place fire) and bucket (to place fluid).
- Added colored glass panes.
- Colored glass, colored glass pane, colored ice and colored portals can now affect beacons (depending on block tag `mishanguc:tints_beacon_beams`).
- Adjusted the placement of slab blocks to avoid in same caves when using fast building tools to place slabs, the operation is incorrectly considered to double the slab.
- Added column building tool.
- Modified the mod's description.
- (For versions 1.19.4 and above) Fixed the issue that cherry leaves use the texture of acacia leaves.
- (For versions 1.19 and above) The block tag `#snow_layer_cannot_survive_on`, instead of mixins, is used to prevent snow placing on road blocks.

### 1.2.2

- Fixed the issue that road mark blocks are not displayed in the creative inventory.
- (For 1.20 only) Removed features related to light update, as well as relevant commands, because the former content does not apply to newer versions anymore.
- (For 1.20 only) The speed effect works even if you stand on the edge of the road block (which is also modifiable via game rules).

### 1.2.1

- Fixed the wrong model of road slab blocks.
- Fixed the issue that using road connection state debugging tool may cause crashes.
- Added more links in the Mod Menu screen.

### 1.2.0

- Adapted to new version BRRP.
- Added localized mod name in the display of Mod Menu (may not support old versions of Mod Menu).
- Removed game rule `mishanguc:warn_deprecated_version`.
- Added a tooltip for road blocks about the speed.
- Fixed the issue that special texts are not correctly aligned when X-scale is set.
- Adjusted the logics of buttons of the sign edit screen. Modifying the values using mouse buttons, mouse wheel or the keyboard will be more intuitive.
- Added the narration in the sign edit screen.
- In the inventory, sorted the invisible signs at the front.
- Adjusted the display of tooltips of text outline settings.

### 1.1.1

- Fixed the severe issue of failing to launch server.
- Realized the control of keyboard only to the sign edit screen.

About the keyboard control added in 1.19.4: Minecraft added the keyboard-only control for the sign edit screen. When you are using only keyboard, you can edit the sign text via the following methods:

- Press `Tab` to switch between text area and button area (including the text field to change custom color). Press direction keys to switch between text rows or between buttons.
    - For example, you're editing the first row. Press `Tab` to select "Bold" button, press `Enter` to switch bold, then press `right` to select "Italic" button, press `Enter` to switch italic; then, press `Tab` again to go back to text area, type anything to modify the content of text, and press direction key to switch to another row of text.
- Press `Ctrl + I/S/U/O` to fastly toggle the italic, strikethrough, underline and obfuscation. Note that pressing Ctrl + B cannot apply bold, because it conflicts with the hotkey of narrator.
- Press `Ctrl + Shift + Equal` or `Ctrl + KP_Add` to add a row. Press `Ctrl + Minus` or `Ctrl + KP_Minus` to remove that row.
- Press `Ctrl + Shift + Up/Down` to move the current line.

### 1.1.0

Updated the following content:

- Added features related to block light.
- Adjusted the order of light blocks。
- Now the same and connected strip light can have invisible face culled.
- Adjusted the outline shape of large wall light and light tube to encompass the whole look.
- Added thick stripped light; in former versions there were only stripp light tubes.
- Added medium wall lights and light tubes.
- Added column lights, column light tubes, light slabs, and light covers.
- Tweaked the texture of three types of lights.
- Added light round decorations for other colors; in former versions there was only one for white.
- Wall lights can be placed on blocks with an empty side shape (such as with empty collision shape).
- Added orange, green, and pink lights.
- Added multiple road blocks, such as road with angle line with two parts offset, roads with T-shaped line with offset side, road with two bevel angle lines, and roads with different-color double lines.
- The `color` field of NBT data of color tool, colored blocks, sign texts, now supports multiple formats, including:
    - integers, such as `16777215`.
    - texts indicating the text color, such as `"red"`.
    - array, in the order of RGBA, such as `[0, 255, 0]`.
    - objects, such as `{signColor: red}`, `{fireworkColor: red}`, and `{mapColor: red}`.
- Optimized the code, including code related to data generation and block registration.
- Roads with auto lines can more smartly handle offsets of line.
- Roads with auto lines, when generating lines, can catch the exceptions if there are any thrown.
- Changed the model of `mishanguc:block/road_with_angle_line`. Now the two sides on west and east use texture `#lineSide` and the south side uses `#lineSide2`.
- Adjusted the distance limit of tp tool from 64 blocks to 256 blocks.
- Added game rule `mishanguc:road_boost_speed` to adjust the speed when stepping on road blocks, which defaults to 1.75.

### 1.0.4

Updated the following content:

- Handrail blocks may connect to fences, fence gates, walls and glass panes.
- Adjusted the display format of messages of Text Copy Tool.
- Slab tools can break down blocks into non-vanilla slabs.
- Added blocks made of block of bamboo, bamboo planks, and bamboo mosaic, which require the 1.20 feature data packs of Minecraft to be enabled.
- Color tools can more smartly handle text styles, including styles in the vanilla signs.

Fixed the following issues:

- Some road blocks have wrong textures.
- Some handrail blocks may incorrectly make their textures hidden when connecting.
- Simply warped stem handrail has a wrong name and block property.

### 1.0.3

Fixed the following issues:

- Client and server sides may differ when breaking with slab tool.
- The default text size of wall signs is incorrect.
- Text scale is not considered in the calculation of text height and width, causing when the scales are not default, text alignment is incorrect.
- Possible crash when placing handrail blocks with fast building tool.
- When holding blocks in offhand, using fast-building tool, placing may cause bugs (such as placing on slabs when you're holding non-slab blocks, the player is incorrectly considered to be doubling the slabs and will incorrectly replace the slabs with blocks in hand).

### 1.0.2

Updated the following content:

- Glowing hung signs and glowing wall signs with customizable colors.
- Added more signs with custom colors, and optimized the source.
- Standing signs.
- Added information tooltip for sign block items.
- Road with bevel angle double or thick line.
- Set the texture of bar part of ice hung sign to blue ice.

Fixed the following issues:

- Tinting a terracotta hung sign bar with a color tool converts it into a sign instead of sign bar.
- If an opaque sign block connects with a translucent sign, the opaque face will be incorrectly invisible.
- Some wrong code in the hung sign models.
- Incorrect configuration of auto lines of roads.

### 1.0.1

Fixed the following issues:

- When fetching NBT with a data tag tool, clicking the "expand" in the result does not work.
- Typo in Chinese translation.
- Incorrect text height in hung sign blocks.
- Wrong display name of full wall signs.
- Wall signs cause the server to error.

### 1.0.0

Updated the following content:

- Adjusted the texture of carrying tool.
- Added game rules to specify which players are those only who can use force placing tool, explosion tool or carrying tool. When the player has no permission to use, the player will get informed.
- Attacking a player with force placing tool will directly kill, instead of causing strange behaviours. Carrying tools cannot be used on players.
- The message that the mod is not stable will no longer be sent. When using 1.19 or 1.19.1, a notice will be sent that the version will be no longer supported later; you can set game rules to suppress the notice.
- Non-OPs cannot handle operator blocks with most items in this mod.
- Growth tool no longer works as bone meal.

Fixed the following issues:

- In 1.16.5, when placing force placing tools and fast building tools, block entities are not correctly updated.

### 0.2.4

Updated the following content:

- Added introduction (tooltip) for road blocks, and fixed some too ambiguous names.
- Modified the notice on player entering a world.
- Added more road blocks, such as roads with different straight and bevel lines, and half-double line.
    - Added a "bevel_top" property for roads with straight and bevel lines of different colors.
- Slab tools can handle road blocks of this mod.
- Added text describing the lines for road blocks, as well as a tooltip of speed-up on the road.
- Resorted roads and their slab blocks.
- Renamed the Chinese name of some road blocks.
- Added growth tool, carrying tool and road tool.
- When breaking a handrail block composed of two ones, no longer leave one.
- Added more colored blocks, including colored leaves, colored andesite.
- Adjusted the durability of tp tool, and the durability worn on each tp depends on the distance moved.
- Adjusted and optimized the generation logic of automatic line on roads, in order to handle some blocks newly added.
- Added framed glass handrail blocks, allowing custom tinting. Adjusted the culling between handrail blocks.
- Adjusted the display of handrail blocks in inventory.
- Changed item id: black_stone_hung_sign → blackstone_hung_sign.
- Added some vanilla-colored blocks to vanilla tags. For example, colored wool can be sheared and can dampen vibrations like vanilla wools.

Fixed the following issues:

- Tools needed when breaking block are not well configured.
- Force-placing tool and fast-building tool do not sync data when replacing block entities with the same block state.
- The item model of color tool is not based on a handheld item model.

### 0.2.3

Updated the following content:

- Added colored blocks. Colored blocks can be tinted any color; the color on placed depends on where placed. If picked with pressing your mouse wheel, the color of that item will be fixed.
    - Colored signs and sign bars are included.
    - Colored glasses do not tint beacon beams.
    - Colored ice becomes simple water after smelting.
    - Relevant block and item tag `mishanguc:colored` is also added.
    - Colored block displays its nearest `MapColor` when displaying on a map.
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

Fixed the following issues:

- In the text edit screen, the display may differ when moving selection.
- Cannot edit the custom color of texts.
- Potential error when adding or removing text.
- When the text color is dark, the text color tooltip in the text-editing screen may look unclear.
- When copying text from the back, if there is no text on the back, crashes might be caused.
- Text fields on the text edit screen are not correctly rendered.
- The Tab order in the text edit screen is not correct.

### 0.2.1

Updated the following content:

- Text copy tools now displays its copied content in the item name.
- Adjusted the outline shape of hung signs and hung sign bars, which is slightly wider than their collision box, but narrower than former outline shape when holding hung signs or hung sign bars. Now the outline shape will not be wider when you hold a hung sign or bar than when not.

Fixed the following issues:

- Hung sign and hung sign bar blocks are not mirrored correctly.
- When holding some tools, the ShapeContext is ignored when drawing outline.

### 0.2.0

Updated the following content:

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
- Adjusted the display of mod info. It displays only when a player logs into the world, and will not display again when the player respawns or switches dimension.
- No longer allow ID checker tools and data tag tools have effect when in Spectator Mode. Now when you hit entities in Spectator Mode holding this item, you will spectate it as in other cases.
- Optimized the rendering of text.
- (For versions above 1.19) Added hung sign, sign bar, wall sing and handrail for made of mangrove.

Fixed the following issues:

- When displaying values of enum properties using block state tool, the result is not the result of `StringIdentifiable#asString`, but directly the name of enum value in the code.
- When placing a sign bar above a hung sign, the sign bars above it do not update.
- When running on a dedicated server, switching the matching rule of fast building tool causes crashes.
- The height of the display range of full wall sign block is not correct.
- Fast building tool does not correctly draw the outline of fluid.
- Slab tool may drop abnormally when mining.
- Adventure Mode players can edit hung signs.
- The outline rendered does not match the real situation when holding items in offhand. (Note: When holding items in offhand and main hand is empty, you can trigger "use" but cannot trigger "attack" or "break".)

### 0.1.7

Updated the following content:

- Added more types of signs, such as wooden signs, ice signs.
- Added handrails. Handrails have multiple styles.
- For force placing tool and fast building tool, in non-water-including mode, if using on waterlogged blocks, then whether the placed block is waterlogged is decided on whether the block at that position before the placement is waterlogged. For example, duplicating a waterlogged block to a non-waterlogged place, the duplicated block is not waterlogged.
- In bi-hand mode, when using force placing tool and fast building tool, the block placed may be influenced by tags `BlockStateTag` and `BlockEntityTag`, to specify the block state and block entity of the block after placing.
- Renamed most road blocks, to solve the problem that names are too long. As old names are no longer recognized,
    *
  *if
  you
  used
  road
  blocks
  previously,
  after
  updating
  this
  mod,
  previous
  road
  blocks
  will
  disappear!
  ** After updating the mod before entering the world, remember to back up the world.
- Added yellow and cyan light blocks, and added stripped light blocks with a background.
- Adjusted the models of light blocks in the inventory.
- Optimized the process of runtime data generation (including resource pack and data pack). Now more data is generated on runtime. Some block states, due to limitations of ARRP mod, still store JSON files in a mod file.
- Added more block tags.
- Fixed the issue that data tag tool has no tooltip.
- As the heavenly Mojang annotates Block.getName as `@Environment(EnvType.CLIENT)` (only versions before 1.16.5), some tools were switched to client-side usage.
    - Data tag tool still uses server data, but server sends the NBT now, instead of the prettified text.
- Now sign blocks support JSON texts. You can type `-json <a json>` in a text box, e.g., `-json {color:red,text:Hi}`.
- Added text copy tool, which can be used to copy and paste texts, and allows copy and paste between vanilla signs and mod signs.
- Added the button to flip texts in a sign edit screen, which can be used to flip one line or all lines of text.
- Improved the logic of road connection state debugging tool. Now can view line types.
- Improved some translations according to word-usage and format of vanilla language files.

Fixed the following issues:

- Force placing tool and fast building tool may not correctly handle waterlogging, and may not see item tags on the offhand item.
- Force placing tool can remove entity even if you are not in Creative Mode.
- When setting formats for sign text lines, formats may be applied to text itself when rendering.
- In multiplayer mode, some NBT prettified text may lose clicking actions when transmitted to the client side, as these clicking action is defined by the mod and cannot be serialized.
- Some data of signs may be stored even if there is no text.

### 0.1.6

Updated the following content:

- Invisible glowing signs.
- For Minecraft version Minecraft 1.17 and above, you can set text outlines.
- Improved sign edit screen. You can adjust the order of lines and cancel the edit.
- Adjusted the logic of rearranging texts. Now each line keeps a margin above and below, the size of which equals to 1/8 of the line.
- Magma creams can be used to quickly rearrange texts.
- Added zh-tw and zh-hk.
- Adjusted the default text size of wall sign blocks.
- Fast building tools allow quickly switching matching mode.
- No items will drop when using force placing tool to break a chest.

Fixed the following issues:

- In game modes except Creative, the fast-building tool and force-placing tool are still available.
- In Survival mode, holding some tools and left-click may execute multiple times.
- Rearrangement multiple of texts is sometimes incorrect.
- Even in Survival mode, breaking slabs with slab tools does not drop stacks.
- Sign edit screen is too crowded in some languages, for example, English.
- Hung sign bars, when placed in water, are not by default waterlogged.
- Incorrect models of some light decoration blocks.

### 0.1.5

Updated the following content:

- ID checker tool can now be used to see biome iD.
- Optimized the NBT display when triggered.

Fixed the following issues:

- Some tools are executed on both client and server side.
- Game instance starts normally when ARRP is not installed.

### 0.1.4

First publish.