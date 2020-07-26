package com.orfangenes.db.orfanbasedb.util;

/**
 * @author Suresh Hewapathirana
 */
public class Constants {

    public enum TRGS {

        MULTI_DOMAIN_GENE("multi-domain gene"),
        DOMAIN_RESTRICTED_GENE("domain restricted gene"),
        KINGDOM_RESTRICTED_GENE("kingdom restricted gene"),
        PHYLUM_RESTRICTED_GENE("phylum restricted gene"),
        CLASS_RESTRICTED_GENE("class restricted gene"),
        ORDER_RESTRICTED_GENE("order restricted gene"),
        FAMILY_RESTRICTED_GENE("family restricted gene"),
        GENUS_RESTRICTED_GENE("genus restricted gene"),
        ORFAN_GENE("ORFan Gene"),
        STRICT_ORFAN("Strict ORFan");

        private String name;

        TRGS(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}

