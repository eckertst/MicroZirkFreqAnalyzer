/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FreqAnalyzer;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author eckerts
 */
public abstract class Sample {
    
    //Fields
    
    /**
     * Contains the Data.
     */
    protected ArrayList<Double> data;
    
    /**
     * Contains the original data which is kept untouched when the sample gets
     * equalized.
     */
    protected ArrayList<Double> originalData;
    
    /**
     * The maximal Value of the data list.
     */
    protected double maxVal = Long.MIN_VALUE;
    
    /**
     * The minimal Value of the data list.
     */
    protected double minVal = Long.MAX_VALUE;
    
    /**
     * The data sample rate (values per second).
     */
    protected double samplingRate;
    
    /**
     * The data value range (difference between maxVal and minVal).
     */
    protected double valRange;
    
    /**
     * The unit for the x-Axis (will be specified by derived classes).
     */
    protected String xUnit = "";
    
    /**
     * The reciprocal Unit for the x-axis (will be specified be derived classes).     
     */
    protected String xUnitReciprocal = "";
    
    /**
     * The labelling for the x-Axis (will be specified by derived classes).
     */
    protected String xAxisLabelling = "";
    
    /**
     * The reciprocal labelling for the x-Axis (will be specified by derived classes).
     */
    protected String xAxisLabellingReciprocal = "";
    
   
    /**
     * The display Mod for the sample. Available are DISPLAY_MODE_STANDARD and     * 
     * DISPLAY_MODE_RECIPROCAL to display the data of a FreqSample in periods instead
     * of frequency.
     */
    protected int displayMode = DISPLAY_MODE_STANDARD;
   
    /**
     * Constant to characterize standard display mode.
     */
    public static final int DISPLAY_MODE_STANDARD = 1;
    
    /**
     * Constant to characterize standard display mode.
     */
    public static final int DISPLAY_MODE_RECIPROCAL = 2;
    
    //constructors
    
    /**
     * Creates a Sample object from the given ArrayList
     * @param dataList the data List to contain
     * @param sr the sample rate
     * @param xUnitOffset the point of time or frequency belonging to the first data point.
     * Normally its zero, but subsamples can have a offset.
     * @param adustForTransformer if true the data is adjusted for the use of FFT
     * algorithms: First value is set to 0, Sample is filled up with zero values 
     * until the size is a power of 2.
     */
    Sample(ArrayList<Double> dataList, double sr, boolean adjustForTransformer){
        data = cloneList(dataList);
        originalData = cloneList(dataList);        
        for (int i=0; i<data.size(); ++i){
            double val = data.get(i).doubleValue();
            if (val < minVal) minVal = val;
            else if (val > maxVal) maxVal = val;
        }
        valRange = maxVal-minVal;
        this.samplingRate = sr;  
        if (adjustForTransformer) adjustForTransformer();
    }
    
    /**
     * Creates a Sample object from the given double array.
     * @param dataVector the array with the data
     * @param sr the sample rate
     * @param adustForTransformer if true the data is adjusted for the use of FFT
     * algorithms: First value is set to 0, Sample is filled up with zero values 
     * until the size is a power of 2.
     */
    Sample(double[] dataVector, double sr, boolean adjustForTransformer){
        data = new ArrayList<Double>();
        originalData = new ArrayList<Double>();
        for (int i=0; i<dataVector.length; ++i) {
            data.add(dataVector[i]);
            originalData.add(dataVector[i]);
        }
        for (int i=0; i<data.size(); ++i){
            double val = data.get(i).doubleValue();
            if (val < minVal) minVal = val;
            else if (val > maxVal) maxVal = val;
        }
        valRange = maxVal-minVal;
        this.samplingRate = sr;
        if (adjustForTransformer) adjustForTransformer();
    }
    
    /**
     * The data is adjusted to be transformed by a FFT algorithm. For this 
     * algorithms the size of the data List has to be a power of two and the first 
     * value has to be a zero. This method sets the first value to zero and 
     * adds zero values at the end until the size reaches the next power of two.
     */
    public final void adjustForTransformer(){
        //First Element has to be 0
        if(data.get(0).doubleValue()!=0.0) data.set(0, new Double(0)); 
        //Size has to be a power of 2
        int n = data.size();
        double log2 = Math.log(data.size())/Math.log(2);
        int newSize = (int)Math.pow(2, (int)log2+1);
        for(int i = 0; i<(newSize-n); ++i){
            data.add(new Double(0));
        }
        //The same for the orignial data:        
        if(originalData.get(0).doubleValue()!=0.0) originalData.set(0, new Double(0)); 
        //Size has to be a power of 2        
        for(int i = 0; i<(newSize-n); ++i){
            originalData.add(new Double(0));
        }
        if (minVal > 0) minVal = 0;
        if (maxVal < 0) maxVal = 0;
    }
    
    /**
     * Extracts a subsample between the two given indices (both including).
     * @param i start index
     * @param j stop index
     * @return the subsample
     */
    abstract Sample subSampleFromTo(int i, int j);
    
   
     /**
     * Will be overwritten by derived classes. Gives the index for a point of time 
     * or a frequency as double value. If you need an int use getRoundedIndexForUnit().
     * @param unit
     * @return the index (as double value) witch holds the data for the given point of time
     */
    abstract public double getIndexForUnit(double unit);
    
    /**
     * Will be overwritten by derived classes. Gives the index for a point of time 
     * or a frequency as integer value. If you need a double use getIndexForUnit().
     * @param unit
     * @return the index (as integer value) witch holds the data for the given point of time
     */
    abstract public int getRoundedIndexForUnit(double unit);
    
    /**
     * Will be overwritten by derived classes. Gives the value for a point of time or a frequency.
     * @param unit
     * @return the value for the given point of time
     */
    abstract public double getValueForUnit(double unit) throws Exception;

    /**
     * Will be overwritten by derived classes. Gives the point of time or a frequency for the specified index.
     * @param index
     */
    abstract public double getUnitForIndex(double index);
    
    /**
     * Will be overwritten by derived classes. Gives the value for the specified index.
     * @param index
     */
    abstract public double getValueForIndex(double index) throws Exception;
    
    
    /**
     * Gets the x-range of the data in the Unit oft this sample.
     * @return 
     */
    abstract public double getXRangeInUnit();
    
    /**
     * Gets the the amount of seconds or 1/seconds for one data point.
     * @return 
     */
    abstract public double xUnitPerIndex();
    
    /**
     * Gets how many data points are in one second or 1/second.
     * @return 
     */
    abstract public double indexPerXUnit();

    /**
     * @return the maxVal
     */
    public double getMaxVal() {
        return maxVal;
    }

    /**
     * @return the minVal
     */
    public double getMinVal() {
        return minVal;
    }

    /**
     * @return the sampleRate
     */
    public double getSamplingRate() {
        return samplingRate;
    }

    /**
     * @return the valRange
     */
    public double getValRange() {
        return valRange;
    }

    /**
     * @return the xUnit
     */
    public String getXUnit() {
        return xUnit;
    }
    
    /**
     * 
     * @return 
     */
    public String getXAxisLabelling(){
        return xAxisLabelling;
    }
    
    /**
     * Creates a String with the data (can be very long).
     * @return the data string
     */
    public String getContentString(){
        String result = "";
        for (int i = 0; i < data.size(); ++i){
            result = result.concat(data.get(i).toString());
            result = result.concat("  |  ");
        }
        return result;
    }
    
    /**
     * Gets the size of the data list.
     * @return the size
     */
    public int getSize(){
        return data.size();
    }
    
    /**
     * Gets the size of the sample in seconds or 1/seconds
     * @return 
     */
    public double getSizeInXUnit(){
        return data.size()*xUnitPerIndex();
    }
    
    /**
     * Gets the data at the position of the given index.
     * @param i the index
     * @return the data in Double format
     */
    public double get(int i){
        return data.get(i);
    }
    
    /**
     * Gets the original data at the position of the given index.
     * @param i
     * @return 
     */
    public double getOriginal(int i){
        return originalData.get(i);
    }
    
    /**
     * Set the data value at the given index
     * @param i the index
     * @param value the data value
     */
    public void set(int i, Double value){
        data.set(i, value);
    }
    
     /**
     * Calculates the average value of the data values.
     * @return the average value
     */
    public double getAverage(){
        double av = 0;
        for (int i=0; i< this.getSize(); i++) av = av + this.get(i);
        av = av/this.getSize();
        return av;
    }   
    
    /**
     * Gets the data ArrayList
     * @return 
     */
    public ArrayList<Double> getData(){
        return data;
    }
    
    /**
     * Gets the original data ArrayList
     * @return 
     */
    public ArrayList<Double> getOriginalData(){
        return originalData;
    }
    
    /**
     * Gets a double array of the data
     * @return 
     */
    public double[] getDataArray(){
        double[] dataArray = new double[data.size()];
        for (int i = 0; i < data.size(); ++i)
            dataArray[i] = data.get(i);
        return dataArray;
    }

    /**
     * Gets a double array of the original data
     * @return 
     */
    public double[] getOriginalDataArray(){
        double[] dataArray = new double[originalData.size()];
        for (int i = 0; i < originalData.size(); ++i)
            dataArray[i] = originalData.get(i);
        return dataArray;
    }
    
    /**
     * @return the displayMode
     */
    public int getDisplayMode() {
        return displayMode;
    }

    /**
     * @param displayMode the displayMode to set
     */
    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    /**
     * @return the xUnitReciprocal
     */
    public String getxUnitReciprocal() {
        return xUnitReciprocal;
    }

    /**
     * @return the xAxisLabellingReciprocal
     */
    public String getxAxisLabellingReciprocal() {
        return xAxisLabellingReciprocal;
    }
    
    /**
     * lower bound index of marked data.
     */
    private double markedFromIndex = -1;
    
    /**
     * upper bound index of marked data.
     * 
     */
    private double markedToIndex = -1;

    /**
     * @return the markedFromIndex
     */
    public double getMarkedFromIndex() {
        return markedFromIndex;
    }

    /**
     * @param markedFromIndex the markedFromIndex to set
     */
    public void setMarkedFromIndex(double markedFromIndex) {
        this.markedFromIndex = markedFromIndex;
    }

    /**
     * @return the markedToIndex
     */
    public double getMarkedToIndex() {
        return markedToIndex;
    }

    /**
     * @param markedToIndex the markedToIndex to set
     */
    public void setMarkedToIndex(double markedToIndex) {
        this.markedToIndex = markedToIndex;
    }
    
    /**
     * Set the data of the Sample. Atention: The originalData field is also replaced!
     * @param dataList 
     */
    public void setData(ArrayList<Double> dataList){
        this.data = dataList;
        this.originalData = cloneList(dataList);
    }
    
    /**
     * Set the data of the Sample. Atention: The originalData field is also replaced!
     * @param dataArray 
     */
    public void setData(double[] dataArray){
        ArrayList<Double> list = new ArrayList<>();
        for (int i=0; i<dataArray.length; ++i)
            list.add(new Double(dataArray[i]));
        this.data = list;
        this.originalData = cloneList(list);
    }
    
    /**
     * The given ArrayList is cloned.
     * @param list
     * @return 
     */
    public ArrayList cloneList(ArrayList list){
        ArrayList returnList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) returnList.add(it.next());
        return returnList;
    }
    
    /**
     * Set the original Data List. Normally the original Data of a Sample is set
     * while creating the Object. Manipulation can cause plenty of problems.
     * This method was written to restore the orignial Data, when a Sample is
     * manipulated by an Equalizer.
     * @param origDataList 
     */
    public void setOriginalData(ArrayList<Double> origDataList){
        this.originalData = origDataList;
    }
    
    /**
     * print the first n data values on System.out
     * @param n 
     */
    public void printData(int n){
        String out = "";
        for (int i = 0; i < n; ++i) 
            if (i < data.size()) out = out.concat(data.get(i).toString() + "; ");
        System.out.println(out);
    }
    
     /**
     * print the first n original data values on System.out
     * @param n 
     */
    public void printOrignialData(int n){
        String out = "";
        for (int i = 0; i < n; ++i) 
            if (i < originalData.size()) out = out.concat(originalData.get(i).toString() + "; ");
        System.out.println(out);
    }
    
    /**
     * Reset the sample data to the original data. Use to undo equalizer work.
     */
    public void resetToOriginalData(){
        for (int i = 0; i < data.size(); ++i){
            data.set(i, originalData.get(i));
        }
    }
}
