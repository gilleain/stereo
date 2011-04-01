package test.chebi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;
import org.openscience.cdk.smsd.labelling.AtomContainerPrinter;

import stereo.wedge.WedgeStereoAnalyser;
import stereo.wedge.WedgeStereoAnalysisResult;
import test.BaseTest;

public class ChebiTests extends BaseTest {
    
    public static final String DATA_DIR = "data/mols/chebi/";
    
    // smiles from chebi
    public static final String smilesFor9859 =
        "OC[C@H]1O[C@H](O[C@@H]2[C@@H](O)[C@@H](O)[C@@H](CO)O" +
        "[C@@H]2O[C@]2(CO)O[C@H](CO)[C@@H](O)[C@@H]2O)[C@H](O)[C@@H](O)[C@H]1O";
    
    public IMolecule get(String chebiID) throws FileNotFoundException, CDKException {
        return getMolecule(DATA_DIR + "ChEBI_" + chebiID + ".mol");
    }
    
    @Test
    public void compareStereoElements() throws FileNotFoundException, CDKException {
        IMolecule molFromFile = get("9859");
        // also sets the stereo elements as a side-effect
        WedgeStereoAnalyser.getResults(molFromFile);
        Map<IAtom, ITetrahedralChirality> fromFileChiralMap = getStereoMap(molFromFile);
        
        SmilesParser parser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule molFromSmiles = parser.parseSmiles(smilesFor9859);
        Map<IAtom, ITetrahedralChirality> fromSmilesChiralMap = getStereoMap(molFromSmiles);
        
        Isomorphism isomorphism = new Isomorphism(Algorithm.DEFAULT, false);
        isomorphism.init(molFromFile, molFromSmiles, false, false);
        Map<IAtom, IAtom> atomAtomMapping = isomorphism.getFirstAtomMapping();
        
        for (IAtom fromFileAtom : fromFileChiralMap.keySet()) {
            ITetrahedralChirality fromFileChirality = fromFileChiralMap.get(fromFileAtom);
            IAtom fromSmilesAtom = atomAtomMapping.get(fromFileAtom);
            ITetrahedralChirality fromSmilesChirality = fromSmilesChiralMap.get(fromSmilesAtom);
            if (fromFileChirality.getStereo() == fromSmilesChirality.getStereo()) {
                System.out.println("MATCH");
            } else {
                System.out.println("MISMATCH " 
                        + molFromFile.getAtomNumber(fromFileAtom)
                        + " : "
                        + molFromSmiles.getAtomNumber(fromSmilesAtom));
            }
        }
    }
    
    public Map<IAtom, ITetrahedralChirality> getStereoMap(IAtomContainer atomContainer) {
        Map<IAtom, ITetrahedralChirality> stereoMap = 
            new HashMap<IAtom, ITetrahedralChirality>();
        for (IStereoElement stereoElement : atomContainer.stereoElements()) {
            if (stereoElement instanceof ITetrahedralChirality) {
                ITetrahedralChirality itc = (ITetrahedralChirality) stereoElement;
                stereoMap.put(itc.getChiralAtom(), itc);
            }
        }
        return stereoMap;
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
        
        // XXX : only need to do this for the added H's - could they be laid out?
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
        SmilesParser parser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IMolecule mol = parser.parseSmiles(smilesFor9859);
        for (IStereoElement stereoElement : mol.stereoElements()) {
            if (stereoElement instanceof ITetrahedralChirality) {
                ITetrahedralChirality chir = (ITetrahedralChirality) stereoElement;
                IAtom chirAtom = chir.getChiralAtom();
                System.out.println(chirAtom.getSymbol() + mol.getAtomNumber(chirAtom) + "{" + chir.getStereo() + "}");
            }
        }
    }

}
