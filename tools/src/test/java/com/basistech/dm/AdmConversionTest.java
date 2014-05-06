/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.dm;

import com.basistech.rlp.AbstractResultAccess;
import com.basistech.rlp.ResultAccessDeserializer;
import com.basistech.rlp.ResultAccessSerializedFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Here's some testing using an ARA as in input.
 */
public class AdmConversionTest extends Assert {
    private AbstractResultAccess deserialize(File file) throws IOException {
        ResultAccessDeserializer deserializer = new ResultAccessDeserializer();
        deserializer.setFormat(ResultAccessSerializedFormat.JSON);
        InputStream in = new FileInputStream(file);
        AbstractResultAccess results;
        try {
            results = deserializer.deserializeAbstractResultAccess(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return results;
    }

    private AbstractResultAccess deserialize(String s) throws IOException {
        ResultAccessDeserializer deserializer = new ResultAccessDeserializer();
        deserializer.setFormat(ResultAccessSerializedFormat.JSON);
        return deserializer.deserializeAbstractResultAccess(new ByteArrayInputStream(
            s.getBytes(Charsets.UTF_8)));
    }

    @Test
    public void testEngRoundTrip() throws Exception {
        AbstractResultAccess ara = deserialize(new File("../model/data/eng-ara.json"));
        Text text = AraDmConverter.convert(ara);

        ByteArrayOutputStream jsonContainer = new ByteArrayOutputStream();

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        writer.writeValue(jsonContainer, text);

        //System.out.println(jsonContainer.toString("utf-8"));

        ObjectReader reader = mapper.reader(Text.class);
        Text deserializedText = reader.readValue(jsonContainer.toByteArray());

        assertEquals(text.length(), deserializedText.length());
        assertEquals(text.getData(), deserializedText.getData());
        // we need about 1000 additional assertions in here.
    }

    @Test
    public void testAraRoundTrip() throws Exception {
        AbstractResultAccess ara = deserialize(new File("../model/data/ara-ara.json"));
        Text text = AraDmConverter.convert(ara);

        ByteArrayOutputStream jsonContainer = new ByteArrayOutputStream();

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        writer.writeValue(jsonContainer, text);

        //System.out.println(jsonContainer.toString("utf-8"));

        ObjectReader reader = mapper.reader(Text.class);
        Text deserializedText = reader.readValue(jsonContainer.toByteArray());

        assertEquals(text.length(), deserializedText.length());
        assertEquals(text.getData(), deserializedText.getData());
    }

    @Test
    public void testJpnRoundTrip() throws Exception {
        AbstractResultAccess ara = deserialize(new File("../model/data/jpn-ara.json"));

        Text text = AraDmConverter.convert(ara);

        ByteArrayOutputStream jsonContainer = new ByteArrayOutputStream();

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        writer.writeValue(jsonContainer, text);

        //System.out.println(jsonContainer.toString("utf-8"));

        ObjectReader reader = mapper.reader(Text.class);
        Text deserializedText = reader.readValue(jsonContainer.toByteArray());

        assertEquals(text.length(), deserializedText.length());
        assertEquals(text.getData(), deserializedText.getData());
    }

    @Ignore
    @Test
    public void testAraWithFragmentBoundaries() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/with-fragment-boundaries.json"));
        AraDmConverter.convert(ara);
    }

    @Ignore
    @Test
    public void testMissingDetectedLanguage() throws IOException {
        String json = "{'RawText':'Cambridge, MA',"
            + "'Tokens':['Cambridge', ',', 'MA'],"
            + "'TokenOffset':[0,9,9,10,11,13],"
            + "'Lemma':['Cambridge',',','MA'],"
            + "'NamedEntity':[0,3,196608],'NamedEntityChainId':[0],'NormalizedNamedEntity':['Cambridge, MA'],"
            + "'SentenceBoundaries':[3]"
            + "}";
        json = json.replace("'", "\"");
        AbstractResultAccess ara = deserialize(json);
        AraDmConverter.convert(ara);
    }

    // The following all come from ARA json files used in RES tests.
    // TODO: once the issues are examined, consider renaming the files and
    // tests, and adding appropriate asserts.  Currently they fail because
    // they throw exceptions.

    @Test
    public void testEmptyString() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/empty.json"));
        Text text = AraDmConverter.convert(ara);
        assertEquals(0, text.length());
        assertEquals(0, text.getTokens().getItems().size());
    }

    @Test
    public void testWhitespace() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/whitespace.json"));
        Text text = AraDmConverter.convert(ara);
        assertTrue(text.length() > 0);
        assertEquals(0, text.getTokens().getItems().size());
    }

    @Ignore
    @Test
    public void testBadAra0() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/adjacent-entity-context-left-overlap.json"));
        AraDmConverter.convert(ara);
    }

    @Ignore
    @Test
    public void testBadAra1() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/bad-and-good-names.json"));
        AraDmConverter.convert(ara);
    }

    @Ignore
    @Test
    public void testBadAra2() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/no-interesting-entities.json"));
        AraDmConverter.convert(ara);
    }

    @Ignore
    @Test
    public void testBadAra3() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/shimon-peres-ara.json"));
        AraDmConverter.convert(ara);
    }

    @Ignore
    @Test
    public void testBadAra4() throws IOException {
        AbstractResultAccess ara = deserialize(new File("../model/data/shimon-peres-zho.json"));
        AraDmConverter.convert(ara);
    }

    // TODO: test for missing zero-valued primitives, e.g. coreferenceChainId
    // startOffset = 0 is also missing from the json, but probably correct
    // from code.  But coreferenceChainId = 0 is wrong when chaining was
    // not run.  In general, I think I (joelb) am opposed to the jackson
    // trick that suppresses default values in the output.  I like to see
    // them explicitly.  At least in the cases I've seen so far.

    // TODO: test for missing RLI result; I expect lang, encoding, script for
    // the single best RLI result.

    // TODO: I think we should be storing token indexes in addition to char offsets
    // in the EntityMention.
}
