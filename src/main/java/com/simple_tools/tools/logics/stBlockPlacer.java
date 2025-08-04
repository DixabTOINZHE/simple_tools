package com.simple_tools.tools.logics;

import com.simple_tools.components.stComponents;
import com.simple_tools.tags.stBlockTags;
import com.simple_tools.tools.values.stValues;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class stBlockPlacer extends Item {

    private final Text place_mode_1 = Text.translatable(
            "item.simple_tools.block_placer.place_mode.mode_1"
    );
    private final Text place_mode_2 = Text.translatable(
            "item.simple_tools.block_placer.place_mode.mode_2"
    );
    private final Text place_mode_3 = Text.translatable(
            "item.simple_tools.block_placer.place_mode.mode_3"
    );
    private final Text place_mode_4 = Text.translatable(
            "item.simple_tools.block_placer.place_mode.mode_4"
    );
    private final Text changed_diameter_pre = Text.translatable(
            "item.simple_tools.block_placer.changed_diameter.pre"
    );
    private final Text changed_diameter_suf = Text.translatable(
            "item.simple_tools.block_placer.changed_diameter.suf"
    );

    public stBlockPlacer(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        return super.getDefaultStack();
    }

    @Override
    public ActionResult useOnEntity(ItemStack using_stack, PlayerEntity using_player, LivingEntity entity, Hand hand) {
        World world = using_player.getWorld();
        if (!world.isClient()) {
            int diameter = using_player.getStackInHand(hand).get(stComponents.BLOCK_PLACER_PLACING_DIAMETER);
            if (change_mode_or_diameter(using_stack, using_player, hand, changed_diameter_pre, diameter, changed_diameter_suf, place_mode_1, place_mode_2, place_mode_3, place_mode_4))
                return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity using_player, Hand hand) {
        if (!world.isClient()) {
            int diameter = using_player.getStackInHand(hand).get(stComponents.BLOCK_PLACER_PLACING_DIAMETER);
            ItemStack using_stack = using_player.getStackInHand(hand);
            if (change_mode_or_diameter(using_stack, using_player, hand, changed_diameter_pre, diameter, changed_diameter_suf, place_mode_1, place_mode_2, place_mode_3, place_mode_4))
                return TypedActionResult.success(using_stack);
        }
        ItemStack using_stack = using_player.getStackInHand(hand);
        return TypedActionResult.success(using_stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        //获取信息
        Hand hand = context.getHand();
        BlockPos block_pos = context.getBlockPos();
        PlayerEntity using_player = context.getPlayer();
        PlayerInventory using_player_inventory = using_player.getInventory();
        World environment = context.getWorld();
        RegistryKey<World> dimension = environment.getRegistryKey();
        ItemStack offhand_item = using_player.getStackInHand(Hand.OFF_HAND);
        Item offhand_itemItem = offhand_item.getItem();
        String offhand_item_name = offhand_itemItem.getName().getString();
        ItemStack using_stack = using_player.getStackInHand(hand);
        int diameter = using_player.getStackInHand(hand).get(stComponents.BLOCK_PLACER_PLACING_DIAMETER);
        Direction side = context.getSide();
        int sdx = 0;
        int sdz = 0;
        int sdy = 0;
        if (side == Direction.EAST) sdx += 1;
        else if (side == Direction.WEST) sdx -=1;
        else if (side == Direction.UP) sdy +=1;
        else if (side == Direction.DOWN) sdy -= 1;
        else if (side == Direction.SOUTH) sdz += 1;
        else sdz -= 1;

        //设置文本
        Text offhand_empty_error = Text.translatable(
                "item.simple_tools.block_placer.offhand_empty_error"
        );
        Text offhand_is_not_block_error = Text.translatable(
                "item.simple_tools.block_placer.offhand_is_not_block_error"
        );
        Text block_is_not_enough_error = Text.translatable(
                "item.simple_tools.block_placer.block_is_not_enough_error",
                offhand_itemItem.getName().getString()
        );
        Text out_of_the_world_error = Text.translatable(
                "item.simple_tools.block_placer.out_of_the_world_error"
        );
        Text not_supported_block_error = Text.translatable(
                "item.simple_tools.block_placer.not_supported_block_error",
                offhand_itemItem.getName().getString()
        );
        Text start = Text.translatable(
                "item.simple_tools.block_placer.start"
        );
        Text finish = Text.translatable(
                "item.simple_tools.block_placer.finish"
        );

        if (!environment.isClient) {

            if (change_mode_or_diameter(using_stack, using_player, hand, changed_diameter_pre, diameter, changed_diameter_suf, place_mode_1, place_mode_2, place_mode_3, place_mode_4)) return ActionResult.SUCCESS;

            //特殊情况：副手物品为空
            if (offhand_item == ItemStack.EMPTY) {

                using_player.sendMessage(Text.of("--------------------"));

                using_player.sendMessage(offhand_empty_error);

                using_player.sendMessage(Text.of("--------------------"));

                return ActionResult.SUCCESS;
            }
            //特殊情况：副手物品不是方块

            if (!(offhand_itemItem instanceof BlockItem)) {
                using_player.sendMessage(Text.of("--------------------"));

                using_player.sendMessage(offhand_is_not_block_error);

                using_player.sendMessage(Text.of("--------------------"));

                return ActionResult.SUCCESS;
            }

            //放置
            else if (!((offhand_itemItem.getDefaultStack().isIn(ItemTags.DOORS))||(offhand_itemItem.getDefaultStack().isIn(ItemTags.BEDS)))){

                //半径
                int radius = (diameter-1)/2;

                using_player.sendMessage(Text.of("--------------------"));

                if (using_player.isCreative()||(block_is_enough(offhand_itemItem, diameter, using_player_inventory, using_stack.get(stComponents.BLOCK_PLACER_PLACING_MODE)))){

                    int number_of_placed_block = 0;
                    //水平放置
                    if (using_stack.get(stComponents.BLOCK_PLACER_PLACING_MODE) == 1){

                        using_player.sendMessage(start);
                        using_player.sendMessage(place_mode_1);

                        for (int dx = -1*radius; dx <= radius; dx++){
                            for (int dz = -1*radius; dz <= radius; dz++){

                                BlockPos object_block = new BlockPos(block_pos.getX()+dx+sdx, block_pos.getY()+sdy, block_pos.getZ()+dz+sdz);
                                BlockState object_state = environment.getBlockState(object_block);
                                String object_name = object_state.getBlock().asItem().getName().getString();

                                if ((using_player.isCreative())||(can_place(object_state))) {
                                    change_block_state(environment, object_block, Block.getBlockFromItem(offhand_itemItem));
                                    Text placed = Text.translatable(
                                            "item.simple_tools.block_placer.placed",
                                            offhand_item_name,
                                            object_name
                                    );
                                    using_player.sendMessage(placed);
                                    number_of_placed_block++;
                                } else {
                                    Text failed_to_place = Text.translatable(
                                            "item.simple_tools.block_placer.failed_to_place",
                                            offhand_item_name,
                                            object_name
                                    );
                                    using_player.sendMessage(failed_to_place);
                                }

                            }
                        }
                    }

                    //竖起x方向
                    else if (using_stack.get(stComponents.BLOCK_PLACER_PLACING_MODE) == 2) {

                        using_player.sendMessage(start);

                        if (allowed_to_place(dimension, radius, block_pos)){

                            using_player.sendMessage(place_mode_2);

                            for (int dx = -1*radius; dx <= radius; dx++){
                                for (int dy = -1*radius; dy <= radius; dy++){

                                    BlockPos object_block = new BlockPos(block_pos.getX()+dx+sdx, block_pos.getY()+dy+sdy, block_pos.getZ()+sdz);
                                    BlockState object_state = environment.getBlockState(object_block);
                                    String object_name = object_state.getBlock().asItem().getName().getString();

                                    if ((using_player.isCreative())||(can_place(object_state))) {
                                        change_block_state(environment, object_block, Block.getBlockFromItem(offhand_itemItem));
                                        Text placed = Text.translatable(
                                                "item.simple_tools.block_placer.placed",
                                                offhand_item_name,
                                                object_name
                                        );
                                        using_player.sendMessage(placed);
                                        number_of_placed_block++;
                                    } else {
                                        Text failed_to_place = Text.translatable(
                                                "item.simple_tools.block_placer.failed_to_place",
                                                offhand_item_name,
                                                object_name
                                        );
                                        using_player.sendMessage(failed_to_place);
                                    }
                                }
                            }
                        } else {
                            using_player.sendMessage(out_of_the_world_error);
                        }
                    }

                    //竖起z方向
                    else if (using_stack.get(stComponents.BLOCK_PLACER_PLACING_MODE) == 3) {

                        using_player.sendMessage(start);

                        if (allowed_to_place(dimension, radius, block_pos)){

                            using_player.sendMessage(place_mode_3);

                            for (int dz = -1*radius; dz <= radius; dz++){
                                for (int dy = -1*radius; dy <= radius; dy++){

                                    BlockPos object_block = new BlockPos(block_pos.getX()+sdx, block_pos.getY()+dy+sdy, block_pos.getZ()+dz+sdz);;
                                    BlockState object_state = environment.getBlockState(object_block);
                                    String object_name = object_state.getBlock().asItem().getName().getString();

                                    if ((using_player.isCreative())||(can_place(object_state))) {
                                        change_block_state(environment, object_block, Block.getBlockFromItem(offhand_itemItem));
                                        Text placed = Text.translatable(
                                                "item.simple_tools.block_placer.placed",
                                                offhand_item_name,
                                                object_name
                                        );
                                        using_player.sendMessage(placed);
                                        number_of_placed_block++;
                                    } else {
                                        Text failed_to_place = Text.translatable(
                                                "item.simple_tools.block_placer.failed_to_place",
                                                offhand_item_name,
                                                object_name
                                        );
                                        using_player.sendMessage(failed_to_place);
                                    }
                                }
                            }
                        } else {
                            using_player.sendMessage(out_of_the_world_error);
                        }
                    }

                    //立方体
                    else {

                        using_player.sendMessage(start);

                        if (allowed_to_place(dimension, radius, block_pos)){

                            using_player.sendMessage(place_mode_4);

                            for (int dx = -1*radius; dx <= radius; dx++){
                                for (int dy = -1*radius; dy <= radius; dy++){
                                    for (int dz = -1*radius; dz <= radius; dz++){

                                        BlockPos object_block = new BlockPos(block_pos.getX()+dx+sdx, block_pos.getY()+dy+sdy, block_pos.getZ()+dz+sdz);
                                        BlockState object_state = environment.getBlockState(object_block);
                                        String object_name = object_state.getBlock().asItem().getName().getString();

                                        if ((using_player.isCreative())||(can_place(object_state))) {
                                            change_block_state(environment, object_block, Block.getBlockFromItem(offhand_itemItem));
                                            Text placed = Text.translatable(
                                                    "item.simple_tools.block_placer.placed",
                                                    offhand_item_name,
                                                    object_name
                                            );
                                            using_player.sendMessage(placed);
                                            number_of_placed_block++;
                                        } else {
                                            Text failed_to_place = Text.translatable(
                                                    "item.simple_tools.block_placer.failed_to_place",
                                                    offhand_item_name,
                                                    object_name
                                            );
                                            using_player.sendMessage(failed_to_place);
                                        }
                                    }
                                }
                            }
                        } else {
                            using_player.sendMessage(out_of_the_world_error);
                        }
                    }

                    context.getStack().damage(radius, using_player, EquipmentSlot.MAINHAND);
                    using_player.sendMessage(finish);

                    //清除物品
                    if (!(using_player.isCreative())){
                        using_player_inventory.remove(stack -> stack.getItem() == offhand_itemItem, number_of_placed_block, null);
                    }

                } else {
                    using_player.sendMessage(block_is_not_enough_error);
                }

                using_player.sendMessage(Text.of("--------------------"));
            } else {

                using_player.sendMessage(Text.of("--------------------"));
                using_player.sendMessage(not_supported_block_error);
                using_player.sendMessage(Text.of("--------------------"));

                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.SUCCESS;
    }

    //检测物品数量
    private boolean block_is_enough(Item block, int diameter, PlayerInventory using_player_inventory, int mode){

        int number_of_item = 0;

        for (int finder=0;finder<=35;finder++){
            ItemStack relative_item_stack = using_player_inventory.main.get(finder);
            Item relative_item = relative_item_stack.getItem();
            if (relative_item == block){
                int count_of_relative_item=relative_item_stack.getCount();
                number_of_item = number_of_item+count_of_relative_item;
            }
        }

        if(using_player_inventory.offHand.get(0).getItem()==block){
            number_of_item = number_of_item+using_player_inventory.offHand.get(0).getCount();
        }

        if (mode!=4){
            return number_of_item>=diameter*diameter;
        } else {
            return number_of_item>=diameter*diameter*diameter;
        }
    }

    //放方块
    private void change_block_state(World environment, BlockPos block_pos, Block placed_block){
        environment.setBlockState(block_pos, placed_block.getDefaultState(), 3);
    }

    //检测放置位置
    private boolean allowed_to_place(RegistryKey<World> dimension, int radius, BlockPos block_pos){
        if (dimension == World.OVERWORLD){
            return (block_pos.getY()-radius >= -64)&&(block_pos.getY()+radius <= 319);
        } else {
            //0, 255
            return (block_pos.getY()-radius >= 0)&&(block_pos.getY()+radius <= 255);
        }
    }

    //检测能否放置
    private boolean can_place(BlockState state){
        return state.isIn(stBlockTags.PLACEABLE_BLOCKS);
    }

    //更改模式和半径
    private boolean change_mode_or_diameter(ItemStack using_stack, PlayerEntity using_player, Hand hand, Text changed_diameter_pre, int diameter, Text changed_diameter_suf, Text place_mode_1, Text place_mode_2, Text place_mode_3, Text place_mode_4){

        //Alt+右键：更改半径
        if (Screen.hasAltDown()){

            using_player.sendMessage(Text.of("--------------------"));
            using_player.getStackInHand(hand).set(stComponents.BLOCK_PLACER_PLACING_DIAMETER, stValues.change_block_placer_place_diameter(diameter));
            using_player.sendMessage(Text.of(changed_diameter_pre.getString()+ using_player.getStackInHand(hand).get(stComponents.BLOCK_PLACER_PLACING_DIAMETER)+changed_diameter_suf.getString()));

            using_player.sendMessage(Text.of("--------------------"));
            return true;
        }
        //Ctrl+右键：更改放置模式
        else if (Screen.hasControlDown()) {

            using_player.sendMessage(Text.of("--------------------"));
            using_player.getStackInHand(hand).set(stComponents.BLOCK_PLACER_PLACING_MODE, stValues.change_block_placer_place_mode(using_stack.get(stComponents.BLOCK_PLACER_PLACING_MODE)));

            if (using_stack.get(stComponents.BLOCK_PLACER_PLACING_MODE) == 1){
                using_player.sendMessage(place_mode_1);
            } else if (using_stack.get(stComponents.BLOCK_PLACER_PLACING_MODE) == 2) {
                using_player.sendMessage(place_mode_2);
            } else if (using_stack.get(stComponents.BLOCK_PLACER_PLACING_MODE) == 3) {
                using_player.sendMessage(place_mode_3);
            } else {
                using_player.sendMessage(place_mode_4);
            }

            using_player.sendMessage(Text.of("--------------------"));
            return true;
        }
        return false;

    }

    //添加工具信息
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        //设置文本

        String place_mode_tip_key = "";
        if (stack.get(stComponents.BLOCK_PLACER_PLACING_MODE) == 1){
            place_mode_tip_key = "item.simple_tools.block_placer.tip.mode_1";
        } else if (stack.get(stComponents.BLOCK_PLACER_PLACING_MODE) == 2){
            place_mode_tip_key = "item.simple_tools.block_placer.tip.mode_2";
        } else if (stack.get(stComponents.BLOCK_PLACER_PLACING_MODE) == 3){
            place_mode_tip_key = "item.simple_tools.block_placer.tip.mode_3";
        } else if (stack.get(stComponents.BLOCK_PLACER_PLACING_MODE) == 4){
            place_mode_tip_key = "item.simple_tools.block_placer.tip.mode_4";
        }

        Text tip_place_mode = Text.translatable(place_mode_tip_key);
        Text tip_diameter = Text.translatable(
                "item.simple_tools.block_placer.tip.diameter",
                "\u00A72"+Integer.toString(stack.get(stComponents.BLOCK_PLACER_PLACING_DIAMETER))
        );
        Text tip = Text.translatable(
                "item.simple_tools.block_placer.tip.normally",
                tip_place_mode.getString()
        );
        Text tip_alt1 = Text.translatable(
                "item.simple_tools.block_placer.tip.alt.line1"
        );
        Text tip_alt2 = Text.translatable(
                "item.simple_tools.block_placer.tip.alt.line2"
        );
        Text tip_alt3 = Text.translatable(
                "item.simple_tools.block_placer.tip.alt.line3"
        );
        Text tip_alt4 = Text.translatable(
                "item.simple_tools.block_placer.tip.alt.line4"
        );

        if ((Screen.hasAltDown())) {
            tooltip.add(net.minecraft.text.Text.of(""));
            tooltip.add(tip_diameter);
            tooltip.add(tip_place_mode);
            tooltip.add(tip_alt3);
            tooltip.add(tip_alt4);
            tooltip.add(tip_alt1);
            tooltip.add(tip_alt2);
            tooltip.add(net.minecraft.text.Text.of(""));
        } else {
            tooltip.add(net.minecraft.text.Text.of(""));
            tooltip.add(tip_diameter);
            tooltip.add(tip_place_mode);
            tooltip.add(tip);
            tooltip.add(net.minecraft.text.Text.of(""));
        }
    }
}
