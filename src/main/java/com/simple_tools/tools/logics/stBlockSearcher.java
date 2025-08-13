package com.simple_tools.tools.logics;

import com.simple_tools.datas.stItemComponents;
import com.simple_tools.datas.stWorldJson;
import com.simple_tools.tools.values.stValues;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class stBlockSearcher extends Item {

    public stBlockSearcher(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient())
            change_diameter(user, user.getStackInHand(hand));
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!user.getWorld().isClient())
            change_diameter(user, stack);
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        //获取信息
        Hand hand=context.getHand();
        BlockPos block_pos=context.getBlockPos();
        PlayerEntity using_player=context.getPlayer();
        World environment=context.getWorld();
        ItemStack using_stack=using_player.getStackInHand(hand);
        ItemStack offhand_item=using_player.getStackInHand(Hand.OFF_HAND);
        Item offhand_itemItem=offhand_item.getItem();

        //设置文本
        Text offhand_empty_error= Text.translatable(
                "item.simple_tools.block_searcher.offhand_empty_error"
        );
        Text offhand_is_not_block_error=Text.translatable(
                "item.simple_tools.block_searcher.offhand_is_not_block_error"
        );
        Text start=Text.translatable(
                "item.simple_tools.block_searcher.start"
        );
        Text finish=Text.translatable(
                "item.simple_tools.block_searcher.finish"
        );
        Text search_diameter=Text.translatable(
                "item.simple_tools.block_searcher.search_diameter",
                "\u00A7r"+Integer.toString(using_stack.get(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER)),
                "\u00A7r"+Integer.toString(using_stack.get(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER)),
                "\u00A7r"+Integer.toString(using_stack.get(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER))
        );
        Text not_found=Text.translatable(
                "item.simple_tools.block_searcher.not_found"
        );
        Text found_pre=Text.translatable(
                "item.simple_tools.block_searcher.found_pre"
        );
        Text found_suf=Text.translatable(
                "item.simple_tools.block_searcher.found_suf"
        );


        //搜索到的方块数量
        int number_of_searched_block=0;

        if(!environment.isClient){

            //更改模式
            if (change_diameter(using_player, using_stack))
                return ActionResult.SUCCESS;

            //特殊情况：副手物品为空
            if(offhand_item==ItemStack.EMPTY){

                play_sound(3, using_player);

                using_player.sendMessage(Text.of("--------------------"));

                using_player.sendMessage(offhand_empty_error);

                using_player.sendMessage(Text.of("--------------------"));

                return ActionResult.SUCCESS;
            }
            //特殊情况：副手物品不是方块

            if (!(offhand_itemItem instanceof BlockItem)) {

                play_sound(3, using_player);

                using_player.sendMessage(Text.of("--------------------"));

                using_player.sendMessage(offhand_is_not_block_error);

                using_player.sendMessage(Text.of("--------------------"));

                return ActionResult.SUCCESS;
            }

            String name_of_item=offhand_itemItem.getName().getString();
            Text name_of_itemText=Text.translatable(
                    "item.simple_tools.block_searcher.show_searching_object",
                    name_of_item
            );
            //开始
            using_player.sendMessage(Text.of("--------------------"));
            using_player.sendMessage(name_of_itemText);
            using_player.sendMessage(start);{

                play_sound(1, using_player);

                using_player.sendMessage(search_diameter);
                int radius = (using_stack.get(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER)-1)/2;
                for(int dy=-1*radius; dy<=radius; dy++) {
                    for (int dx = -1*radius; dx <= radius; dx++) {
                        for (int dz = -1*radius; dz <= radius; dz++) {
                            BlockPos searched_block = new BlockPos(block_pos.getX() + dx, block_pos.getY()+dy, block_pos.getZ() + dz);
                            BlockState searched_block_state = environment.getBlockState(searched_block);
                            Block searched_blockBlock = searched_block_state.getBlock();
                            Item searched_blockItem = searched_blockBlock.asItem();

                            stWorldJson.stSettings settings =
                                    stWorldJson.read(stWorldJson.get_path(using_player.getServer().getOverworld()));
                            if (searched_blockItem == offhand_itemItem){
                                if (settings.block_searcher_show_position) {
                                    Text find = getText(searched_block, searched_blockItem);
                                    using_player.sendMessage(find);
                                }
                                number_of_searched_block++;
                            }
                        }
                    }
                }
            }

            if(!stWorldJson.read(stWorldJson.get_path(using_player.getServer().getOverworld())).block_searcher_show_position) {
                if (number_of_searched_block == 0) {
                    using_player.sendMessage(not_found);
                } else {
                    using_player.sendMessage(Text.of(found_pre.getString() + number_of_searched_block + found_suf.getString()));
                }
            }
            using_player.sendMessage(finish);
            using_player.sendMessage(Text.of("--------------------"));

            //扣除耐久
            context.getStack().damage(1,using_player, EquipmentSlot.MAINHAND);

            return ActionResult.SUCCESS;

        }

        //防止报错
        return ActionResult.SUCCESS;
    }

    private static @NotNull Text getText(BlockPos searched_block, Item searched_blockItem) {
        int x = searched_block.getX();
        int y = searched_block.getY();
        int z = searched_block.getZ();
        Text find = Text.translatable(
                "item.simple_tools.block_searcher.report_position",
                searched_blockItem.getName().getString(),
                Integer.toString(x),
                Integer.toString(y),
                Integer.toString(z)
        );
        return find;
    }

    //更改直径
    private boolean change_diameter(PlayerEntity using_player, ItemStack using_stack){

        stWorldJson.stSettings settings =
        stWorldJson.read(stWorldJson.get_path(using_player.getServer().getOverworld()));
        int min = settings.block_searcher_min_diameter;
        int max = settings.block_searcher_max_diameter;

        if (Screen.hasAltDown()){

            play_sound(2, using_player);

            using_player.sendMessage(Text.of("--------------------"));
            using_stack.set(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER, stValues.change_block_searcher_searching_diameter(using_stack.get(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER), min, max));
            Text changed_diameter = Text.translatable(
                    "item.simple_tools.block_searcher.changed_diameter",
                    Integer.toString(using_stack.get(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER))
            );
            using_player.sendMessage(changed_diameter);
            using_player.sendMessage(Text.of("--------------------"));
            return true;
        } else
            return false;
    }

    //添加工具信息

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        //设置文本
        Text tip = Text.translatable(
                "item.simple_tools.block_searcher.tip.normally"
        );
        Text tip_alt1 = Text.translatable(
                "item.simple_tools.block_searcher.tip.alt.line1"
        );
        Text tip_alt2 = Text.translatable(
                "item.simple_tools.block_searcher.tip.alt.line2"
        );
        Text tip_alt3 = Text.translatable(
                "item.simple_tools.block_searcher.tip.alt.line3"
        );
        Text tip_diameter = Text.translatable(
                "item.simple_tools.block_searcher.tip.diameter",
                "\u00A72"+Integer.toString(stack.get(stItemComponents.BLOCK_SEARCHER_SEARCHING_DIAMETER))+"\u00A72"
        );

        if (!(Screen.hasAltDown())){
            tooltip.add(Text.of(""));
            tooltip.add(tip_diameter);
            tooltip.add(tip);
            tooltip.add(Text.of(""));
        } else {
            tooltip.add(Text.of(""));
            tooltip.add(tip_diameter);
            tooltip.add(tip_alt3);
            tooltip.add(tip_alt1);
            tooltip.add(tip_alt2);
            tooltip.add(Text.of(""));
        }
    }

    //选择声音
    private final Random random = new Random();
    private SoundEvent choose_sound(int mode){


        int random_choose = random.nextInt(3);

        //放置
        if (mode == 1){

            if (random_choose == 0) return SoundEvents.BLOCK_STEM_BREAK;
            else if(random_choose == 1) return SoundEvents.BLOCK_STONE_PLACE;
            else return SoundEvents.BLOCK_STONE_HIT;

        }

        //更改模式或半径
        else if (mode ==2){

            if (random_choose <= 1) return SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
            else return SoundEvents.ENTITY_PLAYER_LEVELUP;

        }

        //执行失败
        else {

            if (random_choose == 0) return SoundEvents.ENTITY_ITEM_BREAK;
            else if(random_choose == 1) return SoundEvents.ITEM_ARMOR_EQUIP_IRON.value();
            else return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND.value();

        }
    }

    //播放声音
    private void play_sound(int mode, PlayerEntity using_player){

        SoundEvent sound = choose_sound(mode);
        using_player.getWorld().playSoundFromEntity(
                null,
                using_player,
                sound,
                SoundCategory.PLAYERS,
                1.1f,
                0.4f
        );

    }
}
