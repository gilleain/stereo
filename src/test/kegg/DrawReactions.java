package test.kegg;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IReaction;

import test.BaseTest;

public class DrawReactions extends BaseTest {
    
    public static final int w = 900;
    public static final int h = 400;
    
    @Test
    public void drawAll() throws IOException, CDKException {
        File rxnDir = new File("data/rxn/");
        File imgDir = new File("img/rxn");
        if (!imgDir.exists()) {
            imgDir.mkdir();
        }
        for (String fileName : rxnDir.list()) {
            System.out.println("drawing " + fileName);
            IReaction reaction = readReaction(new File(rxnDir, fileName).toString());
            drawReaction(reaction, new File(imgDir, fileName + ".png").toString(), w, h);
        }
    }
    
    public void draw(String reactionID) throws IOException, CDKException {
        File rxnDir = new File("data/rxn/");
        File imgDir = new File("img/rxn");
        if (!imgDir.exists()) {
            imgDir.mkdir();
        }
        String fileName = reactionID + ".rxn";
        IReaction reaction = readReaction(new File(rxnDir, fileName).toString());
        drawReaction(reaction, new File(imgDir, fileName + ".png").toString(), w, h);
    }
    
    @Test
    public void drawR00027() throws IOException, CDKException {
       draw("R00027");
    }

}
