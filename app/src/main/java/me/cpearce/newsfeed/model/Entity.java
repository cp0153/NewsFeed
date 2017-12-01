package me.cpearce.newsfeed.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Christopher on 11/11/2017.
 */

public class Entity {
    public final String name;
    public final String type;
    public final Map<String, String> metadata = new HashMap<>();
    public final int salience;

    public Entity(String name, String type, Map<String, String> metadata, int salience) {
        this.name = name;
        this.type = type;
        this.metadata.putAll(metadata);
        this.salience = salience;
    }

    public Map<String, String> getWikiUrl() {
        HashMap<String, String> hm = new HashMap<>();
        if (this.metadata.size() == 1) {

            hm.put(this.name, this.metadata.get("wikipedia_url"));
            return hm;
        }
        else {
            hm.put(this.name, "no wikipedia url");
            return hm;
        }
    }
}
