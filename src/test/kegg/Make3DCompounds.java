package test.kegg;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import stereo.BalloonRunner;
import test.BaseTest;

public class Make3DCompounds extends BaseTest {
    
    @Test
    public void makeAllWithBalloon() throws IOException {
        File outputDir = new File("data/kegg_compounds");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
        File outputSubDir = new File(outputDir, "balloon");
        if (!outputSubDir.exists()) {
            outputSubDir.mkdir();
        }
        File inputDir = new File("data/mols");
        for (String filename : inputDir.list()) {
            System.out.println("generating : " + filename);
            File outputFile = new File(outputSubDir, filename);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            String outputPath = outputFile.toString();
            String inputPath = new File(inputDir, filename).toString();
            boolean addHydrogens = true;
            BalloonRunner.run(inputPath, outputPath, addHydrogens);
        }
            
    }

}
