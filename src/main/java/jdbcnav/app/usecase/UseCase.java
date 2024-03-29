package jdbcnav.app.usecase;

import java.util.HashSet;
import java.util.Set;

/**
 * (c) Copyright 2006  Glamdring Incorporated Enterprises, Inc.  All rights reserved.

 */
public final class UseCase {
    private final String name;

    public UseCase(String id, String name) {
        this.name = name;
    }

    final Set<UseCase> parents = new HashSet<UseCase>();
    final Set<UseCase> includes = new HashSet<UseCase>();

    public String toString() {
        return "UseCase::" + name;
    }

    public String getName() {
        return name;
    }
}
