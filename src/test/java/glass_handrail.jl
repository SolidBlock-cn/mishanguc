using JSON

@enum Position left center right
@enum Shape bottom middle top
@enum Direction north east south west up down
haxis = [north, east, south, west]


function generateModel(position::Position, shape::Shape)::Dict
    model = Dict(
        "parent" => "block/block",
        "textures" => Dict(
            "top" => "#frame",
            "side" => "#frame",
            "bottom" => "#frame",
            "particle" => "#glass"
        ),
        "elements" => Any[]
    )

    elements::Vector{Any} = model["elements"]

    x1 = Dict(left => 1.5, center => 8, right => 14.5)[position]

    # 栏杆两端的柱子部分。对于 left 和 right，z 为两端的坐标；对于 center，z 为中间的坐标。
    if position == center
        if shape == bottom
            # 适用于 bottom 形状的情形，柱子分为两部分，均位于中间，紧挨着，高度差1格
            push!(elements, (
                from=[x1 - 1, 0, 8],
                to=[x1 + 1, 15, 9],
                # 北面不渲染；底面 cullface；uv 无需指定
                faces=(
                    local faces = Dict();
                    for direction in instances(Direction)
                        if direction == north
                            continue
                        end
                        faces[direction] = Dict{String,Any}("texture" => "#side")
                    end;
                    faces[down]["cullface"] = down;
                    faces
                ),
            ))
            push!(elements, (
                from=[x1 - 1, 0, 7],
                to=[x1 + 1, 16, 8],
                # 南面不渲染；底面 cullface；uv 无需指定
                faces=(
                    local faces = Dict();
                    for direction in instances(Direction)
                        if direction == south
                            continue
                        end
                        faces[direction] = Dict{String,Any}("texture" => "#side")
                    end;
                    faces[down]["cullface"] = down;
                    faces
                ),
            ))
        else
            # center；非bottom的情况
            # 中间的柱子同样分成上下两部分，单独应用uv，上部分再分为南北两部分：
            #=
                北 A
                北 AB 南
                北 AB 南
                北 CC 南
                北 CC 南  # C 部分涉及负 y 值，需要手动设置侧面的uv
                北 CC 南
            =#
            ## A
            push!(elements, (
                from=[x1 - 1, 0, 8],
                to=[x1 + 1, 15, 9],
                # 底面不渲染；各面 uv 无需指定
                faces=(
                    local faces = Dict();
                    for direction in instances(Direction)
                        if direction == down
                            continue
                        end
                        faces[direction] = Dict("texture" => "#side")
                    end;
                    faces
                )
            ))
            ## B
            push!(elements, (
                from=[x1 - 1, 0, 9],
                to=[x1 + 1, 14, 10],
                # 底面和北面不渲染；各面 uv 无需指定
                faces=(
                    local faces = Dict();
                    for direction in instances(Direction)
                        if direction == down || direction == north
                            continue
                        end
                        faces[direction] = Dict("texture" => "#side")
                    end;
                    faces
                )
            ))
            ## C
            push!(elements, (
                from=[x1 - 1, -8, 8],
                to=[x1 + 1, 0, 10],
                # 顶面不渲染；各面 uv 需要指定
                faces=(
                    local faces = Dict();
                    for direction in instances(Direction)
                        if direction == up
                            continue
                        end
                        faces[direction] = Dict{String,Any}("texture" => "#side")
                    end;
                    faces[west]["uv"] = [8, 0, 10, 8];
                    faces[east]["uv"] = [6, 0, 8, 8];
                    faces[north]["uv"] = [x1 - 1, 0, x1 + 1, 8];
                    faces[south]["uv"] = [15 - x1, 0, 17 - x1, 8];
                    faces
                )
            ))
        end
    else
        # position 不为 center 的情况
        if shape == top
            push!(elements, (
                from=[x1 - 1, 0, 0],
                to=[x1 + 1, 15, 1],
                faces=(
                    local faces = Dict();
                    for direction in instances(Direction)
                        faces[direction] = Dict{String,Any}("texture" => "#side")
                    end;
                    faces[down]["cullface"] = down;
                    faces
                )
            ))
        elseif shape == bottom
            push!(elements, (
                from=[x1 - 1, 0, 15],
                to=[x1 + 1, 15, 16],
                faces=(
                    local faces = Dict();
                    for direction in instances(Direction)
                        faces[direction] = Dict{String,Any}("texture" => "#side")
                    end;
                    faces[down]["cullface"] = "down";
                    faces
                )
            ))
        end

        if shape != top
            # 16 以上的部分，北半段
            push!(elements, (
                from=[x1 - 1, 16, 0],
                to=[x1 + 1, 23, 1],
                faces=(
                    local faces = Dict();
                    for direction in instances(Direction)
                        if direction == down
                            continue
                        end
                        faces[direction] = Dict{String,Any}("texture" => "#side")
                    end;
                    faces[west]["uv"] = [0, 9, 1, 16];
                    faces[east]["uv"] = [15, 9, 16, 16];
                    faces[south]["uv"] = [x1 - 1, 9, x1 + 1, 16];
                    faces[north]["uv"] = [15 - x1, 9, 17 - x1, 16];
                    faces
                )
            ))
            # 16 以上的部分，南半段
            push!(elements, (
                from=[x1 - 1, 16, 1],
                to=[x1 + 1, 22, 2],
                faces=(
                    local faces = Dict();
                    for direction in instances(Direction)
                        if direction == down || direction == north
                            continue
                        end
                        faces[direction] = Dict{String,Any}("texture" => "#side")
                    end;
                    faces[west]["uv"] = [1, 10, 2, 16];
                    faces[east]["uv"] = [14, 10, 15, 16];
                    faces[south]["uv"] = [x1 - 1, 10, x1 + 1, 16];
                    faces
                )
            ))
            # 16 以下的部分
            push!(elements, (
                from=[x1 - 1, 0, 0],
                to=[x1 + 1, 16, 2],
                faces=(
                    local faces = Dict();
                    for direction in instances(Direction)
                        if direction == up
                            continue
                        end
                        faces[direction] = Dict{String,Any}("texture" => "#side")
                    end;
                    # 仅当这一面正好对准整数地面时，应用 cullface
                    faces[down]["cullface"] = down;
                    faces
                )
            ))
        end
    end

    # 上方边缘
    ## 水平的部分
    if shape == top
        # 顶部
        push!(elements, (
            from=[x1 - 1, 15, 0],
            to=[x1 + 1, 16, 9],
            faces=(
                local faces = Dict(direction => Dict("texture" => "#top") for direction in instances(Direction));
                faces[north]["cullface"] = "north";
                faces
            )
        ))
        # 底部
        push!(elements, (
            from=[x1 - 1, 2, position == center ? 0 : 1],
            to=[x1 + 1, 3, position == center ? 8 : 9],
            faces=(
                local faces = Dict(direction => Dict("texture" => "#top") for direction in instances(Direction));
                faces[north]["cullface"] = "north";
                faces
            )
        ))
        # 玻璃部分（下方）
        push!(elements, (
            from=[x1 - 0.5, 3, position == center ? 0 : 1],
            to=[x1 + 0.5, 12, position == center ? 8 : 9],
            faces=(
                west=(texture="#glass",),
                east=(texture="#glass",)
            )
        ))
        # 装饰部分
        push!(elements, (
            from=[x1 - 0.5, 12, position == center ? 0 : 1],
            to=[x1 + 0.5, 14, position == center ? 8 : 9],
            faces=(
                west=(texture="#decoration", tintindex=0),
                east=(texture="#decoration", tintindex=0)
            )
        ))
        # 玻璃部分（上方）
        push!(elements, (
            from=[x1 - 0.5, 14, position == center ? 0 : 1],
            to=[x1 + 0.5, 15, position == center ? 8 : 9],
            faces=(
                west=(texture="#glass",),
                east=(texture="#glass",)
            )
        ))
    end
    ## 水平的部分
    if shape == bottom
        # 顶部
        push!(elements, (
            from=[x1 - 1, 15, 8],
            to=[x1 + 1, 16, 16],
            faces=(
                local faces = Dict(direction => Dict("texture" => "#top") for direction in instances(Direction));
                faces[south]["cullface"] = "south";
                faces
            )
        ))
        # 底部
        push!(elements, (
            from=[x1 - 1, 2, position == center ? 9 : 8],
            to=[x1 + 1, 3, position == center ? 16 : 15],
            faces=(
                local faces = Dict(direction => Dict("texture" => "#top") for direction in instances(Direction));
                faces[south]["cullface"] = "south";
                faces
            )
        ))
        # 玻璃部分（下方）
        push!(elements, (
            from=[x1 - 0.5, 3, position == center ? 9 : 8],
            to=[x1 + 0.5, 12, position == center ? 16 : 15],
            faces=(
                west=(texture="#glass",),
                east=(texture="#glass",)
            )
        ))
        # 装饰部分
        push!(elements, (
            from=[x1 - 0.5, 12, position == center ? 9 : 8],
            to=[x1 + 0.5, 14, position == center ? 16 : 15],
            faces=(
                west=(texture="#decoration", tintindex=0),
                east=(texture="#decoration", tintindex=0)
            )
        ))
        # 玻璃部分（上方）
        push!(elements, (
            from=[x1 - 0.5, 14, position == center ? 9 : 8],
            to=[x1 + 0.5, 15, position == center ? 16 : 15],
            faces=(
                west=(texture="#glass",),
                east=(texture="#glass",)
            )
        ))
    end

    ## 栏杆顶部倾斜的部分
    ### y=16以下
    if shape != bottom
        for i = (shape == top ? 9 : 8):15
            push!(elements, (
                from=[x1 - 1, 23 - i, i],
                to=[x1 + 1, 24 - i, i + 1],
                faces=Dict(direction => Dict("texture" => "#top") for direction in instances(Direction))
            ))
        end
    end
    ### y=16以上
    if shape != top
        for i = 0:7
            push!(elements, (
                from=[x1 - 1, 23 - i, i],
                to=[x1 + 1, 24 - i, i + 1],
                faces=(
                    local faces = Dict(direction => Dict{Any,Any}("texture" => "#top") for direction in instances(Direction));
                    faces[west]["uv"] = [i, 8 + i, i + 1, 9 + i];
                    faces[east]["uv"] = [15 - i, 8 + i, 16 - i, 9 + i];
                    faces[south]["uv"] = [x1 - 1, 8 + i, x1 + 1, 9 + i];
                    faces[north]["uv"] = [16 - x1, 8 + i, 17 - x1, 9 + i];
                    faces
                )
            ))
        end
    end

    """
    添加元素，其中 y1 和 y2 可能小于 0 或者大于 16，以便于分段处理并手动指定 uv；但是，y1 和 y2 不能小于 -16 或者大于 32，且 x1、x2、z1、z2不得小于 0 或者大于 16。
    """
    local function putseparated!(x1::Real, x2::Real, y1::Real, y2::Real, z1::Real, z2::Real; face)::Nothing
        # 0 到 16 之间的部分
        if y2 > 0 && y1 < 16
            push!(elements, (
                from=[x1, max(0, y1), z1],
                to=[x2, min(16, y2), z2],
                faces=(
                    west=face,
                    east=face
                )
            ))
        end
        #0 以下的部分
        if y1 < 0
            local y2f = min(0, y2)
            push!(elements, (
                from=[x1, y1, z1],
                to=[x2, y2f, z2],
                faces=(
                    west=(face..., uv=[z1, 0 - y2f, z2, 0 - y1]),
                    east=(face..., uv=[16 - z2, 0 - y2f, 16 - z1, 0 - y1])
                )
            ))
        end
        #16 以上的部分
        if y2 > 16
            local y1f = max(y1, 16)
            push!(elements, (
                from=[x1, y1f, z1],
                to=[x2, y2, z2],
                faces=(
                    west=(face..., uv=[z1, 32 - y2, z2, 32 - y1f]),
                    east=(face..., uv=[16 - z2, 32 - y2, 16 - z1, 32 - y1f])
                )
            ))
        end
        return
    end

    """
    在指定位置（i）的地方，添加玻璃和装饰部分的元素。这些元素在z方向的大小仅1格。
    """
    local function putglassdecoration(i::Real)::Nothing
        # 玻璃（下半部分）
        putseparated!(x1 - 0.5, x1 + 0.5, 11 - i, 20 - i, i, i + 1, face=(texture="#glass",))
        # 装饰部分
        putseparated!(x1 - 0.5, x1 + 0.5, 20 - i, 22 - i, i, i + 1, face=(texture="#decoration", tintindex=0))
        # 玻璃（上半部分）
        putseparated!(x1 - 0.5, x1 + 0.5, 22 - i, 23 - i, i, i + 1, face=(texture="#glass",))
        return
    end

    ## 栏杆底部倾斜的部分，以及玻璃和装饰部分
    ### x=8以上
    if shape != bottom
        for i in (position == center ? (10:15) : (shape == top ? 9 : 8):15)
            # 底部栏杆
            push!(elements, (
                from=[x1 - 1, 10 - i, i],
                to=[x1 + 1, 11 - i, i + 1],
                faces=(
                    local faces = Dict(direction => Dict{Any,Any}("texture" => "#top") for direction in instances(Direction));
                    local uvy = mod(5 + i, 16);
                    faces[west]["uv"] = [i, uvy, i + 1, uvy + 1];
                    faces[east]["uv"] = [15 - i, uvy, 16 - i, uvy + 1];
                    faces[south]["uv"] = [x1 - 1, uvy, x1 + 1, uvy + 1];
                    faces[north]["uv"] = [16 - x1, uvy, 17 - x1, uvy + 1];
                    faces
                )
            ))
            putglassdecoration(i)
        end
    end
    ### x=8以下
    if shape != top
        for i in (position == center ? (shape == bottom ? (0:6) : (0:7)) : 2:7)
            # 底部栏杆
            push!(elements, (
                from=[x1 - 1, 10 - i, i],
                to=[x1 + 1, 11 - i, i + 1],
                faces=(
                    local faces = Dict(direction => Dict{Any,Any}("texture" => "#top") for direction in instances(Direction));
                    faces
                )
            ))
            putglassdecoration(i)
        end
    end
    model
end

function main()
    cd(abspath(@__FILE__, "../../../main/resources/assets/mishanguc/models/block"))
    for position in instances(Position), shape in instances(Shape)
        model = generateModel(position, shape)
        open("glass_handrail_stair_$(shape)_$(position).json", "w") do file
            println("Serializing: $model")
            JSON.print(file, model)
        end
    end
end

main()
println("SUCCESS!")