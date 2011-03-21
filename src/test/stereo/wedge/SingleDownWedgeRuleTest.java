package test.stereo.wedge;

import java.util.SortedMap;
import java.util.TreeMap;


import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.ITetrahedralChirality;

import stereo.wedge.SingleDownWedgeRule;
import test.BaseTest;

import static org.openscience.cdk.interfaces.IBond.Stereo.DOWN;
import static org.openscience.cdk.interfaces.IBond.Stereo.NONE;

public class SingleDownWedgeRuleTest extends BaseTest {
    
    @Test
    public void matchTest() {
        IBond.Stereo[] instance = { NONE, DOWN, NONE };
        SingleDownWedgeRule rule = new SingleDownWedgeRule();
        Assert.assertTrue(rule.matches(instance));
    }
    
    @Test
    public void misMatchTest() {
        IBond.Stereo[] instance = { DOWN, DOWN, NONE };
        SingleDownWedgeRule rule = new SingleDownWedgeRule();
        Assert.assertFalse("Matched a pattern it should not have", 
                rule.matches(instance));
    }
    
    @Test
    public void matchPermutationTest() {
        IBond.Stereo[] instance = { NONE, NONE, DOWN };
        SingleDownWedgeRule rule = new SingleDownWedgeRule();
        rule.matches(instance);
        Assert.assertTrue(
                arrayEquals(new int[] {2, 0, 1}, rule.getMatchPermutation()));
    }
    
    public void test(IBond.Stereo[] stereos) {
        IAtomContainer atomContainer = getTriangle(Shape.BENT_UP, stereos);
        IAtom centralAtom = atomContainer.getAtom(0);
        SortedMap<Double, IBond> angleMap = new TreeMap<Double, IBond>();
        angleMap.put(  0.0, atomContainer.getBond(0));
        angleMap.put(120.0, atomContainer.getBond(1));
        angleMap.put(240.0, atomContainer.getBond(2));
        
        SingleDownWedgeRule rule = new SingleDownWedgeRule();
        rule.matches(stereos);
        ITetrahedralChirality element = 
            (ITetrahedralChirality) rule.execute(
                centralAtom, atomContainer, angleMap);
        CIP_CHIRALITY chiral = CIPTool.getCIPChirality(atomContainer, element);
        Assert.assertEquals(CIP_CHIRALITY.R, chiral);
    }
    
    @Test
    public void nndTest() {
        IBond.Stereo[] stereos = { NONE, NONE, DOWN };
        test(stereos);
    }
    
    @Test
    public void ndnTest() {
        IBond.Stereo[] stereos = { NONE, DOWN, NONE };
        test(stereos);
    }
    
    @Test
    public void dnnTest() {
        IBond.Stereo[] stereos = { DOWN, NONE, NONE };
        test(stereos);
    }
}
