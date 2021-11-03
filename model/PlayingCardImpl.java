package model;

import model.interfaces.PlayingCard;

import java.util.Objects;

public class PlayingCardImpl implements PlayingCard{
	private Suit suit;
	private Value value;
	private int score;

	public PlayingCardImpl() {
	}

	public PlayingCardImpl(Suit suit, Value value, int score) {
		this.suit = suit;
		this.value = value;
		this.score = score;
	}
	
	@Override
	public Suit getSuit() {
		return this.suit;
	}

	@Override
	public Value getValue() {
		return this.value;
	}

	@Override
	public int getScore() {
		return this.score;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PlayingCardImpl that = (PlayingCardImpl) o;
		return score == that.score &&
			suit == that.suit &&
			value == that.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(suit, value, score);
	}

	@Override
	public String toString() {
		return "PlayingCardImpl{" +
			"suit=" + suit +
			", value=" + value +
			", score=" + score +
			'}'; 
	}

	@Override
	public boolean equals(PlayingCard card) {
		if(card == null)
			return false;

		return score == card.getScore() &&
			suit == card.getSuit() &&
			value == card.getValue();
	}
}

