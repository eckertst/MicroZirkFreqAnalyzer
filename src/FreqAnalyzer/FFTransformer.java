package FreqAnalyzer;


import java.util.ArrayList;
import org.apache.commons.math3.transform.DstNormalization;
import org.apache.commons.math3.transform.FastSineTransformer;
import org.apache.commons.math3.transform.TransformType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Container Class for the method(s) to do the fast fourier transformation.
 * So far the fastSineTransformer is used, because there is only real data to transform.
 * @author eckerts
 */
public class FFTransformer {
    
    /**
     * Transforms a time-space-ArrayList into a frequency-space-Array. A fast 
     * sine transform algorithm is used.
     * @param dataList the data to transform
     * @return the transformed data
     */
    public static ArrayList<Double> transform(ArrayList<Double> dataList){
        int n = dataList.size();
        double[] sampleVector = new double[n];
        for (int i=0; i<n; ++i) sampleVector[i]=dataList.get(i);
        FastSineTransformer transformer = new FastSineTransformer(DstNormalization.STANDARD_DST_I);        
        double[] transformedSampleVector;
        transformedSampleVector = transformer.transform(sampleVector, TransformType.FORWARD);
        ArrayList<Double> transformedSample = new ArrayList<>();
        for (int i=0; i<n; ++i) transformedSample.add(transformedSampleVector[i]);
        return transformedSample;
    }
    
    /**
     * Transforms a time sample into a frequency sample.
     * @param timeSample the sample to transform
     * @return the transformed data as fequency sample
     */
    public static FreqSample transform(TimeSample timeSample){
        return  new FreqSample(transform(timeSample.getData()), 
                timeSample.getSamplingRate(), Sample.DISPLAY_MODE_STANDARD, false);
    }
    
    /**
     * Transforms a time sample into a frequency sample using the current data. 
     * If you have allready manipulated the sample by an equalizer you will get
     * the Transformation of the manipulated data.
     * @param timeSample the sample to transform
     * @param dispMode the displayMode oft the resulting frequency sample. Choose
     * between Sample.DISPLAY_MODE_STANDARD and Sample.DISPLAY_MODE_RECIPROCAL
     * @return the transformed data as fequency sample
     */
    public static FreqSample transform(TimeSample timeSample, int dispMode){
        return  new FreqSample(transform(timeSample.getData()), 
                timeSample.getSamplingRate(), dispMode, false);
    }
    
    /**
     * Transforms a time sample into a frequency sample using the original data.
     * Use this method if you have allready manipulated the sample by an Equalizer 
     * but you want to get a Transformation of the orignial data.
     * @param timeSample the sample to transform
     * @return the transformed data as fequency sample
     */
    public static FreqSample transformOriginal(TimeSample timeSample){
        timeSample.setData(timeSample.getOriginalData());
        return  new FreqSample(transform(timeSample.getData()), 
                timeSample.getSamplingRate(), Sample.DISPLAY_MODE_STANDARD, false);
    } 
    
    /**
     * Transforms a time sample into a frequency sample using the original data.
     * Use this method if you have allready manipulated the sample by an Equalizer 
     * but you want to get a Transformation of the orignial data.
     * @param timeSample the sample to transform
     * @param dispMode the displayMode oft the resulting frequency sample. Choose
     * between Sample.DISPLAY_MODE_STANDARD and Sample.DISPLAY_MODE_RECIPROCAL
     * @return the transformed data as fequency sample
     */
    public static FreqSample transformOriginal(TimeSample timeSample, int dispMode){
        timeSample.setData(timeSample.getOriginalData());
        return  new FreqSample(transform(timeSample.getData()), 
                timeSample.getSamplingRate(), dispMode, false);
    }
    
    /**
     * Transforms a frequency-space-ArrayList into a time-space-Array. A fast 
     * sine transform algorithm is used.
     * @param dataList the data to transform
     * @return the transformed data
     */
    public static ArrayList<Double> transformInverse(ArrayList<Double> dataList){
        int n = dataList.size();
        double[] sampleVector = new double[n];
        for (int i=0; i<n; ++i) sampleVector[i]=dataList.get(i);
        FastSineTransformer transformer = new FastSineTransformer(DstNormalization.STANDARD_DST_I);        
        double[] transformedSampleVector;
        transformedSampleVector = transformer.transform(sampleVector, TransformType.INVERSE);
        ArrayList<Double> transformedSample = new ArrayList<>();
        for (int i=0; i<n; ++i) transformedSample.add(transformedSampleVector[i]);
        return transformedSample;
    }
    
    /**
     * Transforms a freqency sample into a time sample.
     * @param freqSample the sample to transform
     * @return the transformed data as time sample
     */
    public static TimeSample transformInverse(FreqSample freqSample){
        return  new TimeSample(transformInverse(freqSample.getData()), 
                freqSample.getSamplingRate(), false);
    }
}
