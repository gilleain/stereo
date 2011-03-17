package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.vecmath.Point2d;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Stereo;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.tools.periodictable.PeriodicTable;

import stereo.WedgeStereoComparisonTool;

public class WedgeStereoComparisonToolTest extends BaseTest {
    
    public enum Shape { CROSS, BENT_DOWN, BENT_UP }
    
    private void crossShape(IAtomContainer tetra) {
        tetra.addAtom(new Atom("C",  new Point2d( 0,  0)));
        tetra.addAtom(new Atom("F",  new Point2d( 1,  0)));
        tetra.addAtom(new Atom("Cl", new Point2d( 0, -1)));
        tetra.addAtom(new Atom("Br", new Point2d(-1,  0)));
        tetra.addAtom(new Atom("I",  new Point2d( 0,  1)));
    }
    
    private void bentDownShape(IAtomContainer tetra) {
        tetra.addAtom(new Atom("C",  new Point2d( 0,   0)));
        tetra.addAtom(new Atom("F",  new Point2d( 1, 0.5)));
        tetra.addAtom(new Atom("Cl", new Point2d( 0,  -1)));
        tetra.addAtom(new Atom("Br", new Point2d(-1, 0.5)));
        tetra.addAtom(new Atom("I",  new Point2d( 0,   1)));
    }
    
    private IAtomContainer getTetra(Shape shape, Stereo... stereos) {
        IAtomContainer tetra = new AtomContainer();
        if (shape == Shape.CROSS) {
            crossShape(tetra);
        } else if (shape == Shape.BENT_DOWN) {
            bentDownShape(tetra);
        }
        for (int i = 1; i < 5; i++) {
            tetra.addBond(0, i, IBond.Order.SINGLE);
        }
        for (IAtom atom : tetra.atoms()) {
            Integer atNumber = PeriodicTable.getAtomicNumber(atom.getSymbol());
            if (atNumber == null) { 
                System.err.println("Null atNumber " + atom.getSymbol());
            }
            atom.setAtomicNumber(atNumber);
        }
        
        Assert.assertEquals(4, stereos.length);
        for (int index = 0; index < 4; index++) {
            tetra.getBond(index).setStereo(stereos[index]);
        }
        
        return tetra;
    }
    
    public void printMolFile(IAtomContainer atomContainer, String fileName) {
        File dir = new File("data");
        File fullPath = new File(dir, fileName + ".mol");
        try {
            MDLV2000Writer writer = new MDLV2000Writer(new FileWriter(fullPath));
            writer.writeMolecule(atomContainer);
            writer.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void makeBentImages() throws CDKException, IOException {
        String[] filenames = { "NDNU_bent_down", "NUND_bent_down" };
        for (String filename : filenames) {
            String inputPath = "data/" + filename + ".mol";
            IMolecule mol = getMolecule(inputPath);
            String outputPath = "img/" + filename + "_2D.png";
            draw(mol, outputPath);
        }
    }
    
    @Test
    public void makeImages() throws CDKException, IOException {
        String[] filenames = {"NDNU", "NUDN", "NUND", "NUNU", 
                              "NNDU", "NNUD", "NDUN", "NDND" };
        for (String filename : filenames) {
            String inputPath = "data/" + filename + ".mol";
            IMolecule mol = getMolecule(inputPath);
            String outputPath = "img/" + filename + "_2D.png";
            draw(mol, outputPath);
        }
    }
    
    @Test
    public void makeBentMolFiles() {
        IAtomContainer NDNU = getTetra(Shape.BENT_DOWN, Stereo.NONE, Stereo.DOWN, Stereo.NONE, Stereo.UP);
        printMolFile(NDNU, "NDNU_bent_down");
        IAtomContainer NUND = getTetra(Shape.BENT_DOWN, Stereo.NONE, Stereo.UP, Stereo.NONE, Stereo.DOWN);
        printMolFile(NUND, "NUND_bent_down");
        
    }
    
    @Test
    public void makeMolFiles() {
        IAtomContainer NUNU = getTetra(Shape.CROSS, Stereo.NONE, Stereo.UP, Stereo.NONE, Stereo.UP);
        printMolFile(NUNU, "NUNU");
        IAtomContainer NDNU = getTetra(Shape.CROSS, Stereo.NONE, Stereo.DOWN, Stereo.NONE, Stereo.UP);
        printMolFile(NDNU, "NDNU");
        IAtomContainer NUDN = getTetra(Shape.CROSS, Stereo.NONE, Stereo.UP, Stereo.DOWN, Stereo.NONE);
        printMolFile(NUDN, "NUDN");
        IAtomContainer NUND = getTetra(Shape.CROSS, Stereo.NONE, Stereo.UP, Stereo.NONE, Stereo.DOWN);
        printMolFile(NUND, "NUND");
        IAtomContainer NNDU = getTetra(Shape.CROSS, Stereo.NONE, Stereo.NONE, Stereo.DOWN, Stereo.UP);
        printMolFile(NNDU, "NNDU");
        IAtomContainer NNUD = getTetra(Shape.CROSS, Stereo.NONE, Stereo.NONE, Stereo.UP, Stereo.DOWN);
        printMolFile(NNUD, "NNUD");
        IAtomContainer NDUN = getTetra(Shape.CROSS, Stereo.NONE, Stereo.DOWN, Stereo.UP, Stereo.NONE);
        printMolFile(NDUN, "NDUN");
        IAtomContainer NDND = getTetra(Shape.CROSS, Stereo.NONE, Stereo.DOWN, Stereo.NONE, Stereo.DOWN);
        printMolFile(NDND, "NDND");
    }
    
    @Test
    public void up_none_up_Test() {
        IAtomContainer tetra = getTetra(Shape.CROSS, Stereo.NONE, Stereo.UP, Stereo.NONE, Stereo.UP);
        CIP_CHIRALITY chirality = 
            new WedgeStereoComparisonTool().getChirality2D(tetra.getAtom(0), tetra);
        System.out.println(chirality);
    }
    
    
    @Test
    public void down_none_up_Test() {
        IAtomContainer tetra = getTetra(Shape.CROSS, Stereo.NONE, Stereo.DOWN, Stereo.NONE, Stereo.UP);
        CIP_CHIRALITY chirality = 
            new WedgeStereoComparisonTool().getChirality2D(tetra.getAtom(0), tetra);
        System.out.println(chirality);
    }
    
    @Test
    public void up_down_none_Test() {
        IAtomContainer tetra = getTetra(Shape.CROSS, Stereo.NONE, Stereo.UP, Stereo.DOWN, Stereo.NONE);
        CIP_CHIRALITY chirality = 
            new WedgeStereoComparisonTool().getChirality2D(tetra.getAtom(0), tetra);
        System.out.println(chirality);
    }
    
    @Test
    public void up_none_down_Test() {
        IAtomContainer tetra = getTetra(Shape.CROSS, Stereo.NONE, Stereo.UP, Stereo.NONE, Stereo.DOWN);
        CIP_CHIRALITY chirality = 
            new WedgeStereoComparisonTool().getChirality2D(tetra.getAtom(0), tetra);
        System.out.println(chirality);
    }

}
