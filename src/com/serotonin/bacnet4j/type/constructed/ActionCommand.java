package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ActionCommand extends BaseType {
    private ObjectIdentifier deviceIdentifier;
    private ObjectIdentifier objectIdentifier;
    private PropertyIdentifier propertyIdentifier;
    private UnsignedInteger propertyArrayIndex;
    private Encodable propertyValue;
    private UnsignedInteger priority;
    private UnsignedInteger postDelay;
    private Boolean quitOnFailure;
    private Boolean writeSuccessful;
    
    public ActionCommand(ObjectIdentifier deviceIdentifier, ObjectIdentifier objectIdentifier, 
            PropertyIdentifier propertyIdentifier, UnsignedInteger propertyArrayIndex, Encodable propertyValue, 
            UnsignedInteger priority, UnsignedInteger postDelay, Boolean quitOnFailure, Boolean writeSuccessful) {
        this.deviceIdentifier = deviceIdentifier;
        this.objectIdentifier = objectIdentifier;
        this.propertyIdentifier = propertyIdentifier;
        this.propertyArrayIndex = propertyArrayIndex;
        this.propertyValue = propertyValue;
        this.priority = priority;
        this.postDelay = postDelay;
        this.quitOnFailure = quitOnFailure;
        this.writeSuccessful = writeSuccessful;
    }

    public void write(ByteQueue queue) {
        writeOptional(queue, deviceIdentifier, 0);
        write(queue, objectIdentifier, 1);
        write(queue, propertyIdentifier, 2);
        writeOptional(queue, propertyArrayIndex, 3);
        write(queue, propertyValue, 4);
        writeOptional(queue, priority, 5);
        writeOptional(queue, postDelay, 6);
        write(queue, quitOnFailure, 7);
        write(queue, writeSuccessful, 8);
    }
    
    public ActionCommand(ByteQueue queue) throws BACnetException {
        deviceIdentifier = readOptional(queue, ObjectIdentifier.class, 0);
        objectIdentifier = read(queue, ObjectIdentifier.class, 1);
        propertyIdentifier = read(queue, PropertyIdentifier.class, 2);
        propertyArrayIndex = readOptional(queue, UnsignedInteger.class, 3);
        propertyValue = readEncodable(queue, objectIdentifier.getObjectType(), propertyIdentifier, 4);
        priority = readOptional(queue, UnsignedInteger.class, 5);
        postDelay = readOptional(queue, UnsignedInteger.class, 6);
        quitOnFailure = read(queue, Boolean.class, 7);
        writeSuccessful = read(queue, Boolean.class, 8);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((deviceIdentifier == null) ? 0 : deviceIdentifier.hashCode());
        result = PRIME * result + ((objectIdentifier == null) ? 0 : objectIdentifier.hashCode());
        result = PRIME * result + ((postDelay == null) ? 0 : postDelay.hashCode());
        result = PRIME * result + ((priority == null) ? 0 : priority.hashCode());
        result = PRIME * result + ((propertyArrayIndex == null) ? 0 : propertyArrayIndex.hashCode());
        result = PRIME * result + ((propertyIdentifier == null) ? 0 : propertyIdentifier.hashCode());
        result = PRIME * result + ((propertyValue == null) ? 0 : propertyValue.hashCode());
        result = PRIME * result + ((quitOnFailure == null) ? 0 : quitOnFailure.hashCode());
        result = PRIME * result + ((writeSuccessful == null) ? 0 : writeSuccessful.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ActionCommand other = (ActionCommand) obj;
        if (deviceIdentifier == null) {
            if (other.deviceIdentifier != null)
                return false;
        }
        else if (!deviceIdentifier.equals(other.deviceIdentifier))
            return false;
        if (objectIdentifier == null) {
            if (other.objectIdentifier != null)
                return false;
        }
        else if (!objectIdentifier.equals(other.objectIdentifier))
            return false;
        if (postDelay == null) {
            if (other.postDelay != null)
                return false;
        }
        else if (!postDelay.equals(other.postDelay))
            return false;
        if (priority == null) {
            if (other.priority != null)
                return false;
        }
        else if (!priority.equals(other.priority))
            return false;
        if (propertyArrayIndex == null) {
            if (other.propertyArrayIndex != null)
                return false;
        }
        else if (!propertyArrayIndex.equals(other.propertyArrayIndex))
            return false;
        if (propertyIdentifier == null) {
            if (other.propertyIdentifier != null)
                return false;
        }
        else if (!propertyIdentifier.equals(other.propertyIdentifier))
            return false;
        if (propertyValue == null) {
            if (other.propertyValue != null)
                return false;
        }
        else if (!propertyValue.equals(other.propertyValue))
            return false;
        if (quitOnFailure == null) {
            if (other.quitOnFailure != null)
                return false;
        }
        else if (!quitOnFailure.equals(other.quitOnFailure))
            return false;
        if (writeSuccessful == null) {
            if (other.writeSuccessful != null)
                return false;
        }
        else if (!writeSuccessful.equals(other.writeSuccessful))
            return false;
        return true;
    }
}
