package jdbcnav.app.usecase;

import java.util.HashSet;
import java.util.Set;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public final class Actor {
    public Actor(String id, String name) {
        this.name = name;
    }

    public final String name;

    final Set parents = new HashSet();
    final Set<UseCase> usecases = new HashSet<UseCase>();

    public String toString() {
        return "Actor::" + name;
    }
}
