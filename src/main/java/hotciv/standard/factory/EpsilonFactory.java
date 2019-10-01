package hotciv.standard.factory;

import hotciv.framework.age.AgeStrategy;
import hotciv.framework.age.ConstantAging;
import hotciv.framework.layout.LayoutStrategy;
import hotciv.framework.layout.StandardLayout;
import hotciv.framework.random.RandomStrategy;
import hotciv.framework.resolveAttack.ActualCombat;
import hotciv.framework.resolveAttack.ResolveAttackStrategy;
import hotciv.framework.unitAction.NoAction;
import hotciv.framework.unitAction.UnitActionStrategy;
import hotciv.framework.victoryStrategy.ThreeCombatVictories;
import hotciv.framework.victoryStrategy.VictoryStrategy;
import hotciv.framework.workforce.NoWorkableTiles;
import hotciv.framework.workforce.WorkforceStrategy;

public class EpsilonFactory implements StrategyFactory {
    private ActualCombat combat;

    public AgeStrategy createAgeStrategy() { return new ConstantAging(); }
    public VictoryStrategy createVictoryStrategy() { return new ThreeCombatVictories();}
    public LayoutStrategy createLayoutStrategy() { return new StandardLayout(); }
    public ResolveAttackStrategy createAttackStrategy() { return combat; }
    public UnitActionStrategy createActionStrategy() { return new NoAction(); }
    public WorkforceStrategy createWorkforceStrategy() { return new NoWorkableTiles(); }

    public EpsilonFactory() {
        combat = new ActualCombat();
    }
    public EpsilonFactory(RandomStrategy random) {
        combat = new ActualCombat(random);
    }
}