package test.stereo.wedge;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.interfaces.IBond;

import stereo.wedge.TetrahedralWedgeRule;

import static org.openscience.cdk.interfaces.IBond.Stereo.UP;
import static org.openscience.cdk.interfaces.IBond.Stereo.DOWN;
import static org.openscience.cdk.interfaces.IBond.Stereo.NONE;;

public class TetrahedralWedgeRuleTest {
    
    @Test
    public void matchTest() {
        IBond.Stereo[] instance = { NONE, UP, DOWN, NONE };
        TetrahedralWedgeRule rule = new TetrahedralWedgeRule();
        Assert.assertTrue(rule.matches(instance));
    }
    
    @Test
    public void misMatchTest() {
        IBond.Stereo[] instance = { NONE, UP, UP, NONE };
        TetrahedralWedgeRule rule = new TetrahedralWedgeRule();
        Assert.assertFalse(rule.matches(instance));
    }
    
    @Test
    public void matchPermutationTest() {
        IBond.Stereo[] instance = { NONE, NONE, UP, DOWN };
        TetrahedralWedgeRule rule = new TetrahedralWedgeRule();
        rule.matches(instance);
        int[] permutation = rule.getMatchPermutation();
        System.out.println(Arrays.toString(permutation));
    }

}
