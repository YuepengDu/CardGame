package view;

import java.util.Collection;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.interfaces.GameEngine;
import model.interfaces.Player;
import model.interfaces.PlayingCard;
import view.interfaces.GameEngineCallback;

/**
 * 
 * Skeleton/Partial example implementation of GameEngineCallback showing Java logging behaviour
 * 
 * @author Caspar Ryan
 * @see view.interfaces.GameEngineCallback
 * 
 */
public class GameEngineCallbackImpl implements GameEngineCallback
{
   public static final Logger logger = Logger.getLogger(GameEngineCallbackImpl.class.getName());

   // utility method to set output level of logging handlers
   public static Logger setAllHandlers(Level level, Logger logger, boolean recursive)
   {
      // end recursion?
      if (logger != null)
      {
         logger.setLevel(level);
         for (Handler handler : logger.getHandlers())
            handler.setLevel(level);
         // recursion
         setAllHandlers(level, logger.getParent(), recursive);
      }
      return logger;
   }

   public GameEngineCallbackImpl()
   {
      // setAllHandlers(Level.FINE, logger, true);
   }

   @Override
   public void nextCard(Player player, PlayingCard card, GameEngine engine)
   {
	   String resultStr = String.format("Card Dealt to %s .. Suit: %s, Value: %s, Score: %s", player.getPlayerName(), 
				 card.getSuit(), card.getValue(), card.getScore());
		logger.log(Level.INFO, resultStr); 
   }

   @Override
   public void result(Player player, int result, GameEngine engine)
   {
      // final results logged at Level.INFO
	  String resultStr = String.format("%s, final result=%s", player.getPlayerName(), result);
      logger.log(Level.INFO, resultStr);
   }

	@Override
	public void bustCard(Player player, PlayingCard card, GameEngine engine) {
		this.nextCard(player, card, engine);
	    logger.log(Level.INFO, "YOU BUSTED!");
	}
	
	@Override
	public void nextHouseCard(PlayingCard card, GameEngine engine) {
		String resultStr = String.format("Card Dealt to House .. Suit: %s, Value: %s, Score: %s", 
				card.getSuit(), card.getValue(), card.getScore());
		logger.log(Level.INFO, resultStr);
	}
	
	@Override
	public void houseBustCard(PlayingCard card, GameEngine engine) {
		this.nextHouseCard(card, engine);
	    logger.log(Level.INFO, "HOUSE BUSTED!");
	}
	
	@Override
	public void houseResult(int result, GameEngine engine) {
		String resultStr = String.format("House, final result=%s", result);
		logger.log(Level.INFO, resultStr);
		
		//Printing final result here
		Collection<Player> players = engine.getAllPlayers();
		String finalString = "Final Player Results\n";
		for(Player player : players) {
			finalString+= player.toString() + "\n";
		}
		logger.log(Level.INFO, finalString);
	}
}
