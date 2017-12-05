package me.cpearce.newsfeed.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Christopher on 11/11/2017.
 * Entity model
 */

public class Entity {
    public final String name;
    public final String type;
    public final Map<String, String> metadata = new HashMap<>();
    public final int salience;

    /**
     * api reference: https://cloud.google.com/natural-language/docs/reference/rest/v1/Entity
     * @param name Name of Entity
     * @param type Type of Entity possible options are UNKNOWN, PERSON, LOCATION, ORGANIZATION, EVENT
     *             WORK_OF_ART, CONSUMER_GOOD, OTHER
     * @param metadata map (key: string, value: string)
     *                 Metadata associated with the entity.
     *                 Currently, Wikipedia URLs and Knowledge Graph MIDs are provided, if available.
     *                 The associated keys are "wikipedia_url" and "mid", respectively.
     *                 An object containing a list of "key": value pairs.
     *                 Example: { "name": "wrench", "mass": "1.3kg", "count": "3" }.
     * @param salience The salience score associated with the entity in the [0, 1.0] range.
     */
    public Entity(String name, String type, Map<String, String> metadata, int salience) {
        this.name = name;
        this.type = type;
        this.metadata.putAll(metadata);
        this.salience = salience;
    }
}
