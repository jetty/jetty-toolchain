//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.setuid;

import com.sun.jna.Structure;

@Structure.FieldOrder({"_soft", "_hard"})
public class RLimit extends Structure
{
    public long _soft;
    public long _hard;


    public long getSoft ()
    {
        return _soft;
    }

    public void setSoft (long soft)
    {
        _soft = soft;
    }

    public long getHard ()
    {
        return _hard;
    }

    public void setHard (long hard)
    {
        _hard = hard;
    }

    public String toString()
    {
        return "rlimit_nofiles (soft="+_soft+", hard="+_hard+")";
    }

}
