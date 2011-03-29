package stereo.wedge;

public enum WedgeStereoAnalysisResult {
    CHIRAL_R,       // an R-chirality at the atom
    CHIRAL_S,       // an S-chirality at the atom
    MISSING,        // 4 different neighbours, but missing wedges 
    UNKNOWN,        // combination of wedges unknown
    ERROR,          // wedges around a non-stereo center
    NONE,           // not-chiral
}
