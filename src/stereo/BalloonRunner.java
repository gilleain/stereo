package stereo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;

public class BalloonRunner {
    
    public static final String BALLOON_DIR = "lib/balloon_macosx";
    
    public static IMolecule getMolecule(String inputPath, String outputPath) throws IOException, CDKException {
        int exitValue = run(inputPath, outputPath);
        if (exitValue == 0) {
            MDLV2000Reader reader = new MDLV2000Reader(new FileReader(outputPath));
            return reader.read(new Molecule());
        } else {
            return null;
        }
    }
    
    public static int run(String inputPath, String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        List<String> commands = new ArrayList<String>();
        commands.add(String.format("%s/balloon", BALLOON_DIR));
        commands.add(inputPath);
        commands.add(outputPath);
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        Process process = processBuilder.start();
        
        BufferedReader error = 
            new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = error.readLine()) != null) {
            System.err.println(line);
        }
        try {
            return process.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            return 1;
        }
    }

}
