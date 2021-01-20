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
public class TimeSample extends Sample {
    
    
    /**
     * Creates a new TimeSample object from the given ArrayList of data.
     * @param dataList the dataList
     * @param sr the sampling rate
     * @param adjustForTransformer if true the data is adjusted for the use of FFT
     * algorithms: First value is set to 0, Sample is filled up with zero values 
     * until the size is a power of 2.
     */
    public TimeSample(ArrayList<Double> dataList, double sr, boolean adjustForTransformer) {
        super(dataList, sr, adjustForTransformer);
        xUnit = " s";
        xAxisLabelling = "time in";
    }
    
    /**
     * Creates a new TimeSample object from the given double array of data.
     * @param dataArray the dataList
     * @param sr the sampling rate
     * @param adjustForTransformer if true the data is adjusted for the use of FFT
     * algorithms: First value is set to 0, Sample is filled up with zero values 
     * until the size is a power of 2.
     */
    public TimeSample(double[] dataArray, double sr, boolean adjustForTransformer) {
        super(dataArray, sr, adjustForTransformer);
        xUnit = " s";
        xAxisLabelling = "time in";
    }

    @Override
    public TimeSample subSampleFromTo(int i, int j) {
        int fromIndex = Math.max(i, 0);
        int toIndex = Math.min(j+1, data.size()-1);        
        ArrayList<Double> subList = new ArrayList(data.subList(fromIndex, toIndex));
        return new TimeSample(subList, samplingRate, false);
    }

    /**
     * Gets the index witch holds the data for the given point of time as double value.
     * If you need an integer, use getRoundetIndexForUnit().
     * @param sec
     * @return the index (as double value) witch holds the data for the given point of time    
     */
    @Override
    public double getIndexForUnit(double sec){
        return sec*getSamplingRate();         
    }
    
    /**
     * Gets the index witch holds the data for the given point of time as integer value.
     * If you need an integer, use getRoundetIndexForUnit().
     * @param sec
     * @return the index (as integer value) witch holds the data for the given point of time
     */
    @Override
    public int getRoundedIndexForUnit(double sec) {
        return (int)Math.round(getIndexForUnit(sec));
        
    }

    /**
     * Gets the data value for the given point of time.
     * @param sec
     * @return the value for the given point of time
     * @throws Exception if there is no value available
     */
    @Override
    public double getValueForUnit(double sec) throws Exception { 
        try {
            return data.get(getRoundedIndexForUnit(sec));
        } catch (Exception ex){
            throw new Exception ("getValueForUnit: No Value available");
        }
        
    }
    
    /**
     * Gets the point of time for the given index.
     * @param index
     * @return the point of time for the given index
     */
    @Override
    public double getUnitForIndex(double index) {
        return index/getSamplingRate();        
    }

    
    /**
     * Gets the value for the given index.
     * @param index
     * @return the value for the given index
     * @throws java.lang.Exception if if there is no value available
     */
    @Override
    public double getValueForIndex(double index) throws Exception {
       if(index < data.size() && index >= 0) return data.get((int)Math.round(index));
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
     * Gets the the amount of seconds for one data point.
     * @return 
     */     
    @Override
    public double xUnitPerIndex() {
        return 1/samplingRate;
    }

    /**
     * Gets how many data points are in one second. That is the sampling rate.
     */
    @Override
    public double indexPerXUnit() {
        return samplingRate;
    }

    public TimeSample getClone(){
        return new TimeSample(getData(), samplingRate, false);
    }
    
    
}
