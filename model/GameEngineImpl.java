package model;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;

import model.interfaces.GameEngine;
import model.interfaces.Player;
import model.interfaces.PlayingCard;
import view.interfaces.GameEngineCallback;

public class GameEngineImpl implements GameEngine{
	private Collection<Player> players;
	private Collection<GameEngineCallback> gameEngineCallbacks; 
	/* The collection of all cards in order */
	private ArrayList<PlayingCard> cardCollection;
	/* The cards that shuffled overtime */
	private Deque<PlayingCard> cards;
	/* Result of the house */
	private int houseResult;
	
	public final int JQKSCORE = 10;
	public final int ACESCORE = 11;

	public GameEngineImpl() { 
		gameEngineCallbacks = new ArrayList<GameEngineCallback>();
		cardCollection = new ArrayList<PlayingCard>();
		cards = new ArrayDeque<PlayingCard>();
		players = new ArrayList<Player>();
		
		
		this.initGame();

	}
	
	private void initGame() {
		this.houseResult = 0;
		players.forEach(Player::resetBet);
		//For each suit in all suits
		for(PlayingCard.Suit suit : PlayingCard.Suit.values()) {
			//For each value in all values
			int i = 8; //Cards' value, it would be 8-14
			for(PlayingCard.Value value : PlayingCard.Value.values()) {
				int score = 0;
				if(i <= 10) score = i; //if i is from 8 to 10, score equals to value
				if(i > 10 && i < 14) score = this.JQKSCORE; //if i is from 11 to 13, it is consider as JQK
				if(i == 14) score = this.ACESCORE;//if i is 14, it is consider as ACE
				this.cardCollection.add(new PlayingCardImpl(suit, value, score));//Insert the card
				i++;//next value
			}
		} 
	
	this.cards = this.getShuffledHalfDeck();
	}
	
	@Override
	public void dealPlayer(Player player, int delay) throws IllegalArgumentException {
		if(delay < 0 || delay > 1000) throw new IllegalArgumentException(); //throw exception if delay is not in range
		
		//Player turn
		PlayingCard card = this.cards.getFirst(); //get the top card from cards
		int currentResult = player.getResult();
		currentResult += card.getScore(); //calculate the result of the latest
		if(currentResult <= BUST_LEVEL){
			player.setResult(currentResult);
			cards.removeFirst(); //remove the top card once it's given
			gameEngineCallbacks.forEach(callback -> {
				callback.nextCard(player, card, this);
			});
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			dealPlayer(player, delay);//continue to do same the step
			return;
		} //out of the loop if the next result is over 42
		
		
		//Once out of the self loop, it is bust
		gameEngineCallbacks.forEach(callback -> {
			callback.bustCard(player, card, this);
		});
		
		gameEngineCallbacks.forEach(callback -> {
			callback.result(player, player.getResult(), this);
		});
	}

	@Override
	public void dealHouse(int delay) throws IllegalArgumentException {
		if(delay < 0 || delay > 1000) throw new IllegalArgumentException(); //throw exception if delay is not in range

		PlayingCard card = this.cards.getFirst(); //get the top card from cards
		int nextResult = this.houseResult + card.getScore(); //calculate the result of the latest
		
		if(nextResult <= BUST_LEVEL){
			this.houseResult = nextResult;
			cards.removeFirst(); //remove the top card once it is given
			gameEngineCallbacks.forEach(callback -> {
				callback.nextHouseCard(card, this);
			});
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			dealHouse(delay);//continue to do same the step
			return;
		}; //out of the loop if the next result is over 42
		
		//House busted
		gameEngineCallbacks.forEach(callback -> {
			callback.houseBustCard(card, this);
		});
		
		//Apply all players win and loss
		players.forEach(player -> {
			this.applyWinLoss(player, this.houseResult);
		});
		
		//House result
		gameEngineCallbacks.forEach(callback -> {
			callback.houseResult(this.houseResult, this);
			
		});
		
		//Reset bet for each player
		initGame();
	}

	@Override
	public void applyWinLoss(Player player, int houseResult) {
		if(player.getResult() > houseResult) { //player won
			player.setPoints(player.getPoints() + player.getBet() * 2);
		}else if(player.getResult() < houseResult){ //player lost
			player.setPoints(player.getPoints() - player.getBet());
		}
		//Or draws
//		player.setResult(0);
//		this.houseResult = 0;
		getShuffledHalfDeck();
	}

	@Override
	public void addPlayer(Player player){
		boolean isDupilicated = false;
		for(Player _player : players) {
			if(_player.getPlayerId() == player.getPlayerId()) {
				isDupilicated = true;
			}
		}
		if(isDupilicated == false) {
			players.add(player);
		}
		
	}

	@Override
	public Player getPlayer(String id) {
		for(Player player : players) {
			if(player.getPlayerId() == id) return player;
		}
		return null;
	}

	@Override
	public boolean removePlayer(Player player) {
		try {
			players.remove(player);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public boolean placeBet(Player player, int bet) {
		if(player.setBet(bet)) return true;
		return false;
	}

	@Override
	public void addGameEngineCallback(GameEngineCallback gameEngineCallback) {
		this.gameEngineCallbacks.add(gameEngineCallback);
	}

	@Override
	public boolean removeGameEngineCallback(GameEngineCallback gameEngineCallback) {
		try {
			gameEngineCallbacks.remove(gameEngineCallback);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	@Override
	public Collection<Player> getAllPlayers() {
		return new ArrayList<>(this.players);
	}

	@Override
	public Deque<PlayingCard> getShuffledHalfDeck() {
		Collections.shuffle(this.cardCollection);
		Deque<PlayingCard> shuflledCards = new ArrayDeque<PlayingCard>(this.cardCollection);
		return shuflledCards;
	}

}
