package test;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import stereo.CombinatorialMap;

public class CombinatorialMapTest {
    
    private void setBondIDs(IAtomContainer ac) {
        int i = 0;
        for (IBond bond : ac.bonds()) {
            bond.setID(String.valueOf(i));
            i++;
        }
    }
    
    public CombinatorialMap makeDamiandsExample(int... sigma) {
        IAtomContainer ac = new AtomContainer();
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("C"));
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(1, 2, IBond.Order.SINGLE);
        ac.addBond(2, 3, IBond.Order.SINGLE);
        ac.addBond(0, 3, IBond.Order.SINGLE);
        ac.addBond(0, 4, IBond.Order.SINGLE);
        ac.addBond(4, 5, IBond.Order.SINGLE);
        ac.addBond(5, 3, IBond.Order.SINGLE);
        ac.addBond(3, 6, IBond.Order.SINGLE);
        ac.addBond(6, 2, IBond.Order.SINGLE);
        setBondIDs(ac);
        return new CombinatorialMap(ac, sigma);
    }
    
    public CombinatorialMap makeK4(int... sigma) {
        IAtomContainer ac = new AtomContainer();
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("C"));
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(0, 2, IBond.Order.SINGLE);
        ac.addBond(0, 3, IBond.Order.SINGLE);
        ac.addBond(1, 2, IBond.Order.SINGLE);
        ac.addBond(1, 3, IBond.Order.SINGLE);
        ac.addBond(2, 3, IBond.Order.SINGLE);
        assert sigma.length == 2 * ac.getBondCount();
        setBondIDs(ac);
       
        return new CombinatorialMap(ac, sigma);
    }
    
    public void flagBondMapTest(CombinatorialMap map) {
        Map<Integer, IBond> flagBondMap = map.getFlagBondMap(); 
        for (int flag : flagBondMap.keySet()) {
            IBond bond  = flagBondMap.get(flag);
            System.out.println(flag + " " + bond.getID());
        }
    }
    
    public void phiTest(CombinatorialMap map) {
        System.out.println(Arrays.toString(map.calculateDualPermutation()));
    }
    
    @Test
    public void flagBondMap_K4Test() {
        flagBondMapTest(makeK4(2, 8, 4, 7, 0, 11, 1, 10, 6, 5, 3, 9));
    }
    
    @Test
    public void flagBondMap_DamiandsTest() {
        flagBondMapTest(makeDamiandsExample(8, 2, 1, 4, 17, 7, 0, 13, 6, 10, 9, 12, 11, 14, 5, 16, 15, 3));
    }
    
    @Test
    public void flagInvolutionTest() {
        CombinatorialMap k4 = makeK4(2, 8, 4, 7, 0, 11, 1, 10, 6, 5, 3, 9);
        System.out.println(Arrays.toString(k4.getFlagInvolution()));
    }
    
    @Test
    public void phi_K4Test() {
        phiTest(makeK4(2, 8, 4, 7, 0, 11, 1, 10, 6, 5, 3, 9));
    }
    
    @Test
    public void phi_DamiandsTest() {
        phiTest(makeDamiandsExample(8, 2, 1, 4, 17, 7, 0, 13, 6, 10, 9, 12, 11, 14, 5, 16, 15, 3));
    }

}
