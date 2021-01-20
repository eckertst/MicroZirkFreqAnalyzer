/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FreqAnalyzer;

import java.io.File;

/**
 * Holds the global variables for this project
 * @author eckerts
 */
public final class Globals {
    
    /**
     * If Outiers are further from the average than OUTLIERS_TOLERANCE times 
     * standard deviation they are replaced by the previous value.
     */
    public static double outliersTolerance = 3;
    
    /**
     * Outliers are not replaced.
     */
    public static final int REPLACE_OUTLIERS_OFF = 0;
    
    /**
     * Outliers are replaced automatically according to outliersTolerance.
     */
    public static final int REPLACE_OUTLIERS_AUTO = 1;
    
    /**
     * Outliers are detected by an intervall given by the user.
     */
    public static final int REPLACE_OUTLIERS_MANUALLY = 2;
    
    /**
     * outliers are replaced according to the constant values above.
     */
    public static int replaceOutliers = REPLACE_OUTLIERS_AUTO;
    
    /**
     * Lower Bound of interval where data is accepted. (Only for manual 
     * repalcement of outliers)
     */
    public static double outliersLowerBound = 0;
    
    /**
     * Upper Bound of interval where data is accepted. (Only for manual 
     * repalcement of outliers)
     */
    public static double outliersUpperBound = 100;
    
    
    /**
     * Path of last Data file used.
     */
    public static String pathToDataFile = "";
    
    /**
     * Sampling Rate of the Data, must be given by user when opening a data file.
     */
    public static double samplingRate = 250;
    
    /**
     * Remove bias from data so that the "wave" is pulled down to the x-axis and
     * the fast fourrier algorithm performs better.
     */
    public static boolean removeBias = true;
    
    /**
     * The transformed Sample can be trimmed to smaller size to Zoom into 
     * the area of long Periods. 0.02 means that only the first 2% of the 
     * transformed sample is displayed.
     */
    public static double freqDisplayTrimFactor = 0.05;
    
    /**
     * Relative zoom Factor per notch: 0.1 means 10 % per notch
     */
    public static double zoomPerMousewheelNotch = 0.1;
    
    /**
     * The number of decimals to round to for the x-axis scale and the coordinate indicator.
     */
    public static int roundingLevel = 3;
    
    /**
     * The standard zoom factor for the transformed data. 
     */
    public static double standardZoomFactorTransformed = 20;
    
    
    /**
     * For MikroZirkMixer: Only the DisplayLabel is zoomed where the MouseWheelEvent came from.
     */
    public static final int ZOOM_MARK_MODE_SINGLE_ZOOM = 0;
    
    /**
     * Mode for Time space SampleDisplayPanel: Mouse drag shifts, Mousewheel zooms.
     */
    public static final int ZOOM_MARK_MODE_ZOOM = 1;
    
    /**
     * Mode for Time space SampleDisplayPanel: Mouse drag marks.
     */
    public static final int ZOOM_MARK_MODE_MARK = 2;
    
    /**
     * Mode for MikroZirkMixer: All time-space displays are zoomt/shifted together
     */
    public static final int ZOOM_MARK_MODE_TIME_ZOOM = 3;
    
    /**
     * Mode for MikroZirkMixer: All frequency-space displays are zoomt/shifted together
     */
    public static final int ZOOM_MARK_MODE_FREQ_ZOOM = 4;
    
    /**
     * The zoom factor for shared use. In the App MirkoZirkMixer1.0 this factor 
     * is used only for time display panels. 
     */
    public static double zoomFactor = 1;
    
    /**
     * The zoom factor for the frequency display panels of the App MikroZirkMixer1.0.
     */
    public static double zoomFactorFreq = standardZoomFactorTransformed;
    
    /**
     * Mode for Time space SampleDisplayPanel: Zoom/shift or mark.
     * Us Constants ZOOM_MARK_MODE_ZOOM and ZOOM_MARK_MODE_MARK.
     */
    public static int zoomMarkMode = ZOOM_MARK_MODE_MARK;
    
    /**
     * The title panels of the different display panels in the aplication MikroZirkMixer 
     * have have this width.
     */
    public static int mixerTitelPanelWidth = 140;
    
    /**
     * last File chosen by a FileChooser.
     */
    public static File lastFile = null;
    
    /**
     * Mix data is displayed as simple sum of proband and therapist.
     */
    public static final int ORIG_INTENS_MODE_ORIGINAL = 0;
    
    /**
     * Mix data is displayed as Intensity: That is the square of the sum of
     * proband and therapist. 
     */
    public static final int ORIG_INTENS_MODE_INTENSITY = 1;
    
    /**
     * Display mode for the mix: choose between ORIG_INTENS_MODE_ORIGINAL
     * and ORIG_INTENS_MODE_INTENSITY.
     */
    public static int origIntensMode = ORIG_INTENS_MODE_ORIGINAL;
    
    /**
     * counts how many of the two SampleDisplayLabels for therapist and proband
     * in timespace mode. If value is two joint zooming is allowed.
     */
    public static int numberOfTimeSamplesActive = 0;
    
    /**
     * If true, the values of a sample to load in the mixer are normalized to
     * maximum amplitude 1.
     */
    public static boolean normalization = true;
}
