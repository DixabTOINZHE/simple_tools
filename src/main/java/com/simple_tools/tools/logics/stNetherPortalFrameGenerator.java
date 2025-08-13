package com.simple_tools.tools.logics;

import com.simple_tools.datas.stWorldJson;
import com.simple_tools.tags.stBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class stNetherPortalFrameGenerator extends Item{

    public stNetherPortalFrameGenerator(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        //获取信息
        PlayerEntity using_player = context.getPlayer();
        PlayerInventory inventory = using_player.getInventory();
        World environment = context.getWorld();
        BlockPos pos = context.getBlockPos();
        RegistryKey<World> dimension = environment.getRegistryKey();
        Direction side = context.getSide();

        //设置文本
        Text no_space_error = Text.translatable(
                "item.simple_tools.nether_portal_frame_generator.no_space_error"
        );
        Text obsidian_is_not_enough_error = Text.translatable(
                "item.simple_tools.nether_portal_frame_generator.obsidian_is_not_enough_error"
        );
        Text in_the_end_error = Text.translatable(
                "item.simple_tools.nether_portal_frame_generator.in_the_end_error"
        );
        Text start = Text.translatable(
                "item.simple_tools.nether_portal_frame_generator.start"
        );
        Text finish = Text.translatable(
                "item.simple_tools.nether_portal_frame_generator.finish"
        );

        if (environment.isClient()) return ActionResult.SUCCESS;
        using_player.sendMessage(Text.of("--------------------"));

        //末地
        if (dimension == World.END){
            using_player.sendMessage(in_the_end_error);
            using_player.sendMessage(Text.of("--------------------"));
            return ActionResult.SUCCESS;
        }

        //点击了南或北方
        if (side == Direction.NORTH || side == Direction.SOUTH){

            rect area;
            if (side == Direction.NORTH) area = get_rect(environment, dimension, pos.add(0, 0, -1), true, false, true, true);
            else area = get_rect(environment, dimension, pos.add(0, 0, 1), true, false, true, true);

            if (area.allowed){

                //检测黑曜石数量
                int number_of_obsidian = 0;

                for (int i=0;i<=35;i++){
                    ItemStack relative_item_stack = inventory.main.get(i);
                    Item relative_item = relative_item_stack.getItem();
                    if (relative_item == Items.OBSIDIAN){
                        int count_of_relative_item=relative_item_stack.getCount();
                        number_of_obsidian = number_of_obsidian+count_of_relative_item;
                    }
                }

                if(inventory.offHand.get(0).getItem()== Items.OBSIDIAN){
                    number_of_obsidian = number_of_obsidian+inventory.offHand.get(0).getCount();
                }

                if (using_player.isCreative()){
                    number_of_obsidian=114514;
                }

                if (number_of_obsidian >= area.obsidian_needed){

                    BlockPos obj;

                    //发送信息
                    play_sound(1, using_player);
                    using_player.sendMessage(start);
                    Text massages = Text.translatable(
                            "item.simple_tools.nether_portal_frame_generator.massages",
                            Integer.toString(area.obsidian_needed)
                    );
                    using_player.sendMessage(massages);
                    using_player.sendMessage(finish);
                    using_player.sendMessage(Text.of("--------------------"));

                    //放黑曜石
                    for (int i = pos.getX()-area.w; i <= pos.getX()+area.e; i++){
                        for (int j = pos.getY()-area.d; j <= pos.getY()+area.u; j++){

                            if (side == Direction.NORTH) obj = new BlockPos(i, j, pos.getZ()-1);
                            else obj = new BlockPos(i, j, pos.getZ()+1);
                            change_block_state(obj, environment, Blocks.OBSIDIAN);

                        }
                    }

                    //挖空内部
                    for (int i = pos.getX()-area.w+1; i <= pos.getX()+area.e-1; i++){
                        for (int j = pos.getY()-area.d+1; j <= pos.getY()+area.u-1; j++){

                            if (side == Direction.NORTH) obj = new BlockPos(i, j, pos.getZ()-1);
                            else obj = new BlockPos(i, j, pos.getZ()+1);
                            change_block_state(obj, environment, Blocks.AIR);

                        }
                    }

                    //扣除黑曜石和耐久
                    context.getStack().damage(area.obsidian_needed/4, using_player, EquipmentSlot.MAINHAND);
                    if (!using_player.isCreative()) inventory.remove(stack -> stack.getItem() == Items.OBSIDIAN, area.obsidian_needed, null);

                }

                //黑曜石不足
                else {
                    play_sound(3, using_player);
                    using_player.sendMessage(obsidian_is_not_enough_error);
                    using_player.sendMessage(Text.of("--------------------"));
                }

            }

            //空间不足
            else {
                play_sound(3, using_player);
                using_player.sendMessage(no_space_error);
                using_player.sendMessage(Text.of("--------------------"));
            }

        }

        //点击了东或西方
        else if (side == Direction.EAST || side == Direction.WEST) {

            rect area;
            if (side == Direction.WEST) area = get_rect(environment, dimension, pos.add(-1, 0, 0), false, true, true, true);
            else area = get_rect(environment, dimension, pos.add(1, 0, 0), false, true, true, true);

            if (area.allowed) {

                // 检测黑曜石数量
                int number_of_obsidian = 0;

                for (int i = 0; i <= 35; i++) {
                    ItemStack relative_item_stack = inventory.main.get(i);
                    Item relative_item = relative_item_stack.getItem();
                    if (relative_item == Items.OBSIDIAN) {
                        int count_of_relative_item = relative_item_stack.getCount();
                        number_of_obsidian = number_of_obsidian + count_of_relative_item;
                    }
                }

                if (inventory.offHand.get(0).getItem() == Items.OBSIDIAN) {
                    number_of_obsidian = number_of_obsidian + inventory.offHand.get(0).getCount();
                }

                if (using_player.isCreative()) {
                    number_of_obsidian = 114514;
                }

                if (number_of_obsidian >= area.obsidian_needed) {

                    BlockPos obj;

                    // 发送信息
                    play_sound(1, using_player);
                    using_player.sendMessage(start);
                    Text massages = Text.translatable(
                            "item.simple_tools.nether_portal_frame_generator.massages",
                            Integer.toString(area.obsidian_needed)
                    );
                    using_player.sendMessage(massages);
                    using_player.sendMessage(finish);
                    using_player.sendMessage(Text.of("--------------------"));

                    // 放置黑曜石框架
                    for (int i = pos.getZ() - area.n; i <= pos.getZ() + area.s; i++) {
                        for (int j = pos.getY() - area.d; j <= pos.getY() + area.u; j++) {

                            if (side == Direction.WEST) obj = new BlockPos(pos.getX() - 1, j, i);
                            else obj = new BlockPos(pos.getX() + 1, j, i);
                            change_block_state(obj, environment, Blocks.OBSIDIAN);

                        }
                    }

                    // 挖空内部
                    for (int i = pos.getZ() - area.n + 1; i <= pos.getZ() + area.s - 1; i++) {
                        for (int j = pos.getY() - area.d + 1; j <= pos.getY() + area.u - 1; j++) {

                            if (side == Direction.WEST) obj = new BlockPos(pos.getX() - 1, j, i);
                            else obj = new BlockPos(pos.getX() + 1, j, i);
                            change_block_state(obj, environment, Blocks.AIR);

                        }
                    }

                    // 扣除黑曜石和耐久
                    context.getStack().damage(area.obsidian_needed/4, using_player, EquipmentSlot.MAINHAND);
                    if (!using_player.isCreative()) inventory.remove(stack -> stack.getItem() == Items.OBSIDIAN, area.obsidian_needed, null);

                } else {
                    // 黑曜石不足
                    play_sound(3, using_player);
                    using_player.sendMessage(obsidian_is_not_enough_error);
                    using_player.sendMessage(Text.of("--------------------"));
                }

            } else {
                // 空间不足
                play_sound(3, using_player);
                using_player.sendMessage(no_space_error);
                using_player.sendMessage(Text.of("--------------------"));
            }
        }

//        else {
//            using_player.sendMessage(side_is_up_or_down_error);
//            using_player.sendMessage(Text.of("--------------------"));
//        }

        //点击了下方
        else if (side == Direction.DOWN){

            rect area_x = get_rect(environment, dimension, pos.add(0, -1, 0), true, false, false, true);
            rect area_z = get_rect(environment, dimension, pos.add(0 ,-1, 0), false, true, false, true);

            //X>Z
            if (area_x.obsidian_needed >= area_z.obsidian_needed){
                if (area_x.allowed){

                    //检测黑曜石数量
                    int number_of_obsidian = 0;

                    for (int i=0;i<=35;i++){
                        ItemStack relative_item_stack = inventory.main.get(i);
                        Item relative_item = relative_item_stack.getItem();
                        if (relative_item == Items.OBSIDIAN){
                            int count_of_relative_item=relative_item_stack.getCount();
                            number_of_obsidian = number_of_obsidian+count_of_relative_item;
                        }
                    }

                    if(inventory.offHand.get(0).getItem()== Items.OBSIDIAN){
                        number_of_obsidian = number_of_obsidian+inventory.offHand.get(0).getCount();
                    }

                    if (using_player.isCreative()){
                        number_of_obsidian=114514;
                    }

                    if (number_of_obsidian >= area_x.obsidian_needed){

                        BlockPos obj;

                        //发送信息
                        play_sound(1, using_player);
                        using_player.sendMessage(start);
                        Text massages = Text.translatable(
                                "item.simple_tools.nether_portal_frame_generator.massages",
                                Integer.toString(area_x.obsidian_needed)
                        );
                        using_player.sendMessage(massages);
                        using_player.sendMessage(finish);
                        using_player.sendMessage(Text.of("--------------------"));

                        //放黑曜石
                        for (int i = pos.getX()-area_x.w; i <= pos.getX()+area_x.e; i++){
                            for (int j = pos.getY()-area_x.d; j <= pos.getY()+area_x.u; j++){

                                obj = new BlockPos(i, j-1, pos.getZ());
                                change_block_state(obj, environment, Blocks.OBSIDIAN);

                            }
                        }

                        //挖空内部
                        for (int i = pos.getX()-area_x.w+1; i <= pos.getX()+area_x.e-1; i++){
                            for (int j = pos.getY()-area_x.d+1; j <= pos.getY()+area_x.u-1; j++){

                                obj = new BlockPos(i, j-1, pos.getZ());
                                change_block_state(obj, environment, Blocks.AIR);

                            }
                        }

                        //扣除黑曜石和耐久
                        context.getStack().damage(area_x.obsidian_needed/4, using_player, EquipmentSlot.MAINHAND);
                        if (!using_player.isCreative()) inventory.remove(stack -> stack.getItem() == Items.OBSIDIAN, area_x.obsidian_needed, null);

                    }

                    //黑曜石不足
                    else {
                        using_player.sendMessage(obsidian_is_not_enough_error);
                        using_player.sendMessage(Text.of("--------------------"));
                    }

                }

                //空间不足
                else {
                    using_player.sendMessage(no_space_error);
                    using_player.sendMessage(Text.of("--------------------"));
                }
            }

            //X<Z
            else if (area_z.obsidian_needed > area_x.obsidian_needed) {
                if (area_z.allowed) {

                    // 检测黑曜石数量
                    int number_of_obsidian = 0;

                    for (int i = 0; i <= 35; i++) {
                        ItemStack relative_item_stack = inventory.main.get(i);
                        Item relative_item = relative_item_stack.getItem();
                        if (relative_item == Items.OBSIDIAN) {
                            int count_of_relative_item = relative_item_stack.getCount();
                            number_of_obsidian = number_of_obsidian + count_of_relative_item;
                        }
                    }

                    if (inventory.offHand.get(0).getItem() == Items.OBSIDIAN) {
                        number_of_obsidian = number_of_obsidian + inventory.offHand.get(0).getCount();
                    }

                    if (using_player.isCreative()) {
                        number_of_obsidian = 114514;
                    }

                    if (number_of_obsidian >= area_z.obsidian_needed) {

                        BlockPos obj;

                        // 发送信息
                        using_player.sendMessage(start);
                        Text massages = Text.translatable(
                                "item.simple_tools.nether_portal_frame_generator.massages",
                                Integer.toString(area_z.obsidian_needed)
                        );
                        using_player.sendMessage(massages);
                        using_player.sendMessage(finish);
                        using_player.sendMessage(Text.of("--------------------"));

                        // 放置黑曜石框架
                        for (int i = pos.getZ() - area_z.n; i <= pos.getZ() + area_z.s; i++) {
                            for (int j = pos.getY() - area_z.d; j <= pos.getY() + area_z.u; j++) {

                                obj = new BlockPos(pos.getX(), j-1, i);
                                change_block_state(obj, environment, Blocks.OBSIDIAN);

                            }
                        }

                        // 挖空内部
                        for (int i = pos.getZ() - area_z.n + 1; i <= pos.getZ() + area_z.s - 1; i++) {
                            for (int j = pos.getY() - area_z.d + 1; j <= pos.getY() + area_z.u - 1; j++) {

                                obj = new BlockPos(pos.getX(), j-1, i);
                                change_block_state(obj, environment, Blocks.AIR);

                            }
                        }

                        // 扣除黑曜石和耐久
                        context.getStack().damage(area_z.obsidian_needed/4, using_player, EquipmentSlot.MAINHAND);
                        if (!using_player.isCreative()) inventory.remove(stack -> stack.getItem() == Items.OBSIDIAN, area_z.obsidian_needed, null);

                    } else {
                        // 黑曜石不足
                        play_sound(3, using_player);
                        using_player.sendMessage(obsidian_is_not_enough_error);
                        using_player.sendMessage(Text.of("--------------------"));
                    }

                } else {
                    // 空间不足
                    play_sound(3, using_player);
                    using_player.sendMessage(no_space_error);
                    using_player.sendMessage(Text.of("--------------------"));
                }
            }
        }

        //点击了上方
        else if (side == Direction.UP){

            rect area_x = get_rect(environment, dimension, pos.add(0, 1, 0), true, false, true, false);
            rect area_z = get_rect(environment, dimension, pos.add(0 ,1, 0), false, true, true, false);

            //X>Z
            if (area_x.obsidian_needed >= area_z.obsidian_needed){
                if (area_x.allowed){

                    //检测黑曜石数量
                    int number_of_obsidian = 0;

                    for (int i=0;i<=35;i++){
                        ItemStack relative_item_stack = inventory.main.get(i);
                        Item relative_item = relative_item_stack.getItem();
                        if (relative_item == Items.OBSIDIAN){
                            int count_of_relative_item=relative_item_stack.getCount();
                            number_of_obsidian = number_of_obsidian+count_of_relative_item;
                        }
                    }

                    if(inventory.offHand.get(0).getItem()== Items.OBSIDIAN){
                        number_of_obsidian = number_of_obsidian+inventory.offHand.get(0).getCount();
                    }

                    if (using_player.isCreative()){
                        number_of_obsidian=114514;
                    }

                    if (number_of_obsidian >= area_x.obsidian_needed){

                        BlockPos obj;

                        //发送信息
                        play_sound(1, using_player);
                        using_player.sendMessage(start);
                        Text massages = Text.translatable(
                                "item.simple_tools.nether_portal_frame_generator.massages",
                                Integer.toString(area_x.obsidian_needed)
                        );
                        using_player.sendMessage(massages);
                        using_player.sendMessage(finish);
                        using_player.sendMessage(Text.of("--------------------"));

                        //放黑曜石
                        for (int i = pos.getX()-area_x.w; i <= pos.getX()+area_x.e; i++){
                            for (int j = pos.getY()-area_x.d; j <= pos.getY()+area_x.u; j++){

                                obj = new BlockPos(i, j+1, pos.getZ());
                                change_block_state(obj, environment, Blocks.OBSIDIAN);

                            }
                        }

                        //挖空内部
                        for (int i = pos.getX()-area_x.w+1; i <= pos.getX()+area_x.e-1; i++){
                            for (int j = pos.getY()-area_x.d+1; j <= pos.getY()+area_x.u-1; j++){

                                obj = new BlockPos(i, j+1, pos.getZ());
                                change_block_state(obj, environment, Blocks.AIR);

                            }
                        }

                        //扣除黑曜石和耐久
                        context.getStack().damage(area_x.obsidian_needed/4, using_player, EquipmentSlot.MAINHAND);
                        if (!using_player.isCreative()) inventory.remove(stack -> stack.getItem() == Items.OBSIDIAN, area_x.obsidian_needed, null);

                    }

                    //黑曜石不足
                    else {
                        play_sound(3, using_player);
                        using_player.sendMessage(obsidian_is_not_enough_error);
                        using_player.sendMessage(Text.of("--------------------"));
                    }

                }

                //空间不足
                else {
                    play_sound(3, using_player);
                    using_player.sendMessage(no_space_error);
                    using_player.sendMessage(Text.of("--------------------"));
                }
            }

            //X<Z
            else if (area_z.obsidian_needed > area_x.obsidian_needed) {
                if (area_z.allowed) {

                    // 检测黑曜石数量
                    int number_of_obsidian = 0;

                    for (int i = 0; i <= 35; i++) {
                        ItemStack relative_item_stack = inventory.main.get(i);
                        Item relative_item = relative_item_stack.getItem();
                        if (relative_item == Items.OBSIDIAN) {
                            int count_of_relative_item = relative_item_stack.getCount();
                            number_of_obsidian = number_of_obsidian + count_of_relative_item;
                        }
                    }

                    if (inventory.offHand.get(0).getItem() == Items.OBSIDIAN) {
                        number_of_obsidian = number_of_obsidian + inventory.offHand.get(0).getCount();
                    }

                    if (using_player.isCreative()) {
                        number_of_obsidian = 114514;
                    }

                    if (number_of_obsidian >= area_z.obsidian_needed) {

                        BlockPos obj;

                        // 发送信息
                        play_sound(1, using_player);
                        using_player.sendMessage(start);
                        Text massages = Text.translatable(
                                "item.simple_tools.nether_portal_frame_generator.massages",
                                Integer.toString(area_z.obsidian_needed)
                        );
                        using_player.sendMessage(massages);
                        using_player.sendMessage(finish);
                        using_player.sendMessage(Text.of("--------------------"));

                        // 放置黑曜石框架
                        for (int i = pos.getZ() - area_z.n; i <= pos.getZ() + area_z.s; i++) {
                            for (int j = pos.getY() - area_z.d; j <= pos.getY() + area_z.u; j++) {

                                obj = new BlockPos(pos.getX(), j+1, i);
                                change_block_state(obj, environment, Blocks.OBSIDIAN);

                            }
                        }

                        // 挖空内部
                        for (int i = pos.getZ() - area_z.n + 1; i <= pos.getZ() + area_z.s - 1; i++) {
                            for (int j = pos.getY() - area_z.d + 1; j <= pos.getY() + area_z.u - 1; j++) {

                                obj = new BlockPos(pos.getX(), j+1, i);
                                change_block_state(obj, environment, Blocks.AIR);

                            }
                        }

                        // 扣除黑曜石和耐久
                        context.getStack().damage(area_z.obsidian_needed/4, using_player, EquipmentSlot.MAINHAND);
                        if (!using_player.isCreative()) inventory.remove(stack -> stack.getItem() == Items.OBSIDIAN, area_z.obsidian_needed, null);

                    } else {
                        // 黑曜石不足
                        play_sound(3, using_player);
                        using_player.sendMessage(obsidian_is_not_enough_error);
                        using_player.sendMessage(Text.of("--------------------"));
                    }

                } else {
                    // 空间不足
                    play_sound(3, using_player);
                    using_player.sendMessage(no_space_error);
                    using_player.sendMessage(Text.of("--------------------"));
                }
            }
        }

        return ActionResult.SUCCESS;
    }

    //检测范围
    private rect get_rect(World environment, RegistryKey<World> dimension, BlockPos pos, boolean X, boolean Z, boolean U, boolean D){

        //获取信息
        int x = pos.getX();
        int y = pos.getY();

        //设置值
        rect ret = new rect();

        stWorldJson.stSettings settings =
                stWorldJson.read(stWorldJson.get_path(environment.getServer().getOverworld()));
        int max_height = settings.nether_portal_frame_generator_max_height;
        int min_height = settings.nether_portal_frame_generator_min_height;
        int max_width = settings.nether_portal_frame_generator_max_width;
        int min_width = settings.nether_portal_frame_generator_min_width;
        int max_y = 255;
        int min_y = 0;
        if (dimension == World.OVERWORLD){
            max_y = 319;
            min_y = -64;
        }
        int e = 0;
        int w = 0;
        int n = 0;
        int s = 0;
        int u = 0;
        int d = 0;
        boolean E = X;
        boolean W = X;
        boolean N = Z;
        boolean S = Z;
        BlockPos obj_pos;
        BlockState obj_state;

        //主循环
        while (E || W || N || S || U || D){

            if (E){
                int ud;
                ud = u+d+1;

                for (int i = -1*d; i < (-1*d)+ud; i++){

                    obj_pos = pos.add(e+1, i, 0);
                    obj_state = environment.getBlockState(obj_pos);

                    if (!obj_state.isIn(stBlockTags.PLACEABLE_BLOCKS)){
                        E = false;
                        break;
                    }
                    if (e+w+1 >= max_width){
                        E = false;
                        break;
                    }

                    if (i == (-1*d)+ud-1) e++;
                }
            }

            if (N){
                int ud;
                ud = u+d+1;

                for (int i = -1*d; i < (-1*d)+ud; i++){

                    obj_pos = pos.add(0, i, -(n+1));
                    obj_state = environment.getBlockState(obj_pos);

                    if (!obj_state.isIn(stBlockTags.PLACEABLE_BLOCKS)){
                        N = false;
                        break;
                    }
                    if (s+n+1 == max_width){
                        N = false;
                        break;
                    }

                    if (i == (-1*d)+ud-1) n++;
                }
            }

            if (D){
                int ew;
                ew = e+w+1;

                for (int i = -1*w; i < (-1*w)+ew; i++){

                    obj_pos = pos.add(i, -1*(d+1), 0);
                    obj_state = environment.getBlockState(obj_pos);

                    if (!obj_state.isIn(stBlockTags.PLACEABLE_BLOCKS)){
                        D = false;
                        break;
                    }
                    if (u+d+1 == max_height){
                        System.out.println("D: u+d+1");
                        D = false;
                        break;
                    }
                    if (d+1 == min_y){
                        D = false;
                        break;
                    }

                    if (i == (-1*w)+ew-1) d++;
                }
            }

            if (W){
                int ud;
                ud = u+d+1;

                for (int i = -1*d; i < (-1*d)+ud; i++){

                    obj_pos = pos.add(-1*(w+1), i, 0);
                    obj_state = environment.getBlockState(obj_pos);

                    if (!obj_state.isIn(stBlockTags.PLACEABLE_BLOCKS)){
                        W= false;
                        break;
                    }
                    if (e+w+1 == max_width){
                        W = false;
                        break;
                    }

                    if (i == (-1*d)+ud-1) w++;
                }
            }

            if (S){
                int ud;
                ud = u+d+1;

                for (int i = -1*d; i < (-1*d)+ud; i++){

                    obj_pos = pos.add(0, i, s+1);
                    obj_state = environment.getBlockState(obj_pos);
                    if (!obj_state.isIn(stBlockTags.PLACEABLE_BLOCKS)){
                        S= false;
                        break;
                    }
                    if (s+n+1 == max_width){
                        S = false;
                        break;
                    }

                    if (i == (-1*d)+ud-1) s++;
                }
            }

            if (U){
                int ew;
                ew = e+w+1;

                for (int i = -1*w; i < (-1*w)+ew; i++){

                    obj_pos = pos.add(i, u+1, 0);
                    obj_state = environment.getBlockState(obj_pos);

                    if (!obj_state.isIn(stBlockTags.PLACEABLE_BLOCKS)){
                        U = false;
                        break;
                    }
                    if (u+d+1 == max_height){
                        System.out.println("U: u+d+1");
                        U = false;
                        break;
                    }
                    if (u+1 == max_y){
                        U = false;
                        break;
                    }

                    if (i == (-1*w)+ew-1) u++;
                }
            }
        }

        ret.e = e;
        ret.w = w;
        ret.n = n;
        ret.s = s;
        ret.u = u;
        ret.d = d;
        ret.obsidian_needed = 2*(u+d+1)+2*(e+w+1)+2*(s+n+1)-2-4;
        ret.allowed = (n + s + 1 >= min_width || e + w + 1 >= min_width) && u + d + 1 >= min_height;

        return ret;
    }

    protected class rect {
        protected int e;
        protected int w;
        protected int n;
        protected int s;
        protected int u;
        protected int d;
        protected int obsidian_needed;
        protected boolean allowed;
    }

    private void change_block_state(BlockPos pos, World environment, Block block){
        environment.setBlockState(pos, block.getDefaultState(), 3);
    }

    //工具信息

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Text tip = Text.translatable(
                "item.simple_tools.nether_portal_frame_generator.tip"
        );

        tooltip.add(Text.of(""));
        tooltip.add(tip);
        tooltip.add(Text.of(""));

    }

    //选择声音
    private final Random random = new Random();
    private SoundEvent choose_sound(int mode){


        int random_choose = random.nextInt(3);

        //放置
        if (mode == 1){

            if (random_choose == 0) return SoundEvents.BLOCK_NETHER_ORE_BREAK;
            else if(random_choose == 1) return SoundEvents.BLOCK_NETHER_BRICKS_FALL;
            else return SoundEvents.BLOCK_NETHER_BRICKS_STEP;

        }

        //更改模式或半径
        else if (mode ==2){

            if (random_choose <= 1) return SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
            else return SoundEvents.ENTITY_PLAYER_LEVELUP;

        }

        //执行失败
        else {

            if (random_choose == 0) return SoundEvents.ENTITY_ITEM_BREAK;
            else return SoundEvents.BLOCK_GLASS_BREAK;

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
                1.0f,
                1.1f
        );

    }

}