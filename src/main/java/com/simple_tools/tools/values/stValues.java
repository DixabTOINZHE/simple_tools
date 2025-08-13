package com.simple_tools.tools.values;

import com.simple_tools.SimpleTools;

public class stValues {


    public static void run(){
        SimpleTools.LOGGER.info("com.simple_tools.tools.values.Values are loaded successfully");
    }


    // 1：平面 2：竖起x方向 3：竖起z方向 4：立方体
    public static int change_block_placer_place_mode(int block_placer_place_mode){
        if (block_placer_place_mode == 4){
            block_placer_place_mode = 1;
        } else {
            block_placer_place_mode++;
        }
        return block_placer_place_mode;
    }

    public static int change_block_placer_place_diameter(int block_placer_place_diameter, int min, int max){
        if (block_placer_place_diameter >= max){
            block_placer_place_diameter = min;
        } else {
            block_placer_place_diameter = block_placer_place_diameter+2;
        }
        return block_placer_place_diameter;
    }

    public static int change_slime_block_place_diameter(int slime_block_place_diameter, int min, int max){
        if (slime_block_place_diameter >= max){
            slime_block_place_diameter = min;
        } else {
            slime_block_place_diameter = slime_block_place_diameter+2;
        }
        return slime_block_place_diameter;
    }

    public static int change_block_searcher_searching_diameter(int block_searcher_searching_diameter, int min, int max){
        if (block_searcher_searching_diameter >= max)
            block_searcher_searching_diameter = min;
        else
            block_searcher_searching_diameter += 2;
        return block_searcher_searching_diameter;
    }
}
