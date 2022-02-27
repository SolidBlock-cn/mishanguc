Welcome to use the Mishang Urban Construction mod. This mod provides you with a series of blocks and items to build a splendid urban. Currently, under development and not stable, and more content will be added in future versions.

This mod is not yet stable. Please do not use this mod in your formal development, as future incompatible updates may happen to make former contents removed.

<big style="color:red;text-shadow:1px 1px yellow">**This mod depends on ARRP!**</big>

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

The mod contains an invisible sign block, which is invisible, but the text on it is.

All signs are waterloggable. Besides, wall signs can be placed on floor and ceilings, which is different from vanilla.

Click the sign to edit text. Multiple-line text is supported. Note that when adding a line, texts are not automatically rearranged, and click "Rearrange" button to automatically adjust the vertical position of lines (lines with "Absolute" on will be ignored). You may adjust the size, offset, color and other options of each line. Text color can be adjusted with either the button (only the vanilla installed 16 colors) or customized with the text box (number value, hex or in-game color name). If custom color is used in the text box, the button could be disabled. Notice: be cautious when enabling "shade", as when multiple texts on different sides have shade on, text layers may be abnormal.

Buttons are enhanced on the sign edit screen: Click the button to adjust value; right-click the button or click with Shift down to adjust in opposite direction; click with the mouse wheel or click with Shift+Alt to return default value. Mouse wheel can also be scrolled to adjust values. Besides, hold Ctrl to adjust 8 times speed, or hold Alt to adjust 1/8 times speed.

### Tools

The mod adds a series of tools to build, most of which can be used to handle fluids (water or lava) when holding Shift. Most tools use server data, instead of client data.

- **Road connection state debug tool**: Click the road block to see what the lines are recognized within the game in different directions. These road connection states are used for auto-connecting road blocks.
- **ID checking tool**: Click a block or entity to see the ID of the block or entity. Click in mid-air to see the ID of biome.
- **Fast building tool**: Used to quickly place or remove multiple blocks. What blocks to affect depends on the "matching rule" and "range" or the item. For example, when you hold a fast-building tool with "matching rule = same material, range = 16", and click an oak planks block, other wooden blocks on the plain that connect to the oak planks within 16 blocks will be affected. When placing block, blocks on the clicked plain will be duplicated, but when some block is held in offhand, it will be used instead. Besides, you can place fluids when holding Shift.
- **Rotating tool**: Click a block to rotate. Hold Shift to rotate in opposite direction.
- **Mirroring tool**: Click the block to mirror it. For example, there's a stairs block facing east; click the east or west side to make it west-facing, or click the south or north side has no effect.
- **Slab tool**: Can be used to break the half of a double slab block. Since Minecraft 1.17, it can convert blocks that have slabs (only vanilla blocks and blocks in this mod, not including blocks in other mods) to a corresponding slab. For example, breaking an oak planks block can turn it into an oak slab. This future is not supported in Minecraft 1.16.5.
- **Force placing tool**: Can be used to force placing or break a block without causing block updates. If block already exists where the block will be placed, the already-existing block will be replaced. This tool can make hovering attaching blocks that should not have been successful usually. When a block is held in offhand, it will be placed, otherwise the clicked block will be placed.
- **Block state tool**: Click a block to see its block state properties.
- **Data tag tool**: Click a block or entity to see all of its NBT data tags.

More contents to be added are coming soon.

## About API

As some limitations are found with Fabric API, this mod simply extends the API, to fulfill some features without breaking compatibility.