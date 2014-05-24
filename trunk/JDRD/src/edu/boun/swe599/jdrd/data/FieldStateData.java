/*
 *  Copyright ©2014 Canay ÖZEL (canay.ozel@gmail.com).
 */
package edu.boun.swe599.jdrd.data;

import edu.boun.swe599.jdrd.JDRDConfiguration;
import edu.boun.swe599.jdrd.util.JDRDUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

/**
 *
 * @author Canay ÖZEL (canay.ozel@gmail.com)
 * @version 1.0 created on May 11, 2014 9:04:35 PM
 */
public class FieldStateData {

    private final Set<Long> accessors;
    private Set<Object> lockSet;
    private FieldState state;

    public FieldStateData() {
        this.accessors = new HashSet<>();
        this.state = FieldState.VIRGIN;
    }

    public int getLockSetSize() {
        return this.lockSet != null ? this.lockSet.size() : -1;
    }

    public int getAccessorSetSize() {
        return this.accessors != null ? this.accessors.size() : -1;
    }

    public FieldState getState() {
        return state;
    }

    public void signalAccess(WeakHashMap<Object, ?> locks, boolean isWrite) {
        this.accessors.add(JDRDUtil.getCurrentThreadId());

        if (this.accessors.size() == 1) {
            this.state = FieldState.EXCLUSIVE;
        } else if (this.accessors.size() > 1) {
            if (isWrite) {
                this.state = FieldState.SHARED_MODIFIED;
            } else if (this.state == FieldState.EXCLUSIVE) {
                this.state = FieldState.SHARED;
            }
        }

        if (JDRDConfiguration.isFieldStateCheckEnabled() ? JDRDUtil.in(this.state, FieldState.SHARED, FieldState.SHARED_MODIFIED) : true) {
            // initialize lockset refinement
            if (this.lockSet == null) {
                this.lockSet = new HashMap<>(locks).keySet();
            } else {
                this.lockSet.retainAll(locks.keySet());
            }
        }
    }

}
