package ru.agentlab.yarn.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Word {
    private long id;
    private String name;
    private String url;
    private int author;
    private int version;
    private Date timestamp;
    private String grammar;

    public Word() {
    }

    public Word(long id, String name, String url, int author, int version, String timestampstr, String grammar) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.id = id;
        this.name = name;
        this.author = author;
        this.version = version;
        this.grammar = grammar;
        this.timestamp = new Date();
        try{this.timestamp = formatter.parse(timestampstr);}
        catch(ParseException e){}
        this.url = (url == null ? "" : url);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getGrammar() {
        return grammar;
    }

    public void setGrammar(String grammar) {
        this.grammar = grammar;
    }

    @Override
    public String toString() {
        return "Word [id=" + id + ", name=" + name + ", url=" + url + "]";
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
        Word other = (Word)obj;
        if (id != other.id)
        {
            return false;
        }
        return true;
    }
}
