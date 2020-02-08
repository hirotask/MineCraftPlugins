package com.github.jp.erudo.ebowspleef2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Score;

import com.github.jp.erudo.ebowspleef2.enums.GameState;
import com.github.jp.erudo.ebowspleef2.enums.Teams;
import com.github.jp.erudo.ebowspleef2.utils.MessageManager;
import com.github.jp.erudo.ebowspleef2.utils.TitleSender;

public class Game extends BukkitRunnable {

	private final Main plg;
	private BukkitTask task;
	private int count;

	public Game(Main plg, int count) {
		this.plg = plg;
		this.count = count;
	}

	public void run() {
		if (plg.getCurrentGameState() == GameState.END) {
			MessageManager.broadcastMessage("試合終了！！");
			count = 0;
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				player.setSneaking(false);

				if (player.getWorld().getPVP()) {
					continue;
				} else {
					player.getWorld().setPVP(false);
				}
			}
			plg.setCurrentGameState(GameState.PREPARE);
			plg.getServer().getScheduler().cancelTask(task.getTaskId());
		}

		if (plg.getCurrentGameState() == GameState.GAMING) {
			if (count > 0) {
				Score TimeScore = plg.getObj().getScore(ChatColor.GOLD + "残り時間: ");
				TimeScore.setScore(count);

				Score RedPoint = plg.getObj().getScore(ChatColor.DARK_BLUE + "赤チーム獲得ポイント: ");
				RedPoint.setScore(plg.getRedPoint());

				Score BluePoint = plg.getObj().getScore(ChatColor.DARK_RED + "青チーム獲得ポイント: ");
				BluePoint.setScore(plg.getBluePoint());

				TitleSender title = new TitleSender();
				for(Player p : plg.getServer().getOnlinePlayers()) {
					title.sendTitle(p, null, null, "赤チーム残り人数: " + plg.getTeam(Teams.RED).getEntries().size()
							+ "  " + "青チーム残り人数: " + plg.getTeam(Teams.BLUE).getEntries().size());
				}

				if (plg.getTeam(Teams.RED).getEntries().size() <= 0) {
					plg.setCurrentGameState(GameState.END);
					return;
				}

				if (plg.getTeam(Teams.BLUE).getEntries().size() <= 0) {
					plg.setCurrentGameState(GameState.END);
					return;
				}

			} else {
				plg.setCurrentGameState(GameState.END);
			}
			count--;
		}

	}

	public void setTask(BukkitTask task) {
		this.task = task;
	}

}