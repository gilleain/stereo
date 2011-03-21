package test.stereo.wedge;

import java.util.Arrays;
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

import stereo.wedge.FullTetrahedralWedgeRule;
import test.BaseTest;

import static org.openscience.cdk.interfaces.IBond.Stereo.UP;
import static org.openscience.cdk.interfaces.IBond.Stereo.DOWN;

public class FullTetrahedralWedgeRuleTest extends BaseTest {
    
    @Test
    public void matchTest() {
        IBond.Stereo[] instance = { DOWN, UP, DOWN, UP };
        FullTetrahedralWedgeRule rule = new FullTetrahedralWedgeRule();
        Assert.assertTrue(rule.matches(instance));
    }
    
    @Test
    public void misMatchTest() {
        IBond.Stereo[] instance = { DOWN, UP, UP, DOWN };
        FullTetrahedralWedgeRule rule = new FullTetrahedralWedgeRule();
        Assert.assertFalse(rule.matches(instance));
    }
    
    @Test
    public void matchPermutationTest() {
        IBond.Stereo[] instance = { DOWN, UP, DOWN, UP };
        FullTetrahedralWedgeRule rule = new FullTetrahedralWedgeRule();
        rule.matches(instance);
        int[] expected = new int[] {1, 2, 3, 0};
        int[] actual = rule.getMatchPermutation();
        String error = String.format("expected : %s got %s",
                Arrays.toString(expected), Arrays.toString(actual));
        Assert.assertTrue(error, arrayEquals(expected, actual));
    }
    
    @Test
    public void duduTest() {
        IBond.Stereo[] stereos = { DOWN, UP, DOWN, UP };
        IAtomContainer atomContainer = getTetra(Shape.CROSS, stereos);
        IAtom centralAtom = atomContainer.getAtom(0);
        SortedMap<Double, IBond> angleMap = new TreeMap<Double, IBond>();
        angleMap.put(  0.0, atomContainer.getBond(0));
        angleMap.put( 90.0, atomContainer.getBond(1));
        angleMap.put(180.0, atomContainer.getBond(2));
        angleMap.put(360.0, atomContainer.getBond(3));
        
        FullTetrahedralWedgeRule rule = new FullTetrahedralWedgeRule();
        rule.matches(stereos);
        ITetrahedralChirality element = 
            (ITetrahedralChirality) rule.execute(
                    centralAtom, atomContainer, angleMap);
        CIP_CHIRALITY chiral = CIPTool.getCIPChirality(atomContainer, element);
        Assert.assertEquals(CIP_CHIRALITY.S, chiral);
    }
    
    @Test
    public void ududTest() {
        IBond.Stereo[] stereos = { UP, DOWN, UP, DOWN };
        IAtomContainer atomContainer = getTetra(Shape.CROSS, stereos);
        IAtom centralAtom = atomContainer.getAtom(0);
        SortedMap<Double, IBond> angleMap = new TreeMap<Double, IBond>();
        angleMap.put(  0.0, atomContainer.getBond(0));
        angleMap.put( 90.0, atomContainer.getBond(1));
        angleMap.put(180.0, atomContainer.getBond(2));
        angleMap.put(360.0, atomContainer.getBond(3));
        
        FullTetrahedralWedgeRule rule = new FullTetrahedralWedgeRule();
        rule.matches(stereos);
        ITetrahedralChirality element = 
            (ITetrahedralChirality) rule.execute(
                    centralAtom, atomContainer, angleMap);
        CIP_CHIRALITY chiral = CIPTool.getCIPChirality(atomContainer, element);
        Assert.assertEquals(CIP_CHIRALITY.R, chiral);
    }

}
