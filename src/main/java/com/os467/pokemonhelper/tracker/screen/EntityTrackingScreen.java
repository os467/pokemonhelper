package com.os467.pokemonhelper.tracker.screen;

import com.os467.pokemonhelper.tracker.EntityTracker;
import com.os467.pokemonhelper.tracker.config.Config;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class EntityTrackingScreen extends Screen {
    // UI组件
    private TextFieldWidget nameField;
    private TextFieldWidget rangeField;
    private ButtonWidget toggleButton;

    // 状态跟踪
    private String lastValidRange = String.valueOf(Config.DEFAULT_RANGE);
    private boolean isValidRange = true;

    public EntityTrackingScreen() {
        super(Text.literal("宝可梦追踪设置"));
    }

    @Override
    protected void init() {
        super.init();
        int centerY = this.height / 2;

        // 宝可梦名称输入框
        this.nameField = new TextFieldWidget(
                textRenderer,
                width/2 - 100, centerY - 60,
                200, 20,
                Text.literal("输入追踪宝可梦名称（英文）")
        );
        nameField.setMaxLength(128);
        nameField.setText(Config.getTrackedEntityName());
        addDrawableChild(nameField);

        // 搜索范围输入框
        this.rangeField = new TextFieldWidget(
                textRenderer,
                width/2 - 100, centerY - 30,
                200, 20,
                Text.literal("搜索范围 (1-256)")
        );
        rangeField.setText(String.valueOf(Config.getSearchRange()));
        rangeField.setMaxLength(3);
        rangeField.setTextPredicate(text -> text.matches("[0-9]*")); // 只允许数字输入
        addDrawableChild(rangeField);

        // 确定按钮
        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("保存设置"),
                        button -> saveSettings()
                )
                        .position(width/2 - 100, centerY)
                        .size(90, 20)
                        .build()
        );

        // 启用/禁用按钮
        this.toggleButton = ButtonWidget.builder(
                getToggleButtonText(),
                button -> toggleTracking()
        )
                .position(width/2 + 10, centerY)
                .size(90, 20)
                .build();
        addDrawableChild(toggleButton);
    }

    private void saveSettings() {
        // 保存宝可梦名称
        Config.setTrackedEntityName(nameField.getText());

        // 验证并保存范围
        try {
            int range = Integer.parseInt(rangeField.getText());
            if (range < 1 || range > 256) {
                throw new NumberFormatException();
            }
            Config.setSearchRange(range);
            lastValidRange = rangeField.getText();
            isValidRange = true;
        } catch (NumberFormatException e) {
            isValidRange = false;
            rangeField.setText(lastValidRange); // 恢复有效值
        }

        EntityTracker.resetLastCheck(Config.getTrackedEntityNames());

        this.close();
    }

    private void toggleTracking() {
        Config.setTrackingEnabled(!Config.isTrackingEnabled());
        toggleButton.setMessage(getToggleButtonText());
    }

    private Text getToggleButtonText() {
        return Text.literal(Config.isTrackingEnabled() ? "§c禁用追踪" : "§a启用追踪");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderInGameBackground(context);
        super.render(context, mouseX, mouseY, delta);

        // 绘制标题
        context.drawCenteredTextWithShadow(
                textRenderer, title, width/2, height/2 - 90, 0xFFFFFF
        );

        // 范围输入提示
        if (nameField.getText().isEmpty()) {
            context.drawTextWithShadow(
                    textRenderer,
                    "输入宝可梦名称,多个目标用英文逗号隔开",
                    width/2 - 95, height/2 - 55,
                    0x808080
            );
        }

        // 范围输入提示
        if (rangeField.getText().isEmpty()) {
            context.drawTextWithShadow(
                    textRenderer,
                    "默认范围: " + Config.DEFAULT_RANGE,
                    width/2 - 95, height/2 - 25,
                    0x808080
            );
        }

        // 输入验证提示
        if (!isValidRange) {
            context.drawCenteredTextWithShadow(
                    textRenderer,
                    "§c请输入1-256之间的整数！",
                    width/2, height/2 + 30,
                    0xFF0000
            );
        }

        // 当前状态显示
        String status = String.format("当前状态: %s (范围: %d)",
                Config.isTrackingEnabled() ? "§a启用" : "§c禁用",
                Config.getSearchRange()
        );
        context.drawCenteredTextWithShadow(
                textRenderer,
                Text.literal(status),
                width/2, height/2 + 50,
                0xFFFFFF
        );
    }
}