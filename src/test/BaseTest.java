package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.vecmath.Point2d;

import junit.framework.Assert;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IBond.Stereo;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.IRenderer;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.tools.periodictable.PeriodicTable;

public class BaseTest {

    private final int w = 200;
    private final int h = 200;

    public IMolecule getMolecule(String filename) throws FileNotFoundException, CDKException {
        MDLV2000Reader reader = new MDLV2000Reader(new FileReader(filename));
        return reader.read(new Molecule());
    }

    public void draw(IMolecule molecule, String outputPath) throws IOException {
        List<IGenerator<IAtomContainer>> generators = 
            new ArrayList<IGenerator<IAtomContainer>>();
        generators.add(new BasicSceneGenerator());
        generators.add(new BasicAtomGenerator());
        generators.add(new BasicBondGenerator());
        IRenderer<IAtomContainer> molrenderer = 
            new AtomContainerRenderer(generators, new AWTFontManager());
        molrenderer.setup(molecule, new Rectangle(w, h));
        Image image = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, w, h);
        molrenderer.paint(molecule, new AWTDrawVisitor(graphics));
        ImageIO.write((RenderedImage) image, "PNG", new File(outputPath));
    }

    public enum Shape { CROSS, BENT_DOWN, BENT_UP }

    public  void crossShape(IAtomContainer tetra) {
        tetra.addAtom(new Atom("C",  new Point2d( 0,  0)));
        tetra.addAtom(new Atom("F",  new Point2d( 1,  0)));
        tetra.addAtom(new Atom("Cl", new Point2d( 0, -1)));
        tetra.addAtom(new Atom("Br", new Point2d(-1,  0)));
        tetra.addAtom(new Atom("I",  new Point2d( 0,  1)));
    }

    public void bentDownShape(IAtomContainer tetra) {
        tetra.addAtom(new Atom("C",  new Point2d( 0,   0)));
        tetra.addAtom(new Atom("F",  new Point2d( 1, 0.5)));
        tetra.addAtom(new Atom("Cl", new Point2d( 0,  -1)));
        tetra.addAtom(new Atom("Br", new Point2d(-1, 0.5)));
        tetra.addAtom(new Atom("I",  new Point2d( 0,   1)));
    }

    public IAtomContainer getTetra(Shape shape, Stereo... stereos) {
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

}
