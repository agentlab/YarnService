/**
 *
 */
package ru.agentlab.yarn.ds.host;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

import ru.agentlab.yarn.IOomphService;
import ru.agentlab.yarn.model.IDObject;
import ru.agentlab.yarn.model.Synset;
import ru.agentlab.yarn.model.Word;
import ru.agentlab.yarn.model.WordReference;

/**
 *
 * Oomph component realisation.
 *
 * @author Prihodko
 *
 */
@Component(enabled = true, immediate = true,
    property = { "service.exported.interfaces=*", "service.exported.configs=ecf.jaxrs.jersey.server",
        "ecf.jaxrs.jersey.server.urlContext=http://localhost:8080", "ecf.jaxrs.jersey.server.alias=/oomph",
        "service.pid=com.bmstu.coursework.oomph.ds.host.OomphComponent" })
public class YarnService
    implements IOomphService, ManagedService {

    private List<Word> words;
    private List<Synset> synsets;
    private long wordMaxID;
    private long synsetMaxID;







    @Activate
    public void activate(ComponentContext context) throws IOException {
        words = new ArrayList<>();
        synsets = new ArrayList<>();
        readFile("yarn.xml");
        System.out.println("Oomph service started"); //$NON-NLS-1$
    }

    @Deactivate
    public void deactivate(ComponentContext context) {
        saveFile("yarn.xml");
        System.out.println("Oomph service stopped"); //$NON-NLS-1$
    }

    @Modified
    public void modify() {
        System.out.println("Oomph service modified"); //$NON-NLS-1$
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        // Does nothing
    }



    @Override
    public Word getWord(IDObject idObj) {
        return getWord(Long.parseLong(idObj.getId()));
    }

    protected Word getWord(long id) {
        Optional<Word> synsetOptional = words.stream().filter(t -> t.getId() == id).findAny();
        return synsetOptional.get();
    }

    @Override
    public boolean addWord(Word newWord) {
        Optional<Word> wordOptional = words.stream().filter(t -> t.getName().equals(newWord.getName())).findAny();
        try
        {
            wordOptional.get();
        }
        catch (NoSuchElementException e)
        {
            newWord.setId(++wordMaxID);
            words.add(newWord);
            return true;
        }
        return false;
    }

    @Override
    public boolean delWord(IDObject idObj) {
        return deleteWord(Long.parseLong(idObj.getId()));
    }

    protected boolean deleteWord(long id) {
        Optional<Word> deleteWord = words.stream().filter(t -> t.getId() == id).findAny();
        deleteWord.ifPresent(word -> {
            words.remove(word);
        });
        return deleteWord.isPresent();
    }

    @Override
    public Synset getSynset(IDObject idObj) {
        return getSynset(Long.parseLong(idObj.getId()));
    }

    protected Synset getSynset(long id) {
        Optional<Synset> synsetOptional = synsets.stream().filter(t -> t.getId() == id).findAny();
        return synsetOptional.get();
    }

    @Override
    public boolean addSynset(Synset synset) {
        List<WordReference> wordReferences = synset.getWords();
        for (WordReference w : wordReferences)
        {
            Word word = getWord(Long.parseLong(w.getWordIdObj().getId()));
            w.setWord(word);
        }
        synset.setId(++synsetMaxID);
        synsets.add(synset);
        return true;
    }

    @Override
    public boolean delSynset(IDObject idObj) {
        return deleteSynset(Long.parseLong(idObj.getId()));
    }

    protected boolean deleteSynset(long id) {
        Optional<Synset> deleteSynset = synsets.stream().filter(t -> t.getId() == id).findAny();
        deleteSynset.ifPresent(synset -> {
            synsets.remove(synset);
        });
        return deleteSynset.isPresent();
    }

    private void readFile(String filePath) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SAXBuilder saxBuilder = new SAXBuilder();
        File xmlFile = new File(filePath);
        try
        {
            Document document = saxBuilder.build(xmlFile);
            Element rootNode = document.getRootElement();
            List<Element> wordEntrys = rootNode.getChild("words").getChildren("wordEntry");
            for (Element wordEntry : wordEntrys)
            {
                long id = Integer.parseInt(wordEntry.getAttributeValue("id").substring(1));
                if (id > wordMaxID)
                {
                    wordMaxID = id;
                }
                String authorString = wordEntry.getAttributeValue("author");
                int author = (authorString.equals("") ? 0 : Integer.parseInt(authorString));
                int version = Integer.parseInt(wordEntry.getAttributeValue("version"));

                String timestampString = wordEntry.getAttributeValue("timestamp");
                Date timestamp = formatter.parse(timestampString);

                String word = wordEntry.getChildText("word");
                String url = wordEntry.getChildText("url");
                String grammar = wordEntry.getChildText("grammar");

                words.add(new Word(id, word, url, author, version, timestampString, grammar));
            }

            List<Element> synsetEntrys = rootNode.getChild("synsets").getChildren("synsetEntry");
            for (Element synsetEntry : synsetEntrys)
            {
                long id = Integer.parseInt(synsetEntry.getAttributeValue("id").substring(1));
                if (id > synsetMaxID)
                {
                    synsetMaxID = id;
                }
                String authorString = synsetEntry.getAttributeValue("author");
                int author = (authorString.equals("") ? 0 : Integer.parseInt(authorString));
                int version = Integer.parseInt(synsetEntry.getAttributeValue("version"));
                String timestampString = synsetEntry.getAttributeValue("timestamp");
                Date timestamp = formatter.parse(timestampString);

                List<WordReference> wordReferences = new ArrayList<>();

                List<Element> words = synsetEntry.getChildren("word");
                for (Element word : words)
                {
                    long wordID = -1;
                    try
                    {
                        wordID = Integer.parseInt(word.getAttributeValue("ref").substring(1));
                    }
                    catch (NullPointerException e)
                    {
                    }
                    List<String> definitions = new ArrayList<>();
                    List<Element> wordInnerElements = word.getChildren("definition");
                    for (Element e : wordInnerElements)
                    {
                        definitions.add(e.getText());
                    }

                    List<String> marks = new ArrayList<>();
                    wordInnerElements = word.getChildren("mark");
                    for (Element e : wordInnerElements)
                    {
                        marks.add(e.getText());
                    }

                    List<String> examples = new ArrayList<>();
                    wordInnerElements = word.getChildren("example");
                    for (Element e : wordInnerElements)
                    {
                        examples.add(e.getText());
                    }

                    Word wordVar = null;
                    try
                    {
                        wordVar = getWord(wordID);
                    }
                    catch (NoSuchElementException e)
                    {
                    }

                    if (wordVar != null)
                    {
                        WordReference wordRef = new WordReference(wordVar, definitions, marks, examples);
                        wordReferences.add(wordRef);
                    }
                }

                Synset synset = new Synset(id, author, version, timestampString, wordReferences);
                synsets.add(synset);
            }

            System.out.println("File was loaded");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void saveFile(String filePath) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Element yarn = new Element("yarn");
        Document document = new Document(yarn);
        Element wordsElem = new Element("words");
        yarn.addContent(wordsElem);
        for (Word word : words)
        {
            Element wordEntry = new Element("wordEntry");
            wordEntry.setAttribute("id", "w" + word.getId());
            wordEntry.setAttribute("author", word.getAuthor() + "");
            wordEntry.setAttribute("version", word.getVersion() + "");
            wordEntry.setAttribute("timestamp", formatter.format(word.getTimestamp()));
            wordEntry.addContent(new Element("word").setText(word.getName()));
            wordEntry.addContent(new Element("grammar").setText(word.getGrammar()));
            wordEntry.addContent(new Element("url").setText(word.getUrl()));
            wordsElem.addContent(wordEntry);
        }
        Element synsetsElem = new Element("synsets");
        yarn.addContent(synsetsElem);
        for (Synset synset : synsets)
        {
            Element synsetEntry = new Element("synsetEntry");
            synsetEntry.setAttribute("id", "s" + synset.getId());
            synsetEntry.setAttribute("author", synset.getAuthor() + "");
            synsetEntry.setAttribute("version", synset.getVersion() + "");
            synsetEntry.setAttribute("timestamp", formatter.format(synset.getTimestamp()));

            List<WordReference> wordRefs = synset.getWords();

            for (WordReference w : wordRefs)
            {
                Element wordRefEntry = new Element("word");
                if (w != null)
                {
                    wordRefEntry.setAttribute("ref", "w" + w.getWord().getId());
                }

                List<String> wordRefContent = w.getDefinitions();
                for (String def : wordRefContent)
                {
                    wordRefEntry.addContent(new Element("definition").setText(def));
                }

                wordRefContent = w.getExamples();
                for (String exs : wordRefContent)
                {
                    wordRefEntry.addContent(new Element("example").setText(exs));
                }

                wordRefContent = w.getMarks();
                for (String mark : wordRefContent)
                {
                    wordRefEntry.addContent(new Element("mark").setText(mark));
                }

                synsetEntry.addContent(wordRefEntry);
            }
            synsetsElem.addContent(synsetEntry);
        }
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getRawFormat());
        try
        {
            xmlOutputter.output(document, new FileOutputStream(filePath));
            System.out.println("File was saved");
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }
}
