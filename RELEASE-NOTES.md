# Cumulative Release Notes for the Annotated Data Model

## 1.12.0

### [COMN-198](https://basistech.atlassian.net/browse/COMN-198) Add  Relationship Support
    
This also includes a move to basis parent 57.0.0, and so a requirement
for Java 1.7.

### [COMN-189](https://basistech.atlassian.net/browse/COMN-189) Add ComposingAnnotator
    
`com.basistech.rosette.dm.util.ComposingAnnotator` provides a function
needed in RBL-JVM, which is to group a sequence of annotators into a
single annotator.

### [COMN-186](https://basistech.atlassian.net/browse/COMN-186) Optimize copying attributes

There were too many copies of the map behind extendedProperties, which
exist on every attribute, even if empty.  This showed up in an ADM
application that did a lot of Token copies.

### [COMN-183](http://jira.basistech.net/browse/COMN-183) Rename setEndOffset

`Attribute.Builder.setEndOffset()` was deprecated.  Use the new method
`Attribute.Builder.endOffset()` instead.

### [COMN-190](http://jira.basistech.net/browse/COMN-190) ADM builders can reset lists

Attribute builders that hold lists now have a setter for the entire
list, not just the ability to add new elements.  For example, in
`Token.Builder`:

```
  public Builder addAnalysis(MorphoAnalysis analysis);
  public Builder analyses(List<MorphoAnalysis> analyses);  // new
```

Calling the list setter with an empty list or null results in an empty
list in the Builder.

## 1.11.2

### [COMN-180](http://jira.basistech.net/browse/COMN-180) Remove guava dependency

adm-model-osgi no longer externally depend on guava; adm-shaded no
longer has extra copies of Guava classes in it.

## 1.11.1

In support of [INDOC-11](http://jira.basistech.net/browse/INDOC-11),
added com.basistech.dm.internal to the exported package list so that
indoc can get to TextWrapper. Not wonderfully pretty, and not the end
of the world.

## 1.11.0

### [COMN-167](http://jira.basistech.net/browse/COMN-167) Restructure OSGi more

The Jackson customizations move out of the com.basistech.dm package,
into com.basistech.dm.jackson and com.basistech.dm.jackson.array.  The
com.basistech:adm-osgi artifact was split into
com.basistech:adm-model-osgi and com.basistech:adm-json-osgi.

Moving the Jackson classes to to their own OSGi bundle allows an OSGi
application to use the Jackson code _inside_ the OSGi environment and
still use the main data model to communicate between the inside and
the outside.

Note that the package movement means that this has to fan out to
projects that are using the json support today.

## 1.10.2

### [COMN-163](http://jira.basistech.net/browse/COMN-163) Restructure OSGi
    
All the OSGi metadata moves to a new artifact, adm-osgi. This avoids
'split package' issues with multiple OSGi components with code in the
same package.

### [COMN-158](http://jira.basistech.net/browse/COMN-158) Support array "shape" json

The new adm-json-array artifact supports serialization of ADM as
array-shaped json.  There is some space savings and some possible
runtime savings, however components should never store this format.
It's strictly for use "over the wire" - intended for RaaS.

To use normal ADM "object" shape serialization, do this:

    ObjectMapper mapper = AnnotatedDataModelModule.setupObjectMapper(new ObjectMapper());

To use the new "array" shape serialization, do this:

    ObjectMapper mapper = AnnotatedDataModelArrayModule.setupObjectMapper(new ObjectMapper());

## 1.10.1

### [COMN-142](http://jira.basistech.net/browse/COMN-142) Boxed Integer/Double consistency

ResolvedEntity coreferenceChainId was changed from int to Integer.
ResolvedEntity confidence was changed from double to Double.

Clients may have to adjust code on ADM boundaries if they are dealing
with -1 chainIds, and they may need to add checks for null.

### [COMN-140](http://jira.basistech.net/browse/COMN-140) AraDmConverter - separate api/cli

AraDmConverter#main has been moved to a separate class [AraDmConverterCommand](https://git.basistech.net/textanalytics/annotated-data-model/blob/6e469a572bdf3991280c11fee1c5ace204aa6d0b/tools/src/main/java/com/basistech/rosette/dm/tools/AraDmConverterCommand.java).  This avoids the need for users of the AraDmConverter api
to depend on args4j.

### [COMN-137](http://jira.basistech.net/browse/COMN-137) Name translation api

Added the
[Name](https://git.basistech.net/textanalytics/annotated-data-model/blob/07802b864ac9ba79419695e1a3bd1b45734eb0b1/model/src/main/java/com/basistech/rosette/dm/Name.java)
class, which is a stripped down version of the RNI Name.  The intent
is to allow RNT functionality via the ADM.

### [COMN-61](http://jira.basistech.net/browse/COMN-61) Add map api for metadata

Added
[documentMetadata](https://git.basistech.net/textanalytics/annotated-data-model/blob/fa0d46f034aec71296f2fc455b412e06b09ceb92/model/src/main/java/com/basistech/rosette/dm/AnnotatedText.java#L502)
convenience method to build metadata from a map.

### [COMN-73](http://jira.basistech.net/browse/COMN-73) Lang code serialization
### [COMN-76](http://jira.basistech.net/browse/COMN-76) Enum map key serialzaiton

These allow serialization from ISO codes rather than the enum string,
e.g. "eng" instead of "ENGLISH".

### [COMN-147](http://jira.basistech.net/browse/COMN-147) Dispatching Annotator 

`com.basistech.rosette.dm.util.WholeDocumentLanguageDispatchAnnotatorBuilder`
creates `Annotator` objects that delegate based on language. This
permits an application to build a family of annotators, one per
language, and then aggregate them into a single annotator. It is a DRY
feature; it's not complex, it's just that we don't want to end up with
multiple copies of this in multiple places.

### [COMN-151](http://jira.basistech.net/browse/COMN-151) New Parent/New Common 
   
Pick up rosette-common-java 34.0.0, with new jar structure, via
textanalytics 56.3.1.

### [COMN-133](http://jira.basistech.net/browse/COMN-133) Support unordered json attributes

json attributes are unordered per the spec, so tools are allowed to
shuffle map keys.  This change allows deserialization from shuffled
keys.

### [COMN-149](http://jira.basistech.net/browse/COMN-149) Remove Guava dependency

Guava is now shaded inside adm-model.

### [COMN-152](http://jira.basistech.net/browse/COMN-152) OSGi metadata

The adm-model jar file now has OSGi metadata.  We intend to use this
for a classpath isolation solution for RaaS.

### [COMN-92](http://jira.basistech.net/browse/COMN-92) Empty analysis fix

Fixes serialization of an empty analysis.