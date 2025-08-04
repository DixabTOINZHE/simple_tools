package com.simple_tools.tools.logics;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class stPlayerStealer extends Item {
    public stPlayerStealer(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity using_player, LivingEntity object_player, Hand hand) {

        //获取信息
        World enviroment = using_player.getWorld();
        BlockPos under_block = using_player.getBlockPos();
        BlockPos get = under_block.add(0, 1, 0);
        String object_player_name = object_player.getName().getString();
        String using_player_name = using_player.getName().getString();

        //设置文本
        Text object_is_not_player_error = Text.translatable(
                "item.simple_tools.player_stealer.object_is_not_player_error"
        );
        Text start = Text.translatable(
                "item.simple_tools.player_stealer.start",
                object_player_name
        );
        Text finish = Text.translatable(
                "item.simple_tools.player_stealer.finish",
                object_player_name
        );
        Text be_stolen = Text.translatable(
                "item.simple_tools.player_stealer.be_stolen",
                using_player_name
        );

        //设置扣除的耐久值
        int damage = 0;

        if (!(enviroment.isClient())){

            if (object_player instanceof PlayerEntity){

                using_player.sendMessage(Text.of("--------------------"));
                using_player.sendMessage(start);

                //获取信息x2
                PlayerInventory inventory = ((PlayerEntity) object_player).getInventory();

                //窃取背包和快捷栏
                for (int finder = 0; finder <= 35; finder++){

                    ItemStack object_stack = inventory.main.get(finder);
                    String item_name = object_stack.getItem().getName().getString();
                    int item_count = object_stack.getCount();
                    stole_item(item_count, item_name, using_player, object_stack);
                    ItemEntity item = new ItemEntity(enviroment, get.getX(), get.getY(), get.getZ(), object_stack);
                    enviroment.spawnEntity(item);
                    damage++;

                }

                //窃取盔甲
                for (int finder = 0; finder <= 3; finder++){

                    ItemStack object_stack = inventory.armor.get(finder);
                    String item_name = object_stack.getItem().getName().getString();
                    int item_count = object_stack.getCount();
                    stole_item(item_count, item_name, using_player, object_stack);
                    ItemEntity item = new ItemEntity(enviroment, get.getX(), get.getY(), get.getZ(), object_stack);
                    enviroment.spawnEntity(item);
                    damage++;

                }

                //窃取副手
                {

                    ItemStack object_stack = inventory.offHand.get(0);
                    String item_name = object_stack.getItem().getName().getString();
                    int item_count = object_stack.getCount();
                    stole_item(item_count, item_name, using_player, object_stack);
                    ItemEntity item = new ItemEntity(enviroment, get.getX(), get.getY(), get.getZ(), object_stack);
                    enviroment.spawnEntity(item);
                    damage++;

                }

                //扣除耐久
                if (hand == Hand.MAIN_HAND){
                    stack.damage(damage, using_player, EquipmentSlot.MAINHAND);
                } else {
                    stack.damage(damage, using_player, EquipmentSlot.OFFHAND);
                }

                using_player.sendMessage(finish);
                using_player.sendMessage(Text.of("--------------------"));

                //向被偷玩家发消息并清除物品
                inventory.clear();
                object_player.sendMessage(Text.of("--------------------"));
                object_player.sendMessage(be_stolen);
                object_player.sendMessage(Text.of("--------------------"));

            }
            //选中的不是玩家
            else {
                using_player.sendMessage(Text.of("--------------------"));
                using_player.sendMessage(object_is_not_player_error);
                using_player.sendMessage(Text.of("--------------------"));
            }
        }

        return ActionResult.SUCCESS;
    }

    private void stole_item(int item_count, String item_name, PlayerEntity using_player, ItemStack item){
        Text steal_item = Text.translatable(
                "item.simple_tools.player_stealer.steal_item",
                Integer.toString(item_count),
                item_name
        );
        Item itemItem = item.getItem();
        if (!(itemItem == Items.AIR)) {
            using_player.sendMessage(steal_item);
        }
    }

    //工具信息

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Text tip = Text.translatable(
                "item.simple_tools.player_stealer.tip.normally"
        );
        tooltip.add(Text.of(""));
        tooltip.add(tip);
        tooltip.add(Text.of(""));
    }
}