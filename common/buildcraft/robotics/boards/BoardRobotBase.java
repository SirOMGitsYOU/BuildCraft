package buildcraft.robotics.boards;

import buildcraft.api.boards.RedstoneBoardRobot;
import buildcraft.api.robots.AIRobot;
import buildcraft.api.robots.EntityRobotBase;
import buildcraft.robotics.RobotGateAuthorization;
import buildcraft.robotics.ai.AIRobotGotoSleep;

/** Base class for robot boards that enforces gate work authorization before starting task AIs. */
public abstract class BoardRobotBase extends RedstoneBoardRobot {

    public BoardRobotBase(EntityRobotBase iRobot) {
        super(iRobot);
    }

    @Override
    public void startDelegateAI(AIRobot ai) {
        if (!RobotGateAuthorization.shouldStartWorkAi(robot, ai)) {
            super.startDelegateAI(new AIRobotGotoSleep(robot));
            return;
        }
        super.startDelegateAI(ai);
    }
}
