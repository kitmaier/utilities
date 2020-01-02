# https://pythonprogramming.net/part-of-speech-tagging-nltk-tutorial/

import nltk
nltk.download('popular')

nltk.pos_tag(nltk.word_tokenize("Hello world"))

sentence = "Their companion, an aged professor"
pos_tagged_tokens = nltk.pos_tag(nltk.word_tokenize(sentence))
for (token,pos) in pos_tagged_tokens:
    if pos in ('VB','VBD','VBP','VBZ'):
        print(token,pos)


