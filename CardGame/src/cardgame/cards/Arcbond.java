package cardgame.cards;

import cardgame.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valdemar on 17/05/2016.
 */
public class Arcbond extends AbstractCard{

    private final String nome = "Arcbond";

    class ArcbondDecorator implements CreatureDecorator{

        protected Creature decorated;
        TriggerAction action;

        @Override
        public CreatureDecorator decorate(Creature c) {
            decorated = c;
            return this;
        }

        @Override
        public Creature removeDecorator(CreatureDecorator d) {
            Creature result = this;
            if(d == this){
                result = decorated;
                decorated = null;
                onRemove();
            }
            else if(decorated instanceof  CreatureDecorator)
                decorated = ((CreatureDecorator)decorated).removeDecorator(d);
            return result;
        }

        @Override
        public String name() {
            return decorated.name();
        }

        @Override
        public int power() {
            return decorated.power();
        }

        @Override
        public int toughness() {
            return decorated.toughness();
        }

        @Override
        public void tap() {
            decorated.tap();
        }

        @Override
        public void untap() {
            decorated.untap();
        }

        @Override
        public boolean isTapped() {
            return decorated.isTapped();
        }

        @Override
        public boolean canAttack() {
            return decorated.canAttack();
        }

        @Override
        public void attack() {
            decorated.attack();
        }

        @Override
        public boolean canDefend() {
            return decorated.canDefend();
        }

        @Override
        public void defend() {
            decorated.defend();
        }

        @Override
        public void inflictDamage(int dmg) {
            for(DecoratedCreature dc : CardGame.instance.getCurrentPlayer().getCreatures())
                dc.inflictDamage(dmg);
            for(DecoratedCreature dc : CardGame.instance.getCurrentAdversary().getCreatures()){
                dc.inflictDamage(dmg);
            }
            CardGame.instance.getCurrentPlayer().inflictDamage(dmg);
            CardGame.instance.getCurrentAdversary().inflictDamage(dmg);
        }

        @Override
        public int getDamage() {
            return decorated.getDamage();
        }

        @Override
        public void resetDamage() {
            decorated.resetDamage();
        }

        @Override
        public void destroy() {
            decorated.destroy();
        }

        @Override
        public void onRemove() {
            System.out.println("Removing " + nome + " and deregistering end of turn trigger");
            if (action!=null)
                CardGame.instance.getTriggers().remove(action);
            decorated.onRemove();
        }

        @Override
        public List<Effect> effects() {
            return decorated.effects();
        }

        @Override
        public List<Effect> avaliableEffects() {
            return decorated.avaliableEffects();
        }

        @Override
        public void setTopDecorator(DecoratedCreature c) {
            decorated.setTopDecorator(c);
        }

        @Override
        public DecoratedCreature getTopDecorator() {
            return decorated.getTopDecorator();
        }

        public String toString(){
            return decorated.toString();
        }

        public void setRemoveAction(TriggerAction a){
            System.out.println("Removing " + nome + " and deregistering end of turn trigger");
            if (action!=null)
                CardGame.instance.getTriggers().remove(action);
            decorated.onRemove();
        }
    }

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
        private DecoratedCreature target;

        protected ArcbondEffect(Player p, Card c) {
            super(p, c);
        }

        public boolean play(){
            pickTarget();
            return super.play();
        }

        @Override
        public void pickTarget() {
            System.out.println( owner.name() + ": choose target for " + name() );

            ArrayList<DecoratedCreature> creatures=new ArrayList<>();
            int i=1;

            Player player1 = CardGame.instance.getPlayer(0);
            Player player2 = CardGame.instance.getPlayer(1);

            for (DecoratedCreature c: player1.getCreatures()) {
                System.out.println( i + ") " + player1.name() + ": " + c);
                ++i;
                creatures.add(c);
            }
            for (DecoratedCreature c: player2.getCreatures()) {
                System.out.println( i + ") " + player2.name() + ": " + c);
                ++i;
                creatures.add(c);
            }

            int idx= CardGame.instance.getScanner().nextInt()-1;
            if (idx<0 || idx>=creatures.size()) target=null;
            else target=creatures.get(idx);
        }

        @Override
        public void resolve() {
            if(target == null)
                return;
            if(target.isRemoved()){
                System.out.println("Attaching " + nome + " to removed creature");
                return;
            }

            final ArcbondDecorator decorator = new ArcbondDecorator();
            TriggerAction action = new TriggerAction() {
                @Override
                public void execute() {
                    if (!target.isRemoved()){
                        System.out.println("Triggered removal of " + nome + " from " + target);
                        target.removeDecorator(decorator);
                    } else {
                        System.out.println("Triggered dangling removal of " + nome + " from removed target. Odd should have been invalidated!" );
                    }
                }
            };
            System.out.println("Attaching " + nome + " to " + target.name() + " and registering end_of_turn filter");
            CardGame.instance.getTriggers().register(Triggers.END_FILTER, action);

            decorator.setRemoveAction(action);
            target.addDecorator(decorator);
        }
    }
}
