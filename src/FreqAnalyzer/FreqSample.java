/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FreqAnalyzer;

import java.util.ArrayList;

/**
 *
 * @author eckerts
 */
public class FreqSample extends Sample {
    
    
    
    /**
     * Creates a new FreqSample object from the given ArrayList of data.
     * @param dataList the transformed dataList
     * @param sr the sampling rate
     * @param displayMode Use Sample.DISPLAY_MODE_STANDARD to display the data in frequency (1/s)
     * or Sample.DISPLAY_MODE_RECIPROCAL to display in period time (s)
     * @param adjustForTransformer if true the data is adjusted for the use of FFT
     * algorithms: First value is set to 0, Sample is filled up with zero values 
     * until the size is a power of 2. 
     */
    public FreqSample(ArrayList<Double> dataList, double sr, int displayMode, boolean adjustForTransformer){
        super(dataList, sr, adjustForTransformer);
        xUnit = " 1/s";
        xUnitReciprocal = " s";
        xAxisLabelling = "frequency in";
        xAxisLabellingReciprocal = " period in";
        this.displayMode = displayMode;
    }

    /**
     * Creates a new FreqSample object from the given double array of data.
     * @param dataArray the double array with the data
     * @param sr the sampling rate
     * @param displayMode Use Sample.DISPLAY_MODE_STANDARD to display the data in frequency (1/s)
     * or Sample.DISPLAY_MODE_RECIPROCAL to display in period time (s)
     * @param adjustForTransformer if true the data is adjusted for the use of FFT
     * algorithms: First value is set to 0, Sample is filled up with zero values 
     * until the size is a power of 2.
     */
    public FreqSample(double[] dataArray, double sr, int displayMode, boolean adjustForTransformer){
        super(dataArray, sr, adjustForTransformer);
        xUnit = " 1/s";
        xUnitReciprocal = " s";
        xAxisLabelling = "frequency in";
        xAxisLabellingReciprocal = " period in";
        this.displayMode = displayMode;
    }
    
    @Override
    public FreqSample subSampleFromTo(int i, int j) {
        int fromIndex = Math.max(i, 0);
        int toIndex = Math.min(j+1, data.size()-1);        
        ArrayList<Double> subList = new ArrayList(data.subList(fromIndex, toIndex));
        return new FreqSample(subList, samplingRate, displayMode, false);
    }
    
    
    /**
     * Gets the index witch holds the data for the given frequency as double value.
     * If you need an integer, use getRoundetIndexForUnit().
     * @param freq the frequency
     * @return the index (as double value) witch holds the data for the given frequency    
     */
    @Override
    public double getIndexForUnit(double freq){
        return freq * indexPerXUnit();
    }
   
    /**
     * Gets the index witch holds the data for the given point of time as integer value.
     * If you need an integer, use getRoundetIndexForUnit().
     * @param sec the point of time
     * @return the index (as integer value) witch holds the data for the given point of time
     */
    @Override
    public int getRoundedIndexForUnit(double sec) {
        return (int)Math.round(getIndexForUnit(sec));
        
    }
    
    /**
     * Gets the data value for the given frequency.
     * @param freq the frequency
     * @return the value for the given point of time
     * @throws Exception if there is no value available
     */
    @Override
    public double getValueForUnit(double freq) throws Exception { 
        try {
            return data.get(getRoundedIndexForUnit(freq));
        } catch (Exception ex){
            throw new Exception ("getValueForUnit: No Value available");
        }        
    }
    
    /**
     * Gets the frequency for the given index.
     * @param index the index
     * @return the frequency for the given index
     */
    @Override
    public double getUnitForIndex(double index) {
        return index * xUnitPerIndex();
    }

    /**
     * Gets the value for the given index.
     * @param index
     * @return the value for the given index
     * @throws java.lang.Exception if if there is no value available
     */
    @Override
    public double getValueForIndex(double index) throws Exception {
       if(index < data.size() && index >=0) return data.get((int)Math.round(index));
       else throw new Exception("getValueForIndex: no value available");
    }
    
    /**
     * Gets the x-range of the timeSample in seconds.
     * @return the x-range in seconds or -1 if an error occurs.
     */
    @Override
    public double getXRangeInUnit() {        
        return this.getUnitForIndex(this.getSize()-1)-this.getUnitForIndex(0);        
    }
    
    
    /**
     * Gets the the amount of 1/seconds for one data point.
     * @return 
     */     
    @Override
    public double xUnitPerIndex() {
        return samplingRate / (2*getSize());
    }
    
    
    /**
     * Gets how many data points are in one 1/second.
     */
    @Override
    public double indexPerXUnit() {
        return 2* getSize() / samplingRate;
    } 

    public FreqSample getClone(){
        return new FreqSample(getData(), samplingRate, displayMode, false);
    }
    
}
