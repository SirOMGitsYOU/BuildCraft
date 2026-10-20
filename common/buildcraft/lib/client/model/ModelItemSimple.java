/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.lib.client.model;

import org.jetbrains.annotations.Nullable;

import buildcraft.lib.misc.LazyLoadedValue;
import buildcraft.lib.misc.SpriteUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

/** Provides a simple way of rendering an item model with just a list of quads. */
@SuppressWarnings("deprecation")
public class ModelItemSimple implements ItemModel {
    public static final ItemTransforms TRANSFORM_DEFAULT = ItemTransforms.NO_TRANSFORMS;
    public static final ItemTransforms TRANSFORM_BLOCK;
    public static final ItemTransforms TRANSFORM_PLUG_AS_ITEM;
    public static final ItemTransforms TRANSFORM_PLUG_AS_ITEM_BIGGER;
    public static final ItemTransforms TRANSFORM_PLUG_AS_BLOCK;
    public static final ItemTransforms TRANSFORM_ITEM;

    static {
        ItemTransform thirdp_left = def(75, 45, 0, 0, 2.5, 0, 0.375);
        ItemTransform thirdp_right = def(75, 225, 0, 0, 2.5, 0, 0.375);
        ItemTransform firstp_left = def(0, 225, 0, 0, 0, 0, 0.4);
        ItemTransform firstp_right = def(0, 45, 0, 0, 0, 0, 0.4);
        ItemTransform head = def(0, 0, 0, 0, 0, 0, 1);
        ItemTransform gui = def(30, 225, 0, 0, 0, 0, 0.625);
        ItemTransform ground = def(0, 0, 0, 0, 3, 0, 0.25);
        ItemTransform fixed = def(0, 0, 0, 0, 0, 0, 0.5);
        TRANSFORM_BLOCK =
                new ItemTransforms(thirdp_left, thirdp_right, firstp_left, firstp_right, head, gui, ground, fixed);

        ItemTransform item_head = def(0, 0, 0, 0, 0, 0, 1);
        ItemTransform item_gui = def(0, 90, 0, 0, 0, 0, 1);
        ItemTransform item_ground = def(0, 0, 0, 0, 3, 0, 0.5);
        ItemTransform item_fixed = def(0, 0, 0, 0, 0, 0, 0.85);
        firstp_left = def(0, 225, 0, 0, 0, -4, 0.4);
        firstp_right = def(0, 45, 0, 0, 0, -4, 0.4);
        TRANSFORM_PLUG_AS_ITEM = new ItemTransforms(thirdp_left, thirdp_right, firstp_left, firstp_right,
                item_head, item_gui, item_ground, item_fixed);
        TRANSFORM_PLUG_AS_ITEM_BIGGER = scale(TRANSFORM_PLUG_AS_ITEM, 1.8);

        thirdp_left = def(75, 45, 0, 0, 2.5, 0, 0.375);
        thirdp_right = def(75, 225, 0, 0, 2.5, 0, 0.375);
        firstp_left = def(0, 45, 0, 0, 0, 0, 0.4);
        firstp_right = def(0, 225, 0, 0, 0, 0, 0.4);
        gui = def(30, 135, 0, -3, 1.5, 0, 0.625);
        TRANSFORM_PLUG_AS_BLOCK =
                new ItemTransforms(thirdp_left, thirdp_right, firstp_left, firstp_right, head, gui, ground, fixed);

        ground = def(0, 0, 0, 0, 2, 0, 0.5);
        head = def(0, 180, 0, 0, 13, 7, 1);
        thirdp_right = def(0, 0, 0, 0, 3, 1, 0.55);
        firstp_right = def(0, -90, 25, 1.13, 3.2, 1.13, 0.68);
        thirdp_left = thirdp_right;
        firstp_left = firstp_right;
        fixed = def(0, 180, 0, 0, 0, 0, 1);
        gui = def(0, 0, 0, 0, 0, 0, 1);
        TRANSFORM_ITEM =
                new ItemTransforms(thirdp_left, thirdp_right, firstp_left, firstp_right, head, gui, ground, fixed);
    }

    private static ItemTransforms scale(ItemTransforms from, double by) {
        return new ItemTransforms(
                scale(from.thirdPersonLeftHand, by),
                scale(from.thirdPersonRightHand, by),
                scale(from.firstPersonLeftHand, by),
                scale(from.firstPersonRightHand, by),
                scale(from.head, by),
                scale(from.gui, by),
                scale(from.ground, by),
                scale(from.fixed, by)
        );
    }

    private static ItemTransform scale(ItemTransform from, double by) {
        float scale = (float) by;
        org.joml.Vector3f nScale = new org.joml.Vector3f(from.scale.x(), from.scale.y(), from.scale.z());
        nScale.mul(scale);
        return new ItemTransform(from.rotation, from.translation, nScale);
    }

    private static ItemTransform def(double rx, double ry, double rz, double tx, double ty, double tz, double scale) {
        return def((float) rx, (float) ry, (float) rz, (float) tx, (float) ty, (float) tz, (float) scale);
    }

    private static ItemTransform def(float rx, float ry, float rz, float tx, float ty, float tz, float scale) {
        org.joml.Vector3f rot = new org.joml.Vector3f(rx, ry, rz);
        org.joml.Vector3f translate = new org.joml.Vector3f(tx / 16f, ty / 16f, tz / 16f);
        return new ItemTransform(rot, translate, new org.joml.Vector3f(scale, scale, scale));
    }

    private final ModelRenderProperties properties;
    private List<BakedQuad> quads = ImmutableList.of();

    public ModelItemSimple(List<BakedQuad> quads, ItemTransforms transforms, boolean isGui3d) {
        this.properties = createProperties(quads, transforms, isGui3d);
    }

    public ModelItemSimple(LazyLoadedValue<List<BakedQuad>> lazyLoadedQuads, ItemTransforms transforms, boolean isGui3d,
            Consumer<Runnable> consumerRunOnTextureStitchEvent$Post) {
        this.properties = new ModelRenderProperties(isGui3d, SpriteUtil.missingSprite().get(), transforms);
        consumerRunOnTextureStitchEvent$Post.accept(() -> this.quads = normalizeQuads(lazyLoadedQuads.get()));
    }

    private ModelRenderProperties createProperties(@Nullable List<BakedQuad> quads, ItemTransforms transforms, boolean isGui3d) {
        List<BakedQuad> normalized = normalizeQuads(quads);
        this.quads = normalized;
        TextureAtlasSprite particle = normalized.isEmpty()
                ? SpriteUtil.missingSprite().get()
                : normalized.get(0).getSprite();
        return new ModelRenderProperties(isGui3d, particle, transforms);
    }

    private static List<BakedQuad> normalizeQuads(@Nullable List<BakedQuad> quads) {
        return quads == null ? ImmutableList.of() : quads;
    }

    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver resolver,
            ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        renderState.appendModelIdentityElement(this);
        ItemStackRenderState.LayerRenderState layer = renderState.newLayer();
        layer.setRenderType(Sheets.cutoutBlockSheet());
        properties.applyToLayer(layer, displayContext);
        layer.prepareQuadList().addAll(quads);
    }
}
