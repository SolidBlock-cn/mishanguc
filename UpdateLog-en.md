## Update Log

Note: Not all versions in this update log have been published yet. Please refer to the relevant pages on CurseForge and Modrinth, or the "Releases" section on GitHub.

### 1.6.5

- Fixed the issue that the default text size in the texture presets is 8 and 4 instead of 6 and 3.
- Fixed the issue that the tooltips of presets show their names instead of their IDs in the `/mishanguc:signpreset list` command.
- Fixed the issue of incorrect focus in the sign edit screen in 26.1.2 when adding and removing text lines.
- Further optimized the implementation of Color Tool, Text Copy Tool, and Carrying Tool. The validation logic now runs on the client. When validation fails, it neither swings the player's hand nor sends a request to the server. The server still performs the validation.
- Improved the implementation of Growth Tool. If no mobs are affected (including when their age is modified, frozen, or unfrozen), no hand-swing animation will be displayed.
- Adjusted how Growth Tool handles slimes, magma cubes, and sulfur cubes. Each use now increases or decreases their size by 1 instead of doubling or halving it, and their size can be set to at most 8. Sulfur cubes are set to their baby form only when their size is set to 1.
- Improved the rendering of entity outlines for Data Tag Tool, Carrying Tool, and Force Placing Tool when targeting entities. Now, when an entity moves, its rendered outline follows the movement smoothly.
- Fixed the issue of incorrect translation args in overlay messages when obtaining blocks and overriding obtained entities using Carrying Tool.
- Fixed the issue when catching and placing blocks with block entities, the block entity data may not be loaded or the data is loaded but overridden by irrelevant item components.
- Fixed the issue of Column Building Tool were not correctly using block entity data when holding a block with block entity data on the offhand.
    - Now, when holding a block with block entity data (such as a banner with patterns, a sign with texts on, a custom-colored block with a specified color), or holding a Carrying Tool with a block caught, and using Force Placing Tool, Fast Building Tool or Column Building Tool on the mainhand, the block entity data of the block on the offhand is normally used.
    - Please note: if the block on the offhand is a custom-colored block with an automatic color (such as ones fetched in the creative inventory), the blocks placed with the tools above may not be colored correctly.
    - Column Building Tool may have slight changes.
- For 26.2 versions:
    - The sulfur cube archetype for full light blocks is `regular`.
    - The sulfur cube archetype for road blocks is `slow_bouncy`.
    - Custom colored blocks (except transparent, translucent and incomplete blocks) have the same sulfur cube archetype as their base blocks.
    - When the custom colored block is put into a sulfur cube, the color will be identical as in the inventory. If the color is not specified, it varies with time. If color is set, the color is used.

### 1.6.5-alpha.2

- Fixed the issue that in 1.21.1 when using NeoForge and Sinytra Connector, blocks may be broken accidentally when left-clicking while using some tools (for example, ID Checker Tool, Data Tag Tool, Color Tool).

### 1.6.5-alpha.1

- Fixed the issue that the mod is incompatible with Forge/NeoForge and Sinytra Connector in 1.20.1 and 1.21.1.
    - In 1.21.1, in the `method` method in the mixin annotation, no matter the specified value is a single method name `findCrosshairTarget` (Yarn mapping), or a full method signature, the intermediary name remapped by Yarn only contains method name `method_56153`, and then when remapped by Sinytra Connector into official mapping, the method name is `pick`. In Yarn mapping, in the `GameRenderer` class, the method named `findCrosshairTarget` is only one, but in the official mapping, in the `GameRenderer` class, the methods named `pick` are more than one (corresponding to `findCrosshairTarget` and `updateCrosshairTarget` in the Yarn mapping), so the target cannot be correctly located, causing the mixin to encounter errors. To solve this issue, I added `target = @Desc(...)` into the code to help the game correctly find the method to apply mixin.
  - In 1.20.1, the `BeaconBlockEntity` class has only one method named `tick` in both Yarn and official mappings, and Yarn correctly remaps it to `method_16896`. However, in the mod remapped by Sinytra Connector, the method name is still `method_16896`, which is abnormal in Forge. To solve this issue, the relevant mixins have been set to be silently ignored, so exceptions will not be thrown when they are found, and the relevant mixins will not be applied in this case. This also means that, in 1.20.1, when using Forge/NeoForge and Sinytra Connector to run Mishang Urban Construction, custom-colored glass blocks and other blocks cannot tint beacon beams. If Sinytra Connector is updated in the future, this issue may be resolved.

### 1.6.4

- Fixed the issue of wrong outline shape of handrail block of the central shape.
- Improved the implementation of color tool.

### 1.6.4-beta.1

- Fixed the issue of wrong outline shape of handrail block of the central shape.

### 1.6.3-beta.6

- Improved the implementation of color tool.

### 1.6.3-beta.5

- Now the dyeing logic of color tool runs only on the server.
- Fixed the issue that prevented the server from launching in versions 26.1 and above.

### 1.6.3-beta.4

- Fixed the issue that the mixins of this mod may not run.
- Improved the rendering of beacon beams. Now all beacon beam colors caused by colored blocks of this mod have alpha 255. Before this, the beacon beams may not be rendered correctly with shaders on.
- Fixed the issue that colored stone and colored nether portal are not correctly tinted in the inventory. The issue is because this mod did not correctly use item model mappings introduced in Minecraft 1.21.4.

### 1.6.3-beta.3

- Fixed the issue that explosion tool may use offhand items.

### 1.6.3-beta.2

- Fixed the issue that text copy tool may use offhand items.

### 1.6.3-beta.1

- Optimized operation of carrying items on both hands: when using Growth Tool, Carrying Tool, Tp Tool and Color Tool, any items on offhand (including blocks) will not be used.
- Fixed the issue of sign edit screen containing backgrounds in versions since 26.1.
- Now in 26.1, when holding a golden dandelion in offhand, using Growth Tool on an age-lockable living entity may set or unset its age lock (setting to baby to lock age, and setting to adult to unlock age).

### 1.6.2-beta.1

- Support the new version 26.1.
- Fixed the issue that point-of-interest types that did not support colored nether portal blocks caused a new uncolored nether portal to be generated after entering and returning from a colored nether portal.

### 1.6.1

- Fixed the issue of `-pattern cross-large` and `-pattern square-slant-medium` and `-pattern square-slant-small` being incorrect.

### 1.6.0

- Fixed the issue of wrong operating logic of the text outline control button on the sign edit screen.
- The shade option in the sign edit screen is no longer seen as not recommended enabling.
- Simplified the data format for sign texts. When the text is white (the default) and there is no outline, the corresponding fields will be removed. It is now possible to modify the text preset of signs. When editing sign text, if no text is added, all available presets will be displayed (see the update log for 1.6.0-beta.3 for details).
- Improved the `-pattern` option for sign texts. All patterns now use canonical names while supporting some abbreviated names as aliases (see the update log for 1.6.0-beta.3 for details).
    - When the mod stores patterns in chunk data, canonical names (such as `arrow-left`) are used instead of abbreviated names. Abbreviated names (such as `al`) in chunk data stored by older versions of the mod will be converted automatically, and the data will no longer be compatible with older versions. Therefore, it is recommended to make a backup before entering the world with the new version of the mod.
- Sign text now supports `-nbt`, similar to `-json`, but it represents text components using NBT, and its syntax has some small differences from JSON. Text specified with `-json` will be automatically converted to `-nbt` (for versions 1.21.5 and above).
- Adjusted the `-texture` interaction logic in signs: even if a missing texture ID is entered, it can still take effect normally, rendering black-and-purple checkerboards without issuing warnings.
    - Compared to the mod's texture format, using a sprite text component is preferred because it is more stable and supports animated textures in resource packs. For example: `-nbt {sprite: 'block/lava_still'}`.
- Added a special text type: `debug_text`. Use `-debug_text <any text content>` to test text rendering. Please note that the text's rendering position may not be correct (for versions 1.21.10 and above).
- Fixed the issue that the game rules were not synchronized.
- Fixed the issue where using Ctrl + E to set a custom value caused the text to become invisible.
- Improved simplified mode. Now when simplified mode is enabled, it is no longer required to press Shift for adjusting height or disabling simplified mode.
- Now, when typing special text in the sign edit screen, if the entered content is invalid, the text field is shown in red and a tooltip indicating the error is also displayed.
- Improved the operation of sign edit screen, and the width of text fields will be automatically determined based on whether the scroll bar is rendered.
- Fixed the issue that when the field `textJson` of NBT of text is invalid, the translation key of the returned text component `message.mishanguc.invalid_json` does not exist. Besides, this text will be displayed in red.
- Fixed the outline rendering issue of invisible signs when holding invisible signs in hand in versions 1.21.10.

### 1.6.0-beta.6

- Now when typing special texts in the sign edit screen, if the typed content is invalid, besides showing the text field in red, a tooltip will also be showing indicating the error.
- Fixed the issue where sign preset names containing special characters (such as symbols or spaces) caused commands to be invalid. Suggestions now add quotation marks around sign preset names.
- Improved the operation of sign edit screen, and the width of text fields will be automatically determined based on whether the scroll bar is rendered.
- Fixed the issue that when the field `textJson` of NBT of text is invalid, the translation key of the returned text component `message.mishanguc.invalid_json` does not exist. Besides, this text will be displayed in red.
- Fixed the outline rendering issue of invisible signs when holding invisible signs in hand in versions 1.21.10.

### 1.6.0-beta.5

- Fixed the version that the game rules do not change when entering the world.
- Fixed the issue that, in versions above 1.21.10, when shader is enabled, when no ordinary text is rendered, the special text of this mod is not treated as text, causing it to be rendered dimly.
- Fixed the issue of using Ctrl + E to set custom value will cause the texts invisible.
- Improved simplified mode. Now when simplified mode is enabled, it is no longer required to press Shift for adjusting height or disabling simplified mode.
- Fixed the issue that the Tab key could also navigate to invisible button elements when text was already present.

### 1.6.0-beta.4

- For Minecraft 1.21.10 and above: adjust text rendering to ensure special texts can also be treated as rendered text by mods like Iris, so the special texts can have some special attributes like normal texts when shaders are enabled.
- For Minecraft 1.21.10 and above: fixed the issue that the data generator failed to run.
- Adjusted the `-texture` interaction logic in signs: even if a missing texture ID is entered, it can take effect normally, rendering black-and-purple checkerboards without outputting warnings.
- Added a special text type: `debug_text`. Use `-debug_text <any text content>` to test text rendering. Please note that the text's rendering position may not be correct.
- Removed some redundant code.
- Fixed the issue that the game rules do not synchronize.

### 1.6.0-beta.3

- Now it is possible to modify the text preset of signs. When editing the sign texts, if no text is added, all available presets will be displayed.
    - There are currently 6 built-in sign presets:
        - left arrow + 1 line of text (`left_arrow_one_line`)
        - 1 line of text (`one_line`)
        - right arrow + 1 line of text (`right_arrow_one_line`)
        - left arrow + 2 lines of text (`left_arrow_two_lines`)
        - 2 lines of text (`two_lines`)
        - right arrow + 2 lines of text (`right_arrow_two_lines`)
      - The "two lines of text" in the presets above consist of one line with size 6 and one line with size 3. The arrows can be used for directional signs such as subway signs.
    - The sign presets (excluding built-in presets) are stored in `configs/mishanguc_sign_presets/<id>.json`, where `<id>` can be any valid filename, may contain Chinese characters, but cannot contain certain special characters and cannot include subfolders. Each file uses the `json` format and supports the following fields:
        - `order`: Integer, optional; defaults to 0. Used to control the order in which sign presets are displayed. The lower the value, the higher the priority. The six built-in presets have values from -6 to -1, respectively.
            - `name`: Text component. Optional. The display name of the preset in the interface. If not specified, the translation key `signPreset.mishanguc.<id>.name` will be used, and the id will be displayed directly as a fallback if the translation key does not exist.
        - `description`: Text component. Optional. The description of the preset. In the sign edit screen, the description will be displayed when the cursor hovers over the button or when the button receives focus.
        - `text_contexts`: The text list. Required. The format of each item is equivalent to the format of each text in the sign.
            - `initial_focus`: Integer. Optional. By default, 0. This value indicates which line of text will be automatically selected when the preset is applied. For example, if the value of `initial_focus` is 2, upon applying the preset the third line of text will be automatically selected. The value of this field must be lower than the amount of elements of text list, but when the text list is empty (amount is 0), this value can be 0.
    - Added corresponding commands to handle sign presets. All commands are executed on the client side, not the server side, and take effect immediately; the relevant files will be updated. All file I/O operations are executed on a separate thread.
        - `/mishanguc:signpreset path`: Shows the path where the sign presets are stored. When the path exists, click the relevant message to show it in Explorer.
            - `/mishanguc:signpreset list`: Show a list of all current presets.
            - `/mishanguc:signpreset reload`: Reload sign presets from the disk. Note: `/reload` command will not reload sign presets.
            - `/mishanguc:signpreset save <id> [args]`: Store the sign presets. When executing, you need to locate your crosshair on a sign block of Mishang Urban Construction mod. You can specify some extra arguments (`[args]`), in the NBT object format, supporting the following fields:
                - `force`: False by default. If true, the preset will still be saved when a preset file with the same name exists or when a built-in preset with the same name exists. If false, the command will not execute in these cases. In addition, if the sign text is empty (not a single line), the command will not execute when `force` is false.
                - `order`: Integer.
                - `initial_focus`: Integer.
                - `name`: Text component.
                - `description`: Text component.
            - `/mishanguc:signpreset delete <id>`: Delete a sign preset. When deleting a non-built-in sign preset, the file `<id>.json` will be deleted if possible. When deleting a built-in sign preset, an empty `<id>.json` file will be created to mark this built-in sign preset as not to be loaded.
            - `/mishanguc:signpreset reset <id>`: Reset a sign preset. For non-built-in sign presets, it will be deleted (equivalent to `/mishanguc:signpreset delete <id>`). For built-in sign presets, whether they are overridden or marked not to load, the file `<id>.json` will be deleted to restore the built-in preset.
            - `/mishanguc:signpreset reset`: Reset all sign presets and restore all built-in sign presets. All JSON files in `config/mishanguc_sign_presets` will be deleted.
    - Note: The positions of all presets will be adjusted automatically when applied, even if not adjusted when saved. If you do not need this adjustment, set the relevant text lines to absolute mode.
        - The size of built-in sign presets is now 6 and 3 (for two lines) for all blocks. Formerly, the size of built-in sign presets was 8 and 4 for full wall sign blocks. Note that the default text size still depends on the sign itself: it is 8 for full wall signs and 6 for other blocks.
- Simplified the data format for sign texts. For situations where text is white (by default) and there is no outline, the fields will be removed.
- Improved the `-pattern` option for sign texts. Now all patterns use canonical names while supporting some abbreviated names as aliases:
    - `empty`
    - `arrow-left`, alias: `al`
    - `arrow-right`, alias: `ar`
    - `arrow-up`, alias: `arrow-top`, `au`, `at`
    - `arrow-down`, alias: `arrow-bottom`, `ad`, `ab`
    - `arrow-left-thin`
    - `arrow-right-thin`
    - `arrow-up-thin`
    - `arrow-down-thin`
    - `arrow-left-up`, alias: `arrow-left-top`, `alu`, `alt`
    - `arrow-right-up`, alias: `arrow-right-top`, `aru`, `art`
    - `arrow-left-down`, alias: `arrow-left-bottom`, `ald`, `alb`
    - `arrow-right-down`, alias: `arrow-right-bottom`, `ard`, `arb`
    - `arrow-left-turn-up`, alias: `altu`
    - `arrow-right-turn-up`, alias: `artu`
    - `arrow-left-turn-down`, alias: `altd`
    - `arrow-right-turn-down`, alias: `artd`
    - `arrow-left-right`, alias: `alr`
    - `arrow-up-down`, alias: `aud`
    - `circle-small`, alias: `small-circle`
    - `circle-medium`, alias: `medium-circle`, `circle`, `O`
    - `ban`
    - `u-turn-left-down`, alias: `u-turn-left-bottom`, `uld`, `ulb`
    - `u-turn-right-down`, alias: `u-turn-right-bottom`, `urd`, `urb`
  - `u-turn-left-up`, alias: `u-turn-left-top`, `ulu`, `ult`
      - `u-turn-right-up`, alias: `u-turn-right-top`, `uru`, `urt`
      - `cross-small`, alias: `small-scross`
      - `cross-medium`, alias: `medium-cross`, `cross`, `X`
      - `cross-large`, alias: `large-cross`
      - `square-small`, alias: `small-square`
      - `square-medium`, alias: `medium-square`, `square`
      - `square-large`, alias: `large-square`
      - `square-slant-small`, alias: `small-slant-square`
      - `square-slant-medium`, alias: `medium-slant-square`
      - `square-slant-large`, alias: `large-slant-square`
      - Note: Specifying custom patterns is not supported. To use more complicated patterns, it is recommended to use `-texture` format, or use the vanilla sprite component (example: `-nbt {sprite: xxx}`).
      - When the mod stores patterns in chunk data, it uses canonical names (such as `arrow-left`) instead of abbreviated names (such as `al`). Abbreviated names in chunk data saved by older versions of the mod will be converted automatically, and the data will no longer be compatible with older versions. Therefore, making a backup before entering the world with the new version is recommended.
- Text in signs now supports `-nbt`, similar to `-json`, but it represents text components using NBT, and its syntax has some small differences from JSON. Text specified with `-json` will be automatically converted to `-nbt`.

### 1.6.0-beta.2

- Fixed the issue that the server cannot start.

### 1.6.0-beta.1

- Updated to 1.21.10 and 1.21.11.
- Fixed the issue that the text in the sign edit screen does not display in some cases.
- Fixed the operating logic and display effect in the sign edit screen.
- Fixed the issue that the block entity data is not updated when the block is placed with some tools like Force Placing Tool and the block is from the offhand block or the block specified by the Carrying Tool in the offhand.
- Fixed the issue that Carry Tools do not handle data components of block entities correctly.
- Fixed the issue of wrong operating logic of the text outline control button on the sign edit screen.
- The shade option in the sign edit screen is no longer seen as not recommended enabling.

### 1.5.3

- For versions 1.21.4 and above, fixed the issue that custom-colored sign bars were not tinted dynamically in the inventory.
- For other updates, see the changelogs for 1.5.2, 1.5.1, and 1.5.0.

### 1.5.2

- Now all standing signs and sign bars have the block tag `#wall_post_override` so that the standing signs placed on the walls make the wall posts visible.

### 1.5.1

- Optimized the parsing of JSON text.
- When typing special texts in the sign, if the content is invalid (for example, `-json` with an invalid JSON text), the text field will be now displayed red.
- Fixed the issue that the side texture of road blocks with straight line and two bevel angle lines will disappear on some occasions.
- Fixed the issue that in the sign edit interface, the underline button changes the italic of text by mistake.

### 1.5.0

- Compatible with 1.21.6.
- Fixed the issue of incorrect texture in 1.21.5.
- Fixed the issue that the X-rotation of text cannot be set correctly.
- Adjusted the name of some contents.
- Fixed the issue of typing JSON prefixed with "`-json`" in the sign and opening the edit interface will cause a space character missed and result in incorrect parsing.

### 1.4.9

- Fixed the incorrect texture of the side of some road blocks.

### 1.4.8

- Fixed the issue of incompatibility with Sinytra Connector.

### 1.4.7

- Now in 1.21.5, the tooltips of various blocks and items can be hidden according to the field `hidden_components` in the item component `tooltip_display`.
- Fixed the issue that the item component `mishanguc:includes_fluid` is misspelled as `mishanguc:includes_field`. If there are items defined this item component manually, that item component will be ignored.

### 1.4.6

- Fixed the issue that some custom-colored signs are rendered in incorrect models in the inventory.

### 1.4.5

- Adjusted the placing rule of the wall light block and corner light block. The light can be placed only when the block has a sides shape or collision at the center of the surface, avoiding placing the light on the side where the center is empty.
- Fixed the incorrect behavior of hung sign bars.
- Fixed the issue that in the sign edit screen, when there are too many edit boxes and some boxes are not displayed, those edit boxes not displayed will still influence the interaction of other buttons.
- Introduced a simplified mode for the sign edit screen. Hold Shift and click the "hide" button to enable or disable simplified mode. In simplified mode, even if there are many texts, the text editing interface will still display in a smaller area, leaving more room for displaying the actual effect while editing the text. To adjust the height of the text area, hold Shift, hover the mouse over the "hide" button, and scroll the mouse wheel; alternatively, when the "hide" button has focus, hold Shift and press the up/down arrow key.

### 1.4.4

- Fixed the severe issue that the mod is unable to run in 1.21.1.

### 1.4.3.1

This is a fix update exclusively for 1.19.4.

- Adjusted the placing rule of the wall light block and corner light block. The light can be placed only when the block has a sides shape or collision at the center of the surface, avoiding placing the light on the side where the center is empty.
- Fixed the incorrect texture of the side of some road blocks.
- Now all standing signs and sign bars have the block tag `#wall_post_override` so that the standing signs placed on the walls make the wall posts visible.
- Fixed the issue that when the field `textJson` of NBT of text is invalid, the translation key of the returned text component `message.mishanguc.invalid_json` does not exist. Besides, this text will be displayed in red.
- Fixed the issue that the X-rotation of text cannot be set correctly.
- Adjusted the name of some contents.

### 1.4.3

- Updated for 1.21.4.
- Fixed the issue that blocks in this mod incorrectly influence mobs' pathfinding.
- Fixed the wrong name of column light blocks in versions since 1.21.3.
- Fixed the issue that Color Tool can convert nether wood planks into custom-colored planks (which can be flamed). Now only vanilla flammable planks (not including bamboo planks) can be converted.
- Fixed the issue that Color Tool can convert wall sign blocks of block such as logs into custom-colored plank wall signs. Now only wall signs of vanilla flammable planks can be converted.

### 1.4.2

- Updated for 1.21.3.
- Fixed the issue of incorrect top textures in wooden hung signs.
- Fixed the incorrect English translation for `block.mishanguc.glass_dark_oak_handrail`.
- Now specifying opacity is supported when setting custom text or outline color, and the following hex formats are supported: `#rgb`, `#rgba`, `#rrggbb`, `#rrggbbaa`. For example, `#f80` and `#ff8800` means orange, `#0ff8` and `#00ffff88` means translucent cyan.
    - When displaying the hex format of transparent color, the two bits indicating alpha are also displayed at the end.
  - Please note that z-fighting may occur when text in translucent colors is bolded or outlined in translucent colors.
  - The opacities of the outline and text are separate. When the outline color is determined automatically, the same opacity as the text will be used. When a custom outline color is specified, it will not be affected by the text opacity.
  - Translucent text may cause translucent objects behind it (such as stained glass, water, or clouds) to disappear, so please use it with caution.
- Fixed the severe issue that some tags disappear.
- Fixed the issue that colored corner handrail only drops one item.
- When holding a colored handrail block, if you place a corner block by adding to an existing same block, you can place successfully only when the color is consistent (the color of the item in hand is identical to the existing block, or when holding the item that has auto colors, the color automatically determined according to the context is identical to the existing block), to avoid replacing the color of the existing handrail block.
- Fixed the issue that the slabs of light and road blocks only drop one.
- When using Text Copy Tool to copy the text in blocks of this mod to vanilla signs, if the copied content does not meet the number of the lines in the vanilla sign, the remaining lines of the vanilla sign will be cleared. If it exceeds, the concrete ignored text content will be displayed in the message.
- When using Text Copy Tool to copy the text in blocks of this mod to vanilla signs, warnings about custom colors will no longer be made.
- Fixed the issue that error happens when texts in such forms like `-pattern` or `-rect` with outline is rendered. To be consistent with ordinary texts, shadows will never be rendered when outline exists.
- Added a field `outlineColorType` to sign texts, of which values can be `auto`, `none` or `custom`, fixing the issue that when the outline color is exactly `#fffffeff` or `#ffffffff`, will be identified as none or auto.

### 1.4.1

- Fixed the issue that road painting recipes are lost.

### 1.4.0

- No longer depends on Better Runtime Resource Pack mod.
- Fixed the issue of crash when on-stair handrail blocks are above handrail blocks.
- Added full light blocks, light stairs, etc., into block and item tag `#mishanguc:lights`.
- Added all stairs into block and item tag `#minecraft:stairs`.
- When determining shape upon placing handrail blocks, only stairs of bottom half (regular direction) will affect the handrail's shape, position and direction, and stairs of top half (upside-down direction) will not affect anymore.
- Added more handrail blocks.
    - Added glowing variants for glass handrails made of iron, gold, emerald, diamond, netherite, and lapis, which require a colored light block when dyed. (At present, there are no such blocks without custom coloring. You can color them in white.)
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
      - `hue`: Sets the hue of the block color to that of a specified color while retaining its saturation and luminosity.
      - `hue_and_saturation`: Sets the hue and saturation of the block color to those of a specified color while retaining its luminosity.
          - `hue_rotate`: Rotates the hue of block color. When using while holding Shift, the rotation will be in an opposite direction.
          - `saturation_change`: Increases the saturation of block color. When using while holding Shift, the saturation will be decreased.
          - `brightness_change`: Increases the luminosity of block color. When using while holding Shift, the luminosity will be decreased.
          - In the inventory, except for `normal` which provides multiple tools with different opacity, each of the above provides one tool.
  - The amount of color modification is controlled by the `mishanguc:color_change_amount` component, whose type is float. It specifies the amount by which the hue, saturation, or luminosity is modified. For items in the creative inventory, the default hue rotation amount is 1/24, while the default saturation and luminosity modification amount is 0.1.
  - Note: When a non-custom-colored block is converted to a custom-colored block, the *map color* of the original block is used as the initial color. Therefore, it is normal for its color to differ from the color displayed by custom-colored blocks.
      - Note: If you decrease the saturation to 0 (gray) with color tool that modifies saturation, or change the luminosity to 0 (black) or 1 (white) with color tool that modifies luminosity, then the color's hue will be 0 or 1 (red), and saturation will be 0. For example, if you decrease the saturation to 0, and then increase it, it will only be red. If you change luminosity to 0 or 1 and then increase or decrease it, it will be only gray.
- Now you can select multiple lines when editing signs.
    - Select text box while holding Ctrl to multi-select. Select selected text box while holding Ctrl to deselect it.
    - Select text box while holding Shift to select continuous multiple text boxes at once.
  - Multiple lines can be operated on at once, including adding, deleting, modifying styles, and entering text. If you increase or decrease their size, position coordinate, rotation, the modification is applied relatively to multiple lines. For properties such as color, or setting custom values, multiple lines will be set the same value.
      - The Tab-key behavior when selecting multiple lines may be unstable.
- Fixed the issue that players in Adventure Mode using slab tools with `CanDestroy` NBT tag (or component) cannot destroy blocks correctly.

### 1.3.5-beta.2

- Fixed the issue that modification caused by color tool modifying a block color may not be saved.

### 1.3.5-beta.1

- Fixed the potential block entity issue that results from colored glass handrail blocks.

### 1.3.4

- Fixed the issue that tp tools may fail to teleport.
- Fixed the issue that some tools cannot operate blocks at a far distance after modifying attributes.

### 1.3.3.1-beta.1

This is a fix update exclusively for 1.19.4.

- Adjusted the placing rule of the wall light block and corner light block. The light can be placed only when the block has a sides shape or collision at the center of the surface, avoiding placing the light on the side where the center is empty.
- Now all standing signs and sign bars have the block tag `#wall_post_override` so that the standing signs placed on the walls make the wall posts visible.
- Fixed the issue that when the field `textJson` of NBT of text is invalid, the translation key of the returned text component `message.mishanguc.invalid_json` does not exist. Besides, this text will be displayed in red.
- Fixed the issue that the X-rotation of text cannot be set correctly.
- Adjusted the name of some contents.
- Fixed the issue that the block tag `#leaves` is invalid for containing cherry which does not exist in 1.19.2.
- Fixed the issue that the type of explosion tools in the creative mode inventory is invalid in this version.

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

- Honeycomb can be used to wax signs (only those added by this mod). In Creative Mode, honeycomb can remove wax from signs. Wax cannot be removed outside Creative Mode. Waxed signs cannot be edited, made to glow, or have their glow removed.
- Using a glow ink sac can make text on signs glow. An ink sac can remove the glow from text. Glowing text is displayed at maximum luminance in dim places, but the text outline is not affected. When the text is black or the sign is in a bright place, glowing text may not make an obvious difference. Glowing text also does not affect the brightness of blocks.
- For hanging and standing signs, text glowing and waxing are handled separately on the two sides. Outside Creative Mode, each successful operation consumes one honeycomb, ink sac, or glow ink sac.
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

- Fixed the issue that, after deleting a row of text using Backspace in the sign edit screen, no remaining text was selected.
- Fixed the issue of impossibility in the sign edit screen to set custom values for properties of texts except the last line.
- Modified the display name of glass handrail blocks.
- Added three types of signs of various wood and stripped wood, and sign bars of stripped wood.
- Fixed the issue that standing signs of nether woods are flammable.
- Adjusted the order of some standing signs in the inventory to match the order of the different wood types in vanilla.
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
- Adjusted slab placement to avoid cases where, when using fast building tools to place slabs in the same case, the operation was incorrectly considered to double the slab.
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
- Press `Ctrl + I/S/U/O` to quickly toggle italics, strikethrough, underlining, and obfuscation. Note that pressing Ctrl + B cannot apply bold because it conflicts with the narrator hotkey.
- Press `Ctrl + Shift + Equal` or `Ctrl + KP_Add` to add a row. Press `Ctrl + Minus` or `Ctrl + KP_Minus` to remove that row.
- Press `Ctrl + Shift + Up/Down` to move the current line.

### 1.1.0

Updated the following content:

- Added features related to block light.
- Adjusted the order of light blocks.
- Now the same and connected strip light can have invisible face culled.
- Adjusted the outline shape of large wall light and light tube to encompass the whole look.
- Added thick stripped lights; previous versions had only strip light tubes.
- Added medium wall lights and light tubes.
- Added column lights, column light tubes, light slabs, and light covers.
- Tweaked the texture of three types of lights.
- Added light round decorations for other colors; in former versions there was only one for white.
- Wall lights can be placed on blocks with an empty side shape (such as with empty collision shape).
- Added orange, green, and pink lights.
- Added multiple road blocks, such as road with angle line with two parts offset, roads with T-shaped line with offset side, road with two bevel angle lines, and roads with different-color double lines.
- The `color` field in the NBT data of Color Tool, colored blocks, and sign texts now supports multiple formats, including:
    - integers, such as `16777215`.
    - texts indicating the text color, such as `"red"`.
  - arrays in RGBA order, such as `[0, 255, 0]`.
      - objects, such as `{signColor: red}`, `{fireworkColor: red}`, and `{mapColor: red}`.
- Optimized the code, including code related to data generation and block registration.
- Roads with automatic lines can handle line offsets more intelligently.
- When generating lines, roads with automatic lines can catch any exceptions that are thrown.
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
- Adjusted and optimized the generation logic for automatic road lines to handle some newly added blocks.
- Added framed glass handrail blocks, allowing custom tinting. Adjusted the culling between handrail blocks.
- Adjusted the display of handrail blocks in inventory.
- Changed item id: black_stone_hung_sign → blackstone_hung_sign.
- Added some vanilla-colored blocks to vanilla tags. For example, colored wool can be sheared and can dampen vibrations like vanilla wools.

Fixed the following issues:

- Tools needed when breaking block are not well configured.
- Force-placing tool and fast-building tool do not sync data when replacing block entities with the same block state.
- The item model of Color Tool was not based on a handheld item model.

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

- Text Copy Tool now displays its copied content in the item name.
- Adjusted the outline shape of hung signs and hung sign bars, which is slightly wider than their collision box, but narrower than former outline shape when holding hung signs or hung sign bars. Now the outline shape will not be wider when you hold a hung sign or bar than when not.

Fixed the following issues:

- Hung sign and hung sign bar blocks are not mirrored correctly.
- When holding some tools, the ShapeContext is ignored when drawing outline.

### 0.2.0

Updated the following content:

- The license was changed to LGPLv3.
- BRRP is now used as the dependency instead of ARRP.
- Added custom colored sign blocks, but as it is not stable yet, it is not formally added.
- Added crafting recipe for some content.
- Adjusted the texture of road block.
- No longer allow snow placed on road blocks (blocks with `mishanguc:roads` tag), to prevent the situation roads can't be seen in snowy days.
- Added the `-rect` and `-pattern` features for sign blocks. Clicking a sign holding slime ball can replace arrow characters with `-pattern` form. Clicking holding a slime ball can replace the block entities of the same type in a whole chunk.
- Walking on road blocks (blocks with `mishanguc:roads` tag) can accelerate, which does not affect FOV.
- Added yellow straight line and angle line roads (previously there is only white), yellow cross line roads (previously there is only white), road with white thick T-shaped yellow line (previously there is only road with white normal line with yellow line, and white thick line with yellow double line). Auto-connecting road blocks are adjusted accordingly.
- Changed Chinese names of some road blocks (see the update log in Chinese).
- Changed `RoadConnectionState` into a record. For version 1.16.5, records are not supported in Java 8, so the relevant changes were made using the corresponding formats.
- Adjusted the outline drawing of fast building tool and force placing tool, to make them consistent.
- Some items have durability now.
- When mirroring tool is used on the up or down surface of blocks, the mirror will be determined by player's horizontal facing.
- Added explosion tool and omnipotent tool.
- All items are not stackable now.
- When operating on blocks with rotating or mirroring tools, if the block is unchanged after the operation, the operation is considered a failure.
- The model of fast building tool can be affected by its properties. If the matching range is larger, the texture is deeper.
- Adjusted the display of mod info. It displays only when a player logs into the world, and will not display again when the player respawns or switches dimension.
- No longer allow ID checker tools and data tag tools have effect when in Spectator Mode. Now when you hit entities in Spectator Mode holding this item, you will spectate it as in other cases.
- Optimized the rendering of text.
- (For versions above 1.19) Added mangrove hanging signs, sign bars, wall signs, and handrails.

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
- For Force Placing Tool and Fast Building Tool, in non-water-including mode, when used on waterlogged blocks, whether the placed block is waterlogged is determined by whether the block at that position was waterlogged before placement. For example, when duplicating a waterlogged block to a non-waterlogged location, the duplicated block is not waterlogged.
- In bi-hand mode, when using Force Placing Tool and Fast Building Tool, the placed block may be affected by the `BlockStateTag` and `BlockEntityTag` tags, which specify the block state and block entity after placement.
- Renamed most road blocks, to solve the problem that names are too long. As old names are no longer recognized, **if you used road blocks previously, after updating this mod, previous road blocks will disappear!** After updating the mod before entering the world, remember to back up the world.
- Added yellow and cyan light blocks, and added stripped light blocks with a background.
- Adjusted the models of light blocks in the inventory.
- Optimized the process of runtime data generation (including resource pack and data pack). Now more data is generated on runtime. Some block states, due to limitations of ARRP mod, still store JSON files in a mod file.
- Added more block tags.
- Fixed the issue that data tag tool has no tooltip.
- Because Mojang annotated `Block.getName` with `@Environment(EnvType.CLIENT)` (only in versions before 1.16.5), some tools were switched to client-side usage.
    - Data tag tool still uses server data, but server sends the NBT now, instead of the prettified text.
- Now sign blocks support JSON texts. You can type `-json <a json>` in a text box, e.g., `-json {color:red,text:Hi}`.
- Added text copy tool, which can be used to copy and paste texts, and allows copy and paste between vanilla signs and mod signs.
- Added a button to flip text in the sign edit screen, which can be used to flip one line or all lines of text.
- Improved the logic of road connection state debugging tool. Now can view line types.
- Improved some translations according to word-usage and format of vanilla language files.

Fixed the following issues:

- Force placing tool and fast building tool may not correctly handle waterlogging, and may not see item tags on the offhand item.
- Force placing tool can remove entity even if you are not in Creative Mode.
- When setting formats for sign text lines, formats may be applied to text itself when rendering.
- In multiplayer mode, some prettified NBT text may lose click actions when transmitted to the client, because these click actions are defined by the mod and cannot be serialized.
- Some data of signs may be stored even if there is no text.

### 0.1.6

Updated the following content:

- Invisible glowing signs.
- For Minecraft 1.17 and above, you can set text outlines.
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
- Rearranging multiple texts sometimes produces incorrect results.
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