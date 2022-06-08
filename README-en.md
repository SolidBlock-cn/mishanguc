Welcome to use the Mishang Urban Construction mod. This mod provides you with a series of blocks and items to build a splendid urban. Currently, under development and not stable, and more content will be added in future versions.

This mod is not yet stable. Please do not use this mod in your formal development, as future incompatible updates may happen to make former contents removed.

**[BEFORE 0.2.0] This mod relies on ARRP. Make sure you have installed ARRP when installing this mod!**

**Since 0.2.0, this mod relies on BRRP. Please make sure you have installed [BRRP mod](https://github.com/SolidBlock-cn/BRRP) when installing this mod, and ARRP is not needed anymore!**

Welcome to join Tencent QQ group **587928350** or KaiHeiLa channel invitation code **KlFS0n** to experience the latest update of this mod.

**This mod is published under the LGPLv3 license, which you must follow when propagating this mod.**

*Road blocks are renamed since 0.1.7, and previous road blocks will be removed. Please make a backup of your world in advance.*

中文版的介绍在[此处](README.md)。

[Click here to see update log.](UpdateLog-en.md)

## How to install

This mod uses Fabric Loader and depends on Fabric API and BRRP (Better Runtime Resource Pack) mod. When installing this mod, make sure you have Fabric API and BRRP installed, or the game fails to launch or has content missing.

(Notice: Since 0.2.0, this mod relies on ARRP instead of BRRP. BRRP includes all functions of ARRP.)

Only Fabric version. There's no Forge version of this mod.

## Mod content

This mod adds a series of blocks and items.

### Blocks

#### Road blocks

The mod adds a series of road blocks to construct roads in your cities. Roads are currently all asphalt roads, with support for white and yellow lines, and combination lines in some cases.

Different from some similar mods, in this mod roads and their lines are in the same block. Road blocks include full blocks and slabs.

Most oriented blocks can be switched directions when player is sneaking. For example, when a straight-line road block is placed, the line is through the front-back direction of the player's looking; if placed while sneaking, the line is through the left-right direction of the player's looking.

The mod has auto-line roads (including slabs), which when nearby block updates, can determine the line type, direction and color of itself, according to lines of nearby roads (you can also manually trigger by clicking the top of the block). Not all lines in each type, direction and color are supported; therefore, in many cases the converted block does not match properly with nearby road blocks. The auto-line road blocks are directly converted to other blocks, so if there are nearby block update of roads again, its lines will not change again.

[SINCE 0.2.0] When player steps on a road block, the speed is multiplied by 1.75. This multiplier does not "override" the Speed status effect or other attribute modifiers. To speak accurately, the speed-up exists as long as the player steps on blocks with `mishanguc:roads` tag.

[SINCE 0.2.0] Snow cannot be placed on road blocks.

Road blocks cannot be crafted at present.

#### Light blocks

The mod adds light blocks, including full light blocks and decoration blocks. These blocks (except full blocks) are waterloggable. All blocks glow without the redstone power.

Light blocks are categorized white, yellow and cyan, in which the white light is the most luminous. Full light blocks can be crafted from glowstone dust, dye of the color and concrete of the color, and can be cut into light decoration blocks in various shapes in a stonecutter.

Strip wall light blocks and strip wall light tubes can be placed on walls, floors or ceilings. When placed on wall, it's by default horizontal, and you can make it vertical placed by sneaking.

Corner light tubes are combinations of two strip light tubes, one on ceiling or on floor, and one on wall. A corner light tube can be crafted from two corresponding strip light tube blocks.

The difference of light and light tube: light tubes blocks looks luminous in all parts, while light blocks displays a background of gray concrete. A light can be crafted from a light tube and a gray concrete; for example, a white thick strip light can be crafted from a white thick strip light tube and a white concrete.

Besides, there are auto-connecting decoration light blocks, which are attached to block sides and can automatically connect in each direction to other light blocks (including strip light tubes, corner light tubes and other auto-connecting light tubes).

#### Sign blocks

The mod adds sign blocks in various materials, including hung signs and wall signs. Hung signs can be written on both sides, and when near block or sign bar blocks above, sign bars are displayed. In hung signs, texts correspond to directions, and rotations are not supported, so when placing blocks with NBT, make sure the placing direction consists with when picked, in prevention of disappearance of texts.

The mod contains an invisible sign block as well as a glowing invisible block [SINCE 0.1.6], which is invisible, but the text on it is.

All signs are waterloggable. Besides, wall signs can be placed on floor and ceilings, which is different from vanilla.

Click the sign to edit text. Multiple-line text is supported. You can se the following properties for each line:

- Bold, italic, underline, strikethrough and obfuscation.
- Text size. Default value depends on block. Default text size for hung sign blocks is 5, for wall sign blocks is 6, and for full wall sign blocks (including *two* invisible sign*s*) is 8.
- The offset on X, Y and Z axes. Pay attention to the Z-offset, which may move your texts out of the plane, possibly to display behind the sign (causes invisibility) or in front of the sign (midair text).
- Scale on X and Y, the effect of which adds on text size. No Z-scale, as the text is on the plane. Scaling on Z axis is meaningless.
- Rotation on X, Y and Z axes.
- Text color. You can click the button to adjust text color (only 16 vanilla colors), or type a custom color in the text field (number value, hex in "#aaRRGGBB" form, or in-game color name). If the color in the text field is not a vanilla color, the button could be disabled.
- [SINCE 0.1.6 & MC version ≥ 1.17] Text outline color. You click the button to set: no outline displayed, determine outline color according to text color (similar to vanilla glowing sign), select a vanilla color, or an auto glow color for vanilla colors. Customization for outline color is not supported yet.
- Shade. Not recommended. When multiple texts in different directions have shade on, text layers may be displayed correctly.
- Absolute mode. Lines with absolute mode will be ignored when rearranging texts.
- Horizontal and vertical alignment. Note when you have multiple signs visually connected, you may find that the alignment is based on the sole sign, instead of the multiple signs.

When adding multiple lines of text, texts are not automatically rearranged, and you may find multiple lines overlap. You can click "Rearrange all" to arrange all text lines except those with absolute mode, and text lines will be arranged according to text size. Text lines with different vertical aligns are mutually independent. Each line of text keeps a margin of 1/8 lines above and below the line. Of course, you can manually adjust the Y-offset to arrange them.

[SINCE 0.1.6] If you haven't added texts yet, you can decide to apply double-line text templates, which is a large line plus a small line. (This style is usually used in subway stations, which is a line of local language plus a line of English.) For hung signs, if texts exist on the back side, you can copy from them. When copying texts from back side, X-axis offset and horizontal alignment of all lines will become opposite, and arrow characters (like '`→`') will also be opposite, which makes much convenience when making bi-direction text signs. [SINCE 0.1.7] You can also click "flip" button to flip the current text, or click while holding "Ctrl" to flip all texts.

[SINCE 0.1.7] If you want to customize ths JSON text, you can input `-json <raw JSON>` in the text box, e.g. `-json {text:"Some text", color:red, bold:true}`. The JSON recognized in this mod is "lenient", so you can leave out quotation marks for keys and values.

[SINCE 0.2.0] You can type `-rect <width> <height>` to make a specified rectangle. Its side is also affected by text size and X and Y scales.

[SINCE 0.2.0] You can type `-pattern <pattern name>` to create a pattern defined by this mod. This mod only installed pre-defined several arrow patterns. Although they can be achieved by texts, there are some issues: unless the player enables "force Unicode font", the up/down/left/right arrow characters (`↑↓←→`) displays one time larger than corner arrow characters (`↖↙↗↘`), and these characters are not aligned well; for example, two arrow characters are not displayed on a same horizontal baseline. In light of such issues, I suggest directly using `-pattern <pattern name>` to display arrows. Available pattern names include: `al`, `ar`, `alt`, `art`, `alb`, `arb`, `circle`, `ban`.

Buttons are enhanced on the sign edit screen: Click the button to adjust value; right-click the button or click with Shift down to adjust in opposite direction; click with the mouse wheel or click with Shift+Alt to return default value. Mouse wheel can also be scrolled to adjust values. Besides, hold Ctrl to adjust 8 times speed, or hold Alt to adjust 1/8 times speed.

#### Handrails

Since mod 0.1.7, handrail blocks are added. They are waterloggable.

Handrail blocks take in 5 forms:

- NORMAL. The handrail appears at the edge of a hold block. When placing, its shape direction is perpendicular to your horizontal looking direction. In other word, when you place a handrail block, it is "horizontal". Which edge will be placed on, is decided by where your crossbar hits.
- CENTER. Located at the central position of the block. In this case, the block has auto connection, which is similar to fence and glass pane. But, different from fence and glass pane, the handrail is connected to at least to direction. If there is no block nearby when placed, the handrail block will display perpendicular to your horizontal looking direction when placed. If nearby block changes, this handrail block will change as well.
- CORNER. Two NORMAL handrail blocks overlap. In the same space of a NORMAL handrail block, place a block perpendicular to it to make a corner handrail block. When you break one part of this block, the other part will be kept.
- OUTER. Similar to CORNER, but displays in an outer area from the handrail. In this case, the nearby perpendicular two handrails should be placed in advanced, and then place the handrail block in an appropriate position. Note that in this case, its shape will not change even if nearby handrails change.
- STAIR. Placed near the edge or above the stairs block (expected upside-down stairs), the block will automatically match the shape of stairs.

### Tools

The mod adds a series of tools to assist building.

#### Road connection state debug tool

Click the road block to see the line data such as direction, color and type of the road. Road connection states are mainly used for the recognition of auto-connecting road blocks. Auto connecting road blocks use these data, so if there are issues in the conversion of auto-connecting road blocks, you can use this tool to check whether the connection state of nearby blocks are consistent with what it looks like.

#### ID checking tool

Click a block or entity to see the ID (including raw ID and text ID) and name of the block or entity. Holding this item will not interact of the block or entity, or destroy the block or hurt the entity. Press Shift to get the ID of fluids. Click in midair to see the ID of biome.

#### Fast building tool

Used to quickly place or remove multiple blocks. When pressing "break" button (default left mouse button), blocks on a plane in the range that match the matching rule will be removed. When pressing "use" button (default right mouse button), block on a plane in the range that match the matching rule will be copied (if you have a block on offhand, the block on your offhand will be placed, instead of copying blocks on that plane). When holding this item, you can see the outline of blocks to be broken and blocks to be placed, as a simple preview.

There are following four matching rules:

- Same block state: Blocks must be identically same, including its properties. For example, oak stairs facing east only match with oak stairs facing east, instead of oak stairs facing south. The matching rule only checks block states nad does not check block entities.
- Same block: Blocks must be same but properties can be different. For example, oak stairs facing east can match with oak stairs facing south, but does not match with birch stairs.
- Same material: Blocks match as long as they are in same material. For example, oak planks, oak stairs, birch stairs, spruce fence and so on, match, as they are in same material. Notice that grass blocks and dirt are in different materials, even if both of them look like dirt.
- Any: Blocks always match.

When blocks are placed, block update is triggered, and also whether block can be placed will be checked before the placement. But the placement does not check whether the placed blocks are blocked by entities. That's to say, even there are entity where blocks are placed, you can also place blocks with this tool, making entities stuck inside.

[SINCE 0.1.6] You can hold Shift to place or break fluids. Scrolling mouse wheel while holding Shift or Alt can quickly switch matching mode.

This tool can be used only in Creative Mode.

#### Rotating tool

Click a block to rotate. Hold Shift to rotate in opposite direction. This tool can be used even in Survival Mode.

[SINCE 0.2.0] This tool has durability. In non-Creative Mode, each rotation wears off one durability. However, if the block rotates is identical to the block before rotation, durability will not be worn.

[SINCE 0.2.0] Rotating tools can be crafted with a pink dye, netherite ingots and wooden sticks.

Notice: Since 0.1.7, block entities cannot be rotated, but texts on hung sign blocks correspond to directions when stored in NBT data. Therefore, for versions above 1.17, when rotating hung sign blocks, text will disappear, and when rotating again, text may display at the former positions, instead of rotated 180°. This issues cannot be fixed yet, but you can manually handle it with the text copy tool. In 1.16.5, as block entities can be rotated, texts on hung sign blocks can be normally rotated.

#### Mirroring tool

Click the block to mirror (flip) it in the direction clicked. For example, there's a stairs block facing east; click the east or west side to make it west-facing, and clicking the south or north side has no effect, as it is symmetrical in south-north axis. This tool can be used in Survival Mode. [SINCE 0.2.0] When clicking the top or bottom of the block, the mirroring direction will be determined from player's facing.

[SINCE 0.2.0] This tool has durability. In non-Creative Mode, each mirroring wears off one durability. However, if the mirrored block is identical to the block before mirroring, the durability will not be worn.

[SINCE 0.2.0] Mirroring tools can be crafted from cyan dye, lime dye, netherite ingots and wooden sticks.

#### Slab tool

Can be used to break the half of a double slab block. For example, holding this tool and breaking to the top half of a double birch slab block, the top half will be broken and one birch slab is dropped, leaving the bottom half of birch slab, instead of breaking the whole block and dropping two slab blocks.

Since Minecraft 1.17, it can convert blocks *that have* slabs (only vanilla blocks and blocks in this mod, not including blocks in other mods) to a corresponding slab. For example, breaking an oak planks block can turn it into an oak slab. This future is not supported in Minecraft 1.16.5.

[SINCE 0.2.0] This tool can be crafted from shears, stone (not cobblestone) and sticks. Notice that this tool cannot be effective like pickaxes or axes.

#### Force placing tool

Can be used to force placing or break a block without causing block updates. If block already exists where the block will be placed, the already-existing block will be replaced. This tool can make hovering attaching blocks that should not have been successful usually. When holding this tool, the outline of the block to be broken and the block to be placed is displayed.

When placing blocks, the block clicked will be copied, but if you have a block on your offhand, that block will be placed instead. When holding Shift, you can place and destroy fluid (water or lava). When *destroying* a fluid block, nearby fluid (if there is) will not flow, but if you *place* a fluid block with the block, it will flow normally.

You can also remove entities with this tool, without having animation of entities hurt, or any side effect. For example, removing villagers does not cause them to raise price; removing slimes does not make them divide. This tool may fail to remove ender dragon clearly, but you can remove wardens climbing out, or even kill Creative Mode players.

This tool can be used by Creative Mode players only.

#### Block state tool

Click a block to see its block state properties (not including its NBT data). When holding Shift, the fluid state property and fluid level is also displayed. This process is executed on client side only.

This tool can't be crafted, but can be used in Survival Mode.

#### Data tag tool

Click a block entity or entity to see all of its NBT data tags.

Notice NBT data may differ between client and server (including in single-player mode). For example, chest is *always* empty in client side. This tool queries the server data of block entity or entity.

#### Text copy tool

Copy and paste texts between blocks. Click "attack" (by default left mouse button) to copy all lines of text (for hung sign block, copy all text on one side). Click "use" (by default right mouse button) to paste the texts you have copies, overriding existing texts if there are. Applicable for vanilla signs and mod signs, with some limitations:

- Vanilla signs support only 4 lines, so if you paste more than 4 lines, only the first 4 lines will be pasted.
- When cloning vanilla sign texts to a mod sign, the "color" property of the line uses the color of vanilla sign itself.
- As only one color is supported for vanilla signs, and this color can only be one of the 16 MC internal colors, some colors will be lost when cloning mod sign texts to vanilla signs. But colors defined in JSON texts will be kept.
- When cloning vanilla signs to mod signs, the color of vanilla signs will be applied to each line, and empty lines of vanilla signs will be ignored.

This tool can be crafted with papers, slime blocks and wooden sticks. The tool has durability. In non-Creative Mode, every time pasting text (even if pasted texts is the same as texts replaced), one durability will be worn. Copying text does not wear off durability.

#### Explosion tool

Explosion tool is added in 0.2.0 and can be got only in Creative Mode. There are multiple properties predefined in the creative inventory.

When holding this item, clicking "use" button (by default the right mouse button) to any block within 64 meters will cause an explosion. The properties like power and type of the explosion depend on the item property. Explosion irritates nearby entities, causing villagers to raise price or wolves to be hostile to you, but if you explode while sneaking, they will not be irritated. Besides, even in Survival Mode, the explosion will not hurt players causing it; in other words, players are immune to explosions caused by themselves.

This item has the following properties:

- Power, or range of the explosion, 4 by default. Hold Shift or Alt and scroll mouse wheel to adjust explosion power, which is ranging from -128 to 128 in Creative Mode, or from 0 to 64 in other modes. Zero-power explosions have no effect. Negative-power explosions will not destroy blocks, but can spread entities out and even irritate them, but will not actually hurt them or heal them.
- Whether to create fire, false by default. If true, the explosion will be similar to explosion of beds or respawn anchors. If the explosion creates fire, the fire can be created even if the explosion type is "none".
- Explosion type, which can be "break", "destroy" or "none", and the default is "break". "Break" is similar to TNT explosion. All affected blocks drop at the chance of 100%, and experience orbs may be generated. "Destroy" explosion only drop blocks at a small chance, and the larger power of explosion, the small chance of drops. For example, if there is an explosion with power 32, type "destroy", the destroyed block can drop at the chance of 1/32. Some rare blocks can guarantee 100% dropping, which subject to theirs loot tables. "None"-type explosions will not affect blocks, but can still hurt entities.

The block can be used in Survival mode, wearing off its durability. Degree of durability worn in each explosion depends on its explosion power. Using negative-power explosion tools can restore durability, though the explosion power cannot be adjusted to negative in Survival Mode.

If explosion tool is used in Creative Mode, no block is dropped regardless of its explosion type and the `doTileDrops` game rule.

This tool can be used in dispensers. When a dispenser with an explosion tool installed is triggered, if there is some blocking of blocks or entities (except sneaking entities) in the area of 64 meters in front, explosion is caused at the blocking. Blocks and entities with empty collision box (such as torch, marker armor stands, dropped items) and spectator players will not be treated as blocking. When exploded with the dispenser, item durability is worn, no players is immune to it, and no entities will not be irritated.

#### Omnipotent tool

This tool has infinite durability, mining speed, attacking hurt and attack speed.

In Survival Mode, this tool can be used to mine any block instantly (of cause, bedrock cannot be mined), and the mining is always seen as effective, dropping items the same as what would be dropped when mined by effective tools, and can be also affected by enchantments such as Silk Touch and Fortune.

When attacking entities, they will be definitely slain. Compared to force placing tool, the omnipotent tool *kills* entities instead of *removing* entities, so it can normally irritate entities or make slimes divide, can normally kill ender dragon, and cannot kill wardens climbing out. Clicking "use" button (default right mouse button) can heal it totally.

Similar to netherite tools, this tool is totally fireproof.