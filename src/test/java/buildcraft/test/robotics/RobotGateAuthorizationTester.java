package buildcraft.test.robotics;

import buildcraft.api.robots.DockingStation;
import buildcraft.api.statements.StatementSlot;
import buildcraft.robotics.boards.BoardRobotBase;
import buildcraft.robotics.boards.BoardRobotPicker;
import buildcraft.robotics.boards.BoardRobotPlanter;
import buildcraft.robotics.boards.BoardRobotLumberjack;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class RobotGateAuthorizationTester {

    @Test
    public void workBoardsExtendGateAuthorizedBase() {
        Assert.assertTrue(BoardRobotBase.class.isAssignableFrom(BoardRobotPicker.class));
        Assert.assertTrue(BoardRobotBase.class.isAssignableFrom(BoardRobotPlanter.class));
        Assert.assertTrue(BoardRobotBase.class.isAssignableFrom(BoardRobotLumberjack.class));
    }

    @Test
    public void gatedStationRequiresActiveWorkArea() {
        TestDockingStation station = new TestDockingStation(true, false);
        Assert.assertTrue(station.hasGate());
        Assert.assertFalse(station.isWorkAuthorized());

        station.setWorkAuthorized(true);
        Assert.assertTrue(station.isWorkAuthorized());
    }

    @Test
    public void ungatedStationDoesNotRequireWorkArea() {
        TestDockingStation station = new TestDockingStation(false, false);
        Assert.assertFalse(station.hasGate());
        Assert.assertTrue(station.isWorkAuthorized());
    }

    private static final class TestDockingStation extends DockingStation {
        private final boolean hasGate;
        private boolean workAuthorized;

        private TestDockingStation(boolean hasGate, boolean workAuthorized) {
            this.hasGate = hasGate;
            this.workAuthorized = workAuthorized;
        }

        private void setWorkAuthorized(boolean workAuthorized) {
            this.workAuthorized = workAuthorized;
        }

        @Override
        public boolean hasGate() {
            return hasGate;
        }

        @Override
        public boolean isWorkAuthorized() {
            if (!hasGate) {
                return true;
            }
            return workAuthorized;
        }

        @Override
        public Iterable<StatementSlot> getActiveActions() {
            return Collections.emptyList();
        }
    }
}
