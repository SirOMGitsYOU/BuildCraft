package buildcraft.lib.client.guide.entry;

import buildcraft.lib.client.guide.data.JsonTypeTags;
import buildcraft.lib.misc.JsonUtil;
import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;

public final class PageEntry<T> extends PageValue<T> {

    public final JsonTypeTags typeTags;
    public final Identifier book;

    public PageEntry(PageValueType<T> type, JsonTypeTags typeTags, Identifier book, T value) {
        super(type, value);
        this.typeTags = typeTags;
        this.book = book;
    }

    public PageEntry(PageValueType<T> type, Identifier name, JsonObject json, T value) {
        super(type, value);
        this.book = JsonUtil.getIdentifier(json, "book");
//        String tagType = JsonUtils.getStringOr(json, "tag_type", "");
        String tagType = GsonHelper.getAsString(json, "tag_type");
//        String subType = JsonUtils.getStringOr(json, "tag_subtype", "");
        String subType = GsonHelper.getAsString(json, "tag_subtype");
        this.typeTags = new JsonTypeTags(name.getNamespace(), tagType, subType);
    }

    @Override
    public String toString() {
        return value.getClass().getSimpleName() + ": " + value;
    }
}
