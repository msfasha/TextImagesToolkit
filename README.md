A Java toolkit to generate multi fonts text images for any language including Arabic.

This toolkit was used to generated Arabic Fonts Dataset used in the research below:

https://arxiv.org/abs/2009.01987

The dataset was used in the model presented at:

https://github.com/msfasha/Arabic-Deep-Learning-OCR

Usage:
The base Arabic words file was generated using Wikipdia dump.

Any file with Arabic words can be used as a starter repository, this file can be further refined using (GenerateBaseFileFromWikiSource.java ) file.
This file (GenerateBaseFileFromWikiSource.java) allows the user to process raw Arabic words to:
- remove extended Arabic characters.
- remove duplicate words.
- select words with certain length (min, max characters).
- remove numbers and latin characters.
- limit the number of total selected words in the result file.
- augment the generated word with elagonation i.e. adding "ــ", tatweel, to certain characters, or adding spaces between certain characters to extend the generated words diversity.  This was useful to generate different types of words to be used by the OCR model.
