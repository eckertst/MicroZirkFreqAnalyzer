/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FreqAnalyzer;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;

/**
 *
 * @author eckerts
 */
public class SampleDisplayLabel extends JLabel {
    
    
    //Fields
    
    /**
     * The data sample
     */
    private Sample sample; 
    
    /**
     * Holds a copy of the original sample. Used, when samples are modified e.g. 
     * with equalizers
     */
    private Sample originalSample;
       
    /**
     * The zoom factor. 2 means double size. 
     */
    private double zoomFactor = 1;
    
    /**
     * The point of time or freqency where the zoom area begins. Has to be changed when zooming
     */
    private double zoomStartXUnit = 0;
    
    
    
    //Constructors   
    
    /**
     * Generates a SampleDisplayPanel with the given zoom factor. The data 
     * is shown from the given index.
     * @param sample the data Sample
     * @param zoomFactor the zoomFactor
     * @param xUnitOffset the point of time or frequency where data displaying begins. 
     * Determins the area where the focus of the zoom goes.
     */
    public SampleDisplayLabel (Sample sample, double zoomFactor, double xUnitOffset){
        this.sample = sample;
        this.originalSample = sample;
        this.zoomFactor = zoomFactor;
        this. zoomStartXUnit = xUnitOffset; //alte Version: ...=sample.getIndexForUnit(xUnitOffset);        
    }

    /**
     * Generates a SampleDisplayPanel with zoom factor 1 from the given sample
     * @param sample the data Sample     
     */
    public SampleDisplayLabel (Sample sample){
        this.sample = sample;
        this.originalSample = sample;
        this.zoomFactor = 1;
        this.zoomStartXUnit= 0;   
    }
    
    //Methods
    

    @Override
    public void paintComponent(Graphics graph){
        
        super.paintComponent(graph);
                
        double fromXUnit = getZoomStartXUnit();
        double toXUnit = fromXUnit + getZoomRangeSizeXUnit();
        
        try{
            drawDataFromTo(graph, fromXUnit, toXUnit);
        } catch(Exception ex) {
            System.out.println(ex);
        }
        
        drawXAxisFromTo(graph, fromXUnit, toXUnit);
        
        //Max und Min der Werte eintrage als Orientierung auf der y-Achse
        double max, min;
        if(this.getSample().getData().isEmpty()) {
        } else {
            max = this.getSample().getMaxVal();        
            max = Math.round(100.0 * max) / 100.0; //runden auf 2 Dezimalen
            graph.drawString("max Val.: " + max, 0, 10);
            min = this.getSample().getMinVal();
            min = Math.round(100.0 * min) / 100.0; //runden auf 2 Dezimalen
            graph.drawString("min Val.: " + min, 0, this.getHeight());
        }
    }
    
    /**
     * Draws the Data for the given data intervall to completely
     * fill the SampleDisplayLabel.
     * It is called from paintComponent to manage zooming and should never be called explicitly.
     * @param graph the Graphics used by panintComponent
     * @param fromXUnit the point of Time or frequency to start drawing
     * @param toXUnit the point of Time or frequency to end drawing
     */
    private void drawDataFromTo (Graphics graph, double fromXUnit, double toXUnit) throws Exception {
        if(toXUnit > fromXUnit){
            int lastYPos = 0;
            boolean hasLastYPos = false;
            int yPos;
            try {
                lastYPos = getYPosForXPos(0);
                hasLastYPos = true;
            } catch (Exception ex) {
                
            }
            for (int xPos = 1; xPos < this.getWidth(); ++xPos) {
                try {
                    yPos = getYPosForXPos(xPos);
                    if(this.getIndexForXPos(xPos)>this.getSample().getMarkedFromIndex()
                            && this.getIndexForXPos(xPos)<this.getSample().getMarkedToIndex())
                        graph.setColor(Color.gray);
                    else graph.setColor(Color.red);
                    if(hasLastYPos) graph.drawLine(xPos-1, lastYPos, xPos, yPos);
                    lastYPos = yPos;
                    hasLastYPos = true;
                } catch (Exception ex) {
                    hasLastYPos = false;
                }                
            }
        }
        else throw new Exception("error in draWDataFromTo: fromXUnit > toXUnit");
    }
    
    
    
    /**
     * Draws the x-axis for the given data intervall to completely 
     * fill the SampleDisplayLabel.
     * It is called from paintComponent to manage zooming and should never be called explicitly.
     * @param graph the Graphics used by panintComponent
     * @param fromXUnit the point of Time or frequency to start drawing
     * @param toXUnit the point of Time or frequency to end drawing
     */
    private void drawXAxisFromTo(Graphics graph, double fromXUnit, double toXUnit) {
        graph.setColor(Color.BLUE);
        //draw axis
        int axLevel = getXAxisLevel();
        graph.drawLine(0, axLevel, getWidth(), axLevel);
        //axis labelling
        String axisLabelling = "";
        if (getSample().getDisplayMode() == Sample.DISPLAY_MODE_STANDARD)
             axisLabelling = getSample().getXAxisLabelling() + getSample().getXUnit();
        else if (getSample().getDisplayMode() == Sample.DISPLAY_MODE_RECIPROCAL)
             axisLabelling = getSample().getxAxisLabellingReciprocal() + getSample().getxUnitReciprocal();
        graph.drawString(axisLabelling, getWidth()-100, getHeight()-30);
        //tics
        double ticSpace = getTicSpace();
        double firstTicXUnit;
        if(fromXUnit<0) firstTicXUnit = (int)(fromXUnit/ticSpace) * ticSpace;
        else firstTicXUnit = ((int)(fromXUnit/ticSpace) + 1) * ticSpace;      
        double xUnit = firstTicXUnit;
        while (xUnit < toXUnit) {
            int xPos = getXPosForUnit(xUnit);
            graph.drawLine(xPos, axLevel-10, xPos, getHeight()-30);
            String xUnitString = "";
            if (getSample().getDisplayMode() == Sample.DISPLAY_MODE_STANDARD)
                xUnitString = new Double(round(xUnit)).toString();
            else if (getSample().getDisplayMode() == Sample.DISPLAY_MODE_RECIPROCAL)
                if (Math.abs(1/xUnit) > 1000) xUnitString = "";
                else xUnitString = new Double(round(1/xUnit)).toString();
            graph.drawString(xUnitString, xPos, getHeight()-10);
            xUnit = xUnit + getTicSpace();
        }
    }

    
    
    /**
     * Zooms according to the given zoom factor (2 means double size) around the 
     * given centre x-position.
     * @param zoomFactorRel the zoom factor
     * @param xPosCentre the x position that stays in the same position in the display while zooming
     */
    public void zoomAround(double zoomFactorRel, int xPosCentre){
        double absoluteZoomFactor = zoomFactorRel*this.getZoomFactor();
        if (absoluteZoomFactor >= 1 ){
            try {                
                int dxLeft = xPosCentre;
                int newDxLeft = (int)Math.round(dxLeft/zoomFactorRel);
                int zoomStartXPos = xPosCentre - newDxLeft;
                setZoomStartXUnit(getXUnitForXPos(zoomStartXPos));
                this.setZoomFactor(absoluteZoomFactor);
                repaint();
            } catch (Exception ex) {
                System.out.println("Error while zooming");
            }
        }
    }
    
    /**
     * Zooms according to the given zoom factor (2 means double size). Data is shown
     * from beginning.
     * @param zoomFactor 
     */
    public void zoom(double zoomFactor){
        setZoomStartXUnit(getXUnitForIndex(0));        
        this.setZoomFactor(zoomFactor);
        repaint();
    }
    
    /**
     * Shifts the Display.
     * @param deltaX The amount of pixels to shift
     */
    public void shift(int deltaX){
        double deltaXUnit = deltaX * xUnitPerPix();
        setZoomStartXUnit(getZoomStartXUnit() - deltaXUnit);
        repaint();
    }
    
    /**
     * Marks the area between the two x-positions
     * @param fromXPos
     * @param toXPos 
     */
    public String mark(int fromXPos, int toXPos, int xPos){
        int startInd = (int)Math.round(getIndexForXPos(fromXPos));
        int endInd = (int)Math.round(getIndexForXPos(toXPos));
        getSample().setMarkedFromIndex(getIndexForXPos(fromXPos));
        getSample().setMarkedToIndex(getIndexForXPos(toXPos));
        repaint();
        int numberOfDataPoints = (int)Math.round(getSample().getMarkedToIndex()- getSample().getMarkedFromIndex());
        String returnString = "marked: " + new Integer(startInd).toString() + " to "
                +  new Integer(endInd).toString() + " # " 
                + new Integer(endInd-startInd).toString();
        return returnString;
    }
    
    /**
     * 
     * @return the markedFromXPos
     */
    public int getMarkedFromXPos(){
        return getXPosForIndex(getSample().getMarkedFromIndex());
    }
    
    /**
     * 
     * @return the markedToXPos
     */
    public int getMarkedToXPos(){
        return getXPosForIndex(getSample().getMarkedToIndex());
    }
    
    /**
     * Gets the point of time or frequency where the zoom area starts.
     * @return 
     */
    public double getZoomStartXUnit(){
        return zoomStartXUnit;
    }   
    
    /**
     * Gets index in data sample where the zoom area starts.
     * @return 
     */
    public double getZoomStartIndex() {
        return getIndexForXUnit(getZoomStartXUnit());
    }
    
    
    /**
     * Gets the Size (mesured in Seconds or 1/seconds) of the zoom range.
     * @return 
     */
    public double getZoomRangeSizeXUnit(){
        return getSample().getSizeInXUnit()/getZoomFactor();
    }
    
    /**
     * Gets the Size (mesured in data sample index) of the zoom range.
     * @return 
     */
    public double getZoomRangeSizeIndex(){
        return getSample().getSize()/getZoomFactor();
    }
    
    /**
     * Gets the end of the zoom range in seconds or 1/seconds.
     * @return 
     */
    public double getZoomEndXUnit(){
        return getZoomStartXUnit() + getZoomRangeSizeXUnit();
    }
   
    public double getZoomEndIndex(){
        return getZoomStartIndex() + getZoomRangeSizeIndex();
    }
    
    /**
     * Gets the y-Level of the x-Axis in Pixels
     * @return the y-Level of the x-Axis in Pixels
     */
    public int getXAxisLevel(){
        return (int)Math.round(getSample().getMaxVal()/yValPerPix());
    }
    
    
    /**
     * Gets how much y value is in one y-Pixel
     * @return 
     */
    public double yValPerPix() {
        return getSample().getValRange()/(this.getHeight());
    }
    
    /**
     * Gets how many y-pixels are in one unit of y value.
     * @return 
     */
    public double yPixPerVal() {
        return 1/yValPerPix();
    }
    
    /**
     * Gets how many seconds or 1/seconds are in one x-pixel
     * @return the units. Output depends on zoom status
     */
    public double xUnitPerPix() {        
        return getZoomRangeSizeXUnit()/this.getWidth();
    }
    
    /**
     * Gets how many pixels are in one second or 1/second.
     * @return the number of pixels. Output depends on zomm status.
     */
    public double xPixPerUnit() {
        return 1/xUnitPerPix();
    }
    
    /**
     * Gets how many seconds or 1/seconds are in one data point.
     * @return 
     */
    public double xUnitPerIndex(){
        return getSample().xUnitPerIndex();
    }
       
    /**
     * Gets how many data points are in one second or 1/second.
     * For the time space that is the sampling rate of the data.
     */
    public double indexPerXUnit(){
        return getSample().indexPerXUnit();
    }
    
    /**
     * Gets the number of data points that are in one x-pixel.
     * @return 
     */
    public double indexPerXPix(){
        return (double)(getZoomRangeSizeIndex()-1) / this.getWidth();
    }
    
    /**
     * Gets the numer of x-pixels that are in one data point.
     * @return 
     */
    public double xPixPerIndex(){
        return 1/indexPerXPix();
    }

    /**
     * @return the zoomFactor
     */
    public double getZoomFactor() {
        return zoomFactor;
    }

    /**
     * Gets the point of time or the frequency belonging to the given x-position
     * @param xPos the x-position
     * @return the time in seconds or frequency in 1/seconds
     */
    public double getXUnitForXPos(double xPos) {
        double zoomStartUnit = getSample().getUnitForIndex(getZoomStartIndex());
        double further = xPos * xUnitPerPix();
        return zoomStartUnit + further;
    }
    
    /**
     * Gets the index in the Sample belonging to the given x Position in the label.
     * The index is given as integer value. If you need a double, use getIndexForXPos().
     * @param xPos the x-position
     * @return the Index as integer value
     */
    public int getRoundedIndexForXPos(int xPos){
        double xUnit = getXUnitForXPos(xPos);
        int index = getSample().getRoundedIndexForUnit(xUnit);
            return index;       
    }

    /**
     * Gets the index in the Sample belonging to the given x Position in the label.
     * The index is given as doube value. If you need an integer, use getRoundedIndexForXPos().
     * @param xPos the x-position
     * @return the Index as integer value
     */
    public double getIndexForXPos(int xPos){
        double xUnit = getXUnitForXPos(xPos);
        double index = getSample().getIndexForUnit(xUnit);
            return index;       
    }
    
    /**
     * Gets the data value for the given x-position.
     * @param xPos the x-position
     * @return the data value
     */
    public double getYValForXPos(int xPos) throws Exception{
        int index = getRoundedIndexForXPos(xPos);
        return getSample().getValueForIndex(index);
    }
    
    /**
     * Gets the point of time or frequency for the given data point
     * @param index Index of the data point
     * @return the point of time in seconds or frequency in 1/seconds
     */
    public double getXUnitForIndex(double index) {
        return getSample().getUnitForIndex(index);
    }
    
    /**
     * Gets the x-Position that belongs to the given frequency or point of time.
     * @param xUnit Value in seconds or 1/seconds 
     * @return the x-Position
     */
    public int getXPosForUnit(double xUnit){        
        return getXPosForIndex(getIndexForXUnit(xUnit));
    }

    /**
     * Gets the x position in the Label for the given data point.
     * @param index oft the data point
     * @return the x-position
     */
    public int getXPosForIndex(double index) {
        return (int)(xPixPerIndex()*(index - getZoomStartIndex()));
    }
    
    /**
     * Gets the index of the data point for the given point of time or frequency.
     * The returned index is a doubel value. If you need an integer, use getRoundetIndexForxUnit()
     * @param xUnit the point of time in seconds or the frequency in 1/seconds
     * @return the index of the data point as doubel value
     */
    public double getIndexForXUnit(double xUnit) {
        return getSample().getIndexForUnit(xUnit);
    }
    
    /**
     * Gets the index of the data point for the given point of time or frequency.
     * The returned index is an value. If you need a double, use getIndexForxUnit()
     * @param xUnit the point of time in seconds or the frequency in 1/seconds
     * @return the index of the data point as integer value
     */
    public int getRoundedIndexForXUnit(double xUnit) {
        return getSample().getRoundedIndexForUnit(xUnit);
    }
    
    /**
     * Gets the data value for the given Index.
     * @param index
     * @return the data Value
     * @throws Exception 
     */
    public double getYValforIndex(int index) throws Exception{
        return getSample().getValueForIndex(index);
    }
    
    /**
     * Gets the data value for the given point of time or frequency.
     * @param xUnit the point of time in seconds or the frequency in 1/seconds
     * @return the data Value
     * @throws Exception 
     */
    public double getYValForxUnit(double xUnit) throws Exception{
        return getSample().getValueForUnit(xUnit);
    }
    
    
    public double yValuePerPix() {
        return getSample().getValRange()/this.getHeight();
                
    }
    
    /**
     * Gets the data value for a given y-Position.
     * @param yPos the y-position
     * @return the data Value
     */
    public double getValForYPos(int yPos){
        return (getXAxisLevel()-yPos) * yValuePerPix();
    }
    
    /**
     * Gets the y-position for a given data value.
     * @param value the data value
     * @return the y-position
     */
    public int getYPosForVal(double value){
        return getXAxisLevel()-(int)Math.round(value / yValuePerPix());
    }
    
    /**
     * Gets the y-postition for the data point at the given x-position.
     * @param xPos the x-position
     * @return the y-position
     * @throws Exception 
     */
    public int getYPosForXPos(int xPos) throws Exception{
        return getYPosForVal(getYValForXPos(xPos));
    }
    
    /**
     * Get the xUnit string;
     * @return 
     */
    public String getXUnit(){
        return getSample().getXUnit();
    }
    
    /**
     * Get the x-axis labelling.
     * @return 
     */
    public String getXAxisLabelling(){
        return getSample().getXAxisLabelling();
    }
    
    
    /**
     * Get the sample.
     * @return the sample
     */
    public Sample getSample(){
        return sample;
    }

    
    /**
     * Test if a given point of time or frequency lies in zoom range.
     * @param xUnit the point of time in seconds or the frequency in 1/seconds
     * @return true if the value lies in the range
     */
    public boolean xUnitIsInZoomRange(double xUnit){
        return xUnit > getZoomStartXUnit() && xUnit < getZoomEndXUnit();
        
    }
    
    /**
     * Get the ticspace of the sample.
     * @return 
     */
    public double getTicSpace(){         
        try {
            double ticSpace;
            double xZoomRange = getZoomRangeSizeXUnit();
            if(xZoomRange <0.02) ticSpace = 0.001;
            else if(xZoomRange <0.03) ticSpace = 0.002;
            else if(xZoomRange <0.1) ticSpace = 0.005;
            else if(xZoomRange < 0.2) ticSpace = 0.01;
            else if(xZoomRange < 0.25) ticSpace = 0.01;
            else if(xZoomRange < 1) ticSpace = 0.05;
            else if(xZoomRange < 2) ticSpace = 0.1;
            else if (xZoomRange < 6) ticSpace = 0.5;
            else if (xZoomRange < 20) ticSpace = 1;
            else if (xZoomRange < 60) ticSpace = 5;
            else if (xZoomRange < 200) ticSpace = 10;
            else if (xZoomRange < 600) ticSpace = 50;
            else ticSpace = 100;    
            return ticSpace;
        } catch (Exception ex) {
            return 5;
        }
    }
    
    /**
     * Rounds a double number to the roundig level given in the Globals Class.
     * @param number
     * @return 
     */
    private double round(double number){        
        double stufenzahl = Math.pow(10, Globals.roundingLevel);
        return (double)Math.round(number*stufenzahl)/stufenzahl;
    }

    /**
     * @param sample the sample to set
     */
    public void setSample(Sample sample) {
        this.sample = sample;
    }
    

    /**
     * @param zoomFactor the zoomFactor to set
     */
    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    /**
     * @param zoomStartXUnit the zoomStartXUnit to set
     */
    public void setZoomStartXUnit(double zoomStartXUnit) {
        this.zoomStartXUnit = zoomStartXUnit;
    }
    
}
