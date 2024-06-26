package com.meysam.logcollector.common.model.dtos.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum OutboxEventStatus {
    SENT,SENDING,UNSENT,RETRY_LIMIT_EXCEEDED,SENT_NOT_SYNCED;

    public static List<OutboxEventStatus> getAllValidStatusesForSending(){
        return new ArrayList<>(Arrays.asList(UNSENT));
    }

    public static List<OutboxEventStatus> getAllValidStatusesForSent(){
        return new ArrayList<>(Arrays.asList(SENDING,UNSENT));
    }

    public static List<OutboxEventStatus> getAllValidStatusesForRetryLimitExceed(){
        return new ArrayList<>(Arrays.asList(SENDING,UNSENT));
    }

    public static List<OutboxEventStatus> getAllValidStatusesForUnsent(){
        return new ArrayList<>(Arrays.asList(SENDING));
    }
    public static List<OutboxEventStatus> getAllValidStatusesForSentNotSynced(){
        return new ArrayList<>(Arrays.asList(SENT));
    }
}
