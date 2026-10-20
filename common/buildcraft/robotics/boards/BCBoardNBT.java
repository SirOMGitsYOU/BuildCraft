package buildcraft.robotics.boards;

import org.jetbrains.annotations.Nullable;

import buildcraft.api.boards.RedstoneBoardRobot;
import buildcraft.api.boards.RedstoneBoardRobotNBT;
import buildcraft.api.robots.EntityRobotBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BCBoardNBT extends RedstoneBoardRobotNBT {
    public static final Map<String, BCBoardNBT> REGISTRY = new HashMap<String, BCBoardNBT>();
    // private final Identifier texture;
    private final Identifier robotTexture;
    private final Identifier robotItemTexture;
    // Calen 1.18.2
    private final Identifier robotTextureFullLocation;
    private final Identifier robotId;
    private final Identifier id;
    private final String upperName, boardType;
    private final Constructor<? extends RedstoneBoardRobot> boardInit;

    @OnlyIn(Dist.CLIENT)
    private TextureAtlasSprite icon;

    public BCBoardNBT(String id, String name, Class<? extends RedstoneBoardRobot> board, String boardType) {
        this.id = Identifier.fromNamespaceAndPath(id);
        this.boardType = boardType;
        // this.upperName = name.substring(0, 1).toUpperCase() + name.substring(1);
        String upperName = name.substring(0, 1).toUpperCase() + name.substring(1);
        while (upperName.contains("_")) {
            int index = upperName.indexOf('_');
            if (index + 1 < upperName.length()) {
                upperName = upperName.substring(0, index) + upperName.substring(index + 1, index + 2).toUpperCase() + upperName.substring(index + 2);
            }
        }
        this.upperName = upperName;
        // this.texture = Identifier.fromNamespaceAndPath(DefaultProps.TEXTURE_PATH_ROBOTS + "/robot_" + name + ".png");
        this.robotTexture = Identifier.parse("buildcraftrobotics:entities/robot_" + name);
        this.robotItemTexture = Identifier.parse("buildcraftrobotics:item/robot/robot_" + name);
        this.robotTextureFullLocation = Identifier.parse("buildcraftrobotics:textures/entities/robot_" + name + ".png");
        this.robotId = Identifier.parse("buildcraftrobotics:robot_" + name);

        Constructor<? extends RedstoneBoardRobot> boardInitLocal;
        try {
            boardInitLocal = board.getConstructor(EntityRobotBase.class);
        } catch (Exception e) {
            e.printStackTrace();
            boardInitLocal = null;
        }
        this.boardInit = boardInitLocal;

        REGISTRY.put(name, this);
    }

    @Override
    public Identifier getID() {
        return id;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
//    public void addInformation(ItemStack stack, Player player, List list, boolean advanced)
    public void addInformation(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        // list.add(new TextComponent(ChatFormatting.BOLD.toString()).append(new TranslatableComponent("buildcraft.boardRobot" + this.upperName)));
        list.add(Component.translatable("buildcraft.boardRobot" + upperName + ".desc"));
    }

    @Override
    public RedstoneBoardRobot create(EntityRobotBase robot) {
        try {
            return boardInit.newInstance(robot);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Identifier getRobotTexture() {
        return robotTexture;
    }

    // Calen 1.18.2
    @Override
    public Identifier getRobotItemTexture() {
        return robotItemTexture;
    }

    @Override
    public Identifier getRobotTextureFullLocation() {
        return robotTextureFullLocation;
    }

    @Override
    public Identifier getRobotId() {
        return robotId;
    }

    @Override
    // public String getItemModelLocation()
    public String getBoardTexture() {
        // return "buildcraftrobotics:board/" + boardType;
        return "buildcraftrobotics:item/board/" + boardType;
    }

//    @Override
//    public String getDisplayName() {
//        return LocaleUtil.localize("buildcraft.boardRobot" + upperName);
//    }

    @Override
    public Component getDisplayNameComponent() {
        return Component.translatable("buildcraft.boardRobot" + upperName);
    }
}
