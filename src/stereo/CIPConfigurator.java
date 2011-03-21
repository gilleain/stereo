package stereo;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IStereoElement;

import stereo.wedge.WedgeStereoLifter;

public class CIPConfigurator {
    
    public static void configure(IAtomContainer molecule) {
        for (IAtom atom : molecule.atoms()) {
            if (atom.getPoint2d() != null) {
                configureWedges(molecule);
                return;
            } else if (atom.getPoint3d() != null) {
                configure3D(molecule);
                return;
            }
        }
        
    }
    
    public static void configure3D(IAtomContainer molecule) {
        // TODO Auto-generated method stub
        
    }

    public static void configureWedges(IAtomContainer molecule) {
        WedgeStereoLifter lifter = new WedgeStereoLifter();
        for (IAtom atom : molecule.atoms()) {
            IStereoElement stereoElement = lifter.lift(atom, molecule);
            if (stereoElement != null) {
                molecule.addStereoElement(stereoElement);
            }
        }
    }

}
