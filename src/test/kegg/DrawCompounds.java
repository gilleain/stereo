package test.kegg;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;

import test.BaseTest;

public class DrawCompounds extends BaseTest {
    
    @Test
    public void drawAllDirect() throws CDKException, IOException {
        File outputDir = new File("img/kegg_compounds/direct");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
        File inputDir = new File("data/mols");
        for (String filename : inputDir.list()) {
            System.out.println("drawing : " + filename);
            String outputPath = new File(outputDir, filename).toString();
            String inputPath = new File(inputDir, filename).toString();
            IMolecule molecule = getMolecule(inputPath);
            drawDirect(molecule, outputPath + ".png");
        }
        
    }
    
    @Test
    public void drawAllJCP() throws CDKException, IOException {
        File outputDir = new File("img/kegg_compounds/jcp");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
        File inputDir = new File("data/mols");
        for (String filename : inputDir.list()) {
            System.out.println("drawing : " + filename);
            String outputPath = new File(outputDir, filename).toString();
            String inputPath = new File(inputDir, filename).toString();
            IMolecule molecule = getMolecule(inputPath);
            drawJCP(molecule, outputPath + ".png");
        }
        
    }

}
