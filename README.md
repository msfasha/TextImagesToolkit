A Java toolkit to generate multi fonts text images for any language including Arabic.

This toolkit was used to generated Arabic Fonts Dataset used in the research below:

https://arxiv.org/abs/2009.01987

The dataset was used in the Deep Learning model presented at:

https://github.com/msfasha/Arabic-Deep-Learning-OCR

The toolkit receives a text file containing Arabic words as an input and generates images and labels dataset.
Two types of datasets can be generated:
1- A dataset with a single binary file that contains to the generated images in addition to a text file that contains the labels as will as the memory location for each label in the binary file.
This arrangement was more effecient to easily transfer and iterate the dataset e.g. to google cloud or other remote locations.

2- A dataset that contains seperate image file (.png) for each word, along with a single labels files that refers to all the generated images.

Samples of the generated dataset (binary format) can be found at:
https://drive.google.com/drive/folders/1mRefmN4Yzy60Uh7z3B6cllyyOXaxQrgg?usp=sharing

Usage:
Basically, the Toolkit loops over the words in the base text file and converts each word into a (png) image according to the defined settings.
While looping the words, the Toolkit also loops over the fonts that are included in the (fonts) folder and generate an image for each type of font included in the fonts folder.

The initial Arabic words file was generated using Wikipedia dump. Any text file with Arabic words can be used to generated the required images dataset. Wikipedia was used to provide large numbers of words suitable for the OCR Deep Learning model.

Any file with Arabic words can be used as a starter repository, this file can be further refined using (GenerateBaseFileFromWikiSource.java ) file.
This file (GenerateBaseFileFromWikiSource.java) allows the user to process raw Arabic words to:
- remove extended Arabic characters.
- remove duplicate words.
- select words with certain length (min, max characters).
- remove numbers and latin characters.
- limit the number of total selected words in the result file.
- augment the generated word with elagonation i.e. adding "ــ", tatweel, to certain characters, or adding spaces between certain characters to extend the generated words diversity.  This was useful to generate different types of words to be used by the OCR model.

The settings for these alternatives are hard coded and can be changed in the (GenerateBaseFileFromWikiSource.java) file.
