package buildcraft.robotics.boards;

import org.jetbrains.annotations.Nullable;

import buildcraft.api.boards.RedstoneBoardRobot;
import buildcraft.api.boards.RedstoneBoardRobotNBT;
import buildcraft.api.robots.EntityRobotBase;
import buildcraft.robotics.entity.EntityRobot;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;


import java.util.List;

public class RedstoneBoardRobotEmptyNBT extends RedstoneBoardRobotNBT {

    public static RedstoneBoardRobotEmptyNBT instance = new RedstoneBoardRobotEmptyNBT();

    private static final Identifier ROBOT_ID = Identifier.parse("buildcraftrobotics:robot_base");

    @Override
    public RedstoneBoardRobot create(EntityRobotBase robot) {
        return new BoardRobotEmpty(robot);
    }

    @Override
    public Identifier getRobotTexture() {
        return EntityRobot.ROBOT_BASE;
    }

    @Override
    public Identifier getRobotItemTexture() {
        return Identifier.parse("buildcraftrobotics:item/robot/robot_base");
    }

    @Override
    public Identifier getRobotTextureFullLocation() {
        return EntityRobot.ROBOT_BASE_PNG;
    }

    @Override
    public Identifier getRobotId() {
        return ROBOT_ID;
    }

    @Override
    public Identifier getID() {
        return Identifier.parse("buildcraftrobotics:board_robot_empty");
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addInformation(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {}

    @Override
    // public String getItemModelLocation()
    public String getBoardTexture() {
        // return "buildcraftrobotics:board/clean";
        return "buildcraftrobotics:item/board/clean";
    }

//    @Override
//    public String getDisplayName() {
//        return LocaleUtil.localize("buildcraft.boardRobotClean");
//    }

    @Override
    public Component getDisplayNameComponent() {
        return Component.translatable("buildcraft.boardRobotClean");
    }
}
