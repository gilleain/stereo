package test.chebi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.cip.CIPTool;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.interfaces.ITetrahedralChirality.Stereo;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.smsd.labelling.AtomContainerPrinter;

import stereo.wedge.WedgeStereoAnalyser;
import stereo.wedge.WedgeStereoAnalysisResult;
import stereo.wedge.WedgeStereoLifter;
import test.BaseTest;

public class ChebiTests extends BaseTest {
    
    public static final String DATA_DIR = "data/mols/chebi/";
    
    public IMolecule get(String chebiID) throws FileNotFoundException, CDKException {
        return getMolecule(DATA_DIR + "ChEBI_" + chebiID + ".mol");
    }
    
    @Test
    public void tmpCipTest() {
        IMolecule molecule = new Molecule();
        molecule.addAtom(new Atom("C"));
        molecule.addAtom(new Atom("C"));
        molecule.addAtom(new Atom("C"));
        molecule.addAtom(new Atom("C"));
        molecule.addAtom(new Atom("C"));
        molecule.addAtom(new Atom("C"));
        molecule.addAtom(new Atom("C"));
        molecule.addAtom(new Atom("C"));
        molecule.addAtom(new Atom("C"));
        molecule.addAtom(new Atom("C"));
        molecule.addBond(0, 1, IBond.Order.SINGLE, IBond.Stereo.UP);
        molecule.addBond(0, 2, IBond.Order.SINGLE);
        molecule.addBond(0, 3, IBond.Order.SINGLE);
        molecule.addBond(1, 4, IBond.Order.SINGLE);
        molecule.addBond(2, 5, IBond.Order.SINGLE);
        molecule.addBond(2, 6, IBond.Order.SINGLE);
        molecule.addBond(3, 7, IBond.Order.SINGLE);
        molecule.addBond(3, 8, IBond.Order.SINGLE);
        molecule.addBond(3, 9, IBond.Order.SINGLE);
        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule(molecule, false);
        try {
            sdg.generateCoordinates();
        } catch (Exception e) {
            
        }
        AtomContainerPrinter acp = new AtomContainerPrinter();
        System.out.println(acp.toString(molecule));
//        CIPTool.defineLigancyFourChirality(molecule, 0, 1, 2, 3, -1, Stereo.CLOCKWISE);
        WedgeStereoLifter lifter = new WedgeStereoLifter();
        lifter.lift(molecule.getAtom(0), molecule);
        System.out.println(acp.toString(molecule));
    }
    
    @Test
    public void umbelliferoseTest() throws CDKException, IOException {
        AtomContainerPrinter acp = new AtomContainerPrinter();
        IMolecule umbelliferose = get("9859");
        int atomIndex = 0;
        for (WedgeStereoAnalysisResult result : WedgeStereoAnalyser.getResults(umbelliferose)) {
            System.out.println(atomIndex + " " + result);
            atomIndex++;
        }
        System.out.println(acp.toString(umbelliferose));
        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule(umbelliferose, false);
        try {
            sdg.generateCoordinates();
        } catch (Exception e) {
            
        }
        drawDirect(umbelliferose, "img/umbelliferose.png");
        System.out.println(acp.toString(umbelliferose));
        SmilesGenerator smilesWriter = new SmilesGenerator();
        boolean[] doubleBondConfig = new boolean[umbelliferose.getBondCount()];
        Arrays.fill(doubleBondConfig, false);
        String smiles = smilesWriter.createChiralSMILES(
                umbelliferose, doubleBondConfig);
        System.out.println(smiles);
        SmilesParser parser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = parser.parseSmiles(smiles);
        for (IStereoElement stereoElement : mol.stereoElements()) {
            System.out.println(stereoElement);
        }
    }
    
    @Test
    public void umbelliferoseSmilesTest() throws CDKException, IOException {
        String smiles = "OC[C@H]1O[C@H](O[C@@H]2[C@@H](O)[C@@H](O)[C@@H](CO)O" +
        "[C@@H]2O[C@]2(CO)O[C@H](CO)[C@@H](O)[C@@H]2O)[C@H](O)[C@@H](O)[C@H]1O";
        SmilesParser parser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = parser.parseSmiles(smiles);
        for (IStereoElement stereoElement : mol.stereoElements()) {
            if (stereoElement instanceof ITetrahedralChirality) {
                ITetrahedralChirality chir = (ITetrahedralChirality) stereoElement;
                IAtom chirAtom = chir.getChiralAtom();
                System.out.println(chirAtom.getSymbol() + mol.getAtomNumber(chirAtom) + "{" + chir.getStereo() + "}");
            }
        }
    }

}
