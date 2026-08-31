/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2025 Radix IoT LLC. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Radix IoT LLC,
 * the following extension to GPL is made. A special exception to the GPL is
 * included to allow you to distribute a combined work that includes BAcnet4J
 * without being obliged to provide the source code for any proprietary components.
 *
 * See www.radixiot.com for commercial license options.
 */

package com.serotonin.bacnet4j.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.util.sero.ByteQueue;

/**
 * Tests of the branch of {@link Encodable#readANY} that recognises a property value encoded as a lone NULL, which
 * addendum 135-2016br-2 allows in place of the property's declared datatype. A value is only a lone NULL when the
 * closing context tag immediately follows it; a constructed value that merely begins with a NULL, such as a priority
 * array whose first element is NULL, has to be decoded normally.
 */
public class EncodableNullPrimitiveBodyTest {
    /**
     * A property whose declared type is a leaf primitive, so that a NULL value takes the branch under test.
     */
    private static final PropertyIdentifier PRIMITIVE_PROPERTY = PropertyIdentifier.description;

    @Test
    public void loneNullDecodesAsNull() throws BACnetException {
        ByteQueue queue = wrap(Null.instance, 4);

        Encodable value = Encodable.readANY(queue, ObjectType.analogValue, PRIMITIVE_PROPERTY, null, 4);

        assertEquals(Null.class, value.getClass());
        assertEquals("the whole value should be consumed", 0, queue.size());
    }

    /**
     * The tag of a context id above 14 is two octets rather than one, so the NULL and the closing tag are found at
     * different offsets.
     */
    @Test
    public void loneNullWithExtendedTagDecodesAsNull() throws BACnetException {
        ByteQueue queue = wrap(Null.instance, 15);

        Encodable value = Encodable.readANY(queue, ObjectType.analogValue, PRIMITIVE_PROPERTY, null, 15);

        assertEquals(Null.class, value.getClass());
        assertEquals("the whole value should be consumed", 0, queue.size());
    }

    @Test
    public void priorityArrayWithNullFirstElementDecodesAsPriorityArray() throws BACnetException {
        PriorityArray array = new PriorityArray().put(8, new Real(12.3f));
        ByteQueue queue = wrap(array, 4);

        Encodable value = Encodable.readANY(queue, ObjectType.analogValue, PropertyIdentifier.priorityArray,
                null, 4);

        assertEquals(array, value);
        assertEquals("the whole value should be consumed", 0, queue.size());
    }

    @Test
    public void priorityArrayWithNullFirstElementAndExtendedTagDecodesAsPriorityArray() throws BACnetException {
        PriorityArray array = new PriorityArray().put(8, new Real(12.3f));
        ByteQueue queue = wrap(array, 15);

        Encodable value = Encodable.readANY(queue, ObjectType.analogValue, PropertyIdentifier.priorityArray,
                null, 15);

        assertEquals(array, value);
        assertEquals("the whole value should be consumed", 0, queue.size());
    }

    /**
     * An array of every element NULL is the state of a commandable object that has never been commanded. It is not a
     * lone NULL either.
     */
    @Test
    public void priorityArrayOfAllNullsDecodesAsPriorityArray() throws BACnetException {
        PriorityArray array = new PriorityArray();
        ByteQueue queue = wrap(array, 4);

        Encodable value = Encodable.readANY(queue, ObjectType.analogValue, PropertyIdentifier.priorityArray,
                null, 4);

        assertEquals(array, value);
        assertEquals("the whole value should be consumed", 0, queue.size());
    }

    /**
     * A message that ends part way through the closing tag must be reported as a BACnet error rather than throwing
     * out of the decoder as an unchecked exception.
     */
    @Test
    public void truncatedExtendedClosingTagIsReportedAsABACnetError() {
        ByteQueue queue = new ByteQueue();
        queue.push("4e"); // Opening tag, context id 4.
        queue.push("00"); // NULL.
        queue.push("ff"); // The first octet of an extended closing tag, with its tag number octet missing.

        assertThrows(BACnetException.class,
                () -> Encodable.readANY(queue, ObjectType.analogValue, PRIMITIVE_PROPERTY, null, 4));
    }

    @Test
    public void missingClosingTagIsReportedAsABACnetError() {
        ByteQueue queue = new ByteQueue();
        queue.push("4e"); // Opening tag, context id 4.
        queue.push("00"); // NULL, and then nothing at all.

        assertThrows(BACnetException.class,
                () -> Encodable.readANY(queue, ObjectType.analogValue, PRIMITIVE_PROPERTY, null, 4));
    }

    /**
     * Encode the value in its application form between the opening and closing context tags of the given context id,
     * which is how a property value is carried. Written here rather than with Encodable's own context write because
     * a primitive encodes itself with a single context tag instead.
     */
    private static ByteQueue wrap(Encodable value, int contextId) {
        ByteQueue queue = new ByteQueue();
        pushContextTag(queue, contextId, true);
        value.write(queue);
        pushContextTag(queue, contextId, false);
        return queue;
    }

    private static void pushContextTag(ByteQueue queue, int contextId, boolean start) {
        if (contextId <= 14) {
            queue.push(contextId << 4 | (start ? 0xe : 0xf));
        } else {
            queue.push(start ? 0xfe : 0xff);
            queue.push(contextId);
        }
    }
}
