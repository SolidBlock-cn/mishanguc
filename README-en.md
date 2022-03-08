Welcome to use the Mishang Urban Construction mod. This mod provides you with a series of blocks and items to build a splendid urban. Currently, under development and not stable, and more content will be added in future versions.

This mod is not yet stable. Please do not use this mod in your formal development, as future incompatible updates may happen to make former contents removed.

**This mod relies on ARRP. Make sure you have installed ARRP when installing this mod!**

**This mod is published under the GPLv3 license, which you must follow when propagating this mod. According to GPLv3 license, any programs based on this mod are supposed to be under this license as well. When propagating the source code, the license file is supposed to be kept. When propagating compiled mod files, source code or links should be properly provided. Violation of GPLv3 results to termination of the license, until the mod author agrees or the GPLv3 license allows reinstating. You may refer to the GPLv3 license file for details.**=

中文版的介绍在[此处](README.md)。

[Click here to see update log.](UpdateLog.md)

## How to install

This mod uses Fabric Loader and depends on Fabric API and ARRP (Advanced Runtime Resource Pack) mod. When installing this mod, make sure you have Fabric API and ARRP installed, or the game fails to launch or has content missing.

## Mod content

This mod adds a series of blocks and items, crafting currently not supported.

### Road blocks

The mod adds a series of road blocks to construct roads in your cities. Roads are currently all asphalt roads, with support for white and yellow lines. Roads and lines are in the same block, and include full blocks and slabs. Most oriented blocks can be switched directions when player is sneaking.

The mod has auto-line roads, which when nearby block updates, can determine what the lines should be like according to lines of nearby roads (you can also manually trigger by clicking the top of the block). The lines on road block, since determined, will not automatically change.

### Light blocks

The mod adds light blocks, including full light blocks and decoration blocks. These blocks are waterloggable.

Strip wall light blocks can be placed on walls, floors or ceilings. When placed on wall, it's by default horizontal, and you can make it vertical placed by sneaking.

Light decoration blocks are attached to block sides and can automatically connect to various directions.

### Sign blocks

The mod adds sign blocks in various materials, including hung signs and wall signs. Hung signs can be written on both sides, and when near block or sign bar blocks above, sign bars are displayed. In hung signs, texts correspond to directions, and rotations are not supported, so when placing blocks with NBT, make sure the placing direction consists with when picked, in prevention of disappearance of texts.

The mod contains an invisible sign block as well as a glowing invisible block [SINCE 0.1.6], which is invisible, but the text on it is.

All signs are waterloggable. Besides, wall signs can be placed on floor and ceilings, which is different from vanilla.

Click the sign to edit text. Multiple-line text is supported. You can se the following properties for each line:

- Bold, italic, underline, strikethrough and obfuscation.
- Text size. Default value depends on block. Default text size for hung sign blocks is 5, for wall sign blocks is 6, and for full wall sign blocks (including *two* invisible sign*s*) is 8.
- The offset on X, Y and Z axes. Pay attention to the Z-offset, which may move your texts out of the plane, possibly to display behind the sign (causes invisibility) or in front of the sign (midair text).
- Scale on X and Y, the effect of which adds on text size. No Z-scale, as the text is on the plane. Scaling on Z axis is meaningless.
- Rotation on X, Y and Z axes.
- Text color. You can click the button to adjust text color (only 16 vanilla colors), or type a custom color in the text field (number value, hex in #aaRRGGBB form, or in-game color name). If the color in the text field is not a vanilla color, the button could be disabled.
- [SINCE 0.1.6 && MC version ≥ 1.17] Text outline color. You click the button to set: no outline displayed, determine outline color according to text color (similar to vanilla glowing sign), select a vanilla color, or an auto glow color for vanilla colors. Customization for outline color is not supported yet.
- Shade. Not recommended. When multiple texts in different directions have shade on, text layers may be displayed correctly.
- Absolute mode. Lines with absolute mode will be ignored when rearranging texts.
- Horizontal and vertical alignment. Note when you have multiple signs visually connected, you may find that the alignment is based on the sole sign, instead of the multiple signs.

When adding multiple lines of text, texts are not automatically rearranged, and you may find multiple lines overlap. You can click "Rearrange all" to arrange all text lines except those with absolute mode, and text lines will be arranged according to text size. Text lines with different vertical aligns are mutually independent. Each line of text keeps a margin of 1/8 lines above and below the line. Of course, you can manually adjust the Y-offset to arrange them.

[SINCE 0.1.6] If you haven't added texts yet, you can decide to apply double-line text templates, which is a large line plus a small line. (This style is usually used in subway stations, which is a line of local language plus a line of English.) For hung signs, if texts exist on the back side, you can copy from them. When copying texts from back side, X-axis offset and horizontal alignment of all lines will become opposite, and arrow characters (like '`→`') will also be opposite, which makes much convenience when making bi-direction text signs.

Buttons are enhanced on the sign edit screen: Click the button to adjust value; right-click the button or click with Shift down to adjust in opposite direction; click with the mouse wheel or click with Shift+Alt to return default value. Mouse wheel can also be scrolled to adjust values. Besides, hold Ctrl to adjust 8 times speed, or hold Alt to adjust 1/8 times speed.

### Tools

The mod adds a series of tools to build, most of which can be used to handle fluids (water or lava) when holding Shift. Most tools use server data, instead of client data.

- **Road connection state debug tool**: Click the road block to see what the lines are recognized within the game in different directions. These road connection states are used for auto-connecting road blocks.
- **ID checking tool**: Click a block or entity to see the ID of the block or entity. Click in midair to see the ID of biome.
- **Fast building tool**: Used to quickly place or remove multiple blocks. What blocks to affect depends on the "matching rule" and "range" or the item. For example, when you hold a fast-building tool with "matching rule = same material, range = 16", and click an oak planks block, other wooden blocks on the plane that connect to the oak planks within 16 blocks will be affected. When placing block, blocks on the clicked plane will be duplicated, but when some block is held in offhand, it will be used instead. You can place fluids when holding Shift. And scrolling mouse wheel while holding Shift or Alt can quickly switch matching mode.
- **Rotating tool**: Click a block to rotate. Hold Shift to rotate in opposite direction.
- **Mirroring tool**: Click the block to mirror it. For example, there's a stairs block facing east; click the east or west side to make it west-facing, or click the south or north side has no effect.
- **Slab tool**: Can be used to break the half of a double slab block. Since Minecraft 1.17, it can convert blocks that have slabs (only vanilla blocks and blocks in this mod, not including blocks in other mods) to a corresponding slab. For example, breaking an oak planks block can turn it into an oak slab. This future is not supported in Minecraft 1.16.5.
- **Force placing tool**: Can be used to force placing or break a block without causing block updates. If block already exists where the block will be placed, the already-existing block will be replaced. This tool can make hovering attaching blocks that should not have been successful usually. When a block is held in offhand, it will be placed, otherwise the clicked block will be placed.
- **Block state tool**: Click a block to see its block state properties.
- **Data tag tool**: Click a block or entity to see all of its NBT data tags.

More contents to be added are coming soon.

## About API

As some limitations are found with Fabric API, this mod simply extends the API, to fulfill some features without breaking compatibility.