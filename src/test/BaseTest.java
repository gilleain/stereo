package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
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
import org.openscience.cdk.geometry.GeometryTools;
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
import org.openscience.reactionblast.graphics.direct.DirectMoleculeDrawer;
import org.openscience.reactionblast.graphics.direct.Params;

public class BaseTest {

    private final int w = 500;
    private final int h = 500;
    
    public boolean arrayEquals(int[] expected, int[] actual) {
        if (expected.length != actual.length) return false;
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] == actual[i]) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public IMolecule getMolecule(String filename) throws FileNotFoundException, CDKException {
        MDLV2000Reader reader = new MDLV2000Reader(new FileReader(filename));
        return reader.read(new Molecule());
    }
    
    public void drawDirect(IMolecule molecule, String outputPath) throws IOException {
        Params params = new Params();
        params.bondLength = 40;
        params.atomSymbolFontSize = 14;
        params.drawAtomID = true;
        double scale = GeometryTools.getScaleFactor(molecule, params.bondLength);
        GeometryTools.scaleMolecule(molecule, scale);
        int i = 0;
        for (IAtom atom : molecule.atoms()) {
            Point2d p = atom.getPoint2d();
            p.y *= -1;
            atom.setPoint2d(p);
            atom.setID(String.valueOf(i));
            i++;
        }
        translateTo(molecule, w/ 2, h / 2);
        DirectMoleculeDrawer drawer = new DirectMoleculeDrawer(params);
        Image image = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, w, h);
        graphics.setColor(Color.BLACK);
        drawer.drawMolecule(molecule, graphics);
        ImageIO.write((RenderedImage) image, "PNG", new File(outputPath));
    }

    public void drawJCP(IMolecule molecule, String outputPath) throws IOException {
        List<IGenerator<IAtomContainer>> generators = 
            new ArrayList<IGenerator<IAtomContainer>>();
        generators.add(new BasicSceneGenerator());
        generators.add(new BasicBondGenerator());
        generators.add(new BasicAtomGenerator());
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
    
    public void translateTo(IAtomContainer ac, double x, double y) {
        Rectangle2D bounds = GeometryTools.getRectangle2D(ac);
        double dx = x - bounds.getCenterX();
        double dy = y - bounds.getCenterY();
        System.out.println("moving to " +x + " " + y + " by "+ dx + " " + dy);
        for (IAtom atom : ac.atoms()) {
            atom.getPoint2d().x += dx;
            atom.getPoint2d().y += dy;
        }
    }

    public enum Shape { CROSS, BENT_DOWN, BENT_UP }
    
    public IAtomContainer getTriangle(Shape shape, Stereo... stereos) {
        IAtomContainer tri = new AtomContainer();
        if (shape == Shape.BENT_UP) {
            tri.addAtom(new Atom("C",  new Point2d( 0,  0)));
            tri.addAtom(new Atom("F",  new Point2d( 0,  1)));
            tri.addAtom(new Atom("Cl", new Point2d( 1, -1)));
            tri.addAtom(new Atom("Br", new Point2d(-1, -1)));
        } else {
            tri.addAtom(new Atom("C",  new Point2d( 0,  0)));
            tri.addAtom(new Atom("F",  new Point2d( 0, -1)));
            tri.addAtom(new Atom("Cl", new Point2d( 1,  1)));
            tri.addAtom(new Atom("Br", new Point2d(-1,  1)));
        }
        for (int i = 1; i < 4; i++) {
            tri.addBond(0, i, IBond.Order.SINGLE);
        }
        setAtomNumber(tri);
        Assert.assertEquals(3, stereos.length);
        for (int index = 0; index < 3; index++) {
            tri.getBond(index).setStereo(stereos[index]);
        }
        
        return tri;
    }

    public void crossShape(IAtomContainer tetra) {
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
        setAtomNumber(tetra);

        Assert.assertEquals(4, stereos.length);
        for (int index = 0; index < 4; index++) {
            tetra.getBond(index).setStereo(stereos[index]);
        }

        return tetra;
    }
    
    public void setAtomNumber(IAtomContainer atomContainer) {
        for (IAtom atom : atomContainer.atoms()) {
            Integer atNumber = PeriodicTable.getAtomicNumber(atom.getSymbol());
            if (atNumber == null) { 
                System.err.println("Null atNumber " + atom.getSymbol());
            }
            atom.setAtomicNumber(atNumber);
        }
    }

}
