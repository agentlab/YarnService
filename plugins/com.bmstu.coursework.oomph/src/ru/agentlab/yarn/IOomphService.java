/**
 *
 */
package ru.agentlab.yarn;

import ru.agentlab.yarn.model.IDObject;
import ru.agentlab.yarn.model.Synset;
import ru.agentlab.yarn.model.Word;

public interface IOomphService {

    Word getWord(IDObject idObj);

    boolean addWord(Word word);

    boolean delWord(IDObject idObj);

    boolean addSynset(Synset synset);

    Synset getSynset(IDObject idObj);

    boolean delSynset(IDObject idObj);
}
