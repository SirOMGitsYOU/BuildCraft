package buildcraft.datagen.base;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class BCBaseTextureGenerator implements DataProvider {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    protected static final Logger LOGGER = LogManager.getLogger();
    protected final DataGenerator generator;
    protected final ExistingFileHelper exFileHelper;

    protected BCBaseTextureGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
        this.generator = gen;
        this.exFileHelper = exFileHelper;
    }

    protected abstract void generateTextures(CachedOutput output) throws IOException;

    @Override
    public void run(CachedOutput output) throws IOException {
        generateTextures(output);
    }

    protected void save(BufferedImage image, CachedOutput output, Path path) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteStream);
        byte[] bytes = byteStream.toByteArray();
        output.writeIfNeeded(path, bytes, Hashing.sha1().hashBytes(bytes));
    }
}
