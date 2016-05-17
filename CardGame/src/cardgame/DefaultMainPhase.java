package cardgame;

import java.util.ArrayList;
import java.util.Scanner;


public class DefaultMainPhase implements Phase {

    
    public void execute() {
        Player current_player = CardGame.instance.getCurrentPlayer();
        int response_player_idx = (CardGame.instance.getPlayer(0) == current_player)?1:0;
        
        System.out.println(current_player.name() + ": main phase");
        
        CardGame.instance.getTriggers().trigger(Triggers.MAIN_FILTER);
        
        
        // alternate in placing effect until bith players pass
        int number_passes=0;
        
        if (!Utilities.play_available_effect(current_player, true))
            ++number_passes;
        
        while (number_passes<2) {
            if (Utilities.play_available_effect(CardGame.instance.getPlayer(response_player_idx),false))
                number_passes=0;
            else ++number_passes;
            
            response_player_idx = (response_player_idx+1)%2;
        }
        
        CardGame.instance.getStack().resolve();
    }
    
}
