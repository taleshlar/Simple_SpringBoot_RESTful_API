package ebi.ac.uk.date.service;

import org.joda.time.DateTime;

/**
 * Provides the current date.
 */
public interface DateService {

    /**
     * @return current date at the moment of the call
     */
    DateTime now();
}
