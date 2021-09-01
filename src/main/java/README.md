A Java toolkit to generate multi fonts text images for any language including Arabic.

To generate new dataset:
Make sure that you have the base file, use either wiki source or Auto segments to generate base_words.txt.
Set the required number of samples in that file before running the generate code.
Set the minimum and maximum word size e.g. 7-10.
The the number of words in each sample e.g. 1.
Make sure you have the required fonts in the fonts directory.

Set the type of save, PNG, Binary or both.
Set the image sizing method.
Set the monochrome if needed.
Knowing that augmentation and Kashida are only available in ResizeToFitFixedBoundary mode.
Check font styles and size if required.


