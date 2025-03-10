package com.willfp.eco.core.recipe.recipes;

import com.willfp.eco.core.Eco;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.items.TestableItem;
import com.willfp.eco.core.recipe.Recipes;
import com.willfp.eco.core.recipe.parts.EmptyTestableItem;
import com.willfp.eco.core.recipe.parts.TestableStack;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Shaped 3x3 crafting recipe.
 */
public final class ShapedCraftingRecipe extends PluginDependent<EcoPlugin> implements CraftingRecipe {
    /**
     * Recipe parts.
     */
    private final List<TestableItem> parts;

    /**
     * The key of the recipe.
     */
    private final NamespacedKey key;

    /**
     * The key of the displayed recipe.
     */
    private final NamespacedKey displayedKey;

    /**
     * The recipe's output.
     */
    private final ItemStack output;

    private ShapedCraftingRecipe(@NotNull final EcoPlugin plugin,
                                 @NotNull final String key,
                                 @NotNull final List<TestableItem> parts,
                                 @NotNull final ItemStack output) {
        super(plugin);

        this.parts = parts;
        this.key = plugin.getNamespacedKeyFactory().create(key);
        this.displayedKey = plugin.getNamespacedKeyFactory().create(key + "_displayed");
        this.output = output;
    }

    @Override
    public boolean test(@NotNull final ItemStack[] matrix) {
        boolean matches = true;
        for (int i = 0; i < 9; i++) {
            if (!parts.get(i).matches(matrix[i])) {
                matches = false;
            }
        }

        return matches;
    }

    @Override
    public void register() {
        Recipes.register(this);

        Bukkit.getServer().removeRecipe(this.getKey());
        Bukkit.getServer().removeRecipe(this.getDisplayedKey());

        ShapedRecipe shapedRecipe = new ShapedRecipe(this.getKey(), this.getOutput());
        shapedRecipe.shape("012", "345", "678");
        for (int i = 0; i < 9; i++) {
            if (parts.get(i) instanceof EmptyTestableItem) {
                continue;
            }

            char character = String.valueOf(i).toCharArray()[0];
            shapedRecipe.setIngredient(character, parts.get(i).getItem().getType());
        }

        ShapedRecipe displayedRecipe = new ShapedRecipe(this.getDisplayedKey(), this.getOutput());
        displayedRecipe.shape("012", "345", "678");
        for (int i = 0; i < 9; i++) {
            if (parts.get(i) instanceof EmptyTestableItem) {
                continue;
            }

            char character = String.valueOf(i).toCharArray()[0];
            ItemStack item = parts.get(i).getItem();

            if (parts.get(i) instanceof TestableStack) {
                ItemMeta meta = item.getItemMeta();
                assert meta != null;

                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                assert lore != null;
                lore.add("");
                String add = Eco.getHandler().getEcoPlugin().getLangYml().getString("multiple-in-craft");
                add = add.replace("%amount%", String.valueOf(item.getAmount()));
                lore.add(add);
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            displayedRecipe.setIngredient(character, new RecipeChoice.ExactChoice(item));
        }

        Bukkit.getServer().addRecipe(shapedRecipe);
        Bukkit.getServer().addRecipe(displayedRecipe);
    }

    /**
     * Create a new recipe builder.
     *
     * @param plugin The plugin that owns the recipe.
     * @param key    The recipe key.
     * @return A new builder.
     */
    public static Builder builder(@NotNull final EcoPlugin plugin,
                                  @NotNull final String key) {
        return new Builder(plugin, key);
    }

    /**
     * Get the parts.
     *
     * @return The parts.
     */
    public List<TestableItem> getParts() {
        return this.parts;
    }

    /**
     * Get the key.
     *
     * @return The key.
     */
    public NamespacedKey getKey() {
        return this.key;
    }

    /**
     * Get the displayed key.
     *
     * @return The displayed key.
     */
    public NamespacedKey getDisplayedKey() {
        return this.displayedKey;
    }

    /**
     * Get the output.
     *
     * @return The output.
     */
    public ItemStack getOutput() {
        return this.output;
    }

    /**
     * Builder for recipes.
     */
    public static final class Builder {
        /**
         * The recipe parts.
         */
        private final List<TestableItem> recipeParts = new ArrayList<>(Arrays.asList(null, null, null, null, null, null, null, null, null)); // Jank

        /**
         * The output of the recipe.
         */
        private ItemStack output = null;

        /**
         * The key of the recipe.
         */
        private final String key;

        /**
         * The plugin that created the recipe.
         */
        private final EcoPlugin plugin;

        /**
         * Create a new recipe builder.
         *
         * @param plugin The plugin that owns the recipe.
         * @param key    The recipe key.
         */
        private Builder(@NotNull final EcoPlugin plugin,
                        @NotNull final String key) {
            this.key = key;
            this.plugin = plugin;
        }

        /**
         * Set a recipe part.
         *
         * @param position The position of the recipe within a crafting matrix.
         * @param part     The part of the recipe.
         * @return The builder.
         */
        public Builder setRecipePart(@NotNull final RecipePosition position,
                                     @NotNull final TestableItem part) {
            recipeParts.set(position.getIndex(), part);
            return this;
        }

        /**
         * Set a recipe part.
         *
         * @param position The position of the recipe within a crafting matrix.
         * @param part     The part of the recipe.
         * @return The builder.
         */
        public Builder setRecipePart(final int position,
                                     @NotNull final TestableItem part) {
            recipeParts.set(position, part);
            return this;
        }

        /**
         * Set the output of the recipe.
         *
         * @param output The output.
         * @return The builder.
         */
        public Builder setOutput(@NotNull final ItemStack output) {
            this.output = output;
            return this;
        }

        /**
         * Build the recipe.
         *
         * @return The built recipe.
         */
        public ShapedCraftingRecipe build() {
            for (int i = 0; i < 9; i++) {
                if (recipeParts.get(i) == null) {
                    recipeParts.set(i, new EmptyTestableItem());
                }
            }

            return new ShapedCraftingRecipe(plugin, key.toLowerCase(), recipeParts, output);
        }
    }
}
