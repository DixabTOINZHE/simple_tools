package com.simple_tools.datas;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.simple_tools.SimpleTools;
import net.minecraft.client.realms.Request;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class stWorldJson {

    public static void run(MinecraftServer server, ServerWorld world){
        try {
            String path = stWorldJson.get_path(world);

            stWorldJson.stSettings loader = stWorldJson.read(path);

            //设置初始值
            if (!loader.settings_created){
                loader.block_placer_max_diameter = 15;
                loader.block_placer_min_diameter = 3;
                loader.block_searcher_max_diameter = 15;
                loader.block_searcher_min_diameter = 3;
                loader.block_searcher_show_position = false;
                loader.slime_block_placer_max_diameter = 5;
                loader.slime_block_placer_min_diameter = 1;
                loader.nether_portal_frame_generator_max_height = 23;
                loader.nether_portal_frame_generator_min_height = 5;
                loader.nether_portal_frame_generator_max_width = 23;
                loader.nether_portal_frame_generator_min_width = 4;
                loader.player_stealer_enabled = true;
                loader.settings_created = true;
                stWorldJson.save(loader, path);
            }
        } catch (IllegalStateException e){
            delete(world);
            run(server, world);
        } catch (JsonSyntaxException e){
            delete(world);
            run(server, world);
        }
    }

    public static class stSettings{

        public int block_placer_max_diameter;
        public int block_placer_min_diameter;
        public int block_searcher_max_diameter;
        public int block_searcher_min_diameter;
        public boolean block_searcher_show_position;
        public int slime_block_placer_max_diameter;
        public int slime_block_placer_min_diameter;
        public int nether_portal_frame_generator_max_height;
        public int nether_portal_frame_generator_min_height;
        public int nether_portal_frame_generator_max_width;
        public int nether_portal_frame_generator_min_width;
        public boolean player_stealer_enabled;
        public boolean settings_created;

    }

    //保存
    public static void save(stSettings obj, String file_name){

        try (FileWriter writer = new FileWriter(file_name)) {
            new Gson().toJson(obj, writer);
        } catch (IOException ignored){}

    }

    //读取
    public static stSettings read(String file_name){

        File file = new File(file_name);
        if (!file.exists()){
            stSettings defaults = new stSettings();
            defaults.block_placer_max_diameter = 15;
            defaults.settings_created = false;
            return defaults;
        }

        try (FileReader reader = new FileReader(file_name)){
            return new Gson().fromJson(reader, stSettings.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static String get_path(ServerWorld world) {
        Path worldPath = world.getServer().getSavePath(WorldSavePath.ROOT);
        return worldPath.resolve("st_settings.json").toString();
    }

    public static boolean delete(ServerWorld world) {
        try {
            Path path = Path.of(get_path(world));
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

}
