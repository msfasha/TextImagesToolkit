A Java toolkit that converts text words into images files.

The generated dataset contains binary images for the words in addition to labels file.
This dataset can be used for developing OCR and machine learning applications.
The Toolkit can be used to generate dataset for any language, it also include settings that are adaptive to Arabic language.

This toolkit was used to generated Arabic Fonts Dataset used in the research below:

https://arxiv.org/abs/2009.01987

The dataset was used in the Deep Learning model presented at:

https://github.com/msfasha/Arabic-Deep-Learning-OCR.


Nevertheless, the Toolkit can be used to prepare other datasets.

Initially, the toolkit accepts a text file containing Arabic words (base file) as an input and generates images and labels files from that base file.

Two types of datasets can be generated:

1- A dataset with a single binary file that contains the generated images, in addition to a text file that contains the labels as will as other related information about the related image.

This arrangement was found more effecient to easily transfer to the cloud and to enable efficient loading and processing.

2- A dataset that has a seperate image file (.png) for each word, along with a single labels files that refers to the generated images.


More information and samples of the generated dataset (binary format) can be found at:

https://github.com/msfasha/Arabic-Multi-Fonts-Dataset

and 

https://drive.google.com/drive/folders/1mRefmN4Yzy60Uh7z3B6cllyyOXaxQrgg?usp=sharing

Usage:

The Toolkit loops over the words in the base text file and converts each word into a (png) image according to the defined settings.

While looping the words, the Toolkit also loops over the fonts that are included in the (fonts) folder and generate an image for each font type included in the fonts folder.

The initial Arabic words file was generated using Wikipedia dump. Any text file with Arabic words can be used to generated the required images dataset. Wikipedia was used to provide large numbers of words suitable for the OCR Deep Learning model.

Any file with Arabic words can be used as a starter repository, this file can be further refined using (GenerateBaseFileFromWikiSource.java ) file.

GenerateBaseFileFromWikiSource utility allows the user to process raw Arabic words and do the following:
- remove extended Arabic characters.
- remove duplicate words.
- select words with certain length (min, max characters).
- remove numbers and latin characters.
- limit the number of total selected words in the result file.
- augment the generated word with elagonation i.e. adding "ــ", tatweel, to certain characters, or adding spaces between certain characters to extend the generated words diversity.  This was useful to generate different types of words to be used by the OCR model.

The settings for these alternatives are hard coded and can be changed in the (GenerateBaseFileFromWikiSource.java) file.

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



