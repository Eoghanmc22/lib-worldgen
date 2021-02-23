package net.minestom.worldgen.test;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.network.packet.server.play.ChangeGameStatePacket;
import net.minestom.server.utils.Position;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.WorldGenConfig;
import net.minestom.worldgen.biomes.BiomeGroup;
import net.minestom.worldgen.biomes.impl.Desert;
import net.minestom.worldgen.biomes.impl.Plains;
import net.minestom.worldgen.biomes.impl.RedDesert;
import net.minestom.worldgen.layers.impls.*;

import java.nio.charset.Charset;

public class Minestom {

	public static void main(String[] args) {
		// Initialization
		MinecraftServer minecraftServer = MinecraftServer.init();

		InstanceManager instanceManager = MinecraftServer.getInstanceManager();
		// Create the instance
		InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

		//lib worldgen setup
		WorldGen wg = new WorldGen(instanceContainer, new Config());

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

		System.out.println(Charset.defaultCharset().displayName());

		// Start the server on port 25565
		minecraftServer.start("localhost", 25566);
	}

	public static class Config implements WorldGenConfig {

		@Override
		public void addLayers(WorldGen wg) {
			int i = 2000;
			wg.addLayer(new BaseLayer());
			wg.addLayer(new LandLayer(i++, 1f));
			wg.addLayer(new BiomeGroupLayer(i++));
			wg.addLayer(new ZoomLayer(i++));
			wg.addLayer(new ZoomLayer(i++));
			wg.addLayer(new SmoothLayer(i++));
			wg.addLayer(new BiomeLayer(i++));
			wg.addLayer(new ZoomLayer(i++));
			wg.addLayer(new ZoomLayer(i++));
			wg.addLayer(new ZoomLayer(i++));
			wg.addLayer(new ZoomLayer(i++));
			wg.addLayer(new ZoomLayer(i++));
			wg.addLayer(new ZoomLayer(i++));
		}

		@Override
		public void addBiomes(WorldGen wg) {
			BiomeGroup group = new BiomeGroup();
			group.addBiome(new Desert());
			group.addBiome(new RedDesert());
			wg.addBiomeGroup(group);
			group = new BiomeGroup();
			group.addBiome(new Plains());
			wg.addBiomeGroup(group);
		}

	}
}
