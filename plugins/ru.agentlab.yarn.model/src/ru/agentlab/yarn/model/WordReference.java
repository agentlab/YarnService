/**
 *
 */
package ru.agentlab.yarn.model;

import java.util.List;

public class WordReference {
    private Word word;
    private IDObject wordIdObj;
    private List<String> definitions;
    private List<String> marks;
    private List<String> examples;

    public WordReference(){}

    public WordReference(IDObject wordIdObj, List<String> definitions, List<String> marks, List<String> examples) {
        this.word = null;
        this.wordIdObj = wordIdObj;
        this.definitions = definitions;
        this.marks = marks;
        this.examples = examples;
    }

    public WordReference(Word word, List<String> definitions, List<String> marks, List<String> examples) {
        this.word = word;
        this.definitions = definitions;
        this.marks = marks;
        this.examples = examples;
    }

    public IDObject getWordIdObj() {
        return wordIdObj;
    }

    public void setWordIdObj(IDObject wordIdObj) {
        this.wordIdObj = wordIdObj;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    public List<String> getMarks() {
        return marks;
    }

    public void setMarks(List<String> marks) {
        this.marks = marks;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }

    @Override
    public String toString() {
        String s="{"+word.toString()+'\n';
        s+="definitions:";
        for(int i=0;i<definitions.size();i++)
        {
            s+=(i==0 ? "" : ';')+definitions.get(i);
        }
        s+='\n'+"marks:";
        for(int i=0;i<marks.size();i++)
        {
            s+=(i==0 ? "" : ';')+marks.get(i);
        }
        s+='\n'+"examples:";
        for(int i=0;i<examples.size();i++)
        {
            s+=(i==0 ? "" : ';')+examples.get(i);
        }
        s+="}";
        return s;
    }
}
