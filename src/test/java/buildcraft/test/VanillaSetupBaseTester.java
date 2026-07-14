package buildcraft.test;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import org.junit.BeforeClass;

import java.io.InputStream;
import java.io.PrintStream;

public class VanillaSetupBaseTester {
    @BeforeClass
    public static void init() {
        System.out.println("INIT");
        PrintStream sysOut = System.out;
        InputStream sysIn = System.in;

        SharedConstants.tryDetectVersion();
        try {
            Bootstrap.bootStrap();
        } catch (ExceptionInInitializerError | NoClassDefFoundError | RuntimeException e) {
            // Forge patches Bootstrap to call NetworkHooks.init() after vanilla registries are ready.
            // Under plain JUnit (no ModLauncher), EventBus fails to construct NetworkEvent.
            // isBootstrapped is already true by then, so Items/Blocks registries are usable for unit tests.
            Throwable root = e;
            while (root.getCause() != null && root.getCause() != root) {
                root = root.getCause();
            }
            if (!(root instanceof NoSuchMethodException)) {
                throw e;
            }
            System.out.println("Ignoring Forge NetworkHooks init failure in unit tests: " + root);
        }

        System.setIn(sysIn);
        System.setOut(sysOut);
    }
}
