package net.minestom.worldgen.test;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.utils.Position;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.WorldGenConfig;
import net.minestom.worldgen.biomes.BiomeGroup;
import net.minestom.worldgen.biomes.impl.Desert;
import net.minestom.worldgen.biomes.impl.Plains;
import net.minestom.worldgen.biomes.impl.RedDesert;
import net.minestom.worldgen.biomegen.impls.*;

public class Minestom {

	public static void main(String[] args) {
		// Initialization
		MinecraftServer minecraftServer = MinecraftServer.init();

		InstanceManager instanceManager = MinecraftServer.getInstanceManager();
		// Create the instance
		InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

		//lib worldgen setup
		WorldGen wg = new WorldGen(instanceContainer, new Config(), 12321);

		// Set the ChunkGenerator
		instanceContainer.setChunkGenerator(wg.getChunkGenerator());
		// Enable the auto chunk loading (when players come close)
		instanceContainer.enableAutoChunkLoad(true);

		// Add an event callback to specify the spawning instance (and the spawn position)
		GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
		globalEventHandler.addEventCallback(PlayerLoginEvent.class, event -> {
			final Player player = event.getPlayer();
			event.setSpawningInstance(instanceContainer);
			player.setRespawnPoint(new Position(0, 42, 0));
		});
		globalEventHandler.addEventCallback(PlayerSpawnEvent.class, event -> {
			event.getPlayer().setGameMode(GameMode.SPECTATOR);
		});

		MinecraftServer.setChunkViewDistance(15);

		// Start the server on port 25565
		minecraftServer.start("localhost", 25566);
		/*for (int x = -16; x < 16; x++) {
			for (int y = -16; y < 16; y++) {
				instanceContainer.loadChunk(x,y);
			}
		}*/
	}

	public static class Config implements WorldGenConfig {

		@Override
		public void addBiomeLayers(WorldGen wg) {
			long i = 2000;
			wg.addBiomeLayer(new LandBiomeLayer(wg, i++, 1f));
			wg.addBiomeLayer(new BiomeGroupBiomeLayer(wg, i++));
			wg.addBiomeLayer(new ZoomBiomeLayer(wg, i++));
			wg.addBiomeLayer(new ZoomBiomeLayer(wg, i++));
			wg.addBiomeLayer(new SmoothBiomeLayer(wg, i++));
			wg.addBiomeLayer(new BiomesBiomeLayer(wg, i++));
			wg.addBiomeLayer(new ZoomBiomeLayer(wg, i++));
			wg.addBiomeLayer(new ZoomBiomeLayer(wg, i++));
			wg.addBiomeLayer(new ZoomBiomeLayer(wg, i++));
			wg.addBiomeLayer(new ZoomBiomeLayer(wg, i++));
			wg.addBiomeLayer(new ZoomBiomeLayer(wg, i++));
			wg.addBiomeLayer(new ZoomBiomeLayer(wg, i++));
		}

		@Override
		public void addHeightMapLayers(WorldGen wg) {

		}

		@Override
		public void addTerrainLayers(WorldGen wg) {

		}

		@Override
		public void addBiomes(WorldGen wg) {
			BiomeGroup group = new BiomeGroup();
			group.addBiome(new Desert(wg, 2000));
			group.addBiome(new RedDesert(wg, 2005));
			wg.addBiomeGroup(group);
			group = new BiomeGroup();
			group.addBiome(new Plains(wg, 2010));
			wg.addBiomeGroup(group);
		}

	}
}
