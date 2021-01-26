# MicroZirkFreqAnalyzer
Frequency analyzer for LDF Data of bloodflow velocity in skin micro vessels

MicroZirkFreqAnalyzer is a Java desktop application written by me (Stefan Eckert) to provide a simple-to-use tool for frequency analysis of blood flow velocity data collected by laser doppler flowmetry (LDF). It uses parts of the package commons-math3-3.6.1.
It is provided here "as is" and without any sort of warrenty concerning correctness or usability or anything else. It is published under [Apache license v2.0](https://github.com/eckertst/MicroZirkFreqAnalyzer/blob/main/LICENSE). You are allowed and invited to use, change or expend it according to this license.

## How to use the App

### Install and start

1. Get the MicroZirkFreqAnalyzer_1_4.zip file frome here: https://github.com/eckertst/MicroZirkFreqAnalyzer/tree/main and unzip it in any Folder ("your folder") on your computer. 
2. Go to your folder and run the jar-file MikroZirkFreqAnalyzer1.1.jar there (It's still named 1.1 because my versioning is not very professional...) In Windows a double click should do or rightclick and open with java. If this doesn't work try the command line with `java -jar PathToYourFolder\MikroZirkFreqAnalyzer1.1.jar`

### Load Data
The data has to be in a textfile (name.txt oder name.asc), just one number per line. Decimal seperator has to be a point, no comma. Perhaps there are some single lines with rubbish in your data. That's no problem: The app will replace such errors by the value in the line before. With a high sampling rate this doesn't corrupt the data too much.

If possible, the number of values should be a little bit smaller (about 10) than a power of two. The Fast Fourier Transform algorithm which is used for frequency analysis works only with exactly a power of two values. Therefore the program fills up with zeroes up to the next power of two, if the number doesn't match. So, if you stay slightly below one of these numbers (1024, 2048, 4096, 8192, 16384, 32768 or 65536) you avoid thousands of additional zeroes that could disturb the analysis.
        
### Adjustments

1. Replace Outliers
Outliers can be recognized automatically or manually. They are replaced by the value before. 
* automatically: the smaller you choose the tolerance value (default 3) the stricter the filter works. If a data graph looks "cut off", just take a higher tolerance to get the maxima into your image. If the data graph consists mainly of vertical lines you have to choose a smaller value because the outliers (vertical lines) give the picture a false scaling.
* manually: Just give a upper and a lower bound for the values.

2. Sampling Rate
That is the frequency of the recording you have done with some LDF equipment (Values per second). With a false value here the Presentation of the data on the time axis and the frequency analysis will be completely incorrect!

3. Zoom transformed data to ... % of full range
The frequencies in blood flow velocity are low (f < 1,5 1/s). So here you can choose how much of the full frequency range shall be shown after transformation. Of course you can zoom in and out later. 

4. Remove Bias from Data
Bias here means the shift of the oscillation away from the x-axis. So removing the bias gives you a graph oscillating around the x-axis as assumed for the FFT-algorithm.
If you just want to see the origial data, don't remove the bias. But if you want to do a frequency analysis, you should remove it to make the FFT-algorithm  work correctly.

### Zoom/Shift and mark

In the area of the window, where the original data is shown, you can change between the two modes zoom/shift and mark. Mark is the default.
You can save the marked area as a new text file (save as, under the graph). The transform is always done on the complete data sample, not only on the marked area.
In zoom/shift mode you can zoom with the mouse wheel and shift by left-drag. The frequency area is always in zoom/shift mode.

### Do the analysis

In the menu choose _Transformation_ and click on _fast sine transformation_. There are different types of FFT-algorithms. Fast sine is one of them and the only one implemented here.

### Test the app

We have also put some data samples from our LDF-Recordings into the zip. You may use them to see how the program works.

If you want to test the correctness of the frequency analysis, just simulate some data with excel. There you can use the sine function to generate a data sample with several superimposed oscillations with known frequencies and amplitudes. Then export that sample as txt-file and analyze it with the app. This should work very well. But the peaks of the frequency graph are very sharp, so you have to zoom in a lot, to find them.

## How to get the source code

If you want to take a look at the source code or clone it to change the application for your own purposes go here: [https://github.com/eckertst/MicroZirkFreqAnalyzer/tree/master](https://github.com/eckertst/MicroZirkFreqAnalyzer/tree/master) Make sure to get the "master" branch not the "main".
You'll find a javadoc.zip there which was automatically generated. It was not maintained consequently during programming but it might be of some help nevertheless. Unzip it and open the index.html with your browser.
