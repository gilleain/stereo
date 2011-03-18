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

import stereo.wedge.ACWTetrahedralWedgeRule;
import test.BaseTest;

import static org.openscience.cdk.interfaces.IBond.Stereo.UP;
import static org.openscience.cdk.interfaces.IBond.Stereo.DOWN;
import static org.openscience.cdk.interfaces.IBond.Stereo.NONE;;

public class AntiClockwiseTetrahedralWedgeRuleTest extends BaseTest {
    
    @Test
    public void matchTest() {
        IBond.Stereo[] instance = { NONE, DOWN, UP, NONE };
        ACWTetrahedralWedgeRule rule = new ACWTetrahedralWedgeRule();
        Assert.assertTrue(rule.matches(instance));
    }
    
    @Test
    public void misMatchTest() {
        IBond.Stereo[] instance = { NONE, UP, UP, NONE };
        ACWTetrahedralWedgeRule rule = new ACWTetrahedralWedgeRule();
        Assert.assertFalse(rule.matches(instance));
    }
    
    @Test
    public void matchPermutationTest() {
        IBond.Stereo[] instance = { NONE, NONE, DOWN, UP };
        ACWTetrahedralWedgeRule rule = new ACWTetrahedralWedgeRule();
        rule.matches(instance);
        int[] permutation = rule.getMatchPermutation();
        System.out.println(Arrays.toString(permutation));
    }
    
    @Test
    public void executeSTest() {
        IBond.Stereo[] stereos = { NONE, NONE, DOWN, UP };
        IAtomContainer atomContainer = getTetra(Shape.CROSS, stereos);
        IAtom centralAtom = atomContainer.getAtom(0);
        SortedMap<Double, IBond> angleMap = new TreeMap<Double, IBond>();
        angleMap.put(  0.0, atomContainer.getBond(0));
        angleMap.put( 90.0, atomContainer.getBond(1));
        angleMap.put(180.0, atomContainer.getBond(2));
        angleMap.put(360.0, atomContainer.getBond(3));
        
        ACWTetrahedralWedgeRule rule = new ACWTetrahedralWedgeRule();
        rule.matches(stereos);
        ITetrahedralChirality element = 
            (ITetrahedralChirality) rule.execute(centralAtom, angleMap);
        CIP_CHIRALITY chiral = CIPTool.getCIPChirality(atomContainer, element);
        Assert.assertEquals(CIP_CHIRALITY.S, chiral);
    }
    
    @Test
    public void executeRTest() {
        IBond.Stereo[] stereos = { NONE, DOWN, UP, NONE };
        IAtomContainer atomContainer = getTetra(Shape.CROSS, stereos);
        IAtom centralAtom = atomContainer.getAtom(0);
        SortedMap<Double, IBond> angleMap = new TreeMap<Double, IBond>();
        angleMap.put(  0.0, atomContainer.getBond(0));
        angleMap.put( 90.0, atomContainer.getBond(1));
        angleMap.put(180.0, atomContainer.getBond(2));
        angleMap.put(360.0, atomContainer.getBond(3));
        
        ACWTetrahedralWedgeRule rule = new ACWTetrahedralWedgeRule();
        rule.matches(stereos);
        ITetrahedralChirality element = 
            (ITetrahedralChirality) rule.execute(centralAtom, angleMap);
        CIP_CHIRALITY chiral = CIPTool.getCIPChirality(atomContainer, element);
        Assert.assertEquals(CIP_CHIRALITY.R, chiral);
    }

}
