package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ConfirmedEventNotificationRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 2;
    
    private UnsignedInteger processIdentifier; // 0
    private ObjectIdentifier initiatingDeviceIdentifier; // 1
    private ObjectIdentifier eventObjectIdentifier; // 2
    private TimeStamp timeStamp; // 3
    private UnsignedInteger notificationClass; // 4
    private UnsignedInteger priority; // 5
    private EventType eventType; // 6
    private CharacterString messageText; // 7 optional
    private NotifyType notifyType; // 8
    private Boolean ackRequired; // 9 optional
    private EventState fromState; // 10 optional
    private EventState toState; // 11
    private NotificationParameters eventValues; // 12 optional
    
    public ConfirmedEventNotificationRequest(UnsignedInteger processIdentifier, 
            ObjectIdentifier initiatingDeviceIdentifier, ObjectIdentifier eventObjectIdentifier, TimeStamp timeStamp, 
            UnsignedInteger notificationClass, UnsignedInteger priority, EventType eventType, 
            CharacterString messageText, NotifyType notifyType, Boolean ackRequired, EventState fromState, 
            EventState toState, NotificationParameters eventValues) {
        this.processIdentifier = processIdentifier;
        this.initiatingDeviceIdentifier = initiatingDeviceIdentifier;
        this.eventObjectIdentifier = eventObjectIdentifier;
        this.timeStamp = timeStamp;
        this.notificationClass = notificationClass;
        this.priority = priority;
        this.eventType = eventType;
        this.messageText = messageText;
        this.notifyType = notifyType;
        this.ackRequired = ackRequired;
        this.fromState = fromState;
        this.toState = toState;
        this.eventValues = eventValues;
    }
    
    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from) throws BACnetException {
        localDevice.getEventHandler().fireEventNotification(processIdentifier,
                localDevice.getRemoteDeviceCreate(initiatingDeviceIdentifier.getInstanceNumber(), from),
                eventObjectIdentifier, timeStamp, notificationClass, priority, eventType, messageText, notifyType,
                ackRequired, fromState, toState, eventValues);
        return null;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, processIdentifier, 0);
        write(queue, initiatingDeviceIdentifier, 1);
        write(queue, eventObjectIdentifier, 2);
        write(queue, timeStamp, 3);
        write(queue, notificationClass, 4);
        write(queue, priority, 5);
        write(queue, eventType, 6);
        writeOptional(queue, messageText, 7);
        write(queue, notifyType, 8);
        writeOptional(queue, ackRequired, 9);
        writeOptional(queue, fromState, 10);
        write(queue, toState, 11);
        writeOptional(queue, eventValues, 12);
    }
    
    ConfirmedEventNotificationRequest(ByteQueue queue) throws BACnetException {
        processIdentifier = read(queue, UnsignedInteger.class, 0);
        initiatingDeviceIdentifier = read(queue, ObjectIdentifier.class, 1);
        eventObjectIdentifier = read(queue, ObjectIdentifier.class, 2);
        timeStamp = read(queue, TimeStamp.class, 3);
        notificationClass = read(queue, UnsignedInteger.class, 4);
        priority = read(queue, UnsignedInteger.class, 5);
        eventType = read(queue, EventType.class, 6);
        messageText = readOptional(queue, CharacterString.class, 7);
        notifyType = read(queue, NotifyType.class, 8);
        ackRequired = readOptional(queue, Boolean.class, 9);
        fromState = readOptional(queue, EventState.class, 10);
        toState = read(queue, EventState.class, 11);
        eventValues = NotificationParameters.createNotificationParametersOptional(queue, 12);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((ackRequired == null) ? 0 : ackRequired.hashCode());
        result = PRIME * result + ((eventObjectIdentifier == null) ? 0 : eventObjectIdentifier.hashCode());
        result = PRIME * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = PRIME * result + ((eventValues == null) ? 0 : eventValues.hashCode());
        result = PRIME * result + ((fromState == null) ? 0 : fromState.hashCode());
        result = PRIME * result + ((initiatingDeviceIdentifier == null) ? 0 : initiatingDeviceIdentifier.hashCode());
        result = PRIME * result + ((messageText == null) ? 0 : messageText.hashCode());
        result = PRIME * result + ((notificationClass == null) ? 0 : notificationClass.hashCode());
        result = PRIME * result + ((notifyType == null) ? 0 : notifyType.hashCode());
        result = PRIME * result + ((priority == null) ? 0 : priority.hashCode());
        result = PRIME * result + ((processIdentifier == null) ? 0 : processIdentifier.hashCode());
        result = PRIME * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
        result = PRIME * result + ((toState == null) ? 0 : toState.hashCode());
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
        final ConfirmedEventNotificationRequest other = (ConfirmedEventNotificationRequest) obj;
        if (ackRequired == null) {
            if (other.ackRequired != null)
                return false;
        }
        else if (!ackRequired.equals(other.ackRequired))
            return false;
        if (eventObjectIdentifier == null) {
            if (other.eventObjectIdentifier != null)
                return false;
        }
        else if (!eventObjectIdentifier.equals(other.eventObjectIdentifier))
            return false;
        if (eventType == null) {
            if (other.eventType != null)
                return false;
        }
        else if (!eventType.equals(other.eventType))
            return false;
        if (eventValues == null) {
            if (other.eventValues != null)
                return false;
        }
        else if (!eventValues.equals(other.eventValues))
            return false;
        if (fromState == null) {
            if (other.fromState != null)
                return false;
        }
        else if (!fromState.equals(other.fromState))
            return false;
        if (initiatingDeviceIdentifier == null) {
            if (other.initiatingDeviceIdentifier != null)
                return false;
        }
        else if (!initiatingDeviceIdentifier.equals(other.initiatingDeviceIdentifier))
            return false;
        if (messageText == null) {
            if (other.messageText != null)
                return false;
        }
        else if (!messageText.equals(other.messageText))
            return false;
        if (notificationClass == null) {
            if (other.notificationClass != null)
                return false;
        }
        else if (!notificationClass.equals(other.notificationClass))
            return false;
        if (notifyType == null) {
            if (other.notifyType != null)
                return false;
        }
        else if (!notifyType.equals(other.notifyType))
            return false;
        if (priority == null) {
            if (other.priority != null)
                return false;
        }
        else if (!priority.equals(other.priority))
            return false;
        if (processIdentifier == null) {
            if (other.processIdentifier != null)
                return false;
        }
        else if (!processIdentifier.equals(other.processIdentifier))
            return false;
        if (timeStamp == null) {
            if (other.timeStamp != null)
                return false;
        }
        else if (!timeStamp.equals(other.timeStamp))
            return false;
        if (toState == null) {
            if (other.toState != null)
                return false;
        }
        else if (!toState.equals(other.toState))
            return false;
        return true;
    }
}
