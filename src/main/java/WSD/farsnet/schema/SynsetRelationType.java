package WSD.farsnet.schema;

public enum SynsetRelationType {
    Hypernym,
    Hyponym,
    Instance_hypernym,
    Instance_hyponym,
    Part_holonym,
    Part_meronym,
    Member_holonym,
    Member_meronym,
    Substance_holonym,
    Substance_meronym,
    Portion_holonym,
    Portion_meronym,
    Domain,
    Is_Domain_of,
    Antonym,
    Is_Agent_of,
    Agent,
    Is_Caused_by,
    Cause,
    Is_Instrument_of,
    Instrument,
    Is_Entailed_by,
    Entailment,
    Location,
    Is_Location_of,
    Has_Salient_defining_feature,
    Salient_defining_feature,
    Is_Attribute_of,
    Attribute,
    Unit,
    Has_Unit,
    Related_to,
    Is_Patient_of,
    Patient;

    private SynsetRelationType() {
    }
}
