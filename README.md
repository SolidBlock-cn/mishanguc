欢迎使用迷上城建（Mishang Urban Construction）模组。本模组为您提供了一系列方块和工具，打造出精美的城市。目前本模组还在开发中，各功能还不稳定，未来还会加入更加内容。

模组还不稳定，请勿将本模组内容用于实际开发中，以免因模组出现不兼容的更新而导致旧的内容消失。

**本模组依赖 ARRP，安装本模组时确保已安装 ARRP 模组！**

**本模组是按照 GPLv3 协议发布的，传播本模组时需遵守本协议。根据该协议，由本模组衍生的任何软件都应按 GPLv3 协议发布。传播源代码时，应当保留许可证文件。传播编译后的模组文件时，应当保留源码或提供源码地址。违反 GPLv3 将会丧失本许可证提供的权利，直到模组作者或者 GPLv3 许可证规定的恢复权利。具体请参考许可证原文。**=

For English version of the description, see [this file](README-en.md).

[点击此处查看更新记录。](UpdateLog.md)

## 如何安装

本模组使用 Fabric Loader，并依赖 Fabric API 和 ARRP（高级运行时资源包）模组。请在安装本模组时，确保安装了 Fabric API 和 ARRP，否则可能造成游戏无法启动或者内容缺失。

## 模组内容

本模组加入了一系列方块和物品。这些模组内容暂不支持合成。

### 道路方块

本模组添加了一系列道路方块，用来构建你的城市中的道路。目前道路均为柏油路，支持白色与黄色标线。道路和标线均位于同一方块，且分为完整方块和台阶。大多数带有朝向的方块在潜行时可以切换方向。

本模组还添加了可自动标线的道路，放下后若侧面有方块更新，则会根据周围道路的标线来自动确定自身的标线形状和方向（点击方块顶部也可以手动触发更新），注意一旦确定好标线就无法再次自动更改。

### 灯方块

本模组添加了一系列灯方块，包括完整灯块和装饰物方块。这些方块可放在水中，且不需要红石触发。

条形灯管方块可放置在墙上、地上或天花板上。放置在墙上时默认是水平的，潜行时放置可放置垂直方向的条形灯管方块。

灯装饰物方块是吸附在方块面上并且可以在各个方向自动连接的方块。

### 告示牌方块

本模组添加了各种纹理的告示牌方块，包括悬挂着的告示牌和墙上的告示牌。其中，悬挂告示牌方向的两侧都可以写文字，且当告示牌上方连接有方块或告示牌杆方块时，会显示告示牌杆。悬挂的告示牌的文字与方向对应，暂不支持直接旋转，如放置带有 NBT 的方块，应确保放置的方向与获取时的方向一致，以免文字不显示。

本模组有个隐形告示牌方块和（自 0.1.6）隐形的发光告示牌，自身不可见，但告示牌上的文字可见。

所有告示牌均可放在水中。此外，墙上的告示牌可以放在地上和天花板上，这与原版不同。

点击告示牌即可编辑文本。支持多行文本。你可以为每行文本设置以下属性：

- 加粗、倾斜、下划线、删除线、随机文本等。
- 文本大小。默认值取决于方块。悬挂告示牌上的文字默认大小为5，墙上告示牌上的文字默认大小为6，完整告示牌（含*两种*隐形告示牌）上的文字默认大小为8。
- 文字在 X、Y、Z 三个坐标轴方向上的偏移。注意谨慎调整 Z 轴偏移，该偏移将使文本脱离告示牌平面，可能显示在告示牌后面（导致不可见），也有可能显示告示牌前面（文本悬空）。
- X、Y 两个方向上的缩放。其效果与文本大小互相叠加。没有 Z 轴方向上的缩放，因为文本本就在一个平面上，在 Z 轴方向缩放没有意义。
- 围绕 X、Y、Z 三个坐标轴的旋转。
- 文本颜色。可通过按钮调节文本颜色（仅限内置的 16 种颜色），也可以在旁边的文本框输入自定义颜色（数值、#aaRRGGBB格式的十六进制或者游戏内置的颜色名称）。如果文本框中输入的颜色不是 16 种内置颜色，则按钮会被禁用。
- （自 0.1.6，且限 Minecraft 1.17 以上的版本）文本描边颜色。可通过按钮设置为：不显示边框、根据文本颜色自动决定描边颜色（和原版的发光告示牌一样）、选择内置颜色、选择内置颜色对应的发光告示牌描边颜色。暂不支持自定义。
- 阴影。不建议开启，当不同方向的文本都开启了“阴影”功能时，文本的显示次序可能存在异常。
- 绝对模式。若某一行开启绝对模式，则在“重新排列”文本时，这一行会被忽略。
- 水平对齐和垂直对齐。注意：如果你有连起来放置的多个告示牌，你应该会注意到对齐的时以单个告示牌为基准，而非多个。

添加多行文本时，文本不会自动排版，你可能会发现多层文本叠在一起。你可以通过点击“全部重排”来从上到下依次排列非绝对模式的文本行，该功能会根据文本大小依次排列各行，不同垂直对齐方向的文本互不干扰。每行文本都会在其上下方留下 1/8 行大小的行距。当然你也可以手动调节 Y 轴偏移来进行排版。

（自 0.1.6）如果你的告示牌还没有创建文本，你可以选择应用双行文字模板（一行大的一行小的，通常用在地铁站中，一行中文加一行英文）。对于悬挂告示牌，如果背面有文字，可以从背面复制。从背面复制文本时，所有的 X 轴偏移和水平对齐方式都会反向，文中的箭头文字（如“`→`”）也会反向。这在制作两面都有文字的导向牌时非常方便。

在告示牌编辑界面，各个按钮都进行了增强：点击按钮可调整数值，鼠标右键点击按钮或者按住 Shift 点击按钮可反方向调整，鼠标滚轮点击按钮或者按住 Alt+Shift 点击按钮可恢复默认值。滚动鼠标滚轮亦可用于调节数值。此外，按住 Ctrl 可 8 倍速调整，按住 Alt 可 1/8 倍速调整。

### 工具

本模组添加了一系列用来辅助建造的工具。大多数工具在按下 Shift 时可以处理流体（水或者熔岩）。大多数工具都是获取的服务器数据，而非客户端数据。

- **道路连接状态调试工具**：点击道路方块即可查看该道路在系统内部的各方向上的连接方向等情况。道路连接状态主要用于自动标线的道路方块识别。
- **ID检查工具**：点击方块或实体时可以用来查看方块或实体的ID，如果对着空气点击则可以用于获取生物群系的ID。
- **快速建造工具**：可以用来快速地放置或者移除多个方块。受影响的方块取决于该物品的“匹配规则”和“范围”，例如，拿着匹配规则为“相同材料”、范围为16的快速建造工具点击橡木木板时，该平面上16格以内与该橡木木板方块相连的所有木质方块都会受到影响。放置方块时，会直接复制被点击的平面上的方块，但如果副手拿着方块，则使用副手上的方块。按住 Shift 时可放置或破坏流体。按住 Shift 或者 Alt 并滚动鼠标滚轮可调整匹配模式。
- **旋转工具**：点击方块即可旋转。按住 Shift 可反方向旋转。
- **镜像工具**：点击方块时可沿着被点击的方向翻折方块。例如，朝东的楼梯方块，点击该方块的东侧或者西侧可变成朝西的，点击南侧或者北侧不受影响（因为该方块本来就南北对称）。
- **台阶工具**：可用来只破坏双台阶方块的一半。从 Minecraft 1.17 开始，支持直接将具有台阶的完整方块（仅限原版方块以及本模组的方块，不含其他模组的方块）转化为台阶，例如对着橡木木板方块破坏即可将其转化为橡木台阶。该功能在 Minecraft 1.16.5 不受支持。
- **强制放置工具**：可用于强制放置或破坏方块而不造成方块更新，如果被放置方块的地方已有方块，该方块将会被替换。可用此工具来形成本来不可能做到的方块悬空效果。副手拿着方块时，放置该方块，否则放置被点击的方块。
- **方块状态工具**：点击方块即可获取方块状态属性。
- **数据标签工具**：点击方块或者实体即可获取其所有 NBT 数据标签。

还有更多内容将在新版本添加，敬请期待。

## 关于 API

本模组使用的 Fabric API 存在一些局限，因此对这些 API 进行了一些扩展，以实现模组的一些特性而不影响与其他模组的兼容性。