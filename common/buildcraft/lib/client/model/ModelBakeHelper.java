package buildcraft.lib.client.model;

import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.event.ModelEvent;

public final class ModelBakeHelper {
    private ModelBakeHelper() {
    }

    public static void putItemModel(ModelEvent.ModifyBakingResult event, Identifier modelId, ItemModel model) {
        event.getResults().itemStackModels().put(modelId, model);
    }

    public static void replaceItemModels(ModelEvent.ModifyBakingResult event,
            java.util.function.BiFunction<Identifier, ItemModel, ItemModel> replacer) {
        event.getResults().itemStackModels().replaceAll(replacer);
    }
}
