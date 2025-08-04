package com.simple_tools.tools.logics;

import com.simple_tools.components.stComponents;
import com.simple_tools.tags.stBlockTags;
import com.simple_tools.tools.items.stTools;
import com.simple_tools.tools.values.stValues;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class stSlimeBlockPlacer extends Item {
    public stSlimeBlockPlacer(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity using_player, Hand hand) {

        //获取信息
        ItemStack stack = using_player.getStackInHand(hand);

        //执行操作
        place_slime_block(using_player, stack);

        return TypedActionResult.success(stack);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity using_player, LivingEntity entity, Hand hand) {

        //执行操作
        place_slime_block(using_player, stack);

        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        //获取信息
        PlayerEntity using_player = context.getPlayer();

        //执行操作
        place_slime_block(using_player, context.getStack());

        return ActionResult.SUCCESS;
    }

    private void place_slime_block(PlayerEntity using_player, ItemStack used_stack){

        BlockPos pos_under_player = using_player.getBlockPos();
        BlockPos relative_pos = new BlockPos(pos_under_player.getX(), pos_under_player.getY()-1, pos_under_player.getZ());
        //设置文本
        Text no_slime_block_error = Text.translatable(
                "item.simple_tools.slime_block_placer.no_slime_block_error"
        );
        Text no_space_error = Text.translatable(
                "item.simple_tools.slime_block_placer.no_space_error"
        );
        Text placed_msg = Text.translatable(
                "item.simple_tools.slime_block_placer.placed"
        );

        if (!(using_player.getWorld().isClient())) {

            //获取史莱姆数量
            int number_of_slime_block = 0;

            for (int slime_block_finder=0;slime_block_finder<=35;slime_block_finder++){
                ItemStack relative_item_stack = using_player.getInventory().main.get(slime_block_finder);
                Item relative_item = relative_item_stack.getItem();
                if (relative_item == Items.SLIME_BLOCK){
                    int count_of_relative_item=relative_item_stack.getCount();
                    number_of_slime_block = number_of_slime_block+count_of_relative_item;
                }
            }

            if(using_player.getInventory().offHand.get(0).getItem()==Items.SLIME_BLOCK){
                number_of_slime_block = number_of_slime_block+using_player.getInventory().offHand.get(0).getCount();
            }

            if (using_player.isCreative()){
                number_of_slime_block=Integer.MAX_VALUE;
            }

            using_player.sendMessage(Text.of("--------------------"));

            //Ctrl + 右键：切换半径
            if (Screen.hasControlDown()){
                used_stack.set(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER, stValues.change_slime_block_place_diameter(used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER)));
                Text changed_place_diameter = Text.translatable(
                        "item.simple_tools.slime_block_placer.changed_place_diameter",
                        Integer.toString(used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER))
                );
                using_player.sendMessage(changed_place_diameter);
                using_player.sendMessage(Text.of("--------------------"));
                return;
            }

            //放置粘液块
            if (number_of_slime_block >= used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER)* used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER)) {

                boolean placed = false;

                //没空间的解决办法：
                if (!can_place(used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER), using_player.getWorld(), relative_pos)){
                    using_player.sendMessage(no_space_error);
                    using_player.sendMessage(Text.of("--------------------"));
                    return;
                }

                for (int dy = 0; dy <= relative_pos.getY() + 64; dy++) {

                    BlockPos object_block = new BlockPos(relative_pos.getX(), relative_pos.getY() - dy, relative_pos.getZ());
                    if (!can_place(used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER), using_player.getWorld(), object_block)) {
                        BlockPos place_pos = object_block.add(0, 1, 0);
                        place(using_player.getWorld(), place_pos, used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER));
                        placed = true;
                        break;
                    }
                }

                //推到世界底部
                if (!(placed)) {
                    if (using_player.getWorld().getRegistryKey() == World.OVERWORLD){
                        place(using_player.getWorld(), new BlockPos(relative_pos.getX(), -64, relative_pos.getZ()), used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER));
                    } else if ((using_player.getWorld().getRegistryKey() == World.NETHER)||(using_player.getWorld().getRegistryKey() == World.END)){
                        place(using_player.getWorld(), new BlockPos(relative_pos.getX(), 0, relative_pos.getZ()), used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER));
                    }
                }

                //移除史莱姆和耐久
                if (!(using_player.isCreative())) {
                    if (using_player.getMainHandStack().getItem() == stTools.SLIME_BLOCK_PLACER){
                        using_player.getMainHandStack().damage(used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER)* used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER),using_player, EquipmentSlot.MAINHAND);
                    }
                    using_player.getInventory().remove(stack -> stack.getItem() == Items.SLIME_BLOCK, used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER)* used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER), null);
                }

                using_player.sendMessage(placed_msg);
            } else {
                using_player.sendMessage(no_slime_block_error);
            }
            using_player.sendMessage(Text.of("--------------------"));
        }
    }

    //放方块
    private void change_block_state(World environment, BlockPos block_pos, Block placed_block){
        environment.setBlockState(block_pos, placed_block.getDefaultState(), 3);
    }
    private void place(World environment, BlockPos pos, int diameter){

        int radius = (diameter - 1)/2;

        if (diameter == 1){
            change_block_state(environment, pos, Blocks.SLIME_BLOCK);
        } else {
            for (int dx = -1*radius; dx <= radius; dx++){
                for (int dz = -1*radius; dz <= radius; dz++) {

                    BlockPos object_pos = pos.add(dx, 0 ,dz);
                    change_block_state(environment, object_pos, Blocks.SLIME_BLOCK);

                }
            }
        }
    }

    //检测是否能放
    private boolean can_place(int diameter, World environment, BlockPos pos){

        int radius = (diameter - 1)/2;
        BlockState block = environment.getBlockState(pos);

        if (diameter == 1){
            return block.isIn(stBlockTags.PLACEABLE_BLOCKS);
        } else {
            for (int dx = -1*radius; dx <= radius; dx++){
                for (int dz = -1*radius; dz <= radius; dz++) {

                    BlockPos object_pos = pos.add(dx, 0 ,dz);
                    block = environment.getBlockState(object_pos);
                    if (!block.isIn(stBlockTags.PLACEABLE_BLOCKS)){
                        return false;
                    }

                }
            }
        }

        return true;
    }

    //添加工具信息

    @Override
    public void appendTooltip(ItemStack used_stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(used_stack, context, tooltip, type);

        //设置文本
        Text tip = Text.translatable(
                "item.simple_tools.slime_block_placer.tip.normally"
        );
        Text tip_alt1 = Text.translatable(
                "item.simple_tools.slime_block_placer.tip.alt.line1"
        );
        Text tip_alt2 = Text.translatable(
                "item.simple_tools.slime_block_placer.tip.alt.line2"
        );
        Text diameter = Text.translatable(
                "item.simple_tools.slime_block_placer.tip.diameter",
                "\u00A72"+Integer.toString(used_stack.get(stComponents.SLIME_BLOCK_PLACER_PLACING_DIAMETER))
        );

        if (Screen.hasAltDown()){
            tooltip.add(Text.of(""));
            tooltip.add(diameter);
            tooltip.add(tip_alt1);
            tooltip.add(tip_alt2);
            tooltip.add(Text.of(""));
        } else {
            tooltip.add(Text.of(""));
            tooltip.add(diameter);
            tooltip.add(tip);
            tooltip.add(Text.of(""));
        }
    }
}
