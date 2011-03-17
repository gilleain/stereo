package test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smsd.labelling.AtomContainerPrinter;

import stereo.BalloonRunner;

public class BalloonRunnerTest {
    
    @Test
    public void runTest() throws IOException {
        String inputPath = "data/halomethane.mol";
        String outputPath = "data/halomethane_out.mol";
        
        int exitValue = BalloonRunner.run(inputPath, outputPath);
        Assert.assertEquals(0, exitValue);
    }
    
    @Test
    public void moleculeRunTest() throws IOException, CDKException {
        String inputPath = "data/halomethane.mol";
        String outputPath = "data/halomethane_out.mol";

        IMolecule mol = BalloonRunner.getMolecule(inputPath, outputPath);
        Assert.assertNotNull(mol);
        AtomContainerPrinter printer = new AtomContainerPrinter();
        System.out.println(printer.toString(mol));
    }
    
    @Test
    public void convertWedgeFiles() throws IOException {
        String dir = "data";
//        String[] filenames = {"NDNU", "NUDN", "NUND", "NUNU" };
        String[] filenames = {"NNDU", "NNUD", "NDUN", "NDND" };
        for (String filename : filenames) {
            String inputPath = dir + "/" + filename + ".mol";
            String outputPath = dir + "/" + filename + "_3D.mol";
            int exitValue = BalloonRunner.run(inputPath, outputPath);
            Assert.assertEquals(0, exitValue);
        }
    }
    
    @Test
    public void convertBentWedgeFiles() throws IOException {
        String dir = "data";
        String[] filenames = { "NDNU_bent_down", "NUND_bent_down" };
        for (String filename : filenames) {
            String inputPath = dir + "/" + filename + ".mol";
            String outputPath = dir + "/" + filename + "_3D.mol";
            int exitValue = BalloonRunner.run(inputPath, outputPath);
            Assert.assertEquals(0, exitValue);
        }
    }

}
