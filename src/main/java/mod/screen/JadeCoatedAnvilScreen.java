package mod.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import mod.Mod;

public class JadeCoatedAnvilScreen extends HandledScreen<JadeCoatedAnvilScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(Mod.MOD_ID, "textures/gui/container/jade_coated_anvil_gui.png");

    public JadeCoatedAnvilScreen(JadeCoatedAnvilScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 166; // Główne GUI 176x166
        this.backgroundWidth = 176;
        this.playerInventoryTitleY = 72; // "Inventory" text pozycja
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        // Rysuj główne GUI (176x166 z tekstury 204x198)
        context.drawTexture(TEXTURE, x, y, 0, 0, 176, 166, 204, 198);
        //                                    ^  ^  ^^^  ^^^  ^^^  ^^^
        //                                    |  |   |    |    |    |
        //                                    u  v  width height textureWidth textureHeight
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);


        // Sprawdź czy można craftować
        boolean canCraft = checkIfCanCraft();

        if (!canCraft) {
            // Narysuj czerwony X (strzałka zakaz) - z pozycji 176,0 w teksturze
            context.drawTexture(
                    TEXTURE,
                    98,  // Pozycja X output slotu
                    49,   // Pozycja Y output slotu
                    176,  // X w teksturze (strzałka zaczyna się od x=176)
                    0,    // Y w teksturze (strzałka zaczyna się od y=0)
                    28,   // Szerokość strzałki
                    21,   // Wysokość strzałki
                    204,  // Szerokość całej tekstury
                    198   // Wysokość całej tekstury
            );

        }

    }

    private boolean checkIfCanCraft() {
        // Sprawdź czy są WSZYSTKIE wymagane itemy
        // Jeśli TAK - pokaż output (strzałka znika)
        // Jeśli NIE - pokaż strzałkę zakazu (output zakryty)

        boolean hasInput1 = handler.getSlot(0).hasStack();
        boolean hasInput2 = handler.getSlot(1).hasStack();

        // Craftowanie możliwe tylko gdy są oba itemy
        return hasInput1 && hasInput2;
    }

}