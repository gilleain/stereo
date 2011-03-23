package test.kegg;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.reactionblast.graphics.direct.DirectReactionDrawer;
import org.openscience.reactionblast.graphics.direct.Params;
import org.openscience.reactionblast.graphics.direct.layout.LeftToRightReactionLayout;

import test.BaseTest;

public class DrawReactions extends BaseTest {
    
    private final int w = 800;
    private final int h = 400;
    
    public void drawReaction(IReaction reaction, String outputPath) throws IOException {
        Params params = new Params();
        params.drawMappings = false;
        params.highlightSubgraphs = true;
        params.drawSubgraphBoxes = true;
        LeftToRightReactionLayout layout = new LeftToRightReactionLayout();
        layout.setParams(params);
        layout.shouldInvert = false;
        DirectReactionDrawer reactionDrawer = new DirectReactionDrawer(params, layout);
        Image image = reactionDrawer.drawReaction(reaction, w, h);
        ImageIO.write((RenderedImage) image, "PNG", new File(outputPath));
    }
    
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
            drawReaction(reaction, new File(imgDir, fileName + ".png").toString());
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
        drawReaction(reaction, new File(imgDir, fileName + ".png").toString());
    }
    
    @Test
    public void drawR00027() throws IOException, CDKException {
       draw("R00027");
    }

}
