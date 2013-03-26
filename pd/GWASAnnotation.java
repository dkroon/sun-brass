package pd;

import ch.systemsx.cisd.hdf5.HDF5Factory;
import ch.systemsx.cisd.hdf5.IHDF5Writer;
import net.maizegenetics.pal.alignment.ImportUtils;
import net.maizegenetics.pal.alignment.BitNucleotideAlignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: dkroon
 */
public class GWASAnnotation {

    private boolean myIsSBit = true;

    private String[] chromosomes = new String[] { "chr1", "chr2", "chr3", "chr4", "chr5", "chr6", "chr7", "chr8", "chr9", "chr10" };

    // original files "\\\\andersonii\\group\\E\\SolexaAnal\\hapmapV2\\hp1\\standardQ87Q87Union\\HapMapV2RefGenV2\\";
    
    private String hapMapFile_prefix = "/maizeHapMapV2_B73RefGenV2_201203028_";

    private String hapMapFile_suffix = ".hmp.txt";

    private static final int PHYSICAL_POSITION_COLUMN = 0;
    private static final int MINOR_ALLELE_FREQUENCY_COLUMN = 1;
    private static final int COLUMN_OFFSET = 2;  // one for physical position column and another for minor allele frequency column

    public BitNucleotideAlignment readFile(String inFile){
        BitNucleotideAlignment result = (BitNucleotideAlignment)ImportUtils.readFromHapmap(inFile, myIsSBit, null /*progressListener*/ );
        return result;
    }

//    public static String writeToHDF5(Alignment a, String newHDF5file) {
//
//        a = AlignmentUtils.optimizeForSites(a);
//        a = AlignmentUtils.optimizeForTaxa(a);
//        IHDF5Writer h5w = null;
//        try {
//
//            int numSites = a.getSiteCount();
//            int numTaxa = a.getSequenceCount();
//
//            newHDF5file = Utils.addSuffixIfNeeded(newHDF5file, ".hmp.h5");
//            File hdf5File = new File(newHDF5file);
//            if (hdf5File.exists()) {
//                throw new IllegalArgumentException("ExportUtils: writeToHDF5: File already exists: " + newHDF5file);
//            }
//            IHDF5WriterConfigurator config = HDF5Factory.configure(hdf5File);
////            myLogger.info("Writing HDF5 file: " + newHDF5file);
//            config.overwrite();
//            config.dontUseExtendableDataTypes();
//            h5w = config.writer();
//
//            h5w.setIntAttribute(HDF5Constants.DEFAULT_ATTRIBUTES_PATH, HDF5Constants.MAX_NUM_ALLELES, a.getMaxNumAlleles());
//
//            h5w.setBooleanAttribute(HDF5Constants.DEFAULT_ATTRIBUTES_PATH, HDF5Constants.RETAIN_RARE_ALLELES, a.retainsRareAlleles());
//
//            h5w.setIntAttribute(HDF5Constants.DEFAULT_ATTRIBUTES_PATH, HDF5Constants.NUM_TAXA, numTaxa);
//
//            int numSBitWords = a.getAllelePresenceForAllTaxa(0, 0).getNumWords();
//            h5w.setIntAttribute(HDF5Constants.DEFAULT_ATTRIBUTES_PATH, HDF5Constants.NUM_SBIT_WORDS, numSBitWords);
//
//            int numTBitWords = a.getAllelePresenceForAllSites(0, 0).getNumWords();
//            h5w.setIntAttribute(HDF5Constants.DEFAULT_ATTRIBUTES_PATH, HDF5Constants.NUM_TBIT_WORDS, numTBitWords);
//
//            String[][] aEncodings = a.getAlleleEncodings();
//            //myLogger.info(Arrays.deepToString(aEncodings));
//            int numEncodings = aEncodings.length;
//            int numStates = aEncodings[0].length;
//            MDArray<String> alleleEncodings = new MDArray<String>(String.class, new int[]{numEncodings, numStates});
//            for (int s = 0; s < numEncodings; s++) {
//                for (int x = 0; x < numStates; x++) {
//                    alleleEncodings.set(aEncodings[s][x], s, x);
//                }
//            }
//
//            h5w.createStringMDArray(HDF5Constants.ALLELE_STATES, 100, new int[]{numEncodings, numStates});
//            h5w.writeStringMDArray(HDF5Constants.ALLELE_STATES, alleleEncodings);
//            MDArray<String> alleleEncodingReadAgain = h5w.readStringMDArray(HDF5Constants.ALLELE_STATES);
//            if (alleleEncodings.equals(alleleEncodingReadAgain) == false) {
//                throw new IllegalStateException("ExportUtils: writeToHDF5: Mismatch Allele States, expected '" + alleleEncodings + "', found '" + alleleEncodingReadAgain + "'!");
//            }
//
//            h5w.writeStringArray(HDF5Constants.SNP_IDS, a.getSNPIDs());
//
//            h5w.createGroup(HDF5Constants.SBIT);
//            h5w.setIntAttribute(HDF5Constants.DEFAULT_ATTRIBUTES_PATH, HDF5Constants.NUM_SITES, numSites);
//
//            String[] lociNames = new String[a.getNumLoci()];
//            Locus[] loci = a.getLoci();
//            for (int i = 0; i < a.getNumLoci(); i++) {
//                lociNames[i] = loci[i].getName();
//            }
//            h5w.createStringVariableLengthArray(HDF5Constants.LOCI, a.getNumLoci());
//            h5w.writeStringVariableLengthArray(HDF5Constants.LOCI, lociNames);
//
//            h5w.createIntArray(HDF5Constants.LOCUS_OFFSETS, a.getNumLoci());
//            h5w.writeIntArray(HDF5Constants.LOCUS_OFFSETS, a.getLociOffsets());
//
//            h5w.createIntArray(HDF5Constants.POSITIONS, numSites);
//            h5w.writeIntArray(HDF5Constants.POSITIONS, a.getPhysicalPositions());
//
//            h5w.createByteMatrix(HDF5Constants.ALLELES, a.getSiteCount(), a.getMaxNumAlleles());
//            byte[][] alleles = new byte[numSites][a.getMaxNumAlleles()];
//            for (int i = 0; i < numSites; i++) {
//                alleles[i] = a.getAlleles(i);
//            }
//            h5w.writeByteMatrix(HDF5Constants.ALLELES, alleles);
//
//            String[] tn = new String[numTaxa];
//            for (int i = 0; i < tn.length; i++) {
//                tn[i] = a.getFullTaxaName(i);
//            }
//            h5w.createStringVariableLengthArray(HDF5Constants.TAXA, numTaxa);
//            h5w.writeStringVariableLengthArray(HDF5Constants.TAXA, tn);
//
//            for (int aNum = 0; aNum < a.getTotalNumAlleles(); aNum++) {
//
//                String currentSBitPath = HDF5Constants.SBIT + "/" + aNum;
//                h5w.createLongMatrix(currentSBitPath, numSites, numSBitWords, 1, numSBitWords);
//                for (int i = 0; i < numSites; i++) {
//                    long[][] lg = new long[1][numSBitWords];
//                    lg[0] = a.getAllelePresenceForAllTaxa(i, aNum).getBits();
//                    h5w.writeLongMatrixBlockWithOffset(currentSBitPath, lg, i, 0);
//                }
//
//                String currentTBitPath = HDF5Constants.TBIT + "/" + aNum;
//                h5w.createLongMatrix(currentTBitPath, numTaxa, numTBitWords, 1, numTBitWords);
//                for (int i = 0; i < numTaxa; i++) {
//                    long[][] lg = new long[1][numTBitWords];
//                    lg[0] = a.getAllelePresenceForAllSites(i, aNum).getBits();
//                    h5w.writeLongMatrixBlockWithOffset(currentTBitPath, lg, i, 0);
//                }
//            }
//
//            return newHDF5file;
//
//        } finally {
//            try {
//                h5w.close();
//            } catch (Exception e) {
//                // do nothing
//            }
//        }
//    }


    private float[][] loadAllChromosome(File gwasDir, File hapMapDir){
        IHDF5Writer writer = HDF5Factory.open("chromosomes10.h5");
        // Define the block size as 10 x 10.
        writer.createIntMatrix("results", 10, 10);

       // List<int[][]> aResultList = new ArrayList<int[][]>();
        for(int i =9; i>=9; i--){
            System.out.println("chromosomes = " + chromosomes[i]);
            GWASAnnotation ga = new GWASAnnotation();
            String chromosomeFile = hapMapDir + hapMapFile_prefix + chromosomes[i] + hapMapFile_suffix;
            System.out.println("Reading in " + chromosomeFile.toString());
            BitNucleotideAlignment bna = ga.readFile(chromosomeFile);    // full chromosome
            System.out.println("Completed reading in chromosome.");

            int[][] chrResults = loadGWAS(bna, gwasDir, chromosomes[i]);

            //todo test and verify
            writer.writeIntMatrixBlock("results", chrResults, i, 0);

            //aResultList.add(chrResults);

            chrResults = null;
            bna = null;
        }
        writer.close();
        return null;
    }



    private int[][] loadGWAS(BitNucleotideAlignment bna,  File gwasDirIn, String chromosomeIn){
    	System.out.println("Reading in GWAS Folder.");
    	FolderParser fp = new FolderParser(gwasDirIn);
        String[] traits = fp.getAllTraits();
        System.out.println("getting physical positions.");
        int[] alignmentPhysPos = bna.getPhysicalPositions();

        int chrBaseCount = alignmentPhysPos.length;
        System.out.println("Getting MAF.");
        float[] maf = new float[chrBaseCount];
        for(int i=0; i< maf.length; i++){
            maf[i] = (float)bna.getMinorAlleleFrequency(i);
        }



        int[][] result = new int[chrBaseCount][traits.length + GWASAnnotation.COLUMN_OFFSET];

//        float[] alignPhysPosFloat = convertIntToFloat(alignmentPhysPos);

        // add the alignment's physical positions to the (float) data array
        try{
        	System.out.println("Adding phycial positions ot results.");
            addNewColumns(alignmentPhysPos, result, GWASAnnotation.PHYSICAL_POSITION_COLUMN);
        }catch (Exception e ) { /* ignore */ }

        // when starting a new chromosome, write out the results of the last chromosome
        // write 2^20 bases as a block in HDF5
        for(int j=0; j<traits.length; j++){
        	
            File gwasFile = fp.getFile(chromosomeIn, traits[j]);
            System.out.println("Loading trait: " + j + " " + traits[j]);
            int fieldOfInterest = 1;
            int[]intVals = parseGWASIntValues(gwasFile, fieldOfInterest, true );

            // if the file is empty, move on to next trait
            if(intVals == null) continue;


            // get the gwas value(s) of interest
            int gwasPos = 6;

            int[] vals = new int[alignmentPhysPos.length];
            vals = parseGWASFloatValues( gwasFile, gwasPos, true);

            String matchOutput = new String();  // for writing out to a file for
            // add the gwas values to the final (float) data array
            try{
            	System.out.println("Adding to final results, trait: " + j + " " + traits[j]);
                matchOutput = addNewColumns(vals, result, j+ COLUMN_OFFSET, intVals);;
            }catch (Exception e) {
                System.err.println("Problem adding values to results.  File: " + gwasFile.toString() + " trait: " + traits[j]);
            }

        }

        bna = null;
        fp = null;
        // TODO use FolderParser to get all of the other features that Jason Wallace has obtains
        return result;// all gwas trait results for that chromosome
    }

    private String getFileTrait(File aFile, String token, int field){
        String fileName = aFile.getName();
        String[] part = fileName.split(token);
        return part[field];
    }



    private float[][] loadGWASResults(File directory, int[] alignmentPhysPos ){

        // obtain all files in the directory
        File[] directoryFile = getFiles(directory.toString());


        int fileCount = 0;
        int crossesLoaded = 0;
        FileReader fr = null;

        int baseCount = alignmentPhysPos.length;
        float[][] result = new float[baseCount][directoryFile.length+10];
        String[] trait = new String[directoryFile.length+10];
        float[] alignPhys = convertIntToFloat(alignmentPhysPos);

        // add the alignment's physical positions to the final (float) data array
        try{
            addNewColumns(alignPhys, result, 0);
        }catch (Exception e ) { /* ignore */ }

        // for each file in the directory
        for (int i = 0; i < directoryFile.length; i++) {
            boolean fileAccessed = false;
            if (!directoryFile[i].isDirectory()) {  // do not recurse into sub-directories
                try {
                    fr = new FileReader(directoryFile[i]);
                    fileAccessed = true;
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }

            String traitName;
            if (fileAccessed) {
                int traitPosition = 0;
                String filenameToken = "\\.";
                traitName = getFileTrait(directoryFile[i], filenameToken, traitPosition);
                trait[i] = traitName;
                System.out.println("traitName = " + traitName);
                fileCount++;

                // get the physical position(s)
//                int[] physicalPos = new int[] {1};
                int fieldOfInterest = 1;
                int[]intVals = parseGWASIntValues(directoryFile[i], fieldOfInterest, true );
                // get the gwas value(s) of interest
                int[] gwasPos = new int[] {6};

                float[][] floatVals = new float[alignmentPhysPos.length][1];
                //floatVals = parseGWASFloatValues(directoryFile[i], gwasPos, true);


                String matchOutput = new String();  // for writing out to a file for
                // add the gwas values to the final (float) data array
                try{
                    matchOutput = addNewColumns(floatVals, result, i+1, intVals);
                }catch (Exception e) { /* ignore */ }


                if(intVals.length == 0) continue;

            } else {
                String msg  = "Unable to access file: " + directoryFile[i].getAbsolutePath() + "\n";
                System.out.println(msg);
            }
        }// end for

        writeHDF("20130312_chr09", trait, result);
        return null;
    }


    private File[] getFiles(String dir){
        File directory = new File(dir);
        File[] directoryFile = directory.listFiles();
        if (directoryFile.length < 1) {
            String output = "Directory contains no files.";
//            JOptionPane.showMessageDialog(c, "Directory contains no files: \n" + directory.getPath());
            return null;
        }
        return directoryFile;
    }


    private void writeHDF(String fileName, String[] traitIn, float[][] data){

        IHDF5Writer writer = HDF5Factory.open(fileName + ".h5");
        writer.createStringVariableLengthArray("traitName", traitIn.length);
        writer.writeStringVariableLengthArray("traitName", traitIn);

        // Define the block size as 10 x 10.
        writer.createIntMatrix("mydata", data.length, data[0].length);

        writer.writeFloatMatrix("mydata", data);
//        for(int i = 0; i < data.length; i++) {
//            writer.writeFloatMatrixBlock("mydata", data, i, i);
//        }

        writer.close();
    }

    private float[] convertIntToFloat(int[] input){
        if(input == null) return null;
        float[] output = new float[input.length];
        for(int i = 0; i < input.length; i++){
            output[i] = input[i];
        }
        return output;
    }


    /**
     *
     * @param input
     * @param dataFinal
     * @param column
     */
    private void addNewColumns(float[] input, float[][] dataFinal, int column) throws Exception{
        if(input.length != dataFinal.length) throw new Exception("The float[][] dataFinal must be the same length as the input");
        boolean debugMe = false;
        if(dataFinal[0].length < column)   {
            throw new ArrayIndexOutOfBoundsException("Column does not exist: " + column);
        }
        int lastIndex = 0;
        for(int i = 0; i < dataFinal.length; i++){
            dataFinal[i][column] = input[i];
            if(debugMe){
                System.out.println("i + \" \" + input[i] + \" \" + column  = " + i + " " + input[i] + " " + column );
            }
        }
    }


    /**
     *
     * @param input
     * @param dataFinal
     * @param column
     */
    private void addNewColumns(int[] input, int[][] dataFinal, int column) throws Exception{
        if(input.length != dataFinal.length) throw new Exception("The float[][] dataFinal must be the same length as the input");
        boolean debugMe = false;
        if(dataFinal[0].length < column)   {
            throw new ArrayIndexOutOfBoundsException("Column does not exist: " + column);
        }
        int lastIndex = 0;
        for(int i = 0; i < dataFinal.length; i++){
            dataFinal[i][column] = input[i];
            if(debugMe){
                System.out.println("i + \" \" + input[i] + \" \" + column  = " + i + " " + input[i] + " " + column );
            }
        }
    }
    /**
     * Assumes that input positions are in increasing order
     * @param input float[] data to be added to the dataFinal
     * @param dataFinal float[][] which is the recipient of added data
     * @param column column in dataFinal to which the input is added
     * @param inputPosition int[] physical positions of the input data. It is assumed that the positions are increasing.
     * @throws Exception input[] must be of the same length as inputPosition
     */
    private String addNewColumns(float[] input, float[][] dataFinal, int column, int[] inputPosition) throws Exception{
        if(input.length != inputPosition.length){ throw new Exception("input[] must be the same length as the inputPostions[]"); }

        boolean debugMe = false;
        if(dataFinal[0].length < column)   {
            throw new ArrayIndexOutOfBoundsException("Column does not exist: " + column);
        }

        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < dataFinal.length; i++){
            float alignmentPos = dataFinal[i][0];
            for(int j = 0; j < inputPosition.length; j++){
//                if((alignmentPos -50 < (float)inputPosition[j]) && (alignmentPos +50 > (float)inputPosition[j])){
                if((alignmentPos -2 < (float)inputPosition[j]) && (alignmentPos +2 > (float)inputPosition[j])){
                    dataFinal[i][column] = input[j];

                    sb.append("alignmentPos = " + alignmentPos);
                    sb.append("\tinputPosition[" + j + "]:  " + inputPosition[j]);
                    sb.append("\tdataFinal[" + i +"][" + column + "]: " + dataFinal[i][column]);
//
//                    System.out.print("alignmentPos = " + alignmentPos);
//                    System.out.print("\tinputPosition[" + j + "]:  " + inputPosition[j]);
//                    System.out.println("\tdataFinal[" + i +"][" + column + "]: " + dataFinal[i][column]);
                }
            }
            if(debugMe){
                System.out.println("i + \" \" + input[i] + \" \" + column  = " + i + " " + input[i] + " " + column );
            }
        }
        return sb.toString();
    }


    /**
     * Assumes that input positions are in increasing order
     * @param input float[] data to be added to the dataFinal
     * @param dataFinal float[][] which is the recipient of added data
     * @param column column in dataFinal to which the input is added
     * @param inputPosition int[] physical positions of the input data. It is assumed that the positions are increasing.
     * @throws Exception input[] must be of the same length as inputPosition
     */
    private String addNewColumns(int[] input, int[][] dataFinal, int column, int[] inputPosition) throws Exception{
        if(input.length != inputPosition.length){ throw new Exception("input[] must be the same length as the inputPostions[]"); }

        boolean debugMe = false;
        if(dataFinal[0].length < column)   {
            throw new ArrayIndexOutOfBoundsException("Column does not exist: " + column);
        }

        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < dataFinal.length; i++){
            float alignmentPos = dataFinal[i][0];
            for(int j = 0; j < inputPosition.length; j++){
//                if((alignmentPos -50 < (float)inputPosition[j]) && (alignmentPos +50 > (float)inputPosition[j])){
                if((alignmentPos == (float)inputPosition[j])){
                    dataFinal[i][column] = input[j];
//
//                    sb.append("alignmentPos = " + alignmentPos);
//                    sb.append("\tinputPosition[" + j + "]:  " + inputPosition[j]);
//                    sb.append("\tdataFinal[" + i +"][" + column + "]: " + dataFinal[i][column]);
////
//                    System.out.print("alignmentPos = " + alignmentPos);
//                    System.out.print("\tinputPosition[" + j + "]:  " + inputPosition[j]);
//                    System.out.println("\tdataFinal[" + i +"][" + column + "]: " + dataFinal[i][column]);
                }
            }
            if(debugMe){
                System.out.println("i + \" \" + input[i] + \" \" + column  = " + i + " " + input[i] + " " + column );
            }
        }
        return null; //sb.toString();
    }
    private String addNewColumns(float[][] input, float[][] original,  int startingColumn, int[] inputPositions) throws Exception{

        if(input == null) {
            System.out.println("addNewColumns() input was null");
            return "";
        }
        for(int i = 0; i < input[0].length; i++){
            int rowsToCopy = input.length;
            float[] column = new float[rowsToCopy];
            for(int j=0; j < rowsToCopy; j++){
                column[j] = input[j][i];
            }
            return addNewColumns(column, original, startingColumn, inputPositions);
        }
        return "";
    }

    /**
     * Gets the index matching the value.  Assumes increasing order of values in int[].
     * @param lastMatch
     * @param data  it is assumed that the initial column of data contains physical location
     * @return
     */
    private int getMatchingPositionIndex(int value, int lastMatch, float[][] data){
        int rows = data.length;
        if(lastMatch >= rows) return -1;
        for(int i = lastMatch; i < rows; i++){
            if(data[i][0] == value) return i;
        }
        return -1;
    }

    private float[][] generateCombinedDataset(int[] alignmentPhysicalPosition, int[] traitPhysicalPostion, float[] gwasResult){


        return null;
    }

    /**
     *
     * @param aFile
     * @param fieldsOfInterest
     * @param hasHeader  If true, skips the first line in the file
     * @return
     */
    private int[][] parseGWASIntValues(File aFile, int[] fieldsOfInterest, boolean hasHeader){

        int[][] fieldValue = null;
        StringBuffer sb = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(aFile));
            String str;
            while ((str = in.readLine()) != null) {
                if (sb == null) {
                    sb = new StringBuffer(str + "\n");
                } else {
                    sb.append(str.trim() + "\n");
                }
            }
            if (sb == null) {   //  if the StringBuffer was never initialized
                //  then there are no records in a file
                String msg = "File " + aFile.getName() + " could not be loaded.  Could it be empty?";
            } else {

                String[] line = sb.toString().split("\n");
                int lineCount = line.length;

                int lineIndex = 0;
                fieldValue = new int[lineCount][fieldsOfInterest.length];
                if(hasHeader){
                    lineIndex = 1;
                    fieldValue = new int[lineCount-1][fieldsOfInterest.length];
                }

                int range = 0;

                while (lineIndex < lineCount) {
                    String[] val = line[lineIndex].split("\t");
                    int value = -1;
                    String toBeParsed = null;
                    try{
                        for(int i = 0; i<fieldsOfInterest.length; i++){
                            toBeParsed = val[fieldsOfInterest[i]];
                            if(hasHeader){
                                fieldValue[lineIndex-1][i] = Integer.parseInt(toBeParsed);
                            }else{
                                fieldValue[lineIndex][i] = Integer.parseInt(toBeParsed);
                            }
                        }
                    }catch(NumberFormatException nfe){
                        System.err.println("File " + aFile.getName() + " int parsing failed: " + toBeParsed + "lineIndex: " + lineIndex);
                    }
                    lineIndex++;
                }
            }
            in.close();
        } catch (IOException ioe) {
            String msg = "Failed while reading in file: " + aFile.getAbsolutePath() + "\n";
            System.err.println(msg);
            ioe.printStackTrace();
        }

        return fieldValue;
    }


    /**
     *
     * @param aFile
     * @param fieldOfInterest
     * @param hasHeader  If true, skips the first line in the file
     * @return
     */
    private int[] parseGWASIntValues(File aFile, int fieldOfInterest, boolean hasHeader){

        int[] fieldValue = null;
        StringBuffer sb = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(aFile));
            String str;
            while ((str = in.readLine()) != null) {
                if (sb == null) {
                    sb = new StringBuffer(str + "\n");
                } else {
                    sb.append(str.trim() + "\n");
                }
            }
            if (sb == null) {   //  if the StringBuffer was never initialized
                //  then there are no records in a file
                String msg = "File " + aFile.getName() + " could not be loaded.  Could it be empty?";
                return null;
            } else {

                String[] line = sb.toString().split("\n");
                int lineCount = line.length;

                int lineIndex = 0;
                fieldValue = new int[lineCount];
                if(hasHeader){
                    lineIndex = 1;
                    fieldValue = new int[lineCount-1];
                }

                int range = 0;

                while (lineIndex < lineCount) {
                    String[] val = line[lineIndex].split("\t");
                    int value = -1;
                    String toBeParsed = null;
                    try{
                        toBeParsed = val[fieldOfInterest];
                        if(hasHeader){
                            fieldValue[lineIndex-1] = Integer.parseInt(toBeParsed);
                        }else{
                            fieldValue[lineIndex] = Integer.parseInt(toBeParsed);
                        }
                    }catch(NumberFormatException nfe){
                        System.err.println("File " + aFile.getName() + " int parsing failed: " + toBeParsed + "lineIndex: " + lineIndex);
                    }
                    lineIndex++;
                }
            }
            in.close();
        } catch (IOException ioe) {
            String msg = "Failed while reading in file: " + aFile.getAbsolutePath() + "\n";
            System.err.println(msg);
            ioe.printStackTrace();
        }

        return fieldValue;
    }
    /**
     *
     * @param aFile
     * @param fieldOfInterest
     * @param hasHeader  If true, skips the first line in the file
     * @return
     */
    private int[] parseGWASFloatValues(File aFile, int fieldOfInterest, boolean hasHeader){

        int[] fieldValue = null;
        StringBuffer sb = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(aFile));
            String str;
            while ((str = in.readLine()) != null) {
                if (sb == null) {
                    sb = new StringBuffer(str + "\n");
                } else {
                    sb.append(str.trim() + "\n");
                }
            }
            if (sb == null) {   //  if the StringBuffer was never initialized
                //  then there are no records in a file
                String msg = "File " + aFile.getName() + " could not be loaded.  Could it be empty?";
            } else {

                String[] line = sb.toString().split("\n");
                int lineCount = line.length;
                int lineIndex = 0;
                fieldValue = new int[lineCount];
                if(hasHeader) {
                    lineIndex=1;
                    fieldValue = new int[lineCount-1];
                }

                int field = 1;     // field containing information of interest
                int range = 0;
                while (lineIndex < lineCount) {
                    String[] val = line[lineIndex].split("\t");
                    int value = -1;
                    try {

                        if (hasHeader) {
                            fieldValue[lineIndex - 1] = (int) Float.parseFloat(val[fieldOfInterest]) * 100000;
                        } else {
                            fieldValue[lineIndex] = (int) Float.parseFloat(val[fieldOfInterest]) * 100000;
                        }
                    } catch (NumberFormatException nfe) {
                        System.err.println("File " + aFile.getName() + " float parsing failed: " + val[field] + "lineIndex: " + lineIndex);
                    }
                    lineIndex++;
                }
            }
            in.close();
        } catch (IOException ioe) {
            String msg = "Failed while reading in file: " + aFile.getAbsolutePath() + "\n";
            System.err.println(msg);
            ioe.printStackTrace();
        }

        return fieldValue;
    }


    public void initialVersion(){
        //        String aFile = "maizeHapMapV2_B73RefGenV2_201203028_chr10.hmp.txt";
//        String aFile = "mhmv2_chr1_1000.hmp.txt";
//        String aFile = "mhmv2_chr1_1000.hmp.txt";
//        String aFile = "mhmv2_chr9_1000.hmp.txt";
//        String aFile = "mhmv2_chr9_2E6.hmp.txt";
        String aFile = "mhmv2_chr9_2E6.hmp.txt";
        String fullFile = "maizeHapMapV2_B73RefGenV2_201203028_chr9.hmp.txt";
        String path = "\\\\andersonii\\group\\E\\SolexaAnal\\hapmapV2\\hp1\\standardQ87Q87Union\\HapMapV2RefGenV2\\";
        String hapMapFile_prefix = "maizeHapMapV2_B73RefGenV2_201203028_";
        String hapMapFile_suffix = ".hmp.txt";

        String filePath = path + fullFile;

        GWASAnnotation ga = new GWASAnnotation();
//        BitNucleotideAlignment bna = ga.readFile(fullFile);    // testing version
        BitNucleotideAlignment bna = ga.readFile(filePath);   // full chromosome
        int[] alignmentPhysPos = bna.getPhysicalPositions();

//        // Write the integer matrix.
//        IHDF5Writer writer = HDF5Factory.open("chr9_imatrix.h5");
////        writer.createIntMatrix("sites", alignmentPhysPos.length, 1);
//        writer.createIntArray("sites", alignmentPhysPos.length);
//        writer.writeIntArray("sites", alignmentPhysPos);
//        writer.close();

        // read in the gwas results


//        String pathGwas = "C:\\Documents and Settings\\dkroon\\IdeaProjects\\GWAS\\src\\supporting\\gwas_results";
//        String pathGwas = "C:\\Documents and Settings\\dkroon\\IdeaProjects\\GWAS\\src\\supporting\\gwas_sorted\\chr09";
//        String pathGwas = "C:\\Users\\dkroon\\Documents\\TASSEL\\code\\GWAS\\src\\supporting\\gwas_sorted\\chr09";
        String pathGwas = "C:\\Users\\dkroon\\Documents\\TASSEL\\code\\GWAS\\src\\supporting\\gwas_results";
        File aDir = new File(pathGwas);
        ga.loadGWASResults(aDir, alignmentPhysPos);



//        ga.writeToHDF5(bna, "myhdf5");

        //  FileReader fr = null;
//        File[] directoryFile = filePath.listFiles();
//        if (directoryFile.length < 1) {
//            String output = "Directory contains no files.";
////            JOptionPane.showMessageDialog(c, "Directory contains no files: \n" + directory.getPath());
//            return;
//        }


//        for (int i = 0; i < directoryFile.length; i++) {
//            boolean fileAccessed = false;
//            if (!filePath.isDirectory()) {  // do not recurse into sub-directories
//                try {
//                    fr = new FileReader(filePath);
//                    fileAccessed = true;
//                } catch (FileNotFoundException e1) {
//                    e1.printStackTrace();
//                }
//            }
    }


    public void init(){

//        String hapMapPath = "\\\\andersonii\\group\\E\\SolexaAnal\\hapmapV2\\hp1\\standardQ87Q87Union\\HapMapV2RefGenV2\\";
        String hapMapPath = "/local/workdir/dek29/20130323_workstation/HapMapV2RefGenV2/";
        File aHapMapDir = new File(hapMapPath);
        String pathGwas = "/local/workdir/dek29/20130323_workstation/gwas_results/";
        File aGwasDir = new File(pathGwas);

        GWASAnnotation ga = new GWASAnnotation();

        ga.loadAllChromosome(aGwasDir, aHapMapDir);
    }

    public static void main(String[] args) {
        GWASAnnotation g = new GWASAnnotation();
        g.init();
    }
}
