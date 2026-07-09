package buildcraft.robotics;

import buildcraft.api.robots.AIRobot;
import buildcraft.api.robots.DockingStation;
import buildcraft.api.robots.EntityRobotBase;
import buildcraft.robotics.ai.AIRobotGotoSleep;
import buildcraft.robotics.ai.AIRobotGotoStation;
import buildcraft.robotics.ai.AIRobotGotoStationAndUnload;
import buildcraft.robotics.ai.AIRobotGotoStationAndUnloadFluids;
import buildcraft.robotics.ai.AIRobotGotoStationToUnload;
import buildcraft.robotics.ai.AIRobotGotoStationToUnloadFluids;
import buildcraft.robotics.ai.AIRobotRecharge;
import buildcraft.robotics.ai.AIRobotShutdown;
import buildcraft.robotics.ai.AIRobotSleep;

/** Gate authorization for robots linked to pipe docking stations. */
public final class RobotGateAuthorization {

    private RobotGateAuthorization() {
    }

    public static boolean isWorkAuthorized(EntityRobotBase robot) {
        DockingStation station = robot.getLinkedStation();
        if (station == null) {
            return true;
        }
        return station.isWorkAuthorized();
    }

    /** AIs that may run even when gate work authorization is missing. */
    public static boolean isAllowedWithoutWorkAuthorization(AIRobot ai) {
        return ai instanceof AIRobotGotoSleep
                || ai instanceof AIRobotSleep
                || ai instanceof AIRobotGotoStation
                || ai instanceof AIRobotGotoStationAndUnload
                || ai instanceof AIRobotGotoStationAndUnloadFluids
                || ai instanceof AIRobotGotoStationToUnload
                || ai instanceof AIRobotGotoStationToUnloadFluids
                || ai instanceof AIRobotRecharge
                || ai instanceof AIRobotShutdown;
    }

    public static boolean shouldStartWorkAi(EntityRobotBase robot, AIRobot ai) {
        if (isAllowedWithoutWorkAuthorization(ai)) {
            return true;
        }
        return isWorkAuthorized(robot);
    }
}
