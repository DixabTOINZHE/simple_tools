package com.simple_tools.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.simple_tools.Run;
import com.simple_tools.SimpleTools;
import com.simple_tools.datas.stItemComponents;
import com.simple_tools.datas.stWorldJson;
import com.simple_tools.tools.items.stTools;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class stCommands implements Run {
    public static void run(){
        command_register();
        SimpleTools.LOGGER.info("com.simple_tools.commands.Commands are loaded successfully!");
    }

    //st_settings block_placer max_diameter
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__block_placer__max_diameter = (CommandManager.literal("max_diameter")
            .then(CommandManager.argument("value", IntegerArgumentType.integer(15, 45))
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(stCommands::st_settings__block_placer__max_diameter)));
    //st_settings block_placer min_diameter
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__block_placer__min_diameter = (CommandManager.literal("min_diameter")
            .then(CommandManager.argument("value", IntegerArgumentType.integer(3, 13))
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(stCommands::st_settings__block_placer__min_diameter)));
    //st_settings block_placer
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__block_placer = (CommandManager.literal("block_placer")
            .then(st_settings__block_placer__max_diameter)
            .then(st_settings__block_placer__min_diameter)
            .executes(stCommands::st_settings__block_placer));

    //st_settings block_searcher max_diameter
    private static final LiteralArgumentBuilder<ServerCommandSource> st_setting__block_searcher__max_diameter = (CommandManager.literal("max_diameter")
            .then(CommandManager.argument("value", IntegerArgumentType.integer(15, 55))
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(stCommands::st_settings__block_searcher__max_diameter)));
    //st_settings block_searcher min_diameter
    private static final LiteralArgumentBuilder<ServerCommandSource> st_setting__block_searcher__min_diameter = (CommandManager.literal("min_diameter")
            .then(CommandManager.argument("value", IntegerArgumentType.integer(3, 13))
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(stCommands::st_settings__block_searcher__min_diameter)));
    //st_settings block_searcher show_position
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__block_searcher__show_position = (CommandManager.literal("show_position")
            .then(CommandManager.argument("value", BoolArgumentType.bool())
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(stCommands::st_settings__block_searcher__show_position)));
    //st_settings block_searcher
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__block_searcher = (CommandManager.literal("block_searcher")
            .then(st_setting__block_searcher__max_diameter)
            .then(st_setting__block_searcher__min_diameter)
            .then(st_settings__block_searcher__show_position)
            .executes(stCommands::st_settings__block_searcher));

    //st_settings slime_block_placer max_diameter
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__slime_block_placer__max_diameter = (CommandManager.literal("max_diameter")
            .then(CommandManager.argument("value", IntegerArgumentType.integer(5, 45))
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(stCommands::st_settings__slime_block_placer__max_diameter)));
    //st_settings slime_block_placer min_diameter
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__slime_block_placer__min_diameter = (CommandManager.literal("min_diameter")
            .then(CommandManager.argument("value", IntegerArgumentType.integer(1, 3))
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(stCommands::st_settings__slime_block_placer__min_diameter)));
    //st_settings slime_block_placer
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__slime_block_placer = (CommandManager.literal("slime_block_placer")
            .then(st_settings__slime_block_placer__max_diameter)
            .then(st_settings__slime_block_placer__min_diameter)
            .executes(stCommands::st_settings__slime_block_placer));

    //st_settings nether_portal_frame_generator max_height
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__nether_portal_frame_generator__max_height = (CommandManager.literal("max_height")
            .then(CommandManager.argument("value", IntegerArgumentType.integer(5, 23))
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(stCommands::st_settings__nether_portal_frame_generator__max_height)));
    //st_settings nether_portal_frame_generator min_height
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__nether_portal_frame_generator__min_height = (CommandManager.literal("min_height")
            .then(CommandManager.argument("value", IntegerArgumentType.integer(5, 23))
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(stCommands::st_settings__nether_portal_frame_generator__min_height)));
    //st_settings nether_portal_frame_generator max_width
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__nether_portal_frame_generator__max_width = (CommandManager.literal("max_width")
            .then(CommandManager.argument("value", IntegerArgumentType.integer(5, 23))
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(stCommands::st_settings__nether_portal_frame_generator__max_width)));
    //st_settings nether_portal_frame_generator min_width
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__nether_portal_frame_generator__min_width = (CommandManager.literal("min_width")
            .then(CommandManager.argument("value", IntegerArgumentType.integer(4, 23))
                    .requires(source -> source.hasPermissionLevel(3))
                    .executes(stCommands::st_settings__nether_portal_frame_generator__min_width)));
    //st_settings nether_portal_frame_generator
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__nether_portal_frame_generator = (CommandManager.literal("nether_portal_frame_generator")
            .then(st_settings__nether_portal_frame_generator__max_height)
            .then(st_settings__nether_portal_frame_generator__min_height)
            .then(st_settings__nether_portal_frame_generator__max_width)
            .then(st_settings__nether_portal_frame_generator__min_width)
            .executes(stCommands::st_settings__nether_portal_frame_generator));

    //st_settings player_stealer on
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__player_stealer__on = (CommandManager.literal("on")
            .executes(stCommands::st_settings__player_stealer__on));
    //st_settings player_stealer off
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__player_stealer__off = (CommandManager.literal("off")
            .executes(stCommands::st_settings__player_stealer__off));
    //st_settings player_stealer
    private static final LiteralArgumentBuilder<ServerCommandSource> st_settings__player_stealer = (CommandManager.literal("player_stealer")
            .then(st_settings__player_stealer__on)
            .then(st_settings__player_stealer__off)
            .executes(stCommands::st_settings__player_stealer));



    //st_settings
    private static final CommandRegistrationCallback st_settings = (dispatcher, registryAccess, environment) -> {
        dispatcher.register(CommandManager.literal("st_settings")
                .then(st_settings__block_placer)
                .then(st_settings__block_searcher)
                .then(st_settings__slime_block_placer)
                .then(st_settings__nether_portal_frame_generator)
                .then(st_settings__player_stealer)
                .executes(stCommands::st_settings));
    };

    public static void command_register(){CommandRegistrationCallback.EVENT.register(st_settings);}



    private static int st_settings(CommandContext<ServerCommandSource> context){

        Text Tst_settings = Text.translatable(
                "command.simple_tools.st_settings"
        );
        context.getSource().sendFeedback(
                () -> Tst_settings, false
        );
        return 1;

    }

    //st_settings block_placer
    private static int st_settings__block_placer(CommandContext<ServerCommandSource> context){

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        int block_placer_max_diameter = settings.block_placer_max_diameter;
        int block_placer_min_diameter = settings.block_placer_min_diameter;

        Text Tst_settings__block_placer = Text.translatable(
                "command.simple_tools.st_settings.block_placer",
                Integer.toString(block_placer_max_diameter),
                Integer.toString(block_placer_min_diameter)
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__block_placer, false
        );
        return 1;

    }
    //st_settings block_placer min_diameter
    private static int st_settings__block_placer__min_diameter(CommandContext<ServerCommandSource> context){

        int value = IntegerArgumentType.getInteger(context, "value");
        Text Tst_settings__block_placer__min_diameterADDEDONE = Text.translatable(
                "command.simple_tools.st_settings.block_placer.min_diameter.added"
        );

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        if (value%2 == 0){
            value += 1;
            context.getSource().sendFeedback(
                    () -> Tst_settings__block_placer__min_diameterADDEDONE, false
            );
        }
        settings.block_placer_min_diameter = value;
        Text Tst_settings__block_placer__min_diameter = Text.translatable(
                "command.simple_tools.st_settings.block_placer.min_diameter",
                Integer.toString(value)
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__block_placer__min_diameter, true
        );
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        List<ServerPlayerEntity> players = context.getSource().getServer().getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player:players){

            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < 36; i++){
                ItemStack stack = inventory.main.get(i);
                Item item = stack.getItem();
                if (item == stTools.BLOCK_PLACER){
                    if (stack.get(stItemComponents.BLOCK_PLACER_PLACING_DIAMETER) < value)
                        stack.set(stItemComponents.BLOCK_PLACER_PLACING_DIAMETER, value);
                }
            }

            ItemStack stack = inventory.offHand.get(0);
            Item item = stack.getItem();
            if (item == stTools.BLOCK_PLACER){
                if (stack.get(stItemComponents.BLOCK_PLACER_PLACING_DIAMETER) < value)
                    stack.set(stItemComponents.BLOCK_PLACER_PLACING_DIAMETER, value);
            }

        }

        return 1;
    }
    //st_settings block_placer max_diameter
    private static int st_settings__block_placer__max_diameter(CommandContext<ServerCommandSource> context){

        int value = IntegerArgumentType.getInteger(context, "value");
        Text Tst_settings__block_placer__max_diameterADDEDONE = Text.translatable(
                "command.simple_tools.st_settings.block_placer.max_diameter.added"
        );

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        if (value%2 == 0) {
            value += 1;
            context.getSource().sendFeedback(
                    () -> Tst_settings__block_placer__max_diameterADDEDONE, false
            );
        }
        settings.block_placer_max_diameter = value;
        Text Tst_settings__block_placer__max_diameter = Text.translatable(
                "command.simple_tools.st_settings.block_placer.max_diameter",
                Integer.toString(value)
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__block_placer__max_diameter, true
        );
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        List<ServerPlayerEntity> players = context.getSource().getServer().getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player:players){

            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < 36; i++){
                ItemStack stack = inventory.main.get(i);
                Item item = stack.getItem();
                if (item == stTools.BLOCK_PLACER){
                    if (stack.get(stItemComponents.BLOCK_PLACER_PLACING_DIAMETER) > value)
                        stack.set(stItemComponents.BLOCK_PLACER_PLACING_DIAMETER, value);
                }
            }

            ItemStack stack = inventory.offHand.get(0);
            Item item = stack.getItem();
            if (item == stTools.BLOCK_PLACER){
                if (stack.get(stItemComponents.BLOCK_PLACER_PLACING_DIAMETER) > value)
                    stack.set(stItemComponents.BLOCK_PLACER_PLACING_DIAMETER, value);
            }

        }

        return 1;
    }
    //st_settings block_searcher
    private static int st_settings__block_searcher(CommandContext<ServerCommandSource> context){

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        int block_searcher_max_diameter = settings.block_searcher_max_diameter;
        int block_searcher_min_diameter = settings.block_searcher_min_diameter;
        boolean block_searcher_show_position = settings.block_searcher_show_position;

        Text Tst_settings__block_searcher = Text.translatable(
                "command.simple_tools.st_settings.block_searcher",
                Integer.toString(block_searcher_max_diameter),
                Integer.toString(block_searcher_min_diameter),
                Boolean.toString(block_searcher_show_position)
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__block_searcher, false
        );
        return 1;
    }
    //st_settings block_searcher max_diameter
    private static int st_settings__block_searcher__max_diameter(CommandContext<ServerCommandSource> context){

        int value = IntegerArgumentType.getInteger(context, "value");
        Text Tst_settings__block_searcher__max_diameterADDED = Text.translatable(
                "command.simple_tools.st_settings.block_searcher.max_diameter.added"
        );

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        if (value%2 == 0) {
            value += 1;
            context.getSource().sendFeedback(
                    () -> Tst_settings__block_searcher__max_diameterADDED, false
            );
        }
        settings.block_searcher_max_diameter = value;
        Text Tst_settings__block_searcher__max_diameter = Text.translatable(
                "command.simple_tools.st_settings.block_searcher.max_diameter",
                Integer.toString(value)
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__block_searcher__max_diameter, true
        );
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        List<ServerPlayerEntity> players = context.getSource().getServer().getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player:players){

            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < 36; i++){
                ItemStack stack = inventory.main.get(i);
                Item item = stack.getItem();
                if (item == stTools.BLOCK_SEARCHER){
                    if (stack.get(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER) > value)
                        stack.set(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER, value);
                }
            }

            ItemStack stack = inventory.offHand.get(0);
            Item item = stack.getItem();
            if (item == stTools.BLOCK_SEARCHER){
                if (stack.get(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER) > value)
                    stack.set(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER, value);
            }

        }

        return 1;
    }
    //st_settings block_searcher min_diameter
    private static int st_settings__block_searcher__min_diameter(CommandContext<ServerCommandSource> context){

        int value = IntegerArgumentType.getInteger(context, "value");
        Text Tst_settings__block_searcher__min_diameterADDED = Text.translatable(
                "command.simple_tools.st_settings.block_searcher.min_diameter.added"
        );

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        if (value%2 == 0) {
            value += 1;
            context.getSource().sendFeedback(
                    () -> Tst_settings__block_searcher__min_diameterADDED, false
            );
        }
        settings.block_searcher_min_diameter = value;
        Text Tst_settings__block_searcher__min_diameter = Text.translatable(
                "command.simple_tools.st_settings.block_searcher.min_diameter",
                Integer.toString(value)
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__block_searcher__min_diameter, true
        );
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        List<ServerPlayerEntity> players = context.getSource().getServer().getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player:players){

            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < 36; i++){
                ItemStack stack = inventory.main.get(i);
                Item item = stack.getItem();
                if (item == stTools.BLOCK_SEARCHER){
                    if (stack.get(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER) < value)
                        stack.set(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER, value);
                }
            }

            ItemStack stack = inventory.offHand.get(0);
            Item item = stack.getItem();
            if (item == stTools.BLOCK_SEARCHER){
                if (stack.get(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER) < value)
                    stack.set(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER, value);
            }

        }

        return 1;
    }
    //st_settings block_searcher show_position
    private static int st_settings__block_searcher__show_position(CommandContext<ServerCommandSource> context){

        boolean value = BoolArgumentType.getBool(context, "value");
        Text Tst_settings__block_searcher__show_position = Text.translatable(
                "command.simple_tools.st_settings.block_searcher.show_position",
                Boolean.toString(value)
        );

        context.getSource().sendFeedback(
                () -> Tst_settings__block_searcher__show_position, true
        );

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        settings.block_searcher_show_position = value;
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        return 1;
    }
    //st_settings slime_block_placer
    private static int st_settings__slime_block_placer(CommandContext<ServerCommandSource> context){

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        int slime_block_placer_max_diameter = settings.slime_block_placer_max_diameter;
        int slime_block_placer_min_diameter = settings.slime_block_placer_min_diameter;

        Text Tst_settings__slime_block_placer = Text.translatable(
                "command.simple_tools.st_settings.slime_block_placer",
                Integer.toString(slime_block_placer_max_diameter),
                Integer.toString(slime_block_placer_min_diameter)
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__slime_block_placer, true
        );
        return 1;

    }
    //st_settings slime_block_placer min_diameter
    private static int st_settings__slime_block_placer__min_diameter(CommandContext<ServerCommandSource> context){

        int value = IntegerArgumentType.getInteger(context, "value");
        Text Tst_settings__slime_block_placer__min_diameterADDED = Text.translatable(
                "command.simple_tools.st_settings.slime_block_placer.min_diameter.added"
        );

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        if (value%2 == 0) {
            value += 1;
            context.getSource().sendFeedback(
                    () -> Tst_settings__slime_block_placer__min_diameterADDED, false
            );
        }
        settings.slime_block_placer_min_diameter = value;
        Text Tst_settings__slime_block_placer__min_diameter = Text.translatable(
                "command.simple_tools.st_settings.slime_block_placer.min_diameter",
                Integer.toString(value)
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__slime_block_placer__min_diameter, true
        );
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        List<ServerPlayerEntity> players = context.getSource().getServer().getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player:players){

            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < 36; i++){
                ItemStack stack = inventory.main.get(i);
                Item item = stack.getItem();
                if (item == stTools.SLIME_BLOCK_PLACER){
                    if (stack.get(stItemComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER) < value)
                        stack.set(stItemComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER, value);
                }
            }

            ItemStack stack = inventory.offHand.get(0);
            Item item = stack.getItem();
            if (item == stTools.SLIME_BLOCK_PLACER){
                if (stack.get(stItemComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER) < value)
                    stack.set(stItemComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER, value);
            }

        }

        return 1;
    }
    //st_settings slime_block_placer max_diameter
    private static int st_settings__slime_block_placer__max_diameter(CommandContext<ServerCommandSource> context){

        int value = IntegerArgumentType.getInteger(context, "value");
        Text Tst_settings__slime_block_placer__max_diameterADDED = Text.translatable(
                "command.simple_tools.st_settings.slime_block_placer.max_diameter.added"
        );

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        if (value%2 == 0) {
            value += 1;
            context.getSource().sendFeedback(
                    () -> Tst_settings__slime_block_placer__max_diameterADDED, false
            );
        }
        settings.slime_block_placer_max_diameter = value;
        Text Tst_settings__slime_block_placer__max_diameter = Text.translatable(
                "command.simple_tools.st_settings.slime_block_placer.max_diameter",
                Integer.toString(value)
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__slime_block_placer__max_diameter, true
        );
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        List<ServerPlayerEntity> players = context.getSource().getServer().getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player:players){

            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < 36; i++){
                ItemStack stack = inventory.main.get(i);
                Item item = stack.getItem();
                if (item == stTools.SLIME_BLOCK_PLACER){
                    if (stack.get(stItemComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER) > value)
                        stack.set(stItemComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER, value);
                }
            }

            ItemStack stack = inventory.offHand.get(0);
            Item item = stack.getItem();
            if (item == stTools.SLIME_BLOCK_PLACER){
                if (stack.get(stItemComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER) > value)
                    stack.set(stItemComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER, value);
            }

        }

        return 1;
    }
    //st_settings nether_portal_frame_generator
    private static int st_settings__nether_portal_frame_generator(CommandContext<ServerCommandSource> context){

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));

        Text Tst_settings__nether_portal_frame_generator = Text.translatable(
                "command.simple_tools.st_settings.nether_portal_frame_generator",
                Integer.toString(settings.nether_portal_frame_generator_max_height),
                Integer.toString(settings.nether_portal_frame_generator_min_height),
                Integer.toString(settings.nether_portal_frame_generator_max_width),
                Integer.toString(settings.nether_portal_frame_generator_min_width)
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__nether_portal_frame_generator, false
        );
        return 1;
    }
    //st_settings nether_portal_frame_generator max_height
    private static int st_settings__nether_portal_frame_generator__max_height(CommandContext<ServerCommandSource> context){

        int value = IntegerArgumentType.getInteger(context, "value");
        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        int min = settings.nether_portal_frame_generator_min_height;

        Text Tst_settings__nether_portal_frame_generator__max_height = Text.translatable(
                "command.simple_tools.st_settings.nether_portal_frame_generator.max_height",
                Integer.toString(value)
        );
        Text Tst_settings__nether_portal_frame_generator__max_heightFAIL = Text.translatable(
                "command.simple_tools.st_settings.nether_portal_frame_generator.max_height.fail",
                Integer.toString(value),
                Integer.toString(min)
        );
        if (value >= min) {
            settings.nether_portal_frame_generator_max_height = value;
            context.getSource().sendFeedback(
                    () -> Tst_settings__nether_portal_frame_generator__max_height, true
            );

        } else context.getSource().sendFeedback(
                () -> Tst_settings__nether_portal_frame_generator__max_heightFAIL, false
        );
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        return 1;
    }
    //st_settings nether_portal_frame_generator min_height
    private static int st_settings__nether_portal_frame_generator__min_height(CommandContext<ServerCommandSource> context){

        int value = IntegerArgumentType.getInteger(context, "value");
        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        int max = settings.nether_portal_frame_generator_max_height;

        Text Tst_settings__nether_portal_frame_generator__min_height = Text.translatable(
                "command.simple_tools.st_settings.nether_portal_frame_generator.min_height",
                Integer.toString(value)
        );
        Text Tst_settings__nether_portal_frame_generator__min_heightFAIL = Text.translatable(
                "command.simple_tools.st_settings.nether_portal_frame_generator.min_height.fail",
                Integer.toString(value),
                Integer.toString(max)
        );
        if (value <= max) {
            settings.nether_portal_frame_generator_min_height = value;
            context.getSource().sendFeedback(
                    () -> Tst_settings__nether_portal_frame_generator__min_height, true
            );
        } else context.getSource().sendFeedback(
                () -> Tst_settings__nether_portal_frame_generator__min_heightFAIL, false
        );
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        return 1;
    }
    //st_settings nether_portal_frame_generator max_width
    private static int st_settings__nether_portal_frame_generator__max_width(CommandContext<ServerCommandSource> context){

        int value = IntegerArgumentType.getInteger(context, "value");
        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        int min = settings.nether_portal_frame_generator_min_width;

        Text Tst_settings__nether_portal_frame_generator__max_width = Text.translatable(
                "command.simple_tools.st_settings.nether_portal_frame_generator.max_width",
                Integer.toString(value)
        );
        Text Tst_settings__nether_portal_frame_generator__max_widthFAIL = Text.translatable(
                "command.simple_tools.st_settings.nether_portal_frame_generator.max_width.fail",
                Integer.toString(value),
                Integer.toString(min)
        );
        if (value >= min) {
            settings.nether_portal_frame_generator_max_width = value;
            context.getSource().sendFeedback(
                    () -> Tst_settings__nether_portal_frame_generator__max_width, true
            );

        } else context.getSource().sendFeedback(
                () -> Tst_settings__nether_portal_frame_generator__max_widthFAIL, false
        );
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        return 1;
    }
    //st_settings nether_portal_frame_generator min_width
    private static int st_settings__nether_portal_frame_generator__min_width(CommandContext<ServerCommandSource> context){

        int value = IntegerArgumentType.getInteger(context, "value");
        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        int max = settings.nether_portal_frame_generator_max_width;

        Text Tst_settings__nether_portal_frame_generator__min_width = Text.translatable(
                "command.simple_tools.st_settings.nether_portal_frame_generator.min_width",
                Integer.toString(value)
        );
        Text Tst_settings__nether_portal_frame_generator__min_widthFAIL = Text.translatable(
                "command.simple_tools.st_settings.nether_portal_frame_generator.min_width.fail",
                Integer.toString(value),
                Integer.toString(max)
        );
        if (value <= max) {
            settings.nether_portal_frame_generator_min_width = value;
            context.getSource().sendFeedback(
                    () -> Tst_settings__nether_portal_frame_generator__min_width, true
            );
        } else context.getSource().sendFeedback(
                () -> Tst_settings__nether_portal_frame_generator__min_widthFAIL, false
        );
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        return 1;
    }
    //st_settings player_stealer
    private static int st_settings__player_stealer(CommandContext<ServerCommandSource> context){

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));

        Text Tst_settings__player_stealer = Text.translatable(
                "command.simple_tools.st_settings.player_stealer",
                Boolean.toString(settings.player_stealer_enabled)
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__player_stealer, false
        );
        return 1;
    }
    //st_settings player_stealer on
    private static int st_settings__player_stealer__on(CommandContext<ServerCommandSource> context){

        Text Tst_settings__player_stealer__on = Text.translatable(
                "command.simple_tools.st_settings.player_stealer.on"
        );
        context.getSource().sendFeedback(
                () -> Tst_settings__player_stealer__on, true
        );

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        settings.player_stealer_enabled = true;
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        return 1;
    }
    //st_settings player_stealer off
    private static int st_settings__player_stealer__off(CommandContext<ServerCommandSource> context){

        Text Tst_settings__player_stealer__off = Text.translatable(
                "command.simple_tools.st_settings.player_stealer.off"
        );

        context.getSource().sendFeedback(
                () -> Tst_settings__player_stealer__off, true
        );

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(context.getSource().getWorld()));
        settings.player_stealer_enabled = false;
        stWorldJson.save(settings, stWorldJson.get_path(context.getSource().getWorld()));

        return 1;
    }
}
