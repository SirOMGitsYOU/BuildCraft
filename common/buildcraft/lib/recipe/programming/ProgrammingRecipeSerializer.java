package buildcraft.lib.recipe.programming;

import org.jetbrains.annotations.Nullable;

import buildcraft.api.recipes.IProgrammingRecipe;
import buildcraft.api.recipes.IngredientStack;
import buildcraft.lib.misc.JsonUtil;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;



public class ProgrammingRecipeSerializer implements RecipeSerializer<IProgrammingRecipe> {
    public static final ProgrammingRecipeSerializer INSTANCE;

    static {
        INSTANCE = new ProgrammingRecipeSerializer();
    }

    @Override
    public IProgrammingRecipe fromJson(Identifier recipeId, JsonObject json) {
        String type = GsonHelper.getAsString(json, "type");
        IngredientStack input = JsonUtil.deSerializeIngredientStack(json.getAsJsonObject("input"));
        ItemStack output = JsonUtil.deSerializeItemStack(json.getAsJsonObject("output"));
        long energyCost = GsonHelper.getAsLong(json, "energyCost");
        return new BoardProgrammingRecipe(recipeId, input, output, energyCost);
    }

    public static void toJson(ProgrammingRecipeBuilder builder, JsonObject json) {
        json.addProperty("type", IProgrammingRecipe.TYPE_ID.toString());
        json.add("input", JsonUtil.serializeIngredientStack(builder.getInput()));
        json.add("output", JsonUtil.serializeItemStack(builder.getOutput()));
        json.addProperty("energyCost", builder.getEnergyCost());
    }

    @Nullable
    @Override
    public IProgrammingRecipe fromNetwork(Identifier recipeId, FriendlyByteBuf buffer) {
        Identifier id = buffer.readResourceLocation();
        IngredientStack input = IngredientStack.fromNetwork(buffer);
        ItemStack output = buffer.readItem();
        long energyCost = buffer.readLong();
        return new BoardProgrammingRecipe(id, input, output, energyCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, IProgrammingRecipe recipe) {
        buffer.writeResourceLocation(recipe.getId());
        recipe.getInput().toNetwork(buffer);
        buffer.writeItem(recipe.getOutput());
        buffer.writeLong(recipe.getEnergyCost());
    }
}
