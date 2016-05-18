package cardgame.cards;

import cardgame.*;

/**
 * Created by valdemar on 17/05/2016.
 */
public class Arcbond extends AbstractCard{

    private final String nome = "Arcbond";

    public String toString() {
        return name() + " [" + ruleText() +"]";
    }

    @Override
    public Effect getEffect(Player owner) {
        return null;
    }

    @Override
    public String type() {
        return "Instant";
    }

    @Override
    public String ruleText() {
        return "Choose a target creature. Whenever that creature is dealt damage this turn, it deals that much damage to each other creature and each player.";
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public String name() {
        return nome;
    }

    private class ArcbondEffect extends AbstractCardEffect implements TargetingEffect{
        protected ArcbondEffect(Player p, Card c) {
            super(p, c);
        }

        @Override
        public void pickTarget() {

        }

        @Override
        public void resolve() {

        }
    }
}
