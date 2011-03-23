package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import org.junit.Before;
import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.cip.CIPTool.CIP_CHIRALITY;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond.Stereo;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Writer;

import stereo.wedge.WedgeStereoComparisonTool;
import stereo.wedge.WedgeStereoLifter;

public class WedgeStereoComparisonToolTest extends BaseTest {
    
    private WedgeStereoLifter lifter;
    
    @Before
    public void init() {
        lifter = new WedgeStereoLifter();
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
        String outputDir = "img/simples/";
        File file = new File(outputDir);
        if (!file.exists()) {
            file.mkdir();
        }
        for (String filename : filenames) {
            String inputPath = "data/simples/" + filename + ".mol";
            IMolecule mol = getMolecule(inputPath);
            String outputPath = outputDir + filename + "_2D.png";
            drawJCP(mol, outputPath);
        }
    }
    
    @Test
    public void makeImages() throws CDKException, IOException {
        String[] filenames = {"NDNU", "NUDN", "NUND", "NUNU", 
                              "NNDU", "NNUD", "NDUN", "NDND" };
        String outputDir = "img/simples/";
        File file = new File(outputDir);
        if (!file.exists()) {
            file.mkdir();
        }
        for (String filename : filenames) {
            String inputPath = "data/simples/" + filename + ".mol";
            IMolecule mol = getMolecule(inputPath);
            String outputPath = outputDir + filename + "_2D.png";
            
            drawJCP(mol, outputPath);
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
        
        new WedgeStereoComparisonTool();
        CIP_CHIRALITY chirality = 
            WedgeStereoComparisonTool.getChirality2D(
                    lifter, tetra.getAtom(0), tetra);
        System.out.println(chirality);
    }
    
    
    @Test
    public void down_none_up_Test() {
        IAtomContainer tetra = getTetra(Shape.CROSS, Stereo.NONE, Stereo.DOWN, Stereo.NONE, Stereo.UP);
        new WedgeStereoComparisonTool();
        CIP_CHIRALITY chirality = 
            WedgeStereoComparisonTool.getChirality2D(
                    lifter, tetra.getAtom(0), tetra);
        System.out.println(chirality);
    }
    
    @Test
    public void up_down_none_Test() {
        IAtomContainer tetra = getTetra(Shape.CROSS, Stereo.NONE, Stereo.UP, Stereo.DOWN, Stereo.NONE);
        new WedgeStereoComparisonTool();
        CIP_CHIRALITY chirality = 
            WedgeStereoComparisonTool.getChirality2D(
                    lifter, tetra.getAtom(0), tetra);
        System.out.println(chirality);
    }
    
    @Test
    public void up_none_down_Test() {
        IAtomContainer tetra = getTetra(Shape.CROSS, Stereo.NONE, Stereo.UP, Stereo.NONE, Stereo.DOWN);
        new WedgeStereoComparisonTool();
        CIP_CHIRALITY chirality = 
            WedgeStereoComparisonTool.getChirality2D(
                    lifter, tetra.getAtom(0), tetra);
        System.out.println(chirality);
    }

}
