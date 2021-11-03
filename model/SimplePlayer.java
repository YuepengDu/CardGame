package model;

import model.interfaces.Player;

import java.util.Objects;

public class SimplePlayer implements Player {
	private String id;
	private String name;
	private int points;
	private int bet;
	private int result;
	
	public SimplePlayer(String id, String name, int points) {
		this.id = id;
		this.name = name;
		this.points = points;
		this.result = 0;
		this.bet = 0;
	}
	
	@Override
	public String getPlayerName() {
		return name;
	}

	@Override
	public void setPlayerName(String playerName) {
		this.name = playerName;
	}

	@Override
	public int getPoints() {
		return points;
	}

	@Override
	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public String getPlayerId() {
		return this.id;
	}

	@Override
	public boolean setBet(int bet) {
		if(this.points < bet || bet <= 0) return false;
		this.bet = bet;
		return true;
	}

	@Override
	public int getBet() {
		return bet;
	}

	@Override
	public void resetBet() {
		this.bet = 0;
		this.result = 0;
	}

	@Override
	public int getResult() {
		return result;
	}

	@Override
	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public boolean equals(Player player) {
		return id.equals(player.getPlayerId());
	}

	@Override
	public int compareTo(Player player) {
		return Integer.valueOf(id).compareTo(Integer.valueOf(player.getPlayerId()));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SimplePlayer that = (SimplePlayer) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "SimplePlayer{" +
			"id='" + id + '\'' +
			", name='" + name + '\'' +
			", points=" + points +
			", bet=" + bet +
			", result=" + result +
			'}'; 
	}
}
