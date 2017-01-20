package ru.agentlab.yarn.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Synset {
    private long id;
    private int author;
    private int version;
    private Date timestamp;
    private List<WordReference> wordReferences;

    public Synset() {
    }

    public Synset(long id, int author, int version, String timestamp, List<WordReference> wordReferences) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.id = id;
        this.author = author;
        this.version = version;
        this.timestamp = new Date();
        try
        {
            this.timestamp = formatter.parse(timestamp);
        }
        catch (ParseException e)
        {
        }
        this.wordReferences = wordReferences;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<WordReference> getWords() {
        return wordReferences;
    }

    public void setWords(List<WordReference> wordReferences) {
        this.wordReferences = wordReferences;
    }

    @Override
    public String toString() {
        String s = "Synset(id: " + id + "): [";
        for (WordReference w : wordReferences)
        {
            s += '\n' + w.toString();
        }
        s += "]";
        return s;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int)(id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Synset other = (Synset)obj;
        if (id != other.id)
        {
            return false;
        }
        return true;
    }
}